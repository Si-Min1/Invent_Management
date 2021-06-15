package com.example.user.ncpaidemo;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.ncp.ai.demo.process.OcrProc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_VERTICAL;
import static android.view.Gravity.RIGHT;
import static com.example.user.ncpaidemo.MainActivity.setListViewHeightBasedOnChildren;

public class OcrActivity extends BaseActivity {

    String year, month, day;
    String store_name;

    ArrayList<String> item_name = new ArrayList<>();
    ArrayList<String> item_count = new ArrayList<>();
    ArrayList<Integer> item_unit_price = new ArrayList<>();
    ArrayList<Integer> item_price = new ArrayList<>();

    String total_price;

    int item_total_count=0;

    Intent intent;
    ArrayList<UserItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_ocr);

        ObjectAnimator nextAnim = ObjectAnimator.ofFloat(findViewById(R.id.next), "translationY", 1000f, 0f).setDuration(1000);
        ObjectAnimator titleAnim = ObjectAnimator.ofFloat(findViewById(R.id.title), "translationY", -50f, 0f).setDuration(1000);

        nextAnim.start();
        nextAnim.start();

        if(getIntent().getIntExtra("inoutFlag",0)==0) {
            intent = new Intent(getApplicationContext(), OcrInActivity.class);
        }
        else intent= new Intent(getApplicationContext(), OcrOutActivity.class);

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("userItem",list);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.repeat_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), AddImageTypeMenu.class);
                startActivity(intent);
                finish();
            }
        });



        resultOcr();

    }

    public void resultOcr(){


        //todo OCR API 직접 접근 [1]
        //SharedPreferences sharedPref = getSharedPreferences("PREF", Context.MODE_PRIVATE);
////
        //final String ocrApiGwUrl = sharedPref.getString("ocr_api_gw_url", "");
        //final String ocrSecretKey = sharedPref.getString("ocr_secret_key", "");
////
        //OcrActivity.PapagoNmtTask nmtTask = new OcrActivity.PapagoNmtTask();
        //nmtTask.execute(ocrApiGwUrl, ocrSecretKey);
//
//
        //todo ocr 샘플 확인용 (OCR API 직접 접근 X) [2]
        String str = getString(R.string.ocr_sample);
        ReturnThreadResult(str); //note JSON 결과값 나누기
//

    }


    public class PapagoNmtTask extends AsyncTask<String, String, String> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public String doInBackground(String... strings) {

            return OcrProc.main(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(String result) {

             ReturnThreadResult(result);
        }
    }

    //ReturnThreadResult
    public void ReturnThreadResult(String rlt) {
        System.out.println("###  Retrun Thread Result");


        String img = rlt;

        try {


            JSONObject jsonObject = new JSONObject(img);
            AddJsonPath path = new AddJsonPath(jsonObject);


            //note 매장명명
            store_name = path.store();

            //note 결제날짜
            year = path.date("year"); //note 년
            month = path.date("month"); //note 월
            day = path.date("day"); //note 일

            //note 항목 그룹 정보

            for (int i = 0; i < path.subResults.length(); i++) {
                JSONArray items = path.subResults.getJSONObject(i).optJSONArray("items");

                for (int j = 0; j < items.length(); j++) {
                    if(items!=null){
                        item_name.add(path.items("item_name", i, j)); //note 항목명
                        item_count.add(path.items("item_count", i, j)); //note 항목 수량
                        if(path.items("item_unit_price", i, j)!=null) {
                            item_unit_price.add(Integer.parseInt(path.items("item_unit_price", i, j).replace(",", ""))); //note 단가
                        }else item_unit_price.add(0);
                        //item_unit_price.add(0);
                        if(path.items("item_price", i, j)!=null)
                            item_price.add(Integer.parseInt(path.items("item_price", i, j).replace(",", ""))); //note 금액
                        else item_price.add(0);

                        item_total_count++;
                    }

                }
            }

            //note 총 금액
            total_price = path.total();

            printLog(path); // note 로그 출력
            printTextView();

            //note ListView
            setListView();


        } catch (Exception e) {
            System.out.println("Error!!");
            e.printStackTrace();
        }
    }

    public void setListView() {

        ListView listView = findViewById(R.id.items);


        for(int i=0; i<item_total_count;i++){

            UserItem userItem = new UserItem(item_name.get(i),store_name,item_count.get(i),item_unit_price.get(i),item_price.get(i));
            list.add(userItem);

        }

        new FirebaseUserHelper().readUserItem(new FirebaseUserHelper.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<UserItem> userItems, ArrayList<String> keys) {
                for (int i = 0; i < list.size(); i++) {
                    for(int j=0; j<userItems.size();j++) {
                        if (list.get(i).getName().equals(userItems.get(j).getName())) {

                            list.get(i).setUnit_amount(userItems.get(j).getUnit_amount());
                            list.get(i).setUnit(userItems.get(j).getUnit());
                            list.get(i).setsCategory(userItems.get(j).getsCategory());
                            list.get(i).setlCategory(userItems.get(j).getlCategory());
                            break;
                        }
                    }
                }

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

        UserItemAdapter adpater = new UserItemAdapter(this, R.layout.content_ocr_list, list);
        listView.setAdapter(adpater);

        setListViewHeightBasedOnChildren(listView);



    }

    //note Log 출력
    public void printLog(AddJsonPath path) throws JSONException {

        System.out.println("#################################### 매장명 : " + store_name);
        System.out.println("#################################### 결제날짜 : " + year + "년" + month + "월" + day + "일");
        StringBuilder item = new StringBuilder();
        StringBuilder count = new StringBuilder();
        StringBuilder unit = new StringBuilder();
        StringBuilder price = new StringBuilder();

        for (int i = 0; i < path.subResults.length(); i++) {
            JSONArray items = path.subResults.getJSONObject(i).getJSONArray("items");
            for (int j = 0; j < items.length(); j++) {
                item.append("\n").append(j).append("번 째 품목 : ").append(item_name.get(j)).append("\n");
                count.append("\n").append(j).append("번 째 품목 : ").append(item_count.get(j)).append("\n");
                unit.append("\n").append(j).append("번 째 품목 : ").append(item_unit_price.get(j)).append("\n");
                price.append("\n").append(j).append("번 째 품목 : ").append(item_price.get(j)).append("\n");
            }
        }
        System.out.println("#################################### 품목 : " + item);
        System.out.println("#################################### 수량 : " + count);
        System.out.println("#################################### 단가 : " + unit);
        System.out.println("#################################### 금액 : " + price);

        System.out.println("#################################### 총금액 : " + total_price);
    }

    //note JSON 결과값 경로 나누기
    public class AddJsonPath {

        JSONObject result;
        JSONArray subResults;
        String path = "";

        public AddJsonPath(JSONObject jsonObject) throws JSONException {
            result = jsonObject.getJSONArray("images").getJSONObject(0).getJSONObject("receipt").getJSONObject("result");

            //note 항목경로
            subResults = result.getJSONArray("subResults");

        }

        //note 매장 명
        public String store() throws JSONException {

            JSONObject storeInfo = result.optJSONObject("storeInfo");

            if(storeInfo!=null){
                if (storeInfo.optJSONObject("name") != null) {
                    return storeInfo.optJSONObject("name").optString("text","noValue");
                }
            }
            return null;

        }

        //note 결제 날짜
        public String date(String value) throws JSONException {

            //note value = year, month, day
            JSONObject paymentInfo = result.optJSONObject("paymentInfo");
            if(paymentInfo!=null){
                if(paymentInfo.optJSONObject("date")!=null){
                    JSONObject date = paymentInfo.getJSONObject("date").getJSONObject("formatted");
                    if(date!=null) return date.optString(value,"noValue");
                }
            }
            return null;

        }

        //note 총 금액
        public String total() throws JSONException {

            JSONObject total = result.getJSONObject("totalPrice");
            if( total != null){
                if(total.getJSONObject("price")!=null)
                    return result.getJSONObject("totalPrice").getJSONObject("price").optString("text","novalue");
            }
            return null;
        }


        //note 항목 상세
        public String items(String value, int i, int j) throws JSONException {
            JSONArray items = subResults.getJSONObject(i).optJSONArray("items");
            JSONObject item = items.optJSONObject(j);
            JSONObject price = item.optJSONObject("price");

            switch (value) {
                case "item_name": //note 항목 이름
                    if(item.optJSONObject("name")!=null){
                        return item.optJSONObject("name").optString("text", "noValue");
                    }
                    break;
                case "item_count": //note 항목 수량
                    if(item.optJSONObject("count")!=null)
                    {
                        return item.optJSONObject("count").optString("text", "noValue");
                    }
                    break;
                case "item_unit_price": //note 단가
                    if(price!=null){
                        if(price.optJSONObject("unitPrice")!=null) {
                            return price.optJSONObject("unitPrice").optString("text", "noValue");
                        }
                    }
                    break;
                case "item_price": //note 금액
                    if(price!=null){
                        if(price.optJSONObject("price")!=null) {
                            return  price.optJSONObject("price").optString("text", "noValue");
                        }
                    }
                    break;
            }
            return null;
        }
    }

    public void printTextView() {

        //note 가게명, 날짜
        TextView store = (TextView) findViewById(R.id.store);
        TextView years = (TextView) findViewById(R.id.year);
        TextView month_day = (TextView) findViewById(R.id.month_day);

        store.setText(store_name);
        years.setText(year + "년 ");
        month_day.setText(month + "월" + " " + day + "일");

    }




}
