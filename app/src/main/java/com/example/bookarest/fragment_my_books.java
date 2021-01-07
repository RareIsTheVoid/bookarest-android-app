package com.example.bookarest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class fragment_my_books extends Fragment {

    ImageView wtr1;
    ImageView wtr2;
    ImageView wtr3;
    ImageView cr1;
    ImageView cr2;
    ImageView cr3;
    ImageView r1;
    ImageView r2;
    ImageView r3;
    CurrentUser currentUser;
    Button      btn_editBooks;
    ListView lvBookItems;
    private       List<Author>  authors;
    public static List<Integer> answers = new ArrayList<>();
    private List<Book> books = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_books, container, false);

        initialization(rootView);

        return rootView;
    }

    private void handleImage(View view, ImageView imageView, int id) {
        imageView = view.findViewById(id);
        imageView.setClipToOutline(true);
    }

    void initialization(View rootView){
        books = MainActivity.database.bookDAO().getAllBooks();
        for(int i=0;i<books.size();i++)
            answers.add(0);

        activity_home act = (activity_home)getActivity();
        currentUser = act.currentUser;

        authors = MainActivity.database.authorDAO().getAllAuthors();
        btn_editBooks = rootView.findViewById(R.id.button_my_books_edit);
        btn_editBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });

        wtr1 = rootView.findViewById(R.id.iv_my_books_wtr1);
        wtr2 = rootView.findViewById(R.id.iv_my_books_wtr2);
        wtr3 = rootView.findViewById(R.id.iv_my_books_wtr3);
        cr1 = rootView.findViewById(R.id.iv_my_books_cr1);
        cr2 = rootView.findViewById(R.id.iv_my_books_cr2);
        cr3 = rootView.findViewById(R.id.iv_my_books_cr3);
        r1 = rootView.findViewById(R.id.iv_my_books_r1);
        r2 = rootView.findViewById(R.id.iv_my_books_r2);
        r3 = rootView.findViewById(R.id.iv_my_books_r3);

        handleImage(rootView, wtr1, R.id.iv_my_books_wtr1);
        handleImage(rootView, wtr2, R.id.iv_my_books_wtr2);
        handleImage(rootView, wtr3, R.id.iv_my_books_wtr3);
        handleImage(rootView, cr1, R.id.iv_my_books_cr1);
        handleImage(rootView, cr2, R.id.iv_my_books_cr2);
        handleImage(rootView, cr3, R.id.iv_my_books_cr3);
        handleImage(rootView, r1, R.id.iv_my_books_r1);
        handleImage(rootView, r2, R.id.iv_my_books_r2);
        handleImage(rootView, r3, R.id.iv_my_books_r3);


        if(currentUser.getWantToRead().size()>0){
            loadCoversFromStorage(wtr1, MainActivity.bookCoversData.get(currentUser.getWantToRead().get(0).getBookId()).getCoverFile());
        }
        if(currentUser.getWantToRead().size()>1){
            loadCoversFromStorage(wtr2, MainActivity.bookCoversData.get(currentUser.getWantToRead().get(1).getBookId()).getCoverFile());
        }
        if(currentUser.getWantToRead().size()>2){
            loadCoversFromStorage(wtr3, MainActivity.bookCoversData.get(currentUser.getWantToRead().get(2).getBookId()).getCoverFile());
        }
        if(currentUser.getCurrentlyReading().size()>0){
            loadCoversFromStorage(cr1, MainActivity.bookCoversData.get(currentUser.getCurrentlyReading().get(0).getBookId()).getCoverFile());
        }
        if(currentUser.getCurrentlyReading().size()>1){
            loadCoversFromStorage(cr2, MainActivity.bookCoversData.get(currentUser.getCurrentlyReading().get(1).getBookId()).getCoverFile());
        }
        if(currentUser.getCurrentlyReading().size()>2){
            loadCoversFromStorage(cr3, MainActivity.bookCoversData.get(currentUser.getCurrentlyReading().get(2).getBookId()).getCoverFile());
        }
        if(currentUser.getRead().size()>0){
            loadCoversFromStorage(r1, MainActivity.bookCoversData.get(currentUser.getRead().get(0).getBookId()).getCoverFile());
        }
        if(currentUser.getRead().size()>1){
            loadCoversFromStorage(r2, MainActivity.bookCoversData.get(currentUser.getRead().get(1).getBookId()).getCoverFile());
        }
        if(currentUser.getRead().size()>2){
            loadCoversFromStorage(r3, MainActivity.bookCoversData.get(currentUser.getRead().get(2).getBookId()).getCoverFile());
        }
    }

    void loadCoversFromStorage(final ImageView imageView, String child){

            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            StorageReference coverRef = mStorageRef.child(child);

            final long ONE_MEGABYTE = 1024*1024;
            coverRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length );
                    imageView.setImageBitmap(bmp);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
                }
            });
    }

    private void setupAdapter(){
        List<Book> books = MainActivity.database.bookDAO().getAllBooks();
        ArrayList<Book> allBooks = new ArrayList<>();
        allBooks.addAll(books);

        BookAdapter bookAdapter = new BookAdapter(allBooks,getActivity(), authors);
        lvBookItems.setAdapter(bookAdapter);
    }

    private void showAlert(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final ListView listView = new ListView(getContext());

        lvBookItems = listView;
        setupAdapter();

        linearLayout.addView(listView);
        builder.setView(linearLayout);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i=0;i<books.size();i++) {
                    int progress = 0;
                    if (answers.get(i) == 3) {
                        progress = 100;
                    }
                    if (answers.get(i) == 1) {
                        progress = 0;
                    }
                    if (answers.get(i) == 2) {
                        progress = MainActivity.database.userBookCrossRefDAO().getProgress(currentUser.getUser().getUserId(), books.get(i + 1).getBookId());
                    }
                    MainActivity.database.userBookCrossRefDAO().insertUserBookCrossRef(new UserBookCrossRef(currentUser.getUser().getUserId(), i + 1, answers.get(i), progress));
                }
                loadBooksCR(currentUser);
                loadBooksWTR(currentUser);
                loadBooksR(currentUser);
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

    void loadBooksCR(CurrentUser currentUser){

        List<UserBookCrossRef> cr = MainActivity.database.userBookCrossRefDAO().getAllUserBookCrossRefByIdAndCategory(currentUser.getUser().getUserId(),2);
        currentUser.setCurrentlyReading(new ArrayList<Book>());

        for(int i=0;i<cr.size();i++){
            UserBookCrossRef currentCrossRef = cr.get(i);
            Book currentBook = MainActivity.database.bookDAO().getBookById(currentCrossRef.bookId);

            currentUser.addToCurrentlyReading(currentBook);

        }
    }

    void loadBooksR(CurrentUser currentUser){

        List<UserBookCrossRef> cr = MainActivity.database.userBookCrossRefDAO().getAllUserBookCrossRefByIdAndCategory(currentUser.getUser().getUserId(),3);
        currentUser.setRead(new ArrayList<Book>());

        for(int i=0;i<cr.size();i++){
            UserBookCrossRef currentCrossRef = cr.get(i);
            Book currentBook = MainActivity.database.bookDAO().getBookById(currentCrossRef.bookId);

            currentUser.addToRead(currentBook);

        }
    }

    void loadBooksWTR(CurrentUser currentUser){

        List<UserBookCrossRef> cr = MainActivity.database.userBookCrossRefDAO().getAllUserBookCrossRefByIdAndCategory(currentUser.getUser().getUserId(),1);
        currentUser.setWantToRead(new ArrayList<Book>());

        for(int i=0;i<cr.size();i++){
            UserBookCrossRef currentCrossRef = cr.get(i);
            Book currentBook = MainActivity.database.bookDAO().getBookById(currentCrossRef.bookId);

            currentUser.addToWantToRead(currentBook);
        }
    }
}