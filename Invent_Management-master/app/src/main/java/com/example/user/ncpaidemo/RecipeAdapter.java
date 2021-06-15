package com.example.user.ncpaidemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.user.ncpaidemo.SelectBaseActivity.baseIntent;
import static com.example.user.ncpaidemo.SelectBaseActivity.lStr;

import com.bumptech.glide.Glide;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecipeAdapter {


    private Context mContext;
    private RecipeAdapters mRecipeAdapter;

    //public ArrayList<UserItem> userItemList= new ArrayList<>(); //메인에 보여질 통합 사용자 원재료 리스트
    //todo 레시피탭에서 조회 및 상세보기 위함
    public void setConfig(RecyclerView recyclerView, Context context, List<Recipe> recipes, List<String> keys) {
        mContext = context;
        mRecipeAdapter = new RecipeAdapters(recipes, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mRecipeAdapter);

    }

    class RecipeView extends RecyclerView.ViewHolder {
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
        private de.hdodenhof.circleimageview.CircleImageView img;
        //private ImageView img;

        private String key;

        public RecipeView(ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.content_recipe_list, parent, false));

            name = (TextView) itemView.findViewById(R.id.recipe_name);
            //store = (TextView) itemView.findViewById(R.id.item_store);
            //lCategory = (TextView) itemView.findViewById(R.id.base_lCategory);
            //sCategory = (TextView) itemView.findViewById(R.id.base_sCategory);
            //nDay = (TextView) itemView.findViewById(R.id.item_nDay);
            //count = (TextView) itemView.findViewById(R.id.item_count);
            //unit_price = (TextView) itemView.findViewById(R.id.item_unit_price);
            price = (TextView) itemView.findViewById(R.id.recipe_price);
            unit_price = (TextView) itemView.findViewById(R.id.recipe_unit_price);
            img = (de.hdodenhof.circleimageview.CircleImageView) itemView.findViewById(R.id.recipe_img);
            //img=(ImageView) itemView.findViewById(R.id.recipe_img);
            //amount = (TextView) itemView.findViewById(R.id.item_amount);
            //unit = (TextView) itemView.findViewById(R.id.item_unit);


        }

        @SuppressLint("SetTextI18n")
        public void bind(Recipe recipe, String key) {
            //userItem.print();
            name.setText(recipe.getName());
            price.setText(recipe.getPrice() + "원");
            unit_price.setText("단가 " + recipe.getTotal_unit_price() + "원");
            //name.setText(userItem.getName());
            //store.setText(userItem.getStore());
            //nDay.setText("D-"+userItem.getnDay());
            //count.setText(""+userItem.getCount()+"개");
            // price.setText(userItem.getPrice()+"원");
            //amount.setText(""+userItem.getAmount()+userItem.getUnit());
            this.key = key;

            //note 파이어베이스 스토리지 이미지 불러오기
            if (recipe.getImg() != null) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl("gs://inven-deu.appspot.com");
                StorageReference pathReference = storageReference.child("RecipeImg");
                if (pathReference == null) {
                    Toast.makeText(mContext, "저장소에 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    StorageReference submitProfile = storageReference.child(recipe.getImg());
                    submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(mContext).load(uri).into(img);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        }
    }


    class RecipeAdapters extends RecyclerView.Adapter<RecipeView> {
        private List<Recipe> mRecipeList;
        private List<String> mKeys;


        public RecipeAdapters(List<Recipe> mRecipeList, List<String> reKeys) {
            this.mRecipeList = mRecipeList;
            this.mKeys = reKeys;

            for (int i = 0; i < mRecipeList.size(); i++) {
                //mRecipeList.get(i).print();
            }
        }

        @NonNull
        @Override
        public RecipeView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecipeView(parent);
        }


        @Override
        public void onBindViewHolder(@NonNull RecipeView holder, int position) {
            holder.bind(mRecipeList.get(position), mKeys.get(position));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RecipeMoreActivity.class);
                    intent.putExtra("recipeList", mRecipeList.get(position));
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mRecipeList.size();
        }
    }
}
