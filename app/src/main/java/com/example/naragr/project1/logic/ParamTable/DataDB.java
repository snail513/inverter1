package com.example.naragr.project1.logic.ParamTable;

import android.util.Log;

import com.example.naragr.project1.logic.DataContainer;
import com.example.naragr.project1.logic.ParamSubject.SubList;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class DataDB {
    public static ParamTable tableInstance = new ParamTable();
    public static ParamTable getInstance (){
        return tableInstance;
    }

    private int datas[];
    private static int rangedSize;
    private static DataDB db = new DataDB().init();
    public static DataDB getDBInstance(){
        return db;
    }

    public DataDB()
    {
        datas = new int[getInstance().table.length];
        init();
    }

    public DataDB init()
    {
        rangedSize=0;
        for(int i=0; i<tableInstance.table.length;i++)
        {
            Parameter param = tableInstance.table[i];
            if(param.isRanged)
            {
                datas[i] = param.initVal;
                rangedSize ++;
            }
            else
            {
                datas[i] = 0;
            }
        }
        return this;
    }

    private boolean testSpecialRanges(int idx, int intValue)
    {
        if(idx >= rangedSize)
            return true;

        int minValue=0, maxValue=0;
        //minTest

        if(idx >=0 && idx <=8)
        {
            minValue = datas[Param_idx.freq_min.ordinal()];
            maxValue = datas[Param_idx.freq_max.ordinal()];
        }
        else if(idx==Param_idx.freq_max.ordinal())
        {
            minValue = datas[Param_idx.freq_min.ordinal()];
            maxValue = tableInstance.table[idx].maxVal;
        }
        else if(idx==Param_idx.freq_min.ordinal())
        {
            minValue = tableInstance.table[idx].minVal;
            maxValue = datas[Param_idx.freq_max.ordinal()];
        }

        else if(idx==Param_idx.direction_control.ordinal())
        {

            int direction = datas[Param_idx.direction_domain.ordinal()];
            if(direction == 0) {
                minValue = 0;
                maxValue = 1;
            }
            else if (direction == 1)
            {
                minValue = 0;
                maxValue = 0;
            }
            else if(direction ==2)
            {
                minValue = 1;
                maxValue = 1;
            }
        }
/////////////////////////
        else if(idx == Param_idx.jmp_high0.ordinal())
        {
            minValue = datas[Param_idx.jmp_low0.ordinal()];
            maxValue = tableInstance.table[idx].maxVal;
        }

        else if(idx == Param_idx.jmp_high1.ordinal())
        {
            minValue = datas[Param_idx.jmp_low1.ordinal()];
            maxValue = tableInstance.table[idx].maxVal;
        }

        else if(idx == Param_idx.jmp_high2.ordinal())
        {
            minValue = datas[Param_idx.jmp_low2.ordinal()];
            maxValue = tableInstance.table[idx].maxVal;
        }
        else if(idx == Param_idx.v_in_max_freq.ordinal())
        {
            minValue = datas[Param_idx.v_in_min_freq.ordinal()];
            maxValue = tableInstance.table[idx].maxVal;
        }
        /*else if(idx == Param_idx.i_in_max_freq.ordinal())
        {
            minValue = datas[Param_idx.i_in_min_freq.ordinal()];
            maxValue = tableInstance.table[idx].maxVal;
        }*/
        else if(idx == Param_idx.v_in_max.ordinal())
        {
            minValue = datas[Param_idx.v_in_min.ordinal()];
            maxValue = tableInstance.table[idx].maxVal;
        }
        /*
        else if(idx == Param_idx.i_in_max.ordinal())
        {
            minValue = datas[Param_idx.i_in_min.ordinal()];
            maxValue = tableInstance.table[idx].maxVal;
        }
        */
///////////////

        else if(idx == Param_idx.jmp_low0.ordinal())
        {
            minValue = tableInstance.table[idx].minVal;
            maxValue = datas[Param_idx.jmp_high0.ordinal()];
        }

        else if(idx == Param_idx.jmp_low1.ordinal())
        {
            minValue = tableInstance.table[idx].minVal;
            maxValue = datas[Param_idx.jmp_high1.ordinal()];
        }

        else if(idx == Param_idx.jmp_low2.ordinal())
        {
            minValue = tableInstance.table[idx].minVal;
            maxValue = datas[Param_idx.jmp_high2.ordinal()];
            maxValue = tableInstance.table[idx].maxVal;
        }
        else if(idx == Param_idx.v_in_min_freq.ordinal())
        {
            minValue = tableInstance.table[idx].minVal;
            maxValue = datas[Param_idx.v_in_max_freq.ordinal()];
        }
        /*
        else if(idx == Param_idx.i_in_min_freq.ordinal())
        {
            minValue = tableInstance.table[idx].minVal;
            maxValue = datas[Param_idx.i_in_max_freq.ordinal()];
        }
        */
        else if(idx == Param_idx.v_in_min.ordinal())
        {
            minValue = tableInstance.table[idx].minVal;
            maxValue = datas[Param_idx.v_in_max.ordinal()];
        }
        /*
        else if(idx == Param_idx.i_in_min.ordinal())
        {
            minValue = tableInstance.table[idx].minVal;
            maxValue = datas[Param_idx.i_in_max.ordinal()];
        }
*/
        else
        {
            minValue = tableInstance.table[idx].minVal;
            maxValue = tableInstance.table[idx].maxVal;
        }
        //maxTest
        return intValue<=maxValue && intValue >=minValue;
    }

    public boolean setValue(int idx, Object value)
    {
        int intValue = 0;
        if(tableInstance.isValidIdx(idx))
        {
            switch(tableInstance.table[idx].data_type)
            {
                case ParamTable.AOUT_T:
                    intValue = (int)value;;
                    break;
                case ParamTable.BOOL_T:
                    intValue = (int)value;
                    break;
                case ParamTable.DIN_T:
                    intValue = (int)value;
                    break;
                case ParamTable.DOUT_T:
                    intValue = (int)value;
                    break;
                case ParamTable.ENERGY_SAVE_T:
                    intValue = (int)value;
                    break;
                case ParamTable.FLOAT_T:
                    intValue = (int)value;
                    break;
                case ParamTable.FREQ_T:
                    intValue = (int)value;
                    break;
                case ParamTable.INPUT_COMM_T:
                    intValue = (int)value;
                    break;
                case ParamTable.INT_T:
                    intValue = (int)value;
                    break;
                case ParamTable.ROT_DIR2_T:
                    intValue = (int)value;
                    break;
                case ParamTable.ROT_DIR3_T:
                    intValue = (int)value;
                    break;
                case ParamTable.RUN_COMM_T:
                    intValue = (int)value;
                    break;
                case ParamTable.STOP_T:
                    intValue = (int)value;
                    break;
                case ParamTable.BAUD_T:
                    intValue = (int)value;
                    break;
                case ParamTable.MOTOR_TM_T:
                    intValue = (int)value;
                    break;
                default :
                    value = null;
                    break;
            }

            if(!testSpecialRanges(idx, intValue))
            {
                Log.d("[Failed]SetValue" , "["+idx+"]" +datas[idx] + ", data.ordinal() = " + tableInstance.table[idx].data_type + ", original value = "+value + ", intvalue=" + intValue);
                return false;
            }

            datas[idx] = intValue;

            Log.d("SetValue" , "["+idx+"]" +datas[idx] + ", data.ordinal() = " + tableInstance.table[idx].data_type + ", original value = "+value + ", intvalue=" + intValue);
            return true;
        }
        return false;
    }

    public Object getValue(int idx)
    {
        Object value = null;
        if(tableInstance.isValidIdx(idx))
        {

            switch(tableInstance.table[idx].data_type)
            {

                case ParamTable.AOUT_T:
                    value = Aout_t.get((byte)datas[idx]);
                    break;
                case ParamTable.BOOL_T:
                    value = Bool_t.get((byte)datas[idx]);
                    break;
                case ParamTable.DIN_T:
                    value = Din_t.get((byte)datas[idx]);
                    break;
                case ParamTable.DOUT_T:
                    value = Dout_t.get((byte)datas[idx]);
                    break;
                case ParamTable.ENERGY_SAVE_T:
                    value = EnergySave_t.get((byte)datas[idx]);
                    break;
                case ParamTable.FLOAT_T:
                    value = new Float(datas[idx]);
                    break;
                case ParamTable.FREQ_T:
                    value = Freq_t.get((byte)datas[idx]);
                    break;
                case ParamTable.INPUT_COMM_T:
                    value = InputComm_t.get((byte)datas[idx]);
                    break;
                case ParamTable.INT_T:
                    value = (Integer)datas[idx];
                    break;
                case ParamTable.ROT_DIR2_T:
                    value = RotDir2_t.get((byte)datas[idx]);
                    break;
                case ParamTable.ROT_DIR3_T:
                    value = RotDir3_t.get((byte)datas[idx]);
                    break;
                case ParamTable.RUN_COMM_T:
                    value = RunComm_t.get((byte)datas[idx]);
                    break;
                case ParamTable.STOP_T:
                    value = Stop_t.get((byte)datas[idx]);
                    break;
                case ParamTable.BAUD_T:
                    value = Baud_t.get((byte)datas[idx]);
                    break;
                case ParamTable.MOTOR_TM_T:
                    value = MotorTemp_t.get((byte)datas[idx]);
                    break;
                default :
                    value = null;
                    break;
            }

            Log.d("GetValue:Object","getVal type= "+ tableInstance.table[idx].data_type + ", getVal = " + value+", original datas["+idx+"] = " + datas[idx]);
        }
        return value;
    }

    public ParamTable.Param_table getParamTableOfIdx(int idx)
    {
        if(ParamTable.Param_table.values().length>=idx)
            return null;
        return ParamTable.Param_table.values()[idx];
    }

    public long getCRC() {
        Checksum crc = new CRC32();
        int length = ParamTable.getRangedLength();
        //try {
            ////////////////////////////////////////////////////////////////////////////////
            //BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename));
            ByteBuffer byteBuffer = ByteBuffer.allocate(datas.length * 4);
            IntBuffer intBuffer = byteBuffer.asIntBuffer();
            intBuffer.put(datas);
            byte[] buffer = byteBuffer.array();



            //while ((length = in.read(buffer)) >= 0)
            crc.update(buffer, 0, length);

            //in.close();
            ////////////////////////////////////////////////////////////////////////////////
        /*
        } catch (IOException e) {
            System.err.println(e);
            System.exit(2);
        }
        */
        return crc.getValue();

        /*
        return 0;
        */
    }
/*
    public SubList getSubList(int idx)
    {
        ParamTable.Param_table table = getParamTableOfIdx(idx);
        if(table == null)
            return null;
        return getSubList(table);
    }

    public SubList getSubList(ParamTable.Param_table type)
    {
        SubList subList = null;
        switch(type)
        {
            case paramlunchings:

                break;
            case paramsettings:
                break;
            case protectionsettings:
                break;
            case externio:
                break;
            case parammotor:
                break;
            case devsettings:
                break;
            case statusinverter:
                break;
            case error:
                break;
        }
        return subList;
    }
    */
}
