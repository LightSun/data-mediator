package com.heaven7.java.data.mediator.support.gson;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.heaven7.java.data.mediator.support.test.Car3TypeAdapter;

import java.util.ArrayList;
import java.util.List;

@JsonAdapter(Car3TypeAdapter.class)
public class Car3 {
 
    @Since(2.0)
    private String mark;
     
    @Since(2.1) 
    private int model;
 
    @Until(1.9)
    private String type;
     
    @Until(2.1)
    private String maker;
     
    private double cost;
     
    private List<String> colors = new ArrayList<String>();
 
    public String getMark() {
        return mark;
    }
 
    public void setMark(String mark) {
        this.mark = mark;
    }
 
    public int getModel() {
        return model;
    }
 
    public void setModel(int model) {
        this.model = model;
    }
 
    public String getType() {
        return type;
    }
 
    public void setType(String type) {
        this.type = type;
    }
 
    public String getMaker() {
        return maker;
    }
 
    public void setMaker(String maker) {
        this.maker = maker;
    }
 
    public double getCost() {
        return cost;
    }
 
    public void setCost(double cost) {
        this.cost = cost;
    }
 
    public List<String> getColors() {
        return colors;
    }
 
    public void setColors(List<String> colors) {
        this.colors = colors;
    }
 
    @Override
    public String toString() {
        return "Car3 [mark=" + mark + ", model=" + model + ", type=" + type
                + ", maker=" + maker + ", cost=" + cost + ", colors=" + colors
                + "]";
    }
 
 
}