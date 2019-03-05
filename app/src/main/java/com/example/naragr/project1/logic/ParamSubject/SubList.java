package com.example.naragr.project1.logic.ParamSubject;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public abstract class SubList implements ISubList{


    public int getAddress(int varIdx)
    {
        return getDefaultAddress() + varIdx;
    }

    public List<String> getNameList()
    {
        List<Pair<String, Object>> list = getList();
        List<String> r = new ArrayList<>();
        for (Pair<String, Object> p : list
                ) {
            r.add(p.first);
        }
        return r;
    }
}