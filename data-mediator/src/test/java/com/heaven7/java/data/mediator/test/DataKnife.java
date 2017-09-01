package com.heaven7.java.data.mediator.test;

import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.OnDataChangeListener;

/**
 * note parceable ? pool size.?
 * @author heaven7
 */
//@Fields(serializeName="", propertyNmae="",flags, type=A.class)
//flags : FLAG_transient , flag_expose, FLAG_SNAP, FLAG_SHARE_SNAP, FLAG_CACHE
//super interface: reset, clear snap & share snap, save cache (cache)
public class DataKnife {

	//flags : FLAG_transient , flag_expose, FLAG_SNAP, FLAG_SHARE_SNAP, FLAG_CACHE,FLAG_RESET
	//parceable, flag_Serializable
	public static final int FLAG_TRANSIENT      = 0x0001;
	public static final int FLAG_EXPOSE         = 0x0002;
	public static final int FLAG_SNAP           = 0x0004;
	public static final int FLAG_SHARE_SHAP     = 0x0008;
	public static final int FLAG_CACHE          = 0x0010;
	public static final int FLAG_RESET          = 0x0020;
	public static final int FLAG_SERIALIZABLE   = 0x0040;
	public static final int FLAG_PARCEABLE      = 0x0080;

	static <T> T get(Class<?> clazz){
		return null;
	}
	static <T> T create(Class<?> clazz){
		return null;
	}
	static DataMediator.Proxy createProxy(Class<?> clazz){
		return null;
	}
	static DataMediator.Proxy getProxy(Class<?> clazz){
		return null;
	}

	//PoolConfig
	//hide
	private static class Pool{

	}
	//auto generate Proxy
	public static class Proxy{
		OnDataChangeListener l;
		//getInt(). long, double, char, byte, boolean, float, shortInt, date
	}
}
