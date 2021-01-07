package com.example.bookarest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class fragment_settings extends Fragment {

    TextView tv_interesting_fact1, tv_fact1, tv_interesting_fact2, tv_fact2;
    CurrentUser currentUser;
    Button btn_about;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        initialization(rootView);
        setFacts();


        return rootView;
    }

    void initialization(View view){
        tv_fact1=view.findViewById(R.id.fact1);
        tv_fact2= view.findViewById(R.id.fact2);
        tv_interesting_fact1 = view.findViewById(R.id.interesting_fact1);
        tv_interesting_fact2 = view.findViewById(R.id.interesting_fact2);
        btn_about = view.findViewById(R.id.btn_about);
        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.DialogStyle);

        dialog.setMessage(R.string.about);
        dialog.setTitle(getString(R.string.about_title));
        dialog.setPositiveButton(" OK ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dlg = dialog.create();
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.show();
        dlg.getWindow().setLayout(800, 1000);
    }

    void setFacts(){
        activity_home act = (activity_home)getActivity();
        currentUser = act.currentUser;
        int totalBooks = currentUser.getRead().size();
        int totalPages=0;
        for(Book b : currentUser.getRead()){
            totalPages+=b.getNumberOfPages();
        }

        tv_fact1.setText("You have read a total of "+totalBooks+" books.");
        tv_fact2.setText("You have read a total of "+totalPages+" pages.");

    }
}