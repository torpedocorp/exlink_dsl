package kr.co.bizframe.exlink.sql.scirpt;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlParser {
	
	public static Logger logger = LoggerFactory.getLogger(SqlParser.class);
	
	public ArrayList<String> columnList = new ArrayList<String>();
	

	public String getQuery(String sql){
		if(sql.indexOf("#") > -1){
			String head = sql.substring(0, sql.indexOf("#"));
			String tail = sql.substring(sql.indexOf("#"), sql.length());
			String temp = tail.substring(0, tail.indexOf(" "));
			String column = temp.replace("#", "");
			columnList.add(column);
			String append = tail.substring(tail.indexOf(" "), tail.length());
			sql = head + " ? "+ append;
			sql = getQuery(sql);
		}
		return sql;
		
	}

	public ArrayList<String> getColumnList() {
		return columnList;
	}

	
	public static void main(String args[]) throws Exception{
			    
	}
}
