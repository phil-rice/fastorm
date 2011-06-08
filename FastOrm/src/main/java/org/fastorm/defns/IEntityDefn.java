package org.fastorm.defns;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.fastorm.api.IJob;
import org.fastorm.api.IFastOrmContainer;
import org.fastorm.dataGenerator.IExtraDataGenerator;
import org.fastorm.dataGenerator.NoExtraDataGenerator;
import org.fastorm.defns.impl.MapToEntityDefn;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.temp.ITempTableMakerFactory;
import org.fastorm.temp.impl.SqlHelper;
import org.fastorm.utilities.aggregators.IAggregator;
import org.fastorm.utilities.callbacks.ICallback;
import org.fastorm.utilities.collections.Files;
import org.fastorm.utilities.exceptions.WrappedException;
import org.fastorm.utilities.functions.IFoldFunction;
import org.fastorm.utilities.maps.Maps;
import org.fastorm.xmlToMap.XmlToMapParser;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public interface IEntityDefn {
	Map<String, String> parameters();

	String getEntityName();

	String getTableName();

	String getTempTableName();

	String getIdColumn();

	String getIdType();

	/** Will return null on the primary */
	ISecondaryTempTableMaker getMaker();

	List<IEntityDefn> getChildren();

	static class Utils {
		public static XmlToMapParser<String, Object> makeXmlParser() {
			return makeParser(new ClassPathResource("Defns.xml"));
		}

		public static void dropAndMakeTables(IFastOrmContainer fastOrm, IEntityDefn entityDefn) {
			dropAndMakeTables(fastOrm, entityDefn, new NoExtraDataGenerator());
		}

		public static int countOfSelfAndDescendents(IEntityDefn defn) {
			final AtomicInteger result = new AtomicInteger();
			walk(defn, new IEntityDefnParentChildVisitor() {
				@Override
				public void acceptPrimary(IEntityDefn primary) throws Exception {
					result.incrementAndGet();
				}

				@Override
				public void acceptChild(IEntityDefn parent, IEntityDefn child) throws Exception {
					result.incrementAndGet();
				}
			});
			return result.get();
		}

		public static void dropAndMakeTables(IFastOrmContainer fastOrm, IEntityDefn entityDefn, final IExtraDataGenerator extraDataGenerator) {
			final Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes = findMinimumColumns(fastOrm);
			final SqlHelper sqlHelper = new SqlHelper(fastOrm.getJdbcTemplate());
			sqlHelper.dropAllTables();
			walk(fastOrm.getEntityDefn(), new IEntityDefnParentChildVisitor() {
				@Override
				public void acceptPrimary(IEntityDefn primary) throws Exception {
					extraDataGenerator.enrichColumnsForMakingTables(entityToColumnsAndTypes, primary);
				}

				@Override
				public void acceptChild(IEntityDefn parent, IEntityDefn child) throws Exception {
					extraDataGenerator.enrichColumnsForMakingTables(entityToColumnsAndTypes, child);
				}

			});
			walk(fastOrm.getEntityDefn(), new IEntityDefnParentChildVisitor() {
				@Override
				public void acceptPrimary(IEntityDefn primary) {
					sqlHelper.create(primary.getTableName(), Maps.toArray(entityToColumnsAndTypes.get(primary), new String[0]));
					sqlHelper.index(primary);

				}

				@Override
				public void acceptChild(IEntityDefn parent, IEntityDefn child) {
					sqlHelper.create(child.getTableName(), Maps.toArray(entityToColumnsAndTypes.get(child), new String[0]));
					sqlHelper.index(child);

				}

			});
		}

		public static Map<IEntityDefn, Map<String, String>> findMinimumColumns(IFastOrmContainer fastOrm) {
			final Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes = Maps.newMap();
			walk(fastOrm, new IMakerAndEntityDefnVisitor() {

				@Override
				public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
					maker.enrichColumnsForMakingTables(entityToColumnsAndTypes, primary);
				}

				@Override
				public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
					maker.enrichColumnsForMakingTables(entityToColumnsAndTypes, parent, child);
				}
			});
			return entityToColumnsAndTypes;
		}

		@SuppressWarnings("unchecked")
		public static XmlToMapParser<String, Object> makeParser(Resource resource) {
			XmlBeanFactory beanFactory = new XmlBeanFactory(resource);
			return beanFactory.getBean(XmlToMapParser.class);
		}

		public static <From, To> To fold(IEntityDefn primary, To initial, IEntityDefnFoldVisitor<From, To> visitor, IFoldFunction<From, To> foldFunction) {
			To accumulator = foldFunction.apply(visitor.acceptPrimary(primary), initial);
			return visitChildren(primary, accumulator, visitor, foldFunction);
		}

		private static <To, From> To visitChildren(IEntityDefn parent, To initial, IEntityDefnFoldVisitor<From, To> visitor, IFoldFunction<From, To> foldFunction) {
			To accumulator = initial;
			for (IEntityDefn child : parent.getChildren()) {
				accumulator = foldFunction.apply(visitor.acceptChild(parent, child), accumulator);
				accumulator = visitChildren(parent, accumulator, visitor, foldFunction);
			}
			return accumulator;
		}

		public static <From, To> To aggregate(IEntityDefn primary, IEntityDefnFoldVisitor<From, To> visitor, IAggregator<From, To> aggregator) {
			aggregator.add(visitor.acceptPrimary(primary));
			visitChildren(primary, visitor, aggregator);
			return aggregator.result();
		}

		private static <To, From> void visitChildren(IEntityDefn parent, IEntityDefnFoldVisitor<From, To> visitor, IAggregator<From, To> aggregator) {
			for (IEntityDefn child : parent.getChildren()) {
				aggregator.add(visitor.acceptChild(parent, child));
				visitChildren(child, visitor, aggregator);
			}
		}

		public static <From, To> To aggregateAndTime(IFastOrmContainer fastOrm, IMakerAndEntityDefnFoldVisitor<From, To> visitor, IAggregator<From, To> aggregator, ICallback<Long> timeCallback) {
			long startTime = System.nanoTime();
			try {
				To result = aggregate(fastOrm, visitor, aggregator);
				timeCallback.process(System.nanoTime() - startTime);
				return result;
			} catch (Exception e) {
				throw WrappedException.wrap(e);
			}

		}

		public static <From, To> To aggregate(IFastOrmContainer fastOrm, IMakerAndEntityDefnFoldVisitor<From, To> visitor, IAggregator<From, To> aggregator) {
			aggregator.add(visitor.acceptPrimary(fastOrm.getPrimaryTempTableMaker(), fastOrm.getEntityDefn()));
			visitChildren(fastOrm, fastOrm.getEntityDefn(), visitor, aggregator);
			return aggregator.result();
		}

		private static <To, From> void visitChildren(IFastOrmContainer fastOrm, IEntityDefn parent, IMakerAndEntityDefnFoldVisitor<From, To> visitor, IAggregator<From, To> aggregator) {
			for (IEntityDefn child : parent.getChildren()) {
				aggregator.add(visitor.acceptChild(fastOrm.getTempTableMakerFactory().findReaderMakerFor(parent.parameters(), child.parameters()), parent, child));
				visitChildren(fastOrm, child, visitor, aggregator);
			}
		}

		public static void walk(IEntityDefn entityDefn, IEntityDefnVisitor visitor) {
			try {
				visitor.accept(entityDefn);
				visitChildren(entityDefn, visitor);
			} catch (Exception e) {
				throw WrappedException.wrap(e);
			}
		}

		private static void visitChildren(IEntityDefn parent, IEntityDefnVisitor visitor) throws Exception {
			for (IEntityDefn child : parent.getChildren()) {
				visitor.accept(child);
				visitChildren(child, visitor);
			}
		}

		public static void walk(IEntityDefn primary, IEntityDefnParentChildVisitor visitor) {
			try {
				visitor.acceptPrimary(primary);
				visitChildren(primary, visitor);
			} catch (Exception e) {
				throw WrappedException.wrap(e);
			}
		}

		private static void visitChildren(IEntityDefn parent, IEntityDefnParentChildVisitor visitor) throws Exception {
			for (IEntityDefn child : parent.getChildren()) {
				visitor.acceptChild(parent, child);
				visitChildren(child, visitor);
			}
		}

		public static void walk(IFastOrmContainer fastOrm, IMakerAndEntityDefnVisitor visitor) {
			try {
				IEntityDefn primary = fastOrm.getEntityDefn();
				visitor.acceptPrimary(fastOrm.getPrimaryTempTableMaker(), primary);
				visitChildren(fastOrm, primary, visitor);
			} catch (Exception e) {
				throw WrappedException.wrap(e);
			}
		}

		public static void walk(IJob job, IMutableMakerAndEntityDefnVisitor visitor) {
			try {
				IEntityDefn primary = job.getEntityDefn();
				IFastOrmContainer container = job.getContainer();
				visitor.accept(container.getTempTableMakerFactory().findMutatingMakerFor(primary), primary);
				visitChildren(container, primary, visitor);
			} catch (Exception e) {
				throw WrappedException.wrap(e);
			}
		}

		private static void visitChildren(IFastOrmContainer fastOrm, IEntityDefn parent, IMutableMakerAndEntityDefnVisitor visitor) throws Exception {
			for (IEntityDefn child : parent.getChildren()) {
				visitor.accept(fastOrm.getTempTableMakerFactory().findMutatingMakerFor(child), child);
				visitChildren(fastOrm, child, visitor);
			}
		}

		public static void walkAndTime(IFastOrmContainer fastOrm, IMakerAndEntityDefnVisitor visitor, ICallback<Long> timeCallback) {
			long startTime = System.nanoTime();
			try {
				walk(fastOrm, visitor);
				timeCallback.process(System.nanoTime() - startTime);
			} catch (Exception e) {
				throw WrappedException.wrap(e);
			}
		}

		private static void visitChildren(IFastOrmContainer fastOrm, IEntityDefn parent, IMakerAndEntityDefnVisitor visitor) throws Exception {
			for (IEntityDefn child : parent.getChildren()) {
				visitor.acceptChild(fastOrm.getTempTableMakerFactory().findReaderMakerFor(parent.parameters(), child.parameters()), parent, child);
				visitChildren(fastOrm, child, visitor);
			}
		}

		public static IEntityDefn parse(ITempTableMakerFactory factory, Resource resource) {
			try {
				XmlToMapParser<String, Object> parser = makeXmlParser();
				String xml = Files.getText(resource);
				Map<String, Object> map = parser.parse(xml);
				return new MapToEntityDefn().create(factory, null, map);
			} catch (Exception e) {
				throw WrappedException.wrap(e);
			}
		}

	}

}
