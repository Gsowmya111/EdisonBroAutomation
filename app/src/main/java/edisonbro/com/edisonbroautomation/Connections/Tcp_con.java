package edisonbro.com.edisonbroautomation.Connections;

/**
 *  FILENAME: Tcp_con.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Singleton class used  to connect to gateway through tcp during operation.
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.databasewired.ServerAdapter;

/**
 * Created by Divya on 7/7/2017.
 */

public class Tcp_con {

    private static final String TAG1 = "Tcp - ";
    //*****************************************************
  /*  public static String ssiddb = "EBBackUp";
    public static volatile String ip = "192.168.1.101";
    public static volatile String port = "9951";
    public static volatile String portdb_rem = "9951";*/

    //**********************************************************************************
    static int sl;
    static int slastTypecc = -1;
    public static boolean recState = false;
    static Handler hndlr;
    private static final int READ_BYTE = 1;
    private static final int READ_LINE = 2;
    private static final int ServStatus = 3;
    private static final int signallevel = 4;
    //private static final int Remote = 5;
    private static final int NetwrkType = 5;
    private static final int MAXUSER = 6;
    private static final int ERRUSER = 7;
    private static final int  UPDATE=8;
    private static final int READ_TIMER=9;

    ///******************************************************************************
    private static final String TAG = "TCP Services";
    public static volatile boolean isTcpClientServices = false;
    public static volatile boolean isTcpThreadStarting = false;
    public static volatile boolean isClientStarted = false;
    private static volatile Socket clientSocket = null;
    public static InputStream input = null;
    public static OutputStream output = null;
    private static volatile Thread clientThread = null;

   // public static volatile String ip = "192.168.1.11";
    //*******************************************************************************
    public static volatile String tcpAddress =  "192.168.1.11";
    public static volatile int tcpPort = 9951;

//************************************************************************************************
    static String result1=null;
    static String gdate = null;
    static String Ipadress_Port = null;
    static InputStream is = null;
    static StringBuilder sb=null;

    static String housenamesta;
   public static Context stacontxt;

    static String remoteurl, remipweb=null,remportweb=null,servIp,servPort,ssid,inp,RL,IP,ipdb,
            portdb,ssiddb,servpwd,adminpassword,adminpwd,adminuser,ipdb_rem,portdb_rem;
    static String wirelessport,timerportno;
    static boolean remoteconnection=false;
///////////////////////////////////////////////////////////////////////////
private static ServerAdapter servadap;
    static String IV = "AAAAAAAAAAAAAAAA";
    static String encryptionKey = "edisonbrosmartha";
    static  byte[] cipher;
    static int o=0;

        public static TcpTransfer mTcpTransfer;

        public Tcp_con(TcpTransfer obj) {
            mTcpTransfer = obj;
        }

        static void startClient()
        {
            if(!isClientStarted && !isTcpThreadStarting)
            {
                isTcpThreadStarting = true;
                clientThread = new Thread(new Tcp_con.ClientThread());
                clientThread.start();
            }
        }

