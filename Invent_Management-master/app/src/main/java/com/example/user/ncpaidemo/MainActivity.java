package com.example.user.ncpaidemo;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends BaseActivity {
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ChartActivity frag1;
    private HomeActivity frag2;
    private RecipeActivity frag3;


    private static final String BROADCAST_MESSAGE = "com.example.limky.broadcastreceiver.gogo";
    private BroadcastReceiver mReceiver = null;


    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";

    NotificationManager manager;
    NotificationCompat.Builder builder;
    private ArrayList<UserItem> mList;
    private ArrayList<String> mKeys;

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);
        resetAlarm(MainActivity.this);
        /* ***************************************************************************************************************** */

        //setAlarm();


        //프래그
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_chart:
                        setFrag(0);
                        break;
                    case R.id.action_home:
                        setFrag(1);
                        break;
                    case R.id.action_menu:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        System.out.println("::::::::::::::::::::::: strNick ::::::::::::::::::::: " + strNick);

        frag1 = new ChartActivity();
        frag2 = new HomeActivity();
        frag3 = new RecipeActivity();

        Bundle bundle1 = new Bundle();
        bundle1.putString("strNick", strNick);

        Bundle bundle2 = new Bundle();
        bundle2.putString("strNick", strNick);

        Bundle bundle3 = new Bundle();
        bundle3.putString("strNick", strNick);


        frag1.setArguments(bundle1);
        frag2.setArguments(bundle2);
        frag3.setArguments(bundle3);

        int frag = getIntent().getIntExtra("frag", 0);
        if (frag == 0) {
            setFrag(1);
        } else setFrag(frag);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getAppHashKey(); //해쉬키 얻기
    }

    private void getAppHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("HashKey", something);
            }
        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }
    }

    // 프레그먼트 교체
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.Main_Frame, frag1);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.Main_Frame, frag2);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.Main_Frame, frag3);
                ft.commit();
                break;
        }
    }

    public void resetAlarm(Context context) {

        Intent resetIntent = new Intent(BROADCAST_MESSAGE);
        new FirebaseUserHelper().readUserItem(new FirebaseUserHelper.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<UserItem> userItems, ArrayList<String> keys) {

                for (int i = 0; i < userItems.size(); i++) {
                    userItems.get(i).setnDay(userItems.get(i).getnDay() - 1);
                }
                //resetIntent.putExtra("list", userItems);
                //resetIntent.putStringArrayListExtra("keys", keys);
                mList = userItems;
                mKeys = keys;

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

        AlarmManager resetAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent resetSender = PendingIntent.getBroadcast(context, 0, resetIntent, 0);

        // 자정 시간
        Calendar resetCal = Calendar.getInstance();
        resetCal.setTimeInMillis(System.currentTimeMillis());
        resetCal.set(Calendar.HOUR_OF_DAY, 10);
        resetCal.set(Calendar.MINUTE, 16);
        resetCal.set(Calendar.SECOND, 0);

        //다음날 0시에 맞추기 위해 24시간을 뜻하는 상수인 AlarmManager.INTERVAL_DAY를 더해줌.
        resetAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                resetCal.getTimeInMillis() //+ AlarmManager.INTERVAL_DAY
                , AlarmManager.INTERVAL_DAY
                , resetSender);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd kk:mm:ss");
        String setResetTime = format.format(new Date(resetCal.getTimeInMillis() + AlarmManager.INTERVAL_DAY));
        Log.d("resetAlarm", "ResetHour : " + setResetTime);
    }

    private void registerReceiver() {
        /** 1. intent filter를 만든다
         *  2. intent filter에 action을 추가한다.
         *  3. BroadCastReceiver를 익명클래스로 구현한다.
         *  4. intent filter와 BroadCastReceiver를 등록한다.
         * */
        if (mReceiver != null) return;
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(BROADCAST_MESSAGE);


        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //ArrayList<UserItem> list = (ArrayList<UserItem>) intent.getSerializableExtra("list");
                //ArrayList<String> keys = intent.getStringArrayListExtra("keys");

                for (int i = 0; i < mList.size(); i++) {

                    new FirebaseUserHelper().updateUserItem(mKeys.get(i), mList.get(i), new FirebaseUserHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(ArrayList<UserItem> userItems, ArrayList<String> keys) {
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

                }


                AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                builder = null;
                manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    manager.createNotificationChannel(
                            new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                    );
                    builder = new NotificationCompat.Builder(context, CHANNEL_ID);
                } else {
                    builder = new NotificationCompat.Builder(context);
                }

                //알림창 클릭 시 activity 화면 부름
                Intent intent2 = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 101, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

                //알림창 제목
                builder.setContentTitle("유통기한이 변경되었습니다");
                //알림창 아이콘
                builder.setSmallIcon(R.drawable.title);
                //알림창 터치시 자동 삭제
                builder.setAutoCancel(true);

                builder.setContentIntent(pendingIntent);

                Notification notification = builder.build();
                manager.notify(1, notification);
            }
        };

        this.registerReceiver(this.mReceiver, theFilter);

    }

    /**
     * 동적으로(코드상으로) 브로드 캐스트를 종료한다.
     **/
    private void unregisterReceiver() {
        if (mReceiver != null) {
            this.unregisterReceiver(mReceiver);
            mReceiver = null;
        }

    }


    //버튼 팝업 액티비티


    /*private void setAlarm() {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(MainActivity.this, AlarmRecevier.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, receiverIntent, 0);

        String from = "2021-06-07 06:07:30"; //임의로 날짜와 시간을 지정

        //날짜 포맷을 바꿔주는 소스코드
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(from);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);


    }*/


    //note 리스트뷰 자동 높이 조절
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            //listItem.measure(0, 0);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight;
        listView.setLayoutParams(params);

        listView.requestLayout();
    }
}

