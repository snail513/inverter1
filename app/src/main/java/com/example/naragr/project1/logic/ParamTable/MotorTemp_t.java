package com.example.naragr.project1.logic.ParamTable;

import java.io.Serializable;

public enum MotorTemp_t implements Serializable {
    Normal,
    Attension,
    Danger;

    public static MotorTemp_t get(byte val)
    {
        switch(val)
        {

            default:
                return Normal;
            case 1:
                return Attension;
            case 2:
                return Danger;
        }
    }
    public byte get()
    {
        switch(this)
        {
            default:
                return 0;
            case Attension:
                return 1;
            case Danger:
                return 2;
        }
    }
}
