package com.iot.shcs.helper;

import java.io.IOException;

public class Base64Utils {

	/**  
    * 编码  
    * @param bstr  
    * @return String  
    */    
   @SuppressWarnings("restriction")
   public static String encode(byte[] bstr){    
	   return new sun.misc.BASE64Encoder().encode(bstr);    
   }    
   
   /**  
    * 解码  
    * @param str  
    * @return string  
    */    
   @SuppressWarnings("restriction")
   public static String decode(String str){    
	   byte[] bt = null;    
	   try {    
	       sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();    
	       bt = decoder.decodeBuffer( str );    
	   } catch (IOException e) {    
	       e.printStackTrace();    
	   }    
       return new String(bt);    
   }    
   
   /**
    * 判断一个字符串是否被Base编码过
    * @param args
    */
   public static Boolean isBase64Encode(String str){
	   //将目标字符串  解密后再将解密字符串加密回去 与原来的值做比较   如果相同就是base64 
	   String decodeBuf=decode(str);
	   String encodeBuf=encode(decodeBuf.getBytes());
	   if(str.equals(encodeBuf)) {
		   return true;
	   }else {
		   return false;
	   }
   }
}
