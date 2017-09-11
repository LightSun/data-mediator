package com.heaven7.java.data.mediator;

/**
 * an interface that indicate the object can share some information across some component(eg: android multi activity.).
 * and can be clear in future.
 * @author heaven7
 */
public interface IShareable {

	/**
	 * clear the share data.
	 */
	void clearShare();
}
