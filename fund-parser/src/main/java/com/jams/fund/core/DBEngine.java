package com.jams.fund.core;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;



public class DBEngine {
	private String engineName;
	private JdbcTemplate jdbcTemplate;

	public DBEngine(String engineName, JdbcTemplate jdbcTemplate) {
		super();
		this.engineName = engineName;
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public Map<String, Object> queryForObject(String sql, RowMapper<Map<String, Object>> rowMapper) {
		Map<String,Object> result=jdbcTemplate.queryForObject(sql,rowMapper);
		return  result;
	}

	public Map<String, Object> queryForObject(String sql, RowMapper<Map<String, Object>> rowMapper, String id) {
		Map<String,Object> result=jdbcTemplate.queryForObject(sql,rowMapper);
		return  result;
	}

	public List<Map<String, Object>> queryForList(String sql) {
		List<Map<String, Object>> result=jdbcTemplate.queryForList(sql);
		return  result;
	}

	public <T> T query(final String sql, final ResultSetExtractor<T> rse) throws DataAccessException {
		T result=jdbcTemplate.query(sql,rse);
		return  result;
	}

	public int update(String executableSql) {
		int result=jdbcTemplate.update(executableSql);
		return  result;
	}
}
