package org.fastorm.dataGenerator;

import java.util.Map;
import java.util.Map.Entry;

import org.fastorm.defns.IEntityDefn;
import org.fastorm.utilities.collections.Files;
import org.fastorm.utilities.maps.Maps;
import org.fastorm.xmlItemStrategies.SimpleStringItemStrategy;
import org.fastorm.xmlToMap.AllowElementLikeMeUnrecognisedStrategy;
import org.fastorm.xmlToMap.IStrategyNode;
import org.fastorm.xmlToMap.XmlToMapParser;
import org.fastorm.xmlToMap.XmlToMapStrategy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ExtraDataContributor implements IExtraDataGenerator {
	private final Map<String, Object> extraDataSpec;

	public ExtraDataContributor() {
		this(new ClassPathResource("extraData.xml"));
	}

	public ExtraDataContributor(Resource extraDataResource) {
		Map<String, IStrategyNode<String, Object>> idToStrategy = Maps.makeMap(//
				"tableName", new SimpleStringItemStrategy(),//
				"value", new SimpleStringItemStrategy(),//
				"type", new SimpleStringItemStrategy()//
				);
		XmlToMapStrategy<String, Object> xmlToMapStrategy = new XmlToMapStrategy<String, Object>(idToStrategy, new AllowElementLikeMeUnrecognisedStrategy<String, Object>());
		XmlToMapParser<String, Object> parser = new XmlToMapParser<String, Object>(xmlToMapStrategy);
		String xml = Files.getText(extraDataResource);
		extraDataSpec = parser.parse(xml);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void enrichValues(Map<IEntityDefn, Map<String, IGenerator>> entityToColumnsToRowGenerator, IEntityDefn entityDefn) {
		String entityName = entityDefn.getEntityName();
		Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) extraDataSpec.get(entityName);
		for (Entry<String, Map<String, String>> entry : map.entrySet()) {
			if (entry.getKey().equals("tableName"))
				continue;
			String name = entry.getKey();
			Map<String, String> fieldMap = entry.getValue();
			String type = fieldMap.get("type");
			if (type.equals("integer"))
				Maps.addToMapOfLinkedMaps(entityToColumnsToRowGenerator, entityDefn, name, new SizeIntegerGenerator(name));
			else if (type.startsWith("varchar")) {
				String value = fieldMap.get("value");
				StringGenerator stringGenerator = new StringGenerator(name);
				stringGenerator.setPattern(value);
				Maps.addToMapOfLinkedMaps(entityToColumnsToRowGenerator, entityDefn, name, stringGenerator);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void enrichColumnsForMakingTables(Map<IEntityDefn, Map<String, String>> entityToColumnsAndTypes, IEntityDefn entityDefn) {
		String entityName = entityDefn.getEntityName();
		Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) extraDataSpec.get(entityName);
		for (Entry<String, Map<String, String>> entry : map.entrySet()) {
			String name = entry.getKey();
			if (name.equals("tableName"))
				continue;
			Map<String, String> fieldMap = entry.getValue();
			String type = fieldMap.get("type");
			Maps.addToMapOfLinkedMaps(entityToColumnsAndTypes, entityDefn, name, type);
		}
	}
}
