package com.example.user.ncpaidemo;

import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.ncpaidemo.MainActivity.setListViewHeightBasedOnChildren;
import static com.example.user.ncpaidemo.SelfInActivity.REQUEST_CODE_MENU;
import static com.example.user.ncpaidemo.UserItemAdapter.isStringDouble;

public class RecipeAddActivity extends AppCompatActivity {

    private final int GET_GALLERY_IMAGE = 200;

    private ArrayList<WideUserItem> list = new ArrayList<>();
    private ArrayList<WideUserItem> wList = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    private Recipe recipe;
    private FirebaseStorage storage;
    private RecipeItem item;
    String recipeName = null;                                   //레시피 이름
    int recipePrice = 0;                                        //레시피 가격
    String recipeImg = null;
    Uri selectedImageUri = null;
    Uri mData;
    static ArrayList<RecipeItem> rList = new ArrayList<>();     //레시피 원재료 리스트

    private LinearLayout first, second;
    ObjectAnimator visibleAnimate;
    ObjectAnimator goneAnimate;
    ObjectAnimator next_button;
    ObjectAnimator ok_button;
    ObjectAnimator boxTrans;
    ObjectAnimator camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //note 타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_recipe_add);

        EditText rName = (EditText) findViewById(R.id.recipe_name);
        EditText rPrice = (EditText) findViewById(R.id.recipe_price);
        first = (LinearLayout) findViewById(R.id.recipe_first);
        second = (LinearLayout) findViewById(R.id.recipe_second);

        storage = FirebaseStorage.getInstance();


