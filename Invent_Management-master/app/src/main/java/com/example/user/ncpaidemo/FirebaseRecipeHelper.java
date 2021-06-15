package com.example.user.ncpaidemo;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRecipeHelper extends BaseActivity{

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrenceUsers;
    private ArrayList<Recipe> bases; // 조회 결과 Array List
    List<String> keys; // 조회 결과 key List

    public interface DataStatus {
        void DataIsLoaded(ArrayList<Recipe> recipes, List<String> keys);

        void DataIsInserted();

        void DataIsUpdated();

        void DataIsDeleted();
    }

    public FirebaseRecipeHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mRefrenceUsers = mDatabase.getReference("UserInfo").child(strNick).child("레시피목록");

    }

    public void readRecipe(FirebaseRecipeHelper.DataStatus dataStatus) {

        mRefrenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                bases = new ArrayList<>();
                bases.clear();

                keys = new ArrayList<>();
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    keys.add(keyNode.getKey());
                    Recipe recipe = keyNode.getValue(Recipe.class);
                    bases.add(recipe);
                }

                dataStatus.DataIsLoaded(bases, keys);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void addRecipe(Recipe recipe, final DataStatus dataStatus) {


        String key = mRefrenceUsers.push().getKey(); //note 키 생성
        mRefrenceUsers.child(key).setValue(recipe).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });

    }

    public void updateRecipe(String key, Recipe recipe, final DataStatus dataStatus) {
        mRefrenceUsers.child(strNick).child("레시피목록").child(key).setValue(recipe)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
                });
    }

    public void deleteRecipe(String key, final DataStatus dataStatus) {
        mRefrenceUsers.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
    }

}
