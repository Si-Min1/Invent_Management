package com.example.user.ncpaidemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AuthActivity extends BaseActivity {

    Button cssBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // 인증 정보 load
        SharedPreferences sharedPref1 = getSharedPreferences("PREF", Context.MODE_PRIVATE);

        String ocrApiGwUrl = sharedPref1.getString("ocr_api_gw_url", "");
        String ocrSecretKey = sharedPref1.getString("ocr_secret_key", "");


        EditText editTextOCRApiGwUrl = (EditText) findViewById(R.id.text_input_ocr_api_gw_url);
        editTextOCRApiGwUrl.setText(ocrApiGwUrl);

        EditText editTextOcrSecretKey = (EditText) findViewById(R.id.text_input_ocr_secret_key);
        editTextOcrSecretKey.setText(ocrSecretKey);

        TextView textView1 = (TextView) findViewById(R.id.textView_rlt) ;
        textView1.setText("");


        cssBtn = (Button) findViewById(R.id.btn_auth);
        cssBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    // 인증 정보 Save

                    EditText editTextOCRApiGwUrl = (EditText) findViewById(R.id.text_input_ocr_api_gw_url);
                    String clientOcrApiUrl = editTextOCRApiGwUrl.getText().toString();

                    EditText editTextOcrSecretKey = (EditText) findViewById(R.id.text_input_ocr_secret_key);
                    String clientOcrSecreteKey = editTextOcrSecretKey.getText().toString();

                    SharedPreferences sharedPref = getSharedPreferences("PREF", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("ocr_api_gw_url", clientOcrApiUrl);
                    editor.putString("ocr_secret_key", clientOcrSecreteKey);

                    editor.commit();

                    TextView textView1 = (TextView) findViewById(R.id.textView_rlt) ;
                    textView1.setText("저장되었습니다.");

                } catch (Exception e){
                    TextView textView1 = (TextView) findViewById(R.id.textView_rlt) ;
                    textView1.setText("저장 실패");

                }
            }
        });
    }
}
