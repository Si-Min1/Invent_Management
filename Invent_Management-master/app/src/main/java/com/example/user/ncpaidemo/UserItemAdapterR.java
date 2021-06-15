package com.example.user.ncpaidemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.ncpaidemo.SelectBaseActivity.baseIntent;
import static com.example.user.ncpaidemo.SelectBaseActivity.lStr;

import com.dinuscxj.progressbar.CircleProgressBar;

public class UserItemAdapterR {


    private Context mContext;
    private UserItemAdapter mUserAdapter;

    //public ArrayList<UserItem> userItemList= new ArrayList<>(); //메인에 보여질 통합 사용자 원재료 리스트

    public void setConfig(RecyclerView recyclerView, Context context, List<UserItem> userItems, List<String> keys) {
        mContext = context;
        mUserAdapter = new UserItemAdapter(userItems, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mUserAdapter);

    }

    class UserItemView extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView store;
        private TextView lCategory;
        private TextView sCategory;
        private TextView nDay;
        private TextView count;
        private TextView unit_price;
        private TextView price;
        private TextView amount;
        private TextView unit;

        private String key;

        public UserItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.content_main_more_list, parent, false));

            name = (TextView) itemView.findViewById(R.id.item_name);
            store = (TextView) itemView.findViewById(R.id.item_store);
            //lCategory = (TextView) itemView.findViewById(R.id.base_lCategory);
            //sCategory = (TextView) itemView.findViewById(R.id.base_sCategory);
            nDay = (TextView) itemView.findViewById(R.id.item_nDay);
            count = (TextView) itemView.findViewById(R.id.item_count);
            //unit_price = (TextView) itemView.findViewById(R.id.item_unit_price);
            price = (TextView) itemView.findViewById(R.id.item_price);
            amount = (TextView) itemView.findViewById(R.id.item_amount);
            //unit = (TextView) itemView.findViewById(R.id.item_unit);


        }

        @SuppressLint("SetTextI18n")
        public void bind(UserItem userItem, String key) {
            //userItem.print();
            //name.setText(userItem.getName());
            name.setText(userItem.getName());
            store.setText(userItem.getStore());
            nDay.setText("D-"+userItem.getnDay());
            count.setText(""+userItem.getCount()+"개");
            price.setText(userItem.getPrice()+"원");
            amount.setText(""+userItem.getAmount()+userItem.getUnit());
            this.key = key;
        }

    }

    class UserItemAdapter extends RecyclerView.Adapter<UserItemView> {
        private List<UserItem> mUserItemList;
        private List<String> mKeys;


        public UserItemAdapter(List<UserItem> mUserItemList, List<String> reKeys) {
            this.mUserItemList = mUserItemList;
            this.mKeys = reKeys;


        }

        @NonNull
        @Override
        public UserItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UserItemView(parent);
        }


        @Override
        public void onBindViewHolder(@NonNull UserItemView holder, int position) {
            holder.bind(mUserItemList.get(position), mKeys.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return mUserItemList.size();
        }
    }
}