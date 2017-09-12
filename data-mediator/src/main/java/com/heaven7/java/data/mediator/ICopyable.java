package com.heaven7.java.data.mediator;

/**
 * indicate the target can be copy.
 * @param <T> the data type
 * @author heaven7
 */
public interface ICopyable<T> {

	/**
	 * copy the data.
	 * @return the copied data
	 */
	T copy();

	/**
	 * copy the data out to the target.
	 * @param out the out object.
	 */
	void copyTo(T out);
}
