/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe exlink project licenses this file to you under the Apache License,     
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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;

import kr.co.bizframe.exlink.parser.conf.FileParserConfingFactory;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMapConf;
import kr.co.bizframe.exlink.parser.conf.model.FileParserMetaConf;
import kr.co.bizframe.exlink.type.InterfaceType;

public class TestReadFile {
	
	public void readBlock() throws Exception {
		

		InputStream is = new FileInputStream("misc/doc/ESB_DATA/test/I070108079999410_IL09001000_B60060010_011741");
		int data = is.read();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int length = 0;
		while (data != -1) {
			//'\n' 개행문자가 있는 경우 
			if (data == 10) {
				byte[] temp = baos.toByteArray();
				byte[] endBytes = Arrays.copyOfRange(temp, temp.length-3, temp.length);
				String boundStr = new String(endBytes);
				if(boundStr.equals("END")){
					System.out.println(baos.toByteArray().length+"block\n" +new String(baos.toByteArray(), "euc-kr"));
					byte[] blockBytes = Arrays.copyOfRange(baos.toByteArray(), 1, baos.toByteArray().length-3);
					System.out.println("blockBytes:"+new String(blockBytes, "euc-kr"));
					baos = new ByteArrayOutputStream();
				}else if(boundStr.equals("EOF")){
					System.out.println("file ended");
				}
			} 
			baos.write((byte)data);
			data = is.read();
		}
	}
	
	public void readIndicator() throws Exception {
		

		InputStream is = new FileInputStream("misc/doc/ESB_DATA/test/test");
		int data = is.read();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int length = 0;
		boolean isHeader = true;
		
		
		FileParserMetaConf metaConf = FileParserConfingFactory.getInstance().getMetaConf(InterfaceType.B07000010);
		FileParserMapConf mapConf = null;
		String indicator = "";
		int readsize = 5;
		while (data != -1) {
			//'\n' 개행문자가 있는 경우 
			if(isHeader){
				if (data == 10) {
					byte[] headerbyte = baos.toByteArray();
					System.out.println(baos.toByteArray().length+"header\n" +new String(headerbyte, "euc-kr"));
					baos = new ByteArrayOutputStream();
					isHeader = false;
				} 
			}else{
				if(baos.toByteArray().length == 4){
					System.out.println(baos.toByteArray().length +" data=[" +new String(baos.toByteArray())+"]");
					indicator = new String(Arrays.copyOfRange(baos.toByteArray(), 1, baos.toByteArray().length));
					if(indicator.equals("EOF")) break;
					mapConf = metaConf.getMapConfByFileId(indicator).getMapConf();
					readsize = Integer.parseInt(mapConf.getLengthByte());
					System.out.println("indicator=" +indicator+" read size="+readsize);
				}
				System.out.println("temp length=" +baos.toByteArray().length+" read size="+readsize);
				if(baos.toByteArray().length == readsize+1 ){
					byte[] row = Arrays.copyOfRange(baos.toByteArray(), 1, baos.toByteArray().length);
					System.out.println(baos.toByteArray().length +" row=[" +new String(row, "euc-kr")+"]");
					baos = new ByteArrayOutputStream();
				}
			}
			baos.write((byte)data);
			data = is.read();
		}
	}
	
	public void read() throws Exception{
		RandomAccessFile rdma = new RandomAccessFile("misc/doc/ESB_DATA/test/test", "r");
		String line = "";
 
		// 전체 문자열을 출력
		while ((line = rdma.readLine()) != null) {
			System.out.println( ":"+line);			
		}
 
		// 문자열 총 길이
		System.out.println("total length : " + rdma.length()+"\n");
	}

	public static void main(String args[]) throws Exception {
		FileParserConfingFactory.getInstance().init("misc/mapScript", "");
		System.out.println((int)'\n');
		TestReadFile trf = new TestReadFile();
		trf.readIndicator();
		
	}

}
