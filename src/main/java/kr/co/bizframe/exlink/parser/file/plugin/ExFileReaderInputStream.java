package kr.co.bizframe.exlink.parser.file.plugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import kr.co.bizframe.exlink.ExlinkDslException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ExFileReaderInputStream extends FileInputStream{

	public static Logger logger = LoggerFactory.getLogger(ExFileReaderInputStream.class);
	

	byte[] buffer = null;
	int SIZE_OF_READ = 4000;
	int bufferPos = 0;
	int position = 0;
	boolean isEnd = false;
	
	public ExFileReaderInputStream(File file) throws Exception {
		super(file);
		writeToBuffer();
	}
	
	public ExFileReaderInputStream(String file) throws Exception {
		super(file);
		writeToBuffer();
	}
	
	public boolean isReadable() throws Exception {
		if(SIZE_OF_READ > buffer.length){
//			logger.info(" pos="+(pos)+" data length:"+buffer.length+" data:["+new String(buffer)+"] isEnd:"+isEnd);
			if(buffer.length == bufferPos)
				return false;
		}
		return true;
	}
	
	public void writeToBuffer() throws IOException{
		byte[] temp = new byte[SIZE_OF_READ];
		int size = 0;
		if((size = read(temp)) != -1) {
			buffer = Arrays.copyOfRange(temp, 0, size);
			if(size != SIZE_OF_READ) isEnd = true;
			bufferPos = 0;
//			System.out.println(size + "buffer["+new String(buffer)+"]");
			
		}
		
	}
	
	public byte readByte() throws ExlinkDslException {
		byte ret = 0;
		try {	
			if(bufferPos == buffer.length && !isEnd){
				writeToBuffer();
			}
			ret = buffer[bufferPos];
//			System.out.print((char)ret);
//			System.out.println("position="+pos + " char:"+(char)ret);
			bufferPos++;
			position++;
			
		} catch (Exception e) {
			logger.error(" pos="+bufferPos+" data length:"+buffer.length+" data:["+new String(buffer)+"] isEnd:"+isEnd);
			e.printStackTrace();
			throw new ExlinkDslException("readByte error:: pos="+bufferPos+" data:"+new String(buffer)+":"+isEnd+e.toString(), e);
		}
		return ret;
	}
	
	
	
	public int getPosition() {
		return position;
	}

	public byte[] readByte(int toRead) throws ExlinkDslException {
		byte[] ret = new byte[toRead];
		int i=0;
		while(i < toRead){
			ret[i] = readByte();
			i++;
		}
		return ret;
	}
	
	
	
	
	
	
	public static void main(String args[]) throws Exception{
		try{
			long startTime = System.currentTimeMillis();
			ExFileReaderInputStream is = new ExFileReaderInputStream("misc/doc/ESB_DATA/test/test");
			int i=0;
			int readcount = 0 ;
			int totalreadcount = 0 ;
			while(is.isReadable()){
				
				byte tt = is.readByte();
				System.out.print((char)tt);
//				if(readcount == 1000){
//					totalreadcount++;
//					System.out.println("totalreadcount="+totalreadcount);
//					readcount = 0;
//				}
//				readcount++;
			}
			long endTime = System.currentTimeMillis();
			System.out.println("take time = "+(endTime - startTime));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
