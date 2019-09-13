package edisonbro.com.edisonbroautomation.StaticClasses;


/**
 *  FILENAME: StaticVariabes_div.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  class for accessing static variables and method created to store data during runtime of application.
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 *
 *
 */

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.CheckBox;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//import com.edisonbro.automation.adapters.StaticVariables;

public class StaticVariabes_div 
{

	//public static String Home_ip_download_url= "http://automation.edisonbro.com/remoteip.php?q=";

	public static String Home_ip_download_url= "https://edisonbro.in/automation/remoteip.php?q=";

	public static int frag_dv=0;

	public static int frag_num=0;
	public static int frag_post=0;
	public static String Fragment_ToLoad="";

	public static boolean Fragment_firsttime=false;

	public static String frag_toload;
	public static String devnumbr;
	public static String devtyp;

	public static int arr_count_num=0;
	public static int arr_count[]={0 ,0 , 1, 2};

	public static int exact_frag_post=0;

	public static String Host=null;
	public static int Port=0;
	public static String HOUSE_NAME="";
	public static String classname="";
	public static int base=0;
	public static int housebase=0;


	public static String timer_roomnum_edit ;

	public static String Mood_sel_number ;

	public static String mood_roomnum_edit ;

	public static boolean loaded_lay_Multiple=false;
    public static String Selected_device;
	public static String typ_G_I;
	
	public static String[] housenamearr;
	public static String[] housenumarr;
	public static void housenameinit(){
		housenamearr = new String[housebase];
	}
	
	public static int counts=0;
	static String[] housenoa=new String[counts];
	public static String[] devnoa=new String[counts];
	public static String[] roomnoa=new String[counts];
	public static String[] devnoaitem=new String[counts];
	static int pst=0;
	//static String roomimgarr[]={null,null,null,null,null,null,null,null,null,null};
	
	static String[] roomimgarr;// = new String[base];
	public static String[] roomnamearr;
	
	public static String[] roomnameusrlistarr;
	
	public static String[] roomdeviceslistarr;
	
	public static String[] roomdevicesnumberlistarr;
	public static String[]roomdevicesnamelistarr;
	
	static String[] roomswitchnumberlistarr;
	
	public static String devselectedforuser="" ;
	
	public static String devswitchsselectedforuser ;
	
	public static String devselectedforuser_edit ;
	
	public static String[] roomdevicesnumberaccessarr;
	
	static String roomdeviceswitchnumberaccess;
	
	public static String[] roomnoarr;
	
	public static String[] roomdevicestypearr;
	
	public static String[] roommodletypearr;
	
	public static String[] roomgrouptypearr;

	public static String[] roomdevnamearr;
	//************************************************************
	public static volatile boolean DebugEnable = false;
	//************************************************************
	public static  boolean continueSelection = false;

	public static String House_reference_num="0000";

	public static String House_dbver_num_local="00";
	public static String House_dbver_num_gateway="00";

	//public static String ModelNameNumber;
	
	public static String TAG = "EdisonBro Ph1";

	public static String spinnerroom="";

	
	public static void log(String logStr,String TAG1)
	{
		//enable logs for configuration
		StaticVariables.isLogEnabled=DebugEnable;
		
		if(logStr!=null && DebugEnable)
		{
			Log.d(TAG, TAG1 + logStr);
		}
	}

	public static String sheerdevno="00";
	public static boolean sheerselected=false;

	//static int base=0;
	private static Bitmap[] mThumbIds;	// = new Bitmap[StaticData.base];
	public static String roomname="";
	//public static String loggedusrname="";
	public static String loggedpwd="";
	public static String loggeduser="";
	public static String loggeduser_type="";
	public static String housename="";
	public static String housenumber="";

	public static String devnumber="";
	public static  String dev_name;
	public  static  String typegrpindi;
	public  static  String room_n;
	public static  String dev_typ_num;
	
	static boolean remoteconnection=false,dynamicstatic=false;
	public static int camposition;
	//private A_aa dev_adap;
	
	static int SERVERPORT;
	static String servIp,servPort,ssid,inp,RL,IP,ipdb,portdb,ssiddb,servpwd,adminpassword,adminpwd;

	public 	static int myStringArr_Pos=0;
	
	public static void roomimgarr1(){
		roomimgarr = new String[base];
		roomnamearr = new String[base];
	}
	
