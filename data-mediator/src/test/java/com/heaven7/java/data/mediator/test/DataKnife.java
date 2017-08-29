package com.heaven7.java.data.mediator.test;

/**
 * note parceable ? pool size.?
 * @author heaven7
 */
//@Fields(serializeName="", propertyNmae="",flags, type=A.class)
//flags : FLAG_transient , flag_expose, FLAG_SNAP, FLAG_SHARE_SNAP, FLAG_CACHE
//super interface: reset, clear snap & share snap, save cache (cache)
public class DataKnife {

	private transient volatile int key;
	
	static <T> T create(Class<?> clazz){
		return null;
	}
	static Proxy createProxy(Class<?> clazz){
		return null;
	}
	static Proxy getProxy(Class<?> clazz){
		return null;
	}
	static <T> T get(Class<?> clazz){
		return null;
	}
	static class Proxy{
		//register and unregister listener(data change , ).
	}
	static class Pool{
		//obtain , recycle,
	}
}
