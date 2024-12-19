package com.jams.finance.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.jams.finance.Config;

import javax.sql.DataSource;
 
@Configuration
public class DatabaseConfig {
 
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Config.dbDriver);
        dataSource.setUrl(Config.dbUrl);
        dataSource.setUsername(Config.dbUser);
        dataSource.setPassword(Config.dbPass);
        return dataSource;
    }
 
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    
    @Bean
    public DbEngine dbEngine(JdbcTemplate template) {
        return new DbEngine(template);
    }
    
    
}