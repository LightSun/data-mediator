package com.heaven7.java.data.mediator;

/**
 * the global setting of 'data-mediator'.
 * @author heaven7
 * @since 1.2.0
 */
public final class GlobalSetting {

    private double currentVersion = 1.0; //default

    private static class Creator{
       static final GlobalSetting INSTANCE = new GlobalSetting();
    }
    private GlobalSetting(){}

    public static GlobalSetting getDefault(){
        return Creator.INSTANCE;
    }
    public double getCurrentVersion() {
        return currentVersion;
    }
    public void setCurrentVersion(double currentVersion) {
        this.currentVersion = currentVersion;
    }
}
