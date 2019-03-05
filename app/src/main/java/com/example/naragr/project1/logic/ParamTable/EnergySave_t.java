package com.example.naragr.project1.logic.ParamTable;


import java.io.Serializable;

public enum	EnergySave_t	implements Serializable {	unuse, use_init , use_normal, use_init_normal;

    public static EnergySave_t get(byte val)
    {
        switch(val)
        {
            default:
                return unuse;
            case 1:
                return use_init;
            case 2:
                return use_normal;
            case 3:
                return use_init_normal;
        }
    }
    public byte get()
    {
        switch(this)
        {
            default:
                return 0;
            case use_init:
                return 1;
            case use_normal:
                return 2;
            case use_init_normal:
                return 3;
        }
    }
}