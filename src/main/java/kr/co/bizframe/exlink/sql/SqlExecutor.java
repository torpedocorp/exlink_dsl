package kr.co.bizframe.exlink.sql;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.sql.DataSource;

import kr.co.bizframe.exlink.ExlinkDslException;
import kr.co.bizframe.exlink.sql.scirpt.SqlScriptInfo;
import kr.co.bizframe.exlink.transform.plugin.JsonTransform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;


public class SqlExecutor {

	public static Logger logger = LoggerFactory.getLogger(SqlExecutor.class);
	
	public SqlExecutor(){
		
	}

	public List<Map<String, Object>> getGsonData(String filePath){
		try {
			JsonTransform jtf = new JsonTransform();
			List<Map<String, Object>> datas = jtf.loadFile(filePath);
			System.out.println(datas);
			for(Map data : datas){
				System.out.println("####"+data);
			}
			return datas;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
		return null;

	}
	
	public void run(SqlScriptInfo.Sql sql, DataSource dataSource, List<Map<String, Object>> params) {
		executeSql(sql.getRunSql(), sql.getColumnList(), dataSource,  params);
	}
	
	
	public void setParam(PreparedStatement ps, Object obj, int index) throws SQLException{
		if(obj == null) ps.setString(index, null);
		else if(obj instanceof String) ps.setString(index, (String)obj);
		else if(obj instanceof Date) ps.setDate(index, new java.sql.Date(((Date)obj).getTime()));
		else if(obj instanceof Float) ps.setFloat(index, (Float)obj);
		else if(obj instanceof Double) ps.setDouble(index, (Double)obj);
		else new SQLException(obj.getClass()+" not defined param type");
	}

	private Object getDate(Object obj){
		
		if(obj instanceof String){
			String date = (String)obj;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			try {
				Date ret = formatter.parse(date);
				return ret;
			} catch (ParseException e) {
				//e.printStackTrace();
			}
		}
		return obj;
	}
	public void executeSql(String sql, ArrayList<String> columnList, DataSource dataSource, List<Map<String, Object>> params){
		try {
			Connection conn = null;

			try {

				conn = dataSource.getConnection();
				
				for(Map dataMap :params){
					PreparedStatement ps = conn.prepareStatement(sql);
					int index = 1;
					for(String columnName : columnList){
						Object obj = dataMap.get(columnName);
						obj = getDate(obj);
						setParam(ps, obj, index++);
					}
					ps.executeQuery();
					ps.close();
					conn.commit();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);

			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}

	}
	
	public void executeSql(String sql, DataSource dataSource,  MapSqlParameterSource params) {
		NamedParameterJdbcTemplate  jdbcTemplate = new NamedParameterJdbcTemplate (dataSource);
		jdbcTemplate.update(sql, params); 
	}
	
	
	public void executeSql(String sql, ArrayList<String> columnList, DataSource dataSource, AbstractMap<String, Object> params) throws ExlinkDslException{
		try {
			Connection conn = null;

			try {

				conn = dataSource.getConnection();

				PreparedStatement ps = conn.prepareStatement(sql);
				int index = 1;
				for(String columnName : columnList){
					Object obj = params.get(columnName) == null ? params.get(columnName.toUpperCase()) : params.get(columnName);
					obj = getDate(obj);
					setParam(ps, obj, index++);
				}
				ps.executeQuery();
				ps.close();
				conn.commit();

			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);

			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {}
				}
			}
		} catch (Exception e) {
			logger.error("error occured while execute sql ="+sql + " params="+params);
			// TODO Auto-generated catch block
			throw new ExlinkDslException("error occured while execute sql ="+sql + " params="+params, e);
		}

	}

	

	public static void main(String args[]){
		try {


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}

	}


}
