package com.solution.yoti.service;

import com.solution.yoti.constants.HooverConstants;
import com.solution.yoti.exception.HooverException;
import com.solution.yoti.exception.ValidationException;
import com.solution.yoti.model.InputModel;
import com.solution.yoti.model.Point;
import com.solution.yoti.model.ResultModel;
import com.solution.yoti.repository.HooverRepository;
import com.solution.yoti.transformers.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HooverServiceImpl implements HooverService{

    private Point roomDimeneion =null;
    private Point initialPosition =null;
    private List<Point> patchLocations = null;
    private String instructions = null;
    private Point currentPosition = null;
    private int patchesCleaned = 0;

    @Autowired
    HooverRepository repo;
    private final Logger logger = LoggerFactory.getLogger(HooverServiceImpl.class);

    @Override
    public ResultModel cleanRoomWithHoover(InputModel inputModel) throws HooverException,ValidationException {
        final String method_name ="cleanRoomWithHoover";
        logger.debug("{} start",method_name);
        if(inputModel!=null){
            try {
                //set up data for processing
                setup(inputModel);
                char[] ch = instructions.toCharArray();
                for(int i=0;i<ch.length;i++){
                    logger.info("instruction is {}",String.valueOf(ch[i]));
                    //cleaning for each instruction
                    moveHoover(String.valueOf(ch[i]));
                }
                logger.debug("Hoover position after cleaning is {}",currentPosition.toString());
                logger.debug("Number of patches cleaned {}",patchesCleaned);
                //create result model
                ResultModel resultModel = new ResultTransformer().getResultModel(patchesCleaned,currentPosition);
                //save data to database
                saveToDatabase(inputModel,resultModel);
                logger.debug("{} end",method_name);
                return resultModel;
            } catch (HooverException e) {
               logger.info("{}  Hoover exception {}",method_name,e.getStatus());
               throw e;
            } catch (ValidationException e) {
                logger.info("{}  Validation exception {}",method_name,e.getMessage());
                throw e;
            }
        }
        logger.debug("{} end",method_name);
        return null;
    }

    /**
     * This metod will use the input model to setup data
     * processing
     * @param inputModel
     */
    private void setup(InputModel inputModel) throws ValidationException {
        final String method_name = "setup";
        logger.debug("{} start",method_name);
        List<Integer> roomSizeList = inputModel.getRoomSize();
        //validation and setup for roomsize
        if(roomSizeList!=null && roomSizeList.size()==2){
            roomDimeneion = new Point(roomSizeList.get(0),roomSizeList.get(1));
            logger.info("room is " + roomDimeneion.toString());
        }
        else{
            throw new ValidationException(HooverConstants.ROOM_DIMENSTION_ERROR);
        }
        //validation and setup for patches
        if(inputModel.getPatches()!=null && !inputModel.getPatches().isEmpty() ){
            patchLocations = new ArrayList<>();
            inputModel.getPatches().forEach(patchPair->{
                Point patch = new Point(patchPair.get(0),patchPair.get(1));
                logger.info("patch position  is " + patch.toString());
                patchLocations.add(patch);
            });
        }
        //validation and setup for inital coordinates
        if(inputModel.getCoords()!=null && inputModel.getCoords().size()==2){
            initialPosition = new Point(inputModel.getCoords().get(0),inputModel.getCoords().get(1));
            logger.info("initial position  is " + initialPosition.toString());
        }
        else{
            throw new ValidationException(HooverConstants.INITIAL_COORDINATES_ERROR);
        }
        // validation and setup for instructions
        if(inputModel.getInstructions().matches("[NSEW]+")){
            instructions = inputModel.getInstructions();
        }else{
            throw new ValidationException(HooverConstants.INVALID_INSTRUCTIONS);
        }
        currentPosition = initialPosition;
        logger.info("instructions are " + instructions);
    }

    private void moveHoover(String direction){
        int oldX = currentPosition.getX();
        int oldY = currentPosition.getY();
        switch (direction){
            case "N": {
                currentPosition.setY(oldY+1);
                if(roomDimeneion.getY() -1 <currentPosition.getY()){
                    currentPosition.setY(oldY);
                }
                cleanDirt();
                logger.info("Moved North"); break;
            }
            case "E": {
                currentPosition.setX(oldX+1);
                if(roomDimeneion.getX() -1 <currentPosition.getX()){
                    currentPosition.setX(oldX);
                }
                cleanDirt();
                logger.info("Moved East"); break;
            }
            case "W": {
                currentPosition.setX(oldX-1);
                if(currentPosition.getX() -1 < 0){
                    currentPosition.setX(oldX);
                }
                cleanDirt();
                logger.info("Moved West"); break;
            }
            case "S": {
                currentPosition.setY(oldY-1);
                if( currentPosition.getY()-1 < 0){
                    currentPosition.setY(oldY);
                }
                cleanDirt();
                logger.info("Moved South"); break;
            }
        }
    }

    private void cleanDirt() {
        if(!patchLocations.isEmpty() &&patchLocations.remove(currentPosition)){
            logger.info("current position {}",currentPosition );
            patchesCleaned = patchesCleaned + 1;
            logger.info("Cleaned dirt");
        }
    }

    private void saveToDatabase(InputModel inputModel, ResultModel resultModel) throws HooverException {
        final String method_name ="saveToDatabase";
        logger.debug("{} start",method_name);
        try {
            repo.saveData(inputModel,resultModel);
        } catch (HooverException e) {
           logger.info("{} exception {}",method_name,e.getStatus());
           throw e;
        }
    }

}
