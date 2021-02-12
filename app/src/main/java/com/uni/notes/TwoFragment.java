package com.uni.notes;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.File;
import java.util.ArrayList;

//επεξεργασια μαθηματων. Μπορει να προσθεσει να αφαιρεσει και να επεξεργαστει τα μαθηματα που παρακολουθει
public class TwoFragment extends Fragment {
    Button button1, button2, button3; //1 για διαγραφη, 2 για προσθήκη , 3 για επεξεργασια
    ListView listView;
    Profile profile;
    Gson gson = new Gson();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_two, container, false);

        getActivity().setTitle("Επεξεργασία Μαθημάτων");

        final SharedPreferences prefs = this.getActivity().getSharedPreferences(SubsActivity.SubsPREFERENCES, 0);   // ανακτηση του profile αρχικα αποθηκευμενες στην subsActivity
        String jsonN = prefs.getString("DATES", "");
        profile = gson.fromJson(jsonN, Profile.class);

        final ArrayList full = new ArrayList();
        for (int i = 0; i < 9; i++) {
            if(i<profile.getSize()) {
                if (profile.day2.get(i) != 5) {
                    full.add(profile.subject.get(i) + " (" + profile.week.get(profile.day.get(i)) + ", " + profile.week.get(profile.day2.get(i)) + ")");
                } else {
                    full.add(profile.subject.get(i) + " (" + profile.week.get(profile.day.get(i)) + ")");
                }
            }
        }

        //δημιουργια λιστας με multiple choice και περιεχομενο τα μαθηματα του profile
        listView = (ListView) myInflatedView.findViewById(R.id.editList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, full);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //trash button
        button1 = (Button) myInflatedView.findViewById(R.id.buttonDelete);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean zero = true; //αν δεν εχει επιλεγει κανενα = true
                int cntChoice = listView.getCount();
                ArrayList<String> sel = new ArrayList<>(cntChoice);
                for (int i = 0; i < cntChoice; i++) {                       //βαζει στην sel τα μαθηματα που εχουν επιλεγει
                    if (listView.isItemChecked(i)) {
                        sel.add(profile.subject.get(i));
                        zero = false;
                    }
                }
                //μηνυμα για να επιλεξουν μαθηματα
                if (zero) {
                    Toast.makeText(getContext(), "Διαλέξτε τουλάχιστον ένα μάθημα για διαγραφή", Toast.LENGTH_SHORT).show();
                }

                for (int j = 0; j < sel.size(); j++) {
                    deleteFromEdit(sel.get(j));

                    int pos = profile.subject.indexOf(sel.get(j));

                    //διαγραφη αντιστοιχων ειδοποιησεων
                    deleteNoti(pos, profile);

                    profile.subject.remove(pos);
                    profile.day.remove(pos);
                    profile.day2.remove(pos);
                    profile.code1.remove(pos);
                    profile.code2.remove(pos);
                    full.remove(pos);
                    adapter.notifyDataSetChanged();

                    //αποθηκευει τις αλλαγες στο profile
                    SharedPreferences.Editor editor = prefs.edit();
                    String json1 = gson.toJson(profile);
                    editor.putString("DATES", json1);
                    editor.apply();
                }
                listView.clearChoices();        //ξεεπιλεγει ολες τις επιλογες
                adapter.notifyDataSetChanged();
            }
        });

        //add Button
        button2 = (Button) myInflatedView.findViewById(R.id.buttonAdd);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profile.getSize() < 9) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());  //create the AlertBuilder
                    alert.setTitle("Μάθημα");
                    // Set an EditText view to get user input
                    final EditText input = new EditText(getContext());
                    alert.setView(input);
                    alert.setPositiveButton("Επόμενο", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //adds the sub to profile
                            final String name = input.getText().toString();
                            profile.subject.add(name);

                            String[] days = {"Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή"};
                            String[] none = {"Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή", "Κενό"};

                            final Dialog d = new Dialog(getContext());
                            d.setTitle("Ημέρα");
                            d.setContentView(R.layout.dialog);
                            Button b1 = (Button) d.findViewById(R.id.button1);
                            Button b2 = (Button) d.findViewById(R.id.button2);
                            final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                            final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker2);
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
                                    int day2 = np2.getValue();
                                    if(np.getValue() == day2) day2=5; //αν οι δυο μερες ειναι ιδιες βαζει την δευτερη κενη
                                    profile.day.add(np.getValue());
                                    profile.day2.add(day2);

                                    if (day2 != 5) {
                                        full.add(input.getText().toString() + " (" + profile.week.get(np.getValue()) + ", " + profile.week.get(np2.getValue()) + ")");
                                    } else {
                                        full.add(input.getText().toString() + " (" + profile.week.get(np.getValue()) + ")");
                                    }
                                    //αποθηκευει τις αλλαγες στο profile
                                    SharedPreferences.Editor editor = prefs.edit();
                                    String json1 = gson.toJson(profile);
                                    editor.putString("DATES", json1);
                                    editor.apply();

                                    addNoti(np.getValue(), np2.getValue(), profile , name);

                                    d.dismiss();
                                    adapter.notifyDataSetChanged();

                                }
                            });
                            b2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    d.dismiss();
                                }
                            });
                            d.show();

                            create(input.getText().toString());

                            listView.clearChoices();
                            adapter.notifyDataSetChanged();
                        }

                    });
                    alert.setNegativeButton("Άκυρο", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                        }
                    });
                    alert.show();
                } else {
                    Toast.makeText(getContext(), "Μέγιστος αριθμός μαθημάτων", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //edit button
        button3 = (Button) myInflatedView.findViewById(R.id.editDay);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean zero = true; //αν δεν εχει επιλεγει κανενα μαθημα = true

                int cntChoice = listView.getCount();
                SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();

                for (int i = 0; i < cntChoice; i++) {
                    if (sparseBooleanArray.get(i)) {
                        zero = false;
                        String[] days = {"Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή"};
                        String[] none = {"Δευτέρα", "Τρίτη", "Τετάρτη", "Πέμπτη", "Παρασκευή", "Κενό"};

                        final int j = i;
                        final Dialog d = new Dialog(getContext());
                        d.setTitle(profile.subject.get(j));
                        d.setContentView(R.layout.dialog);
                        Button b1 = (Button) d.findViewById(R.id.button1);
                        Button b2 = (Button) d.findViewById(R.id.button2);
                        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
                        final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker2);
                        np.setMaxValue(4);
                        np.setMinValue(0);
                        np.setDisplayedValues(days);
                        np.setWrapSelectorWheel(false);
                        np.setValue(profile.day.get(i));
                        np2.setMaxValue(5);
                        np2.setMinValue(0);
                        np2.setDisplayedValues(none);
                        np2.setWrapSelectorWheel(false);
                        np2.setValue(profile.day2.get(i));
                        b1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //διαγραφω τις παλαιες ειδοποιησεις
                                deleteNoti(j,profile);
                                //αλλαγη των ημερων
                                int day2 = np2.getValue();
                                if(np.getValue() == day2 ) day2 = 5;
                                profile.day.set(j, np.getValue());
                                profile.day2.set(j, day2);
                                //νεο εβδομαδιαιο προγραμμα μαθηματων
                                profile.generateWeekSchedule();
                                //δημιουργια νεων ειδοποιησεων
                                addNoti(np.getValue() , day2 ,profile, profile.subject.get(j));

                                if (day2 != 5) {
                                    full.set(j, profile.subject.get(j) + " (" + profile.week.get(np.getValue()) + ", " + profile.week.get(np2.getValue()) + ")");
                                } else {
                                    full.set(j, profile.subject.get(j) + " (" + profile.week.get(np.getValue()) + ")");
                                }
                                //αποθηκευει τις αλλαγες στο profile
                                SharedPreferences.Editor editor = prefs.edit();
                                String json1 = gson.toJson(profile);
                                editor.putString("DATES", json1);
                                editor.apply();

                                d.dismiss();

                                listView.clearChoices();
                                adapter.notifyDataSetChanged();
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
                }
                if (zero) {
                    Toast.makeText(getContext(), "Διαλέξτε τουλάχιστον ένα μάθημα για αλλαγή ημέρας", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return myInflatedView;
    }

    //δημιουργει τις ειδοποιησεις για το μαθημα, 2 κανονικα , 1 αν day2 = κενο  (μονο στο add button λογω του pos)
    private void addNoti(int day1, int day2, Profile profile, String name) {
        profile.generateWeekSchedule();         //δημιουργει το προγραμμα της εβδομαδας
        String[] d1 = profile.subsOfWeek[day1];
        int pos=-1;  //η θεση του μαθηματος στον πινακα με τα μαθηματα της ημερας
        for(int i=0;i<d1.length;i++){ //αναζητηση για την θεση του μαθηματος στον πινακα της πρωτης μερας
            if(d1[i].equals(name)){
                pos=i;
            }
        }
        int code = day1 * 10 + pos;     // κωδικος ειδοποιησης
        profile.code1.add(code);

        CustomNotification cm = new CustomNotification(code, getContext(), d1[pos], day1);
        cm.makeNotification(); //δημιουργια ειδοποιησης

        //ιδια διαδικασια αν 2η μερα  δνε ειναι κενο
        if(day2 != 5){
            d1 = profile.subsOfWeek[day2];
            for(int i=0;i<d1.length;i++){
                if(d1[i].equals(name)){
                    pos=i;
                }
            }
            code = day2 * 10 + pos;

            cm = new CustomNotification(code, getContext(), d1[pos], day2);
            cm.makeNotification();
        }
        else{
            code = 55;
        }
        profile.code2.add(code);

        //αποθηκευει τις αλλαγες στο profile
        SharedPreferences prefs = this.getActivity().getSharedPreferences(SubsActivity.SubsPREFERENCES, 0);
        SharedPreferences.Editor editor = prefs.edit();
        String json1 = gson.toJson(profile);
        editor.putString("DATES", json1);
        editor.apply();
    }

    private void deleteNoti(int pos, Profile profile) {
        String name = profile.subject.get(pos);
        int day = profile.day.get(pos);

        CustomNotification cm = new CustomNotification(profile.code1.get(pos), getContext(), name, day);
        cm.deleteNotification();

        if (profile.code2.get(pos) < 50) {
            day = profile.day2.get(pos);

            cm.setDay(day);
            cm.setCode(profile.code2.get(pos));
            cm.deleteNotification();
        }
    }

    //δημιουργει και αποθηκευει sPref με τον ονομα που καταχωρηθηκε παραπανω.
    public void create(String s) {
        SharedPreferences subPreferences = this.getActivity().getSharedPreferences(s, 0);
        SharedPreferences.Editor editor = subPreferences.edit();
        String json = gson.toJson(new Notes());
        editor.putString("DATES", json);
        editor.apply();
    }

    //διαγραφει το sPref και το file για καθε μαθημα, οταν παταμε διαγραφη
    private void deleteFromEdit(String s) {
        File file = new File(LoginActivity.SHARED_PATH + s + ".xml");
        SharedPreferences subPreferences = this.getActivity().getSharedPreferences(s, 0);
        SharedPreferences.Editor editorS = subPreferences.edit();
        editorS.clear();
        editorS.commit();

        file.delete();
    }
}