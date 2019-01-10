package com.iot.center.helper;

public class ResponseBodys {
	
	private int result;
	private String msg;
	
	public ResponseBodys(int reslut,String msg){
		this.result=reslut;
		this.msg=msg;
	}
	
	public ResponseBodys(){
		
	}
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
