package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;

/**
 * Created by ketmany on 18/03/2017.
 */

public class InputActivity extends Activity implements View.OnClickListener{

    private EditText input_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        findViewById(R.id.searchButton).setOnClickListener(this);

        Intent intent = getIntent();

        input_value = (EditText) findViewById(R.id.inputValue);
        input_value.setText(intent.getStringExtra(ScannerCaptureActivity.TextBlockObject));

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.searchButton) {
            Intent intent = new Intent(this, ResultPage.class);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("input_value", input_value.getText().toString());
            startActivity(intent);
        }
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(ScannerCaptureActivity.TextBlockObject);
                    input_value.setText(text, TextView.BufferType.EDITABLE);
                }
            }

        }
    }*/
}
