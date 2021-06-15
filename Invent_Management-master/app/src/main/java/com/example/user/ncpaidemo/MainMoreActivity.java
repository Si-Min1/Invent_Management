package com.example.user.ncpaidemo;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainMoreActivity extends AppCompatActivity {

    private ArrayList<UserItem> list = new ArrayList<>(); // 아이템 리스트
    private ArrayList<String> keys = new ArrayList<>(); //키 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //note 타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //note 투명 배경
        getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        setContentView(R.layout.content_main_more);

        ObjectAnimator imgAnimT = ObjectAnimator.ofFloat(findViewById(R.id.titleAnim), "translationY", -50f,-250f).setDuration(500);
        ObjectAnimator infoAnimT = ObjectAnimator.ofFloat(findViewById(R.id.item_info), "translationY", -100f,-40f).setDuration(1000);
        imgAnimT.start();
        infoAnimT.start();


        Intent intent = getIntent();

        list = (ArrayList<UserItem>) intent.getSerializableExtra("userItems");
        keys = (ArrayList<String>) intent.getSerializableExtra("keys");

        TextView lCategory = findViewById(R.id.item_lCategory);
        TextView sCategory = findViewById(R.id.item_sCategory);
        TextView nowAmount = findViewById(R.id.item_nowAmount);
        TextView maxDay = findViewById(R.id.item_maxDay);


        lCategory.setText(intent.getStringExtra("lCategory"));
        sCategory.setText(intent.getStringExtra("sCategory"));
        nowAmount.setText(""+intent.getIntExtra("nowAmount",0)+intent.getStringExtra("unit"));
        maxDay.setText("D-"+intent.getIntExtra("maxDay",0));


        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.user_item_list);


        new UserItemAdapterR().setConfig(mRecyclerView, MainMoreActivity.this, list, keys);

    }
}
