package com.example.naragr.project1.logic.ParamTable;


import java.io.Serializable;

public enum	Freq_t	implements Serializable {	PWM_4kHz, PWM_8kHz, PWM_12kHz, PWM_16kHz	;
    public static Freq_t get(byte val)
    {
        switch(val)
        {
            default:
                return PWM_4kHz;
            case 1:
                return PWM_8kHz;
            case 2:
                return PWM_12kHz;
            case 3:
                return PWM_16kHz;

        }
    }
    public byte get()
    {
        switch(this)
        {
            default:
                return 0;
            case PWM_8kHz:
                return 1;
            case PWM_12kHz:
                return 2;
            case PWM_16kHz:
                return 3;
        }
    }
}