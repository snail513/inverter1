package com.example.naragr.project1.logic;

import android.util.Log;

import com.example.naragr.project1.logic.ParamSubject.GeneralSubList;
import com.example.naragr.project1.logic.ParamSubject.SubList;
import com.example.naragr.project1.logic.ParamTable.Aout_t;
import com.example.naragr.project1.logic.ParamTable.Bool_t;
import com.example.naragr.project1.logic.ParamTable.DataDB;
import com.example.naragr.project1.logic.ParamTable.Dout_t;
import com.example.naragr.project1.logic.ParamTable.EnergySave_t;
import com.example.naragr.project1.logic.ParamTable.Freq_t;
import com.example.naragr.project1.logic.ParamTable.InputComm_t;
import com.example.naragr.project1.logic.ParamTable.ParamTable;
import com.example.naragr.project1.logic.ParamTable.Param_idx;
import com.example.naragr.project1.logic.ParamTable.Parameter;
import com.example.naragr.project1.logic.ParamTable.RotDir2_t;
import com.example.naragr.project1.logic.ParamTable.RotDir3_t;
import com.example.naragr.project1.logic.ParamTable.RunComm_t;
import com.example.naragr.project1.logic.ParamTable.Stop_t;
import com.example.naragr.project1.view.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;


public class DataContainer implements Serializable{
    public static final int MAX_IDX = 8;
    public static final int MAX_IDX_WRITABLE = 4;
    public String mFileName;
    public static String TAG = "DataContainer TAG";


    public HashSet<Integer> writerIndices = new HashSet<>();

    public GeneralSubList[] sublists;

    public byte[] serialize()
    {
        byte[] data = new byte[getTotalBlockSize()*4];
        for(int i=0; i< getTotalBlockSize(); i++)
        {

            byte[] b = getValue(i);
            for(int j=0;j<4;j++)
            {
                data[i*4+j] = b[j];
            }

            data[i*4+1] = b[1];
            data[i*4+2] = b[2];
            data[i*4+3] = b[3];
            String s = String.format("[%02X:%02X:%02X:%02X]", b[0], b[1], b[2], b[3]);
            Log.d("Deserialize", "set value["+i+"] = "+s);
        }

        return data;
    }

    public void deserialize(byte[] data)
    {

        for(int i=0; i< getTotalBlockSize(); i++)
        {

            byte[] b = new byte[4];
            b[0] = data[i*4+0];
            b[1] = data[i*4+1];
            b[2] = data[i*4+2];
            b[3] = data[i*4+3];
            String s = String.format("%02x:%02x:%02x:%02x", b[0],b[1],b[2],b[3]);
            Log.d("Deserialize", "set value["+i+"] = " + s);
            setValue(i, b);

        }

    }

