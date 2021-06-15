package com.example.user.ncpaidemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinuscxj.progressbar.CircleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.example.user.ncpaidemo.RecipeAddActivity.rList;
import static com.example.user.ncpaidemo.SelectBaseActivity.lStr;
import static com.example.user.ncpaidemo.UserItemAdapter.isStringDouble;

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

public class RecipeItemAdapters extends BaseAdapter {


    private int layout;
    private LayoutInflater inflater;
    public ArrayList<RecipeItem> data = new ArrayList<>(); //Item 목록을 담을 배열
    public ArrayList<WideUserItem> wData;
    private ArrayList<RecipeItem> filteredItemList;

    public RecipeItemAdapters(Context context, int layout, RecipeItem data) {
        rList.add(data);
        this.data = rList;
        this.layout=layout;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.filteredItemList = this.data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getsCategory();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "CutPasteId", "WrongViewCast"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolder.ViewHolderW holder;

        if (convertView == null) {

            holder = new ViewHolder.ViewHolderW();
            convertView = inflater.inflate(layout, parent, false);
            holder.sCategory = (TextView) convertView.findViewById(R.id.item_name);
            holder.count = (EditText) convertView.findViewById(R.id.item_count);
            holder.amount = (EditText) convertView.findViewById(R.id.item_amount);
            holder.unit =(TextView) convertView.findViewById(R.id.item_unit) ;
            convertView.setTag(holder);


        } else {
            holder = (ViewHolder.ViewHolderW) convertView.getTag();
        }
        holder.ref = position;


        RecipeItem userItem = data.get(position);


        EditText count = (EditText) convertView.findViewById(R.id.item_count);
        EditText amount = (EditText) convertView.findViewById(R.id.item_amount);
        TextView unit = (TextView) convertView.findViewById(R.id.item_unit);
        TextView sCategory = (TextView) convertView.findViewById(R.id.item_sCategory);
        ImageButton delete = (ImageButton) convertView.findViewById(R.id.remove);


        holder.unit.setText(userItem.getUnit());
        holder.count.setText("" + userItem.getCount());
        holder.sCategory.setText(userItem.getsCategory());
        holder.amount.setText(""+userItem.getAmount());



        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("######  삭제된 리스트  : " + position + " | " + data.get(position).getsCategory());
                System.out.print("######  이전 리스트  : ");
                for (int i = 0; i < data.size(); i++) {
                    System.out.print(data.get(i).getsCategory() + " | ");
                }
                System.out.println("\n");
                data.remove(userItem);
                System.out.print("######  이후 리스트  : ");
                for (int i = 0; i < data.size(); i++) {
                    System.out.print(data.get(i).getsCategory() + " | ");
                }
                System.out.println("\n");
                notifyDataSetChanged();
            }
        });


        holder.count.addTextChangedListener(new TextWatcher() {
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
                if (!isStringDouble(count.getText().toString())) {
                    filteredItemList.get(holder.ref).setCount(-1);
                } else {
                    int str = Integer.parseInt(s.toString());
                    filteredItemList.get(holder.ref).setCount(str);
                    //userItem.print(":::: count :::::");
                }
            }
        });


        holder.amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int c) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!isStringDouble(amount.getText().toString())) {
                    filteredItemList.get(holder.ref).setAmount(-1);
                } else {
                    int str = Integer.parseInt(s.toString());
                    filteredItemList.get(holder.ref).setAmount(str);
                    //userItem.print(":::: count :::::");
                }
            }
        });

        return convertView;
}

}

