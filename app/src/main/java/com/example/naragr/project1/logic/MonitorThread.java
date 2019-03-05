package com.example.naragr.project1.logic;

import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.util.Log;

import com.example.naragr.project1.logic.ParamTable.ParamTable;
import com.example.naragr.project1.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class MonitorThread extends Thread {
    //private List<String> valueList = new ArrayList<>();

    private boolean isQuit = false;
    private DataContainer dataContainer;
    private Tag tag = null;
    private MainActivity mParent;

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
        while(!isQuit)
        {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(tag!=null) {

                ReadMonitoringData(tag);
                mParent.callSwapMoniterView();
                Log.d("MonitorThread" , "tagging and updating");

            }
            else
            {
                Log.d("MonitorThread" , "Not tagging");
            }

        }
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
        byte[] result = NfcVComm.readTagMultiBlock(detectedTag, headPos, blockSize);

        if(result !=null)
        {
            byte[] block = new byte[4];
            for(int j=0; j<blockSize; j++)
            {

                block[3] = result[j*4 + 0];
                block[2] = result[j*4 + 1];
                block[1] = result[j*4 + 2];
                block[0] = result[j*4 + 3];

                //System.arraycopy(result, j*4, block, 0, 4);
                Log.d("MonitorThread" , "Read IO");
                int varIdx = DataContainer.getVarIdx(statusInverterIdx, j);
                dataContainer.setValue(varIdx, block);
            }
            //Toast.makeText(this, "[ReadTab SUCCESS]"+idx +  "result= null", Toast.LENGTH_SHORT).show();

        }

        else
        {
            Log.d("ReadTab", "[ERROR] tab[" + statusInverterIdx + "], result= null");
            //Toast.makeText(this, "[ReadTab ERROR]"+idx +  "result= null", Toast.LENGTH_SHORT).show();
            return false;
        }

        Log.d("ReadTab", "setValue = "+ dataContainer.toString());
        return true;
    }
}
