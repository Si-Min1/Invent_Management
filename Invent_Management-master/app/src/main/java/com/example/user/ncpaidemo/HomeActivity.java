package com.example.user.ncpaidemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.user.ncpaidemo.BaseActivity.inList;
import static com.example.user.ncpaidemo.BaseActivity.push_count;
import static com.example.user.ncpaidemo.BaseActivity.strNick;

public class HomeActivity extends Fragment {

    private View view;
    private ArrayList<UserItem> mUserItems;
    private List<String> mKeys;
    static final String DEFAULT = "DEFAULT";

    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;
    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_home, container, false);
        System.out.println("##### HomeActivity 에서의 inList : " + inList);

        notificationManager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        mCalender = new GregorianCalendar();

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(strNick + " 냉장고");

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.item_list);
        new FirebaseUserHelper().readUserItem(new FirebaseUserHelper.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<UserItem> userItems, ArrayList<String> keys) {
                mUserItems = userItems;
                mKeys=keys;
                new HomeAdapter().setConfig(mRecyclerView, view.getContext(), userItems, keys);

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

        view.findViewById(R.id.input).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AddImageTypeMenu.class);
                startActivityForResult(intent, 1);
            }
        });

        view.findViewById(R.id.setting_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AuthActivity.class);

                NotificationManager notificationManager= (NotificationManager)view.getContext().getSystemService(NOTIFICATION_SERVICE);
                Intent intent1 = new Intent(view.getContext().getApplicationContext(),MainActivity.class); //인텐트 생성.



                Notification.Builder builder = new Notification.Builder(requireActivity().getApplicationContext());
                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);

                PendingIntent pendingNotificationIntent = PendingIntent.getActivity( view.getContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setSmallIcon(R.drawable.popup_round).setTicker("HETT").setWhen(System.currentTimeMillis())
                        .setNumber(1).setContentTitle("푸쉬 제목").setContentText("푸쉬내용")
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingNotificationIntent).setAutoCancel(true).setOngoing(true);

                notificationManager.notify(1, builder.build()); // Notification send
                //startActivity(intent);
            }
        });

        //resetAlarm(view.getContext());
        //setAlarm();
        return view;

    }
    void createNotificationChannel(String channelId, String channelName, int importance)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
        }
    }

    void createNotification(String channelId, int id, String title, String text, Intent intent)
    {
        PendingIntent pendingIntent = PendingIntent.getActivity(view.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(view.getContext(), channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.title)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)    // 클릭시 설정된 PendingIntent가 실행된다
                .setAutoCancel(true)                // true이면 클릭시 알림이 삭제된다
                //.setTimeoutAfter(1000)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    void destroyNotification(int id)
    {
        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }




    public static void resetAlarm(Context context) {
        AlarmManager resetAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent resetIntent = new Intent(context, AlarmRecevier.class);
        PendingIntent resetSender = PendingIntent.getBroadcast(context, 0, resetIntent, 0);
        // 자정 시간
        Calendar resetCal = Calendar.getInstance();
        resetCal.setTimeInMillis(System.currentTimeMillis());
        resetCal.set(Calendar.HOUR_OF_DAY, 07);
        resetCal.set(Calendar.MINUTE,15);
        resetCal.set(Calendar.SECOND, 0);

        //다음날 0시에 맞추기 위해 24시간을 뜻하는 상수인 AlarmManager.INTERVAL_DAY를 더해줌.
        resetAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                resetCal.getTimeInMillis()// + AlarmManager.INTERVAL_DAY
                , AlarmManager.INTERVAL_DAY
                , resetSender);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd kk:mm:ss");
        String setResetTime = format.format(new Date(resetCal.getTimeInMillis() + AlarmManager.INTERVAL_DAY));
        Log.d("resetAlarm", "ResetHour : " + setResetTime);
    }

    private void setAlarm() {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(view.getContext(), AlarmRecevier.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), 0, receiverIntent, 0);

        String from = "06:52:30"; //임의로 날짜와 시간을 지정

        //날짜 포맷을 바꿔주는 소스코드
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date datetime = null;
        try {
            datetime = dateFormat.parse(from);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);

        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);


    }


}
