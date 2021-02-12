package com.uni.notes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.Date;

//διαχειριζεται τις ημερομηνιες των σημειωσεων. Πινακας με τις ημερομηνιες, μπορεις να προσθεσεις αυτοματα ή manual ημερομηνια και να διαγραψεις μια υπαρχουσα
public class DatesActivity extends AppCompatActivity {
    String title,date;
    Boolean f=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dates);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //βαζει το βελακι με πατερα το DrawerActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ελεγχει αν υπαρχουν προηγουμενες ημερομηνιες
        check();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exit_only, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void addButtonHandler(View view) {

        Intent intent =getIntent();
        final String title  = intent.getStringExtra("name");

        SharedPreferences prefs = getSharedPreferences(title , MODE_PRIVATE);   // get notes object
        final Gson gsonN = new Gson();
        String jsonN = prefs.getString("DATES" , "");
        final Notes notes = gsonN.fromJson(jsonN , Notes.class);

        TableLayout table = (TableLayout) findViewById(R.id.test_table);
        final TableRow tr = new TableRow(this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkAuto);

        //ελεγχει αν το checkBox ειναι τικαρισμενο. Αν ειναι περναει την ημερομηνια αυτοματα με textView, αλλιως δημιουργει editText
        if(checkBox.isChecked()){
            //get the date in string
            date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            //the TextView with the date
            final TextView tv = new TextView(this);
            tv.setText(date);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP , 18);
            tv.setTextColor(Color.WHITE);
            TableRow.LayoutParams params = new TableRow.LayoutParams(getPx(100) , TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(0 ,0 ,getPx(100) ,0);
            tv.setLayoutParams(params);
            tr.addView(tv);
            //add info in note
            notes.addNote(date);
        }
        else{
            final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.edit_linear);
            linearLayout.setVisibility(View.VISIBLE);

            final EditText editText = (EditText) findViewById(R.id.editDate);

            Button button = (Button) findViewById(R.id.date_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!editText.getText().toString().equals("")) {
                        notes.addNote(editText.getText().toString());
                        // saving the changes in notes
                        SharedPreferences sharedPreferences = getSharedPreferences(title, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String json1 = gsonN.toJson(notes);
                        editor.putString("DATES", json1);
                        editor.apply();
                        //βγαζει μηνυμα αποθηκευσης
                        CharSequence text = "Αποθηκεύτηκε";
                        Toast.makeText(DatesActivity.this, text, Toast.LENGTH_LONG).show();
                        //κανει αορατα το κουμπι και το editText
                        linearLayout.setVisibility(View.GONE);
                        //δημιουργει και βαζει TextView με αυτο που εγραψε ο χρηστης
                        TextView tv = new TextView(DatesActivity.this);
                        tv.setText(editText.getText().toString());
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        tv.setTextColor(Color.WHITE);
                        TableRow.LayoutParams params = new TableRow.LayoutParams(getPx(100), TableRow.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, getPx(100), 0);
                        tv.setLayoutParams(params);
                        tr.addView(tv);

                        //κρυβει το πληκτρολογιο
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    else{
                        Toast.makeText(DatesActivity.this , "Πρέσθεσε ημερομηνία" , Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        // Adding row in table
        table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT
                , TableLayout.LayoutParams.WRAP_CONTENT));

        // saving the changes in notes
        SharedPreferences sharedPreferences =getSharedPreferences(title ,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json1 = gsonN.toJson(notes);
        editor.putString("DATES" , json1);
        editor.apply();
    }


    public int getPx(int dimensionDp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }

    private void check(){
        Intent intent =getIntent();
        title  = intent.getStringExtra("name");
        if(!intent.getStringExtra("receiver").equals("") ){     //it means that this method is executed cause of the notification
            title = intent.getStringExtra("receiver");          //it is supposed to get the string from the receiver
            f=true;
        }
        setTitle(title);

        SharedPreferences prefs = getSharedPreferences(title , MODE_PRIVATE);   // ανακτηση του notes
        Gson gsonN = new Gson();
        String jsonN = prefs.getString("DATES" , "");
        final Notes notes = gsonN.fromJson(jsonN , Notes.class);

        TableLayout table = (TableLayout) findViewById(R.id.test_table);
        // αν εχουν εισαχθει ημερομηνιες τις δειχνει με την βοηθεια του test layout
        if (notes.getNum_dates() != 0){
            for(int i=0 ; i<notes.getNum_dates() ; i++){
                TableRow tr = new TableRow(this);

                tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                final TextView tv = new TextView(this);
                TableRow.LayoutParams params = new TableRow.LayoutParams(getPx(100) , TableRow.LayoutParams.WRAP_CONTENT);
                params.setMargins(0 ,0 ,getPx(100) ,0);
                tv.setText(notes.getDate(i));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP , 18);
                tv.setLayoutParams(params);
                tv.setTextColor(Color.WHITE);

                //button για την διαγραφη του row
                ImageView imV = new ImageView(this);
                TableRow.LayoutParams imageParams = new TableRow.LayoutParams(50,70);
                imV.setLayoutParams(imageParams);
                imV.setImageResource(R.drawable.ok);
                imV.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewGroup parent = (ViewGroup) view.getParent();
                        ViewGroup grandparent = (ViewGroup) parent.getParent();
                        grandparent.removeView(parent);

                        String del = tv.getText().toString();
                        notes.delete(del);  // this does not seem to work

                        // saving the changes in notes
                        SharedPreferences sharedPreferences =getSharedPreferences(title ,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Gson gsonNm = new Gson();
                        String json1 = gsonNm.toJson(notes);
                        editor.putString("DATES" , json1);
                        editor.apply();
                    }
                });

                // Προσθηκη των αντικειμενων στην γραμμη του πινακα
                tr.addView(tv);
                tr.addView(imV);

                // Προσθηκη της γραμμης στον πινακα
                table.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT
                        , TableLayout.LayoutParams.WRAP_CONTENT));
            }
        }

        if(f){ //ηρθε απο notification
            //add info in note
            date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            notes.addNote(date);

            // saving the changes in notes
            SharedPreferences sharedPreferences =getSharedPreferences(title ,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gsonNm = new Gson();
            String json1 = gsonNm.toJson(notes);
            editor.putString("DATES", json1);
            editor.apply();

            Toast.makeText(this, "Ημερομηνία αποθηκεύτηκε", Toast.LENGTH_LONG).show();

            f = false;

            this.finish();
        }
    }

}
