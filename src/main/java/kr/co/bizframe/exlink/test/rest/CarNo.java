package kr.co.bizframe.exlink.test.rest;

import com.google.gson.Gson;

public class CarNo {
	
    private String carno;
    
    
    public String getCarno() {
		return carno;
	}

	public void setCarno(String carno) {
		this.carno = carno;
	}

	
	public static void main(String args[]){
		Gson gson = new Gson();
		CarNo car = new CarNo();
		car.setCarno("test");
		System.out.println(gson.toJson(car));
	}

}