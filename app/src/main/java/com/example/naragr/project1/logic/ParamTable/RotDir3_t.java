package com.example.naragr.project1.logic.ParamTable;

import java.io.Serializable;

public enum	RotDir3_t implements Serializable {
    bidirect,  forward, reverse;
    public static RotDir3_t get(byte val)
    {
        switch(val)
        {
            default:
                return bidirect;
            case 1:
                return forward;
            case 2:
                return reverse;
        }
    }
    public byte get()
    {
        switch(this)
        {
            default:
                return 0;
            case forward:
                return 1;
            case reverse:
                return 2;
        }
    }

}