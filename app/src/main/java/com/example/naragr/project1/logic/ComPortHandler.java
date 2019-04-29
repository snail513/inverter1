package com.example.naragr.project1.logic;

import android.util.Log;
import com.example.naragr.project1.view.MainActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android_serialport_api.SerialPort;


public class ComPortHandler {
    static SerialPort mSerialPort;
    static OutputStream mOutputStream;
    static InputStream mInputStream;

    private static boolean isConnected = false;

    public static boolean isConnected()
    {
        return isConnected;
    }

    private static final String TAG = "ComPortHandler";

    private static ComPortHandler comPortHandler = null;

    public static boolean connect(int baudrate)
    {
        if(isConnected)
        {
            closeSerialPort();
        }
        try{
            comPortHandler = new ComPortHandler(baudrate);
            return true;
        }catch(Exception e)
        {
            return false;
        }

    }

    private ComPortHandler(int baudrate)
    {
        if (mSerialPort == null) {
            String path = "dev/ttymxc4";
            try{
                mSerialPort = new SerialPort(new File(path), baudrate, 0);
                isConnected = true;
                mOutputStream = mSerialPort.getOutputStream();
                mInputStream = mSerialPort.getInputStream();
            } catch (IOException e) {
                Log.d(TAG, "Com Port openning error!");
                isConnected =false;
            }
        }
    }



    public static void sendDate(byte[] writeBytes) {
        try {
            if (mOutputStream != null) {
                mOutputStream.write(writeBytes);
            }
        } catch (IOException e) {

        }
    }

    static byte[] readData = new byte[7];
    static boolean isRead = false;

    private  static void handleReadData()
    {
        if(isRead)
        {
            isRead = false;
            String readDatas = String.format("%02x ", readData);
            Log.d(TAG, readDatas);
        }
        else
        {

        }
    }

    public static void readData() {
        Thread t = new Thread(){
            @Override
            public void run() {
                super.run();
                while(isConnected){
                    try {

                        if (mInputStream != null) {
                            int size = mInputStream.read(readData);
                            if (size > 0) {
                                //읽은 응답이 있음
                                //아래에서 데이터 처리
                                isRead = true;
                                handleReadData();
                            } else {
                                //읽음 없음
                                isRead = false;
                                Thread.sleep(1000); //1초후 다시 버퍼를 검사한다.
                            }

                        }

                    } catch (IOException e) {
                        isConnected = false;
                        isRead = false;
                    } catch (InterruptedException e) {
                        isConnected = false;
                        isRead = false;
                    }
                }
                Log.d(TAG, "Stop reading");
            }
        };
        t.run();
    }
    public static void closeSerialPort() {
        isConnected = false;
        if (mSerialPort != null)
        {
            mSerialPort.close();
            mSerialPort = null;
        }
    }
}
