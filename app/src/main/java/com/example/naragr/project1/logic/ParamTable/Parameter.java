package com.example.naragr.project1.logic.ParamTable;

public class Parameter{
    public Param_idx param_idx;
    public byte data_type;
    public ParamTable.Param_table table_type;
    public boolean isRanged;
    public int initVal;
    public int maxVal;
    public int minVal;
    public int addr;



    public Parameter(
            Param_idx param_idx,
            byte data_type,
            ParamTable.Param_table table_type,
            boolean isRanged,
            int initVal,
            int maxVal,
            int minVal,
            int addr)
    {
        this.param_idx = param_idx;
        this.data_type = data_type;
        this.table_type = table_type;
        this.isRanged = isRanged;
        this.initVal = initVal;
        this.maxVal = maxVal;
        this.minVal = minVal;
        this.addr = addr;
    }

    @Override
    public String toString() {
        String result = "[Param_idx:" + param_idx +
                ", data_type:" + data_type +
                ", table_type:" + table_type +
                ", isRanged:" + isRanged +
                ", initVal:" + initVal +
                ", maxVal:" + maxVal +
                ", minVal:" + minVal +
                ", addr:" + addr +
                "]";
        return result;
    }
}