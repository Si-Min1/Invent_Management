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

public class FirebaseUserHelper extends BaseActivity{

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrenceUsers;
    private ArrayList<UserItem> bases; // 조회 결과 Array List
    ArrayList<String> keys; // 조회 결과 key List

    public interface DataStatus{
        void DataIsLoaded(ArrayList<UserItem> userItems, ArrayList<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseUserHelper(String strNick){
        mDatabase = FirebaseDatabase.getInstance();
        System.out.println("::::::::::::::::::::::: Firebase User Helper2 || strNick ::::::::::::::::::::: " +strNick);
        mRefrenceUsers = mDatabase.getReference("UserInfo").child(strNick).child("식자재목록");

    }

    public FirebaseUserHelper(){
        mDatabase = FirebaseDatabase.getInstance();
        System.out.println("::::::::::::::::::::::: Firebase User Helper1 || strNick ::::::::::::::::::::: " +strNick);
        mRefrenceUsers = mDatabase.getReference("UserInfo").child(strNick).child("식자재목록");

    }


    public void readUserItem(FirebaseUserHelper.DataStatus dataStatus){

        mRefrenceUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    bases = new ArrayList<>();
                    bases.clear();

                    keys = new ArrayList<>();
                    for (DataSnapshot keyNode : snapshot.getChildren()) {
                        keys.add(keyNode.getKey());
                        UserItem userItem = keyNode.getValue(UserItem.class);
                        bases.add(userItem);
                    }

                        dataStatus.DataIsLoaded(bases,keys);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public void readsCategory(FirebaseUserHelper.DataStatus dataStatus){
            mRefrenceUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    bases = new ArrayList<>();
                    bases.clear();

                    keys = new ArrayList<>();
                    for (DataSnapshot keyNode : snapshot.getChildren()) {
                        keys.add(keyNode.getKey());
                        UserItem userItem = keyNode.getValue(UserItem.class);
                        bases.add(userItem);
                    }

                    dataStatus.DataIsLoaded(bases,keys);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public void addUserItem (ArrayList<UserItem> userItems, final DataStatus dataStatus){

            for(int i=0; i<userItems.size(); i++) {
                String key = mRefrenceUsers.push().getKey(); //note 키 생성
                mRefrenceUsers.child(key).setValue(userItems.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsInserted();
                    }
                });
            }
        }

        public void updateUserItem(String key, UserItem userItem, final DataStatus dataStatus){
            mRefrenceUsers.child(key).setValue(userItem)
                .addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsUpdated();
                    }
            });
        }
        public void deleteUserItem(String key,final DataStatus dataStatus){
            mRefrenceUsers.child(key).setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dataStatus.DataIsDeleted();
                    }
                });
        }


    }
