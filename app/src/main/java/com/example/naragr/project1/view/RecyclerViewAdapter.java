package com.example.naragr.project1.view;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.naragr.project1.logic.DataContainer;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    protected List<String> mDataset;
    protected List<String> mDataValue;
    private int listIndex;

    protected List<ItemView> listItemView;

    public RecyclerViewAdapter() {
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public ItemView mView;

        public ViewHolder(@NonNull ItemView itemView) {
            super(itemView);
            mView = itemView;
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)

    public RecyclerViewAdapter(List<String> name, List<String> value, int idx) {
        listItemView = new ArrayList<>();

        mDataset = name;
        mDataValue = value;
        listIndex = idx;

    }




    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        ItemView itemView = new ItemView(parent.getContext());

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mView.setText(mDataset.get(position), mDataValue.get(position));
        holder.mView.itemIdx =position;
        holder.mView.listIdx = listIndex;
        Log.d("RVAdapter", "MainActivity.idx = " +MainActivity.inputIdx+", ItemView Idx = " +  MainActivity.dataContainer.writerIndices);
        if(MainActivity.dataContainer.writerIndices.contains(DataContainer.getVarIdx(listIndex, position)) ) {

            holder.mView.mTextView.setTextColor(Color.RED);

        }
        else
            holder.mView.mTextView.setTextColor(Color.BLUE);

        try {
            listItemView.add(position, holder.mView);
        }
        catch(IndexOutOfBoundsException e)
        {
            //Log.d()
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void refresh()
    {
        for (int i = 0; i<listItemView.size();i++) {
            if(MainActivity.dataContainer.writerIndices.contains(DataContainer.getVarIdx(listIndex, i)) ) {

                listItemView.get(i).mTextView.setTextColor(Color.RED);

            }
            else
                listItemView.get(i).mTextView.setTextColor(Color.BLUE);
        }
    }

}
