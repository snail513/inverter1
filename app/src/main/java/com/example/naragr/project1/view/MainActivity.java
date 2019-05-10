package com.example.naragr.project1.view;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.naragr.project1.R;
import com.example.naragr.project1.logic.ComPortHandler;
import com.example.naragr.project1.logic.DataContainer;
import com.example.naragr.project1.logic.FileManager;
import com.example.naragr.project1.logic.MonitorThread;
import com.example.naragr.project1.logic.NfcVComm;
import com.example.naragr.project1.logic.ParamTable.Bool_t;
import com.example.naragr.project1.logic.ParamTable.ParamTable;
import com.example.naragr.project1.logic.ParamTable.Param_idx;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    //public static ComPortHandler mSerialPortHandler;
    public static MainActivity me;
    public static DataContainer dataContainer;
    private RecyclerView[] recyclerViewArray ;
    private NfcAdapter mNFCadapter;
    private PendingIntent mPendingIntent;
    private RWMode.Mode rwMode = RWMode.Mode.Summary;
    final String defaultFile = "defaultFile";


    ToggleButton mButtonRead;
    ToggleButton mButtonWrite;
    ToggleButton mButtonHotfix;

    public static int inputIdx = -1;
    public static String inputText = "";
    public static Vibrator vibe = null;
    public ToggleButton mButtonRefresh;
    private ImageButton mButtonTurnOn;
    private boolean isForceToTurnOn = false;
    private Button mButtonToParam;
    private Button mButtonToMonitoring;
    private Button mButtonToFile;
    private Button mButtonToSummary;
    private ToggleButton mButtonSwitchInput;
    private TextView mTvSelectedFileName;



    private TextView[] tvCurrRPM = new TextView[5];
    private TextView[] tvTarRPM = new TextView[5];
    private TextView tvDirectionState;
    private TextView tvProtectionState;
    private TextView tvTripState;

    public void updateSelectedFileName(String fileName)
    {
        mTvSelectedFileName.setText(fileName);
    }

    LinearLayout mLayoutMainSummary;
    private void setSummaryUIVisible(int isVisible) {

        mLayoutMainSummary.setVisibility(isVisible);
    }

    private void generateSummaryUI()
    {

        mLayoutMainSummary = (LinearLayout)findViewById(R.id.layout_summary);
        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_main_summary, mLayoutMainSummary);

        tvCurrRPM[0] = findViewById(R.id.tvRPM0);
        tvCurrRPM[1] = findViewById(R.id.tvRPM1);
        tvCurrRPM[2] = findViewById(R.id.tvRPM2);
        tvCurrRPM[3] = findViewById(R.id.tvRPM3);
        tvCurrRPM[4] = findViewById(R.id.tvRPM4);

        tvTarRPM[0] = findViewById(R.id.tvTarValue0);
        tvTarRPM[1] = findViewById(R.id.tvTarValue1);
        tvTarRPM[2] = findViewById(R.id.tvTarValue2);
        tvTarRPM[3] = findViewById(R.id.tvTarValue3);
        tvTarRPM[4] = findViewById(R.id.tvTarValue4);

        tvDirectionState = findViewById(R.id.tvDirctionState);
        tvDirectionState.setTextSize(20);
        tvTripState = findViewById(R.id.tvError);
        tvTripState.setTextSize(20);
        tvProtectionState = findViewById(R.id.tvProtection);
        tvProtectionState.setTextSize(20);
        for(int i=0;i<5;i++)
        {
            tvCurrRPM[i].setTextSize(20);
            tvTarRPM[i].setTextSize(20);
        }
    }

    private void generateMainColtrollerUI(){
        LinearLayout layoutMainController = (LinearLayout)findViewById(R.id.layout_control);
        LayoutInflater inflater =  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_main_menu, layoutMainController);

        mButtonTurnOn = (ImageButton)findViewById(R.id.buttonTurnOn);
        mButtonToParam = (Button)findViewById(R.id.buttonToParam);
        mButtonToMonitoring= (Button)findViewById(R.id.buttonToMonitor);
        mButtonToFile = (Button)findViewById(R.id.buttonToFile);
        mButtonToSummary = (Button)findViewById(R.id.buttonToSummary);
        mButtonTurnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isForceToTurnOn = !isForceToTurnOn;
                //Toast.makeText(me, "turnon = " + isForceToTurnOn, Toast.LENGTH_SHORT).show();
                if(isForceToTurnOn){
                    mButtonTurnOn.setImageResource(R.drawable.turn_on_button);
                }
                else
                {
                    mButtonTurnOn.setImageResource(R.drawable.turn_off_button);
                }
            }
        });

        mButtonToParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectMenuItem(R.id.menuItemEdit);
            }
        });

        mButtonToFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMenuItem(R.id.menuItemParamSet);
            }
        });

        mButtonToMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMenuItem(R.id.menuItemMonitor);
            }
        });

        mButtonToSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMenuItem(R.id.menuItemSummary);
            }
        });

        mTvSelectedFileName = findViewById(R.id.textViewSelectedFileName);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sample, menu);
        return true;
    }

    static void showItemValueChangeDialog(String title, final int idx, String value){
        if(!me.isItemSelectable)
            return;
        AlertDialog.Builder builder = new AlertDialog.Builder(me);
        if(idx< ParamTable.getRangedLength())
        {
            if(ParamTable.table[idx].data_type == ParamTable.FLOAT_T)
            //    if(ParamTable.param_type[idx] == ParamTable.FLOAT_T)
            {
                builder.setTitle(title + "[" + (float)ParamTable.table[idx].minVal/10+ "~"+ (float)ParamTable.table[idx].maxVal/10+"]");
            }
            else
            {
                builder.setTitle(title + "[" + ParamTable.table[idx].minVal+ ":"+ ParamTable.table[idx].maxVal+"]");
            }


        }
        else
        {
            builder.setTitle(title);
        }


// Set up the input
        final EditText input = new EditText(me);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);
        input.setText(value);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputIdx = idx;
                inputText = input.getText().toString();


                try{
                    dataContainer.setValue(idx, inputText);
                }catch (NumberFormatException e)
                {
                    Toast.makeText(me, "숫자형식에 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                dataContainer.writerIndices.add(idx);
                me.updateView();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                inputIdx = -1;
                inputText = "";

            }
        });
        builder.show();

    }

    private void updateView() {
        int listIdx = DataContainer.getTableIdx(inputIdx);
        Log.d("update", ""+listIdx+ ":"+inputText);
        swapRecyclerListView(recyclerViewArray[listIdx], dataContainer.getSubList(listIdx).getNameList(), dataContainer.getValueList(listIdx), listIdx);
        updateSummary();
    }

    private void updateAllView() {
        Log.d("updateAll", "");

        for(int listIdx =0; listIdx<DataContainer.MAX_IDX; listIdx++)
        {
            List<String> nameList = dataContainer.getSubList(listIdx).getNameList();
            List<String> valueList = dataContainer.getValueList(listIdx);

            Log.d("updateAll", "nameList : " + nameList);
            Log.d("updateAll", "valueList : " + valueList);
            swapRecyclerListView(recyclerViewArray[listIdx], nameList, valueList, listIdx);
        }

        //me.swapFileListView();
        //me.swapSettingsListView();
        me.swapMonitorView();
        me.updateSummary();
    }

    private void updateSummary() {
        float currRPM, tarRPM;
        Bool_t direction;
        String errorState = "";
        String protection = "";

        currRPM = (float)dataContainer.getObject(ParamTable.getParamValue(Param_idx.run_freq));
        tarRPM = (float)dataContainer.getObject(ParamTable.getParamValue(Param_idx.value));
        direction = (Bool_t) dataContainer.getObject(ParamTable.getParamValue(Param_idx.direction_control));

        ///for(int i =0; i< 5; i++)
        {
            tvCurrRPM[0].setText(""+(int)((currRPM)%10));
            tvCurrRPM[1].setText(""+(int)((currRPM/10)%10));
            tvCurrRPM[2].setText(""+(int)((currRPM/100)%10));
            tvCurrRPM[3].setText(""+(int)((currRPM/1000)%10));
            tvCurrRPM[4].setText(""+(int)((currRPM/10000)%10));

            tvTarRPM[0].setText(""+(int)((tarRPM)%10));
            tvTarRPM[1].setText(""+(int)((tarRPM/10)%10));
            tvTarRPM[2].setText(""+(int)((tarRPM/100)%10));
            tvTarRPM[3].setText(""+(int)((tarRPM/1000)%10));
            tvTarRPM[4].setText(""+(int)((tarRPM/10000)%10));

            if(direction==Bool_t.TRUE)
            {
                tvDirectionState.setText("역방향");
            }
            else
            {
                tvDirectionState.setText("정방향");
            }

            tvProtectionState.setText(protection);
            tvTripState.setText(errorState);
        }

    }


    public void vibrateStart()
    {
        if(vibe==null)
            return;
        vibe.cancel();
        vibe.vibrate(new long[] { 300, 300}, -1);
    }

    public void vibrateEnd()
    {
        if(vibe==null)
            return;
        vibe.cancel();
        vibe.vibrate(new long[] { 100, 100}, -1);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;

        setContentView(R.layout.activity_main);

        vibe = (Vibrator)
                getSystemService(this.VIBRATOR_SERVICE);


        dataContainer = new DataContainer();
        //bar = findViewById(R.id.progressBar);
        if(!FileManager.isFileExist(new File(me.getFilesDir(), defaultFile)))
        {

            dataContainer.save(me, defaultFile);
        }
        else
        {

            dataContainer.load(me, defaultFile);

        }
        //generateRunStopUI();
        generateMonitorUI();
        setToggleButtonUI();
        generateTabUI();
        generateFileListUI();
        generateSettingsUI();


        mNFCadapter = NfcAdapter.getDefaultAdapter(this);
        if(mNFCadapter==null)
        {
            Toast.makeText(me, "Error : Cannot get NFC adapter!", Toast.LENGTH_SHORT).show();
            //this.finish();
            //return;

        }
        else
        {
            if(!mNFCadapter.isEnabled())
            {
                Toast.makeText(me, "Error : Not Enabled NFC!", Toast.LENGTH_SHORT).show();
                this.finish();
                return;
            }
            Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        }
        //DialogFactory.showInitialDialog();

        generateMainColtrollerUI();
        generateSummaryUI();
        updateSelectedFileName(dataContainer.mFileName);
        updateAllView();
    }

    //RecyclerView mRViewMonitor;
    private void generateMonitorUI() {
        mRViewMonitor = findViewById(R.id.recyclerViewMonitor);
        mRViewMonitor.setVisibility(View.INVISIBLE);

        LinearLayoutManager manager = new LinearLayoutManager(me);

        mAdapterMonitors = new RecyclerViewAdapter_monitor(
                dataContainer.getSubList(ParamTable.Param_table.statusinverter).getNameList(),
                dataContainer.getSubList(ParamTable.Param_table.statusinverter).getValueList());
        mRViewMonitor = (RecyclerView)findViewById(R.id.recyclerViewMonitor);
        mRViewMonitor.setHasFixedSize(true);
        mRViewMonitor.setAdapter(mAdapterMonitors);
        mRViewMonitor.setLayoutManager(manager);
        mRViewMonitor.setVisibility(View.INVISIBLE);
        mButtonRefresh = (ToggleButton) findViewById(R.id.toggleButtonRefresh);
        mButtonRefresh.setVisibility(View.INVISIBLE);
        mButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mButtonRefresh.isChecked()) {
                    //
                    rwMode = RWMode.Mode.Monitor;
                    //setMonitorEnter();

                }
                else{
                    rwMode = RWMode.Mode.Default;
                    //setMonitorQuit();
                }
            }
        });

        mButtonSwitchInput = findViewById(R.id.toggleButtonInput);
        mButtonSwitchInput.setVisibility(View.INVISIBLE);

        mButtonSwitchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mButtonSwitchInput.isChecked())
                {
                    int baudrate= 9600;
                    if(!ComPortHandler.connect(baudrate))
                    {
                        mButtonSwitchInput.setChecked(false);
                    }

                }
                else
                {
                    if(ComPortHandler.isConnected())
                    {
                        ComPortHandler.closeSerialPort();
                    }

                }
            }
        });

    }

    @Override
    protected void onResume() {

        super.onResume();
        if (mNFCadapter != null) {
            //IntentFilter ndef =new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
            mNFCadapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            Log.d("Result", "onResume");
        }
    }



    @Override
    protected void onPause() {
        if(mButtonRefresh.isChecked()) {
            mButtonRefresh.callOnClick();
        }

        if (mNFCadapter != null) {
            mNFCadapter.disableForegroundDispatch(this);
            Log.d("Result", "onPause");
        }

        super.onPause();

    }

    public static String byteArrayToHexString(byte[] b) {
        if(b==null)
            return "warnning : null String";
        int len = b.length;
        String data = new String();

        for (int i = 0; i < len; i++){
            data += " 0x" + Integer.toHexString((b[i] >> 4) & 0xf);

            data += Integer.toHexString(b[i] & 0xf);
        }
        return data;
    }

    public static String byteArrayToHexString(byte[] b, int offset) {
        if(b==null)
            return "warnning : null String";
        int len = b.length;
        String data = new String();

        for (int i = offset; i < len; i++){
            data += " 0x" + Integer.toHexString((b[i] >> 4) & 0xf);

            data += Integer.toHexString(b[i] & 0xf);
        }
        return data;
    }

    List<Integer> ReadTabAndGetDiffDataIdxes(Tag detectedTag, int idx)
    {
        byte[] block = new byte[4];
        //int i= tabHost1.getCurrentTab();
        //int i = idx;
        byte blockSize = (byte)dataContainer.getSubList(idx).getTotalBlockSize();
        short headPos = (short)dataContainer.getSubList(idx).getDefaultAddress();

        byte[] result = NfcVComm.readTagMultiBlock(detectedTag, headPos, blockSize);
        if(result==null || result.length ==0)
        {
            return null;
        }
        List<Integer> resultList= new ArrayList<Integer>();
        if(result !=null)
        {
            for(int j=0; j<blockSize; j++)
            {

                block[3] = result[j*4 + 0];
                block[2] = result[j*4 + 1];
                block[1] = result[j*4 + 2];
                block[0] = result[j*4 + 3];

                //System.arraycopy(result, j*4, block, 0, 4);
                //Log.d("MainActivity" , "cropped value : [" +j+"]"+ byteArrayToHexString(block));
                int varIdx = DataContainer.getVarIdx(idx, j);
                byte[] dataValue = dataContainer.getValue(varIdx);

                if(dataValue[3]==block[3] && dataValue[2]==block[2] && dataValue[1]==block[1] && dataValue[0]==block[0])
                {
                    Log.d("MainActivity" , "compare value[E] : [" +j+"]"+ byteArrayToHexString(block) + ":" + byteArrayToHexString(dataValue) );
                }
                else
                {
                    Log.d("MainActivity" , "compare value[N] : [" +j+"]"+ byteArrayToHexString(block) + ":" + byteArrayToHexString(dataValue) );
                    resultList.add(varIdx);

                }
            }
            //Toast.makeText(this, "[ReadTab SUCCESS]"+idx +  "result= null", Toast.LENGTH_SHORT).show();
            return resultList;
        }

        else
        {
            Log.d("ReadTab", "[ERROR] tab[" + idx + "], result= null");
            //Toast.makeText(this, "[ReadTab ERROR]"+idx +  "result= null", Toast.LENGTH_SHORT).show();
            return null;
        }
    }



    boolean ReadTab(Tag detectedTag, int idx)
    {
        byte[] block = new byte[4];
        //int i= tabHost1.getCurrentTab();
        int i = idx;
        byte blockSize = (byte)dataContainer.getSubList(i).getTotalBlockSize();
        short headPos = (short)dataContainer.getSubList(i).getDefaultAddress();

        byte[] result = NfcVComm.readTagMultiBlock(detectedTag, headPos, blockSize);
        try{
            if(result !=null)
            {
                for(int j=0; j<blockSize; j++)
                {

                    block[3] = result[j*4 + 0];
                    block[2] = result[j*4 + 1];
                    block[1] = result[j*4 + 2];
                    block[0] = result[j*4 + 3];

                    //System.arraycopy(result, j*4, block, 0, 4);
                    Log.d("MainActivity" , "cropped value : [" +j+"]"+ byteArrayToHexString(block));
                    int varIdx = DataContainer.getVarIdx(idx, j);

                    dataContainer.setValue(varIdx, block);
                }
                //Toast.makeText(this, "[ReadTab SUCCESS]"+idx +  "result= null", Toast.LENGTH_SHORT).show();

            }

            else
            {
                Log.d("ReadTab", "[ERROR] tab[" + idx + "], result= null");
                //Toast.makeText(this, "[ReadTab ERROR]"+idx +  "result= null", Toast.LENGTH_SHORT).show();
                return false;
            }

            Log.d("ReadTab", "setValue = "+ dataContainer.toString());
            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

    boolean ReadAll(Tag detectedTag)
    {

        boolean isSuccess = true;
        String idxFailedTab = "";
        for(int i = 0; i< recyclerViewArray.length; i++) {
            if(!ReadTab(detectedTag, i))
            {
                idxFailedTab+= ""+i+", ";
                isSuccess = false;
            }
        }
        Log.d("ReadAll", "isSuccess = " + isSuccess + ", idx = " + idxFailedTab);
        return isSuccess;

    }

    void WriteEditedOnly(Tag detectedTag)
    {
        HashMap<Integer, Integer> map = dataContainer.getWriterByteValues();
        HashSet<Integer> writenIdxs = NfcVComm.writeTagAll(detectedTag, map);
        dataContainer.clearWriterIdx(writenIdxs);
        for(int i=0; i<DataContainer.MAX_IDX;i++)
            ((RecyclerViewAdapter)recyclerViewArray[i].getAdapter()).refresh();
    }

    boolean ReadMonitorValues2(Tag detectedTag)
    {
        MonitorThread thread = new MonitorThread(me, dataContainer);
        thread.setTag(detectedTag);
        thread.start();
        return true;
    }

    boolean ReadMonitorValues(Tag detectedTag)
    {
        if(detectedTag ==null)
            return false;

        int statusInverterIdx = ParamTable.getTableIdx(ParamTable.Param_table.statusinverter);
        byte blockSize = (byte)dataContainer.getSubList(statusInverterIdx).getTotalBlockSize();
        short headPos = (short)dataContainer.getSubList(statusInverterIdx).getDefaultAddress();
        Log.d("MonitorThread", "tabIdx = " + statusInverterIdx + ", block size = " + blockSize + ", headPos = " + headPos);
        //byte[] result = NfcVComm.readTagMultiBlock(detectedTag, headPos, blockSize);
        byte[] result = NfcVComm.readTagMultiBlockForMonitoring(detectedTag, headPos, blockSize);

        if(result !=null)
        {
            byte[] block = new byte[4];
            for(int j=0; j<blockSize; j++)
            {

                block[3] = result[j*4 + 0];
                block[2] = result[j*4 + 1];
                block[1] = result[j*4 + 2];
                block[0] = result[j*4 + 3];

                //System.arraycopy(result, j*4, block, 0, 4);
                //Log.d("MonitorThread" , "Read IO");
                int varIdx = DataContainer.getVarIdx(statusInverterIdx, j);
                dataContainer.setValue(varIdx, block);
            }
        }
        else
        {
            Log.d("ReadTab", "[ERROR] tab[" + statusInverterIdx + "], result= null");
            //Toast.makeText(this, "[ReadTab ERROR]"+idx +  "result= null", Toast.LENGTH_SHORT).show();
            return false;
        }

        Log.d("ReadTab", "setValue = "+ dataContainer.toString());
        return true;
    }

    void WriteAll(Tag detectedTag)
    {
        Log.d("WriteAll", "Call WriteAll");
        //HashSet<Integer> writenIdxs = NfcVComm.writeTagAll(detectedTag, map);
        int[] getWriterIntValuesAll = dataContainer.getWriterIntValuesAll();
        short headAddr = 0;

        if(!NfcVComm.writeTagAll(detectedTag, getWriterIntValuesAll))
        {
            Toast.makeText(me, "Write all is failed!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(me, "Write all is succeed!", Toast.LENGTH_SHORT).show();
        }
    }

    void ReadAllAndWriteDiff(Tag detectedTag)
    {

        //Read All writables
        Log.d("WriteAll", "Call WriteAll");

        List<Integer> diffs = new ArrayList<>();

        int[] selectedIdxes;
        for(int i = 0; i< DataContainer.MAX_IDX_WRITABLE; i++) {
            List<Integer> getTabs = ReadTabAndGetDiffDataIdxes(detectedTag, i);

            if(getTabs !=null)
            {
                diffs.addAll(getTabs);
            }
            else
            {
                Toast.makeText(me, "Write : Read curr is failed!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(diffs.size()==0)
        {

            Toast.makeText(me, "Write : No changed value!!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("ReadDiff", "idxs = " + diffs.toString());

        selectedIdxes = new int[diffs.size()];
        for(int i=0; i<diffs.size() ;i++)
        {
            selectedIdxes[i] = diffs.get(i);
        }
        int[] getWriterIntValuesAll = dataContainer.getWriterIntValuesByIdxs(selectedIdxes);

        if(!NfcVComm.writeTag(detectedTag, selectedIdxes, getWriterIntValuesAll))
        {
            Toast.makeText(me, "Write all is failed!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(me, "Write all is succeed!", Toast.LENGTH_SHORT).show();
        }

    }

    int monitorIdx = 0;
    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        if (intent == null) {
            Log.e("MainActivity", "intent is null!");
            return;
        }


        final Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String[] tech = detectedTag.getTechList();
        String techList = "";
        for(int i =0; i<tech.length;i++)
            techList += tech[i] + ":";
        Log.d("onNewIntent", ""+techList + "Action  = " + rwMode);


        vibrateStart();

        int sublistIdx = tabHost1.getCurrentTab();
        switch(rwMode)
        {
            case Read:

                if(ReadTab(detectedTag, tabHost1.getCurrentTab()))
                    Toast.makeText(this, "Success Read!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Failed Read! Please retry...", Toast.LENGTH_SHORT).show();
                OnTag();
                updateAllView();
                //ReadTab(detectedTag, tabHost1.getCurrentTab());
                break;
            case ReadAll:

                if(ReadAll(detectedTag))
                    Toast.makeText(this, "Success ReadAll!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Failed ReadAll! Please retry...", Toast.LENGTH_SHORT).show();
                OnTag();
                updateAllView();
                break;
            case EditAndWrite:
                Toast.makeText(this, "EditAndWrite try!", Toast.LENGTH_SHORT).show();
                WriteEditedOnly(detectedTag);
                OnTag();
                updateAllView();

                break;
            case CopyAndWriteAll:

                Toast.makeText(this, "CopyAndWriteAll try!", Toast.LENGTH_SHORT).show();
                ReadAllAndWriteDiff(detectedTag);
                OnTag();
                updateAllView();
                break;
            case Monitor:
                if(mButtonRefresh.isChecked() && !mButtonSwitchInput.isChecked())
                {
                    if(ReadMonitorValues2(detectedTag))
                    //if(ReadMonitorValues(detectedTag))
                    {
                        Toast.makeText(this, "Read Monitor Value", Toast.LENGTH_SHORT).show();
                        swapMonitorView();
                    }
                    else
                    {
                        Toast.makeText(this, "Read Monitor Value failed", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(this, "Read Monitor Value : button off" + monitorIdx++ , Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                if(isForceToTurnOn)
                {
                    Toast.makeText(this, "tag : Run !", Toast.LENGTH_SHORT).show();
                    if(WriteRun(detectedTag))
                    {
                        Toast.makeText(this, "Run control set!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "Tag : Stop!", Toast.LENGTH_SHORT).show();
                    if(WriteStop(detectedTag))
                    {
                        Toast.makeText(this, "Stop control set!", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
        vibrateEnd();
    }

    private boolean WriteStop(Tag detectedTag) {
        return NfcVComm.writeRun(detectedTag, false);
    }

    private boolean WriteRun(Tag detectedTag) {
        return NfcVComm.writeRun(detectedTag, true);
    }


    public void refreshFromUI()
    {
        if(mButtonWrite.isChecked())
            rwMode = RWMode.Mode.CopyAndWriteAll;
        else if(mButtonRead.isChecked())
            rwMode = RWMode.Mode.ReadAll;
        else if(mButtonHotfix.isChecked())
            rwMode = RWMode.Mode.EditAndWrite;
        else if(mButtonRefresh!=null && mButtonRefresh.isChecked())
        {
            //rwMode = RWMode.Mode.Monitor;
            rwMode = RWMode.Mode.Monitor;
            //setMonitorEnter();
        }
        else
        {
            //rwMode = RWMode.Mode.Default;
            rwMode = RWMode.Mode.Default;
            //setMonitorQuit();
            //return;
        }
        Toast.makeText(me,"set, mode : " + rwMode, Toast.LENGTH_SHORT).show();

    }


    public void OnTag()
    {
        mButtonRead.setChecked(false);
        mButtonWrite.setChecked(false);
        rwMode = RWMode.Mode.Default;
        setEnable(true);
        //Toast.makeText(me,"tag, mode : " + rwMode, Toast.LENGTH_SHORT).show();
    }
    TabHost tabHost1;
    public void generateTabUI()
    {
        tabHost1 = (TabHost) findViewById(R.id.MainTabHost) ;
        tabHost1.setup() ;

        TabHost.TabSpec[] ts = new TabHost.TabSpec[dataContainer.MAX_IDX ];
        recyclerViewArray = new RecyclerView[dataContainer.MAX_IDX ];

        recyclerViewArray[0] = (RecyclerView)findViewById(R.id.recyclerView1);
        recyclerViewArray[1] = (RecyclerView)findViewById(R.id.recyclerView2);
        recyclerViewArray[2] = (RecyclerView)findViewById(R.id.recyclerView3);
        recyclerViewArray[3] = (RecyclerView)findViewById(R.id.recyclerView4);
        recyclerViewArray[4] = (RecyclerView)findViewById(R.id.recyclerView5);
        recyclerViewArray[5] = (RecyclerView)findViewById(R.id.recyclerView6);
        recyclerViewArray[6] = (RecyclerView)findViewById(R.id.recyclerView7);
        recyclerViewArray[7] = (RecyclerView)findViewById(R.id.recyclerView8);




        for (int i=0; i<dataContainer.MAX_IDX ; i++)
        {
            ts[i]=  tabHost1.newTabSpec("Tab Spec"+ i) ;
            generateRecyclerListView(recyclerViewArray[i], dataContainer.getSubList(i).getNameList(), dataContainer.getSubList(i).getValueList(), i);
        }

        ts[0].setContent(R.id.tab1) ;
        ts[1].setContent(R.id.tab2) ;
        ts[2].setContent(R.id.tab3) ;
        ts[3].setContent(R.id.tab4) ;
        ts[4].setContent(R.id.tab5) ;
        ts[5].setContent(R.id.tab6) ;
        ts[6].setContent(R.id.tab7) ;
        ts[7].setContent(R.id.tab8) ;



        TabWidget tw = (TabWidget)tabHost1.findViewById(android.R.id.tabs);


        //spinners = new Spinner[dataContainer.MAX_IDX];

        //Log.d("onCreate", "toggleButtons = "+toggleButtons[0]);
        //isWrite = new boolean[dataContainer.MAX_IDX];



        for (int i=0; i<dataContainer.MAX_IDX ; i++)
        {
            String tabName = dataContainer.getSubList(i).getName();
            //String result = tabName.charAt(0) + "."  + tabName.charAt(tabName.length()/2) + tabName.charAt(tabName.length()-1);
            ts[i].setIndicator(tabName ) ;

            tabHost1.addTab(ts[i]);
            TextView tv = (TextView)tw.getChildTabViewAt(i).findViewById(android.R.id.title);
            tv.setTextSize(12);

        }

        tabHost1.setVisibility(View.INVISIBLE);

        //mProgressbar = (ProgressBar)findViewById(R.id.progressBar);
        //mProgressbar.setVisibility(View.INVISIBLE);
        refreshFromUI();
    }


    private final String[] mSpinnersList = {
            "Tab 읽기",
            "모두 읽기",
            "편집 쓰기",
            "일괄 쓰기",

    };



    public void selectMenuItem(int menuID)
    {
        int settings = View.INVISIBLE;
        int file= View.INVISIBLE;
        int edit= View.INVISIBLE;
        int monitor = View.INVISIBLE;
        int summary = View.INVISIBLE;
        switch(menuID)
        {
            case R.id.menuItemParamSet:
                file = View.VISIBLE;
                break;
            case R.id.menuItemEdit:
                edit = View.VISIBLE;
                break;
            case R.id.menuItemMonitor:
                monitor = View.VISIBLE;
                if(mButtonRefresh.isChecked())
                {
                    rwMode = RWMode.Mode.Monitor;
                }
                else
                {
                    rwMode = RWMode.Mode.Default;
                }
                break;
            case R.id.menuItemFinish:
                finish();
                break;
            case R.id.menuItemSummary:
                summary = View.VISIBLE;
                rwMode = RWMode.Mode.Summary;
                break;
            default:
                rwMode = RWMode.Mode.Default;
                break;
        }
        tabHost1.setVisibility(edit);
        //mProgressbar.setVisibility(edit);

        mRViewSettings.setVisibility(settings);

        mRViewFiles.setVisibility(file);

        mRViewMonitor.setVisibility(monitor);

        mButtonRefresh.setVisibility(monitor);
        mButtonSwitchInput.setVisibility(monitor);
        if(rwMode != RWMode.Mode.Monitor)
        {
            mButtonRefresh.setChecked(false);
            mButtonSwitchInput.setChecked(false);
        }
        /*
        if(mButtonRefresh.isChecked())
        {
            mButtonRefresh.callOnClick();
        }


        if(mButtonSwitchInput.isChecked()) {
            mButtonSwitchInput.callOnClick();
        }
        */
        setSummaryUIVisible(summary);





    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        selectMenuItem(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void generateRecyclerListView(RecyclerView recyclerView, List<String> tableName, List<String> tableItems, final int idx)
    {


        LinearLayoutManager manager = new LinearLayoutManager(me);
        recyclerView.setHasFixedSize(true);
        RecyclerViewAdapter thisAdapter = new RecyclerViewAdapter(tableName, tableItems, idx);
        recyclerView.setAdapter(thisAdapter);
        recyclerView.setLayoutManager(manager);
    }

    public void swapRecyclerListView(RecyclerView recyclerView, List<String> tableName, List<String> tableItems, final int idx)
    {

        RecyclerViewAdapter thisAdapter = new RecyclerViewAdapter(tableName, tableItems, idx);
        recyclerView.swapAdapter(thisAdapter, false);

    }

    public void swapMonitorView()
    {
        int statusInverterIDX = ParamTable.getTableIdx(ParamTable.Param_table.statusinverter);
        List<String> names = dataContainer.getSubList(ParamTable.Param_table.statusinverter).getNameList();
        List<String> values = dataContainer.getValueList(statusInverterIDX) ;
                //dataContainer.getSubList(ParamTable.Param_table.statusinverter).getValueList();
        mAdapterMonitors = new RecyclerViewAdapter_monitor(names, values);
        mRViewMonitor.swapAdapter(mAdapterMonitors, false);
    }

    public void swapFileListView()
    {

        mAdapterFilse = new RecyclerViewAdapter_File(getFilesList());
        mRViewFiles.swapAdapter(mAdapterFilse, false);

    }

    public void swapSettingsListView()
    {
        mAdapterSettings = new RecyclerViewAdapter_File(new ArrayList<String>());
        mRViewSettings.swapAdapter(mAdapterSettings, false);

    }

    public RecyclerView mRViewSettings;
    public RecyclerView mRViewFiles;
    public RecyclerView mRViewMonitor;
    public RecyclerViewAdapter_File mAdapterSettings;
    public RecyclerViewAdapter_File mAdapterFilse;
    public RecyclerViewAdapter_monitor mAdapterMonitors;

    public List<String> getFilesList()
    {
        String defaultItem  = "NEW ITEM";
        List<String> list = new ArrayList<>();
        list.add(defaultItem);
        list.addAll(Arrays.asList(FileManager.getFileList(this)));
        return list;
    }

    public List<String> getMonitorList()
    {
        List<String> list = new ArrayList<>();
        list.addAll(dataContainer.getSubList(ParamTable.Param_table.statusinverter).getNameList());
        //list.addAll(Arrays.asList(FileManager.getFileList(this)));
        return list;
        //, dataContainer.getValueList(listIdx)
        //list.addAll(DataContainer.getTableIdx());
    }

    public void generateFileListUI()
    {
        LinearLayoutManager manager = new LinearLayoutManager(me);

        mAdapterFilse = new RecyclerViewAdapter_File(getFilesList());
        mRViewFiles = (RecyclerView)findViewById(R.id.recyclerViewFiles);
        mRViewFiles.setHasFixedSize(true);
        mRViewFiles.setAdapter(mAdapterFilse);
        mRViewFiles.setLayoutManager(manager);
        mRViewFiles.setVisibility(View.INVISIBLE);
    }

    public void generateSettingsUI()
    {
        mAdapterSettings = new RecyclerViewAdapter_File(new ArrayList<String>());
        mRViewSettings = (RecyclerView)findViewById(R.id.recyclerViewSettings);
        mRViewSettings.setHasFixedSize(true);
        mRViewSettings.setAdapter(mAdapterSettings);
        LinearLayoutManager manager = new LinearLayoutManager(me);
        mRViewSettings.setLayoutManager(manager);
        mRViewSettings.setVisibility(View.INVISIBLE);
    }


    static void showFileCreateDialog(String title, final int idx, String value){
        AlertDialog.Builder builder = new AlertDialog.Builder(me);
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(me);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        input.setText(value);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            inputIdx = idx;
            inputText = input.getText().toString();

            if(inputText == null || inputText.length() ==0)
            {
                return ;
            }

            //file already exists
            if(Arrays.asList(FileManager.getFileList(me)).contains(inputText))
            {
                return ;
            }
            //FileManager.makeFile(me, inputText);
            dataContainer = new DataContainer();
            dataContainer.save(me, inputText);
            dataContainer.load(me, inputText);
            MainActivity.setFileName(inputText);

            me.mAdapterFilse.updateSelection(inputText);
            me.swapFileListView();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                inputIdx = -1;
                inputText = "";

            }
        });
        builder.show();

    }


    static void showFileNameChangeDialog(String title, final int idx, final String value){
        AlertDialog.Builder builder = new AlertDialog.Builder(me);
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(me);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        input.setText(value);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                inputIdx = idx;
                inputText = input.getText().toString();

                if(inputText == null || inputText.length() ==0)
                {
                    return ;
                }

                //file already exists
                if(Arrays.asList(FileManager.getFileList(me)).contains(inputText))
                {
                    return ;
                }

                FileManager.renameFile(me, value, inputText);
                deleteFileItem(value);
                saveData(inputText);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                inputIdx = -1;
                inputText = "";

            }
        });
        builder.show();

    }
    private static String TAG = "Main Activity";

    public static void deleteFileItem(String fileName) {
        Log.d(TAG, "Del");
        if(FileManager.deleteFile(me, fileName))
            me.swapFileListView();
    }


    public static void loadData(String fileName) {
        Log.d(TAG, "LOADDATA");
        if(fileName == null)
            return;
        dataContainer = DataContainer.load(me, fileName);
        setFileName(fileName);
        me.updateAllView();
    }

    public static void saveData(String fileName) {
        Log.d(TAG, "SAVEDATA");
        if(fileName == null)
            return;
        setFileName(fileName);
        dataContainer.save(me, fileName);

    }

    public static void setFileName(String fileName)
    {

        dataContainer.mFileName = fileName;
        me.updateSelectedFileName(fileName);
        //Toast.makeText(me, fileName, Toast.LENGTH_SHORT).show();
    }



    public void setToggleButtonUI(){
        mButtonRead = findViewById(R.id.toggleButtonRead);
        mButtonRead.setTextOn("읽기 중");
        mButtonRead.setTextOff("읽기");
        mButtonRead.setChecked(false);

        mButtonWrite = findViewById(R.id.toggleButtonWrite);
        mButtonWrite.setTextOn("쓰기 중");
        mButtonWrite.setTextOff("쓰기");
        mButtonWrite.setChecked(false);

        mButtonHotfix = findViewById(R.id.toggleButtonHotfix);
        mButtonHotfix.setTextOn("핫픽스");
        mButtonHotfix.setTextOff("핫픽스");
        mButtonHotfix.setChecked(false);

        mButtonRead.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(mButtonRead.isChecked())
                {
                    me.setEnable(false);
                    mButtonRead.setEnabled(true);
                }
                else
                {
                    me.setEnable(true);
                }
                refreshFromUI();
            }
        });

        mButtonWrite.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(mButtonWrite.isChecked())
                {
                    me.setEnable(false);
                    mButtonWrite.setEnabled(true);
                }
                else
                {
                    me.setEnable(true);
                }
                refreshFromUI();
            }
        });

        mButtonHotfix.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(mButtonHotfix.isChecked())
                {
                    me.setEnable(false);
                    mButtonHotfix.setEnabled(true);
                }
                else
                {
                    me.setEnable(true);
                }
                refreshFromUI();
            }
        });

        mButtonLoadParam = findViewById(R.id.buttonLoadParam);
        mButtonSaveParam = findViewById(R.id.buttonSaveParam);

        mButtonLoadParam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                loadData(dataContainer.mFileName);
            }
        });

        mButtonSaveParam.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                saveData(dataContainer.mFileName);
            }
        });


    }

    Button mButtonLoadParam;
    Button mButtonSaveParam;
    boolean isItemSelectable = true;

    public void setEnable(boolean enable) {
        mButtonRead.setEnabled(enable);
        mButtonWrite.setEnabled(enable);
        mButtonHotfix.setEnabled(enable);
        mButtonLoadParam.setEnabled(enable);
        mButtonSaveParam.setEnabled(enable);
        tabHost1.setEnabled(enable);
        tabHost1.getTabWidget().setEnabled(enable);
        isItemSelectable = enable;
    }




    public void callSwapMoniterView() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        swapMonitorView();
                    }
                });

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }

    public void updateMonitorValue(byte[] response) {
        setMonirotValue();
        callSwapMoniterView();
    }

    private void setMonirotValue() {

    }

    public DataContainer getDataContainer() {
        return dataContainer;
    }


}