	public static int swbcounts=0;
	public static String[] swbnoa;
	public static String[] swbmodela;
	public static String[] swbdevicenames;
	public static int swbpst=0;
	public static void Swbinit(int countval,String swbnoarr[],String swbmodelarr[],String swbdevicenamearr[]){
		swbcounts=countval;
		swbnoa=new String[swbcounts];
		swbmodela=new String[swbcounts];
		swbdevicenames=new String[swbcounts];
		swbnoa=swbnoarr;
		swbmodela=swbmodelarr;
		swbdevicenames=swbdevicenamearr;
	}
	
	
	public static int swdcounts=0;
	static String[] swdnoa;
	static String[] swdmodela;	
	public static int swdpst=0;
	public static void Swdinit(int countval,String swdnoarr[],String swdmodelarr[]){
		swdcounts=countval;
		swdnoa=new String[swdcounts];
		swdmodela=new String[swdcounts];
		swdnoa=swdnoarr;
		swdmodela=swdmodelarr;
	}


	public static int curtcounts=0;
	public static String[] curtnoa;
	public static String[] curtmodela;
	public static String[] curtdevicenames;
	public static int curtpst=0;
	public static void Curtinit(int countval,String curtnoarr[],String curtmodelarr[],String curtdevicenamearr[]){
	curtcounts=countval;
	curtnoa=new String[curtcounts];
	curtmodela=new String[curtcounts];
		curtdevicenames=new String[curtcounts];
	curtnoa=curtnoarr;
	curtmodela=curtmodelarr;
		curtdevicenames=curtdevicenamearr;
   }




	public static int dmmrcounts=0;
	public static String[] dmmrnoa;
	public static String[] dmmrmodela;
	public static String[] dmmrdevicenames;
	public static int dmmrpst=0;
	public static void dmmrinit(int countval,String dmmrnoarr[],String dmmrmodelarr[],String dmmrdevicenamearr[]){
		dmmrcounts=countval;
		dmmrnoa=new String[dmmrcounts];
		dmmrmodela=new String[dmmrcounts];
		dmmrdevicenames=new String[dmmrcounts];
		dmmrnoa=dmmrnoarr;
		dmmrmodela=dmmrmodelarr;
		dmmrdevicenames=dmmrdevicenamearr;
		StaticVariabes_div.log("dmmrinit_individual called"+dmmrnoa.length, "StaticVariabes_div");
	}

	public static int rgbbcounts=0;
	public static String[] rgbbnoa;
	public static String[] rgbbmodela;
	public static String[] rgbbdevicenames;
	public static int rgbbpst=0;
	public static void rgbbinit(int countval,String rgbbnoarr[],String rgbbmodelarr[],String rgbbdevicenamearr[]){
		rgbbcounts=countval;
		rgbbnoa=new String[rgbbcounts];
		rgbbmodela=new String[rgbbcounts];
		rgbbdevicenames=new String[rgbbcounts];
		rgbbnoa=rgbbnoarr;
		rgbbmodela=rgbbmodelarr;
		rgbbdevicenames=rgbbdevicenamearr;

	}
	public static int pircounts=0;
	public static String[] pirnoa;
	public static String[] pirmodela,pirdevicenames;
	public static int pirrpst=0;

	public static void pirinit(int countval,String pirnoarr[],String pirmodelarr[],String pirdevicenamearr[]){
		pircounts=countval;
		pirnoa=new String[pircounts];
		pirmodela=new String[pircounts];
		pirdevicenames=new String[pircounts];
		pirnoa=pirnoarr;
		pirmodela=pirmodelarr;
		pirdevicenames=pirdevicenamearr;
	}




	public static int accounts=0;
	public static String[] acnoa;
	public static String[] acmodela,acdevicenames;
	public static int acpst=0;
	public static void acinit(int countval,String acnoarr[],String acmodelarr[],String[] acdevicenamearr){
		accounts=countval;
		acnoa=new String[accounts];
		acmodela=new String[accounts];
		acdevicenames=new String[accounts];
		acnoa=acnoarr;
		acmodela=acmodelarr;
		acdevicenames=acdevicenamearr;

	}

	public static int smrtdogcounts=0;
	public static String[] smrtdognoa;
	public static String[] smrtdogmodela,smrtdogdevicenames;
	public static int smrtdogpst=0;
	public static void smrtdoginit(int countval,String smrtdognoarr[],String smrtdogmodelarr[],String[] smrtdogdevicenamearr){
		smrtdogcounts=countval;
		smrtdognoa=new String[smrtdogcounts];
		smrtdogmodela=new String[smrtdogcounts];
		smrtdogdevicenames=new String[smrtdogcounts];
		smrtdognoa=smrtdognoarr;
		smrtdogmodela=smrtdogmodelarr;
		smrtdogdevicenames=smrtdogdevicenamearr;

	}


