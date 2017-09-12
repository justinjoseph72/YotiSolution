package com.solution.yoti.service;

import com.solution.yoti.exception.HooverException;
import com.solution.yoti.exception.ValidationException;
import com.solution.yoti.model.InputModel;
import com.solution.yoti.model.ResultModel;

public interface HooverService {

    ResultModel cleanRoomWithHoover(InputModel inputModel) throws HooverException,ValidationException;

}
