package com.wty.app.wificar.wifi;

import android.util.Log;
import com.wty.app.wificar.base.Constant;
import com.wty.app.wificar.event.RefreshEvent;
import com.wty.app.wificar.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	public static final int STATE_NONE = 0;       // we're doing nothing
	public static final int STATE_CONNECTING = 1; // now initiating an outgoing connection 监听发送出去的连接
	public static final int STATE_CONNECTED = 2;  // now connected to a remote device 已经连接的监听

	private ConnectThread mConnectThread;//监听连接过程的线程
	private ConnectedThread mConnectedThread;//监听通信过程的线程
	private int mState; //记录当前连接状态
	Socket mSocket = null;

	/**
	 * 单例模式，获取instance实例
	 * @return
	 */
	public synchronized static WifiChatService getInstance() {
		return sInstance;
	}

	private WifiChatService(){
		mState = STATE_NONE;
	}

	/**
	 * 尝试连接到wifi小车
	 **/
	public synchronized void start() {
		if (mConnectThread != null) {mConnectThread = null;}
		if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
		mConnectThread = new ConnectThread();
		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 * Stop all threads
	 */
	public synchronized void stop() {
		if (mConnectThread != null) {mConnectThread = null;}
		if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
		setState(STATE_NONE);
	}

	/**
	 * 连接失败，通知UI
	 */
	private void connectionFailed() {
		setState(STATE_NONE);
		EventBus.getDefault().post(new RefreshEvent(Constant.Connect_Fail));
	}

	/**
	 * 连接成功，通知UI
	 **/
	private void connectionSuccess() {
		setState(STATE_CONNECTED);
		EventBus.getDefault().post(new RefreshEvent(Constant.Connect_Success));
	}

	/**
	 * 连接断开，通知UI
	 */
	private void connectionLost() {
		setState(STATE_NONE);
		EventBus.getDefault().post(new RefreshEvent(Constant.Connect_Lost));
	}

	/**
	 * 连接上wifi小车，启动监听通信线程
	 **/
	public synchronized void connected(Socket socket) {
		if (mConnectedThread != null) {mConnectedThread.interrupt(); mConnectedThread = null;}
		mConnectedThread = new ConnectedThread(socket);
		mConnectedThread.start();
		connectionSuccess();
	}

	/**
	 * 设置状态
	 **/
	private synchronized void setState(int state) {
		mState = state;
	}

	/**
	 *  获取当前连接状态
	 **/
	public synchronized int getState() {
		return mState;
	}

	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 * @param out The bytes to write
	 * @see ConnectedThread#write(byte[])
	 */
	public void write(byte[] out) {
		// Create temporary object
		ConnectedThread r;
		// Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (mState != STATE_CONNECTED) return;
			r = mConnectedThread;
		}
		// Perform the write unsynchronized
		r.write(out);
	}

	/**
	 * @负责监听连接过程的线程
	 **/
	private class ConnectThread extends Thread {

		@Override
		public void run() {
			try {
				mSocket = new Socket();
				SocketAddress socketAddress = new InetSocketAddress(PreferenceUtil.getInstance().getIP(),
						PreferenceUtil.getInstance().getPort());// IP和端口号
				//阻塞等待连接
				mSocket.connect(socketAddress, Constant.TIMEOUT);
			} catch (IOException e) {
				connectionFailed();
				//关闭socket
				try {
					mSocket.close();
				} catch (IOException e1) {
					Log.e(TAG, "unable to close() socket during connection failure", e1);
				}
				return;
			}

			//启动连接成功线程监听通信过程
			connected(mSocket);
		}
	}

	/**
	 * @监听通信过程的线程
	 **/
	private class ConnectedThread extends Thread {

		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(Socket socket){
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}
			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		@Override
		public void run() {
			byte[] buffer = new byte[1024];
			int bytes;
			while (!this.isInterrupted()) {
				try {
					// 读取输入流
					bytes = mmInStream.read(buffer);
					Log.d(TAG, "wutingyou 收到数据");
					if(bytes>0){
						EventBus.getDefault().post(new RefreshEvent(new String(buffer,0,bytes)));
					}
				} catch (IOException e) {
					Log.e(TAG, "disconnected", e);
					connectionLost();
					break;
				}
			}
		}

		public void cancel(){
			interrupt();
		}

		/**
		 * Write to the connected OutStream.
		 * @param buffer  The bytes to write
		 */
		public void write(byte[] buffer) {
			try {
				mmOutStream.write(buffer);
				EventBus.getDefault().post(new RefreshEvent(new String(buffer)));
			} catch (IOException e) {
				Log.e(TAG, "Exception during write", e);
			}
		}
	}

}
