package org.fastorm.defns;

import org.fastorm.temp.IMutatingTempTableMaker;

public interface IMutableMakerAndEntityDefnVisitor {
	void accept(IMutatingTempTableMaker maker, IEntityDefn entityDefn) throws Exception;
}