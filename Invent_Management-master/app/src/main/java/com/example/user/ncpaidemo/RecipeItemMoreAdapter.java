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
import static com.example.user.ncpaidemo.UserItemAdapter.editTextList;
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

public class RecipeItemMoreAdapter extends BaseAdapter {


    private int layout;
    private LayoutInflater inflater;
    public ArrayList<RecipeItem> data = new ArrayList<>(); //Item 목록을 담을 배열


    public RecipeItemMoreAdapter(Context context, int layout, ArrayList<RecipeItem> data) {
        this.data = data;
        this.layout=layout;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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



        if (convertView == null) {


            convertView = inflater.inflate(layout, parent, false);


        }

        RecipeItem userItem = data.get(position);

        TextView sCategory = (TextView) convertView.findViewById(R.id.item_sCategory);
        TextView count = (TextView) convertView.findViewById(R.id.item_count);
        TextView amount = (TextView) convertView.findViewById(R.id.item_amount);
        TextView unit_price = (TextView) convertView.findViewById(R.id.item_unit_price);

        count.setText("" + userItem.getCount());
        sCategory.setText(userItem.getsCategory());
        amount.setText(""+userItem.getAmount()+userItem.getUnit());
        unit_price.setText(""+userItem.getPrice()+"원");



        return convertView;
    }

}

