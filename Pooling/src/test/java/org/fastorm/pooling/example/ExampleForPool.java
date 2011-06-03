package org.fastorm.pooling.example;

public class ExampleForPool implements IExampleForPool {

	int value;

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ExampleForPool [value=" + value + "]";
	}
}
