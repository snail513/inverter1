package com.example.naragr.project1.logic.ParamSubject;

import android.util.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public interface ISubList extends Serializable {
    HashMap<String, Object> getHashMap();
    List<Pair<String, Object>> getList();
    String getName();
    List<String> getNameList();
    List<String> getValueList();
    int getDefaultAddress();
    int getAddress(int varIdx);
    int getTotalBlockSize();
}