	public static int alexacounts=0;
	public static String[] alexanoa;
	public static String[] alexamodela,alexadevicenames;
	public static int alexapst=0;
	public static void alexainit(int countval,String alexanoarr[],String alexamodelarr[],String[] alexadevicenamearr){
		alexacounts=countval;
		alexanoa=new String[alexacounts];
		alexamodela=new String[alexacounts];
		alexadevicenames=new String[alexacounts];
		alexanoa=alexanoarr;
		alexamodela=alexamodelarr;
		alexadevicenames=alexadevicenamearr;

	}

	public static int geysercounts=0;
	static String[] geysernoa;
	static String[] geysermodela;	
	public static int geyserpst=0;
	public static void geyserinit(int countval,String geysernoarr[],String geysermodelarr[]){
	geysercounts=countval;
	geysernoa=new String[geysercounts];
	geysermodela=new String[geysercounts];
	geysernoa=geysernoarr;
	geysermodela=geysermodelarr;
}

	public static int clbcounts=0;
	static String[] clbnoa;
	static String[] clbmodela;	
	public static int clbpst=0;
	public static void clbinit(int countval,String clbnoarr[],String clbmodelarr[]){
	clbcounts=countval;
	clbnoa=new String[clbcounts];
	clbmodela=new String[clbcounts];
	clbnoa=clbnoarr;
	clbmodela=clbmodelarr;
}
	
	public static int dlscounts=0;
	static String[] dlsnoa;
	static String[] dlsmodela;	
	public static int dlspst=0;
	public static void dlsinit(int countval,String dlsnoarr[],String dlsmodelarr[]){
	dlscounts=countval;
	dlsnoa=new String[dlscounts];
	dlsmodela=new String[dlscounts];
	dlsnoa=dlsnoarr;
	dlsmodela=dlsmodelarr;
}
	
	
	public static int fmcounts=0;
	static String[] fmnoa;
	static String[] fmmodela;	
	public static int fmpst=0;
	public static void fminit(int countval,String fmnoarr[],String fmmodelarr[]){
	fmcounts=countval;
	fmnoa=new String[fmcounts];
	fmmodela=new String[fmcounts];
	fmnoa=fmnoarr;
	fmmodela=fmmodelarr;
}
	

	public static int watrpumpcounts=0;
	static String[] watrpumpnoa;
	static String[] watrpumpmodela;	
	public static int watrpumppst=0;
	public static void watrpumpinit(int countval,String watrpumpnoarr[],String watrpumpmodelarr[]){
	watrpumpcounts=countval;
	watrpumpnoa=new String[watrpumpcounts];
	watrpumpmodela=new String[watrpumpcounts];
	watrpumpnoa=watrpumpnoarr;
	watrpumpmodela=watrpumpmodelarr;
}
	

	public static int gskcounts=0;
	public static String[] gsknoa;
	public static String[] gskmodela,gskdevicenames;
	public static int gskrpst=0;
	public static void gskinit(int countval,String gsknoarr[],String gskmodelarr[],String gskdevicenamearr[]){
	gskcounts=countval;
	gsknoa=new String[gskcounts];
	gskmodela=new String[gskcounts];
	gskdevicenames=new String[gskcounts];
	gsknoa=gsknoarr;
	gskmodela=gskmodelarr;
	gskdevicenames=gskdevicenamearr;
	}




/*	public static void pirinit(int countval,String pirnoarr[]){
		pircounts=countval;
		pirnoa=new String[pircounts];
		pirmodela=new String[pircounts];
		pirnoa=pirnoarr;

	}*/
	
	public static int fancounts=0;
	static String[] fannoa;
	static String[] fanmodela;	
	public static int fanpst=0;
	public static void faninit(int countval,String fannoarr[],String fanmodelarr[]){
		fancounts=countval;
		fannoa=new String[fancounts];
		fanmodela=new String[fancounts];
		fannoa=fannoarr;
		fanmodela=fanmodelarr;
	}
	
	
	public static int projscrcounts=0;
	public static String[] projscrnoa;
	public static String[] projscrdevicenames;
	public static String[] projscrmodela;
	public static int projscrpst=0;

	public static void projscrinit(int countval,String projscrnoarr[],String projscrmodelarr[],String projscrdevicenamearr[]){

		projscrcounts=countval;
		projscrnoa=new String[projscrcounts];
		projscrmodela=new String[projscrcounts];
		projscrdevicenames=new String[projscrcounts];
		projscrnoa=projscrnoarr;
		projscrmodela=projscrmodelarr;
		projscrdevicenames=projscrdevicenamearr;
   }


//-----somphy----//
	public static int sompyscrcounts=0;
	public static String[] sompyscrnoa;
	public static String[] sompyscrdevicenames;
	public static String[] sompyscrmodela;
	public static int sompyscrpst=0;

