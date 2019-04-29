package com.example.naragr.project1.logic.ParamSubject;

import android.util.Pair;

import com.example.naragr.project1.logic.ParamTable.Bool_t;
import com.example.naragr.project1.logic.ParamTable.DataDB;
import com.example.naragr.project1.logic.ParamTable.ParamTable;
import com.example.naragr.project1.logic.ParamTable.Param_idx;
import com.example.naragr.project1.logic.ParamTable.Parameter;
import com.example.naragr.project1.logic.ParamTable.RotDir3_t;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GeneralSubList extends SubList {

    private ParamTable.Param_table table;

    public ArrayList<String> names;

    private int headAddr = 0;



    public List<String> getValueList()
    {
        int headAddr = ParamTable.getSubjectHead(table);
        int count = ParamTable.getSubjectCount(table);
        List<String> r = new ArrayList<>();
        for(int i = 0; i<count; i++)
        {
            int addr = headAddr+i;
            String value = ""+DataDB.getDBInstance().getValue(addr);
            r.add(value);


        }

        return r;
    }
    public int getTotalBlockSize(){
        return names.size();
    }



    public GeneralSubList(ParamTable.Param_table subject) {
        table = subject;
        int subjectCount = ParamTable.getSubjectCount(subject);
        int subjectHead = ParamTable.getSubjectHead(subject);
        //System.out.println("[" + subject + "] subject count:" + subjectCount +  ", head addr : "+ subjectHead);
        names = new ArrayList<>();

        for(int i = 0; i<subjectCount;i++)
        {
            Parameter detail = DataDB.getInstance().table[i+subjectHead];
            int addr = i+subjectHead;
            DataDB.getDBInstance().setValue(addr, detail.initVal);
            names.add(ParamTable.getName(Param_idx.values()[addr]));
            System.out.println("[" + addr + "] detail.initVal" + detail.initVal);

        }
        headAddr = DataDB.getInstance().table[subjectHead].addr;
    }

    @Override
    public String getName() {

        return ParamTable.getTableName(table);
    }

    @Override
    public int getDefaultAddress() {
        //return ParamTable.getSubjectHead(table)*4;
        return headAddr;
    }

    public HashMap<String, Object> getHashMap()
    {
        HashMap<String, Object> r = new HashMap<String, Object>();
        int headAddr = ParamTable.getSubjectHead(table);
        int count = ParamTable.getSubjectCount(table);

        List<String> value = getValueList();

        for (int i= 0; i<names.size();i++) {
            r.put(names.get(i), value.get(i));
        }
        return r;
    }

    public List<Pair<String, Object>> getList()
    {
        List<Pair<String, Object>> list = new ArrayList<>();
        Pair<String, Object> pair;
        int head = ParamTable.getSubjectHead(table);
        for(int idx = 0; idx< names.size(); idx++)
        {
            //System.out.println("idx = " + idx + ", name = " + names[idx] + ", value = " + DataDB.getDBInstance().getValue(head + idx));
            String name = names.get(idx);
            Object value = DataDB.getDBInstance().getValue(head + idx);
            list.add( new Pair<>(name, value));
        }
        return list;
    }

    public String toString()
    {
        String out = "GenSubList [" + table + "]:\n";
        List<String> listValue = getValueList();
        for(int i = 0; i<names.size(); i++)
        {
            out += "[" + names.get(i) + ", " + listValue.get(i)+"],\n";
        }
        return out;
    }

    public static void main(String args[]){
        for(ParamTable. Param_table table : ParamTable.Param_table.values())
        {
            GeneralSubList glist = new GeneralSubList(table);
            System.out.println(glist);
        }



    }
}
