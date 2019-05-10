package com.example.naragr.project1.logic.ParamTable;


public class ParamTable{

    public static int getParamValue(Param_idx idx)
    {
        for (int i=0;i<Param_idx.values().length; i++)
        {
            if(idx == Param_idx.values()[i])
            {
                return i;
            }
        }
        return -1;
    }

    public static String getName(Param_idx idx) {
        switch (idx)
        {
            case value: return "지령주파수";
            case multi_val_0: return "다단 지령주파수 0";
            case multi_val_1: return "다단 지령주파수 1";
            case multi_val_2: return "다단 지령주파수 2";
            case multi_val_3: return "다단 지령주파수 3";
            case multi_val_4: return "다단 지령주파수 4";
            case multi_val_5: return "다단 지령주파수 5";
            case multi_val_6: return "다단 지령주파수 6";
            case multi_val_7: return "다단 지령주파수 7";
            case freq_min: return "최소 지령주파수";
            case freq_max: return "최대 지령주파수";
            case accel_time: return "가속시간";
            case decel_time: return "감속시간";
            case direction_control: return "회전방향명령";
            case jmp_enable0: return "점프 주파수 설정0";
            case jmp_enable1: return "점프 주파수 설정1";
            case jmp_enable2: return "점프 주파수 설정2";
            case jmp_low0: return "점프 하한 주파수0";
            case jmp_low1: return "점프 하한 주파수1";
            case jmp_low2: return "점프 하한 주파수2";
            case jmp_high0: return "점프 상한 주파수0";
            case jmp_high1: return "점프 상한 주파수1";
            case jmp_high2: return "점프 상한 주파수2";
            case direction_domain: return "회전방향 도메인";
            case acc_base_set_type: return "가감속 기준설정";


            case ctrl_in: return "지령방법선택";
            case energy_save: return "에너지 절약";
            case pwm_freq: return "캐리어주파수";
            //case foc_torque_limit: return "FOC 토크 제한값";
            case brake_type: return "정지방식";
            case brake_freq: return "축 브레이크 동작주파수";
            case dci_brake_freq: return "직류제동 시작주파수";
            case dci_brake_block_time: return "직류제동 출력제한 시간";
            case dci_brake_time: return "직류제동 출력 시간";
            case dci_brake_rate: return "직류제동 출력비율";



            case ovl_warn_limit: return "과부하 경고 레벨";
            case ovl_warn_duration: return "과부하 경고 설정시간";
            case ovl_enable: return "과부하 트립 사용";
            case ovl_trip_limit: return "과부하 트립 레벨";
            case ovl_trip_duration: return "과부하 트립 설정시간";
            case regen_duty: return "회생 전압 duty 비";
            case regen_band: return "회생 운전범위";
            case fan_onoff: return "팬 제어 설정";



            case multi_Din_0: return "디지털 입력 설정1";
            case multi_Din_1: return "디지털 입력 설정2";
            case multi_Din_2: return "디지털 입력 설정3";
            case multi_Din_3: return "디지털 입력 설정4";
            case multi_Dout_0: return "디지털 출력 설정1";
            case multi_Dout_1: return "디지털 출력 설정2";
            case v_in_min: return "전압 지령 최소 전압";
            case v_in_min_freq: return "전압 지령 주파수 하한";
            case v_in_max: return "전압 지령 최대 전압";
            case v_in_max_freq: return "전압 지령 주파수 상한";
            case aout_type: return "아날로그 출력 설정";
            case aout_rate: return "아날로그 출력 비율";
            case mb_address: return "모드버스 주소 설정";
            case baudrate: return "485 baudrate";




            case Rs: return "고정자 저항";
            case Rr: return "회전자 저항";
            case Ls: return "고정자 인덕턴스";
            case noload_current: return "무부하 전류";
            case rated_current: return "정격 전류";
            case poles: return "극수";
            case input_voltage: return "입력전압";
            case rated_freq: return "정격 주파수";



            case model: return "인버터 모델 번호";
            //case modbus_addr: return "모드버스 주소";
            case motor_type: return "연결된 모터의 타입";
            case gear_ratio: return "연결된 기어의 기어비";
            case motor_on_count: return "모터 운전 횟수";
            case elapsed_hour: return "모터 구동 총시간";
            case operating_hour: return "인버터 동작 총시간";



            case err_date_0: return "에러발생 날짜0";
            case err_code_0: return "에러 코드0";
            case err_status_0: return "에러 발생시 동작 상태0";
            case err_current_0: return "에러 발생시 전류량0";
            case err_freq_0: return "에러 발생시 동작 주파수0";
            case err_date_1: return "에러발생 날짜1";
            case err_code_1: return "에러 코드1";
            case err_status_1: return "에러 발생시 동작 상태1";
            case err_current_1: return "에러 발생시 전류량1";
            case err_freq_1: return "에러 발생시 동작 주파수1";
            case err_date_2: return "에러발생 날짜2";
            case err_code_2: return "에러 코드2";
            case err_status_2: return "에러 발생시 동작 상태2";
            case err_current_2: return "에러 발생시 전류량2";
            case err_freq_2: return "에러 발생시 동작 주파수2";
            case err_date_3: return "에러발생 날짜3";
            case err_code_3: return "에러 코드3";
            case err_status_3: return "에러 발생시 동작 상태3";
            case err_current_3: return "에러 발생시 전류량3";
            case err_freq_3: return "에러 발생시 동작 주파수3";
            case err_date_4: return "에러발생 날짜4";
            case err_code_4: return "에러 코드4";
            case err_status_4: return "에러 발생시 동작 상태4";
            case err_current_4: return "에러 발생시 전류량4";
            case err_freq_4: return "에러 발생시 동작 주파수4";

            case run_status1: return "동작 상태1";
            case run_status2: return "동작 상태2";
            case I_rms: return "전류값";
            case run_freq: return "동작주파수";
            case dc_voltage: return "직류 전압값";
            case ipm_temperature: return "IPM 온도";
            case motor_temperature: return "모터 온도 상태";

            default : return "UNKNOWN";
        }

    }


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
    public final static byte BAUD_T = 13;
    public final static byte MOTOR_TM_T = 14;


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
        for(Parameter p : table)
        {
            if(subject == p.table_type)
            {
                count++;
            }
        }
        return count;
    }

    public static int getSubjectHead(Param_table subject) {
        int count = 0;
        for(Parameter p : table)
        {
            if(subject == p.table_type)
            //if(subject.compareTo(t)==0)
            {
                return count;
            }
            count++;
        }
        return -1;
    }
    public static Parameter[] table = {
            new Parameter(Param_idx.values()[0],FLOAT_T, Param_table.paramlunchings,true,200,2000,10,64),
            new Parameter(Param_idx.values()[1],FLOAT_T, Param_table.paramlunchings,true,200,2000,10,65),
            new Parameter(Param_idx.values()[2],FLOAT_T, Param_table.paramlunchings,true,200,2000,10,66),
            new Parameter(Param_idx.values()[3],FLOAT_T, Param_table.paramlunchings,true,200,2000,10,67),
            new Parameter(Param_idx.values()[4],FLOAT_T, Param_table.paramlunchings,true,200,2000,10,68),
            new Parameter(Param_idx.values()[5],FLOAT_T, Param_table.paramlunchings,true,200,2000,10,69),
            new Parameter(Param_idx.values()[6],FLOAT_T, Param_table.paramlunchings,true,200,2000,10,70),
            new Parameter(Param_idx.values()[7],FLOAT_T, Param_table.paramlunchings,true,200,2000,10,71),
            new Parameter(Param_idx.values()[8],FLOAT_T, Param_table.paramlunchings,true,200,2000,10,72),
            new Parameter(Param_idx.values()[9],FLOAT_T, Param_table.paramlunchings,true,10,2000,10,73),
            new Parameter(Param_idx.values()[10],FLOAT_T, Param_table.paramlunchings,true,2000,2000,10,74),
            new Parameter(Param_idx.values()[11],FLOAT_T, Param_table.paramlunchings,true,100,6000,10,75),
            new Parameter(Param_idx.values()[12],FLOAT_T, Param_table.paramlunchings,true,100,6000,10,76),
            new Parameter(Param_idx.values()[13],BOOL_T, Param_table.paramlunchings,true,0,1,1,77),
            new Parameter(Param_idx.values()[14],BOOL_T, Param_table.paramlunchings,true,0,1,0,78),
            new Parameter(Param_idx.values()[15],BOOL_T, Param_table.paramlunchings,true,0,1,0,79),
            new Parameter(Param_idx.values()[16],BOOL_T, Param_table.paramlunchings,true,0,1,0,80),
            new Parameter(Param_idx.values()[17],FLOAT_T, Param_table.paramlunchings,true,10,2000,10,81),
            new Parameter(Param_idx.values()[18],FLOAT_T, Param_table.paramlunchings,true,10,2000,10,82),
            new Parameter(Param_idx.values()[19],FLOAT_T, Param_table.paramlunchings,true,10,2000,10,83),
            new Parameter(Param_idx.values()[20],FLOAT_T, Param_table.paramlunchings,true,10,2000,10,84),
            new Parameter(Param_idx.values()[21],FLOAT_T, Param_table.paramlunchings,true,10,2000,10,85),
            new Parameter(Param_idx.values()[22],FLOAT_T, Param_table.paramlunchings,true,10,2000,10,86),
            new Parameter(Param_idx.values()[23],ROT_DIR3_T, Param_table.paramlunchings,true,0,2,0,87),
            new Parameter(Param_idx.values()[24],INT_T, Param_table.paramlunchings,true,0,1,0,87),



            new Parameter(Param_idx.values()[25],INPUT_COMM_T, Param_table.paramsettings,true,0,4,0,128),
            new Parameter(Param_idx.values()[26],BOOL_T, Param_table.paramsettings,true,0,1,0,129),
            new Parameter(Param_idx.values()[27],FREQ_T, Param_table.paramsettings,true,0,3,0,130),
            new Parameter(Param_idx.values()[28],STOP_T, Param_table.paramsettings,true,0,2,0,131),
            new Parameter(Param_idx.values()[29],FLOAT_T, Param_table.paramsettings,true,10,600,1,132),
            new Parameter(Param_idx.values()[30],FLOAT_T, Param_table.paramsettings,true,30,600,1,133),
            new Parameter(Param_idx.values()[31],FLOAT_T, Param_table.paramsettings,true,10,600,0,134),
            new Parameter(Param_idx.values()[32],FLOAT_T, Param_table.paramsettings,true,50,600,0,135),
            new Parameter(Param_idx.values()[33],FLOAT_T, Param_table.paramsettings,true,500,2000,0,136),



            new Parameter(Param_idx.values()[34],INT_T, Param_table.protectionsettings,true,150,200,100,160),
            new Parameter(Param_idx.values()[35],INT_T, Param_table.protectionsettings,true,10,30,0,161),
            new Parameter(Param_idx.values()[36],BOOL_T, Param_table.protectionsettings,true,1,1,0,162),
            new Parameter(Param_idx.values()[37],INT_T, Param_table.protectionsettings,true,180,200,100,163),
            new Parameter(Param_idx.values()[38],INT_T, Param_table.protectionsettings,true,30,60,0,164),
            new Parameter(Param_idx.values()[39],INT_T, Param_table.protectionsettings,true,30,80,0,165),
            new Parameter(Param_idx.values()[40],INT_T, Param_table.protectionsettings,true,10,80,0,166),
            new Parameter(Param_idx.values()[41],BOOL_T, Param_table.protectionsettings,true,0,1,0,167),



            new Parameter(Param_idx.values()[42],DIN_T, Param_table.externio,true,0,8,0,192),
            new Parameter(Param_idx.values()[43],DIN_T, Param_table.externio,true,0,8,0,193),
            new Parameter(Param_idx.values()[44],DIN_T, Param_table.externio,true,0,8,0,194),
            new Parameter(Param_idx.values()[45],DIN_T, Param_table.externio,true,0,8,0,195),
            new Parameter(Param_idx.values()[46],DOUT_T, Param_table.externio,true,0,5,0,196),
            new Parameter(Param_idx.values()[47],DOUT_T, Param_table.externio,true,0,5,0,197),
            new Parameter(Param_idx.values()[48],FLOAT_T, Param_table.externio,true,0,100,0,198),
            new Parameter(Param_idx.values()[49],FLOAT_T, Param_table.externio,true,10,2000,10,199),
            new Parameter(Param_idx.values()[50],FLOAT_T, Param_table.externio,true,100,100,0,200),
            new Parameter(Param_idx.values()[51],FLOAT_T, Param_table.externio,true,2000,2000,10,201),
            new Parameter(Param_idx.values()[52],AOUT_T, Param_table.externio,true,0,3,0,202),
            new Parameter(Param_idx.values()[53],INT_T, Param_table.externio,true,100,200,10,208),
            new Parameter(Param_idx.values()[54],INT_T, Param_table.externio,true,1,254,1,209),
            new Parameter(Param_idx.values()[55],BAUD_T, Param_table.externio,true,2,5,0,210),




            new Parameter(Param_idx.values()[56],FLOAT_T, Param_table.parammotor,false,0,0,0,8),
            new Parameter(Param_idx.values()[57],FLOAT_T, Param_table.parammotor,false,0,0,0,9),
            new Parameter(Param_idx.values()[58],FLOAT_T, Param_table.parammotor,false,0,0,0,10),
            new Parameter(Param_idx.values()[59],FLOAT_T, Param_table.parammotor,false,0,0,0,11),
            new Parameter(Param_idx.values()[60],FLOAT_T, Param_table.parammotor,false,0,0,0,12),
            new Parameter(Param_idx.values()[61],INT_T, Param_table.parammotor,false,0,0,0,13),
            new Parameter(Param_idx.values()[62],INT_T, Param_table.parammotor,false,0,0,0,14),
            new Parameter(Param_idx.values()[63],INT_T, Param_table.parammotor,false,0,0,0,15),



            new Parameter(Param_idx.values()[64],INT_T, Param_table.devsettings,false,0,0,0,24),
            new Parameter(Param_idx.values()[65],INT_T, Param_table.devsettings,false,0,0,0,25),
            new Parameter(Param_idx.values()[66],INT_T, Param_table.devsettings,false,0,0,0,26),
            new Parameter(Param_idx.values()[67],INT_T, Param_table.devsettings,false,0,0,0,27),
            new Parameter(Param_idx.values()[68],INT_T, Param_table.devsettings,false,0,0,0,28),
            new Parameter(Param_idx.values()[69],INT_T, Param_table.devsettings,false,0,0,0,29),



            new Parameter(Param_idx.values()[70],INT_T, Param_table.error,false,0,0,0,224),
            new Parameter(Param_idx.values()[71],INT_T, Param_table.error,false,0,0,0,225),
            new Parameter(Param_idx.values()[72],INT_T, Param_table.error,false,0,0,0,226),
            new Parameter(Param_idx.values()[73],FLOAT_T, Param_table.error,false,0,0,0,227),
            new Parameter(Param_idx.values()[74],FLOAT_T, Param_table.error,false,0,0,0,228),
            new Parameter(Param_idx.values()[75],INT_T, Param_table.error,false,0,0,0,229),
            new Parameter(Param_idx.values()[76],INT_T, Param_table.error,false,0,0,0,230),
            new Parameter(Param_idx.values()[77],INT_T, Param_table.error,false,0,0,0,231),
            new Parameter(Param_idx.values()[78],FLOAT_T, Param_table.error,false,0,0,0,232),
            new Parameter(Param_idx.values()[79],FLOAT_T, Param_table.error,false,0,0,0,233),
            new Parameter(Param_idx.values()[80],INT_T, Param_table.error,false,0,0,0,234),
            new Parameter(Param_idx.values()[81],INT_T, Param_table.error,false,0,0,0,235),
            new Parameter(Param_idx.values()[82],INT_T, Param_table.error,false,0,0,0,236),
            new Parameter(Param_idx.values()[83],FLOAT_T, Param_table.error,false,0,0,0,237),
            new Parameter(Param_idx.values()[84],FLOAT_T, Param_table.error,false,0,0,0,238),
            new Parameter(Param_idx.values()[85],INT_T, Param_table.error,false,0,0,0,239),
            new Parameter(Param_idx.values()[86],INT_T, Param_table.error,false,0,0,0,240),
            new Parameter(Param_idx.values()[87],INT_T, Param_table.error,false,0,0,0,241),
            new Parameter(Param_idx.values()[88],FLOAT_T, Param_table.error,false,0,0,0,242),
            new Parameter(Param_idx.values()[89],FLOAT_T, Param_table.error,false,0,0,0,243),
            new Parameter(Param_idx.values()[90],INT_T, Param_table.error,false,0,0,0,244),
            new Parameter(Param_idx.values()[91],INT_T, Param_table.error,false,0,0,0,245),
            new Parameter(Param_idx.values()[92],INT_T, Param_table.error,false,0,0,0,246),
            new Parameter(Param_idx.values()[93],FLOAT_T, Param_table.error,false,0,0,0,247),
            new Parameter(Param_idx.values()[94],FLOAT_T, Param_table.error,false,0,0,0,248),






            new Parameter(Param_idx.values()[95],INT_T, Param_table.statusinverter,false,0,0,0,40),
            new Parameter(Param_idx.values()[96],INT_T, Param_table.statusinverter,false,0,0,0,41),
            new Parameter(Param_idx.values()[97],FLOAT_T, Param_table.statusinverter,false,0,0,0,42),
            new Parameter(Param_idx.values()[98],FLOAT_T, Param_table.statusinverter,false,0,0,0,43),
            new Parameter(Param_idx.values()[99],FLOAT_T, Param_table.statusinverter,false,0,0,0,44),
            new Parameter(Param_idx.values()[100],FLOAT_T, Param_table.statusinverter,false,0,0,0,45),
            new Parameter(Param_idx.values()[101],MOTOR_TM_T, Param_table.statusinverter,false,0,0,0,46),

    };


    public ParamTable()
    {
        int size = Param_idx.values().length;
        table = new Parameter[size];


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
    //Todo: reform tables
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

    public static String getTableName(Param_table idx)
    {
        switch(idx)
        {
            case paramlunchings:
                return "구동";
            case paramsettings:
                return "설정";
            case protectionsettings:
                return "보호";
            case externio:
                return "I/O";
            case parammotor:
                return "모터";
            case devsettings:
                return "dev";
            case statusinverter:
                return "상태";
            case error:
                return "에러";
            default:
                return "";
        }
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

    public static int getRangedLength()
    {
        int result =0 ;
        for(Parameter p : table)
        {
            if(p.isRanged)
                result++;

        }
        return result;
    }
}
