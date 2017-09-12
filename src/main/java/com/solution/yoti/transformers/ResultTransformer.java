package com.solution.yoti.transformers;

import com.solution.yoti.model.Point;
import com.solution.yoti.model.ResultModel;

import java.util.Arrays;

public class ResultTransformer {

    public ResultModel getResultModel(Integer patches, Point currentPosition){
        ResultModel resultModel = new ResultModel();
        resultModel.setPatches(patches);
        if(currentPosition!=null){
            resultModel.setCoords(Arrays.asList(currentPosition.getX(),currentPosition.getY()));
        }
       return resultModel;
    }
}
