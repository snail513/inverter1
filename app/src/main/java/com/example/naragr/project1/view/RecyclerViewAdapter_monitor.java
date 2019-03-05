package com.example.naragr.project1.view;

import android.graphics.Color;
import android.nfc.Tag;
import android.util.Log;

import com.example.naragr.project1.logic.DataContainer;
import com.example.naragr.project1.logic.ParamTable.ParamTable;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter_monitor extends RecyclerViewAdapter {
    private boolean isRefreshing = false;

    public void onSwitchButton()
    {
        isRefreshing=!isRefreshing;

    }


    public RecyclerViewAdapter_monitor(List<String> name, List<String> value) {
        super.listItemView = new ArrayList<>();
        super.mDataValue = value;
        super.mDataset = name;

        //super(name, value, idx);
    }
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mView.setText(mDataset.get(position), mDataValue.get(position));
        holder.mView.itemIdx =position;
        int listIdx= ParamTable.getTableIdx(ParamTable.Param_table.statusinverter);
        //holder.mView.listIdx = listIdx;
        Log.d("RVAdapter", "MainActivity.idx = " +MainActivity.inputIdx+", ItemView Idx = " +  MainActivity.dataContainer.writerIndices);
        if(MainActivity.dataContainer.writerIndices.contains(DataContainer.getVarIdx(listIdx, position)) ) {

            holder.mView.mTextView.setTextColor(Color.RED);

        }
        else
            holder.mView.mTextView.setTextColor(Color.BLUE);

        try {
            super.listItemView.add(position, holder.mView);
        }
        catch(IndexOutOfBoundsException e)
        {
            //Log.d()
        }
    }
}
