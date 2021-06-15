package com.example.user.ncpaidemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.ncpaidemo.SelectBaseActivity.lStr;

public class SelfInAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    public ArrayList<UserItem> data; //Item 목록을 담을 배열

    private ArrayList<UserItem> filteredItemList;

    static ArrayList<UserItem> editTextList; //수정한 EditText 배열
    static String unitStr[] = {"g", "kg", "mL", "L"};
    private int layout;

    public SelfInAdapter(Context context, int layout, ArrayList<UserItem> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data = data;
        this.layout = layout;
        this.filteredItemList = data;
    }

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

    @SuppressLint({"SetTextI18n", "CutPasteId", "WrongViewCast"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolder.SelfUserItemHolder holder;

        if (layout == R.layout.content_self_input_list) {
            if (convertView == null) {
                holder = new ViewHolder.SelfUserItemHolder();
                convertView = inflater.inflate(layout, parent, false);
                holder.name = (EditText) convertView.findViewById(R.id.item_name);
                holder.category = (TextView) convertView.findViewById(R.id.item_category);
                holder.nDay = (EditText) convertView.findViewById(R.id.item_nDay);
                holder.unit_amount = (EditText) convertView.findViewById(R.id.item_unit_amount);
                holder.count = (EditText) convertView.findViewById(R.id.item_count);
                holder.price = (EditText) convertView.findViewById(R.id.item_price);
                holder.unit = (Spinner) convertView.findViewById(R.id.spinner);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder.SelfUserItemHolder) convertView.getTag();
            }
            holder.ref = position;
        } else {
            if (convertView == null) {
                convertView = inflater.inflate(layout, parent, false);

            }
            holder = null;
        }


        UserItem userItem = data.get(position);


        //note 추가할 사용자 원재료 리스트

        //note 추가된 사용자 원재료 리스트
        EditText name = (EditText) convertView.findViewById(R.id.item_name);
        TextView category = (TextView) convertView.findViewById(R.id.item_category);
        EditText nDay = (EditText) convertView.findViewById(R.id.item_nDay);
        EditText unit_amount = (EditText) convertView.findViewById(R.id.item_unit_amount);
        EditText price = (EditText) convertView.findViewById(R.id.item_price);
        EditText count = (EditText) convertView.findViewById(R.id.item_count);
        Spinner unit = (Spinner) convertView.findViewById(R.id.spinner);

        holder.name.setText(userItem.getName());
        holder.category.setText(userItem.getlCategory() + "/" + userItem.getsCategory());
        holder.nDay.setText("" + userItem.getnDay());
        holder.unit_amount.setText("" + userItem.getUnit_amount());
        holder.price.setText("" + userItem.getPrice());
        holder.unit.setSelection(getStrPosition(userItem.getUnit(), unitStr));
        holder.count.setText("" + userItem.getCount());

        convertView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                data.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                String str = (String) unit.getSelectedItem();
                filteredItemList.get(holder.ref).setUnit(str);
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
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isStringDouble(price.getText().toString())) {
                    filteredItemList.get(holder.ref).setUnit_price(-1);
                } else {
                    int str = Integer.parseInt(s.toString());
                    filteredItemList.get(holder.ref).setPrice(str);
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
        if (str == null || str.contains("▼")) {
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