	public static void sompyscrinit(int countval,String projscrnoarr[],String projscrmodelarr[],String projscrdevicenamearr[]){

		sompyscrcounts=countval;
		sompyscrnoa=new String[sompyscrcounts];
		sompyscrmodela=new String[sompyscrcounts];
		sompyscrdevicenames=new String[sompyscrcounts];
		sompyscrnoa=projscrnoarr;
		sompyscrmodela=projscrmodelarr;
		sompyscrdevicenames=projscrdevicenamearr;
	}

//--------swing gate
	public static int swingscrcounts=0;
	public static String[] swingscrnoa;
	public static String[] swingscrdevicenames;
	public static String[] swingscrmodela;
	public static int swingscrpst=0;

	public static void swingscrinit(int countval,String projscrnoarr[],String projscrmodelarr[],String projscrdevicenamearr[]){

		swingscrcounts=countval;
		swingscrnoa=new String[swingscrcounts];
		swingscrmodela=new String[swingscrcounts];
		swingscrdevicenames=new String[swingscrcounts];
		swingscrnoa=projscrnoarr;
		swingscrmodela=projscrmodelarr;
		swingscrdevicenames=projscrdevicenamearr;
	}
/////////////-slide gate----------
	public static int slidescrcounts=0;
	public static String[] slidescrnoa;
	public static String[] slidescrdevicenames;
	public static String[] slidescrmodela;
	public static int slidescrpst=0;

	public static void slidescrinit(int countval,String projscrnoarr[],String projscrmodelarr[],String projscrdevicenamearr[]){

		slidescrcounts=countval;
		slidescrnoa=new String[slidescrcounts];
		slidescrmodela=new String[slidescrcounts];
		slidescrdevicenames=new String[slidescrcounts];
		slidescrnoa=projscrnoarr;
		slidescrmodela=projscrmodelarr;
		slidescrdevicenames=projscrdevicenamearr;
	}



	public static int projliftcounts=0;
	static String[] projliftnoa;
	static String[] projliftmodela;	
	public static int projliftpst=0;
	public static void projliftinit(int countval,String projliftnoarr[],String projliftmodelarr[]){
		projliftcounts=countval;
		projliftnoa=new String[projliftcounts];
		projliftmodela=new String[projliftcounts];
		projliftnoa=projliftnoarr;
		projliftmodela=projliftmodelarr;
}


	public static int aqucounts=0;
	static String[] aqunoa;
	static String[] aqumodela;	
	public static int aqupst=0;
	public static void aquinit(int countval,String aqunoarr[],String aqumodelarr[]){
	aqucounts=countval;
	aqunoa=new String[aqucounts];
	aqumodela=new String[aqucounts];
	aqunoa=aqunoarr;
	aqumodela=aqumodelarr;
}
	

	
	
///////////////for groups///////////////////////////////////////////////////////////


	public static boolean load_group_master=true;

	
	public static int dimgrplen=0;
	public static int dimindilen=0;
	
	public static String[] dimgrpsarr,dimgrpsmodarr,dimgrpsnamarr;
	public static int dimunder_grpcount=0;
	public static int dimpst=0;
	
	public static void Dmrinit_group(int countval,String dmgrparr[],String dimgrpmodarr[],String dimgrpnamarr[]){

	dimunder_grpcount=countval;
	dimgrpsarr=new String[dimunder_grpcount];
	dimgrpsnamarr=new String[dimunder_grpcount];
	dimgrpsarr=dmgrparr;
	dimgrpsmodarr=dimgrpmodarr;
	dimgrpsnamarr=dimgrpnamarr;
		StaticVariabes_div.log("Dmrinit_groupcalled"+dimgrpsarr.length, "StaticVariabes_div");
}
	
	
	public static int rgbgrplen=0;
	public static int rgbindilen=0;

	public static String[] rgbgrpsarr,rgbgrpsmodarr,rgbgrpsnamarr;
	public static int rgbunder_grpcount=0;
	public static int rgbpst=0;

	public static void Rgbinit_group(int countval,String rgbgrparr[],String rgbgrpmodarr[],String rgbgrpnamarr[]){
		rgbunder_grpcount=countval;
		rgbgrpsarr=new String[rgbunder_grpcount];
		rgbgrpsnamarr=new String[rgbunder_grpcount];
		rgbgrpsarr=rgbgrparr;
		rgbgrpsmodarr=rgbgrpmodarr;
		rgbgrpsnamarr=rgbgrpnamarr;

	}

