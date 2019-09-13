package edisonbro.com.edisonbroautomation.connectionswirelessir;


/**
 *  FILENAME: Tcp_dwn_config.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Singleton class used  to connect to gateway through tcp.
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.databasewired.LocalDatabaseHelper;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;

/**
 * Created by Divya on 1/30/2018.
 */

public class Tcp_dwn_config {

    public static Socket tcpSocket=null;
    public static InputStream tcpInputStream=null;
    private static OutputStream tcpOutputStream=null;
    public static BufferedReader tcpBufferedReader=null;

    public static Handler tcpHandler;
    static ReadThread tcpReadThread=null;

    final static int READ_LINE=1 ,READ_BYTE=2,EXCEPTION=3,TCP_LOST=4,BT_LOST=5,TCP_CONNECTED=6,ERR_USER=7,MAX_USER=8,
            READ_TIMER=9;
    public static String tcpHost=null;
    public static int tcpPort=0;
    final static int connectionTimeout=3000;
    public static volatile boolean tcpDownloading=false, tcpConnected=false,tcpReadLineEnabled=false,upload_send_sts=false;
    public static boolean tcpRunning=false, TempDownoad_Completed = false,	MainDownoad_Completed = false;
    static String PACKAGE_NAME=null;
    static boolean isConnected=false;
    static String DataRead=null;
    static String SERVER_PASSWORD=null;;
    //static String SERVER_PASSWORD="12345678";
    static Context dbContext=null;
    static boolean isExceptionOccured=false;
    static byte cipher[]=null;
    //constructor
    public Tcp_dwn_config(Handler handler)
    {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //getting package name
        //PACKAGE_NAME= LocalDatabaseAdapter.mcontext.getPackageName();
        PACKAGE_NAME= LocalDatabaseHelper.mcontext.getPackageName();

        tcpHandler=handler;			//passing handler context
        //setting server password
        //SERVER_PASSWORD= StaticVariables.UserName+StaticVariables.Password ;
        //SERVER_PASSWORD="admin1234512345678";

        SERVER_PASSWORD= StaticVariabes_div.loggeduser+StaticVariabes_div.loggedpwd;

        if(tcpSocket==null)	{
            StaticVariables.printLog("TAG","socket is null");
            tcpConnectionOpen();	//opening connection if socket is freshly opened
        }
        else if(tcpSocket!=null)
        {
            StaticVariables.printLog("TAG","socket is not null test");
            if(!tcpSocket.isConnected()) {
                StaticVariables.printLog("TAG","socket not connected");
                tcpConnectionOpen();	//opening connection if socket is opened but server not connected
            }
            else
            {
                tcpConnectionClose();
                tcpConnectionOpen();
            }
        }

    }

    //tcp connection opening method
    public static  void tcpConnectionOpen()
    {
        try
        {

            StaticVariables.printLog("TCP","Host :"+tcpHost+" Port :"+tcpPort);

            tcpSocket=new Socket();		//initializing socket
            //trying to connect to server with ip and port passed and
            //will automatically timeout after 3 sec if unable to connect
            tcpSocket.connect(new InetSocketAddress(tcpHost, tcpPort), connectionTimeout);

            tcpInputStream=tcpSocket.getInputStream();		//getting inputstream
            tcpOutputStream=tcpSocket.getOutputStream();	//getting outputstream
            //getting bufferedReader
            tcpBufferedReader=new BufferedReader(new InputStreamReader(tcpInputStream));

            tcpConnected=true;
            tcpRunning=true;
            tcpReadLineEnabled=true;

            if(tcpReadThread==null && tcpConnected){
                StaticVariables.printLog("TCP start read","Host :"+tcpHost+" Port :"+tcpPort);
                //starting reading thread
                tcpReadThread=new ReadThread();
                tcpReadThread.start();

                if(tcpHandler!=null)
                    tcpHandler.obtainMessage(TCP_CONNECTED,"SERVER CONNECTED").sendToTarget();

            }
        }catch(SocketException s){
            isExceptionOccured=true;
            s.printStackTrace();
            if(tcpHandler!=null)
                tcpHandler.obtainMessage(EXCEPTION,"Network Unreachable").sendToTarget();
        }
        catch(Exception e)	{
            e.printStackTrace();
            isExceptionOccured=true;
        }
    }


