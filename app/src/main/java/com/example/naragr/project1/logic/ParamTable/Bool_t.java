package com.example.naragr.project1.logic.ParamTable;


import java.io.Serializable;

public enum	Bool_t implements Serializable {
    FALSE, TRUE;
    public static Bool_t get(byte val)
    {
        switch(val)
        {
            default:
                return FALSE;
            case 1:
                return TRUE;
        }
    }
    public byte get()
    {
        switch(this)
        {
            default:
                return 0;
            case TRUE:
                return 1;
        }
    }

}