package com.example.user.ncpaidemo;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.user.ncpaidemo.SelectBaseActivity.lStr;

public class FirebaseDatabaseHelper {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefenceBases;
    private ArrayList<DatabaseReference> mRefList = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<BaseInfo> baseInfos, List<String> keys, int id);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();

    }

    public FirebaseDatabaseHelper(){
        for(int i=0; i<lStr.length; i++) {
            mDatabase = FirebaseDatabase.getInstance();
            mRefenceBases = mDatabase.getReference("BaseInfo").child("BaseInfo").child("lCategory").child(lStr[i]);
            mRefList.add(mRefenceBases);
        }

    }


    public void fireBase(DataStatus dataStatus){
        for(int i=0; i<mRefList.size(); i++) {
            int finalI = i;
            mRefList.get(i).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        List<BaseInfo> bases = new ArrayList<>();
                        bases.clear();

                        List<String> keys = new ArrayList<>();
                        for (DataSnapshot keyNode : snapshot.getChildren()) {
                            keys.add(keyNode.getKey());
                            BaseInfo baseInfo = keyNode.getValue(BaseInfo.class);
                            baseInfo.setlCategory(lStr[finalI]);
                            bases.add(baseInfo);
                        }


                        dataStatus.DataIsLoaded(bases, keys, finalI);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        }
    }

}
