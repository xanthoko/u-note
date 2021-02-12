package com.uni.notes;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;

//2ο σταδιο login. Δηλωνει τα μαθματα που παρακολουθει και τις μερες τους
public class SubsActivity extends AppCompatActivity {
    public int i;
    public TextView textView;
    public Profile profile;
    int size=0;

    public static final String SubsPREFERENCES = "subsPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        profile = (Profile) intent.getSerializableExtra("choise");   // παιρνω το profile απο το MainActivity

        size = Integer.parseInt(profile.getAttending()); //ποσα παρακολουθει

        final EditText[] editText = new EditText[size];
        final Button[] button = new Button[size];
        //για καθε μαθημα δημιουργει μια γραμμη πινακα με editext,textview και κουμπι
        for (i = 0; i < size; i++) {
            TableLayout table = (TableLayout) findViewById(R.id.declare_table);

            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            textView = new TextView(this);
            textView.setText("Μάθημα " + (i + 1) + ":   ");
            textView.setTextColor(Color.WHITE);

            editText[i] = new EditText(this);
            editText[i].setMinimumWidth(getPx(180));
            editText[i].setMaxWidth((getPx(220)));
            editText[i].setSingleLine(true);
            editText[i].setId(i);
            editText[i].setBackground(getResources().getDrawable(R.drawable.edit_text));
            editText[i].setTextColor(Color.WHITE);

            button[i] = new Button(this);
            button[i].setText("Μέρα");
            button[i].setId(i);
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    String[] days = {"Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή"};
                    String[] none = {"Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή", "Κενό"};

                    //για να επιλεξει μερες
                    final Dialog d = new Dialog(SubsActivity.this);
                    d.setTitle("Ημέρα");
                    d.setContentView(R.layout.dialog);
                    Button b1 = (Button) d.findViewById(R.id.button1);
                    Button b2 = (Button) d.findViewById(R.id.button2);
                    final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1); //1η μερα
                    final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker2); //2η μερα
                    np.setMaxValue(4);
                    np.setMinValue(0);
                    np.setDisplayedValues(days);
                    np.setWrapSelectorWheel(false);
                    np2.setMaxValue(5);
                    np2.setMinValue(0);
                    np2.setDisplayedValues(none);
                    np2.setWrapSelectorWheel(false);
                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int k = view.getId();               //αν το κουμπι ειναι αυτο που ειναι στην αρχικη σειρα, δεν θα εχει τιποτα
                            if (profile.day.size() == k) {       //αρα θα πρεπει να γινει add στην λιστα
                                int day2 = np2.getValue();
                                if(np.getValue() == day2) day2=5;
                                profile.day.add(np.getValue());
                                profile.day2.add(day2);
                            } else {                              //αλλιως σημαινει οτι εχει ξαναπατηθει, αρα αντικατασταση της μερας
                                profile.day.set(k, np.getValue());
                                profile.day2.set(k, np2.getValue());
                            }
                            d.dismiss();
                        }
                    });
                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.dismiss();
                        }
                    });
                    d.show();
                }
            });

            tr.addView(textView);
            tr.addView(editText[i]);
            tr.addView(button[i]);
            tr.setMinimumHeight(getPx(50));

            table.addView(tr);
        }
    }

    public void finishButtonHandler(View view) {
        boolean blank = false;
        for(int i=0;i<size;i++){
            EditText et = (EditText) findViewById(i);
            if(et.getText().toString().equals("")){
                blank = true;
            }
        }

        //αν εχουν συμπληρωθει ολα
        if (profile.day.size() == size && !blank) {
            String[] subjects = new String[size]; // δημιουργια πινακα μαθηματων

            for (int i = 0; i < size; i++) {
                EditText et = (EditText) findViewById(i);
                subjects[i] = et.getText().toString();
            }
            profile.setSubject(subjects);       // κραταω το περιεχομενο του editText και το βαζω στον πινακα subject
            profile.generateWeekSchedule();     //δημιουργια του εβδομαδιαιου προγραμματος

            //κωδικοι για τις ειδοποιησεις
            for (int i = 0; i < profile.getSize(); i++) {
                int place = -1; //η θεση του μαθηματος στην αντιστοιχη μερα-πινακα
                int day = profile.day.get(i);   //οι μερες της εβδομαδας
                String[] d = profile.subsOfWeek[day];
                for (int j = 0; j < d.length; j++) {
                    if (d[j].equals(profile.subject.get(i))) {
                        place = j;
                    }
                }

                int code = day * 10 + place;
                profile.code1.add(code);    //προσθετει τον κωδικο για την πρωτη μερα του μαθηματος

                day = profile.day2.get(i);
                if (day != 5) { //αν δεν ειναι "κενο"
                    d = profile.subsOfWeek[day];
                    for (int j = 0; j < d.length; j++) {
                        if (d[j].equals(profile.subject.get(i))) {
                            place = j;
                        }
                    }
                    code = day * 10 + place;
                } else {    //αν ειναι παει ο κωδικος 55
                    code = 55;
                }
                profile.code2.add(code);
            }

            //αποθηκευση του profile
            SharedPreferences notePreferences = getSharedPreferences(SubsPREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor editorN = notePreferences.edit();
            Gson gsonN = new Gson();
            String json1 = gsonN.toJson(profile);
            editorN.putString("DATES", json1);
            editorN.apply();

            //Με το που μπει εδω σημαινει οτι εχει κανει log in και αρα βαζω την hasLoggedIn true , την οποια ελεγχω στην MainActivity
            SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0); // 0 - for private mode
            SharedPreferences.Editor editorLog = settings.edit();
            editorLog.putBoolean("hasLoggedIn", true);
            editorLog.apply();

            // για καθε ενα μαθημα αποθηκευω ενα αντικειμενο notes
            for (int i = 0; i < profile.subject.size(); i++) {
                SharedPreferences subPreferences = getSharedPreferences(profile.subject.get(i), MODE_PRIVATE);
                SharedPreferences.Editor editor = subPreferences.edit();
                String json = gsonN.toJson(new Notes());
                editor.putString("DATES", json);
                editor.apply();
            }
            Intent intent2 = new Intent(this, DrawerActivity.class);     //παμε στην PhotoActivity
            startActivity(intent2);
        } else { //αλλιως βγαζει μηνυμα
            Toast.makeText(this, "Πρέπει να δηλωθούν όλα τα μαθήματα και οι μέρες τους!", Toast.LENGTH_LONG).show();
        }
    }

    //μετατροπη dp σε px
    public int getPx(int dimensionDp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }
}


