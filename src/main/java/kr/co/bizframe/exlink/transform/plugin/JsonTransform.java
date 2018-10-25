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

public class JsonTransform {
	
	public static Logger logger = LoggerFactory.getLogger(JsonTransform.class);

	
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
