package com.solution.yoti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.yoti.constants.HooverConstants;
import com.solution.yoti.exception.HooverException;
import com.solution.yoti.exception.ValidationException;
import com.solution.yoti.model.InputModel;
import com.solution.yoti.model.ResultModel;
import com.solution.yoti.service.HooverService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HooverControlller {
    @Autowired
    HooverService service;

    private final Logger logger = LoggerFactory.getLogger(HooverControlller.class);


    @RequestMapping(path = "/getResults", method = RequestMethod.POST)
    public ResponseEntity<ResultModel> getResult(@RequestBody  String jsonStr)  {
        final String method_name ="getResult";
        logger.debug("{} start",method_name);
        if(StringUtils.isNotBlank(jsonStr)){
            ObjectMapper mapper = new ObjectMapper();
            try {
                InputModel inputModel = mapper.readValue(jsonStr,InputModel.class);
                //start cleaning process
                ResultModel resultModel = service.cleanRoomWithHoover(inputModel);
                logger.debug("{} end",method_name);
                return new ResponseEntity<ResultModel>(resultModel,HttpStatus.OK);

            } catch (IOException e) {
                logger.info("{} invalid json String",method_name);
                logger.info(jsonStr);
                return new ResponseEntity<ResultModel>(createHeaders(HooverConstants.INVALID_JSON_STRING),HttpStatus.BAD_REQUEST);
            } catch (HooverException e) {
                logger.info("{} Hoover Exception {}  {}",method_name,e.getStatus(),e.getMessage());
                return new ResponseEntity<ResultModel>(createHeaders(e.getStatus()),HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (ValidationException e) {
                logger.info("{} Validation Exception {}",method_name,e.getMessage());
                return new ResponseEntity<ResultModel>(createHeaders(e.getMessage()),HttpStatus.BAD_REQUEST);
            }

        }
        logger.debug("{} end",method_name);
        return new ResponseEntity<ResultModel>( HttpStatus.NOT_FOUND);
    }

    private HttpHeaders createHeaders(String message){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HooverConstants.ERROR_MESSAGE,message);
        return headers;
    }




}
