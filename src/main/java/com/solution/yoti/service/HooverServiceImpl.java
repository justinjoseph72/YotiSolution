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

    private Point roomDimension =null;
    private Point initialPosition =null;
    private List<Point> patchLocations = null;
    private String instructions = null;
    private Point currentPosition = null;
    private int patchesCleaned = 0;

    @Autowired
    HooverRepository repo;
    private final Logger logger = LoggerFactory.getLogger(HooverServiceImpl.class);

    /**
     * this method will clean the room with hoover.
     * It takes a InputModel object and setups the data to process.
     * Id successfull the input and the result will be saved to database
     *
     * @param inputModel
     * @return
     * @throws HooverException
     * @throws ValidationException
     */
    @Override
    public ResultModel cleanRoomWithHoover(InputModel inputModel) throws HooverException,ValidationException {
        final String method_name ="cleanRoomWithHoover";
        logger.debug("{} start",method_name);
        reset();
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
        /*
        * validation and setup for roomsize
         */

        //no room present or invalid number of coordinates
        if(roomSizeList==null || roomSizeList.size()!=2){
            throw new ValidationException(HooverConstants.ROOM_DIMENSTION_ERROR);
        }

        if(roomSizeList!=null && roomSizeList.size()==2){
            //any negative coordinate present
            if(roomSizeList.stream().filter(x->x<0).findAny().isPresent()){
                throw new ValidationException(HooverConstants.INVALID_ROOM_SIZE);
            }
            roomDimension = new Point(roomSizeList.get(0),roomSizeList.get(1));
            logger.info("room is " + roomDimension.toString());
        }
        /*
         *validation and setup for patches
         */
        if(inputModel.getPatches()!=null && !inputModel.getPatches().isEmpty() ){
            patchLocations = new ArrayList<>();
            inputModel.getPatches().forEach(patchPair->{
                Point patch = new Point(patchPair.get(0),patchPair.get(1));
                logger.info("patch position  is " + patch.toString());
                patchLocations.add(patch);
            });
        }
        /*
        *validation and set up for inital coordinate
         */
        //no inital coordinate present or invalid number of coordinates
        if(inputModel.getCoords()==null || inputModel.getCoords().size()!=2){
            throw new ValidationException(HooverConstants.INITIAL_COORDINATES_ERROR);
        }
        // any negative coordinate or the hoover is out of room
        if(inputModel.getCoords()!=null && inputModel.getCoords().size()==2){
            if(inputModel.getCoords().get(0) < 0 || inputModel.getCoords().get(0) >= inputModel.getRoomSize().get(0)
                    || inputModel.getCoords().get(1) < 0 || inputModel.getCoords().get(1) >= inputModel.getRoomSize().get(1)
                    ){
                throw new ValidationException(HooverConstants.INITIAL_COORDINATES_OUT_OF_ROOM_ERROR);
            }
            initialPosition = new Point(inputModel.getCoords().get(0),inputModel.getCoords().get(1));
            logger.info("initial position  is " + initialPosition.toString());
        }
        /*
         *validation and setup for instructions
          */
        if(inputModel.getInstructions().matches("[NSEW]+")){
            instructions = inputModel.getInstructions();
        }else{
            throw new ValidationException(HooverConstants.INVALID_INSTRUCTIONS);
        }
        currentPosition = initialPosition;
        logger.info("instructions are " + instructions);
    }

    /**
     * This method will update the current location and clean the dirt
     * based on the direction
     * @param direction
     */
    private void moveHoover(String direction){
        int oldX = currentPosition.getX();
        int oldY = currentPosition.getY();
        switch (direction){
            case "N": {
                currentPosition.setY(oldY+1);
                if(roomDimension.getY() -1 <currentPosition.getY()){
                    currentPosition.setY(oldY);
                    logger.info("WALL" );
                }
                cleanDirt();
                 break;
            }
            case "E": {
                currentPosition.setX(oldX+1);
                if(roomDimension.getX() -1 <currentPosition.getX()){
                    currentPosition.setX(oldX);
                    logger.info("WALL" );
                }
                cleanDirt();
                 break;
            }
            case "W": {
                currentPosition.setX(oldX-1);
                if(currentPosition.getX() < 0){
                    currentPosition.setX(oldX);
                    logger.info("WALL" );
                }
                cleanDirt();
                 break;
            }
            case "S": {
                currentPosition.setY(oldY-1);
                if( currentPosition.getY() < 0){
                    currentPosition.setY(oldY);
                    logger.info("WALL" );
                }
                cleanDirt();
                break;
            }
        }
    }

    /**
     * This method will remove the position from the patches list to clean it
     */
    private void cleanDirt() {
        if(!patchLocations.isEmpty() &&patchLocations.remove(currentPosition)){
            patchesCleaned = patchesCleaned + 1;
            logger.info("Cleaned dirt");
        }
    }

    /**
     * This method will write the data to the database
     * @param inputModel
     * @param resultModel
     * @throws HooverException
     */
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

    /**
     * This method will reset all the data
     */
    private void reset(){
          roomDimension =null;
          initialPosition =null;
          patchLocations = null;
          instructions = null;
          currentPosition = null;
          patchesCleaned = 0;
    }

}
