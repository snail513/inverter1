package com.example.naragr.project1.logic.ParamTable;

import java.io.Serializable;

public enum Din_t implements Serializable {
    unuse,
    drive_stop,
    direction,
    multi_freq_low,
    multi_freq_middle,
    multi_freq_high,
    stop_emergency,
    out_trip_input,
    realese_axis_break_input,
    ;
    public static Din_t get(byte val)
    {
        switch(val)
        {
            default:
                return unuse;
            case 1:
                return drive_stop;
            case 2:
                return direction;
            case 3:
                return multi_freq_low;
            case 4:
                return multi_freq_middle;
            case 5:
                return multi_freq_high;
            case 6:
                return stop_emergency;
            case 7:
                return out_trip_input;
            case 8:
                return realese_axis_break_input;


        }
    }
    public byte get()
    {
        switch(this)
        {
            default:
                return 0;
            case unuse:
                return 1;
            case drive_stop:
                return 2;
            case direction:
                return 3;
            case multi_freq_low:
                return 4;
            case multi_freq_middle:
                return 5;
            case multi_freq_high:
                return 6;
            case stop_emergency:
                return 7;
            case out_trip_input:
                return 8;
            case realese_axis_break_input:
                return 9;
        }
    }
}