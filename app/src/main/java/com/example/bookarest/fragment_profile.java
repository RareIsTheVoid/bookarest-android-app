package com.example.bookarest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class fragment_profile extends Fragment {

    ImageView profilePicture;
    TextView tv_profile_email, tv_profile_name, tv_profile_phone, tv_profile_sex, tv_profile_birthdate, tv_profile_country;
    CurrentUser currentUser;
    Button      btn_edit_profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        handleImage(rootView, profilePicture, R.id.profile_picture);
        initialization(rootView);
        profileSetText();

        return rootView;
    }


    private void handleImage(View view, ImageView imageView, int id) {
        imageView = view.findViewById(id);
        imageView.setClipToOutline(true);
    }

    private void initialization(View view) {
        tv_profile_email=view.findViewById(R.id.tv_profile_email_value);
        tv_profile_email.setSelected(true);
        tv_profile_name=view.findViewById(R.id.tv_profile_name);
        tv_profile_phone=view.findViewById(R.id.tv_profile_phone_number_value);
        tv_profile_sex=view.findViewById(R.id.tv_profile_sex_value);
        tv_profile_birthdate=view.findViewById(R.id.tv_profile_birthdate_value);
        tv_profile_country=view.findViewById(R.id.tv_profile_country_value);

        btn_edit_profile=view.findViewById(R.id.button_profile_edit);
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });

        activity_home act = (activity_home)getActivity();
        currentUser = act.currentUser;
    }

    private void profileSetText(){
        tv_profile_name.setText(currentUser.getUser().getFirstName()+" "+currentUser.getUser().getLastName());
        tv_profile_email.setText(currentUser.getUser().getEmail());
        tv_profile_phone.setText(currentUser.getUser().getPhoneNumber());
        tv_profile_sex.setText(currentUser.getUser().getSex()==1?"M":"F");
        tv_profile_birthdate.setText(currentUser.getUser().getBirthDate());
        tv_profile_country.setText(currentUser.getUser().getCountry());
    }

    @SuppressLint ( {"NewApi", "ResourceAsColor", "ResourceType"} )
    private void showAlert(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogStyle);
        builder.setTitle("Update profile");

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText phoneNumberBox = new EditText(getContext());
        phoneNumberBox.setHint("Phone number");
        linearLayout.addView(phoneNumberBox);

        final RadioGroup radioGroup = new RadioGroup(getContext());
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        RadioButton radioButtonM = new RadioButton(getContext());
        radioButtonM.setText("Male");
        radioButtonM.setId(1);
        radioGroup.addView((radioButtonM));
        RadioButton radioButtonF = new RadioButton(getContext());
        radioButtonF.setText("Female");
        radioButtonF.setId(2);
        radioGroup.addView(radioButtonF);
        linearLayout.addView(radioGroup);


        final Calendar myCalendar = Calendar.getInstance();
        final EditText birthdateBox= new EditText(getContext());
        birthdateBox.setFocusable(false);
        birthdateBox.setHint("Birth date");
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                birthdateBox.setText(sdf.format(myCalendar.getTime()));;
            }
        };

        birthdateBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        linearLayout.addView(birthdateBox);

        final Spinner countryBox = new Spinner(getContext());
        String[] items = new String[]{"Romania", "United Kingdom", "United States", "Germany", "Spain", "Canada"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, items);
        countryBox.setAdapter(adapter);
        linearLayout.addView(countryBox);
        builder.setView(linearLayout);



        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String newPhone = phoneNumberBox.getText().toString();
                final String newCountry = countryBox.getSelectedItem().toString();
                final String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                final String newDate = sdf.format(myCalendar.getTime());
                final int newSex = radioGroup.getCheckedRadioButtonId();

                currentUser.getUser().setPhoneNumber(newPhone);
                currentUser.getUser().setCountry(newCountry);
                currentUser.getUser().setBirthDate(newDate);
                currentUser.getUser().setSex(newSex);


                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        MainActivity.database.userDAO().insertUser(new User(currentUser.getUser().getUserId(),currentUser.getUser().getEmail(),currentUser.getUser().getPassword(),currentUser.getUser().getFirstName(),currentUser.getUser().getLastName(),newPhone,newSex,newDate,newCountry));
                        return null;
                    }
                };

                //MainActivity.database.userDAO().insertUser(new User(currentUser.getUser().getUserId(),currentUser.getUser().getEmail(),currentUser.getUser().getPassword(),currentUser.getUser().getFirstName(),currentUser.getUser().getLastName(),newPhone,newSex,newDate,newCountry));
                profileSetText();
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


}