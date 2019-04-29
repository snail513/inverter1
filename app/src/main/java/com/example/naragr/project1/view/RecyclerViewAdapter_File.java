package com.example.naragr.project1.view;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.naragr.project1.logic.DataContainer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RecyclerViewAdapter_File extends RecyclerView.Adapter<RecyclerViewAdapter_File.ViewHolder> {
    public List<String> fileList;
    //public List<FileItemView> fileItemViews = new LinkedList<>();
    @NonNull
    @Override
    public RecyclerViewAdapter_File.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FileItemView itemView = new FileItemView(viewGroup.getContext());
        RecyclerViewAdapter_File.ViewHolder vh  = new RecyclerViewAdapter_File.ViewHolder(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mView.setText(fileList.get(position), "");
        holder.mView.itemIdx =position;


        if(position == 0)
        {
            holder.mView.mTextView.setWidth(holder.mView.getWidth());
            //holder.mView.setBackgroundColor(Color.LTGRAY);
            holder.mView.mBtEdit.setVisibility(View.INVISIBLE);
            holder.mView.mBtEdit.setEnabled(false);
            holder.mView.mBtDelete.setVisibility(View.INVISIBLE);
            holder.mView.mBtDelete.setEnabled(false);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.showFileCreateDialog("Create new file", 1, "Enter new file name");
                }
            });
        }
        else
        {
            holder.mView.enableButtonListners();
            final String fileName = holder.mView.mTextView.getText().toString();
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MainActivity.loadData(fileName);
                    updateSelection(MainActivity.dataContainer.mFileName);
                    //Log.d("File", "updateSelection : " + fileName);
                }
            });
            //updateSelection(MainActivity.dataContainer.mFileName);
        }
    }

    public void updateSelection(String selectedFileName)
    {
        /*for (FileItemView fileItemview : fileItemViews) {
            //Log.d("File", "updateSelection : " + fileItemview.mTextView.getText());
            if(fileItemview.mTextView.getText().toString().compareTo(selectedFileName)==0)
            {
                fileItemview.mTextView.setTextColor(Color.BLUE);
            }
            else
            {
                fileItemview.mTextView.setTextColor(Color.GRAY);
            }
        }*/
    }

    public RecyclerViewAdapter_File(List<String> name) {
        fileList = name;

    }



    @Override
    public int getItemCount() {
        return fileList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public FileItemView mView;

        public ViewHolder(@NonNull FileItemView itemView) {
            super(itemView);
            mView = itemView;
        }

    }
}
