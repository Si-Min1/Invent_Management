package com.example.user.ncpaidemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class AddImageTypeMenu extends  PopupActivity {

    int inoutFlag; //       0 : 입고 /    1: 출고
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //note 타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.flag_plus);
        getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));

        RadioGroup inoutGroup = findViewById(R.id.inoutGroup);
        inoutGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);


        // note 카메라
        findViewById(R.id.camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                intent.putExtra("inoutFlag",inoutFlag);
                startActivity(intent);
                finish();
            }
        });
        // note 갤러리
        findViewById(R.id.gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GalleryActivity.class);
                intent.putExtra("inoutFlag",inoutFlag);
                startActivity(intent);
                finish();
            }
        });

        //note 직접입력
        findViewById(R.id.self_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inoutFlag == 0) //입고
                {
                    Intent intent = new Intent(getApplicationContext(), SelfInActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(inoutFlag == 1)//출고
                {
                    Intent intent = new Intent(getApplicationContext(), SelfOutActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        // 권한 체크
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }
    //권한 허용 및 거부
    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "권한이 허용됨",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "권한이 거부됨",Toast.LENGTH_SHORT).show();
        }
    };

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @SuppressLint("UseCompatLoadingForDrawables")
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            findViewById(R.id.input_option).setVisibility(View.VISIBLE);
            Button inbtn = findViewById(R.id.inbtn);
            Button outbtn = findViewById(R.id.outbtn);


            if(i == R.id.inbtn){
                inbtn.setBackground(getResources().getDrawable(R.drawable.button_round));
                outbtn.setBackground(getResources().getDrawable(R.drawable.button_round_solid));
                inoutFlag = 0;
            }
            else if(i == R.id.outbtn){
                outbtn.setBackground(getResources().getDrawable(R.drawable.button_round));
                inbtn.setBackground(getResources().getDrawable(R.drawable.button_round_solid));
                inoutFlag = 1;
            }

        }
    };

}
