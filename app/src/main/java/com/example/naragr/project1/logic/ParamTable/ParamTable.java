package com.example.naragr.project1.logic.ParamTable;

import com.example.naragr.project1.logic.DataContainer;

public class ParamTable{





    public final static byte FLOAT_T = 0;
    public final static byte INT_T= 1;
    public final static byte BOOL_T = 2;
    public final static byte DOUT_T = 3;
    public final static byte AOUT_T = 4;
    public final static byte ENERGY_SAVE_T = 5;
    public final static byte FREQ_T = 6;
    public final static byte INPUT_COMM_T = 7;
    public final static byte ROT_DIR2_T = 8;
    public final static byte ROT_DIR3_T = 9;
    public final static byte RUN_COMM_T = 10;
    public final static byte STOP_T = 11;
    public final static byte DIN_T = 12;


    private int getMaxValue(int idx )
    {
        Param_idx pid = getParamIdxValue(idx);
        int maxValue = table[idx].maxVal ;



        return maxValue;
    }

    private int getMinValue(int idx)
    {
        Param_idx pid = getParamIdxValue(idx);
        int minValue = table[idx].minVal ;


        return minValue;
    }

    public boolean isValueInRanged(int idx, int value) {
        if(idx>=table.length)
            return false;
        if(!table[idx].isRanged)
        {
            return true;

        }
        return value <= getMaxValue(idx)&& value >= getMinValue(idx);
    }

    public boolean isValidIdx(int idx) {
        return idx<table.length;
    }

    public static int getSubjectCount(Param_table subject) {
        int count = 0;
        for(Param_table t : tableType)
        {
            if(subject == t)
            {
                count++;
            }
        }
        return count;
    }

    public static int getSubjectHead(Param_table subject) {
        int count = 0;
        for(Param_table t : tableType)
        {
            if(subject.compareTo(t)==0)
            {
                return count;
            }
            count++;
        }
        return -1;
    }

    public class Parameter{
        public Param_idx param_idx;
        public byte data_type;
        public Param_table table_type;
        public boolean isRanged;
        public int initVal;
        public int maxVal;
        public int minVal;
        public int addr;

        public Parameter(
                Param_idx param_idx,
                byte data_type,
                Param_table table_type,
                boolean isRanged,
                int initVal,
                int maxVal,
                int minVal,
                int addr)
        {
            this.param_idx = param_idx;
            this.data_type = data_type;
            this.table_type = table_type;
            this.isRanged = isRanged;
            this.initVal = initVal;
            this.maxVal = maxVal;
            this.minVal = minVal;
            this.addr = addr;
        }

        @Override
        public String toString() {
            String result = "[Param_idx:" + param_idx +
                    ", data_type:" + data_type +
                    ", table_type:" + table_type +
                    ", isRanged:" + isRanged +
                    ", initVal:" + initVal +
                    ", maxVal:" + maxVal +
                    ", minVal:" + minVal +
                    ", addr:" + addr +
                    "]";
            return result;
        }
    }

    public Parameter[] table;


    public ParamTable()
    {
        int size = Param_idx.values().length;
        table = new Parameter[size];
        for(int i = 0 ; i< size; i++)
        {
            Param_idx param_idx = getParamIdxValue(i);
            if(param_isRanged[i])
            {
                table[i] = new Parameter(Param_idx.values()[i], param_type[i], tableType[i], param_isRanged[i], param_init[i], param_max[i], param_min[i], param_addr[i] );
            }
            else
            {
                table[i] = new Parameter(Param_idx.values()[i], param_type[i], tableType[i], param_isRanged[i], 0, 0, 0, param_addr[i] );
            }
        }
        //System.out.println(toString());
    }

    @Override
    public String toString() {
        String result = "[ParamTable]\n";
        for(int i = 0 ; i< table.length; i++)
        {
            result += table[i].toString() + ",\n";
        }

        return result;
    }

    public Param_idx getParamIdxValue(int idx)
    {
        return Param_idx.values()[idx];
    }

    final static int param_addr[]={

        64,
        65,
        66,
        67,
        68,
        69,
        70,
        71,
        72,
        73,
        74,
        75,
        76,
        77,
        78,
        79,
        80,
        81,
        82,
        83,
        84,
        85,
        86,
        87,



        128,
        129,
        130,
        131,
        132,
        133,
        134,
        135,
        136,
        137,
        138,
        139,



        160,
        161,
        162,
        163,
        164,
        165,
        166,
        167,
        168,
            169,



        192,
        193,
        194,
        195,
        196,
        197,
        198,
        199,
        200,
        201,
        202,
        203,
        204,
        205,
        206,
        207,
        208,




        8,
        9,
        10,
        11,
        12,
        13,
        14,
        15,



        24,
        25,
        26,
        27,
        28,



        40,
        41,
        42,
        43,
        44,
        45,
        46,
        47,
        48,
        49,



        224,
        225,
        226,
        227,
        228,
        229,
        230,
        231,
        232,
        233,
        234,
        235,
        236,
        237,
        238,
        239,
        240,
        241,
        242,
        243,
        244,
        245,
        246,
        247,
        248,
        249,
        250,
        251,
        252,
        253,
    };

    public enum Param_table{
        paramlunchings,//0
        paramsettings,//1
        protectionsettings,//2
        externio,//3
        parammotor,//4
        devsettings,//5
        statusinverter,//6
        error,//7
    }

    public static int getTableIdx(Param_table t_idx)
    {
        switch(t_idx)
        {
            case paramlunchings: return 0;
            case paramsettings: return 1;
            case protectionsettings: return 2;
            case externio: return 3;
            case parammotor: return 4;
            case devsettings: return 5;
            case statusinverter: return 6;
            case error: return 7 ;
            default:return -1;
        }
    }

