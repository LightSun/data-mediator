package com.heaven7.java.data.mediator;

/**
 * the global setting of 'data-mediator'.
 * @author heaven7
 * @since 1.2.0
 */
public final class GlobalSetting {

    private double currentVersion = 1.0; //default

    static {
        //static load. like gson version of global config.
        try {
            Class.forName("com.heaven7.java.data.mediator.internal.$StaticLoader");
        } catch (ClassNotFoundException e) {
            //ignore
        }
    }

    private static class Creator{
       static final GlobalSetting INSTANCE = new GlobalSetting();
    }
    private GlobalSetting(){}

    public static GlobalSetting getDefault(){
        return Creator.INSTANCE;
    }

    /**
     * get the gson version which is used to serialize/deserialize json data.
     * @return the gson version.
     */
    public double getGsonVersion() {
        return currentVersion;
    }
    /**
     * set the gson version which is used to serialize/deserialize json data.
     * @param currentVersion the current gson version
     */
    public void setGsonVersion(double currentVersion) {
        if(currentVersion < 1.0){
            throw new IllegalArgumentException();
        }
        this.currentVersion = currentVersion;
    }
}
