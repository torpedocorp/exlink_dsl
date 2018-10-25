package kr.co.bizframe.exlink.transform.plugin;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonReader;

public class ByteReadTransform {
	
	public static Logger logger = LoggerFactory.getLogger(ByteReadTransform.class);
	
	
	public  String getString(String str, int sPoint, int length) throws Exception{
	    String EncodingLang = "euc-kr";
	    byte[] bytes = str.getBytes("euc-kr");

	    byte[] value = new byte[length];

	    if(bytes.length < sPoint + length){
	        throw new Exception("Length of bytes is less. length : " + bytes.length + " sPoint : " + sPoint + " length : " + length);
	    }

	    for(int i = 0; i < length; i++){
	        value[i] = bytes[sPoint + i];
	    }

	  /*  System.out.println("utf-8 -> euc-kr        : " + new String(word.getBytes("utf-8"), "euc-kr"));
	    System.out.println("utf-8 -> ksc5601       : " + new String(word.getBytes("utf-8"), "ksc5601"));
	    System.out.println("utf-8 -> x-windows-949 : " + new String(word.getBytes("utf-8"), "x-windows-949"));
	    System.out.println("utf-8 -> iso-8859-1    : " + new String(word.getBytes("utf-8"), "iso-8859-1"));
	    System.out.println("iso-8859-1 -> euc-kr        : " + new String(word.getBytes("iso-8859-1"), "euc-kr"));
	    System.out.println("iso-8859-1 -> ksc5601       : " + new String(word.getBytes("iso-8859-1"), "ksc5601"));
	    System.out.println("iso-8859-1 -> x-windows-949 : " + new String(word.getBytes("iso-8859-1"), "x-windows-949"));
	    System.out.println("iso-8859-1 -> utf-8         : " + new String(word.getBytes("iso-8859-1"), "utf-8"));
	    System.out.println("euc-kr -> utf-8         : " + new String(word.getBytes("euc-kr"), "utf-8"));
	    System.out.println("euc-kr -> ksc5601       : " + new String(word.getBytes("euc-kr"), "ksc5601"));
	    System.out.println("euc-kr -> x-windows-949 : " + new String(word.getBytes("euc-kr"), "x-windows-949"));
	    System.out.println("euc-kr -> iso-8859-1    : " + new String(word.getBytes("euc-kr"), "iso-8859-1"));
	    System.out.println("ksc5601 -> euc-kr        : " + new String(word.getBytes("ksc5601"), "euc-kr"));
	    System.out.println("ksc5601 -> utf-8         : " + new String(word.getBytes("ksc5601"), "utf-8"));
	    System.out.println("ksc5601 -> x-windows-949 : " + new String(word.getBytes("ksc5601"), "x-windows-949"));
	    System.out.println("ksc5601 -> iso-8859-1    : " + new String(word.getBytes("ksc5601"), "iso-8859-1"));
	    System.out.println("x-windows-949 -> euc-kr     : " + new String(word.getBytes("x-windows-949"), "euc-kr"));
	    System.out.println("x-windows-949 -> utf-8      : " + new String(word.getBytes("x-windows-949"), "utf-8"));
	    System.out.println("x-windows-949 -> ksc5601    : " + new String(word.getBytes("x-windows-949"), "ksc5601"));
	    System.out.println("x-windows-949 -> iso-8859-1 : " + new String(word.getBytes("x-windows-949"), "iso-8859-1"));*/
	    return new String(value, EncodingLang).trim();
	}


	
	JsonSerializer<Date> ser = new JsonSerializer<Date>() {
		@Override
		public JsonElement serialize(Date src,
				java.lang.reflect.Type arg1, JsonSerializationContext arg2) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			logger.debug("#############################"+src);
			return new JsonPrimitive(formatter.format(src));
		}
	};
	
	JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
		@Override
		public Date deserialize(JsonElement src,
				java.lang.reflect.Type arg1, JsonDeserializationContext arg2)
				throws JsonParseException {
			String date = src.getAsString();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			try {
				logger.debug("#############################"+date);
				return formatter.parse(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	};


	public String transform(Object obj) {
		Gson gson =  new GsonBuilder().registerTypeAdapter(Date.class, ser)
		   .registerTypeAdapter(String.class, deser).setPrettyPrinting().create();
		return gson.toJson(obj);
	}

	public List<Map<String, Object>> loadFile(String filepath) throws FileNotFoundException {
		Gson gson =  new GsonBuilder().registerTypeAdapter(Date.class, ser)
				   .registerTypeAdapter(String.class, deser).create();
		JsonReader reader = new JsonReader(new FileReader(filepath));
		List<Map<String, Object>> rows = gson.fromJson(reader, ArrayList.class);
		return rows;
	}
	
	public List<Map<String, Object>> load(String jsonString) throws Exception {
		Gson gson =  new GsonBuilder().registerTypeAdapter(Date.class, ser)
				   .registerTypeAdapter(String.class, deser).create();
		JsonReader reader = new JsonReader(new StringReader(jsonString));
		List<Map<String, Object>> rows = gson.fromJson(reader, ArrayList.class);
		return rows;
	}
}
