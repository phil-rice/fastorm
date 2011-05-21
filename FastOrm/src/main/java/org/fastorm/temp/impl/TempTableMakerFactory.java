package org.fastorm.temp.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.fastorm.temp.ISecondaryTempTableMaker;
import org.fastorm.temp.ITempTableMakerFactory;

public class TempTableMakerFactory implements ITempTableMakerFactory {

	private final List<ISecondaryTempTableMaker> items;

	public TempTableMakerFactory() {
		this(Arrays.<ISecondaryTempTableMaker> asList(new OneToMany(), new ManyToOne()));
	}

	public TempTableMakerFactory(List<ISecondaryTempTableMaker> items) {
		this.items = Collections.unmodifiableList(new ArrayList<ISecondaryTempTableMaker>(items));
	}

	public ISecondaryTempTableMaker findMakerFor(Map<String, String> parent, Map<String, String> child) {
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

}