        findViewById(R.id.add_wUserItem_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RecipeFinditemActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

        findViewById(R.id.img_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        findViewById(R.id.next_btn).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                visibleAnimate = ObjectAnimator.ofFloat(second, "translationX", -1000f, 0f).setDuration(300);
                goneAnimate = ObjectAnimator.ofFloat(first, "translationX", 1000f, 0f).setDuration(300);
                ok_button = ObjectAnimator.ofFloat(findViewById(R.id.ok_btn), "translationY", 1000f, 0f).setDuration(1000);
                goneAnimate.start();
                first.setVisibility(View.GONE);
                visibleAnimate.start();
                second.setVisibility(View.VISIBLE);
                ok_button.start();


            }
        });


        next_button = ObjectAnimator.ofFloat(findViewById(R.id.next_btn), "translationY", 1000f).setDuration(0);
        ok_button = ObjectAnimator.ofFloat(findViewById(R.id.ok_btn), "translationY", 1000f).setDuration(0);
        boxTrans = ObjectAnimator.ofFloat(findViewById(R.id.recipe_info), "translationY",  -500f).setDuration(0);
        camera = ObjectAnimator.ofFloat(findViewById(R.id.img_add), "translationY",  -500f).setDuration(0);
        ok_button.start();
        next_button.start();
        boxTrans.start();
        camera.start();


        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseRecipeHelper helper = new FirebaseRecipeHelper();

                boolean success = false;

                //레시피 가격
                if (isStringDouble(rPrice.getText().toString())) {
                    recipePrice = Integer.parseInt(rPrice.getText().toString());
                } else recipePrice = -1;

                //레시피 이름
                recipeName = rName.getText().toString();


                //레시피 원재료 리스트
                for (int i = 0; i < rList.size(); i++) {
                    if (rList.get(i).getAmount() <= 0 || rList.get(i).getCount() <= 0 || recipePrice <= 0) {
                        Toast.makeText(RecipeAddActivity.this, "빈칸 혹은 잘못된 값이 입력되었습니다", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    if (i == rList.size() - 1) success = true;
                }

                if (success) {

                    int total_unit_price = 0;
                    //원가 계산
                    for (int i = 0; i < rList.size(); i++) {
                        int total_price = 0;
                        int total_amount = 0;
                        int unit_amount;

                        for (int j = 0; j < wList.size(); j++) {
                            if (rList.get(i).getsCategory().equals(wList.get(j).getsCategory())) {

                                total_price = wList.get(j).getTotal_price();
                                total_amount = wList.get(j).getNowAmount();
                                break;
                            }
                        }
                        unit_amount = (rList.get(i).getAmount() * total_price) / total_amount;
                        System.out.println("########가격 : " + rList.get(i).getAmount() + " || " + total_price + "|| " + total_amount);
                        rList.get(i).setPrice(unit_amount); //아이템당 가격
                        total_unit_price += unit_amount;

                    }

                    recipeImg = "RecipeImg/" + recipeName;
                    recipe = new Recipe(recipeName, recipePrice, total_unit_price, 1, rList, recipeImg);

                    //이미지 스토리지에 업로드
                    StorageReference storageReference = storage.getReferenceFromUrl("gs://inven-deu.appspot.com");
                    StorageReference riversRef = storageReference.child(recipeImg);
                    UploadTask uploadTask = riversRef.putFile(selectedImageUri);

                    try {
                        InputStream in = getContentResolver().openInputStream(mData);
                        //Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RecipeAddActivity.this, "사진에 오류가 있습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });

                    helper.addRecipe(recipe, new FirebaseRecipeHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(ArrayList<Recipe> userItems, List<String> keys) {
                        }

                        @Override
                        public void DataIsInserted() {
                            Toast.makeText(RecipeAddActivity.this, "성공!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void DataIsUpdated() {
                        }

                        @Override
                        public void DataIsDeleted() {
                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);
        System.out.println(resultCode);
        System.out.print(data);
        if (requestCode == REQUEST_CODE_MENU) {
            if (resultCode == RESULT_OK) {
                assert data != null;

                WideUserItem wUserItem = new WideUserItem(data.getStringExtra("sCategory"), data.getStringExtra("unit"),
                        data.getIntExtra("nowAmount", 0), data.getIntExtra("maxCount", 0), data.getIntExtra("total_price", 0));

                wList.add(wUserItem);

                item = new RecipeItem(wUserItem.getsCategory(), wUserItem.getUnit());


                ListView listView = findViewById(R.id.add_wUserItem_list);

                RecipeItemAdapters adapters = new RecipeItemAdapters(RecipeAddActivity.this, R.layout.content_recipe_add_list, item);
                //adapters.data.add(item);
                listView.setAdapter(adapters);


                //list.add((WideUserItem) data.getSerializableExtra("wUserItem"));
                //keys.add( data.getStringExtra("keys"));
                setListViewHeightBasedOnChildren(listView);
                //RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.add_wUserItem_list);
                // new RecipeItemAdapters().setConfig(mRecyclerView, RecipeAddActivity.this, list);
            }
        }

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            mData = data.getData();
            selectedImageUri = Uri.fromFile(new File(getPath(data.getData())));
            ImageView recipe_img = (ImageView) findViewById(R.id.recipe_img);


            boxTrans = ObjectAnimator.ofFloat(findViewById(R.id.recipe_info), "translationY", -500f, 0).setDuration(1000);
            camera = ObjectAnimator.ofFloat(findViewById(R.id.img_add), "translationY", -500f, 0).setDuration(1000);
            next_button = ObjectAnimator.ofFloat(findViewById(R.id.next_btn), "translationY", 1000f, 0f).setDuration(1000);

            //ObjectAnimator imgScale =  ObjectAnimator.ofFloat(findViewById(R.id.recipe_img),"scaleY",2f).setDuration(1000);
            recipe_img.setVisibility(View.VISIBLE);
            AnimatorSet scaleSet = new AnimatorSet();
            scaleSet.playTogether(
                    boxTrans,
                    next_button,
                    camera
            );
            scaleSet.start();

            recipe_img.setImageURI(selectedImageUri);
        }
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == RESULT_OK) {
            }
        }
    }

    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.recipe_second).getVisibility() == View.VISIBLE) {
            visibleAnimate.start();
            first.setVisibility(View.VISIBLE);
            goneAnimate.start();
            second.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }


}
