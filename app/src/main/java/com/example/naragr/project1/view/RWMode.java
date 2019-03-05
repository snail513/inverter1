package com.example.naragr.project1.view;

public class RWMode {

    public enum Mode{
        Read,
        ReadAll,
        EditAndWrite,
        CopyAndWriteAll,
        Monitor,
        Default
    }

    public static Mode getMode(int i)
    {
        switch(i)
        {
            case 0 : return Mode.Read;
            case 1 : return Mode.ReadAll;
            case 2 : return Mode.EditAndWrite;
            case 3 : return Mode.CopyAndWriteAll;
            case 4 : return Mode.Default;
            case 5: return Mode.Monitor;
            default : return Mode.Read;
        }
    }

    public static int getIdx(Mode mode)
    {
        switch(mode)
        {
            case Read: return 0;
            case ReadAll: return 1;
            case EditAndWrite: return 2;
            case CopyAndWriteAll : return 3;
            case Default: return 4;
            case Monitor:return 5;
            default : return 0;
        }
    }

}
