package org.fastorm.dataGenerator;

public interface IRowGenerator extends Iterable<IGenerator>{
	
	int fanout();

}
