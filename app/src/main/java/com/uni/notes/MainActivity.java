package com.uni.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

//1ο σταδιο login. Δηλωνονται τα στοιχεια του χρηστη
public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); //να μην φαινεται ο τιτλος

        //να κρυβεται το πληκτρολογιο οταν παταω καπου αλλου απο το editText
        EditText etUser = (EditText) findViewById(R.id.editUserName);
        etUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        EditText etUni = (EditText) findViewById(R.id.editFirstInput);
        etUni.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        EditText etSem = (EditText) findViewById(R.id.editSecondInput);
        etSem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        EditText etOw = (EditText) findViewById(R.id.editl1Input);
        etOw.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    //handler για το κουμπι μεταβασης, αποθηκευει τα πρωτα στοιχεια στο προφιλ
    public void transButtonHandler(View view) {
        EditText etUser = (EditText) findViewById(R.id.editUserName);   //username
        String username = etUser.getText().toString();

        EditText etUni = (EditText) findViewById(R.id.editFirstInput);  //σχολη
        String university = etUni.getText().toString();

        EditText etSem = (EditText) findViewById(R.id.editSecondInput);  //εξαμηνο           //παιρνει τα στοιχεια του προφιλ
        String semester = etSem.getText().toString();

        EditText etOw = (EditText) findViewById(R.id.editl1Input);  //χρωστουμενα
        String owed = etOw.getText().toString();

        Spinner spinner = (Spinner) findViewById(R.id.spinner2);    //παρακολουθει
        String attending = spinner.getSelectedItem().toString();

        //αν εχουν συμπληρωθει ολα τα πεδιο δημιουγει το προφιλ, αλλιως δειχει σχετικο μηνυμα
        if(!username.equals("") && !university.equals("") && !semester.equals("") && !attending.equals("") && !owed.equals("") ){
            Profile profile = new Profile(username, university, semester, owed, attending);  // δημιουργει profile με τα παραπανω στοιχιεα

            Intent intent1 = new Intent(this , SubsActivity.class);
            intent1.putExtra("choise" , profile);                       //παμε στην SubsActivity κουβαλωντας το profile
            startActivity(intent1);
        }
        else{
            Toast.makeText(this , "Πρέπει να συμπληρωθούν όλα τα πεδία!" , Toast.LENGTH_LONG).show();
        }
    }

    //για να κρυβει το πληκτρολογιο
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}