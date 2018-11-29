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

package kr.co.bizframe.exlink.model;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ExFileBlockInfos{
	
	
	private ArrayList<BlockInfo> blockInfos = new ArrayList<BlockInfo>();
	
	
	
	public ArrayList<BlockInfo> getBlockInfos() {
		return blockInfos;
	}

	public void setBlockInfos(ArrayList<BlockInfo> blockInfos) {
		this.blockInfos = blockInfos;
	}
	
	public void addBlockInfo(BlockInfo blockInfo) {
		this.blockInfos.add(blockInfo);
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(BlockInfo info : blockInfos){
			sb.append("blockinfos ="+info+"\n");
		}
		return sb.toString();
	}
	
	public class BlockInfo{
		
		
		int rowcount = 0;
		
		int readsize = 0;
		
		int blocksize = 0;
		
		private boolean isHeader;
		
		private byte[] blockData = null;
		
		public BlockInfo(boolean isHeader){
			this.isHeader = isHeader;
		}
		
		public boolean isHeader() {
			return isHeader;
		}

		public int getRowcount() {
			return rowcount;
		}

		public int getReadsize() {
			return readsize;
		}

		public int getBlocksize() {
			return blocksize;
		}

		public void setBlocksize(int blocksize) {
			this.blocksize = blocksize;
		}

		public byte[] getBlockData() {
			return blockData;
		}

		public void setBlockData(byte[] blockData, int readsize) {
			this.blockData = blockData;
			this.blocksize = blockData.length;
			this.readsize = readsize;
			this.rowcount = blocksize/readsize;
		}

		
		public String toString(){
			StringBuffer sb = new StringBuffer();
			sb.append("readsize="+this.readsize);
			sb.append(" blocksize="+this.blockData.length);
			sb.append(" rowcount="+this.rowcount);
			sb.append(" isHeader="+this.isHeader);
			try {
				sb.append(" \ndata=["+new String(this.getBlockData(), "euc-kr")+"]");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			for(int key : rowSizes.keySet()){
//					sb.append(" byte size["+key+"] row count="+rowSizes.get(key));
//			}
			return sb.toString();
		}
		
	}
	

}
