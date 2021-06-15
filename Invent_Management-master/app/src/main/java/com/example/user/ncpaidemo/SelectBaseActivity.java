package com.example.user.ncpaidemo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.ncpaidemo.MainActivity.setListViewHeightBasedOnChildren;
import static com.example.user.ncpaidemo.SelfInActivity.REQUEST_CODE_MENU;

public class SelectBaseActivity extends BaseActivity {

    TabHost tabHost;

    private ArrayList<RecyclerView> mRecList = new ArrayList<>();
    private int parentPosition;
    static final String lStr[] = {"채소","과일","양곡","견과","육류","수산물","양념","조미료","소스","면류","유제품","음료","인스턴트","김치젓갈","반찬"};
    static final int id[] = {R.id.vegetable,R.id.fruit,R.id.grain,R.id.nut,R.id.meat,R.id.sea,R.id.seasoning,R.id.condiment,R.id.sauce,R.id.noodle,R.id.daily,R.id.drink,R.id.instant,R.id.salted,R.id.side};
    static Intent baseIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_select_base);

        Intent intent = getIntent();
        parentPosition = intent.getIntExtra("pos",0);

        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        for(int i=0; i<lStr.length;i++){
            setTabSpec(id[i],lStr[i]);
        }
        tabHost.setCurrentTab(0);

        for (int i=0; i<id.length; i++) {
            RecyclerView mRecyclerView = (RecyclerView) findViewById(id[i]);
            mRecList.add(mRecyclerView);
        }

        new FirebaseDatabaseHelper().fireBase(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<BaseInfo> baseInfos, List<String> keys, int id) {
                new BaseInfoAdapter().setConfig( mRecList.get(id),SelectBaseActivity.this, baseInfos, keys);

            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //baseIntent.putExtra("pos",parentPosition);
                setResult(RESULT_OK,baseIntent);
                finish();
            }
        });
    }


    void setTabSpec(int id,String name){
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tabSpec"+id).setIndicator(name);
        tabSpec.setContent(id);
        tabHost.addTab(tabSpec);

    }

}
