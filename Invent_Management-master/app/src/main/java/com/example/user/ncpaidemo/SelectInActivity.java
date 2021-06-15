package com.example.user.ncpaidemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kakao.usermgmt.response.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.layout.simple_list_item_1;
import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_VERTICAL;
import static android.view.Gravity.RIGHT;
import static com.example.user.ncpaidemo.MainActivity.setListViewHeightBasedOnChildren;

public class SelectInActivity extends BaseActivity {

    private int item_position;
    Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //note 타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //note 투명 배경
        getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));
        setContentView(R.layout.content_select_in);


        // itemsList();

        ImageButton ok = findViewById(R.id.ok_btn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent == null) {
                    Toast.makeText(getApplicationContext(), "선택하세요", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("################## : " + intent.getExtras());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        ListView listView = (ListView) findViewById(R.id.in_group);
        new FirebaseUserHelper().readUserItem(new FirebaseUserHelper.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<UserItem> userItems, ArrayList<String> keys) {
                //new WideUSerItemAdapter().setConfig(mRecyclerView, getContext(), userItems, keys);
                UserItemAdapter adpater = new UserItemAdapter(SelectInActivity.this, R.layout.content_select_in_list, userItems);
                listView.setAdapter(adpater);

                setListViewHeightBasedOnChildren(listView);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        intent = new Intent(getApplicationContext(), SelfInActivity.class);
                        AssetManager assetManager = view.getResources().getAssets();

                        adpater.setPosition(position);
                        try {
                            InputStream is = assetManager.open("base.json");
                            InputStreamReader isr = new InputStreamReader(is);
                            BufferedReader reader = new BufferedReader(isr);

                            StringBuffer buffer = new StringBuffer();
                            String line = reader.readLine();
                            while (line != null) {
                                buffer.append(line + "\n");
                                line = reader.readLine();
                            }
                            String jsonData = buffer.toString();

                            JSONArray jsonArray = new JSONObject(jsonData).getJSONObject("BaseInfo").getJSONObject("lCategory").getJSONArray(userItems.get(position).getlCategory());
                            ArrayList<String> list = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (userItems.get(position).getsCategory().equals(jsonArray.getJSONObject(i).getString("sCategory"))) {
                                    userItems.get(position).setnDay(jsonArray.getJSONObject(i).getInt("nDay"));
                                }

                            }
                            intent.putExtra("userItem", userItems.get(position));
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        listView.setAdapter(adpater);

                    }

                });
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


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("UseCompatLoadingForDrawables")
    public void itemsList() {

        ListView listView = findViewById(R.id.in_group);

        ArrayList<UserItem> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            UserItem userItem = new UserItem("(특대)딸기", "과일", "딸기", 2);

            list.add(userItem);
        }

        UserItemAdapter adpater = new UserItemAdapter(this, R.layout.content_select_in_list, list);
        listView.setAdapter(adpater);

        setListViewHeightBasedOnChildren(listView);
    }
}