     public  static void stopClient()
        {
            try {
                if(clientSocket!=null)
                {
                    if(clientSocket.isConnected())
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                try {
                    if(clientThread!=null)
                    {
                        clientThread.interrupt();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isClientStarted = false;
                isTcpThreadStarting = false;
                mTcpTransfer.read(ServStatus, "FALSE", null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public static void WriteString_test(String Message){

        if(isClientStarted) {

            if(output!=null) {
                try {
                    StaticVariabes_div.log(output+"out stream "+Message +" len "+Message.toString().length(),"tcpout");
                  //  Message=Message+"\r\n";

                    //output.write(Message.getBytes());
                    PrintWriter pw=new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())),true);
                    pw.println(Message);
                    pw.flush();
                   // output.write(Message.getBytes());
                   // output.flush();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void WriteString(String Message){

        if(isClientStarted) {

            if(output!=null) {
                try {
                    StaticVariabes_div.log(output+"out stream "+Message +" len "+Message.toString().length(),"tcpout");
                    //  Message=Message+"\r\n";
                    cipher = encrypt(IV.getBytes(), encryptionKey.getBytes(),Message.getBytes());
                    output.write(cipher);
                    // output.write(Message.getBytes());
                     output.flush();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void WriteBytes(final byte Message[]){


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                if(isClientStarted) {

                    if(output!=null) {
                        try {
                           // StaticVariabes_div.log("out stream all"+Message.toString()+"len "+Message.toString().length(),"tcpout");
                            cipher = encrypt(IV.getBytes(), encryptionKey.getBytes(),Message);
                            output.write(cipher);
                            //  output.write(Message);
                            output.flush();
                            StaticVariabes_div.log("out write len"+cipher.length,"tcpout");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                super.onPostExecute(aVoid);
            }
        }.execute();


    }

    public static void WriteBytes_without_aes(final byte Message[]){


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                if(isClientStarted) {

                    if(output!=null) {
                        try {
                            StaticVariabes_div.log("out stream all"+Message.toString()+"len "+Message.toString().length(),"tcpout");
                            //cipher = encrypt(IV.getBytes(), encryptionKey.getBytes(),Message);
                           // output.write(cipher);
                            output.write(Message);
                            output.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                super.onPostExecute(aVoid);
            }
        }.execute();


    }
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

    //method to write data on tcp socket
    public static void tcpWrite_String(String data) {
        try {
            if (isClientStarted) {
                data=data+"\r\n";
                byte[] Data = data.getBytes();    //getting bytes from data string
                output.write(Data, 0, Data.length);    //writing data on stream
                output.flush();        //flushing data on to stream
                StaticVariabes_div.log("TAG", Data.length + " len Data Sent On Stream is : " + data);
            }

        } catch (SocketException s) {
            s.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static InputStream ins()
    {
        try {
            return clientSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
static class ClientThread implements Runnable {

    public void run() {

        try {
            android.os.Process.setThreadPriority(20);
           // clientSocket = new Socket(tcpAddress, Integer.parseInt(tcpPort));
            StaticVariabes_div.log("tcpAddress"+tcpAddress+"tcpPort"+tcpPort, TAG1);
            clientSocket = new Socket(tcpAddress,tcpPort);
            clientSocket.setKeepAlive(true);
            clientSocket.setTcpNoDelay(true);
            //clientSocket.setSoTimeout(100);
            input = clientSocket.getInputStream();
            output = clientSocket.getOutputStream();

            //br_input = new BufferedReader(new InputStreamReader(input));
            isClientStarted = true;
            isTcpThreadStarting = false;

           // output.write((StaticVariabes_div.loggeduser+StaticVariabes_div.loggedpwd).getBytes());
          //  output.flush();

            cipher = encrypt(IV.getBytes(), encryptionKey.getBytes(),(StaticVariabes_div.loggeduser+StaticVariabes_div.loggedpwd).getBytes());
            output.write(cipher);
            output.flush();
          //  mTcpTransfer.read(READ_LINE, "Connected", null);
          //  mTcpTransfer.read(ServStatus, "TRUE", null);


//            Thread.sleep(200);
            /*cipher = encrypt(IV.getBytes(), encryptionKey.getBytes(),("$115&").getBytes());
            output.write(cipher);
            output.flush();*/

        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
      //  byte[] buffer = new byte[1024*8];
        while (!Thread.currentThread().isInterrupted() && isClientStarted) {
            try {
                int readTo = input.read();
                if (readTo!=-1) {

                   byte[] tempBuf1 = new byte[48];
                  // Arrays.fill(tempBuf1, (byte) 0);
                   // byte[] tempBuf1 = new byte[12096];
                    byte[] tempBuf2;
                    tempBuf1[0] = (byte) readTo;
                    int dataToRead = input.read(tempBuf1,1,47);
                    dataToRead++;
                   // int howmanytimes = 0;
                    int posStart = -1;
                    int posEnd = -1;
                    int actualRec = 0;
                    String str = null;
                    byte   decryptedd[]=null;
                    if(dataToRead>1){

                        StaticVariabes_div.log(" decrypt read: dataToRead " , ""+dataToRead);
                       String  strtest=new String(tempBuf1);
                        StaticVariabes_div.log(" encrypt read: dataToRead " , ""+strtest);

                   // tempBuf1[dataToRead]='\0';


                       // char[] buffer1 = new char[dataToRead];
                       // howmanytimes++;
                      /*  for(int j = dataToRead; j >= 0; j--){

                           *//* if (tempBuf1[j] == (byte) 0x26)
                                posEnd = j;
                            if (posEnd != 0 && tempBuf1[j] == (byte) 0x24) {
                                posStart = j;
                                break;
                            }*//*

                            if (tempBuf1[j] == (byte) 0x23)
                                posEnd = j;
                            if (posEnd != 0 && tempBuf1[j] == (byte) 0x2A) {
                                posStart = j;
                                break;
                            }

                            if (tempBuf1[j] == (byte) 0x40)
                                posEnd = j;
                            if (posEnd != 0 && tempBuf1[j] == (byte) 0x26) {
                                posStart = j;
                                break;
                            }

                            if (tempBuf1[j] == (byte) 0x24)
                                posEnd = j;
                            if (posEnd != 0 && tempBuf1[j] == (byte) 0x2A) {
                                posStart = j;
                                break;
                            }

                            if (tempBuf1[j] == (byte) 0x3A)
                                posEnd = j;
                            if (posEnd != 0 && tempBuf1[j] == (byte) 0x2A) {
                                posStart = j;
                                break;
                            }

                            if (tempBuf1[j] == (byte) 0x24)
                                posEnd = j;
                            if (posEnd != 0 && tempBuf1[j] == (byte) 0x21) {
                                posStart = j;
                                break;
                            }

                            if (tempBuf1[j] == (byte) 0x29)
                                posEnd = j;
                            if (posEnd != 0 && tempBuf1[j] == (byte) 0x28) {
                                posStart = j;
                                break;
                            }

                            if (tempBuf1[j] == (byte) 0x3E)
                                posEnd = j;
                            if (posEnd != 0 && tempBuf1[j] == (byte) 0x3C) {
                                posStart = j;
                                break;
                            }


                       *//*     if (tempBuf1[j] == (byte) 0x5D)
                                posEnd = j;
                           if (posEnd != 0 && tempBuf1[j] == (byte) 0x5B) {
                           // if (tempBuf1[j] == (byte) 0x5B) {
                                posStart = j;
                                break;
                            }*//*

                          //0x26  &((RL.length()-1))=='@' 0x40

                        }*/
                       // if(posStart != -1 && posEnd != -1){
                        //    actualRec = 1 + (posEnd - posStart);
                            tempBuf2 = new byte[dataToRead];
                            System.arraycopy(tempBuf1, 0, tempBuf2, 0, dataToRead);

                            if (dataToRead < 28) {
                               // String str = new String(tempBuf2, 0, dataToRead);
                              //  StaticVariabes_div.log("msg"+str,TAG1);
                               // mTcpTransfer.read(READ_LINE, str, null);

                                try {
                                     decryptedd = decrypt(IV.getBytes(), encryptionKey.getBytes() ,tempBuf2);
                                     str=new String(decryptedd);
                                    StaticVariabes_div.log(str.length()+" decrypt read: " , str);

                               // str="test";

                               if(str.equals("*ERRUSER#")){
                                    mTcpTransfer.read(ERRUSER, "TRUE", null);
                                    StaticVariabes_div.log("user error"+str, TAG1);
                                }else if(str.equals("*MAXUSER#")){
                                    mTcpTransfer.read(MAXUSER, "TRUE", null);
                                    StaticVariabes_div.log("max user"+str, TAG1);
                                }else if(str.equals("*null#")){
                                    Tcp_con.stopClient();
                                    mTcpTransfer.read(ServStatus, "FALSE", null);
                                    StaticVariabes_div.log("inside null"+str, TAG1);
                                }else if(str.startsWith("*")&&str.endsWith("_")){
                                   //Tcp_con.stopClient();
                                   int inend=str.indexOf("_");

                                   String strverdb=str.substring(1,inend);
                                   if(strverdb.equals("NULL")) {
                                       StaticVariabes_div.log("verdb null "+strverdb, TAG1);
                                   }else {
                                       StaticVariabes_div.House_dbver_num_gateway =strverdb;
                                           mTcpTransfer.read(UPDATE, "TRUE", null);
                                       StaticVariabes_div.log("verdb"+strverdb, TAG1);
                                   }

                               }else if(str.startsWith("*OK#")){

                                   mTcpTransfer.read(ServStatus, "TRUE", null);
                                   StaticVariabes_div.log("inside null"+str, TAG1);
                                   //Tcp_con.WriteBytes(("$115&").getBytes());
                               }else{

                                       mTcpTransfer.read(READ_LINE, str, null);
                                       //hndlr.obtainMessage(ServStatus, 0, 0, "TRUE").sendToTarget();
                                       StaticVariabes_div.log("inside ok"+str, TAG1);

                                     }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                                //str="test";
                               try {
                                       decryptedd = decrypt(IV.getBytes(), encryptionKey.getBytes() ,tempBuf2);
                                    str=new String(decryptedd);

                                   StaticVariabes_div.log(str.length()+" decrypt read byte: " , str);

                                   mTcpTransfer.read(READ_BYTE, null, decryptedd);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                   stopClient();
                                  // Thread.sleep(300);
                                   //startClient();
                                }

                               // String str = new String(tempBuf2, 0, actualRec);
                               // StaticVariabes_div.log("byte msg"+str,TAG1);
                                //mTcpTransfer.read(READ_LINE, str, null);

                                //if (tempBuf1[0] == (byte) 0x2A) {

                               // }else{
                               //     mTcpTransfer.read(READ_TIMER, null, tempBuf2);
                                //}
                            }
                      //  }
                    }
                    else
                    {
                        isClientStarted = false;
                        Thread.currentThread().interrupt();
                    }
                }else if (readTo == -1) {
                    StaticVariabes_div.log("minus 1 ", TAG1);
                    //tcpConnectionClose();
                   switchwifi3g();
                  //  startClient();
                    mTcpTransfer.read(ServStatus, "FALSE", null);
                }

                o=0;
            } catch(Exception e){

                e.printStackTrace();
                StaticVariabes_div.log("start in excep"+e.toString(), TAG1);

                //stopClient();
                String er = "";
               // if (e != null)// er = e.toString();
               if (!(e.toString().contains("java.net.SocketException"))) {
                   if(mTcpTransfer!=null)
                   mTcpTransfer.read(ServStatus, "FALSE", null);
                  // stopClient();
                  /* if(o<2) {
                       startClient();
                       StaticVariabes_div.log("start in excep", TAG1);
                       o++;
                   }*/

                }


                if(!(e.toString().contains("java.net.SocketTimeoutException"))){
                    StaticVariabes_div.log("test SocketTimeoutException", TAG1);
                }
            }
        }
        isClientStarted = false;
        isTcpThreadStarting = false;
    }
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
//***********************************************************************************
    //  SERVER CONNECTIONS
    //*************************************************************************************


    public static void serverdetailsfetch(Context contxt,String housename ){

        try {
            servadap = new ServerAdapter(contxt);
            ServerAdapter.OriginalDataBase = housename + ".db";
            servadap.open();

            Cursor curem = servadap.fetch_detdb(1);
            remoteconnection = (curem.getInt(5) == 1);

            remoteurl = curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_da));

            StaticVariabes_div.log("reg recv remoteconnection" + remoteconnection, TAG1);
            StaticVariabes_div.log("remoteurl" + remoteurl, TAG1);


                ipdb_rem = curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_ri));
                portdb_rem = curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_rp));
                StaticVariabes_div.log("dyanmic ip db" + ipdb_rem + "dyanmic port db" + portdb_rem, TAG1);


            ipdb = curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_i));
            portdb = curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_p));
            //	ssiddb=curem.getString(curem.getColumnIndexOrThrow(Aa_a.KEY_ss));

            // }

            wirelessport = curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_ea));
            timerportno = curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_eb));

            adminuser = curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_db));
            ssiddb = curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_ss));
            adminpwd = curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_dc));


            curem.close();
            servadap.close();

           /* if (wirelessport != null)
                StaticVariables.WIRELSESS_SERVER_PORT = Integer.parseInt(wirelessport);*/

            if (portdb != null)
                StaticVariables.MAIN_SERVER_PORT = Integer.parseInt(portdb);

    /*        if (adminuser != null)
                StaticVariables.UserName = adminuser;

            if (adminpwd != null)
                StaticVariables.Password = adminpwd;*/
          //  StaticVariables.UserName = StaticVariabes_div.loggeduser;
          //  StaticVariables.Password = StaticVariabes_div.loggeduser;

                    //  StaticVariabes_div.log("admin usr db" + adminuser + "admin pwd db" + adminpwd, TAG1);
            StaticVariabes_div.log("ssid db" + ssiddb, TAG1);

        }catch(Exception ep){

        }

    }

    //	RECIEVERS
    //******************************************************************

    public static int rssirec() {
        int result = 0;
        try {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifi_info = wm.getConnectionInfo();
            //String str2 = wifi_info.getSSID();
            result = WifiManager.calculateSignalLevel(wifi_info.getRssi(), 4);
            String rs = Integer.toString(result);
            // hndlr.obtainMessage(signallevel, 0, 0, rs).sendToTarget();
            mTcpTransfer.read(signallevel, rs, null);
            //StaticVariabes_div.log("rssi signal level" + rs, TAG1);

           if(tcpAddress!=null){
                //server_notreachable();
               // exec_shell_command();
            }

        } catch(NullPointerException ne){

        } catch(Exception e) {
            e.printStackTrace();
        }

        return result;
    }

