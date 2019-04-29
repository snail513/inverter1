package com.example.naragr.project1.logic.ParamTable;

import java.io.Serializable;

public enum Baud_t implements Serializable {

    B2400,
    B4800,
    B9600,
    B19200,
    B38400,
    B115200;


    public static Baud_t get(byte val)
    {
        switch(val)
        {

            case 0:
                return B2400;
            case 1:
                return B4800;
            case 2:
                return B9600;
            case 3:
                return B19200;
            case 4:
                return B38400;
            default:
                return B115200;
        }
    }
    public byte get()
    {
        switch(this)
        {
            case B2400:
                return 0;
            case B4800:
                return 1;
            case B9600:
                return 2;
            case B19200:
                return 3;
            case B38400:
                return 4;
            default:
                return 5;
        }
    }

}
