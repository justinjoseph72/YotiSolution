package com.solution.yoti.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultModel implements Serializable{
    private static final long serialVersionUID = 1L;
    private List<Integer> coords;
    private Integer patches;

    public List<Integer> getCoords() {
        return coords;
    }

    public void setCoords(List<Integer> coords) {
        this.coords = coords;
    }

    public Integer getPatches() {
        return patches;
    }

    public void setPatches(Integer patches) {
        this.patches = patches;
    }
}
