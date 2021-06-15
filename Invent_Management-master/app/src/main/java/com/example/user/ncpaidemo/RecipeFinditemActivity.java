package com.example.user.ncpaidemo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipeFinditemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //note 타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.content_recipe_add_item);


        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.wUserItem_list);
        new FirebaseUserHelper().readUserItem(new FirebaseUserHelper.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<UserItem> userItems, ArrayList<String> keys) {

                new WideUSerItemAdapter().setConfig(mRecyclerView, RecipeFinditemActivity.this, userItems, keys);

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

    }
}