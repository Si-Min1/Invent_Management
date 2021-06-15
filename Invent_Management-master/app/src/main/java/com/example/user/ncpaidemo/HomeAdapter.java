package com.example.user.ncpaidemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.user.ncpaidemo.BaseActivity.inList;
import static com.example.user.ncpaidemo.BaseActivity.outList;
import static com.example.user.ncpaidemo.BaseActivity.push_count;
import static com.example.user.ncpaidemo.HomeActivity.DEFAULT;
import static com.example.user.ncpaidemo.R.color.colorAccent;
import static com.example.user.ncpaidemo.R.color.progress1;
import static com.example.user.ncpaidemo.R.color.white;
import static com.example.user.ncpaidemo.SelectBaseActivity.baseIntent;
import static com.example.user.ncpaidemo.SelectBaseActivity.lStr;
import static java.lang.Math.abs;

import com.dinuscxj.progressbar.CircleProgressBar;

public class HomeAdapter{


    private Context mContext;
    private UserItemAdapter mUserItemAdapter;
    private int layout;

    private ArrayList<ArrayList<UserItem>> sCategoryList = new ArrayList<>(); //sCategory 대로 정렬한 사용자 원재료 리스트

    private ArrayList<ArrayList<String>> reKeysList = new ArrayList<>(); //sCategory 대로 정렬한 사용자 원재료 리스트

    private ArrayList<WideUserItem> wUserItemList = new ArrayList<>(); //메인에 보여질 통합 사용자 원재료 리스트

    public ArrayList<WideUserItem> wUserItemsForRecipe = new ArrayList<>(); // 레시피에 들어갈 사용자 원재료 리스트


