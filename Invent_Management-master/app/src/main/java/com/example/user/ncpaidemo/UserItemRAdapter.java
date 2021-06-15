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

public class UserItemRAdapter {


    private Context mContext;
    private UserItemAdapter mUserItemAdapter;

    public ArrayList<WideUserItem> wUserItemList = new ArrayList<>(); //메인에 보여질 통합 사용자 원재료 리스트

    public void setConfigMain(RecyclerView recyclerView, Context context, List<UserItem> userItems, List<String> keys) {

        try {

            List<UserItem> mUserItems = userItems;
            ArrayList<ArrayList<UserItem>> sCategoryList = new ArrayList<>();
            ArrayList<String> reKeys = new ArrayList<>();

            ArrayList<UserItem> noCategoryItem = new ArrayList<>();

            for (int k = 0; k < mUserItems.size(); k++) {
                System.out.print(mUserItems.get(k).getName() + " | ");
            }
            System.out.println("\n ==============end============ ");

            ArrayList<Integer> pos = new ArrayList<>();

            for (int i = 0; i < mUserItems.size(); i++) {
                ArrayList<UserItem> sCategoryItem = new ArrayList<>();

                if (mUserItems.get(i).getsCategory() != "") {               //사용자 원재료 내 sCategory 존재 여부 확인
                    String sCategoryName = mUserItems.get(i).getsCategory();
                    for (int j = i; j < mUserItems.size(); j++) {
                        if (sCategoryName.equals(mUserItems.get(j).getsCategory()) && !pos.contains(j)) { //사용자 원재료 내 겹치는 sCategory가 있는 지 확인
                            sCategoryItem.add(mUserItems.get(j));
                            reKeys.add(keys.get(j));
                            pos.add(j);
                        }
                    }
                    //for (int k = 0; k<pos.size(); k++){ System.out.println(pos);}
                } else {
                    noCategoryItem.add(mUserItems.get(i));
                    reKeys.add(keys.get(i));

                }
                if (!sCategoryItem.isEmpty()) {
                    sCategoryList.add(sCategoryItem);
                }

            }
            if(!noCategoryItem.isEmpty())
                sCategoryList.add(noCategoryItem);

            //Log 출력
            for (int i = 0; i < sCategoryList.size(); i++) {
                for (int j = 0; j < sCategoryList.get(i).size(); j++) {
                    System.out.print(sCategoryList.get(i).get(j).getName() + " | ");
                    System.out.println(sCategoryList.get(i).get(j).getsCategory());
                }
                System.out.println("=============================================");
            }


            mUserItems.clear();


            //note 통합 사용자 원재료 리스트
            for (int i = 0; i < sCategoryList.size(); i++) {
                WideUserItem wUserItem = new WideUserItem();
                wUserItem.setsCategory(sCategoryList.get(i).get(0).getsCategory());
                wUserItem.setlCategory(sCategoryList.get(i).get(0).getlCategory());

                if (sCategoryList.get(i).get(0).getsCategory() == "") {
                    wUserItem.setlCategory("기타");
                }

                if(sCategoryList.get(i).get(0).getUnit().equals("kg")) wUserItem.setUnit("g");
                else if(sCategoryList.get(i).get(0).getUnit().equals("L")) wUserItem.setUnit("mL");
                else{wUserItem.setUnit(sCategoryList.get(i).get(0).getUnit());}

                int sumAmount = 0;
                int sumCount = 0;
                int maxAmount = 0;
                int maxNDay = sCategoryList.get(i).get(0).getnDay();

                for (int j = 0; j < sCategoryList.get(i).size(); j++) {
                    UserItem item = sCategoryList.get(i).get(j);

                    int amount = item.getAmount();
                    int unitAmount = item.getUnit_amount();
                    if(item.getUnit().equals("kg") || item.getUnit().equals("L")){
                        amount *=1000;
                        unitAmount*=1000;
                    }
                    sumAmount += amount;
                    maxAmount += (unitAmount*item.getCount());
                    sumCount += item.getCount();
                    maxNDay = Math.max(maxNDay, item.getnDay());
                }

                wUserItem.setMaxCount(sumCount);
                wUserItem.setNowAmount(sumAmount);
                wUserItem.setMaxDay(maxNDay);

                wUserItemList.add(wUserItem);
                wUserItem.print();
                System.out.println("========================================================================================================================");
            }

            mContext = context;
            mUserItemAdapter = new UserItemAdapter(wUserItemList, reKeys);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mUserItemAdapter);

        }
        catch(Exception e) {
            System.out.println(e);
            System.out.println("No Items");
        }

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
        private CircleProgressBar amount;
        private TextView unit;

        private String key;

        public UserItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.content_main_list, parent, false));

            name = (TextView) itemView.findViewById(R.id.item_name);
            //store = (TextView) itemView.findViewById(R.id.item_store);
            //lCategory = (TextView) itemView.findViewById(R.id.base_lCategory);
            //sCategory = (TextView) itemView.findViewById(R.id.base_sCategory);
            nDay = (TextView) itemView.findViewById(R.id.item_nDay);
            //count = (TextView) itemView.findViewById(R.id.item_count);
            //unit_price = (TextView) itemView.findViewById(R.id.item_unit_price);
            //price = (TextView) itemView.findViewById(R.id.item_price);
            amount = (CircleProgressBar) itemView.findViewById(R.id.item_amount);
            //unit = (TextView) itemView.findViewById(R.id.item_unit);


        }

        @SuppressLint("SetTextI18n")
        public void bind(UserItem userItem, String key) {
            //userItem.print();
            //name.setText(userItem.getName());
            name.setText(userItem.getsCategory());
            nDay.setText(""+userItem.getnDay());
            amount.setMax(userItem.getUnit_amount());
            amount.setProgress(userItem.getAmount());
            this.key = key;
        }

        public void bind(WideUserItem wUserItem, String key) {
            //userItem.print();
            //name.setText(userItem.getName());
            name.setText(wUserItem.getsCategory());
            nDay.setText(""+wUserItem.getMaxDay());
            amount.setProgress(wUserItem.getNowAmount());
            this.key = key;
        }
    }

    class UserItemAdapter extends RecyclerView.Adapter<UserItemView> {
        private List<UserItem> mUserItemList;
        private List<WideUserItem> wUserItemList;
        private List<String> mKeys;


        public UserItemAdapter(List<UserItem> mUserItemList, List<String> mKeys) {
            this.mUserItemList = mUserItemList;
            this.mKeys = mKeys;
        }



        public UserItemAdapter(ArrayList<WideUserItem> wUserItemList, ArrayList<String> reKeys) {
            this.wUserItemList = wUserItemList;
            this.mKeys = reKeys;
        }

        @NonNull
        @Override
        public UserItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UserItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull UserItemView holder, int position) {
            holder.bind(wUserItemList.get(position), mKeys.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return wUserItemList.size();
        }
    }
}