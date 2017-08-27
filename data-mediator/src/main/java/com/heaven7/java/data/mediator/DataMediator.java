package com.heaven7.java.data.mediator;

public class DataMediator{
    //flags : FLAG_transient , flag_expose, FLAG_SNAP, FLAG_SHARE_SNAP, FLAG_CACHE,FLAG_RESET 
	//parceable, flag_Serializable
	public static final int FLAG_TRANSIENT = 1;
	public static final int FLAG_EXPOSE    = 2;
	
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
	
	//PoolConfig
	//hide
	private static class Pool{
		
	}
	//auto generate Proxy
	public static class Proxy{
		OnFieldDataChangeListener l;
	}
}