	public static int curgrplen=0;
	public static int curindilen=0;

	public static String[] curgrpsarr_sheer;

	public static String[] curgrpsarr,curgrpsmodarr,curgrpsnamarr;
	public static int curunder_grpcount=0;
	public static int curpst=0;
	public static void Curinit_group(int countval,String curgrparr[],String curgrpmodarr[],String curgrpnamarr[]){
		curunder_grpcount=countval;
		curgrpsarr=new String[curunder_grpcount];
		curgrpsnamarr=new String[curunder_grpcount];
		curgrpsarr=curgrparr;
		curgrpsmodarr=curgrpmodarr;
		curgrpsnamarr=curgrpnamarr;
	}
	
	public static int pirgrplen=0;
	public static int pirindilen=0;
	
	public static String[] pirgrpsarr,pirgrpsmodarr,pirgrpsnamarr;
	public static int pirunder_grpcount=0;
	public static int pirpst=0;
	
	public static void Pirinit_group(int countval,String pirgrparr[],String pirgrpmodarr[],String pirgrpnamarr[]){
		pirunder_grpcount=countval;
		pirgrpsarr=new String[pirunder_grpcount];
		pirgrpsmodarr=new String[pirunder_grpcount];
		pirgrpsnamarr=new String[pirunder_grpcount];
		pirgrpsarr=pirgrparr;
		pirgrpsmodarr=pirgrpmodarr;
		pirgrpsnamarr=pirgrpnamarr;
		StaticVariabes_div.log("Pirinit_groupcalled"+pirgrpsarr.length, "StaticVariabes_div");
	}
	
	public static int gskgrplen=0;
	public static int gskindilen=0;
	
	public static String[] gskgrpsarr,gskgrpsmodarr,gskgrpsnamarr;
	public static int gskunder_grpcount=0;
	public static int gskpst=0;
	
	public static void Gskinit_group(int countval,String gskgrparr[],String gskgrpmodarr[],String gskgrpnamarr[]){
		gskunder_grpcount=countval;
		gskgrpsarr=new String[gskunder_grpcount];
		gskgrpsnamarr=new String[gskunder_grpcount];
		gskgrpsarr=gskgrparr;
		gskgrpsmodarr=gskgrpmodarr;
		gskgrpsnamarr=gskgrpnamarr;

	}
	
/*	static String[] clbgrpsarr;
	public static int clbunder_grpcount=0;
	public static int clbpst=0;
	
	public static void Clbinit(int countval,String clbgrparr[]){
		clbunder_grpcount=countval;
		clbgrpsarr=new String[clbunder_grpcount];		
		clbgrpsarr=clbgrparr;		
	}*/
	/*static String[] grpsarr;
	public static int under_grpcount=0;
	public static int devpst=0;
	public static void devicesinit(int countval,String grparr[]){
		under_grpcount=countval;
		grpsarr=new String[dimunder_grpcount];		
		grpsarr=grparr;		
	}*/
	public static Hashtable<CheckBox, Boolean> table1 = new Hashtable(); 
	public static Enumeration<?> tableValue1;


	public static String IV = "AAAAAAAAAAAAAAAA";
	public static String encryptionKey = "edisonbrosmartha";

	public static byte[] encrypt(byte[] ivBytes, byte[] keyBytes, byte[] mes)
			throws NoSuchAlgorithmException,
			NoSuchPaddingException,
			InvalidKeyException,
			InvalidAlgorithmParameterException,
			IllegalBlockSizeException,
			BadPaddingException, IOException {

		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
		Cipher cipher = null;
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

		return  cipher.doFinal(mes);

	}

	public static byte[] decrypt(byte[] ivBytes, byte[] keyBytes, byte[] bytes)
			throws NoSuchAlgorithmException,
			NoSuchPaddingException,
			InvalidKeyException,
			InvalidAlgorithmParameterException,
			IllegalBlockSizeException,
			BadPaddingException, IOException, ClassNotFoundException {

		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);

		return  cipher.doFinal(bytes);

	}



	public static String decrypt_db(byte[] ivBytes, byte[] keyBytes, byte[] bytes)
			throws NoSuchAlgorithmException,
			NoSuchPaddingException,
			InvalidKeyException,
			InvalidAlgorithmParameterException,
			IllegalBlockSizeException,
			BadPaddingException, IOException, ClassNotFoundException {

		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
		//byte[] decode = Base64.decode(bytes, Base64.NO_WRAP);
		String decryptString = new String(cipher.doFinal(bytes), "UTF-8");
		return decryptString;
		//return  cipher.doFinal(bytes);

	}
}
