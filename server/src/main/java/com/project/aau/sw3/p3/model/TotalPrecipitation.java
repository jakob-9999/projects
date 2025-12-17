package com.project.aau.sw3.p3.model;

import java.util.List;

public class TotalPrecipitation {
    private String type;
    private String dataType;
    private List<String> axisNames;
    private List<Integer> shape;
    private List<Double> values;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public List<String> getAxisNames() {
        return axisNames;
    }

    public void setAxisNames(List<String> axisNames) {
        this.axisNames = axisNames;
    }

    public List<Integer> getShape() {
        return shape;
    }

    public void setShape(List<Integer> shape) {
        this.shape = shape;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }
}
