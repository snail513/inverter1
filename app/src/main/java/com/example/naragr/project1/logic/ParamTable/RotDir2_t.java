package com.example.naragr.project1.logic.ParamTable;


import java.io.Serializable;

public enum	RotDir2_t	implements Serializable {	 forward, reverse;
    public static RotDir2_t get(byte val)
    {
        switch(val)
        {
            default:
                return forward;
            case 1:
                return reverse;
        }
    }
    public byte get()
    {
        switch(this)
        {
            default:
                return 0;
            case reverse:
                return 1;
        }
    }
}