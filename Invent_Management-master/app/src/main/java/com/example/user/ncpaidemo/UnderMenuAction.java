package com.example.user.ncpaidemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UnderMenuAction extends BaseActivity
{
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ChartActivity frag1;
    private HomeActivity frag2;
    private RecipeActivity frag3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.action_chart:
                        setFrag(0);
                        System.out.println("############ok");
                        break;
                    case R.id.action_home:
                        setFrag(1);
                        System.out.println("############ok");
                        break;

                    case R.id.action_menu:
                        setFrag(2);
                        System.out.println("############ok");
                        break;
                }
                return true;
            }
        });

        frag1=new ChartActivity();
        frag2=new HomeActivity();
        frag3=new RecipeActivity();

        setFrag(1); // 첫 프래그먼트 화면 지정
    }

    // 프레그먼트 교체
    private void setFrag(int n)
    {
        fm = getSupportFragmentManager();
        ft= fm.beginTransaction();
        switch (n)
        {
            case 0:
                ft.replace(R.id.Main_Frame,frag1);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.Main_Frame,frag2);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.Main_Frame,frag3);
                ft.commit();
                break;

        }
    }
}