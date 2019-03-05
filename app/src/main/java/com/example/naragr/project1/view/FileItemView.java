package com.example.naragr.project1.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.naragr.project1.R;

public class FileItemView extends LinearLayout {
    protected TextView mTextView;
    protected Button mBtEdit;
    protected Button mBtDelete;
    public int itemIdx;

    public FileItemView(final Context context) {

        super(context);
        init();

    }

    protected void init() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.item_recycler_list_file, this, false);
        addView(v);

        mTextView = (TextView) findViewById(R.id.textViewName);
        mTextView.setTextSize(15);

        mBtDelete = (Button)findViewById(R.id.buttonDelete);
        mBtEdit = (Button)findViewById(R.id.buttonEdit);

        {
            mBtEdit.setVisibility(View.VISIBLE);
            mBtDelete.setVisibility(View.VISIBLE);

        }
    }

    public void enableButtonListners()
    {
        mBtDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String fileName = mTextView.getText().toString();
                MainActivity.me.deleteFileItem(fileName);
            }
        });

        mBtEdit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String oldFileName = mTextView.getText().toString();
                String newFileName= mTextView.getText().toString();
                MainActivity.showFileNameChangeDialog("Change file name", itemIdx, mTextView.getText().toString());
            }
        });
    }

    public FileItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FileItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String name, String value )
    {
        mTextView.setText(name);
    }

}
