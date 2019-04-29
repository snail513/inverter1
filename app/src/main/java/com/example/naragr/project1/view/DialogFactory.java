package com.example.naragr.project1.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.naragr.project1.R;

import java.util.logging.LogManager;

public class DialogFactory {

    public static void showInitialDialog()
    {
        String title = "작업 선택";
        String message = "작업을 선택하세요";
        String fileButton = "파일 관리";
        String paramButton = "파라미터 작업";
        String monitorButton = "모니터링";
        //String runButton = "모터제어";
        MainActivity me = MainActivity.me;
        AlertDialog.Builder builder = new AlertDialog.Builder(me);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(fileButton,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        MainActivity.me.selectMenuItem(R.id.menuItemParamSet);
                        dialog.cancel();
                    }
                });

        builder.setNeutralButton(paramButton,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        MainActivity.me.selectMenuItem(R.id.menuItemEdit);
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(monitorButton,
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        MainActivity.me.selectMenuItem(R.id.menuItemMonitor);
                        dialog.cancel();
                    }
                });

        builder.create().show();
    }
}
