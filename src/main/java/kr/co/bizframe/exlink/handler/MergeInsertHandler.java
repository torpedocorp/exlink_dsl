package kr.co.bizframe.exlink.handler;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.sql.DataSource;

import kr.co.bizframe.exlink.transform.plugin.JsonTransform;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MergeInsertHandler {

	public static Logger logger = LoggerFactory.getLogger(MergeInsertHandler.class);

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
	
	public void setParam(PreparedStatement ps, Object obj, int index) throws SQLException{
		logger.debug(index +" param="+obj.getClass()+" "+obj);
		if(obj instanceof String) ps.setString(index, (String)obj);
		else if(obj instanceof Date) ps.setDate(index, new java.sql.Date(((Date)obj).getTime()));
		else if(obj instanceof Float) ps.setFloat(index, (Float)obj);
		else if(obj instanceof Double) ps.setDouble(index, (Double)obj);
		else new SQLException(obj.getClass()+" not defined param type");
	}

	public void mergeInsert(String sql, ArrayList<String> columnList, String jdbcDsFilename, String jsonFilePath){
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
					jdbcDsFilename);

			DataSource dataSource = (DataSource) context.getBean("dataSource");
			Connection conn = null;

			try {

				conn = dataSource.getConnection();
				
				List<Map<String, Object>> params = getGsonData(jsonFilePath);
				int index = 1;
				
				for(Map dataMap :params){
					PreparedStatement ps = conn.prepareStatement(sql);
					for(String columnName : columnList){
						Object obj = dataMap.get(columnName);
						logger.debug(columnName+"=["+obj+"]");
						if(obj instanceof String){
							String date = (String)obj;
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
							formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
							try {
								Date ret = formatter.parse(date);
								obj = ret;
							} catch (ParseException e) {
							}
						}
						setParam(ps, obj, index++);
					}
					logger.debug("EXECUTE QUERY = "+sql);
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
			logger.debug("■■■■■■■■■■■■■■■■■■■■■■■■ insert ended");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}

	}

	

}
