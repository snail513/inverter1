package com.example.naragr.project1.logic;

import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.naragr.project1.logic.ParamTable.ParamTable;
import com.example.naragr.project1.view.MainActivity;

import java.io.IOException;

public class MonitorThread extends Thread {
    //private List<String> valueList = new ArrayList<>();

    private boolean isQuit = false;
    private DataContainer dataContainer;
    private Tag tag = null;
    private MainActivity mParent;
    private Runnable poster = new Runnable() {
        @Override
        public void run() {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mParent.swapMonitorView();
                }
            });
        }
    };

    public void Quit()
    {
        isQuit = true;
    }
    public void setTag(Tag tagIn)
    {
        this.tag = tagIn;
    }

    public MonitorThread(MainActivity main, DataContainer dataContainerIn)
    {
        mParent = main;
        dataContainer = dataContainerIn;
    }

    @Override
    public void run() {
        super.run();
        Log.d("MonitorThread" , "Enter thread");
        ReadMonitoringData(tag);
        Log.d("MonitorThread" , "Quit thread");

    }

    public boolean ReadMonitoringData(Tag detectedTag)
    {
        if(detectedTag ==null)
            return false;

        int statusInverterIdx = ParamTable.getTableIdx(ParamTable.Param_table.statusinverter);
        byte blockSize = (byte)dataContainer.getSubList(statusInverterIdx).getTotalBlockSize();
        short headPos = (short)dataContainer.getSubList(statusInverterIdx).getDefaultAddress();
        Log.d("MonitorThread", "tabIdx = " + statusInverterIdx + ", block size = " + blockSize + ", headPos = " + headPos);

        NfcV nfcv = NfcV.get(tag);
        try {
            nfcv.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean isContinue = true;
        while(isContinue){

            if(isQuit)
            {
                break;
            }
            isContinue = NfcVComm.readTagMultiBlockForMonitoringContinue(nfcv, headPos, blockSize, dataContainer);
            if(isContinue) {
                mParent.callSwapMoniterView();
            }
            Thread t = new Thread(poster);
            t.start();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            nfcv.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