    public void setConfig(RecyclerView recyclerView, Context context, List<UserItem> userItems, List<String> keys) {

        //try {

        List<UserItem> mUserItems = userItems;
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
                reKeysList.add(reKeys);
            }

        }
        if (!noCategoryItem.isEmpty()) {
            sCategoryList.add(noCategoryItem);
            reKeysList.add(reKeys);
        }


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

            if (sCategoryList.get(i).get(0).getUnit().equals("kg")) wUserItem.setUnit("g");
            else if (sCategoryList.get(i).get(0).getUnit().equals("L")) wUserItem.setUnit("mL");
            else {
                wUserItem.setUnit(sCategoryList.get(i).get(0).getUnit());
            }

            int sumAmount = 0;
            int sumCount = 0;
            int total_price = 0;
            int maxNDay = sCategoryList.get(i).get(0).getnDay();

            for (int j = 0; j < sCategoryList.get(i).size(); j++) {
                UserItem item = sCategoryList.get(i).get(j);

                int amount = item.getAmount();
                int unitAmount = item.getUnit_amount();
                if (item.getUnit().equals("kg") || item.getUnit().equals("L")) {
                    amount *= 1000;
                    unitAmount *= 1000;
                }
                sumAmount += amount;
                sumCount += item.getCount();
                maxNDay = Math.min(maxNDay, item.getnDay());
                total_price += item.getPrice();

            }

            layout = R.layout.content_main_list;

            wUserItem.setMaxCount(sumCount);
            wUserItem.setNowAmount(sumAmount);
            wUserItem.setMaxDay(maxNDay);
            wUserItem.setTotal_price(total_price);

            wUserItemList.add(wUserItem);

        }


        mContext = context;
        mUserItemAdapter = new UserItemAdapter(wUserItemList, reKeys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mUserItemAdapter);

        for(int i=0; i<wUserItemList.size();i++){
            if(wUserItemList.get(i).getNowAmount()<1000 && push_count ==true){
                push_count = false;
                createNotificationChannel (DEFAULT, "기본 채널", NotificationManager.IMPORTANCE_HIGH);
                Intent intent = new Intent(mContext, MainActivity.class);       // 클릭시 실행할 activity를 지정
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                createNotification (DEFAULT, 1, "재고량 부족", "재고량이 1kg 미만입니다",intent);
            }
        }

        /*}
        catch(Exception e) {
            System.out.println(e);
            System.out.println("No Items");
        }*/

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
        //private TextView amount;
        private CircleProgressBar amount;
        private TextView unit;
        private com.airbnb.lottie.LottieAnimationView upgrade;
        private com.airbnb.lottie.LottieAnimationView downgrade;

        private String key;

        //        @SuppressLint("NonConstantResourceId")
        public UserItemView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(layout, parent, false));

            name = (TextView) itemView.findViewById(R.id.item_name);
            nDay = (TextView) itemView.findViewById(R.id.item_nDay);
            count = (TextView) itemView.findViewById(R.id.item_count);
            amount = (CircleProgressBar) itemView.findViewById(R.id.item_amount);

            upgrade = (com.airbnb.lottie.LottieAnimationView)itemView.findViewById(R.id.upgrade);
            downgrade = (com.airbnb.lottie.LottieAnimationView)itemView.findViewById(R.id.downgrade);


            //name = (TextView) itemView.findViewById(R.id.item_name);
            //store = (TextView) itemView.findViewById(R.id.item_store);
            //lCategory = (TextView) itemView.findViewById(R.id.base_lCategory);
            //sCategory = (TextView) itemView.findViewById(R.id.base_sCategory);
            //nDay = (TextView) itemView.findViewById(R.id.item_nDay);
            //count = (TextView) itemView.findViewById(R.id.item_count);
            //unit_price = (TextView) itemView.findViewById(R.id.item_unit_price);
            //price = (TextView) itemView.findViewById(R.id.item_price);
            //amount = (CircleProgressBar) itemView.findViewById(R.id.item_amount);
            //amount = (TextView) itemView.findViewById(R.id.item_amount);
            //unit = (TextView) itemView.findViewById(R.id.item_unit);
            //unit =(TextView) itemView.findViewById(R.id.item_unit);


        }

        @SuppressLint("SetTextI18n")
        public void bind(WideUserItem wUserItem) {


            name.setText(wUserItem.getsCategory());
            if(wUserItem.getMaxDay()<0){
                nDay.setTextColor(itemView.getResources().getColor(R.color.colorAccent));
                nDay.setText("D+"+abs(wUserItem.getMaxDay()));
            }
            else nDay.setText("D-" + wUserItem.getMaxDay());

            String unitStr = null;
            int amountUnity = wUserItem.getNowAmount();


            amount.setMax(20000);

            //if(amountUnity>=1000) amount.setProgress(amountUnity/1000);
            amount.setProgress(amountUnity);

            if (wUserItem.getUnit().equals("g")) {
                if (amountUnity >= 1000) unitStr = "kg";
                else unitStr = "g";
            } else if (wUserItem.getUnit().equals("mL")) {
                if (amountUnity >= 1000) unitStr = "L";
                else unitStr = "mL";
            }
            amount.setProgressFormatter(new MyProgressFormatter(unitStr));
            if(inList!=null) {
                for (int i = 0; i < inList.size(); i++) {
                    if (inList.get(i).getsCategory().equals(wUserItem.getsCategory())) {
                        upgrade.setVisibility(View.VISIBLE);
                        downgrade.setVisibility(View.GONE);
                    }
                }
            }

            if(outList!=null){
                for (int i = 0; i < outList.size(); i++) {
                    if (outList.get(i).getsCategory().equals(wUserItem.getsCategory())) {
                        upgrade.setVisibility(View.GONE);
                        downgrade.setVisibility(View.VISIBLE);
                    }
                }
            }

        }
    }

    class UserItemAdapter extends RecyclerView.Adapter<UserItemView> {
        //private List<UserItem> mUserItemList;
        private List<WideUserItem> wUserItemList;
        private List<String> mKeys;


        public UserItemAdapter(ArrayList<WideUserItem> wUserItemList, ArrayList<String> reKeys) {
            this.wUserItemList = wUserItemList;
            this.mKeys = reKeys;
        }

        public UserItemAdapter(ArrayList<WideUserItem> wUserItemList) {
            this.wUserItemList = wUserItemList;
        }

        @NonNull
        @Override
        public UserItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new UserItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull UserItemView holder, int position) {
            holder.bind(wUserItemList.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(mContext, MainMoreActivity.class);
                    intent.putExtra("keys", reKeysList.get(position));
                    intent.putExtra("userItems", sCategoryList.get(position));
                    intent.putExtra("lCategory", wUserItemList.get(position).getlCategory());
                    intent.putExtra("sCategory", wUserItemList.get(position).getsCategory());
                    intent.putExtra("maxDay", wUserItemList.get(position).getMaxDay());
                    intent.putExtra("nowAmount", wUserItemList.get(position).getNowAmount());
                    intent.putExtra("maxCount", wUserItemList.get(position).getMaxCount());
                    intent.putExtra("unit", wUserItemList.get(position).getUnit());
                    mContext.startActivity(intent);


                }


            });
        }

        @Override
        public int getItemCount() {
            return wUserItemList.size();
        }
    }

    private static final class MyProgressFormatter implements CircleProgressBar.ProgressFormatter {
        private static String DEFAULT_PATTERN;
        private String unit;
        private int num = 1;

        @SuppressLint("DefaultLocale")
        @Override
        public CharSequence format(int progress, int max) {
            DEFAULT_PATTERN = "%d" + unit;

            if (unit.equals("kg") || unit.equals("L")) {
                num = 1000;
            }
            return String.format(DEFAULT_PATTERN, progress / num);
        }

        public MyProgressFormatter(String unit) {
            this.unit = unit;
        }
    }



    void createNotificationChannel(String channelId, String channelName, int importance)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
        }
    }

    void createNotification(String channelId, int id, String title, String text, Intent intent)
    {
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.title)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)    // 클릭시 설정된 PendingIntent가 실행된다
                .setAutoCancel(true)                // true이면 클릭시 알림이 삭제된다
                //.setTimeoutAfter(1000)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    void destroyNotification(int id)
    {
        NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}