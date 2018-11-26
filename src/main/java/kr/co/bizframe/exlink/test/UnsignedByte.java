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