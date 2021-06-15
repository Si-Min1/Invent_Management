package com.example.user.ncpaidemo;
//로그인 후 확인 장면 나중에 데이터 베이스에 있는지 없는지 확인하고 있으면 다음 장면으로 스킵

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;


public class LoginActivity extends AppCompatActivity {
    private Button sendInfo;//정보 데이터베이스에 업로드 하는 버튼

    public static String strNick, strProfileImg,strEmail;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    public class UserModel {
        // 사용자 기본정보

        public String strNick; // 사용자 이름(닉네임)
        public String strProfileImg; // 사용자 프로필사진
//    public String pushToken;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //activity_login 레이아웃과 연결
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        strNick = intent.getStringExtra("name");
        strProfileImg = intent.getStringExtra("profileImg");
        strEmail = intent.getStringExtra("email");

        TextView user_NickName = findViewById(R.id.user_Nickname);
        de.hdodenhof.circleimageview.CircleImageView user_Profile = findViewById(R.id.user_Profile);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.wave);
        user_Profile.startAnimation(animation);

        sendInfo = (Button) findViewById(R.id.button_send);
        sendInfo.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 버튼 누르면 수행 할 명령
                /*databaseReference.child("UserInfo").child(strNick).child("식자재목록").child("식자재1").child("수량").setValue(10); //message 이름값 하위에 특정한 규칙을 가진 child를 생성하면서 그 값을 "2"
                databaseReference.child("UserInfo").child(strNick).child("식자재목록").child("식자재1").child("유통기한").setValue("2012-10-12");
                databaseReference.child("UserInfo").child(strNick).child("식자재목록").child("식자재1").child("구매위치").setValue("우리집");
                databaseReference.child("UserInfo").child(strNick).child("식자재목록").child("식자재1").child("가격").setValue(0);
                databaseReference.child("UserInfo").child(strNick).child("식자재목록").child("식자재1").child("이미지명").setValue("https//:@@@");
                databaseReference.child("UserInfo").child(strNick).child("식자재목록").child("식자재2");
                databaseReference.child("UserInfo").child(strNick).child("영수증목록").child("영수증1").setValue("https//:@@@1");
                databaseReference.child("UserInfo").child(strNick).child("영수증목록").child("영수증2").setValue("https//:@@@2");
                databaseReference.child("UserInfo").child(strNick).child("레시피목록").child("레시피1").child("가격").setValue(5000);
                databaseReference.child("UserInfo").child(strNick).child("레시피목록").child("레시피1").child("식자재 이름 받아오기").setValue(5);
                databaseReference.child("UserInfo").child(strNick).child("레시피목록").child("레시피1").child("쌀").setValue(15);
                databaseReference.child("UserInfo").child(strNick).child("수입지출").child("5월달").child("수입").setValue(0);
                databaseReference.child("UserInfo").child(strNick).child("수입지출").child("5월달").child("지출").setValue(100000);*/
                //databaseReference.child("message").child("gbgg").setValue("2"); //message - gbgg 항목의 값을 "2"로 덮어씌운다는 뜻입니다.
                //databaseReference.child("userInfo").child(strNick).setValue(strEmail);
                //databaseReference.child("userInfo").child(strNick).push().setValue("Info2");

                //테스트한다고 레시피로 옮김 메인 엑티비티로 다시 옮길것
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name",strNick);
                startActivity(intent);
                finish();
            }
        });





        //유저 이름
        user_NickName.setText(strNick);
        //프로필 이미지
        Glide.with(this).load(strProfileImg).into(user_Profile);



    }
}