package com.example.naragr.project1.logic;

import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Looper;
import android.util.Log;

import com.example.naragr.project1.logic.ParamTable.DataDB;
import com.example.naragr.project1.logic.ParamTable.ParamTable;
import com.example.naragr.project1.view.MainActivity;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Handler;

public class NfcVComm {

    //flag
    enum SYSTEM_PARAM{
        SYSTEM_PARAM_NFC_TAGGED,
        SYSTEM_PARAM_CRC_VALUE,
        SYSTEM_PARAM_IS_INITIATED,
        SYSTEM_PARAM_HAS_SYSTEM_ERROR,
        SYSTEM_PARAM_ENABLE_NFC_WRITER,
        SYSTEM_PARAM_NFC_TRYED,
        SYSTEM_PARAM_ON_MONITORING,
        SYSTEM_PARAM_IDLE0_RUN1_STOP2
    };

    static int getSystemParamAddress (SYSTEM_PARAM param)
    {

        switch(param)
        {
            case SYSTEM_PARAM_NFC_TAGGED:
                return 100;
            case SYSTEM_PARAM_CRC_VALUE:
                return 101;
            case SYSTEM_PARAM_IS_INITIATED:
                return 102;
            case SYSTEM_PARAM_HAS_SYSTEM_ERROR:
                return 103;
            case SYSTEM_PARAM_ENABLE_NFC_WRITER:
                return 104;
            case SYSTEM_PARAM_NFC_TRYED:
                return 105;
            case SYSTEM_PARAM_ON_MONITORING:
                return 106;
            case SYSTEM_PARAM_IDLE0_RUN1_STOP2:
                return 107;
            default:
                return 0;
        }
    }

    //cmd
    final static byte CMD_READ_SINGLE_BLOCK = 0x20;
    final static byte CMD_WRITE_SINGLE_BLOCK = 0x21;
    final static byte CMD_LOCK_BLOCK = 0x22;
    final static byte CMD_READ_MULTI_BLOCK = 0x23;
    final static byte CMD_SELECT_TAG = 0x25;
    final static byte CMD_RESET_TO_READY= 0x26;
    final static byte CMD_WRITE_AFI = 0x27;
    final static byte CMD_LOCK_AFI = 0x28;
    final static byte CMD_WRITE_DSFID = 0x29;
    final static byte CMD_LOCK_DSFID = 0x2A;
    final static byte CMD_GET_SYSTEM_INFO = 0x2B;
    final static byte CMD_M_BLK_SEC_ST = 0x2C;


    final static byte ERR_option_not_supported = 0x03;
    final static byte ERR_no_information_given = 0x0F;
    final static byte ERR_specified_block_not_available = 0x10;
    final static byte ERR_block_locked_cannot_be_locked_again = 0x11;
    final static byte ERR_block_locked_cannot_be_changed = 0x12;
    final static byte ERR_block_not_programmed = 0x13;
    final static byte ERR_block_not_locked = 0x14;
    final static byte ERR_block_read_protected = 0x15;

    final static int ADDR_WRITE_START_FLAG = 105;
    final static int ADDR_WRITE_END_FLAG = 100;

    static String getErrString(byte errorCode)
    {
        switch(errorCode)
        {
            case 0x03:return "ERR_option_not_supported";
            case 0x0F:return "ERR_no_information_given";
            case 0x10:return "ERR_specified_block_not_available";
            case 0x11:return "ERR_block_locked_cannot_be_locked_again";
            case 0x12:return "ERR_block_locked_cannot_be_changed";
            case 0x13:return "ERR_block_not_programmed";
            case 0x14:return "ERR_block_not_locked";
            case 0x15:return "ERR_block_read_protected";
            default:return "code error";
        }
    }

