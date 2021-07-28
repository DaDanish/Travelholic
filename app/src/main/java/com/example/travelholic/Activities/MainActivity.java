package com.example.travelholic.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.travelholic.Fragments.AlbumFragment;
import com.example.travelholic.Fragments.HomeFragment;
import com.example.travelholic.Fragments.MapFragment;
import com.example.travelholic.Fragments.ProfileFragment;
import com.example.travelholic.R;
import com.example.travelholic.databinding.ActivityMainBinding;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;
    FirebaseAuth firebaseAuth;


    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

      // getSupportFragmentManager().beginTransaction().replace(R.id.navController,new HomeFragment()).commit();

        //Navigation Bar
        activityMainBinding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
//                FragmentActivity selectedFragmentActivity= null;

                switch (item.getItemId()){
                    case R.id.homeFragment:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.mapFragment:
                        selectedFragment = new MapFragment();
                        break;
                    case R.id.albumFragment:
                        selectedFragment = new AlbumFragment();
                        break;
                    case R.id.profileFragment:
                        selectedFragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.navController,selectedFragment).commit();

                return true;
            }
        });








            firebaseAuth = FirebaseAuth.getInstance();




    }













    //Top right corner 3-dots menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.itemSettings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.itemLogout:
                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();        //this will not remember the previous logged in user
                Intent intent = new Intent(MainActivity.this,LogInActivity.class);
                startActivity(intent);
                finishAffinity();
                break;
        }
        return true;
    }
}