public static void exec_shell_command(){
    try {
        String line;
//        String[] cmd = { "nc -w 1 "+tcpAddress+" "+tcpPort+" ;$?" };
        //String[] cmd = { "/system/bin/ping -c 1 -p "+tcpPort+" "+tcpAddress};
        Process p = Runtime.getRuntime().exec("ping -c 1 -p "+tcpPort+" "+tcpAddress );
        BufferedReader in =
                new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            Log.d("line",line);
        }
        in.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }


   /* try {
        Process process = Runtime.getRuntime().exec("/system/bin/ps");
        InputStreamReader reader = new InputStreamReader(process.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(reader);
        int numRead;
        char[] buffer = new char[5000];
        StringBuffer commandOutput = new StringBuffer();
        while ((numRead = bufferedReader.read(buffer)) > 0) {
            commandOutput.append(buffer, 0, numRead);
        }
        bufferedReader.close();
        process.waitFor();

        return commandOutput.toString();
    } catch (IOException e) {
        throw new RuntimeException(e);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }*/
}
    public static boolean server_notreachable(){
        boolean status=false;
        try
        {
            StaticVariabes_div.log("Sending Ping Request to " , tcpAddress+":"+tcpPort);

            InetAddress inet = InetAddress.getByName(tcpAddress);

            status = inet.isReachable(5000); //Timeout = 5000 milli seconds

            if (status)
            {
                StaticVariabes_div.log("Status "," Host is reachable");
            }
            else
            {
                StaticVariabes_div.log("Status "," Host is not reachable");
            }
        }
        catch (UnknownHostException e)
        {
            System.err.println("Host does not exists");
        }
        catch (IOException e)
        {
            System.err.println("Error in reaching the Host");
        }catch (Exception e)
        {
            System.err.println("Error in reaching the Host");
        }

        return  status;
    }

    public static Context context = null;

    public static void registerReceivers(Context ctx) {
        slastTypecc = -1;
        if (ctx != null) {
            context = ctx;
            recState = true;
            context.registerReceiver(mConnReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    public static void unregReceivers() {
        slastTypecc = -1;
        if (stacontxt != null) {
            if(mConnReceiver!=null) {
                context = stacontxt;
                context.unregisterReceiver(mConnReceiver);
                mConnReceiver=null;
            }
        }
    }

    private static BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            recState = true;

            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            WifiManager wifiManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            recState = true;

           // Log.d("ssid",ssid);
          //  Log.d("ssiddb",ssiddb);

            if(ssiddb!=null) {

                StaticVariabes_div.log("ssiddb"+ssiddb, TAG1);


                if (slastTypecc != 1 && activeNetInfo != null && activeNetInfo.isConnected() && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI && ssid.contains(ssiddb) || ssid.contains(ssiddb + "_ext1") || ssid.contains(ssiddb + "_ext2") || ssid.contains(ssiddb + "_ext3")
                        || ssid.contains(ssiddb + "_ext4") || ssid.contains(ssiddb + "_ext5")) {

                    slastTypecc = 1;
                    //StaticVariables.RemoteConnectionIP=null;
                    sl = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 4);
                    String res = Integer.toString(sl);

                    // hndlr.obtainMessage(signallevel, 0, 0, res).sendToTarget();
                    // StaticVariabes_div.log("local wifi true"+ssid, TAG1);
                    // hndlr.obtainMessage(Remote, 0, 0, "FALSE").sendToTarget();

                    mTcpTransfer.read(signallevel, res, null);
                    mTcpTransfer.read(NetwrkType, "FALSE", null);
                    tcpAddress = ipdb;
                    tcpPort = Integer.parseInt(portdb);

                    // StaticVariables.Host=tcpAddress;
                    //  StaticVariables.Port=tcpPort;

                    //Http_Connect.Urlconip_port = tcpAddress;
                    //HttpCon_GatewaySetting.Urlconip_port = tcpAddress;

                    // Http_Connect.Urlconip_port=tcpAddress+":"+"8080";

                    if (clientSocket != null) {
                        if (switchwifi3g()) {
                            mTcpTransfer.read(ServStatus, "FALSE", null);
                            // hndlr.obtainMessage(ServStatus, 0, 0, "FALSE").sendToTarget();
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {

                                e.printStackTrace();
                            }

                            StaticVariabes_div.log("local wifi conn", TAG1);
                            startClient();
                        }
                    } else {
                        startClient();
                    }

                } else if (slastTypecc != 2 && activeNetInfo != null && activeNetInfo.isConnected() && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI && !((ssid.contains(ssiddb))
                        || ssid.contains(ssiddb + "_ext1") || ssid.contains(ssiddb + "_ext2") || ssid.contains(ssiddb + "_ext3")
                        || ssid.contains(ssiddb + "_ext4") || ssid.contains(ssiddb + "_ext5"))) {
                    slastTypecc = 2;
                    sl = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 4);
                    String res = Integer.toString(sl);
                    // hndlr.obtainMessage(signallevel, 0, 0, res).sendToTarget();
                    //  hndlr.obtainMessage(Remote, 0, 0, "TRUE").sendToTarget();

                    StaticVariabes_div.log(ssid+"remote wifi db"+ssiddb, TAG1);

                    mTcpTransfer.read(signallevel, res, null);
                    mTcpTransfer.read(NetwrkType, "TRUE", null);
                    StaticVariabes_div.log("remote wifi true", TAG1);

                    Thread th = new Thread() {
                        public void run() {
                            connect();
                            // StaticVariabes_div.log("remote wifi true conn", TAG1);
                            // StaticVariables.RemoteConnectionIP=ipdb_rem;
                            tcpAddress = ipdb_rem;

                            //HttpConnections.Urlconip_port=tcpAddress+":"+"9957";
                            // Http_Connect.Urlconip_port=tcpAddress+":"+timerportno;
                            // Http_Connect.Urlconip_port=tcpAddress+":"+portdb_rem;

                            // Http_Connect.Urlconip_port=tcpAddress;

                           // Http_Connect.Urlconip_port = tcpAddress + ":" + timerportno;
                          //  HttpCon_GatewaySetting.Urlconip_port = tcpAddress + ":" + timerportno;

                            //Http_Connect.Urlconip_port=tcpAddress+":"+"8080";

                            // tcpAddress = "192.168.1.7";
                            if (portdb_rem != null) {
                                try {
                                    tcpPort = Integer.parseInt(portdb_rem);
                                } catch (Exception ex) {
                                    StaticVariabes_div.log("remote wifi true conn pot excep" + " remportdb" + portdb_rem, TAG1);
                                }

                                //  StaticVariables.Host=tcpAddress;
                                //  StaticVariables.Port=tcpPort;

                                if (clientSocket != null) {
                                    if (switchwifi3g()) {
                                        // hndlr.obtainMessage(ServStatus, 0, 0, "FALSE").sendToTarget();
                                        mTcpTransfer.read(ServStatus, "FALSE", null);
                                        try {
                                            Thread.sleep(500);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        StaticVariabes_div.log("remote wifi true conn tcp not null", TAG1);

                                        startClient();
                                    }
                                } else {
                                    StaticVariabes_div.log("remote wifi true conn tcp null", TAG1);
                                    startClient();
                                }

                            } else {
                                StaticVariabes_div.log("remote wifi true conn url null", TAG1);
                                startClient();
                            }


                        }
                    };
                    th.start();

                } else if (slastTypecc != 3 && activeNetInfo != null && activeNetInfo.isConnected() && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {//&& TimerServices.mobile_data) {
                    slastTypecc = 3;
                    StaticVariabes_div.log("inside 3G TRUE ", TAG1);
                    // hndlr.obtainMessage(Remote, 0, 0, "TRUE3G").sendToTarget();


                    mTcpTransfer.read(NetwrkType, "TRUE3G", null);

                    Thread th = new Thread() {

                        public void run() {
                            StaticVariabes_div.log("inside connect 3G TRUE ", TAG1);
                            connect();
                            // StaticVariables.RemoteConnectionIP=ipdb_rem;
                            tcpAddress = ipdb_rem;

                            //HttpConnections.Urlconip_port=tcpAddress+":"+"9957";
                            //Http_Connect.Urlconip_port=tcpAddress+":"+timerportno;
                            // Http_Connect.Urlconip_port=tcpAddress+":"+portdb_rem;

                            // Http_Connect.Urlconip_port=tcpAddress;
                            //Http_Connect.Urlconip_port = tcpAddress + ":" + timerportno;
                            //HttpCon_GatewaySetting.Urlconip_port = tcpAddress + ":" + timerportno;
                            // Http_Connect.Urlconip_port=tcpAddress+":"+"8080";

                            if (portdb_rem != null) {


                                try {
                                    tcpPort = Integer.parseInt(portdb_rem);

                                    //  StaticVariables.Host=tcpAddress;
                                    //   StaticVariables.Port=tcpPort;

                                    if (clientSocket != null && !Tcp_con.isClientStarted) {
                                        if (switchwifi3g()) {
                                            mTcpTransfer.read(ServStatus, "FALSE", null);
                                            //  hndlr.obtainMessage(ServStatus, 0, 0, "FALSE").sendToTarget();
                                            try {
                                                Thread.sleep(500);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            StaticVariabes_div.log("3G true socket not  null conn to server", TAG1);

                                            startClient();
                                        }
                                    } else {

                                        StaticVariabes_div.log("3G true socket null conn to server", TAG1);
                                        startClient();
                                    }


                                } catch (Exception ex) {
                                    StaticVariabes_div.log("3G true exception  conn to server", TAG1);
                                }

                            } else {
                                StaticVariabes_div.log("3G true socket null conn to server", TAG1);
                                startClient();

                            }


                        }
                    };
                    th.start();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } else if (slastTypecc != 0 && activeNetInfo == null) {
                    slastTypecc = 0;
                    if (isClientStarted) {
                        //hndlr.obtainMessage(ServStatus, 0, 0, "FALSE").sendToTarget();
                        // hndlr.obtainMessage(Remote, 0, 0, "FALSE").sendToTarget();

                        mTcpTransfer.read(ServStatus, "FALSE", null);
                        mTcpTransfer.read(NetwrkType, "FALSE", null);

                        //Cc.tcpConnectionClose();
                        //Tcp_Cc.stopClient();
                        stopClient();
                    }

                    if (mTcpTransfer != null) {
                        mTcpTransfer.read(ServStatus, "FALSE", null);
                        mTcpTransfer.read(NetwrkType, "NONET", null);
                    }

                    // hndlr.obtainMessage(ServStatus, 0, 0,"FALSE").sendToTarget();
                    // hndlr.obtainMessage(Remote, 0, 0, "NONET").sendToTarget();

                    // HttpConnections.Urlconip_port="nonet";
                    Toast.makeText(context.getApplicationContext(), "Please make sure your Network Connection is ON ", Toast.LENGTH_SHORT).show();
                }
            }else{

                Toast.makeText(context.getApplicationContext(), "SSID Not Available Enter Manually or Download Home again", Toast.LENGTH_LONG).show();

            }
        }
    };

    static boolean switchwifi3g() {

        try {
            StaticVariabes_div.log("INSIDE SWITCHING", TAG1);
            StaticVariabes_div.log("switchwifi3g closeswitch", TAG1);

          //  Tcp_con.stopClient();



            try {
                if(clientSocket!=null)
                {
                    if(clientSocket.isConnected())
                        try {
                            clientSocket.close();

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                }
                try {
                    if(clientThread!=null)
                    {
                        clientThread.interrupt();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                isClientStarted = false;
                isTcpThreadStarting = false;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }



          /*  if(mReadThread!=null){
                mReadThread.interrupt();
                mReadThread=null;
            }
            if(tcpSocket!=null){
                if(tcpBufferedReader!=null)tcpBufferedReader.close();
                if(tcpInputStream!=null)tcpInputStream.close();
                if(tcpOutputStream!=null){
                    tcpOutputStream.flush();
                    tcpOutputStream.close();
                }
                tcpSocket.close();
                tcpConnected = false;
                tcpSocket=null;
                StaticVariabes_div.log("all closed success", TAG1);
                return true;
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage().toString();
            return false;
        }
        return false;
    }

    static void connect()
    {
        StaticVariabes_div.log("inside connect to url", TAG1);
        try {
            fetchremurl(stacontxt, StaticVariabes_div.housename);
        }catch(Exception ef){
            ef.printStackTrace();
        }
        //String str1 = "http://homeautomation.sudteck.com/HAremoteip.php?q="+str;
        // String str1 = "http://www.einucleus.in/rbremoteip.php?q="+str;
        // String str1="http://www.sudteck.com/remoteip.php?q=100100";
        String str1= remoteurl;
        String str2 = Fetchremoteip(str1);
        StaticVariabes_div.log("inside remote ip fetch from url", TAG1);
        if(str2!=null){
            if(str2.equals("server error")){
		    		/*runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(),
				                    "server not reachable", Toast.LENGTH_LONG).show();
						}
					});*/
                StaticVariabes_div.log("inside remote ip fetch from url Server Error", TAG1);

            }else{
                StringTokenizer tokens = new StringTokenizer(str2, ",");
                remipweb = tokens.nextToken();
                String porttemp=tokens.nextToken();
                String[] separated = porttemp.split(";");
                remportweb=separated[0];
                StaticVariabes_div.log("ip rem json"+remipweb+"port rem json"+remportweb,TAG1);

                if(remipweb!=null&&remportweb!=null){
                    ipdb_rem=remipweb;portdb_rem=remportweb;
                    insertremip(stacontxt, StaticVariabes_div.housename);
                }

            }

        }
    }

    //***********************************************************************************
    //to fetch remote ip
    //************************************************************************************
    public static String Fetchremoteip(String str)
    {
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost((str));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("IP", null));
            nameValuePairs.add(new BasicNameValuePair("PORT", null));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }catch(Exception e){
            Ipadress_Port="server error";
        }
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line="0";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result1=sb.toString();
            StaticVariabes_div.log("inside remote url json result",TAG1);
        }catch(Exception e){
            e.printStackTrace();
            Ipadress_Port="server error";
        }
        try{
            if(result1!=null){
                JSONArray jArray = new JSONArray(result1);
                JSONObject json_data=null;
                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    Ipadress_Port = json_data.getString("IP");
                    Ipadress_Port += ",";
                    Ipadress_Port += json_data.getString("PORT");
                }
            }else{
                Ipadress_Port="server error";
            }
        }catch(JSONException e1){
            Ipadress_Port="server error";
        }catch (ParseException e1){
            Ipadress_Port="server error";
        }
        return Ipadress_Port;
    }



    public static void insertremip(Context contxt,String housename){
        StaticVariabes_div.log("inside insert remote ip/port housename"+housename,TAG1);
        servadap = new ServerAdapter(contxt);
        ServerAdapter.OriginalDataBase=housename+".db";
        try {
            servadap.open();
            servadap.updaterirp(1, remipweb, remportweb);
            servadap.close();
        }catch (Exception ed){
            ed.printStackTrace();
        }
    }

    public static void fetchremurl(Context contxt,String housename){
        servadap = new ServerAdapter(contxt);
        ServerAdapter.OriginalDataBase=housename+".db";
        servadap.open();
        Cursor curem= servadap.fetch_detdb(1);
        remoteurl=curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_da));
        servadap.close();
    }

}
