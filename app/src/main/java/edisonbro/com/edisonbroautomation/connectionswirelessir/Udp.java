package edisonbro.com.edisonbroautomation.connectionswirelessir;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;

//class to get ip of server by broadcasting ip request in current network
public class Udp {

	DatagramSocket clientSocket = null;
	InetAddress IPAddress = null;
	DatagramPacket receivePacket = null;
	DatagramPacket sendPacket = null;

	ReadThread t1=null;
	Handler udpHandler;

	static boolean udpRunning=false;
	final int RESPONSE=1,EXCEPTION=4;

	//Default IP and Port setting for finding server 
	int UDP_SERVER_PORT; 
	String BROADCAST_IP=null;
	String myDeviceIp;
	Context context;
	//constructor for class
	public Udp(Handler activityHandler, Context activitycontext) {
		udpHandler = activityHandler;	//assigning handler instance
		context=activitycontext;	//assigining activity context

		//setting udp port
		UDP_SERVER_PORT= StaticVariables.UDP_SERVER_PORT;

		//getting wifi multicast lock
		getWifiLock(context);

		if (clientSocket == null) {
			//fetching device ip to broadcast msg
			// and opening connection if socket is freshly opened
			getMyDeviceIP();

		} else if (clientSocket != null) {

			if (!clientSocket.isConnected()) {
				//fetching device ip to broadcast msg
				//opening connection if socket is  opened but servernot connected yet
				getMyDeviceIP();

			}

		}
	}

	public static void getWifiLock(Context context){


		try{
			WifiManager wifi = (WifiManager)context.getSystemService( Context.WIFI_SERVICE );

			if(wifi != null){
				WifiManager.MulticastLock lock = wifi.createMulticastLock("Log_Tag");
				lock.setReferenceCounted(true);
				lock.acquire();
				System.out.println("Wifi lock acquired");
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	//connection initiating method
	void udpConnectionOpen() {

		try {

			clientSocket = new DatagramSocket();	//initializing  socket

			//setting global ip to search server
			IPAddress = InetAddress.getByName(BROADCAST_IP);				
			udpRunning=true;
			//checking if read thread is null
			if(t1==null){
				t1=new ReadThread();
				t1.start();		//starting Reading Thread to read server response
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

	//method to write data on to socket streams
	public void udpWrite(final String sendmsg) {

		//getting bytes from string data passed to method
		byte[] sendData = sendmsg.getBytes();

		//making data packet that is to send
		sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress,UDP_SERVER_PORT);	
		try {
			clientSocket.send(sendPacket);	//sending data
			StaticVariables.printLog("UDP","UDP REQUEST SENT");
		}catch(SocketException s){
			s.printStackTrace();
			udpHandler.obtainMessage(EXCEPTION,"Network Unreachable").sendToTarget();
		}catch (IOException e) {
			e.printStackTrace();
		}

	}

	//closing connections
	public void udpConnectionClose()
	{
		try
		{
			if(clientSocket.isConnected())
				clientSocket.close();	//closing socket

			//closing reading thread
			if(t1!=null)
			{
				udpRunning=false;
				t1.interrupt();
				t1=null;
			}
			StaticVariables.printLog("UDP","UDP CONNECTION CLOSED");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}


	//method to check device ip
	String getMyDeviceIP(){
		//initialize variable
		myDeviceIp=null;
		Thread t=new Thread(){
			public void run(){
				try{
					ConnectivityManager cm=(ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
					NetworkInfo netinfo=cm.getNetworkInfo(cm.TYPE_WIFI);

					WifiManager wm=(WifiManager) context.getSystemService(context.WIFI_SERVICE);
					WifiInfo wif=wm.getConnectionInfo();

					//checking if network is connected
					if(netinfo != null && netinfo.getState()==NetworkInfo.State.CONNECTED)
					{
						//getting ip of currently connected network from wifi manager
						int wifi_ip=wif.getIpAddress();
						int ip1=(wifi_ip & 0xff);
						int ip2=(wifi_ip >> 8 & 0xff);
						int ip3=(wifi_ip >> 16 & 0xff);
						int ip4=(wifi_ip >> 24 & 0xff);
						String DeviceIP=String.format( "%d.%d.%d.%d",ip1,ip2,ip3,ip4);
						StaticVariables.printLog("DEVICE IP","Device ip detected :"+DeviceIP);		  
						myDeviceIp=String.format( "%d.%d.%d.%d",ip1,ip2,ip3,255);
						//assigning selected ip for broad cast
						BROADCAST_IP=myDeviceIp;
						StaticVariables.printLog("BROADCAST_IP","BROADCAST IP SET :"+myDeviceIp);
						//starting connection
						udpConnectionOpen();		
					}

				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};t.start();

		return myDeviceIp;
	}

	//UDP reading Thread to handle broadcast response
	public class ReadThread extends Thread
	{
		public void run() 
		{
			//thread will continue to run till udpRunning  variable remains true
			while (udpRunning) 
			{
				byte[] receiveData = new byte[1024];

				//making packet to receiving data from socket
				receivePacket = new DatagramPacket(receiveData,receiveData.length);

				try {
					clientSocket.receive(receivePacket);	//receiving the data packet

				} catch (IOException e) {
					e.printStackTrace();
				}

				// filtering udp response
				String udpReponse = new String(receivePacket.getData(), 0,receivePacket.getLength());

				StaticVariables.printLog("DATA FROM UDP", "server log : " + udpReponse);

				//sending response to activity
				udpHandler.obtainMessage(RESPONSE,udpReponse).sendToTarget();

			}
		}


	}

}
