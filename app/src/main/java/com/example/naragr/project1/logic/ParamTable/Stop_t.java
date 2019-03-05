package com.example.naragr.project1.logic.ParamTable;


import java.io.Serializable;

public enum	Stop_t implements Serializable {	decel_invert, DC_invert, freerun	;
    public static Stop_t get(byte val)
    {
        switch(val)
        {
            default:
                return decel_invert;
            case 1:
                return DC_invert;
            case 2:
                return freerun;

        }
    }
    public byte get()
    {
        switch(this)
        {
            default:
                return 0;
            case DC_invert:
                return 1;
            case freerun:
                return 2;
        }
    }
}