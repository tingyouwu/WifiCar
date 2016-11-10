package com.wty.app.wificar.wifi;

import com.wty.app.wificar.base.Constant;
import com.wty.app.wificar.event.RefreshEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author wty
 * @desc 负责管理和wifi通信
 */
public class WifiChatService{

	private static final String TAG = "WifiChatService";
	private static volatile WifiChatService sInstance = new WifiChatService();

	private ConnectThread mConnectThread;//监听连接过程的线程
	private ConnectedThread mConnectedThread;//监听通信过程的线程

	/**
	 * 单例模式，获取instance实例
	 * @return
	 */
	public synchronized static WifiChatService getInstance() {
		return sInstance;
	}

	private WifiChatService(){

	}

	/**
	 * @负责监听连接过程的线程
	 **/
	private class ConnectThread extends Thread {

		private Socket mSocket = null;

		@Override
		public void run() {
			try {
				mSocket = new Socket();
				SocketAddress socketAddress = new InetSocketAddress(Constant.IP,
						Constant.PORT);// IP和端口号
				//阻塞等待连接
				mSocket.connect(socketAddress, Constant.TIMEOUT);
				EventBus.getDefault().post(new RefreshEvent(Constant.Connect_Success));
			} catch (IOException e) {
				EventBus.getDefault().post(new RefreshEvent(Constant.Connect_Fail));
			}
		}
	}

	/**
	 * @监听通信过程的线程
	 **/
	private class ConnectedThread extends Thread {

		@Override
		public void run() {

		}
	}

}
