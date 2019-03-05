package com.example.naragr.project1.logic.ParamTable;


import java.io.Serializable;

public enum	Dout_t	implements Serializable {	unuse, running, overloading, stopping, damaged, breaking;
    public static Dout_t get(byte val)
    {
        switch(val)
        {
            default:
                return unuse;
            case 1:
                return running;
            case 2:
                return overloading;
            case 3:
                return stopping;
            case 4:
                return damaged;
            case 5:
                return breaking;

        }
    }
    public byte get()
    {
        switch(this)
        {
            default:
                return 0;
            case running:
                return 1;
            case overloading:
                return 2;
            case stopping:
                return 3;
            case damaged:
                return 4;
            case breaking:
                return 5;
        }
    }
}