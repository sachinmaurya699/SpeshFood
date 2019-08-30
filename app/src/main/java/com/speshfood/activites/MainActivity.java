package com.speshfood.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.speshfood.R;
import com.speshfood.dialogs.signInDailog;
import com.speshfood.fragments.orderFragment;
import com.speshfood.fragments.profileFragment;

public class MainActivity extends AppCompatActivity implements signInDailog.getLoginStatusInterface {
    BottomNavigationView btv;
    FrameLayout fl;
    orderFragment of1;


    @Override
    public
    void onBackPressed() {
        of1.onBackPressed ();

       // Toast.makeText (getApplicationContext (),"background pressed",Toast.LENGTH_LONG).show ();
        //super.onBackPressed ();
    }

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        btv=findViewById (R.id.btmNav);
        fl=findViewById (R.id.frameLayout);
        of1=new orderFragment ();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                of1).commit();
        btv.setOnNavigationItemSelectedListener (navListener);
    }
    BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener () {
                @Override
                public
                boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment=null;
                    switch(menuItem.getItemId()){
                        case R.id.home:selectedFragment=of1;
                            break;
                        case R.id.profile:
                            selectedFragment= new profileFragment ();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,selectedFragment).commit();
                    return true;
                }

            };

    @Override
    public
    boolean LoginStatus(boolean b) {
        Toast.makeText (getApplicationContext (),"changed",Toast.LENGTH_LONG).show ();
        Log.d ("loginstatus", "LoginStatus: ");
        orderFragment orderFragment=new orderFragment ();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,orderFragment).commit();
        orderFragment.changeLoginStatus (b);
        return false;
    }
}
