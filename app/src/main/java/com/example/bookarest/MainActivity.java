package com.example.bookarest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button         switchToLogin;
    Button         switchToSignup;
    activity_login actLogin;
    ActivitySignup actSignUp;
    TextView       tv_terms_of_service;
    public static AppDb           database;
    public static List<BookCover> bookCoversData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        initialization();
        switchToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivities(actLogin);
            }
        });


        switchToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivities(actSignUp);
            }
        });

        tv_terms_of_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(getString(R.string.terms_of_service_lorem), MainActivity.this);
            }
        });
    }

    private void initialization() {
        switchToLogin = findViewById(R.id.button_login);
        switchToSignup = findViewById(R.id.button_signup);
        actLogin = new activity_login();
        actSignUp = new ActivitySignup();
        tv_terms_of_service = findViewById(R.id.tv_terms_of_service);


        loadCoversData();

        database = Room.databaseBuilder(this, AppDb.class, "test2").allowMainThreadQueries().build();
        loadBooksIntoDatabase();
    }


    private void switchActivities(AppCompatActivity activity) {
        Intent switchActivityIntent = new Intent(this, activity.getClass());
        startActivity(switchActivityIntent);
    }


    private void showAlert(String message, Context con) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(con, R.style.DialogStyle);

        dialog.setMessage(message);
        dialog.setTitle(getString(R.string.dlg_terms_of_service_title));
        dialog.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dlg = dialog.create();
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.show();
        dlg.getWindow().setLayout(1000, 1600);
    }

    private String loadJSONWithBooks() {
        String json = null;
        try {
            InputStream inputStream = this.getAssets().open("books.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private void loadBooksIntoDatabase() {
        try {
            JSONObject jsonObject = new JSONObject(loadJSONWithBooks());

            Set<Book> books = new HashSet<>();
            Set<Author> authors = new HashSet<>();

            JSONArray array = jsonObject.getJSONArray("Books");
            for (int i = 0; i < array.length(); i++) {
                int id = array.getJSONObject(i).getInt("id");
                String title = array.getJSONObject(i).getString("title");
                JSONObject authorJSON = new JSONObject(array.getJSONObject(i).getString("author"));
                int authorId = authorJSON.getInt("id");
                String authorName = authorJSON.getString("name");
                Author author = new Author(authorId, authorName);
                authors.add(author);
                int noPages = array.getJSONObject(i).getInt("noPages");
                books.add(new Book(id, title, author.getAuthorId(), noPages));
            }

            Iterator<Book> iterator = books.iterator();
            while (iterator.hasNext()) {
                database.bookDAO().insertBook(iterator.next());
            }

            Iterator<Author> iterator2 = authors.iterator();
            while (iterator2.hasNext()) {
                database.authorDAO().insertAuthor(iterator2.next());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void loadCoversData() {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://bookarest-bookcovers-rares-default-rtdb.firebaseio.com/");
        final DatabaseReference reference = firebaseDatabase.getReference("Books");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    BookCover bookCover = new BookCover(Integer.parseInt(dataSnapshot.child(String.valueOf(i)).child("id").getValue().toString()), dataSnapshot.child(String.valueOf(i++)).child("name").getValue().toString());
                    MainActivity.bookCoversData.add(bookCover);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

