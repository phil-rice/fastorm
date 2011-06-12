package org.fastorm.reader.impl;

import org.fastorm.api.IFastOrmContainer;
import org.fastorm.context.IContext;
import org.fastorm.dataSet.IMutableDataSet;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.defns.IMakerAndEntityDefnVisitor;
import org.fastorm.memory.IMemoryManager;
import org.fastorm.reader.IEntityReaderThin;
import org.fastorm.temp.IPrimaryTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.utilities.callbacks.ICallback;

public class EntityReaderThin implements IEntityReaderThin {

	@Override
	public IMutableDataSet readPage(final int page, final IContext context) {
		final IFastOrmContainer fastOrm = context.getFastOrm();
		IMemoryManager memoryManager = fastOrm.getMemoryManager();
		memoryManager.dispose();

		ICallback<Long> total = new ICallback<Long>() {
			@Override
			public void process(Long t) throws Exception {
				fastOrm.getSqlLogger().total(t);
			}
		};
		if (page == 0)
			IEntityDefn.Utils.walk(fastOrm, new CreateTempTables(context));
		IEntityDefn.Utils.walkAndTime(fastOrm, new TruncateTempTables(context), total);
		IEntityDefn.Utils.walkAndTime(fastOrm, new PopulateTempTables(context, page), total);
		IEntityDefn.Utils.walkAndTime(fastOrm, new DrainTables(context), total);
		IMutableDataSet dataSet = context.buildDataSet();
		return dataSet;
	}
}

class CreateTempTables implements IMakerAndEntityDefnVisitor {
	private final IContext readContext;

	public CreateTempTables(IContext context) {
		this.readContext = context;
	}

	@Override
	public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
		maker.drop(readContext);
		maker.create(readContext);
	}

	@Override
	public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
		maker.drop(readContext, child);
		maker.create(readContext, parent, child);
	}
}

class PopulateTempTables implements IMakerAndEntityDefnVisitor {
	private final IContext readContext;
	private final int page;

	public PopulateTempTables(IContext context, int page) {
		this.readContext = context;
		this.page = page;
	}

	@Override
	public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
		maker.populate(readContext, page);
	}

	@Override
	public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
		maker.populate(readContext, parent, child);

	}
}

class DrainTables implements IMakerAndEntityDefnVisitor {

	private final IContext readContext;

	public DrainTables(IContext readContext) {
		super();
		this.readContext = readContext;
	}

	@Override
	public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
		maker.drain(readContext);
	}

	@Override
	public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
		maker.drain(readContext, parent, child);
	}
}

class TruncateTempTables implements IMakerAndEntityDefnVisitor {
	private final IContext readContext;

	public TruncateTempTables(IContext context) {
		super();
		this.readContext = context;
	}

	@Override
	public void acceptPrimary(IPrimaryTempTableMaker maker, IEntityDefn primary) {
		maker.truncate(readContext);
	}

	@Override
	public void acceptChild(ISecondaryTempTableMaker maker, IEntityDefn parent, IEntityDefn child) {
		maker.truncate(readContext, parent, child);

	}
}