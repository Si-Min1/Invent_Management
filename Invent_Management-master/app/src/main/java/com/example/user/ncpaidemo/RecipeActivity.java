package com.example.user.ncpaidemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends Fragment {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private ChildEventListener mChild;

    private ListView listView;
    private ArrayAdapter<String> adapter;

    private Button btnP;
    private String strNick;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        view = inflater.inflate(R.layout.frag_recipe,container,false);

        /*listView = (ListView) view.findViewById(R.id.listview);

        List<String> Array = new ArrayList<String>();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1,Array);
        listView.setAdapter(adapter);

        adapter.add("asdf");
        Array.add("아메리카노");
        Array.add("카페라떼");
        Array.add("바닐라라떼");
        Array.add("카푸치노");

        btnP = (Button) view.findViewById(R.id.button);
        btnP.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                //Intent intent = new Intent(getApplicationContext(), recipes.class);
                // intent.putExtra("name",strNick);
                //startActivity(intent);
            }
        });*/

        Bundle bundle = getArguments();
        strNick = bundle.getString("strNick");

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recipe_list);
        new FirebaseRecipeHelper().readRecipe(new FirebaseRecipeHelper.DataStatus() {
            @Override
            public void DataIsLoaded(ArrayList<Recipe> recipes, List<String> keys) {

                new RecipeAdapter().setConfig(mRecyclerView, view.getContext(), recipes, keys);

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

        view.findViewById(R.id.addRecipe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), RecipeAddActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        return view;
    }

    /*private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("log");
        mReference.child("log").setValue("check");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }*/

}
