package com.example.user.ncpaidemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
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

public class OcrOutAdapter extends BaseAdapter {


    private int layout;
    private Context context;
    private LayoutInflater inflater;
    public ArrayList<Recipe> data = new ArrayList<>(); //조회되는/안되는 레시피 리스트

    private ArrayList<Recipe> filteredItemList;

    public OcrOutAdapter(Context context, int layout, ArrayList<Recipe> data){
        this.context = context;
        this.data = data;
        this.layout=layout;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @SuppressLint({"SetTextI18n", "CutPasteId", "WrongViewCast", "ResourceAsColor"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolder.RecipeViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder.RecipeViewHolder();
            convertView = inflater.inflate(layout, parent, false);
            holder.name = (EditText) convertView.findViewById(R.id.recipe_name);
            holder.count = (EditText) convertView.findViewById(R.id.recipe_count);
            holder.price = (EditText) convertView.findViewById(R.id.recipe_price);
            convertView.setTag(holder);


        } else {
            holder = (ViewHolder.RecipeViewHolder) convertView.getTag();
        }
        holder.ref = position;


        Recipe userItem = data.get(position);

        EditText name =(EditText) convertView.findViewById(R.id.recipe_name);
        EditText count = (EditText) convertView.findViewById(R.id.recipe_count);
        EditText price = (EditText) convertView.findViewById(R.id.recipe_price);

        ImageButton delete = (ImageButton) convertView.findViewById(R.id.delete);


        holder.name.setText(userItem.getName());
        holder.count.setText("" + userItem.getCount());
        holder.price.setText(""+userItem.getPrice());

        int color = convertView.getResources().getColor(R.color.colorAccent);
        if(userItem.getUserItems()==null)
            holder.name.setTextColor(color);

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

        holder.name.addTextChangedListener(new TextWatcher() {
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
                filteredItemList.get(holder.ref).setName(s.toString());
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


        holder.price.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int c) {
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

        return convertView;
    }

}

