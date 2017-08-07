package com.example.finalproject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

public class MainActivity extends Activity implements View.OnClickListener {

    private CompoundButton autoFocus;
    private CompoundButton useFlash;



    private static final int RC_SCANNER_CAPTURE = 9001;
    private static final int INPUT_REQUEST = 1;
    private static final String TAG = "ScannerMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoFocus = (CompoundButton)findViewById(R.id.auto_focus);
        useFlash = (CompoundButton)findViewById(R.id.flash);
        findViewById(R.id.scanButton).setOnClickListener(this);
        findViewById(R.id.inputButton).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.scanButton) {
            Intent intent = new Intent(this, ScannerCaptureActivity.class);
            intent.putExtra(ScannerCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(ScannerCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_SCANNER_CAPTURE);
        }

        else if(view.getId() == R.id.inputButton) {
            Intent intent = new Intent(this, InputActivity.class);
            startActivityForResult(intent, INPUT_REQUEST);
        }
    }


}
