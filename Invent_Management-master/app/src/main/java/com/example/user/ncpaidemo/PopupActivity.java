package com.example.user.ncpaidemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;


public class PopupActivity extends Activity {

        //note 확인 버튼 클릭
        public void mOnClose(View v){
            //액티비티(팝업) 닫기
            finish();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            //note 바깥레이어 클릭시 안닫히게
            if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
                return false;
            }
            return true;
        }

        @Override
        public void onBackPressed() {
            //note 안드로이드 백버튼 막기
            return;
        }
    }
