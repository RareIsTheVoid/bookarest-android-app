package com.example.bookarest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class activity_home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    CurrentUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home);




        loadFragment(new fragment_home());

        BottomNavigationView navigation = findViewById(R.id.nav_home);
        navigation.setOnNavigationItemSelectedListener(this);

        currentUser= (CurrentUser) getIntent().getSerializableExtra("currentUser");

        Log.v("books", MainActivity.database.bookDAO().getAllBooks().toString());
    }


    private boolean loadFragment(Fragment fragment) {
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
            return true;
        }
        return false;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.menu_item_home:
                fragment = new fragment_home();
                break;

            case R.id.menu_item_my_books:
                fragment = new fragment_my_books();
                break;

            case R.id.menu_item_profile:
                fragment = new fragment_profile();
                break;

            case R.id.menu_item_settings:
                fragment = new fragment_settings();
                break;
        }

        return loadFragment(fragment);
    }
}