    //method to write data on tcp socket
    public static void tcpWrite(String data)
    {
        try
        {
            if(tcpConnected){
                byte[] Data=data.getBytes();	//getting bytes from data string
                tcpOutputStream.write(Data, 0, Data.length);	//writing data on stream
                tcpOutputStream.flush();		//flushing data on to stream
                StaticVariabes_div.log("TAG",Data.length+" len Data Sent On Stream is : "+data);
            }

        }catch(SocketException s){
            s.printStackTrace();
            if(tcpHandler!=null)
                tcpHandler.obtainMessage(EXCEPTION,"Network Unreachable").sendToTarget();
            //closing tcp connection
            tcpConnectionClose();
        }catch(Exception e)	{
            e.printStackTrace();
        }
    }

    //overloaded method .write data on tcp socket
    public static void tcpWrite(byte[] data, boolean flush)
    {
        try {
            if (tcpConnected) {
                tcpOutputStream.write(data, 0, data.length);

                if (flush)
                    tcpOutputStream.flush();
            }
        }catch(SocketException s){
            s.printStackTrace();
            if(tcpHandler!=null)
                tcpHandler.obtainMessage(EXCEPTION,"Network Unreachable").sendToTarget();
            //closing tcp connection
            tcpConnectionClose();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    //method to write data on tcp socket
    public static void tcpWrite(String data,Context mcontext)
    {
        try
        {
            dbContext=mcontext;

            if(tcpConnected){
                byte[] Data=data.getBytes();	//getting bytes from data string
                tcpOutputStream.write(Data, 0, Data.length);	//writing data on stream
                tcpOutputStream.flush();		//flushing data on to stream
                StaticVariables.printLog("TAG","Data Sent On Stream is : "+data);
            }

        }catch(SocketException s){
            s.printStackTrace();
            if(tcpHandler!=null)
                tcpHandler.obtainMessage(EXCEPTION,"Network Unreachable").sendToTarget();
            //closing tcp connection
            tcpConnectionClose();
        }catch(Exception e)	{
            e.printStackTrace();
        }
    }


    //method to write data on tcp socket
    public static void tcpWrite_aes(String data)
    {
        byte[] decryptedd=null;
        try
        {
            if(tcpConnected){
                byte[] Data=data.getBytes();	//getting bytes from data string
                cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),Data);

                tcpOutputStream.write(cipher, 0, cipher.length);	//writing data on stream
                tcpOutputStream.flush();		//flushing data on to stream
                StaticVariabes_div.log("TAG "+cipher.length,Data.length+" len Data Sent On Stream is : "+data);
                try {

                    decryptedd = StaticVariabes_div.decrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(), cipher);
                    DataRead = new String(decryptedd);

                    StaticVariabes_div.log(" TAG decrypt read: ", DataRead);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }catch(SocketException s){
            s.printStackTrace();
            if(tcpHandler!=null)
                tcpHandler.obtainMessage(EXCEPTION,"Network Unreachable").sendToTarget();
            //closing tcp connection
            tcpConnectionClose();
        }catch(Exception e)	{
            e.printStackTrace();
        }
    }

    //overloaded method .write data on tcp socket
    public static void tcpWrite_aes(byte[] data, boolean flush)
    {
        try {
            if (tcpConnected) {
                cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),data);

                tcpOutputStream.write(cipher, 0, cipher.length);

                if (flush)
                    tcpOutputStream.flush();
            }
        }catch(SocketException s){
            s.printStackTrace();
            if(tcpHandler!=null)
                tcpHandler.obtainMessage(EXCEPTION,"Network Unreachable").sendToTarget();
            //closing tcp connection
            tcpConnectionClose();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    //method to write data on tcp socket
    public static void tcpWrite_aes(String data,Context mcontext)
    {
        try
        {
            dbContext=mcontext;

            if(tcpConnected){
                byte[] Data=data.getBytes();	//getting bytes from data string
                cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),Data);

                tcpOutputStream.write(cipher, 0, cipher.length);	//writing data on stream
                tcpOutputStream.flush();		//flushing data on to stream
                StaticVariables.printLog("TAG","Data Sent On Stream is : "+data);
            }

        }catch(SocketException s){
            s.printStackTrace();
            if(tcpHandler!=null)
                tcpHandler.obtainMessage(EXCEPTION,"Network Unreachable").sendToTarget();
            //closing tcp connection
            tcpConnectionClose();
        }catch(Exception e)	{
            e.printStackTrace();
        }
    }


    //closing tcp connections
    public static void tcpConnectionClose()
    {
        try
        {
            //sending response to activity that connection lost with server
            if(tcpHandler!=null)
                tcpHandler.obtainMessage(TCP_LOST, "Tcp Connection Disconnected").sendToTarget();

            //closing inputstream
            if(tcpInputStream!=null){
                tcpInputStream.close();
                tcpInputStream=null;
                StaticVariables.printLog("TAG","LOG2");
            }

            //closing outputstream
            if(tcpOutputStream!=null){
                tcpOutputStream.flush();
                tcpOutputStream.close();
                tcpOutputStream=null;
                StaticVariables.printLog("TAG","LOG3");
            }

            try{
                if(tcpSocket!=null)
                    tcpSocket.shutdownOutput();
            }catch(Exception e){
                //e.printStackTrace();
                StaticVariables.printLog("TAG",e.getMessage());
            }

            //closing reading thread
            if(tcpReadThread!=null)	{
                tcpReadThread.interrupt();
                tcpReadThread=null;

                if(tcpBufferedReader!=null){
                    tcpBufferedReader.close();
                    tcpBufferedReader=null;
                }

                tcpRunning=false;
                tcpReadLineEnabled=false;
                tcpDownloading=false;
                tcpConnected=false;
                isConnected=false;
                StaticVariables.printLog("TAG","LOG4");
            }


            //closing socket
            if(tcpSocket!=null)	{
                tcpSocket.close();
                tcpSocket=null;
                StaticVariables.printLog("TAG","LOG1");
            }

        }catch(Exception e)
        {
            e.printStackTrace();
            StaticVariables.printLog("TAG","exp");
        }

    }



    // Download Task i.e downloading db at sd card
    private static void downloadDatabase(final int lenghtOfFile,String HOUSE_DATABASE)
    {
        String SD_PATH= Environment.getExternalStorageDirectory().getPath() + "/HomeAutomationDB";
        StaticVariables.printLog("TAG","SD Path : "+SD_PATH);
        try{
            File sdFolder=new File(SD_PATH);
            if(!sdFolder.exists()){
                sdFolder.mkdir();
            }

            if(sdFolder.isDirectory()){
                //HOUSE_DATABASE=StaticVariables.HOUSE_NAME;
                String PATH=SD_PATH+"/"+HOUSE_DATABASE+".db";	//SD card dir to download db temporarly
                StaticVariables.printLog("TAG","HOUSE_DATABASE : "+HOUSE_DATABASE);
                File sdDB=new File(PATH);

                try{

                    boolean hasPermission = (ContextCompat.checkSelfPermission(dbContext,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPermission) {
                        StaticVariables.printLog("TAG","hasPermission : "+false);
                        //	ActivityCompat.requestPermissions(dbContext,
                        //			new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        //			REQUEST_WRITE_STORAGE);
                    }else{
                        StaticVariables.printLog("TAG","hasPermission : true "+hasPermission);
                    }
                    sdDB.createNewFile();
                }catch(IOException i){
                    i.printStackTrace();
                }

                FileOutputStream fout = new FileOutputStream(PATH);
                byte data[] = new byte[1024];

                int count=0;
                long totalBytes=0;
                while (count >=0) {
                    count = Tcp_dwn_config.tcpInputStream.read(data);
                    StaticVariables.printLog("TAG","total :"+totalBytes+" count : "+count);
                    if(count>=0)
                    {
                        StaticVariables.printLog("TAG","total :"+totalBytes+" count : "+count);
                        fout.write(data, 0, count);	//writing bytes to sd card file directory
                        totalBytes +=count;
                        StaticVariables.printLog("TAG","total :"+totalBytes+" count : "+count);

                        //checking if total bytes downloaded reach to file length
                        if(totalBytes==lenghtOfFile){
                            Tcp_dwn_config.tcpReadLineEnabled=true;
                            Tcp_dwn_config.tcpDownloading=false;
                            totalBytes=0;
                            TempDownoad_Completed=true;
                            StaticVariables.printLog("Databse Downloaded","Download Completed");

                            break;
                        }else if(totalBytes>lenghtOfFile){
                            Tcp_dwn_config.tcpReadLineEnabled=true;
                            Tcp_dwn_config.tcpDownloading=false;
                            totalBytes=0;
                            TempDownoad_Completed=false;
                            StaticVariables.printLog("Databse Downloaded","Download failed");
                            break;
                        }
                    }

                }
                //flushing and closing file output stream
                fout.flush();
                fout.close();

                File file=new File(PATH);
                long pathFileLength=file.length();
                StaticVariables.printLog("","Length of Fil on SD:"+pathFileLength);

                boolean isDatabaseIntegrated=false;

                try{

                    //checking if db is of wireless type
                    if(HOUSE_DATABASE.endsWith("_WLS")){
                        StaticVariables.printLog("","inside wls HOUSE_DATABASE:"+HOUSE_DATABASE);

                        StaticVariables.printLog("","inside wls HOUSE:"+SD_PATH+"/"+HOUSE_DATABASE+".db");

                        try {

                            //checking  database integrity
                            //	WirelessConfigurationAdapter houseDatabase = new WirelessConfigurationAdapter(dbContext, SD_PATH + "/", HOUSE_DATABASE + ".db");
                            //	houseDatabase.open();

                            //isDatabaseIntegrated = houseDatabase.isDataBaseCorrect();

                            //	StaticVariables.printLog("", "inside wls isDatabaseIntegrated:" + isDatabaseIntegrated);

                            //	houseDatabase.close();

                            isDatabaseIntegrated =true;
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        StaticVariables.printLog("",StaticVariabes_div.housename+"inside wired HOUSE_DATABASE:"+HOUSE_DATABASE);
                        //checking  database integrity
                        HouseConfigurationAdapter houseDatabase=new HouseConfigurationAdapter(dbContext,SD_PATH+"/",HOUSE_DATABASE+".db");
                        houseDatabase.open();

                        isDatabaseIntegrated=houseDatabase.isDataBaseCorrect();

                        houseDatabase.close();

                    }
                }catch(Exception e){
                    e.printStackTrace();
                    StaticVariables.printLog("DB DOWNLOAD ","exception :"+e.toString());
                }

                StaticVariables.printLog("DB DOWNLOAD ","is Db Integerated :"+isDatabaseIntegrated);
                StaticVariables.printLog("DB DOWNLOAD ","TempDownload_Completed :"+TempDownoad_Completed);
                StaticVariables.printLog("DB DOWNLOAD ","pathFileLength :"+pathFileLength);
                StaticVariables.printLog("DB DOWNLOAD ","lenghtOfFile :"+lenghtOfFile);



                // start copying db to main directory
                if(TempDownoad_Completed && (pathFileLength==lenghtOfFile) && isDatabaseIntegrated){
                    StaticVariables.printLog("","inside  TempDownoad_Completed:");
                    TempDownoad_Completed=false;
                    copyDataBase(HOUSE_DATABASE);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    //copying database from sd card to main database directory
    public static void copyDataBase(String HOUSE_DATABASE)
    {

        String ORIGINAL_PATH="/data/data/"+PACKAGE_NAME+"/databases";
        String SD_PATH=Environment.getExternalStorageDirectory().getPath() + "/HomeAutomationDB";
        try{

            File DbFolder=new File(ORIGINAL_PATH);

            if(DbFolder.isDirectory()){
                //HOUSE_DATABASE=StaticVariables.HOUSE_NAME;
                String DatabseName=HOUSE_DATABASE+".db";
                String PATH=ORIGINAL_PATH+"/"+DatabseName;
                File sdDB=new File(PATH);
                try{
                    sdDB.createNewFile();
                }catch(IOException i){
                    i.printStackTrace();
                }

                File sdDb_Path=new File(SD_PATH+"/"+DatabseName);
                long lenghtOfFile=sdDb_Path.length();
                StaticVariables.printLog("","sd file length : "+lenghtOfFile+" file destination:"+ORIGINAL_PATH);

                FileInputStream fin=new FileInputStream(sdDb_Path);
                FileOutputStream fout = new FileOutputStream(PATH);
                byte data[] = new byte[1024];

                int count=0;
                long totalBytes=0;
                while (count >=0) {
                    count = fin.read(data);
                    if(count>=0)
                    {
                        totalBytes += count;
                        StaticVariables.printLog("","total count coppying :"+totalBytes);
                        fout.write(data, 0, count);
                        if(totalBytes==lenghtOfFile){
                            MainDownoad_Completed=true;
                            File file = new File(String.valueOf(sdDb_Path));
                            boolean deleted = file.delete();

                            //Toast.makeText(,"delete"+deleted,Toast.LENGTH_SHORT).show();
                            StaticVariables.printLog("msg"," delete"+deleted+"main down"+MainDownoad_Completed);
                            totalBytes=0;
                            StaticVariables.printLog("Main Download","Main Download Completed");
                            break;
                        }else if(totalBytes>lenghtOfFile){
                            MainDownoad_Completed=false;
                            totalBytes=0;
                            StaticVariables.printLog("Main Download","Main Download failed");
                            break;
                        }

                    }

                }
                //flushing outputstream
                fout.flush();
                //closing outputstream
                fout.close();
                //closing inputstream
                fin.close();

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //upload database to server
    public  static void uploadDatabse(String DatabseName){

        try{
            //path for current database to be upload
            //String path =Environment.getExternalStorageDirectory().getPath() + "/demo123/home6_WLS.db";
            String path ="/data/data/"+PACKAGE_NAME+"/databases/"+DatabseName+".db" ;

            StaticVariables.printLog("UPLOAD PATH ","Path UPLOAD : "+path);
            File file = new File(path);
            int flen = (int) file.length(); // getting db file length
            delay(500);	//provide delay of 200 sec
            tcpWrite(""+flen+"\r\n");	//writing file length on output stream
            delay(1000);	//provide delay of 1000 sec

            FileInputStream fin = null;
            byte[] buf = new byte[flen];
            try {
                fin = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fin);
                bis.read(buf, 0, buf.length);
                BufferedOutputStream bout = new BufferedOutputStream(
                        tcpOutputStream);
                bout.write(buf, 0, buf.length);

                bout.flush();	//flushing out stream
                bis.close();	//closing buffered stream
                fin.close();	//closing file input stream

                upload_send_sts=true;

                StaticVariables.printLog("file size", "file size is : " + flen + " buffer size is : "
                        + buf.length);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //reading thread to handle tcp responses
    static class ReadThread extends Thread {
        public void run()
        {
            DataRead=null;

            while(tcpRunning)
            {
                //checking if stream have no data then comming out of loop
                if(tcpInputStream==null){
                    return;
                }
                else
                {
                    if(tcpReadLineEnabled)	//if data is to be read as line by line format
                    {
                       // StaticVariables.printLog("TAG","INSIDE READ THREAD");
                        try {
                            //Reading data from socket
                          //  DataRead=tcpBufferedReader.readLine();

                            int readTo = tcpInputStream.read();
                            if (readTo!=-1) {

                                byte[] tempBuf1 = new byte[4096];
                               // byte[] tempBuf1 = new byte[53248];
                                byte[] tempBuf2 = null;
                                tempBuf1[0] = (byte) readTo;
                                int dataToRead = tcpInputStream.read(tempBuf1, 1, 4095);
                                //int dataToRead = tcpInputStream.read(tempBuf1, 1, 53247);
                                dataToRead++;
                                String str = null;
                                byte decryptedd[] = null;
                                if (dataToRead > 1) {
                                    tempBuf2 = new byte[dataToRead];
                                    System.arraycopy(tempBuf1, 0, tempBuf2, 0, dataToRead);
                                    StaticVariables.printLog("TAG", "dataToRead" + dataToRead);
                                    try {
                                        decryptedd = StaticVariabes_div.decrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(), tempBuf2);
                                        DataRead = new String(decryptedd);

                                       // Log.d("decrypt read: ", DataRead);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                StaticVariables.printLog("DATA", "DATA FROM TCP STREAM : " + DataRead);


                                if (DataRead.startsWith("*") && DataRead.endsWith("#")) {
                                    String sub = DataRead.substring(1, DataRead.length() - 1);
                                    StaticVariables.printLog("DATA", "sub string : " + sub);

                                    if (sub.equals("ERRUSER")) {

                                        //sending response to activity
                                        if (tcpHandler != null)
                                            tcpHandler.obtainMessage(ERR_USER, DataRead).sendToTarget();

                                    } else if (sub.equals("MAXUSER")) {
                                        //sending response to activity
                                        if (tcpHandler != null)
                                            tcpHandler.obtainMessage(MAX_USER, DataRead).sendToTarget();

                                    } else if (sub.equals("null")) {

                                        StaticVariables.printLog("TAG", "null string from server");
                                        //closing connections if null came in stream
                                        tcpConnectionClose();

                                    }else if (sub.equals("OK")) {

                                        StaticVariables.printLog("TAG", "ok string from server");

                                        if(tcpHandler!=null)
                                            tcpHandler.obtainMessage(TCP_CONNECTED,"SERVER CONNECTED").sendToTarget();

                                    }
                                }

                                if (DataRead != null && DataRead.startsWith("$") && DataRead.endsWith("&")) {

                                    tcpReadLineEnabled = false;
                                    tcpDownloading = true;
                                    int fileLength = Integer.parseInt(DataRead.substring(1, DataRead.length() - 1));
                                    String HOUSE_DB_NAME = StaticVariables.HOUSE_DB_NAME;
                                    StaticVariables.printLog("TAG", "Current database to download w : " + HOUSE_DB_NAME);

                                    //starting download task
                                    downloadDatabase(fileLength, HOUSE_DB_NAME);

                                } else if (DataRead != null && DataRead.startsWith("$") && DataRead.endsWith(":")) {

                                    tcpReadLineEnabled = false;
                                    tcpDownloading = true;
                                    int fileLength = Integer.parseInt(DataRead.substring(1, DataRead.length() - 1));
                                    String HOUSE_DB_NAME = StaticVariables.HOUSE_DB_NAME;
                                    StaticVariables.printLog("TAG", "Current database to download wls : " + HOUSE_DB_NAME);

                                    //starting download task
                                    downloadDatabase(fileLength, HOUSE_DB_NAME);

                                } else if (DataRead != null && DataRead.startsWith("[") && DataRead.endsWith("]")) {

                                    if (tcpHandler != null) {
                                        tcpHandler.obtainMessage(READ_TIMER, DataRead).sendToTarget();

                                    }

                                } else {

                                    //sending response to activity
                                    if (tcpHandler != null) {
                                        tcpHandler.obtainMessage(READ_LINE, DataRead).sendToTarget();

                                    }

                                }
                            }
                        }catch(NullPointerException n){
                            tcpConnectionClose();	//closing connections
                            n.printStackTrace();
                        }catch(SocketException s){
                            s.printStackTrace();
                            tcpConnectionClose();	//closing connections
                        }catch (IOException e) {
                            tcpConnectionClose();	//closing connections
                            e.printStackTrace();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else if(tcpDownloading){	// if data download process is running
                        //StaticVariables.printLog("DOWNLOAD","Database is Downloading..");

                    }else 		// if data is to be read in byte format
                    {
                        try	{
                            int size=0;
                            byte[] buffer=new byte[26];
                            //reading data from socket
                            size=tcpInputStream.read(buffer);
                            if(size>0)
                            {
                                String ISclosed= (new String(buffer, 0, size)).trim();
                                StaticVariables.printLog("in byte read", "byte read size"+size);

                                if(ISclosed.equals("*null#")){

                                    StaticVariables.printLog("TAG2","Null string from server");

                                    //closing connections if null came in stream
                                    tcpConnectionClose();

                                }else{
                                    //sending response to activity
                                    if(tcpHandler!=null)
                                        tcpHandler.obtainMessage(READ_BYTE, size, -1,
                                                buffer).sendToTarget();
                                }
                            }
                        }catch(NullPointerException n){
                            tcpConnectionClose();	//closing connections
                            n.printStackTrace();
                        }catch(SocketException s){
                            s.printStackTrace();
                            tcpConnectionClose();	//closing connections
                        }catch (IOException e) {
                            tcpConnectionClose();	//closing connections
                            e.printStackTrace();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }


    //adding delay
    static void delay(long time)
    {
        try{
            Thread.sleep(time);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    //creating wifi broadcast reciever class
    public static class TcpBroadcastreciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean isWifi_Connected = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
            StaticVariables.printLog("RECIEVER","WIFI STATE CHANGED");
            if(isWifi_Connected){
                Thread t=new Thread(){
                    public void run(){
                        //checking if handler is not null
                        if(tcpHandler!=null){
                            //checking if tcp is not connected and socket is null
                            if(!tcpConnected && tcpSocket==null){
                                StaticVariables.printLog("RECIEVER","OPENING TCP CONNECTION");
                                //opening tcp connection
                                Tcp_dwn_config.tcpConnectionOpen();

                                if(tcpConnected && tcpSocket!=null){
                                    StaticVariables.printLog("RECIEVER","PASSWORD WIRTE");
                                    //writing server password
                                   // tcpWrite(SERVER_PASSWORD);
                                    byte cipher[]=null;
                                    try {
                                        cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),SERVER_PASSWORD.getBytes());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Tcp_dwn_config.tcpWrite(cipher,true);
                                }
                            }
                        }

                    }
                };t.start();

            }


        }

    }

    //static connecting method
    public static boolean EstablishConnection(final Handler myHanler){

        try{
            if(!tcpConnected && !isConnected){
                StaticVariabes_div.log("notcon"+SERVER_PASSWORD,"Host");
                //passing handler instance to TCP class
                new Tcp_dwn_config(myHanler);

                //waiting time for tcp connection 10 sec
                int counter1=1;
                while(counter1<=3)
                {
                    if(isExceptionOccured){
                        isExceptionOccured=false;
                        break;
                    }else{

                        counter1++;
                        int counter2=1;
                        while(counter2<10)
                        {
                            if(tcpConnected){
                                counter1=20;
                                break;
                            }else{
                                delay(100);
                            }

                            counter2++;
                        }
                    }

                }

                if(tcpConnected && counter1==20){
                    StaticVariabes_div.log("SERVER_PASSWORD"+SERVER_PASSWORD,"Host");
                    byte cipher[]=null;
                    try {
                        cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),SERVER_PASSWORD.getBytes());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Tcp_dwn_config.tcpWrite(cipher,true);
                   // Tcp_dwn_config.tcpWrite(SERVER_PASSWORD);
                    isConnected=true;
                    delay(1000);
                }
            }else{
                StaticVariabes_div.log("con","Host");
                //passing handler instance to TCP class
                new Tcp_dwn_config(myHanler);
                isConnected=true;
            }

        }catch(Exception e){
            e.printStackTrace();
            StaticVariabes_div.log("msg", e.toString()+"err");
        }
        return isConnected;
    }
}

