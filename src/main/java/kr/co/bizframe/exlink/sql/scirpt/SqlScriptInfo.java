package kr.co.bizframe.exlink.sql.scirpt;

import java.util.ArrayList;

public class SqlScriptInfo {
	
	private String datasource;
	
	private ArrayList<Sql>  sqlList = new ArrayList<Sql>();
	
	
	
	public String getDatasource() {
		return datasource;
	}


	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}


	public ArrayList<Sql> getSqlList() {
		return sqlList;
	}


	public void setSqlList(ArrayList<Sql> sqlList) {
		this.sqlList = sqlList;
	}
	
	public void addSqlList(Sql sqlList) {
		this.sqlList.add(sqlList);
	}

	public class Sql{
		private String type;
		private String id;
		private String transfrom; 
		private String from;
		private String scirpSql;
		private String runSql;
		private ArrayList<String> columnList = new ArrayList<String>();
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTransfrom() {
			return transfrom;
		}
		public void setTransfrom(String transfrom) {
			this.transfrom = transfrom;
		}
		public String getFrom() {
			return from;
		}
		public void setFrom(String from) {
			this.from = from;
		}
		
		public String getScirpSql() {
			return scirpSql;
		}
		public void setScirpSql(String scirpSql) {
			this.scirpSql = scirpSql;
		}
		public String getRunSql() {
			return runSql;
		}
		public void setRunSql(String runSql) {
			this.runSql = runSql;
		}
		public ArrayList<String> getColumnList() {
			return columnList;
		}
		public void setColumnList(ArrayList<String> columnList) {
			this.columnList = columnList;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
		public String toString(){
			StringBuffer sb = new StringBuffer();
			sb.append("type="+type);
			sb.append(" id="+id);
			sb.append(" transfrom="+transfrom);
			sb.append(" from="+from);
			sb.append("\n========================================================\n");
			sb.append("runSql="+runSql);
			sb.append("scirpSql="+scirpSql);
			sb.append("========================================================\n");
			for(String column :columnList){
				sb.append("## column="+column);
			}
			return sb.toString();
		}
		
		
	}
	
	
	

}
