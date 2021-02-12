package com.uni.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

//αρχικη οθονη. Οταν ο χρηστης εχει κανει login παραλειπεται. Μπορεις να δημιουργηθει νεο προφιλ εδω
public class LoginActivity extends AppCompatActivity {

    public static final String SHARED_PATH = "data/data/com.uni.notes/shared_prefs/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        //παιρνει την "hasLoggedIn". Αν δεν υπαρχει δηλαδη δεν εχει κανει login επιστρεφει false
        boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        //αν εχει γινει ηδη το login πηγαινει στο drawer
        if (hasLoggedIn) {
            Intent logintent = new Intent(this, DrawerActivity.class);
            startActivity(logintent);
        }
    }

    //handler για το νεο προφιλ, διαγραφει το hasLoggedIn για να μπει στην Main
    public void loginButtonHandler(View view) {
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("hasLoggedIn");
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //με το κουμπι επιστροφης πηγαινει στην αρχικη σελιδα
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}