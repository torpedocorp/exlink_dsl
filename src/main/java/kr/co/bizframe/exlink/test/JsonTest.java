package kr.co.bizframe.exlink.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class JsonTest {
	
	public static void main(String args[]){
		Gson gson = new Gson();
		JsonReader reader;
		try {
			reader = new JsonReader(new FileReader("D:/project/open_sw_camel/camel_comp_test/data/http/server/ID-sushin-1530844062233-0-4"));
			List<Map<String, Object>> datas = gson.fromJson(reader, ArrayList.class);
			System.out.println(datas);
			for(Map data : datas){
				System.out.println("####"+data);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}
	
	}

}