    public static byte[] readTagMultiBlockForMonitoring(Tag tag, short address, byte size)
        /* When ths byte size is n, read n+1 blocks. */
    {
        byte[] response=null;
        NfcV ndef = NfcV.get(tag);

        byte[] tagUid = ndef.getTag().getId();
        Log.d("ReadMonitor", "tagUid = " + MainActivity.byteArrayToHexString(tagUid));


        if (ndef != null) {

            try {
                if(ndef.isConnected())
                {
                    ndef.close();
                }
                ndef.connect();
                int blockAddress = 0;

                Log.d("ReadMonitor", "writeOnMonitoringFlag");
                writeOnMonitoringFlag(ndef);
                Log.d("ReadMonitor", "writeOnMonitoringFlag end");
                byte[] cmd = new byte[]{
                        (byte) 0x0A,
                        CMD_READ_MULTI_BLOCK,
                        (byte)(address&0x0FF),(byte)((address>>8)&0x0FF),
                        size
                };
                //Log.d("NFCV", "readTagMultiBlock preencoded request addr= " + address);
                //Log.d("NFCV", "readTagMultiBlock request = " + MainActivity.byteArrayToHexString(cmd));
                response = ndef.transceive(cmd);

                ndef.close();
                //Log.d("NFCV", "readTagMultiBlock response = " + MainActivity.byteArrayToHexString(response));
                if(response[0] ==0x01)
                {
                    Log.d("ReadMonitor", "readTagMultiBlock result = ERROR response");
                    return null;
                }
                else
                {

                    byte[] result = new byte[response.length-1];
                    System.arraycopy(response, 1, result, 0, response.length-1);
                    Log.d("ReadMonitor", "readTagMultiBlock result = " + MainActivity.byteArrayToHexString(result));
                    return result;
                }
            } catch (Exception e) {
                Log.d("ReadMonitor", "error, request = " + MainActivity.byteArrayToHexString(response));
                e.printStackTrace();
            }
            return null;
        } else {
            Log.d("ReadMonitor", "readTagMultiBlock NDEF is null!");
            return null;
        }
    }

