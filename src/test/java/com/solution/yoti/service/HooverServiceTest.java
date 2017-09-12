package com.solution.yoti.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.yoti.constants.HooverConstants;
import com.solution.yoti.exception.HooverException;
import com.solution.yoti.exception.ValidationException;
import com.solution.yoti.model.InputModel;
import com.solution.yoti.model.ResultModel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class HooverServiceTest {
    @Autowired HooverService service;
    InputModel inputModel = null;
    ResultModel resultModel = null;


    public void  init(String inputJsonStr) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        inputModel = mapper.readValue(inputJsonStr,InputModel.class);

    }

    @Test
    public void test_result_matching() throws HooverException, ValidationException, IOException {
        String resultStr = "{\"coords\":[1,3],\"patches\":1}";
        String inputJsonStr="{\n" +
                "  \"roomSize\" : [5, 5],\n" +
                "  \"coords\" : [1, 2],\n" +
                "  \"patches\" : [\n" +
                "    [1, 0],\n" +
                "    [2, 2],\n" +
                "    [2, 3]\n" +
                "  ],\n" +
                "  \"instructions\" : \"NNESEESWNWW\"\n" +
                "}";
        init(inputJsonStr);
        ResultModel outputModel = service.cleanRoomWithHoover(inputModel);
        Assert.assertNotNull(outputModel);
        ObjectMapper mapper = new ObjectMapper();
        String outputJsonStr = mapper.writeValueAsString(outputModel);
        Assert.assertEquals(resultStr,outputJsonStr);

    }

    @Test
    public void test_result_matching_with_Wall() throws HooverException, ValidationException, IOException {
        String resultStr = "{\"coords\":[0,4],\"patches\":4}";
        String inputJsonStr="{\n" +
                "  \"roomSize\" : [5, 5],\n" +
                "  \"coords\" : [2, 1],\n" +
                "  \"patches\" : [\n" +
                "    [1, 2],\n" +
                "    [3, 2],\n" +
                "    [2, 3],\n" +
                "    [1, 4],\n" +
                "    [0, 4]\n" +
                "  ],\n" +
                "  \"instructions\" : \"NNNWNSSWWNN\"\n" +
                "}";
        init(inputJsonStr);
        ResultModel outputModel = service.cleanRoomWithHoover(inputModel);
        Assert.assertNotNull(outputModel);
        ObjectMapper mapper = new ObjectMapper();
        String outputJsonStr = mapper.writeValueAsString(outputModel);
        Assert.assertEquals(resultStr,outputJsonStr);

    }

    @Test
    public void test_matching_traversing_dirt_again()  throws HooverException, ValidationException, IOException{
        String resultStr = "{\"coords\":[3,3],\"patches\":3}";
        String inputJsonStr="{\n" +
                "  \"roomSize\" : [5, 5],\n" +
                "  \"coords\" : [2, 1],\n" +
                "  \"patches\" : [\n" +
                "    [1, 2],\n" +
                "    [3, 2],\n" +
                "    [2, 3],\n" +
                "    [1, 4],\n" +
                "    [0, 4]\n" +
                "  ],\n" +
                "  \"instructions\" : \"NNNWWEEES\"\n" +
                "}";
        init(inputJsonStr);
        ResultModel outputModel = service.cleanRoomWithHoover(inputModel);
        Assert.assertNotNull(outputModel);
        ObjectMapper mapper = new ObjectMapper();
        String outputJsonStr = mapper.writeValueAsString(outputModel);
        Assert.assertEquals(resultStr,outputJsonStr);
    }


    @Test
    public void test_input_validation_instructions() throws IOException, HooverException {
        String inputString = "{\n" +
                "  \"roomSize\" : [5, 5],\n" +
                "  \"coords\" : [1, 2],\n" +
                "  \"patches\" : [\n" +
                "    [1, 0],\n" +
                "    [2, 2],\n" +
                "    [2, 3]\n" +
                "  ],\n" +
                "  \"instructions\" : \"NNESEESWONWW\"\n" +
                "}";
        init(inputString);
        try{
            ResultModel outputModel = service.cleanRoomWithHoover(inputModel);
        }
        catch (ValidationException e){
            Assert.assertEquals(HooverConstants.INVALID_INSTRUCTIONS,e.getMessage());
        }
    }

    @Test
    public void test_input_validation_initial_Incorrect_coordinates() throws IOException, HooverException {
        String inputString = "{\n" +
                "  \"roomSize\" : [5, 5],\n" +
                "  \"coords\" : [1, 2,3],\n" +
                "  \"patches\" : [\n" +
                "    [1, 0],\n" +
                "    [2, 2],\n" +
                "    [2, 3]\n" +
                "  ],\n" +
                "  \"instructions\" : \"NNESEESWONWW\"\n" +
                "}";
        init(inputString);
        try{
            ResultModel outputModel = service.cleanRoomWithHoover(inputModel);
        }
        catch (ValidationException e){
            Assert.assertEquals(HooverConstants.INITIAL_COORDINATES_ERROR,e.getMessage());
        }
    }

    @Test
    public void test_input_validation_initial_absent() throws IOException, HooverException {
        String inputString = "{\n" +
                "  \"roomSize\" : [5, 5],\n" +
               // "  \"coords\" : [1, 2,3],\n" +
                "  \"patches\" : [\n" +
                "    [1, 0],\n" +
                "    [2, 2],\n" +
                "    [2, 3]\n" +
                "  ],\n" +
                "  \"instructions\" : \"NNESEESWONWW\"\n" +
                "}";
        init(inputString);
        try{
            ResultModel outputModel = service.cleanRoomWithHoover(inputModel);
        }
        catch (ValidationException e){
            Assert.assertEquals(HooverConstants.INITIAL_COORDINATES_ERROR,e.getMessage());
        }
    }


    @Test
    public void test_input_validation_initial_coordinates_negative() throws IOException, HooverException {
        String inputString = "{\n" +
                "  \"roomSize\" : [5, 5],\n" +
                "  \"coords\" : [1, -2],\n" +
                "  \"patches\" : [\n" +
                "    [1, 0],\n" +
                "    [2, 2],\n" +
                "    [2, 3]\n" +
                "  ],\n" +
                "  \"instructions\" : \"NNESEESWONWW\"\n" +
                "}";
        init(inputString);
        try{
            ResultModel outputModel = service.cleanRoomWithHoover(inputModel);
        }
        catch (ValidationException e){
            Assert.assertEquals(HooverConstants.INITIAL_COORDINATES_OUT_OF_ROOM_ERROR,e.getMessage());
        }
    }

    @Test
    public void test_input_validation_initial_coordinates__outside_room_1() throws IOException, HooverException {
        String inputString = "{\n" +
                "  \"roomSize\" : [5, 5],\n" +
                "  \"coords\" : [1, 9],\n" +
                "  \"patches\" : [\n" +
                "    [1, 0],\n" +
                "    [2, 2],\n" +
                "    [2, 3]\n" +
                "  ],\n" +
                "  \"instructions\" : \"NNESEESWONWW\"\n" +
                "}";
        init(inputString);
        try{
            ResultModel outputModel = service.cleanRoomWithHoover(inputModel);
        }
        catch (ValidationException e){
            Assert.assertEquals(HooverConstants.INITIAL_COORDINATES_OUT_OF_ROOM_ERROR,e.getMessage());
        }
    }

    @Test
    public void test_input_validation_initial_coordinates__outside_room_2() throws IOException, HooverException {
        String inputString = "{\n" +
                "  \"roomSize\" : [5, 5],\n" +
                "  \"coords\" : [12, 3],\n" +
                "  \"patches\" : [\n" +
                "    [1, 0],\n" +
                "    [2, 2],\n" +
                "    [2, 3]\n" +
                "  ],\n" +
                "  \"instructions\" : \"NNESEESWONWW\"\n" +
                "}";
        init(inputString);
        try{
            ResultModel outputModel = service.cleanRoomWithHoover(inputModel);
        }
        catch (ValidationException e){
            Assert.assertEquals(HooverConstants.INITIAL_COORDINATES_OUT_OF_ROOM_ERROR,e.getMessage());
        }
    }

    @Test
    public void test_input_validation_room_dimension() throws IOException, HooverException {
        String inputString = "{\n" +
                "  \"roomSize\" : [5],\n" +
                "  \"coords\" : [1, 2],\n" +
                "  \"patches\" : [\n" +
                "    [1, 0],\n" +
                "    [2, 2],\n" +
                "    [2, 3]\n" +
                "  ],\n" +
                "  \"instructions\" : \"NNESEESWONWW\"\n" +
                "}";
        init(inputString);
        try{
            ResultModel outputModel = service.cleanRoomWithHoover(inputModel);
        }
        catch (ValidationException e){
            Assert.assertEquals(HooverConstants.ROOM_DIMENSTION_ERROR,e.getMessage());
        }
    }

    @Test
    public void test_input_validation_room_dimension_absent() throws IOException, HooverException {
        String inputString = "{\n" +
              //  "  \"roomSize\" : [5],\n" +
                "  \"coords\" : [1, 2],\n" +
                "  \"patches\" : [\n" +
                "    [1, 0],\n" +
                "    [2, 2],\n" +
                "    [2, 3]\n" +
                "  ],\n" +
                "  \"instructions\" : \"NNESEESWONWW\"\n" +
                "}";
        init(inputString);
        try{
            ResultModel outputModel = service.cleanRoomWithHoover(inputModel);
        }
        catch (ValidationException e){
            Assert.assertEquals(HooverConstants.ROOM_DIMENSTION_ERROR,e.getMessage());
        }
    }

    @Test
    public void test_input_validation_room_dimension_negative_1() throws IOException, HooverException {
        String inputString = "{\n" +
                  "  \"roomSize\" : [-1,5],\n" +
                "  \"coords\" : [1, 2],\n" +
                "  \"patches\" : [\n" +
                "    [1, 0],\n" +
                "    [2, 2],\n" +
                "    [2, 3]\n" +
                "  ],\n" +
                "  \"instructions\" : \"NNESEESWONWW\"\n" +
                "}";
        init(inputString);
        try{
            ResultModel outputModel = service.cleanRoomWithHoover(inputModel);
        }
        catch (ValidationException e){
            Assert.assertEquals(HooverConstants.INVALID_ROOM_SIZE,e.getMessage());
        }
    }

    @Test
    public void test_input_validation_room_dimension_negative_2() throws IOException, HooverException {
        String inputString = "{\n" +
                "  \"roomSize\" : [1,-5],\n" +
                "  \"coords\" : [1, 2],\n" +
                "  \"patches\" : [\n" +
                "    [1, 0],\n" +
                "    [2, 2],\n" +
                "    [2, 3]\n" +
                "  ],\n" +
                "  \"instructions\" : \"NNESEESWONWW\"\n" +
                "}";
        init(inputString);
        try{
            ResultModel outputModel = service.cleanRoomWithHoover(inputModel);
        }
        catch (ValidationException e){
            Assert.assertEquals(HooverConstants.INVALID_ROOM_SIZE,e.getMessage());
        }
    }

    @After
    public void destroy(){
        inputModel = null;
        resultModel = null;
    }

}
