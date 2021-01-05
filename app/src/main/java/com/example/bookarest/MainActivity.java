package com.example.bookarest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button switchToLogin;
    Button switchToSignup;
    activity_login actLogin;
    ActivitySignup actSignUp;
    TextView tv_terms_of_service;
    public static AppDb database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                showAlert(getString(R.string.terms_of_service_lorem),MainActivity.this);
            }
        });
    }

    private void initialization() {
        switchToLogin = findViewById(R.id.button_login);
        switchToSignup = findViewById(R.id.button_signup);
        actLogin= new activity_login();
        actSignUp=new ActivitySignup();
        tv_terms_of_service=findViewById(R.id.tv_terms_of_service);

        database = Room.databaseBuilder(this, AppDb.class, "test2").allowMainThreadQueries().build();
        //loadBooksIntoDatabase();
        List<Book> books = database.bookDAO().getAllBooks();
        List<Author> authors = database.authorDAO().getAllAuthors();


        for(Book b: books) {
            Log.v("testing", b.toString());
        }
        for(Author a:authors){
            Log.v("testing", a.toString());
        }
    }


    private void switchActivities(AppCompatActivity activity) {
        Intent switchActivityIntent = new Intent(this, activity.getClass());
        startActivity(switchActivityIntent);
    }


    private void showAlert(String message, Context con){
        AlertDialog.Builder dialog = new AlertDialog.Builder(con,R.style.DialogStyle);

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
        dlg.getWindow().setLayout(1000,1600);
    }

    private String loadJSONWithBooks(){
        String json = null;
        try {
            InputStream inputStream = this.getAssets().open("aaa.json");
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

    private void loadBooksIntoDatabase(){
        try {
            JSONObject jsonObject = new JSONObject(loadJSONWithBooks());

            Set<Book> books = new HashSet<>();
            Set<Author> authors = new HashSet<>();

            JSONArray array = jsonObject.getJSONArray("Books");
            for(int i=0;i<array.length();i++){
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
            while (iterator.hasNext()){
                database.bookDAO().insertBook(iterator.next());
            }

            Iterator<Author> iterator2 = authors.iterator();
            while (iterator2.hasNext()){
                database.authorDAO().insertAuthor(iterator2.next());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}