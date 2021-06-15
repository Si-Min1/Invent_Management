package com.example.user.ncpaidemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.load.engine.Resource;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.ncpaidemo.MainActivity.setListViewHeightBasedOnChildren;

public class SelfInActivity extends BaseActivity {

    ArrayList<UserItem> list = new ArrayList<>();
    public static final int REQUEST_CODE_MENU = 101;
    public static final int REQUEST_CODE_MENU_USER = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_self_input);

        ImageButton selectInBtn = (ImageButton) findViewById(R.id.select_in_btn);

        selectInBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                AlertDialog.Builder dlg = new AlertDialog.Builder(SelfInActivity.this);
                dlg.setTitle("가져올 원재료를 선택하세요"); //제목
                String[] versionArray = new String[]{"베이스 원재료", "사용자 원재료"};

                dlg.setItems(versionArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (versionArray[which].equals("사용자 원재료")) {
                            Intent intent = new Intent(getApplicationContext(), SelectInActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_MENU_USER);
                        } else { //베이스 원재료
                            Intent intent = new Intent(getApplicationContext(), SelectBaseActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_MENU);
                        }

                    }
                });
                dlg.show();
            }
        });


        //note 상세입력 모두 입력후 확인 버튼
        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUserHelper userHelper = new FirebaseUserHelper();

                boolean success=false;

                for(int i=0; i<list.size(); i++){
                    if(list.get(i).getPrice()<=0 || list.get(i).getUnit_amount()<=0 ||
                            list.get(i).getCount()<=0 || list.get(i).getnDay()<=0){
                        Toast.makeText(SelfInActivity.this, "빈칸 혹은 잘못된 값이 입력되었습니다", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(list.get(i).getsCategory().equals("")||list.get(i).getUnit().contains("▼")){
                        Toast.makeText(SelfInActivity.this, "카테고리를 다시 확인하세요", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(i==list.size()-1) success=true;
                }
                if(success) {

                    for(int i=0; i<list.size();i++){
                        list.get(i).setUnit_price(list.get(i).getPrice()/list.get(i).getCount());
                        list.get(i).setAmount(list.get(i).getUnit_amount()*list.get(i).getCount());
                    }

                    userHelper.addUserItem(list, new FirebaseUserHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(ArrayList<UserItem> userItems, ArrayList<String> keys) {
                        }

                        @Override
                        public void DataIsInserted() {
                            Toast.makeText(SelfInActivity.this, "성공!", Toast.LENGTH_SHORT).show();
                            finish();
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
        });



    }


    public void itemsList(Intent intent) {

        ListView listView = findViewById(R.id.in_group);

        String name = intent.getStringExtra("name");
        String lCategory = intent.getStringExtra("lCategory");
        String sCategory = intent.getStringExtra("sCategory");
        int nDay = intent.getIntExtra("nDay", 1);

        UserItem userItem = new UserItem(sCategory, lCategory, sCategory, nDay);
        list.add(userItem);

        SelfInAdapter adapter = new SelfInAdapter(this, R.layout.content_self_input_list, list);
        listView.setAdapter(adapter);

        setListViewHeightBasedOnChildren(listView);

    }

    public void userList(Intent intent) {
        ListView listView = findViewById(R.id.in_group);

        UserItem item = (UserItem) intent.getSerializableExtra("userItem");
        list.add(item);

        SelfInAdapter adapter = new SelfInAdapter(this, R.layout.content_self_input_list, list);
        listView.setAdapter(adapter);

        setListViewHeightBasedOnChildren(listView);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MENU) {
            if (resultCode == RESULT_OK) {
                itemsList(data);
                System.out.print(data.getExtras());
            }
        } else if (requestCode == REQUEST_CODE_MENU_USER) {
            if (resultCode == RESULT_OK) {
                userList(data);
            }
        }
    }

}
