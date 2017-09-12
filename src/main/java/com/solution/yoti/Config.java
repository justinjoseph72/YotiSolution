package com.solution.yoti;

import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

/**
 * Created by Justin on 10/09/2017.
 */
@Configuration
public class Config {

    @Value("${database-server}")
    private String serverName;
    @Value("${database-name}")
    private String databaseName;
    @Value("${user-name}")
    private String userName;
    @Value("${password}")
    private String password;

    @Bean
    public JdbcTemplate getTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSource getDatasource(){
        PGPoolingDataSource source = new PGPoolingDataSource();
        source.setServerName(serverName);
        source.setDatabaseName(databaseName);
        //source.setCurrentSchema("testSchema");
        source.setUser(userName);
        source.setPassword(password);
        source.setMaxConnections(10);
        return source;
    }







}
