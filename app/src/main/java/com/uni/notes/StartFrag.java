package com.uni.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.gson.Gson;

//αρχικη σελιδα. Εχει την λιστα με τα μαθηματα που παρακολουθει και μπορει να μεταβει στις σημειωσεις του
public class StartFrag extends Fragment {
    Profile profile;
    View myinflatedview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myinflatedview = inflater.inflate(R.layout.fragment_start, container, false);

        getActivity().setTitle("Σημειώσεις");

        SharedPreferences prefs = getContext().getSharedPreferences(SubsActivity.SubsPREFERENCES, 0);   // ανακτηση του profile
        Gson gsonN = new Gson();                                                          // αρχικα αποθηκευμενες στην subsActivity
        String jsonN = prefs.getString("DATES", "");
        profile = gsonN.fromJson(jsonN, Profile.class);

        //πινακας με αυτο που θα φαινεται στην λιστα
        String[] math = new String[9];
        for (int i = 0; i < 9 ; i++) {
            if(i<profile.getSize()) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(profile.subject.get(i) , 0);
                gsonN = new Gson();
                jsonN = sharedPreferences.getString("DATES" , "");
                Notes notes = gsonN.fromJson(jsonN , Notes.class);
                String t = profile.subject.get(i);
                if (profile.day2.get(i) != 5) {
                    if (t.length() > 18){
                        t = profile.subject.get(i).substring(0, 18);
                    }
                    math[i] = t +  " (" + profile.week.get(profile.day.get(i)) + ", " + profile.week.get(profile.day2.get(i)) + ")" + "     " + notes.getNum_dates();
                } else {
                    math[i] = t + " (" + profile.week.get(profile.day.get(i)) + ")"+ "     " + notes.getNum_dates();
                }
            }
            else{
                math[i] = "";
            }
        }

        //η listView με τα μαθηματα που παρακολουθει
        ListView sublist = (ListView) myinflatedview.findViewById(R.id.sub_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.custom_list, math);
        sublist.setAdapter(adapter);

        sublist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(profile.getSize() > i) {  //όχι τα κενα στον πίνακα
                    Intent intent3 = new Intent(getContext(), DatesActivity.class);
                    intent3.putExtra("name", profile.subject.get(i));
                    intent3.putExtra("receiver", "");    // σημαινει οτι ο χρηστης δεν πηγε μεσω της ειδοποιησης στο μαθημα.
                    startActivity(intent3);
                }
            }
        });

        makeNotification();

        return myinflatedview;
    }

    //δημιουργει ειδοποιηση για τα μαθήματα (εδω και οχι στο subs γιατι στο photo μπορει να πατησει πισω και να αλλαξει δεδομενα και να μην τα πιασει)
    public void makeNotification() {
        SharedPreferences prefs = getContext().getSharedPreferences(SubsActivity.SubsPREFERENCES, 0);
        Boolean flag = prefs.getBoolean("alarm", false);

        if (!flag) {      //ελεγχει αν εχει ξαναγινει . Αν δεν εχει ξαναμπει η flag θα ειναι false

            profile.generateWeekSchedule(); //δημιουργει το πραγραμμα της εβδομαδας
            for (int j = 0; j < 5; j++) {
                String[] day = profile.subsOfWeek[j];  //πινακας με τα μαθηματα της ημερας
                for (int i = 0; i < day.length; i++) {
                    String name = day[i];

                    if (name.equals("")) {
                        continue;           //αν ειναι "" προχωραει στην επομενη επαναληψη
                    }
                    CustomNotification cm = new CustomNotification(i+10*j, getContext(), name, j);
                    cm.makeNotification(); //δημιουργει την ειδοποιηση
                }
            }
        }

        SharedPreferences.Editor editor = prefs.edit(); //βαζει την boolean μεταβλητη flag true
        editor.putBoolean("alarm", true);
        editor.apply();
    }
}
