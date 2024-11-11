package com.jams.fund;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jams.fund.core.DbEngine;
import com.jams.fund.core.DatabaseConfig;


public class DbDemo{

	public static void main(String[] args) {
		ApplicationContext ctx=new AnnotationConfigApplicationContext(DatabaseConfig.class);
		DbEngine engine=(DbEngine) ctx.getBean("dbEngine");
		
		List<Map<String, Object>> rows=engine.queryForList("select rpt_date,value from netvalue where code='YLFFB2201' order by rpt_date desc");
		
		for(Map<String,Object> row: rows) {
			row.forEach((key,value)->{System.out.println(key+":  "+value);});
			System.out.println("-----------------");
		}
		
	}
}
