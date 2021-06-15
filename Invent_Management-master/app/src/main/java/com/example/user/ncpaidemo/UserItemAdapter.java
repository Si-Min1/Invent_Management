package com.example.user.ncpaidemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.example.user.ncpaidemo.SelectBaseActivity.lStr;

public class UserItemAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    public ArrayList<UserItem> data; //Item 목록을 담을 배열

    private ArrayList<UserItem> filteredItemList;

    static ArrayList<UserItem> editTextList; //수정한 EditText 배열
    static String unitStr[] = {"g", "kg", "mL", "L"};
    private int layout;

    private int pos = -1;

    public UserItemAdapter(Context context, int layout, ArrayList<UserItem> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
        this.filteredItemList=data;

    }
    public void setPosition(int position) {pos = position;}

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"SetTextI18n", "CutPasteId", "WrongViewCast"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolder.WideUserItemHolder holder;

        if (layout == R.layout.content_ocr_in_list) {
            if (convertView == null) {
                holder = new ViewHolder.WideUserItemHolder();
                convertView = inflater.inflate(layout, parent, false);
                holder.name = (EditText) convertView.findViewById(R.id.item_name);
                holder.count = (EditText) convertView.findViewById(R.id.item_count);
                holder.price = (EditText) convertView.findViewById(R.id.item_price);
                holder.unit_amount = (EditText) convertView.findViewById(R.id.item_unit_amount);
                holder.nDay = (EditText) convertView.findViewById(R.id.item_nDay);
                holder.unit = (Spinner) convertView.findViewById(R.id.spinner);
                holder.lCategory = (Spinner) convertView.findViewById(R.id.item_category);
                holder.sCategory = (EditText) convertView.findViewById(R.id.item_sCategory);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder.WideUserItemHolder) convertView.getTag();
            }
            holder.ref = position;

        }

        else {
            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);

            }
            holder = null;
        }


        UserItem userItem = data.get(position);

        //note 추가할 사용자 원재료 리스트

        if (layout == R.layout.content_select_in_list) {

            //이름
            TextView name = (TextView) convertView.findViewById(R.id.item_name);
            name.setText(userItem.getName());

            //대분류+ 소분류
            TextView category = (TextView) convertView.findViewById(R.id.item_category);
            category.setText(userItem.getlCategory() + " / " + userItem.getsCategory());
            if(pos!=-1){
                if(pos==position) {
                    com.airbnb.lottie.LottieAnimationView select = (com.airbnb.lottie.LottieAnimationView) convertView.findViewById(R.id.item_select);
                    select.setVisibility(View.VISIBLE);
                }
            }
            else {
                com.airbnb.lottie.LottieAnimationView select = (com.airbnb.lottie.LottieAnimationView) convertView.findViewById(R.id.item_select);
                select.setVisibility(View.GONE);
            }
        }

        //note 영수증 인식 결과 리스트
        if (layout == R.layout.content_ocr_list) {

            TextView name = (TextView) convertView.findViewById(R.id.item_name);
            TextView count = (TextView) convertView.findViewById(R.id.item_count);
            TextView unit_price = (TextView) convertView.findViewById(R.id.item_unit_price);
            TextView price = (TextView) convertView.findViewById(R.id.item_price);

            name.setText(userItem.getName());
            count.setText("" + userItem.getCount());
            price.setText(""+userItem.getPrice());
            unit_price.setText(""+userItem.getUnit_price());

        }

        //note 영수증 상세 입력 리스트
        if (layout == R.layout.content_ocr_in_list) {

            EditText name = (EditText) convertView.findViewById(R.id.item_name);
            EditText count = (EditText) convertView.findViewById(R.id.item_count);
            EditText price = (EditText) convertView.findViewById(R.id.item_price);
            EditText unit_amount = (EditText) convertView.findViewById(R.id.item_unit_amount);
            EditText sCategory = (EditText) convertView.findViewById(R.id.item_sCategory);
            EditText nDay = (EditText) convertView.findViewById(R.id.item_nDay);
            ImageButton delete = (ImageButton) convertView.findViewById(R.id.delete);
            Spinner unit = (Spinner) convertView.findViewById(R.id.spinner);
            Spinner category = (Spinner) convertView.findViewById(R.id.item_category);

            if(userItem.getnDay()==0) holder.nDay.setHint("유통기한");
            else holder.nDay.setText("" + userItem.getnDay());
            if(userItem.getUnit_amount()==0)holder.unit_amount.setHint("상세수량");
            else holder.unit_amount.setText("" + userItem.getUnit_amount());

            holder.name.setText(userItem.getName());
            holder.count.setText("" + userItem.getCount());
            holder.price.setText(""+userItem.getPrice());
            holder.sCategory.setText(userItem.getsCategory());
            holder.unit.setSelection(getStrPosition(userItem.getUnit(), unitStr));
            holder.lCategory.setSelection(getStrPosition(userItem.getlCategory(), lStr));

            //name.setText(userItem.getName());
            //count.setText("" + userItem.getCount());
            //unit_price.setText(userItem.getUnit_price());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("######  삭제된 리스트  : " + position + " | " + data.get(position).getName());
                    System.out.print("######  이전 리스트  : ");
                    for (int i = 0; i < data.size(); i++) {
                        System.out.print(data.get(i).getName() + " | ");
                    }
                    System.out.println("\n");
                    data.remove(userItem);
                    System.out.print("######  이후 리스트  : ");
                    for (int i = 0; i < data.size(); i++) {
                        System.out.print(data.get(i).getName() + " | ");
                    }
                    System.out.println("\n");
                    notifyDataSetChanged();
                }
            });


            //note Spinner

            holder.lCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                    String str = (String) holder.lCategory.getSelectedItem();

                    AssetManager assetManager = arg1.getResources().getAssets();
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

                        JSONArray jsonArray = new JSONObject(jsonData).getJSONObject("BaseInfo").getJSONObject("lCategory").getJSONArray(str);
                        ArrayList<String> list = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            list.add(jsonArray.getJSONObject(i).getString("sCategory"));

                        }
                        int max = 0;
                        int count = 0;
                        int index = -1;
                        for (int i = 0; i < list.size(); i++) {
                            if (userItem.getName().contains(list.get(i))) {
                                if (count == 0) {
                                    max = list.get(i).length();
                                    index = i;
                                    count++;
                                } else if (max < list.get(i).length()) {
                                    max = list.get(i).length();
                                    index = i;
                                }

                            }
                        }
                        if (index == -1) {
                            //filteredItemList.get(holder.ref).setnDay(-1);
                            //userItem.setnDay(-1);
                            //holder.nDay.setHint("유통기한");
                        } else {
                            filteredItemList.get(holder.ref).setsCategory(jsonArray.getJSONObject(index).getString("sCategory"));
                            filteredItemList.get(holder.ref).setnDay(jsonArray.getJSONObject(index).getInt("nDay"));
                            //userItem.setsCategory(jsonArray.getJSONObject(index).getString("sCategory"));
                            //userItem.setnDay(jsonArray.getJSONObject(index).getInt("nDay"));
                            //sCategory.setFocusable(false);
                            //sCategory.setClickable(false);
                            //holder.sCategory.setText(filteredItemList.get(holder.ref).getsCategory());
                            //holder.nDay.setText("" + filteredItemList.get(holder.ref).getnDay());

                        }
                        filteredItemList.get(holder.ref).setlCategory(str);

                        //userItem.setlCategory(str);

                        notifyDataSetChanged();

                        holder.sCategory.requestFocus();


                    } catch (IOException | JSONException e) {
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            holder.unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                    String str = (String) unit.getSelectedItem();

                    filteredItemList.get(holder.ref).setUnit(str);
                    //userItem.setUnit(str);
                    holder.unit_amount.requestFocus();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


            //note Edit뷰!

            holder.name.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    filteredItemList.get(holder.ref).setName(s.toString());
                }
            });


            holder.price.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //String str = unit_price.getText().toString();
                    //userItem.setUnit_price(str);
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!isStringDouble(price.getText().toString())) {
                        filteredItemList.get(holder.ref).setPrice(-1);
                    } else {
                        int str = Integer.parseInt(s.toString());
                        filteredItemList.get(holder.ref).setPrice(str);
                        //userItem.print(":::: count :::::");
                    }
                }
            });


            holder.count.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int c) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (!isStringDouble(count.getText().toString())) {
                        filteredItemList.get(holder.ref).setCount(-1);
                    } else {
                        int str = Integer.parseInt(s.toString());
                        filteredItemList.get(holder.ref).setCount(str);
                        //userItem.print(":::: count :::::");
                    }
                }
            });


            holder.nDay.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!isStringDouble(s.toString())) {
                        filteredItemList.get(holder.ref).setnDay(-1);
                    } else {
                        int str = Integer.parseInt(s.toString());
                        filteredItemList.get(holder.ref).setnDay(str);
                    }
                }
            });


            holder.unit_amount.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (!isStringDouble(s.toString())) {
                        filteredItemList.get(holder.ref).setUnit_amount(-1);
                    } else {
                        int str = Integer.parseInt(s.toString());
                        filteredItemList.get(holder.ref).setUnit_amount(str);
                    }
                }
            });
            if (sCategory.isClickable()) {
                holder.sCategory.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        filteredItemList.get(holder.ref).setsCategory(s.toString());
                    }
                });
            }
        }



        return convertView;
    }

    //문자열이 숫자인지 판별하는 함수
    public static boolean isStringDouble(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int getStrPosition(String str, String[] strArray) {
        int pos = 0;
        if (str==null||str.contains("▼")) {
            return pos;
        }
        for (int i = 0; i < strArray.length; i++) {
            if (strArray[i].equals(str)) {
                pos = i + 1;
            }
        }
        return pos;
    }


    }
