package com.solution.yoti.repository;

import com.solution.yoti.exception.HooverException;
import com.solution.yoti.model.InputModel;
import com.solution.yoti.model.ResultModel;

public interface HooverRepository {
    void saveData(InputModel inputModel, ResultModel resultModel) throws HooverException;


}