    public static DataContainer load(MainActivity me, String fileName) {
        DataContainer container = new DataContainer();

        container.mFileName = fileName;
        byte[] datas = new byte[container.getTotalBlockSize()*4];


        File file = new File(me.getFilesDir(), fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(datas);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*for(int i=0;i<datas.length; i++)
        {
            Log.d("DataContainer:load", "["+i+"]" +datas[i]);
        }*/
        container.deserialize(datas);

        Log.d(TAG, "File load!!"+ file.getAbsolutePath());
        return container;
    }

    public List<String> getValueList(int tableIdx)
    {
        List<String> result = new ArrayList<>();
        int startIdx = getTableOffset(tableIdx);
        for(int i=0; i<getSubList(tableIdx).getTotalBlockSize();i++)
        {
            //Log.d("getValueList", "add : " + );
            Object o = getObject(i+startIdx);
            if(DataDB.getInstance().table[i+startIdx].data_type == ParamTable.FLOAT_T)
            {

                float fvalue = (float)o;
                fvalue/=10.f;
                Log.d("getValueList", "fvalue : " + fvalue);
                result.add(""+fvalue);
            }
            else {
                result.add(o.toString());
            }
        }
        return result;
    }


    public boolean save(MainActivity me, String fileName){
        byte[] datas = serialize();
        String s = "";
        for(int i=0;i<datas.length;i++)
        {
            if(i%4==0)
                s+="["+(i/4) +"]";
            String ss = String.format("%02x", datas[i]);
            s += ss;
        }
        Log.d("DataContainer:save", s);
        try {
            FileOutputStream fos = new FileOutputStream(new File(me.getFilesDir(), fileName));
            fos.write(datas);
            fos.close();
            mFileName = fileName;
            writerIndices.clear();
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();


        } catch (IOException e) {
            e.printStackTrace();

        }

        return false;
    }



    public void clearWriterIdx(HashSet<Integer> writenIdxs) {
        writerIndices.removeAll(writenIdxs);
    }


    public int getTotalBlockSize()
    {
        int result = 0;
        for(int i=0; i<MAX_IDX;i++) {
            //SubList sub = ;
            result+= getSubList(i).getTotalBlockSize();
        }
        return result;
    }


    public int getWritableBlockSize()
    {
        int result = 0;
        for(int i=0; i<MAX_IDX_WRITABLE;i++) {
            //SubList sub = ;
            result+= getSubList(i).getTotalBlockSize();
        }
        return result;
    }





    public SubList getSubList(int idx)
    {
        if(idx < sublists.length)
            return sublists[idx];

        return null;
    }

    public SubList getSubList(ParamTable.Param_table idx)
    {
        switch(idx)
        {
            case paramlunchings://0
                return sublists[0];
            case paramsettings://1
                return sublists[1];
            case protectionsettings://2
                return sublists[2];
            case externio://3
                return sublists[3];
            case parammotor://4
                return sublists[4];
            case devsettings://5
                return sublists[5];
            case statusinverter://6
                return sublists[6];
            case error://7
                return sublists[7];
        }
        return null;
    }



    private static int getTableOffset(int listIdx)
    {
        ParamTable.Param_table[] listTable = ParamTable.Param_table.values();
        if(listTable.length<=listIdx)
        {
            //System.out.println("getTableOffset : " + listIdx);
            return Integer.MIN_VALUE;
        }

        return ParamTable.getSubjectHead(listTable[listIdx]);

        /*
        switch(listIdx)
        {
            case 0:
                return 0;
            case 1:
                return 23;
            case 2:
                return 35;
            case 3:
                return 44;
            case 4:
                return 61;
            case 5:
                return 69;
            case 6:
                return 73;
            case 7:
                return 83;
            default:
                return Integer.MIN_VALUE;

        }
        */
    }

    public static int getTableIdx(int varIdx)
    {

        Param_idx[] paramTable = Param_idx.values();

        if(varIdx >= paramTable.length) {
            return -1;
        }
        if(varIdx<0)
        {
            return -1;
        }

        ParamTable.Param_table[] listTable = ParamTable.Param_table.values();

        for(int i=0; i<listTable.length ; i++)
        {
            int currHead = ParamTable.getSubjectHead(listTable[i]);
            int currCount = ParamTable.getSubjectCount(listTable[i]);

            if(varIdx>=currHead && varIdx <currHead +currCount)
            {
                return i;
            }

        }

        return -1;
    }

    public static int getDetailIdx(int varIdx)
    {
        if(varIdx<0||varIdx>=103)
            return -1;
        return varIdx-getTableIdx(varIdx);
    }

    public static int getVarIdx(int listIdx, int detailIdx)
    {
        int value = getTableOffset(listIdx)+detailIdx;
        if(value<0)
            return Integer.MIN_VALUE;
        return value;
    }

    public void addWriterIndex(int listIdx, int detailIdx)
    {
        int value = getVarIdx(listIdx, detailIdx);
        if(value<0)
            return;
        writerIndices.add(value);
    }



    public void updateDB(HashMap<Integer, byte[]> writeMap)
    {
        for (int i:writeMap.keySet())
        {
            byte[] value = writeMap.get(i);
            setValue(i, value);
            writerIndices.remove(i);
        }
    }

    public HashMap<Integer, Integer> getWriterByteValues()
    {
        HashMap<Integer, Integer> table = new HashMap<>();
        for (int i:writerIndices)
        {
            int value = ByteBuffer.wrap(getValue(i)).getInt();
            table.put(i, value);

        }
        return table;
    }

    public HashMap<Short, Integer> getWriterByteValuesAll()
    {
        HashMap<Short, Integer> table = new HashMap<>();
        for (Short i=0;i<getTotalBlockSize();i++)
        {
            int value = ByteBuffer.wrap(getValue(i)).getInt();
            int HeadAddr = getSubList(getTableIdx(i)).getDefaultAddress() + (i-getTableOffset(i));
            table.put((short)HeadAddr, value);
        }
        return table;
    }

    public int[] getWriterIntValuesTable(int subIdx)
    {
        SubList sub = getSubList(subIdx);
        int size = sub.getTotalBlockSize();
        int[] result = new int[size];

        for (int i=0;i<size;i++)
        {
            result[i] = ByteBuffer.wrap(getValue(i)).getInt();
            //table.put(i, value);
        }
        return result;
    }

    public int[] getWriterIntValuesAll()
    {
        int max_writable_idx = getWritableBlockSize();
        int[] result = new int[max_writable_idx ];

        for (int i=0;i<max_writable_idx ;i++)
        {
            result[i] = ByteBuffer.wrap(getValue(i)).getInt();
        }
        return result;
    }

    public int[] getWriterIntValuesByIdxs(int[] idxs)
    {
        int max_writable_idx = getWritableBlockSize();
        int[] result = new int[idxs.length];

        for (int i=0;i<idxs.length;i++)
        {
            result[i] = ByteBuffer.wrap(getValue(idxs[i])).getInt();
        }
        return result;
    }


    public DataContainer()
    {
        mFileName = "defaultFile";

        ParamTable.Param_table[] listTable = ParamTable.Param_table.values();
        sublists = new GeneralSubList[listTable.length];
        for(int idx = 0; idx<listTable.length; idx++)
        {
            sublists[idx] = new GeneralSubList(listTable [idx]);
        }
        /*System.out.println("init DataContainer");*/
    }

    @Override
    public String toString() {
        String result ="";
        for(int i=0;i<getTotalBlockSize();i++)
        {
            result += getObject(i) + ", ";
        }
        return result;
    }

    public boolean setValue(int varIdx, byte[] bytes)
    {
        byte[] value = new byte[4];
        if(varIdx<0 || varIdx> Param_idx.values().length)
        {
            return false;
        }

        int ivar = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
        String s = String.format("%02x:%02x:%02x:%02x", bytes[0],bytes[1],bytes[2],bytes[3]);
        Log.d("DataContainer", "setValue : [" +varIdx +"]" + ivar + ", bytes = " + s);

        return DataDB.getDBInstance().setValue(varIdx, ivar);

        /*
        float fvar = -1;
        int ivar = -1;

        if(varIdx<0)	{				}
        else if(varIdx==0)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramlunchings.value < paramlunchings.value_range[0]||paramlunchings.value > paramlunchings.value_range[2]) return false;	paramlunchings.value = fvar;	}
        else if(varIdx<9)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramlunchings.multi_val[varIdx-1] < paramlunchings.multi_val_range[0]||paramlunchings.multi_val[varIdx-1] > paramlunchings.multi_val_range[2]) return false;	paramlunchings.multi_val[varIdx-1] = fvar;	}
        else if(varIdx==9)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramlunchings.freq_min < paramlunchings.freq_min_range[0]||paramlunchings.freq_min > paramlunchings.freq_min_range[2]) return false;	paramlunchings.freq_min = fvar;	}
        else if(varIdx==10)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramlunchings.freq_max < paramlunchings.freq_max_range[0]||paramlunchings.freq_max > paramlunchings.freq_max_range[2]) return false;	paramlunchings.freq_max = fvar;	}
        else if(varIdx==11)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramlunchings.accel_time < paramlunchings.accel_time_range[0]||paramlunchings.accel_time > paramlunchings.accel_time_range[2]) return false;	paramlunchings.accel_time = fvar;	}
        else if(varIdx==12)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramlunchings.decel_time < paramlunchings.decel_time_range[0]||paramlunchings.decel_time > paramlunchings.decel_time_range[2]) return false;	paramlunchings.decel_time = fvar;	}
        else if(varIdx<16)	{	Bool_t var = Bool_t.get(bytes[0]);		paramlunchings.jmp_enable[varIdx-13] = var;	}
        else if(varIdx<19)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramlunchings.jmp_low[varIdx-16] < paramlunchings.jmp_low_range[0]||paramlunchings.jmp_low[varIdx-16] > paramlunchings.jmp_low_range[2]) return false;	paramlunchings.jmp_low[varIdx-16] = fvar;	}
        else if(varIdx<22)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramlunchings.jmp_high[varIdx-19] < paramlunchings.jmp_high_range[0]||paramlunchings.jmp_high[varIdx-19] > paramlunchings.jmp_high_range[2]) return false;	paramlunchings.jmp_high[varIdx-19] = fvar;	}
        else if(varIdx==22)	{	RotDir3_t var = RotDir3_t.get(bytes[0]);		paramlunchings.direction = var;	}

        else if(varIdx==23)	{	InputComm_t var = InputComm_t.get(bytes[0]);		paramsettings.ctrl_in = var;	}
        else if(varIdx==24)	{	RunComm_t var = RunComm_t.get(bytes[0]);		paramsettings.vf_foc_sel = var;	}
        else if(varIdx==25)	{	EnergySave_t var = EnergySave_t.get(bytes[0]);		paramsettings.energy_save = var;	}
        else if(varIdx==26)	{	Freq_t var = Freq_t.get(bytes[0]);		paramsettings.pwm_freq = var;	}
        else if(varIdx==27)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramsettings.v_boost < paramsettings.v_boost_range[0]||paramsettings.v_boost > paramsettings.v_boost_range[2]) return false;	paramsettings.v_boost = fvar;	}
        else if(varIdx==28)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramsettings.foc_torque_limit < paramsettings.foc_torque_limit_range[0]||paramsettings.foc_torque_limit > paramsettings.foc_torque_limit_range[2]) return false;	paramsettings.foc_torque_limit = fvar;	}
        else if(varIdx==29)	{	Stop_t var = Stop_t.get(bytes[0]);		paramsettings.brake_type = var;	}
        else if(varIdx==30)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramsettings.brake_freq < paramsettings.brake_freq_range[0]||paramsettings.brake_freq > paramsettings.brake_freq_range[2]) return false;	paramsettings.brake_freq = fvar;	}
        else if(varIdx==31)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramsettings.dci_brake_freq < paramsettings.dci_brake_freq_range[0]||paramsettings.dci_brake_freq > paramsettings.dci_brake_freq_range[2]) return false;	paramsettings.dci_brake_freq = fvar;	}
        else if(varIdx==32)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramsettings.dci_brake_block_time < paramsettings.dci_brake_block_time_range[0]||paramsettings.dci_brake_block_time > paramsettings.dci_brake_block_time_range[2]) return false;	paramsettings.dci_brake_block_time = fvar;	}
        else if(varIdx==33)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(paramsettings.dci_brake_time < paramsettings.dci_brake_time_range[0]||paramsettings.dci_brake_time > paramsettings.dci_brake_time_range[2]) return false;	paramsettings.dci_brake_time = fvar;	}
        else if(varIdx==34)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;	if(paramsettings.dci_brake_rate < paramsettings.dci_brake_rate_range[0]||paramsettings.dci_brake_rate > paramsettings.dci_brake_rate_range[2]) return false;	paramsettings.dci_brake_rate = ivar;	}

        else if(varIdx==35)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;	if(protectionsettings.ovl_warn_limit < protectionsettings.ovl_warn_limit_range[0]||protectionsettings.ovl_warn_limit > protectionsettings.ovl_warn_limit_range[2]) return false;	protectionsettings.ovl_warn_limit = ivar;	}
        else if(varIdx==36)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;	if(protectionsettings.ovl_warn_duration < protectionsettings.ovl_warn_duration_range[0]||protectionsettings.ovl_warn_duration > protectionsettings.ovl_warn_duration_range[2]) return false;	protectionsettings.ovl_warn_duration = ivar;	}
        else if(varIdx==37)	{	Bool_t var = Bool_t.get(bytes[0]);		protectionsettings.ovl_enable = var;	}
        else if(varIdx==38)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;	if(protectionsettings.ovl_trip_limit < protectionsettings.ovl_trip_limit_range[0]||protectionsettings.ovl_trip_limit > protectionsettings.ovl_trip_limit_range[2]) return false;	protectionsettings.ovl_trip_limit = ivar;	}
        else if(varIdx==39)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;	if(protectionsettings.ovl_trip_duration < protectionsettings.ovl_trip_duration_range[0]||protectionsettings.ovl_trip_duration > protectionsettings.ovl_trip_duration_range[2]) return false;	protectionsettings.ovl_trip_duration = ivar;	}
        else if(varIdx==40)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(protectionsettings.regen_resistance < protectionsettings.regen_resistance_range[0]||protectionsettings.regen_resistance > protectionsettings.regen_resistance_range[2]) return false;	protectionsettings.regen_resistance = fvar;	}
        else if(varIdx==41)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(protectionsettings.regen_thermal < protectionsettings.regen_thermal_range[0]||protectionsettings.regen_thermal > protectionsettings.regen_thermal_range[2]) return false;	protectionsettings.regen_thermal = fvar;	}
        else if(varIdx==42)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;	if(protectionsettings.regen_power < protectionsettings.regen_power_range[0]||protectionsettings.regen_power > protectionsettings.regen_power_range[2]) return false;	protectionsettings.regen_power = ivar;	}
        else if(varIdx==43)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;	if(protectionsettings.regen_band < protectionsettings.regen_band_range[0]||protectionsettings.regen_band > protectionsettings.regen_band_range[2]) return false;	protectionsettings.regen_band = ivar;	}

        else if(varIdx<49)	{	Bool_t var = Bool_t.get(bytes[0]);		externio.multi_Din[varIdx-44] = var;	}
        else if(varIdx==49)	{	Dout_t var = Dout_t.get(bytes[0]);		externio.multi_Dout = var;	}
        else if(varIdx==50)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;	if(externio.multi_din_cnt < externio.multi_din_cnt_range[0]||externio.multi_din_cnt > externio.multi_din_cnt_range[2]) return false;	externio.multi_din_cnt = ivar;	}
        else if(varIdx==51)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(externio.v_in_min < externio.v_in_min_range[0]||externio.v_in_min > externio.v_in_min_range[2]) return false;	externio.v_in_min = fvar;	}
        else if(varIdx==52)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(externio.v_in_min_freq < externio.v_in_min_freq_range[0]||externio.v_in_min_freq > externio.v_in_min_freq_range[2]) return false;	externio.v_in_min_freq = fvar;	}
        else if(varIdx==53)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(externio.v_in_max < externio.v_in_max_range[0]||externio.v_in_max > externio.v_in_max_range[2]) return false;	externio.v_in_max = fvar;	}
        else if(varIdx==54)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(externio.v_in_max_freq < externio.v_in_max_freq_range[0]||externio.v_in_max_freq > externio.v_in_max_freq_range[2]) return false;	externio.v_in_max_freq = fvar;	}
        else if(varIdx==55)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(externio.i_in_min < externio.i_in_min_range[0]||externio.i_in_min > externio.i_in_min_range[2]) return false;	externio.i_in_min = fvar;	}
        else if(varIdx==56)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(externio.i_in_min_freq < externio.i_in_min_freq_range[0]||externio.i_in_min_freq > externio.i_in_min_freq_range[2]) return false;	externio.i_in_min_freq = fvar;	}
        else if(varIdx==57)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(externio.i_in_max < externio.i_in_max_range[0]||externio.i_in_max > externio.i_in_max_range[2]) return false;	externio.i_in_max = fvar;	}
        else if(varIdx==58)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();	if(externio.i_in_max_freq < externio.i_in_max_freq_range[0]||externio.i_in_max_freq > externio.i_in_max_freq_range[2]) return false;	externio.i_in_max_freq = fvar;	}
        else if(varIdx==59)	{	Aout_t var = Aout_t.get(bytes[0]);		externio.aout_type = var;	}
        else if(varIdx==60)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;	if(externio.aout_rate < externio.aout_rate_range[0]||externio.aout_rate > externio.aout_rate_range[2]) return false;	externio.aout_rate = ivar;	}

        else if(varIdx==61)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();		parammotor.Rs = fvar;	}
        else if(varIdx==62)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();		parammotor.Rr = fvar;	}
        else if(varIdx==63)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();		parammotor.Ls = fvar;	}
        else if(varIdx==64)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();		parammotor.noload_current = fvar;	}
        else if(varIdx==65)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();		parammotor.rated_current = fvar;	}
        else if(varIdx==66)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;		parammotor.poles = ivar;	}
        else if(varIdx==67)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;		parammotor.input_voltage = ivar;	}
        else if(varIdx==68)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;		parammotor.rated_freq = ivar;	}

        else if(varIdx==69)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;		devsettings.model = ivar;	}
        else if(varIdx==70)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;		devsettings.motor_type = ivar;	}
        else if(varIdx==71)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;		devsettings.gear_type = ivar;	}
        else if(varIdx==72)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;		devsettings.gear_ratio = ivar;	}

        else if(varIdx==73)	{	Bool_t var = Bool_t.get(bytes[0]);		statusinverter.is_stopped = var;	}
        else if(varIdx==74)	{	RotDir2_t var = RotDir2_t.get(bytes[0]);		statusinverter.dir_status = var;	}
        else if(varIdx==75)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();		statusinverter.I_rms = fvar;	}
        else if(varIdx==76)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();		statusinverter.run_freq = fvar;	}
        else if(varIdx==77)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();		statusinverter.dc_voltage = fvar;	}
        else if(varIdx==78)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();		statusinverter.ipm_temperature = fvar;	}
        else if(varIdx==79)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();		statusinverter.motor_temperature = fvar;	}
        else if(varIdx==80)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;		statusinverter.motor_on_count = ivar;	}
        else if(varIdx==81)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;		statusinverter.elapsed_hour = ivar;	}
        else if(varIdx==82)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;		statusinverter.operating_hour = ivar;	}

        else if(varIdx<88)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;		error.err_code[varIdx-83] = ivar;	}
        else if(varIdx<93)	{	ivar = bytes[0] + bytes[1]<<8 + bytes[2]<<16 + bytes[3]<<24;		error.err_status[varIdx-88] = ivar;	}
        else if(varIdx<98)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();		error.err_current[varIdx-93] = fvar;	}
        else if(varIdx<103)	{	fvar = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();		error.err_freq[varIdx-98] = fvar;	}

*/
    }

    public Object getType(int varIdx){

        return getObject(varIdx).getClass();

    }

    public void setValue(int varIdx, String valueString) throws NumberFormatException
    {

        if(varIdx<0 || varIdx> Param_idx.values().length)
        {
            return;
        }
        Parameter p =  DataDB.getInstance().table[varIdx];
        int var;
        if(p.data_type == ParamTable.FLOAT_T)
        {
            float float_var = Float.parseFloat(valueString);
            float_var*=10;
            var = (int)float_var;
            DataDB.getDBInstance().setValue(varIdx, var);
        }
        else
        {
            var = Integer.parseInt(valueString);
            DataDB.getDBInstance().setValue(varIdx, var);
        }
        /*

        if(varIdx<0 || varIdx>=103)	{			}
        else if(varIdx==0)	{	float var = Float.parseFloat(valueString);	paramlunchings.value = var;	}
        else if(varIdx<9)	{	float var = Float.parseFloat(valueString);	paramlunchings.multi_val[varIdx-1] = var;	}
        else if(varIdx==9)	{	float var = Float.parseFloat(valueString);	paramlunchings.freq_min = var;	}
        else if(varIdx==10)	{	float var = Float.parseFloat(valueString);	paramlunchings.freq_max = var;	}
        else if(varIdx==11)	{	float var = Float.parseFloat(valueString);	paramlunchings.accel_time = var;	}
        else if(varIdx==12)	{	float var = Float.parseFloat(valueString);	paramlunchings.decel_time = var;	}
        else if(varIdx<16)	{	Bool_t var = Bool_t.get(Byte.parseByte(valueString));	paramlunchings.jmp_enable[varIdx-13] = var;	}
        else if(varIdx<19)	{	float var = Float.parseFloat(valueString);	paramlunchings.jmp_low[varIdx-16] = var;	}
        else if(varIdx<22)	{	float var = Float.parseFloat(valueString);	paramlunchings.jmp_high[varIdx-19] = var;	}
        else if(varIdx==22)	{	RotDir3_t var = RotDir3_t.get(Byte.parseByte(valueString));	paramlunchings.direction = var;	}

        else if(varIdx==23)	{	InputComm_t var =  InputComm_t.get(Byte.parseByte(valueString));;	paramsettings.ctrl_in = var;	}
        else if(varIdx==24)	{	RunComm_t var =  RunComm_t.get(Byte.parseByte(valueString));;	paramsettings.vf_foc_sel = var;	}
        else if(varIdx==25)	{	EnergySave_t var =  EnergySave_t.get(Byte.parseByte(valueString));;	paramsettings.energy_save = var;	}
        else if(varIdx==26)	{	Freq_t var =  Freq_t.get(Byte.parseByte(valueString));;	paramsettings.pwm_freq = var;	}
        else if(varIdx==27)	{	float var = Float.parseFloat(valueString);	paramsettings.v_boost = var;	}
        else if(varIdx==28)	{	float var = Float.parseFloat(valueString);	paramsettings.foc_torque_limit = var;	}
        else if(varIdx==29)	{	Stop_t var =  Stop_t.get(Byte.parseByte(valueString));;	paramsettings.brake_type = var;	}
        else if(varIdx==30)	{	float var = Float.parseFloat(valueString);	paramsettings.brake_freq = var;	}
        else if(varIdx==31)	{	float var =Float.parseFloat(valueString);	paramsettings.dci_brake_freq = var;	}
        else if(varIdx==32)	{	float var = Float.parseFloat(valueString);	paramsettings.dci_brake_block_time = var;	}
        else if(varIdx==33)	{	float var = Float.parseFloat(valueString);	paramsettings.dci_brake_time = var;	}
        else if(varIdx==34)	{	int var = Integer.parseInt(valueString);	paramsettings.dci_brake_rate = var;	}

        else if(varIdx==35)	{	int var = Integer.parseInt(valueString); protectionsettings.ovl_warn_limit = var;	}
        else if(varIdx==36)	{	int var = Integer.parseInt(valueString);	protectionsettings.ovl_warn_duration = var;	}
        else if(varIdx==37)	{	Bool_t var =  Bool_t.get(Byte.parseByte(valueString));;	protectionsettings.ovl_enable = var;	}
        else if(varIdx==38)	{	int var = Integer.parseInt(valueString);	protectionsettings.ovl_trip_limit = var;	}
        else if(varIdx==39)	{	int var = Integer.parseInt(valueString);	protectionsettings.ovl_trip_duration = var;	}
        else if(varIdx==40)	{	float var = Float.parseFloat(valueString);	protectionsettings.regen_resistance = var;	}
        else if(varIdx==41)	{	float var = Float.parseFloat(valueString);	protectionsettings.regen_thermal = var;	}
        else if(varIdx==42)	{	int var = Integer.parseInt(valueString);;	protectionsettings.regen_power = var;	}
        else if(varIdx==43)	{	int var = Integer.parseInt(valueString);protectionsettings.regen_band = var;	}

        else if(varIdx<49)	{	Bool_t var =  Bool_t.get(Byte.parseByte(valueString));	externio.multi_Din[varIdx-44] = var;	}
        else if(varIdx==49)	{	Dout_t var = Dout_t.get(Byte.parseByte(valueString));	externio.multi_Dout = var;	}
        else if(varIdx==50)	{	int var = Integer.parseInt(valueString);	externio.multi_din_cnt = var;	}
        else if(varIdx==51)	{	float var = Float.parseFloat(valueString);	externio.v_in_min = var;	}
        else if(varIdx==52)	{	float var = Float.parseFloat(valueString);	externio.v_in_min_freq = var;	}
        else if(varIdx==53)	{	float var = Float.parseFloat(valueString);	externio.v_in_max = var;	}
        else if(varIdx==54)	{	float var = Float.parseFloat(valueString);	externio.v_in_max_freq = var;	}
        else if(varIdx==55)	{	float var = Float.parseFloat(valueString);	externio.i_in_min = var;	}
        else if(varIdx==56)	{	float var = Float.parseFloat(valueString);	externio.i_in_min_freq = var;	}
        else if(varIdx==57)	{	float var = Float.parseFloat(valueString);	externio.i_in_max = var;	}
        else if(varIdx==58)	{	float var = Float.parseFloat(valueString);	externio.i_in_max_freq = var;	}
        else if(varIdx==59)	{	Aout_t var =  Aout_t.get(Byte.parseByte(valueString));;	externio.aout_type = var;	}
        else if(varIdx==60)	{	int var = Integer.parseInt(valueString);	externio.aout_rate = var;	}

        else if(varIdx==61)	{	float var = Float.parseFloat(valueString);	parammotor.Rs = var;	}
        else if(varIdx==62)	{	float var = Float.parseFloat(valueString);	parammotor.Rr = var;	}
        else if(varIdx==63)	{	float var = Float.parseFloat(valueString);	parammotor.Ls = var;	}
        else if(varIdx==64)	{	float var = Float.parseFloat(valueString);	parammotor.noload_current = var;	}
        else if(varIdx==65)	{	float var = Float.parseFloat(valueString);	parammotor.rated_current = var;	}
        else if(varIdx==66)	{	int var = Integer.parseInt(valueString);parammotor.poles = var;	}
        else if(varIdx==67)	{	int var = Integer.parseInt(valueString);parammotor.input_voltage = var;	}
        else if(varIdx==68)	{	int var = Integer.parseInt(valueString);	parammotor.rated_freq = var;	}

        else if(varIdx==69)	{	int var = Integer.parseInt(valueString);	devsettings.model = var;	}
        else if(varIdx==70)	{	int var = Integer.parseInt(valueString);devsettings.motor_type = var;	}
        else if(varIdx==71)	{	int var = Integer.parseInt(valueString);devsettings.gear_type = var;	}
        else if(varIdx==72)	{	int var = Integer.parseInt(valueString);devsettings.gear_ratio = var;	}

        else if(varIdx==73)	{	Bool_t var =  Bool_t.get(Byte.parseByte(valueString));;	statusinverter.is_stopped = var;	}
        else if(varIdx==74)	{	RotDir2_t var =  RotDir2_t.get(Byte.parseByte(valueString));;	statusinverter.dir_status = var;	}
        else if(varIdx==75)	{	float var = Float.parseFloat(valueString);	statusinverter.I_rms = var;	}
        else if(varIdx==76)	{	float var = Float.parseFloat(valueString);	statusinverter.run_freq = var;	}
        else if(varIdx==77)	{	float var = Float.parseFloat(valueString);	statusinverter.dc_voltage = var;	}
        else if(varIdx==78)	{	float var = Float.parseFloat(valueString);	statusinverter.ipm_temperature = var;	}
        else if(varIdx==79)	{	float var = Float.parseFloat(valueString);	statusinverter.motor_temperature = var;	}
        else if(varIdx==80)	{	int var = Integer.parseInt(valueString);statusinverter.motor_on_count = var;	}
        else if(varIdx==81)	{	int var = Integer.parseInt(valueString);statusinverter.elapsed_hour = var;	}
        else if(varIdx==82)	{	int var = Integer.parseInt(valueString);	statusinverter.operating_hour = var;	}

        else if(varIdx<88)	{	int var = Integer.parseInt(valueString);;	error.err_code[varIdx-83] = var;	}
        else if(varIdx<93)	{	int var = Integer.parseInt(valueString);;	error.err_status[varIdx-88] = var;	}
        else if(varIdx<98)	{	float var = Float.parseFloat(valueString);	error.err_current[varIdx-93] = var;	}
        else if(varIdx<103)	{	float var = Float.parseFloat(valueString);	error.err_freq[varIdx-98] = var;	}
*/
    }


    public byte[] getValue(int varIdx)
    {
        Object o = getObject(varIdx);
        Log.d(TAG, "["+varIdx+"] class = " + o.getClass() + ", val = " + o);
        if(o.getClass()==Float.class)
        {

            float value = (float)o;
            int ivalue = (int)(value);
            byte[] result = ByteBuffer.allocate(4).putInt(ivalue).order(ByteOrder.LITTLE_ENDIAN).array();
            //byte[] result = ByteBuffer.allocate(4).putInt(ivalue).order(ByteOrder.LITTLE_ENDIAN).array();
            //Log.d(TAG, "case float = " + result[0] + ":" + result[1] + ":" + result[2] + ":" + result[3] + ":" );
            return result;
        }
        int value;
        if(o.getClass()==Integer.class)
        {
            //Log.d(TAG, "case int");
            value = (int)o;
        }
        else if(o.getClass()==Bool_t.class)
        {
            //Log.d(TAG, "case Bool_t");
            value = ((Bool_t)o).get();
        }
        else if(o.getClass()==Aout_t.class)
        {
            //Log.d(TAG, "case Aout_t");
            value = ((Aout_t)o).get();
        }
        else if(o.getClass()==Dout_t.class)
        {
            //Log.d(TAG, "case Dout_t");
            value = ((Dout_t)o).get();
        }
        else if(o.getClass()==EnergySave_t.class)
        {
            //Log.d(TAG, "case EnergySave_t");
            value = ((EnergySave_t)o).get();
        }
        else if(o.getClass()==Freq_t.class)
        {
            //Log.d(TAG, "case Freq_t");
            value = ((Freq_t)o).get();
        }
        else if(o.getClass()==InputComm_t.class)
        {
            //Log.d(TAG, "case InputComm_t");
            value = ((InputComm_t)o).get();
        }
        else if(o.getClass()==RotDir2_t.class)
        {
            //Log.d(TAG, "case RotDir2_t");
            value = ((RotDir2_t)o).get();
        }
        else if(o.getClass()==RunComm_t.class)
        {
            //Log.d(TAG, "case RunComm_t");
            value = ((RunComm_t)o).get();
        }
        else if(o.getClass()==Stop_t.class)
        {
            //Log.d(TAG, "case Stop_t");
            value = ((Stop_t)o).get();
        }
        else if(o.getClass()==RotDir3_t.class)
        {
            //Log.d(TAG, "case RotDir3_t");
            value = ((RotDir3_t)o).get();
        }
        else
        {
            value = 0;
        }
        return ByteBuffer.allocate(4).putInt(value).array();
    }

    public Object getObject(int varIdx)
    {
        if(varIdx<0 || varIdx> Param_idx.values().length)
        {
            return new byte[]{0,0,0,0};
        }
        //ParamTable.Parameter p =  DataDB.getInstance().table[varIdx];

        return DataDB.getDBInstance().getValue(varIdx);
        /*

        if(varIdx<0)	{			}
        else if(varIdx==0) return paramlunchings.value;
        else if(varIdx<9) return paramlunchings.multi_val[varIdx-1];
        else if(varIdx==9) return paramlunchings.freq_min;
        else if(varIdx==10) return paramlunchings.freq_max;
        else if(varIdx==11) return paramlunchings.accel_time;
        else if(varIdx==12) return paramlunchings.decel_time;
        else if(varIdx<16) return paramlunchings.jmp_enable[varIdx-13];
        else if(varIdx<19) return paramlunchings.jmp_low[varIdx-16];
        else if(varIdx<22) return paramlunchings.jmp_high[varIdx-19];
        else if(varIdx==22) return paramlunchings.direction;

        else if(varIdx==23) return paramsettings.ctrl_in;
        else if(varIdx==24) return paramsettings.vf_foc_sel;
        else if(varIdx==25) return paramsettings.energy_save;
        else if(varIdx==26) return paramsettings.pwm_freq;
        else if(varIdx==27) return paramsettings.v_boost;
        else if(varIdx==28) return paramsettings.foc_torque_limit;
        else if(varIdx==29) return paramsettings.brake_type;
        else if(varIdx==30) return paramsettings.brake_freq;
        else if(varIdx==31) return paramsettings.dci_brake_freq;
        else if(varIdx==32) return paramsettings.dci_brake_block_time;
        else if(varIdx==33) return paramsettings.dci_brake_time;
        else if(varIdx==34) return paramsettings.dci_brake_rate;

        else if(varIdx==35) return protectionsettings.ovl_warn_limit;
        else if(varIdx==36) return protectionsettings.ovl_warn_duration;
        else if(varIdx==37) return protectionsettings.ovl_enable;
        else if(varIdx==38) return protectionsettings.ovl_trip_limit;
        else if(varIdx==39) return protectionsettings.ovl_trip_duration;
        else if(varIdx==40) return protectionsettings.regen_resistance;
        else if(varIdx==41) return protectionsettings.regen_thermal;
        else if(varIdx==42) return protectionsettings.regen_power;
        else if(varIdx==43) return protectionsettings.regen_band;

        else if(varIdx<49) return externio.multi_Din[varIdx-44];
        else if(varIdx==49) return externio.multi_Dout;
        else if(varIdx==50) return externio.multi_din_cnt;
        else if(varIdx==51) return externio.v_in_min;
        else if(varIdx==52) return externio.v_in_min_freq;
        else if(varIdx==53) return externio.v_in_max;
        else if(varIdx==54) return externio.v_in_max_freq;
        else if(varIdx==55) return externio.i_in_min;
        else if(varIdx==56) return externio.i_in_min_freq;
        else if(varIdx==57) return externio.i_in_max;
        else if(varIdx==58) return externio.i_in_max_freq;
        else if(varIdx==59) return externio.aout_type;
        else if(varIdx==60) return externio.aout_rate;

        else if(varIdx==61) return parammotor.Rs;
        else if(varIdx==62) return parammotor.Rr;
        else if(varIdx==63) return parammotor.Ls;
        else if(varIdx==64) return parammotor.noload_current;
        else if(varIdx==65) return parammotor.rated_current;
        else if(varIdx==66) return parammotor.poles;
        else if(varIdx==67) return parammotor.input_voltage;
        else if(varIdx==68) return parammotor.rated_freq;

        else if(varIdx==69) return devsettings.model;
        else if(varIdx==70) return devsettings.motor_type;
        else if(varIdx==71) return devsettings.gear_type;
        else if(varIdx==72) return devsettings.gear_ratio;

        else if(varIdx==73) return statusinverter.is_stopped;
        else if(varIdx==74) return statusinverter.dir_status;
        else if(varIdx==75) return statusinverter.I_rms;
        else if(varIdx==76) return statusinverter.run_freq;
        else if(varIdx==77) return statusinverter.dc_voltage;
        else if(varIdx==78) return statusinverter.ipm_temperature;
        else if(varIdx==79) return statusinverter.motor_temperature;
        else if(varIdx==80) return statusinverter.motor_on_count;
        else if(varIdx==81) return statusinverter.elapsed_hour;
        else if(varIdx==82) return statusinverter.operating_hour;

        else if(varIdx<88) return error.err_code[varIdx-83];
        else if(varIdx<93) return error.err_status[varIdx-88];
        else if(varIdx<98) return error.err_current[varIdx-93];
        else if(varIdx<103) return error.err_freq[varIdx-98];
        return new byte[]{0,0,0,0};
        */
    }
    int getTableCRC()
    {
        return (int)DataDB.getDBInstance().getCRC();
    }

}
