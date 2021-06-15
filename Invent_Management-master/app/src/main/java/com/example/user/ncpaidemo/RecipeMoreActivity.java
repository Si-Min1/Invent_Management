package com.example.user.ncpaidemo;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.user.ncpaidemo.MainActivity.setListViewHeightBasedOnChildren;

public class RecipeMoreActivity extends AppCompatActivity {

    private Recipe list = new Recipe(); // 아이템 리스트
    //private ArrayList<String> keys = new ArrayList<>(); //키 리스트

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //note 타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.content_recipe_more);

        Intent intent = getIntent();

        list = (Recipe)intent.getSerializableExtra("recipeList");
        ArrayList<RecipeItem> rlist = list.getUserItems();

        TextView name = findViewById(R.id.recipe_name);
        TextView price = findViewById(R.id.recipe_price);
        TextView unit_price = findViewById(R.id.recipe_unit_price);
        //CircleImageView img = findViewById(R.id.recipe_img);
        ImageView img = findViewById(R.id.recipe_img);
        LinearLayout info = findViewById(R.id.recipe_info);

        img.setClipToOutline(true);
        img.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);

        //ObjectAnimator imgAnimX = ObjectAnimator.ofFloat(img, "scaleX", 2f).setDuration(0);
       // ObjectAnimator imgAnimY = ObjectAnimator.ofFloat(img, "scaleY", 2f).setDuration(0);
        ObjectAnimator imgAnimT = ObjectAnimator.ofFloat(img, "translationY", -150f,-350f).setDuration(500);
        ObjectAnimator infoAnimT = ObjectAnimator.ofFloat(info, "translationY", -50f,0f).setDuration(1000);
        //imgAnimX.start();
        // imgAnimY.start();
         imgAnimT.start(); infoAnimT.start();

        if (list.getImg() != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://inven-deu.appspot.com");
            StorageReference pathReference = storageReference.child("RecipeImg");
            if (pathReference == null) {
                Toast.makeText(RecipeMoreActivity.this, "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
            } else {
                StorageReference submitProfile = storageReference.child(list.getImg());
                submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(RecipeMoreActivity.this).load(uri).into(img);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        }


        name.setText(list.getName());
        price.setText(""+list.getPrice()+"원");
        unit_price.setText(""+list.getTotal_unit_price()+"원");



        ListView listView = findViewById(R.id.recipe_list);

        RecipeItemMoreAdapter adapters = new RecipeItemMoreAdapter(RecipeMoreActivity.this, R.layout.content_recipe_more_list, rlist);
        //adapters.data.add(item);
        listView.setAdapter(adapters);
        setListViewHeightBasedOnChildren(listView);
    }
}
