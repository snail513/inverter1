package com.example.naragr.project1.logic.ParamTable;


import java.io.Serializable;

public enum	Aout_t implements Serializable {	unuse, comm_freq, output_freq, load_ratio	;
    public static Aout_t get(byte val)
    {
        switch(val)
        {
            default:
                return unuse;
            case 1:
                return comm_freq;
            case 2:
                return output_freq;
            case 3:
                return load_ratio;
        }
    }
    public byte get()
    {
        switch(this)
        {
            default:
                return 0;
            case comm_freq:
                return 1;
            case output_freq:
                return 2;
            case load_ratio:
                return 3;
        }
    }
}
