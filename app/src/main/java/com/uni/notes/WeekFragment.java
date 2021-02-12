package com.uni.notes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.gson.Gson;
import java.util.ArrayList;

//Εμφανιζει το εβδομαδιαιο προγραμμα του αναλογα με τα μαθηματα που δηλωσε οτι παρακολουθει
public class WeekFragment extends Fragment {
    Profile profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_prog, container, false);

        getActivity().setTitle("Πρόγραμμα");

        SharedPreferences prefs = getContext().getSharedPreferences(SubsActivity.SubsPREFERENCES, 0);   // ανακτηση του profile
        Gson gsonN = new Gson();                                                          // αρχικα αποθηκευμενες στην subsActivity
        String jsonN = prefs.getString("DATES", "");
        profile = gsonN.fromJson(jsonN, Profile.class);

        int c=0; // μετρητης πινακα
        int size = profile.getSize();
        profile.generateWeekSchedule();
        String[] subs = new String[15]; //πινακας που θα γεμισει
        for(int i=0;i<5;i++){  //για κάθε μέρα της εβδομάδας
            if(size <= 3){
                for(int j=0;j<3;j++){  //για καθε θεση της μερας, αν ειναι μέσα στα όρια του πίνακα βάζει το περιεχομενο του subsOfWeek
                    if(j<size){
                        subs[c] = profile.subsOfWeek[i][j];
                        c++;
                    }
                    else {  //αλλιως γεμίζει με ""
                        subs[c]="";
                        c++;
                    }
                }
            }
            else {
                ArrayList<String> valid = new ArrayList<>(); //περιεχει τα μαθηματα της μερας χωρις ""
                for(int j=0;j<size;j++){
                    if(!profile.subsOfWeek[i][j].equals("")) { //γεμιζει τον πινακα valid
                        valid.add(profile.subsOfWeek[i][j]);
                    }
                }
                for(int p=0;p<3;p++){     //αν το μεγεθος του valid ειναι μικροτερο απο το 3, τοτε βαζει την τιμη του valid
                    if(p<valid.size()){
                        subs[c]=valid.get(p);
                        c++;
                    }
                    else{       //γεμιζει το υπολοιπο με κενο
                        subs[c]="";
                        c++;
                    }
                }
            }
        }

        //o 2oς πινακας
        TableLayout tableLayout = (TableLayout) myInflatedView.findViewById(R.id.tt2);
        for(int i=0;i<tableLayout.getChildCount();i++){
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
            TextView textView = (TextView) tableRow.getChildAt(0);
            textView.setText(subs[i]);
        }

        return myInflatedView;
    }
}
