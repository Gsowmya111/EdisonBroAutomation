
package edisonbro.com.edisonbroautomation.blaster;
import java.util.Arrays;

public class Blaster {
    private static native byte[] D1( byte[] arr , int size );
 	
 	private static native byte[] E1( byte[] arr, int size );
 	
 	private static native byte[] D2( byte[] arr , int size );
 	
 	private static native byte[] E2( byte[] arr, int size );
     
    static {
 	    System.loadLibrary("Blaster");
 	}
    
    public static byte[] WriteData(String str, int type)
    {
    	byte[] enB;
    	if(type==1)
    		enB = E1(str.getBytes(),str.length());
    	else if(type==2)
    		enB = E2(str.getBytes(),str.length());
    	else
    		return str.getBytes();
    	
    	return (Arrays.copyOf(enB, str.length()));
    }
	
    public static String ReadData(byte[] data, int type)
    {
    	byte[] deB;
    	if(type==1)
    		deB= D1(data, data.length);
    	else if(type==2)
    		deB= D2(data, data.length);
    	else
    		return new String(data);
    	String result = new String(deB);
    	return result.substring(0, data.length);	
    }

}
