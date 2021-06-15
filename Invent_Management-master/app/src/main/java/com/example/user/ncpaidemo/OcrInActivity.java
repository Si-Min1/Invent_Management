package com.example.user.ncpaidemo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.ncpaidemo.MainActivity.setListViewHeightBasedOnChildren;
import static com.example.user.ncpaidemo.UserItemAdapter.isStringDouble;

public class OcrInActivity extends BaseActivity {

    //note UserItem List!
    private ArrayList<UserItem> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //note 타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //note 투명 배경
        getWindow().setBackgroundDrawable(new ColorDrawable((Color.TRANSPARENT)));

        setContentView(R.layout.content_ocr_in);


        ListView listView = findViewById(R.id.in_group);
        Intent intent = getIntent();

        list = (ArrayList<UserItem>) intent.getSerializableExtra("userItem");

        UserItemAdapter adpater = new UserItemAdapter(this, R.layout.content_ocr_in_list, list);
        listView.setAdapter(adpater);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (SCROLL_STATE_TOUCH_SCROLL == scrollState) {
                    listView.findViewById(R.id.item_name).clearFocus();
                    listView.findViewById(R.id.item_sCategory).clearFocus();
                    listView.findViewById(R.id.item_price).clearFocus();
                    listView.findViewById(R.id.item_unit_amount).clearFocus();
                    listView.findViewById(R.id.item_count).clearFocus();
                    listView.findViewById(R.id.item_nDay).clearFocus();

                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

        });


        setListViewHeightBasedOnChildren(listView);


        //note 상세입력 모두 입력후 확인 버튼
        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<UserItem> addItem = new ArrayList<>(); //새로 추가할 아이템
                ArrayList<UserItem> updateItem = new ArrayList<>(); // 업데이트할 아이템

                FirebaseUserHelper userHelper = new FirebaseUserHelper();

                boolean success=false;

                for(int i=0; i<list.size(); i++){
                    if(list.get(i).getPrice()<=0 || list.get(i).getUnit_amount()<=0 ||
                            list.get(i).getCount()<=0 || list.get(i).getnDay()<=0){
                        Toast.makeText(OcrInActivity.this, "빈칸 혹은 잘못된 값이 입력되었습니다", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if(list.get(i).getlCategory()==null||list.get(i).getsCategory().equals("")||list.get(i).getUnit().contains("▼")){
                        Toast.makeText(OcrInActivity.this, "카테고리를 다시 확인하세요", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(OcrInActivity.this, "성공!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            inList = list;
                            System.out.println("####### Ocr In Activiity 에서의 inList : "+inList);
                            intent.putExtra("frag", 1);
                            startActivity(intent);
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
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
