package com.heaven7.java.data.mediator.compiler;

public class GlobalConfig {

    private static GlobalConfig sInstance = new GlobalConfig();
    //start gson
    private double version = 1.0;
    private boolean generateJsonAdapter = true;
    private boolean disableGson = false;

    public static GlobalConfig getInstance(){
        return sInstance;
    }

    public double getVersion() {
        return version;
    }
    public void setVersion(double version) {
        this.version = version;
    }

    public boolean isGenerateJsonAdapter() {
        return generateJsonAdapter;
    }

    public void setGenerateJsonAdapter(boolean generateJsonAdapter) {
        this.generateJsonAdapter = generateJsonAdapter;
    }

    public boolean isDisableGson() {
        return disableGson;
    }

    public void setDisableGson(boolean disableGson) {
        this.disableGson = disableGson;
    }

    public boolean isJsonAdapterEnabled(){
        if(isDisableGson() || !isGenerateJsonAdapter()){
            return false;
        }
        return true;
    }
}
