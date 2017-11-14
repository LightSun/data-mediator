package com.heaven7.plugin.data.mediator.convertor;

public class PropertyInfo {

    private Property property;

    private String serializeName;
    private boolean serialize;
    private boolean deserialize;
    private double since;
    private double until;
    private boolean useExpose;

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getSerializeName() {
        return serializeName;
    }

    public void setSerializeName(String serializeName) {
        this.serializeName = serializeName;
    }

    public boolean isSerialize() {
        return serialize;
    }

    public void setSerialize(boolean serialize) {
        this.serialize = serialize;
    }

    public boolean isDeserialize() {
        return deserialize;
    }

    public void setDeserialize(boolean deserialize) {
        this.deserialize = deserialize;
    }

    public double getSince() {
        return since;
    }

    public void setSince(double since) {
        this.since = since;
    }

    public double getUntil() {
        return until;
    }

    public void setUntil(double until) {
        this.until = until;
    }

    public void setUseExpose(boolean b) {
        this.useExpose = b;
    }
    public boolean isUseExpose() {
        return useExpose;
    }
}
