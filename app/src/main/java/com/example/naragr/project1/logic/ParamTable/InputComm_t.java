package com.example.naragr.project1.logic.ParamTable;


import java.io.Serializable;

public enum	InputComm_t	implements Serializable {	keypad, nfc, digital, analog_V, analog_I, feildbus,  webserver;
    public static InputComm_t get(byte val)
    {
        switch(val)
        {
            default:
                return keypad;
            case 1:
                return nfc;
            case 2:
                return digital;
            case 3:
                return analog_V;
            case 4:
                return analog_I;
            case 5:
                return feildbus;
            case 6:
                return webserver;

        }
    }
    public byte get()
    {
        switch(this)
        {
            default:
                return 0;
            case nfc:
                return 1;
            case digital:
                return 2;
            case analog_V:
                return 3;
            case analog_I:
                return 4;
            case feildbus:
                return 5;
            case webserver:
                return 6;
        }
    }

}