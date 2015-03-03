package com.chijsh.banana.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chijsh on 3/3/15.
 */
public class Geo {
    public String type;

    public List<Float> coordinates = new ArrayList<>(2);

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Float> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Float> coordinates) {
        this.coordinates = coordinates;
    }
}
