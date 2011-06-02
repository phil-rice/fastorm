package org.fastorm.utilities.strings;

public interface ISimpleStringWithSetters extends ISimpleString {

	void setFromString(String string);

	void setFromByteArray(byte[] byteArray, int start, int size);

}
