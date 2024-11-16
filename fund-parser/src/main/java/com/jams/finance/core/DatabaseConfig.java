package com.jams.finance.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
 
import javax.sql.DataSource;
 
@Configuration
public class DatabaseConfig {
 
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setUrl("jdbc:sqlite:../../pywork/Tagui/data/wm.db");
        //dataSource.setUsername("your_username");
        //dataSource.setPassword("your_password");
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