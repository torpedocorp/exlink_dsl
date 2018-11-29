/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe mas project licenses this file to you under the Apache License,     
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:                   
 *                                                                              
 *   http://www.apache.org/licenses/LICENSE-2.0                                 
 *                                                                              
 * Unless required by applicable law or agreed to in writing, software          
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the     
 * License for the specific language governing permissions and limitations      
 * under the License.                                                           
 */ 

package kr.co.bizframe.exlink.test;

public class UnsignedByte {
	public static void main (String args[]) {
		byte b1 = 127;
		byte b2 = -128;
		byte b3 = -1;

		System.out.println(b1);
		System.out.println(b2);
		System.out.println(b3);
		System.out.println(unsignedByteToInt(b1));
		System.out.println(unsignedByteToInt(b2));
		System.out.println(unsignedByteToInt(b3));
		/*
	    127
	    -128
	    -1
	    127
	    128
	    255
		 */
	}

	public static String byteToHex(byte b){
		int i = b & 0xFF;
		return Integer.toHexString(i);
	}

	public static int unsignedByteToInt(byte b) {
		System.out.println("hex="+byteToHex(b));
		return (int) b & 0xFF;
	}
}