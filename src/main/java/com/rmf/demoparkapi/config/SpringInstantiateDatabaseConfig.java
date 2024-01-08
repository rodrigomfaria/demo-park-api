package com.rmf.demoparkapi.config;

import com.rmf.demoparkapi.services.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
public class SpringInstantiateDatabaseConfig {

    @Autowired
    private DbService dbService;

    @Bean
    @Profile("test")
    public boolean instantiateDatabase() throws ParseException {
        dbService.instantiateTestDatabase();
        return true;
    }

}
