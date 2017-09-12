package com.solution.yoti.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.yoti.constants.HooverConstants;
import com.solution.yoti.exception.HooverException;
import com.solution.yoti.model.InputModel;
import com.solution.yoti.model.ResultModel;
import com.sun.corba.se.impl.activation.RepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;

@Repository
public class RespositoryImpl implements HooverRepository {
    private final Logger logger = LoggerFactory.getLogger(RepositoryImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * This method will write data to database
     * @param inputModel
     * @param resultModel
     * @throws HooverException
     */
    @Override
    @Transactional
    public void saveData(InputModel inputModel, ResultModel resultModel) throws HooverException {
        final String method_name ="saveData";
        logger.debug("{}: save begin",method_name);
        try {
            // parsing the object to json string
        ObjectMapper mapper = new ObjectMapper();
        String inputStr = mapper.writeValueAsString(inputModel);
        String resultString = mapper.writeValueAsString(resultModel);
        //getting the next id for the new row
        Integer nextVal = jdbcTemplate.queryForObject("select nextval('hoover_seq')",Integer.class);
       //inseting the record to the database
        String sql = "insert into public.\"Hoover_Table\" (id, input,output) values (?, ?,?)";
        logger.info("{} sql is {}",method_name,sql);
        Object[] params = new Object[] { nextVal, inputStr,resultString };
        int[] types = new int[] { Types.BIGINT, Types.OTHER,Types.OTHER };
        jdbcTemplate.update(sql,params,types);
        } catch (DataAccessException e) {
            logger.info("{} : {}",method_name,e.getMessage());
            throw new HooverException(HooverConstants.DATABASE_ERROR,e);
        } catch (JsonProcessingException e) {
            logger.info("{} : {}",method_name,e.getMessage());
            throw new HooverException(HooverConstants.JSON_PARSING_ERROR,e);
        }
        logger.debug("{}: save end",method_name);
    }

}
