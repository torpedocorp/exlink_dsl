package kr.co.bizframe.exlink.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import kr.co.bizframe.exlink.model.ExFileBlockInfos;
import kr.co.bizframe.exlink.model.ExFileBlockInfos.BlockInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestReadFileStream {

	public static Logger logger = LoggerFactory.getLogger(TestReadFileStream.class);

	public void readFile(File file){
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file),"euc-kr"));
			String tempDir = "./data/tmp";
			String word;
			
			ExFileBlockInfos list = new ExFileBlockInfos();
			BlockInfo binfo = list.new BlockInfo(true);
			while ((word = in.readLine()) != null) {
				System.out.print(word.getBytes("euc-kr").length+"|");
				if(word.equals("END")){
					System.out.println(word);
					list.addBlockInfo(binfo);
					binfo = list.new BlockInfo(false);
				}else{
//					binfo.addRowList(word);
				}
			}
			in.close();
			for(BlockInfo blockInfo : list.getBlockInfos()){
				System.out.println(blockInfo);
			}
			////////////////////////////////////////////////////////////////
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
			System.exit(1);
		}
	}

	public static void main(String args[]){
		TestReadFileStream trtf = new TestReadFileStream();
		trtf.readFile(new File("misc/doc/ESB_DATA/I070104309999401_IL09001000_B60010010_010404"));
	}



}