    static final Param_table tableType[] = {

            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,
            Param_table.paramlunchings,



            Param_table.paramsettings,
            Param_table.paramsettings,
            Param_table.paramsettings,
            Param_table.paramsettings,
            Param_table.paramsettings,
            Param_table.paramsettings,
            Param_table.paramsettings,
            Param_table.paramsettings,
            Param_table.paramsettings,
            Param_table.paramsettings,
            Param_table.paramsettings,
            Param_table.paramsettings,




            Param_table.protectionsettings,
            Param_table.protectionsettings,
            Param_table.protectionsettings,
            Param_table.protectionsettings,
            Param_table.protectionsettings,
            Param_table.protectionsettings,
            Param_table.protectionsettings,
            Param_table.protectionsettings,
            Param_table.protectionsettings,
            Param_table.protectionsettings,


            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,
            Param_table.externio,




            Param_table.parammotor,
            Param_table.parammotor,
            Param_table.parammotor,
            Param_table.parammotor,
            Param_table.parammotor,
            Param_table.parammotor,
            Param_table.parammotor,
            Param_table.parammotor,



            Param_table.devsettings,
            Param_table.devsettings,
            Param_table.devsettings,
            Param_table.devsettings,
            Param_table.devsettings,



            Param_table.statusinverter,
            Param_table.statusinverter,
            Param_table.statusinverter,
            Param_table.statusinverter,
            Param_table.statusinverter,
            Param_table.statusinverter,
            Param_table.statusinverter,
            Param_table.statusinverter,
            Param_table.statusinverter,
            Param_table.statusinverter,



            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
            Param_table.error,
    };



    static final boolean TRUE = true;
    static final boolean FALSE = false;

    static boolean param_isRanged[] = {

            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,



            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,



            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,


            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,
            TRUE,




            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,



            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,



            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,



            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
            FALSE,
    };

    public static final byte param_type[] ={

            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            BOOL_T,
            BOOL_T,
            BOOL_T,
            BOOL_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            ROT_DIR3_T,



            INPUT_COMM_T,
            RUN_COMM_T,
            ENERGY_SAVE_T,
            FREQ_T,
            FLOAT_T,
            FLOAT_T,
            STOP_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,



            INT_T,
            INT_T,
            BOOL_T,
            INT_T,
            INT_T,
            FLOAT_T,
            FLOAT_T,
            INT_T,
            INT_T,
            BOOL_T,


            DIN_T,
            DIN_T,
            DIN_T,
            DIN_T,
            DIN_T,
            DOUT_T,
            INT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            AOUT_T,
            INT_T,




            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            INT_T,
            INT_T,
            INT_T,



            INT_T,
            INT_T,
            INT_T,
            INT_T,
            INT_T,



            BOOL_T,
            ROT_DIR2_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            FLOAT_T,
            INT_T,
            INT_T,
            INT_T,



            INT_T,
            INT_T,
            INT_T,
            INT_T,
            FLOAT_T,
            FLOAT_T,
            INT_T,
            INT_T,
            INT_T,
            INT_T,
            FLOAT_T,
            FLOAT_T,
            INT_T,
            INT_T,
            INT_T,
            INT_T,
            FLOAT_T,
            FLOAT_T,
            INT_T,
            INT_T,
            INT_T,
            INT_T,
            FLOAT_T,
            FLOAT_T,
            INT_T,
            INT_T,
            INT_T,
            INT_T,
            FLOAT_T,
            FLOAT_T,
    };

    public static final int param_max[]={

            4000,
            4000,
            4000,
            4000,
            4000,
            4000,
            4000,
            4000,
            4000,
            4000,
            4000,
            6000,
            6000,
            1,
            1,
            1,
            1,
            4000,
            4000,
            4000,
            4000,
            4000,
            4000,
            2,



            6,
            1,
            3,
            3,
            150,
            2000,
            2,
            600,
            600,
            600,
            600,
            2000,



            200,
            30,
            1,
            200,
            60,
            5000,
            65535,
            65535,
            150,
            1,



            8,
            8,
            8,
            8,
            8,
            5,
            3,
            10,
            400,
            10,
            400,
            20,
            400,
            20,
            400,
            3,
            200,
    };


    public static final int param_min[]={

            10,
            10,
            10,
            10,
            10,
            10,
            10,
            10,
            10,
            10,
            10,
            10,
            10,
            0,
            0,
            0,
            0,
            10,
            10,
            10,
            10,
            10,
            10,
            0,



            0,
            0,
            0,
            0,
            0,
            1000,
            0,
            1,
            1,
            0,
            0,
            0,



            100,
            0,
            0,
            100,
            0,
            1500,
            0,
            10,
            0,
            0,



            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            1,
            0,
            1,
            0,
            1,
            0,
            1,
            0,
            10,
    };

    final static int param_init[] = {
        200,
        200,
        200,
        200,
        200,
        200,
        200,
        200,
        200,
        10,
        3000,
        100,
        100,
            0,
        0,
        0,
        0,
        10,
        10,
        10,
        10,
        10,
        10,
        0,

        0,
        0,
        0,
        0,
        0,
        1800,
        0,
        10,
        30,
        10,
        50,
        500,



        150,
        10,
        1,
        180,
        30,
        2000,
        10000,
        400,
        12,
            0,



        0,
        0,
        0,
        0,
        0,
        0,
        0,
        0,
        1,
        10,
        400,
        0,
        1,
        20,
        400,
        0,
        100,
    };





}
