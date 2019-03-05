package com.example.naragr.project1.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.naragr.project1.R;
import com.example.naragr.project1.logic.DataContainer;

public class ItemView extends LinearLayout {
    protected TextView mTextView;
    protected TextView mTextViewValue;
    protected String lastString;
    protected ToggleButton toggleButton;
    public int itemIdx;
    public int listIdx;

    public ItemView(Context context) {

        super(context);
        init();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                lastString = mTextViewValue.getText().toString();
                Log.d("ItemView", "Click IDx "+  DataContainer.getVarIdx(listIdx, itemIdx));
                MainActivity.showItemValueChangeDialog(mTextView.getText().toString(), DataContainer.getVarIdx(listIdx, itemIdx), mTextViewValue.getText().toString());

            }
        });

    }




    protected void init() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.item_recycler_list, this, false);
        addView(v);
        mTextView = (TextView) findViewById(R.id.textViewName);
        mTextView.setTextSize(20);

        mTextViewValue = (TextView) findViewById(R.id.textViewValue);
        mTextViewValue.setTextSize(20);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setText(String name, String value )
    {
        mTextView.setText(name);
        mTextViewValue.setText(value);


    }

}
