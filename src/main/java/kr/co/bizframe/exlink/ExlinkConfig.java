package kr.co.bizframe.exlink;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ExlinkConfig {

	private static String mapDir;
	private static String dsFilename;
	private static String sendFileDir;
	
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("mapDir="+mapDir);
		sb.append(" dsFilename="+dsFilename);
		sb.append(" sendFileDir="+sendFileDir);
		return sb.toString();
	}

	public static String getMapDir() {
		return mapDir;
	}

	public static String getDsFilename() {
		return dsFilename;
	}

	public static void setMapDir(String mapDir) {
		ExlinkConfig.mapDir = mapDir;
	}

	public static void setDsFilename(String dsFilename) {
		ExlinkConfig.dsFilename = dsFilename;
	}

	public static String getSendFileDir() {
		return sendFileDir;
	}

	public static void setSendFileDir(String sendFileDir) {
		ExlinkConfig.sendFileDir = sendFileDir;
	}

}
