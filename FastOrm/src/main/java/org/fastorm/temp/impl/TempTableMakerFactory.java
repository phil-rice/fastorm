package org.fastorm.temp.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.fastorm.api.IJobOptimisations;
import org.fastorm.defns.IEntityDefn;
import org.fastorm.manyToOne.ManyToOne;
import org.fastorm.oneToMany.OneToManyUsingStoredProcs;
import org.fastorm.temp.IMutatingTempTableMaker;
import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.temp.ITempTableMakerFactory;

public class TempTableMakerFactory implements ITempTableMakerFactory {

	private final List<ISecondaryTempTableMaker> items;

	public TempTableMakerFactory(IJobOptimisations optimisations) {
		this(Arrays.<ISecondaryTempTableMaker> asList(new OneToManyUsingStoredProcs(optimisations), new ManyToOne(optimisations)));
	}

	public TempTableMakerFactory(List<ISecondaryTempTableMaker> items) {
		this.items = Collections.unmodifiableList(new ArrayList<ISecondaryTempTableMaker>(items));
	}

	@Override
	public ISecondaryTempTableMaker findReaderMakerFor(Map<String, String> parent, Map<String, String> child) {
		if (parent == null)
			throw new NullPointerException();
		if (child == null)
			throw new NullPointerException();
		for (ISecondaryTempTableMaker maker : items) {
			if (maker.accept(parent, child))
				return maker;
		}
		throw new CannotDetermineTempTableMakerToUseException();
	}

	@Override
	public IMutatingTempTableMaker findMutatingMakerFor(IEntityDefn entityDefn) {
		return null;
	}

}
