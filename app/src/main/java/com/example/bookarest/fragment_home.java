package com.example.bookarest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class fragment_home extends Fragment {

    ImageView img;
    Button btn_update_progress;
    ProgressBar progressBar;
    TextView tv_percentage, tv_bookTitle, tv_author;
    CurrentUser currentUser;
    public static Book currentlyReadingBook;
    AppDb database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            loadCoversFromStorage();
        } else {
            signInAsAnonymous(mAuth);
        }

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        handleImage(v, img, R.id.img_home_currently_reading);
        initialization(v);
        //insertSomeCrossRefs();
        btn_update_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert(currentlyReadingBook.getNumberOfPages(), currentlyReadingBook.getTitle());
            }
        });

        List<UserBookCrossRef> refs = new ArrayList<>();
        refs = database.userBookCrossRefDAO().getAllUserBookCrossRefByIdAndCategory(currentUser.getUser().getUserId(),2);
        Log.v("creatie", refs.toString());

        // Inflate the layout for this fragment
        return v;
    }



    private void initialization(View view) {
        btn_update_progress = view.findViewById(R.id.button_home_update_progress);
        progressBar = view.findViewById(R.id.progress_home);
        tv_percentage = view.findViewById(R.id.tv_home_progress_percentage);
        tv_bookTitle = view.findViewById(R.id.tv_home_cr_title);
        tv_author = view.findViewById(R.id.tv_home_cr_author);
        img = view.findViewById(R.id.img_home_currently_reading);
        activity_home act = (activity_home)getActivity();
        currentUser = act.currentUser;

        //loadCoversFromStorage();

        database = MainActivity.database;
        loadCurrentlyReading(currentUser);
        updateHomeFragment();
    }

    private void handleImage(View view, ImageView imageView, int id) {
        imageView = view.findViewById(id);
        imageView.setClipToOutline(true);
    }

    @SuppressLint ( {"ResourceAsColor", "NewApi"} )
    private void showAlert(final int totalPages, String title){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);
        builder.setTitle(R.string.button_home_update_progress);

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setTextCursorDrawable(R.color.colorFirstActivityButtons);
        input.setBackgroundTintList(ColorStateList.valueOf(R.color.colorFirstActivityButtons));
        input.setHint("Enter number of pages read");
        builder.setMessage(title+" has "+ totalPages + " pages.");
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.userBookCrossRefDAO().updateProgress(Integer.parseInt(input.getText().toString()), currentUser.getUser().getUserId(), currentlyReadingBook.getBookId());
                progressBar.setProgress(Integer.parseInt(input.getText().toString())*100/totalPages);

                if(progressBar.getProgress() >= 100){
                    //1-wtr,2-cr,3-r

                    //sterg cartea din currently reading
                    currentUser.removeFromCurrentlyReading(currentlyReadingBook);

                    //adaug cartea in read:
                    currentUser.addToRead(currentlyReadingBook);

                    //modific in cross refs:
                    database.userBookCrossRefDAO().insertUserBookCrossRef(new UserBookCrossRef(currentUser.getUser().getUserId(),currentlyReadingBook.getBookId(),3,69));

                    //testez:
                    Log.v("creatie","lista currently reading din current user: "+currentUser.getCurrentlyReading());

                    List<UserBookCrossRef> refList = new ArrayList<>();
                    refList = database.userBookCrossRefDAO().getAllUserBookCrossRefByIdAndCategory(currentUser.getUser().getUserId(),2);
                    Log.v("creatie",refList.toString());

                }

                tv_percentage.setText(new StringBuilder().append(progressBar.getProgress()).append("%").toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dlg = builder.create();
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.show();
    }

    private Book returnCurrentlyReadingBook() {
        Random random = new Random();

        return currentUser.getCurrentlyReading().get(random.nextInt(currentUser.getCurrentlyReading().size()));

    }

    private void setCurrentlyReadingText() {
        tv_bookTitle.setText(currentlyReadingBook.getTitle());
        tv_author.setText(database.authorDAO().getAuthorNameById(currentlyReadingBook.getAuthorOfBookId()));
        int savedProgress = database.userBookCrossRefDAO().getProgress(currentUser.getUser().getUserId(), currentlyReadingBook.getBookId()) * 100 / currentlyReadingBook.getNumberOfPages();
        tv_percentage.setText(new StringBuilder().append(savedProgress).append("%").toString());
        progressBar.setProgress(savedProgress);
    }

    private void updateHomeFragment(){
        if(currentUser.getCurrentlyReading().size()>0){
            currentlyReadingBook = returnCurrentlyReadingBook();
            setCurrentlyReadingText();
        } else {
            tv_bookTitle.setText("Not reading anything right now...");
            tv_author.setText("Go find a book you like!");
            btn_update_progress.setEnabled(false);
        }
    }


    private void signInAsAnonymous(FirebaseAuth mAuth) {
        mAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                loadCoversFromStorage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    void loadCoversFromStorage(){
        if(fragment_home.currentlyReadingBook!=null){
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference coverRef = mStorageRef.child(MainActivity.bookCoversData.get(currentlyReadingBook.getBookId()).getCoverFile());

            final long ONE_MEGABYTE = 1024*1024;
            coverRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length );
                    img.setImageBitmap(bmp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    void insertSomeCrossRefs(){
        database.userBookCrossRefDAO().insertUserBookCrossRef(new UserBookCrossRef(currentUser.getUser().getUserId(),25,2,55));
        database.userBookCrossRefDAO().insertUserBookCrossRef(new UserBookCrossRef(currentUser.getUser().getUserId(),26,2,56));
        database.userBookCrossRefDAO().insertUserBookCrossRef(new UserBookCrossRef(currentUser.getUser().getUserId(),27,2,57));
    }

    void loadCurrentlyReading(CurrentUser currentUser){

        List<UserBookCrossRef> cr = database.userBookCrossRefDAO().getAllUserBookCrossRefByIdAndCategory(currentUser.getUser().getUserId(),2);
        currentUser.setCurrentlyReading(new ArrayList<Book>());

        for(int i=0;i<cr.size();i++){
            UserBookCrossRef currentCrossRef = cr.get(i);
            Book currentBook = database.bookDAO().getBookById(currentCrossRef.bookId);

            currentUser.addToCurrentlyReading(currentBook);

        }

        Log.v("loadCR", currentUser.getCurrentlyReading().toString());
    }



}