    public static boolean readTagMultiBlockForMonitoringContinue(NfcV ndef, short address, byte size, DataContainer dataContainer)
        /* When ths byte size is n, read n+1 blocks. */
    {
        byte[] response=null;
        int statusInverterIdx = ParamTable.getTableIdx(ParamTable.Param_table.statusinverter);
        byte[] tagUid = ndef.getTag().getId();
        Log.d("ReadMonitor", "tagUid = " + MainActivity.byteArrayToHexString(tagUid));


        if (ndef != null) {

            try {
                int blockAddress = 0;

                Log.d("ReadMonitor", "writeOnMonitoringFlag");
                writeOnMonitoringFlag(ndef);
                Log.d("ReadMonitor", "writeOnMonitoringFlag end");
                byte[] cmd = new byte[]{
                        (byte) 0x0A,
                        CMD_READ_MULTI_BLOCK,
                        (byte)(address&0x0FF),(byte)((address>>8)&0x0FF),
                        size
                };
                //Log.d("NFCV", "readTagMultiBlock preencoded request addr= " + address);
                //Log.d("NFCV", "readTagMultiBlock request = " + MainActivity.byteArrayToHexString(cmd));
                response = ndef.transceive(cmd);
                //Log.d("NFCV", "readTagMultiBlock response = " + MainActivity.byteArrayToHexString(response));
                if(response[0] ==0x01)
                {
                    Log.d("ReadMonitor", "readTagMultiBlock result = ERROR response");
                    return false;
                }
                else
                {

                    byte[] result = new byte[response.length-1];
                    System.arraycopy(response, 1, result, 0, response.length-1);
                    Log.d("ReadMonitor", "readTagMultiBlock result = " + MainActivity.byteArrayToHexString(result));
                    if(result !=null)
                    {
                        byte[] block = new byte[4];
                        for(int j=0; j<size; j++)
                        {

                            block[3] = result[j*4 + 0];
                            block[2] = result[j*4 + 1];
                            block[1] = result[j*4 + 2];
                            block[0] = result[j*4 + 3];

                            //System.arraycopy(result, j*4, block, 0, 4);
                            //Log.d("MonitorThread" , "Read IO");
                            //int statusInverterIdx = ParamTable.getTableIdx(ParamTable.Param_table.statusinverter);
                            int varIdx = DataContainer.getVarIdx(statusInverterIdx, j);
                            dataContainer.setValue(varIdx, block);

                        }
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
            } catch (Exception e) {
                //Log.d("ReadMonitor", "error, request = " + e.);
                Log.d("ReadMonitor", "error, request = " + e.getMessage());
                //e.printStackTrace();
            }
            return false;
        } else {
            Log.d("ReadMonitor", "readTagMultiBlock NDEF is null!");
            return false;
        }
    }

    public static byte[] readTagMultiBlock(Tag tag, short address, byte size)
            /* When ths byte size is n, read n+1 blocks. */
    {
        byte[] response=null;
        NfcV ndef = NfcV.get(tag);

        byte[] tagUid = ndef.getTag().getId();
        Log.d("NFCV", "tagUid = " + MainActivity.byteArrayToHexString(tagUid));


        if (ndef != null) {

            try {
                if(ndef.isConnected())
                {
                    ndef.close();
                }
                ndef.connect();
                int blockAddress = 0;
                byte[] cmd = new byte[]{
                        (byte) 0x0A,
                        CMD_READ_MULTI_BLOCK,
                        (byte)(address&0x0FF),(byte)((address>>8)&0x0FF),
                        size
                };
                Log.d("NFCV", "readTagMultiBlock preencoded request addr= " + address);
                //Log.d("NFCV", "readTagMultiBlock request = " + MainActivity.byteArrayToHexString(cmd));
                response = ndef.transceive(cmd);
                ndef.close();
                //Log.d("NFCV", "readTagMultiBlock response = " + MainActivity.byteArrayToHexString(response));
                if(response[0] ==0x01)
                {
                    Log.d("NFCV", "readTagMultiBlock result = ERROR response");
                    return null;
                }
                else
                {

                    byte[] result = new byte[response.length-1];
                    System.arraycopy(response, 1, result, 0, response.length-1);
                    Log.d("NFCV", "readTagMultiBlock result = " + MainActivity.byteArrayToHexString(result));
                    return result;
                }
            } catch (Exception e) {
                Log.d("NFCV", "error, request = " + MainActivity.byteArrayToHexString(response));
                e.printStackTrace();
            }
            return null;
        } else {
            Log.d("NFCV", "readTagMultiBlock NDEF is null!");
            return null;
        }
    }

    public static void readTagMultiBlockInThread(Tag tag, short address, byte size, MainActivity mainActivity)
        /* When ths byte size is n, read n+1 blocks. */
    {
        NfcV ndef = connectNFC(tag);
        class MyThread extends Thread
        {
            public NfcV ndef;
            public short address;
            public byte size;
            public boolean isConnected = true;
            public MainActivity mainActivity;
            public MyThread(NfcV ndef, short addr, byte size, MainActivity mainActivity){
                this.ndef = ndef;
                this.address = addr;
                this.size = size;
                this.mainActivity = mainActivity;
            }
            @Override
            public void run() {
                super.run();
                while(isConnected)
                {
                    byte[] response = transceive(ndef, address, size);
                    mainActivity.updateMonitorValue(response);

                }

            }
        };
        Thread thread = new MyThread(ndef, address, size, mainActivity);

        disconnectNFC(ndef);
        return;
    }



    static NfcV  connectNFC(Tag tag){
        NfcV ndef = NfcV.get(tag);

        byte[] tagUid = ndef.getTag().getId();
        Log.d("NFCV", "tagUid = " + MainActivity.byteArrayToHexString(tagUid));
        if (ndef != null) {
            try {
                if (ndef.isConnected()) {
                    ndef.close();
                }
                ndef.connect();
                return ndef;
            }catch (Exception e)
            {
                Log.d("NFCV", "error connecting NFC");
                e.printStackTrace();
                return null;
            }
        }
        else
        {
            Log.d("NFCV", "readTagMultiBlock NDEF is null!");
        }
        return null;
    }
    static byte[] transceive(NfcV ndef, short address, byte size) {
        byte[] response = null;
        int blockAddress = 0;
        byte[] cmd = new byte[]{
                (byte) 0x0A,
                CMD_READ_MULTI_BLOCK,
                (byte) (address & 0x0FF), (byte) ((address >> 8) & 0x0FF),
                size
        };
        Log.d("NFCV", "readTagMultiBlock preencoded request addr= " + address);
        //Log.d("NFCV", "readTagMultiBlock request = " + MainActivity.byteArrayToHexString(cmd));
        try {
            response = ndef.transceive(cmd);
        } catch (Exception e)
        {
            Log.d("NFCV", "error transceive NFC");
            e.printStackTrace();
        }

        if(response[0] ==0x01)
        {
            Log.d("NFCV", "readTagMultiBlock result = ERROR response");
            return null;
        }
        else
        {

            byte[] result = new byte[response.length-1];
            System.arraycopy(response, 1, result, 0, response.length-1);
            Log.d("NFCV", "readTagMultiBlock result = " + MainActivity.byteArrayToHexString(result));
            return result;
        }
    }
    static void disconnectNFC(NfcV ndef)
    {
        try {
            ndef.close();
        }catch (Exception e)
        {
            Log.d("NFCV", "error disconnecting NFC");
            e.printStackTrace();
        }
    }

    public static boolean writeTag(Tag tag, short address, int block)
        /* When ths byte size is n, read n+1 blocks. */
    {

        NfcV ndef = NfcV.get(tag);

        byte[] tagUid = ndef.getTag().getId();
        Log.d("wei", "tagUid = " + MainActivity.byteArrayToHexString(tagUid));
        byte[] response=null;

        if (ndef != null) {

            try {
                if(ndef.isConnected())
                    ndef.close();
                ndef.connect();
                writeStartFlag(ndef);
                //int blockAddress = 0;
                byte[] cmd = new byte[]{
                        (byte) 0x0A,
                        CMD_WRITE_SINGLE_BLOCK,
                        (byte)(address&0x0FF),(byte)((address>>8)&0x0FF),
                        (byte)(block&0x0FF),(byte)((block>>8)&0x0FF),(byte)((block>>16)&0x0FF),(byte)((block>>24)&0x0FF),
                };
                response = ndef.transceive(cmd);
                writeEndFlag(ndef);
                ndef.close();
                Log.d("NFCV", "write request = " + MainActivity.byteArrayToHexString(cmd));
                Log.d("NFCV", "write response = " + MainActivity.byteArrayToHexString(response));
                if(response[0] ==0x01)
                {

                    return false;
                }
                else
                {
                    Log.d("NFCV", "Write success!");
                    return true;
                }
            } catch (IOException e) {
                Log.d("NFCV", "error, request = " + MainActivity.byteArrayToHexString(response));
                e.printStackTrace();
                return false;
            }

        } else {
            return false;
        }
    }






    enum NFC_STATE{
        UNKNOWN,
        ENABLED,
        DISABLED,
    }
    static NFC_STATE stateNFC = NFC_STATE.UNKNOWN;


    private static byte[] readNFCByIdxList(Tag tag, int idx[]){


        NfcV ndef = NfcV.get(tag);

        byte[] tagUid = ndef.getTag().getId();
        Log.d("NFCV", "tagUid = " + MainActivity.byteArrayToHexString(tagUid));
        byte[] response=null;
        byte[] resultAll = new byte[4*idx.length];
        int address;
        final byte size = 1;

        for(int i=0; i<idx.length;i++)
        {
            address = idx[i];

            if (ndef != null) {

                try {
                    if(ndef.isConnected())
                    {
                        ndef.close();
                    }
                    ndef.connect();
                    int blockAddress = 0;
                    byte[] cmd = new byte[]{
                            (byte) 0x0A,
                            CMD_READ_MULTI_BLOCK,
                            (byte)(address&0x0FF),(byte)((address>>8)&0x0FF),
                            size
                    };
                    Log.d("NFCV", "readTagMultiBlock preencoded request addr= " + address);
                    //Log.d("NFCV", "readTagMultiBlock request = " + MainActivity.byteArrayToHexString(cmd));
                    response = ndef.transceive(cmd);
                    ndef.close();
                    //Log.d("NFCV", "readTagMultiBlock response = " + MainActivity.byteArrayToHexString(response));
                    if(response[0] ==0x01)
                    {
                        Log.d("NFCV", "readTagMultiBlock result = ERROR response");
                        return null;
                    }
                    else
                    {

                        byte[] result = new byte[response.length-1];
                        System.arraycopy(response, 1, result, 0, response.length-1);
                        Log.d("NFCV", "readTagMultiBlock result = " + MainActivity.byteArrayToHexString(result));
                        //return result;
                    }
                } catch (Exception e) {
                    Log.d("NFCV", "error, request = " + MainActivity.byteArrayToHexString(response));
                    e.printStackTrace();
                }
                return null;
            } else {
                Log.d("NFCV", "readTagMultiBlock NDEF is null!");
                return null;
            }
        }
        return null;
    }


    private static boolean readNFCEnable(Tag tag){
        /* When ths byte size is n, read n+1 blocks. */
        NfcV ndef = NfcV.get(tag);
        stateNFC = NFC_STATE.UNKNOWN;
        int address = 104;
        byte[] tagUid = ndef.getTag().getId();
        Log.d("NFCV", "tagUid = " + MainActivity.byteArrayToHexString(tagUid));
        byte[] response=null;
        byte size = 1;
        if (ndef != null) {

            try {
                if(ndef.isConnected())
                {
                    ndef.close();
                }
                ndef.connect();
                int blockAddress = 0;
                byte[] cmd = new byte[]{
                        (byte) 0x0A,
                        CMD_READ_MULTI_BLOCK,
                        (byte)(address&0x0FF),(byte)((address>>8)&0x0FF),
                        size
                };
                Log.d("NFCV", "readTagMultiBlock preencoded request addr= " + address);
                //Log.d("NFCV", "readTagMultiBlock request = " + MainActivity.byteArrayToHexString(cmd));
                response = ndef.transceive(cmd);
                ndef.close();
                //Log.d("NFCV", "readTagMultiBlock response = " + MainActivity.byteArrayToHexString(response));
                if(response[0] ==0x01)
                {
                    Log.d("NFCV", "readTagMultiBlock result = ERROR response");
                    return false;
                }
                else
                {

                    byte[] result = new byte[response.length-1];
                    System.arraycopy(response, 1, result, 0, response.length-1);
                    Log.d("NFCV", "readTagMultiBlock result = " + MainActivity.byteArrayToHexString(result));
                    int value = ByteBuffer.wrap(result).getInt();
                    switch(value)
                    {
                        case 0:
                            stateNFC = NFC_STATE.DISABLED;
                            break;
                        case 1:
                            stateNFC = NFC_STATE.ENABLED;

                            break;
                    }

                    return true;
                }
            } catch (Exception e) {
                Log.d("NFCV", "error, request = " + MainActivity.byteArrayToHexString(response));
                e.printStackTrace();
            }
            return false;
        } else {
            Log.d("NFCV", "readTagMultiBlock NDEF is null!");
            return false;
        }
    }

    private static boolean writeStartFlag(NfcV ndef ) throws IOException {

        byte[] response = null;
        int isWriteAddr = getSystemParamAddress(SYSTEM_PARAM.SYSTEM_PARAM_NFC_TRYED);
        int isWrite = 1;
        byte[] cmd_isWrite = new byte[]{
                (byte) 0x0A,
                CMD_WRITE_SINGLE_BLOCK,
                (byte) (isWriteAddr & 0x0FF), (byte) ((isWriteAddr >> 8) & 0x0FF),
                (byte) (isWrite >> 0 & 0x0FF), (byte) ((isWrite >> 8) & 0x0FF), (byte) ((isWrite >> 16) & 0x0FF), (byte) ((isWrite >> 24) & 0x0FF),
        };
        response = ndef.transceive(cmd_isWrite);
        if(response[0] ==0x01)
        {
            Log.d("NFCV", "readTagMultiBlock result = ERROR response");
            return false;
        }
        else
        {
            //Log.d("NFCV", "Write block "+ i +" success!");
            return true;
        }

    }

    private static boolean writeRunFlag(NfcV ndef, boolean isRun) throws IOException {

        byte[] response = null;
        int isWriteAddr = getSystemParamAddress(SYSTEM_PARAM.SYSTEM_PARAM_IDLE0_RUN1_STOP2);
        int isWrite = 0;
        if(isRun)
            isWrite = 1;
        else
            isWrite = 2;

        byte[] cmd_isWrite = new byte[]{
                (byte) 0x0A,
                CMD_WRITE_SINGLE_BLOCK,
                (byte) (isWriteAddr & 0x0FF), (byte) ((isWriteAddr >> 8) & 0x0FF),
                (byte) (isWrite >> 0 & 0x0FF), (byte) ((isWrite >> 8) & 0x0FF), (byte) ((isWrite >> 16) & 0x0FF), (byte) ((isWrite >> 24) & 0x0FF),
        };
        response = ndef.transceive(cmd_isWrite);
        if(response[0] ==0x01)
        {
            Log.d("NFCV", "readTagMultiBlock result = ERROR response");

            return false;
        }
        else
        {
            //Log.d("NFCV", "Write block "+ i +" success!");
            return true;
        }

    }

    public static boolean writeOnMonitoringFlag(NfcV ndef ) throws IOException {

        byte[] response = null;
        int isWriteAddr = getSystemParamAddress (SYSTEM_PARAM.SYSTEM_PARAM_ON_MONITORING);
        int isWrite = 1;
        byte[] cmd_isWrite = new byte[]{
                (byte) 0x0A,
                CMD_WRITE_SINGLE_BLOCK,
                (byte) (isWriteAddr & 0x0FF), (byte) ((isWriteAddr >> 8) & 0x0FF),
                (byte) (isWrite >> 0 & 0x0FF), (byte) ((isWrite >> 8) & 0x0FF), (byte) ((isWrite >> 16) & 0x0FF), (byte) ((isWrite >> 24) & 0x0FF),
        };
        response = ndef.transceive(cmd_isWrite);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.d("NFCV", "Write Monitor flag result = ERROR / sleep");
            return false;
        }
        if(response[0] ==0x01)
        {
            Log.d("NFCV", "Write Monitor flag result = ERROR / response");

            return false;
        }
        else
        {
            Log.d("NFCV", "Write Monitor flag success! addr = " + isWriteAddr);
            return true;
        }
    }

    private static boolean writeEndFlag(NfcV ndef ) throws IOException {

        byte[] response = null;
        //int isWriteAddr = 100;
        int isWriteAddr = getSystemParamAddress (SYSTEM_PARAM.SYSTEM_PARAM_NFC_TAGGED);
        int isWrite = 1;
        byte[] cmd_isWrite = new byte[]{
                (byte) 0x0A,
                CMD_WRITE_SINGLE_BLOCK,
                (byte) (isWriteAddr & 0x0FF), (byte) ((isWriteAddr >> 8) & 0x0FF),
                (byte) (isWrite >> 0 & 0x0FF), (byte) ((isWrite >> 8) & 0x0FF), (byte) ((isWrite >> 16) & 0x0FF), (byte) ((isWrite >> 24) & 0x0FF),
        };
        response = ndef.transceive(cmd_isWrite);
        if(response[0] ==0x01)
        {
            Log.d("NFCV", "readTagMultiBlock result = ERROR response");

            return false;
        }
        else
        {
            //Log.d("NFCV", "Write block "+ i +" success!");
            return true;
        }

    }

    public static boolean writeTag(final Tag tag, final int[] idxs, final int[] blocks)
    {
        //DialogFactory.showProgressDialog("쓰기", "진행도", blocks.length);

        NfcV ndef = NfcV.get(tag);

        String s = "";
        byte[] tagUid = ndef.getTag().getId();
        Log.d("NFCV.writeTag", "NFCV.call writeTag");

        byte[] response=null;
        short i=0;
        try {


            if(ndef.isConnected())
            {
                ndef.close();
                while(ndef.isConnected())
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }


            ndef.connect();
            writeStartFlag(ndef);
            for(i = 0; i<blocks.length; i++)
            {
                //MainActivity.dataContainer.getSubList(DataContainer.getTableIdx(i)).getDefaultAddress();
                //int addr =
                int valueIdx = idxs[i];
                int block = blocks[i];

                int currAddr = DataDB.getInstance().table[valueIdx].addr;
                if (ndef != null) {

                    do{
                        byte[] cmd = new byte[]{
                                (byte) 0x0A,
                                CMD_WRITE_SINGLE_BLOCK,
                                (byte)(currAddr&0x0FF),(byte)((currAddr>>8)&0x0FF),
                                (byte)(block>>0&0x0FF),(byte)((block>>8)&0x0FF),(byte)((block>>16)&0x0FF),(byte)((block>>24)&0x0FF),
                        };
                        response = ndef.transceive(cmd);
                        Log.d("NFCV", "write block preencode request addr= (" + currAddr + ", "+ block + ")" + ", idx = "+i + ", addr = " + currAddr);
                        Log.d("NFCV", "write block request = " + MainActivity.byteArrayToHexString(cmd));
                        Log.d("NFCV", "write block response = " + MainActivity.byteArrayToHexString(response));

                        if(response[0] ==0x01)
                            Log.d("NFCV", "WRONG VALUE WROTE: [" + i + "] "+ block);
                    }while(response[0] ==0x01);
//                    s += "["+currAddr+"]" + block;
                } else {
                    ndef.close();
                    return false;
                }
            }
            writeEndFlag(ndef);
            //s += "[100]" + 1;
            //Log.d("NFCV", s);
            ndef.close();



        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean writeRun(final Tag tag, boolean isRun)
    {
        NfcV ndef = NfcV.get(tag);

        String s = "";
        byte[] tagUid = ndef.getTag().getId();
        Log.d("NFCV.writeTag", "NFCV.call writeTag");

        byte[] response=null;
        short i=0;
        try {


            if(ndef.isConnected())
            {
                ndef.close();
                while(ndef.isConnected())
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }


            ndef.connect();
            writeRunFlag(ndef, isRun);
            ndef.close();



        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean writeTag(final Tag tag, final int[] blocks)
        /* When ths byte size is n, read n+1 blocks. */
    {

        //DialogFactory.showProgressDialog("쓰기", "진행도", blocks.length);

        NfcV ndef = NfcV.get(tag);

        String s = "";
        byte[] tagUid = ndef.getTag().getId();
        Log.d("NFCV.writeTag", "NFCV.call writeTag");

        byte[] response=null;
        short i=0;
        try {


            if(ndef.isConnected())
            {
                ndef.close();
                while(ndef.isConnected())
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }


            ndef.connect();
            writeStartFlag(ndef);
            for(i = 0; i<blocks.length; i++)
            {
                //MainActivity.dataContainer.getSubList(DataContainer.getTableIdx(i)).getDefaultAddress();
                //int addr =
                int block = blocks[i];


                int currAddr = DataDB.getInstance().table[i].addr;
                if (ndef != null) {

                    do{
                        byte[] cmd = new byte[]{
                                (byte) 0x0A,
                                CMD_WRITE_SINGLE_BLOCK,
                                (byte)(currAddr&0x0FF),(byte)((currAddr>>8)&0x0FF),
                                (byte)(block>>0&0x0FF),(byte)((block>>8)&0x0FF),(byte)((block>>16)&0x0FF),(byte)((block>>24)&0x0FF),
                        };
                        response = ndef.transceive(cmd);
                        Log.d("NFCV", "write block preencode request addr= (" + currAddr + ", "+ block + ")" + ", idx = "+i + ", addr = " + currAddr);
                        Log.d("NFCV", "write block request = " + MainActivity.byteArrayToHexString(cmd));
                        Log.d("NFCV", "write block response = " + MainActivity.byteArrayToHexString(response));

                        if(response[0] ==0x01)
                        Log.d("NFCV", "WRONG VALUE WROTE: [" + i + "] "+ block);
                    }while(response[0] ==0x01);
//                    s += "["+currAddr+"]" + block;
                } else {
                    ndef.close();
                    return false;
                }
            }
            writeEndFlag(ndef);
            //s += "[100]" + 1;
            //Log.d("NFCV", s);
            ndef.close();



        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static HashSet<Integer> writeTagAll(Tag detectedTag, HashMap<Integer, Integer> map) {
        HashSet<Integer> succeed = new HashSet<>();
        int values[] = new int[map.size()];
        for (int idx:map.keySet()) {
            int value = map.get(idx);
            short addr = (short)idx;
            Log.d("NFCV", "write try = " +addr + "::val = " + value);
            boolean result = writeTag(detectedTag, addr, value);

            try {
                Thread.sleep(0, 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(result == true)
            {
                succeed.add(idx);
            }
        }
        return succeed;
    }

    public static boolean writeTagAll(Tag detectedTag, int[] blocks) {
        //boolean succeed = false;
        Log.d("WriteTagAll", "Call WriteTagAll");
        //readNFCEnable(detectedTag);
        //if (stateNFC == NFC_STATE.ENABLED)
        {
            boolean result = writeTag(detectedTag, blocks);
            return result;
        }

    }


    public enum NFCV_CMD{
        CMD_READ_SINGLE_BLOCK,
        CMD_WRITE_SINGLE_BLOCK,
        CMD_LOCK_BLOCK,
        CMD_READ_MULTI_BLOCK,
        CMD_SELECT_TAG,
        CMD_RESET_TO_READY,
        CMD_WRITE_AFI,
        CMD_LOCK_AFI,
        CMD_WRITE_DSFID,
        CMD_LOCK_DSFID,
        CMD_GET_SYSTEM_INFO,
        CMD_M_BLK_SEC_ST,

    }

    private static byte getCMD(NFCV_CMD cmd)
    {
        switch(cmd)
        {
            case CMD_READ_SINGLE_BLOCK:
                return CMD_READ_SINGLE_BLOCK;
            case CMD_WRITE_SINGLE_BLOCK:
                return CMD_WRITE_SINGLE_BLOCK;
            case CMD_LOCK_BLOCK:
                return CMD_LOCK_BLOCK;
            case CMD_READ_MULTI_BLOCK:
                return CMD_READ_MULTI_BLOCK;
            case CMD_SELECT_TAG:
                return CMD_SELECT_TAG;
            case CMD_RESET_TO_READY:
                return CMD_RESET_TO_READY;
            case CMD_WRITE_AFI:
                return CMD_WRITE_AFI;
            case CMD_LOCK_AFI:
                return CMD_LOCK_AFI;
            case CMD_WRITE_DSFID:
                return CMD_WRITE_DSFID;
            case CMD_LOCK_DSFID:
                return CMD_LOCK_DSFID;
            case CMD_GET_SYSTEM_INFO:
                return CMD_GET_SYSTEM_INFO;
            case CMD_M_BLK_SEC_ST:
                return CMD_M_BLK_SEC_ST;
            default:
                return 0;
        }
    }

}
