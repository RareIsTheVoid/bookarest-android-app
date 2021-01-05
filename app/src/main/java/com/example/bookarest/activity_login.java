package com.example.bookarest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class activity_login extends AppCompatActivity {

    TextView tv_not_a_member;
    ActivitySignup actSignup;
    Button switchToHome;
    activity_home actHome;
    AppDb database = MainActivity.database;

    private CurrentUser currentUser;
    EditText et_login_email, et_login_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_login);

        initialization();
        tv_not_a_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivities(actSignup);
                finish();
            }
        });

        switchToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateLogIn()!=null){
                    getCurrentUserData(validateLogIn());
                    switchActivities(actHome);
                }
            }
        });
        
    }

    private void initialization(){
        tv_not_a_member=findViewById(R.id.tv_not_a_member);
        actSignup = new ActivitySignup();
        switchToHome = findViewById(R.id.button_login_login);
        actHome= new activity_home();
        et_login_email = findViewById(R.id.et_login_email);
        et_login_password = findViewById(R.id.et_login_password);



        insert();

    }

    private void switchActivities(AppCompatActivity activity){
        Intent switchActivityIntent = new Intent(this,activity.getClass());
        switchActivityIntent.putExtra("currentUser", currentUser);
        startActivity(switchActivityIntent);
    }

    private void insert() {
        database.authorDAO().insertAuthor(new Author(1, "damian"));
        database.authorDAO().insertAuthor(new Author(2, "Rares"));
        database.bookDAO().insertBook(new Book(1, "carte1", 1, 12));
        database.bookDAO().insertBook(new Book(2, "carte2", 1, 124));
        database.bookDAO().insertBook(new Book(3, "carte3", 1, 125));
        database.bookDAO().insertBook(new Book(4, "carte4", 2, 122));
        database.bookDAO().insertBook(new Book(5, "carte5", 2, 121));

        database.userDAO().insertUser(new User(0, "admin@gmail.com", "admin", "ADMIN", "ADMIN", "072ADMIN", 1, "01/01/2020", "Romania"));

        List<UserBookCrossRef> cross = database.userBookCrossRefDAO().getAllUserBookCrossRef();
        if(cross.size()==0){
            database.userBookCrossRefDAO().insertUserBookCrossRef(new UserBookCrossRef(0, 1, 3, 12));
            database.userBookCrossRefDAO().insertUserBookCrossRef(new UserBookCrossRef(0, 2, 2, 25));
            database.userBookCrossRefDAO().insertUserBookCrossRef(new UserBookCrossRef(0, 3, 2, 37));
            database.userBookCrossRefDAO().insertUserBookCrossRef(new UserBookCrossRef(0, 4, 1, 0));
            database.userBookCrossRefDAO().insertUserBookCrossRef(new UserBookCrossRef(0, 5, 1, 0));
        }
    }

    private void getCurrentUserData(String email) { // 1-wtr, 2-cr, 3-r
        User user = database.userDAO().getUserByEmail(email);

        List<Integer> wtrId = database.userBookCrossRefDAO().getAllUserBooksIdRefByIdAndCategory(user.getUserId(), 1);
        List<Integer> crId = database.userBookCrossRefDAO().getAllUserBooksIdRefByIdAndCategory(user.getUserId(), 2);
        List<Integer> rId = database.userBookCrossRefDAO().getAllUserBooksIdRefByIdAndCategory(user.getUserId(), 3);

        List<Book> wtrBooks = new ArrayList<>();
        List<Book> crBooks = new ArrayList<>();
        List<Book> rBooks = new ArrayList<>();
        for(Integer i: wtrId){
            Book newWtrBook = database.bookDAO().getBookById(i);
            wtrBooks.add(newWtrBook);
        }
        for(Integer i: crId){
            Book newCrBook = database.bookDAO().getBookById(i);
            crBooks.add(newCrBook);
        }
        for(Integer i: rId){
            Book newRBook = database.bookDAO().getBookById(i);
            rBooks.add(newRBook);
        }

        currentUser = new CurrentUser(user, wtrBooks, crBooks, rBooks);
    }

    private String validateLogIn(){
        if(TextUtils.isEmpty(et_login_email.getText().toString())){
            Toast.makeText(this, "Email field cannot be empty!", Toast.LENGTH_LONG).show();
            return null;
        }
        if(TextUtils.isEmpty(et_login_password.getText().toString())){
            Toast.makeText(this, "Password field cannot be empty!", Toast.LENGTH_LONG).show();
            return null;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(et_login_email.getText().toString()).matches()){
            Toast.makeText(this, "Invalid email format!", Toast.LENGTH_LONG).show();
            return null;
        }
        if(database.userDAO().getMatchingUser(et_login_email.getText().toString(), et_login_password.getText().toString())==0){
            Toast.makeText(this, "Incorrect email and/or password!", Toast.LENGTH_LONG).show();
            return null;
        }
        return et_login_email.getText().toString();
    }
}