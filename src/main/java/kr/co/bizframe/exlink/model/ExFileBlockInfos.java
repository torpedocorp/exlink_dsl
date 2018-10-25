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
