package com.example.naragr.project1.logic.ParamTable;


import java.io.Serializable;

public enum	RunComm_t	implements Serializable {	vf, foc;

    public static RunComm_t get(byte val)
    {
        switch(val)
        {
            default:
                return vf;
            case 1:
                return foc;
        }
    }
    public byte get()
    {
        switch(this)
        {
            default:
                return 0;
            case foc:
                return 1;
        }
    }
}