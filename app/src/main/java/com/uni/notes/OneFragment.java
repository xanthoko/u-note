package com.uni.notes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;

//επεξεργασια στοιχειων χρηστη. Μπορει να αλλαξει τα στοιχεια του
public class OneFragment extends Fragment{

    TextView tv1,tv2,tv3,tv4;
    Button button;
    EditText editText1,editText2,editText3,editText4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View myInflatedView = inflater.inflate(R.layout.fragment_one, container,false);

        getActivity().setTitle("Επεξεργασία στοιχείων");

        final SharedPreferences prefs = this.getActivity().getSharedPreferences(SubsActivity.SubsPREFERENCES , 0);   // ανακτηση του profile
        final Gson gsonN = new Gson();                                                          // αρχικα αποθηκευμενες στην subsActivity
        String jsonN = prefs.getString("DATES" , "");
        final Profile profile = gsonN.fromJson(jsonN , Profile.class);

        //δημιουργει τα πεδιο για επεξεργασια
        tv1 = (TextView) myInflatedView.findViewById(R.id.textEditUser);
        tv1.setText("Username:  " + profile.getUsername());
        tv2 = (TextView) myInflatedView.findViewById(R.id.textEditUni);
        tv2.setText("Σχολή:  " + profile.getUniversity());
        tv3 = (TextView) myInflatedView.findViewById(R.id.textEditSem);
        tv3.setText("Εξάμηνο:  " + profile.getSemester());
        tv4 = (TextView) myInflatedView.findViewById(R.id.textEditOwed);
        tv4.setText("Χρωστούμενα:  " + profile.getOwed());
        button = (Button) myInflatedView.findViewById(R.id.saveButton);

        //αλλαγη username, βαζει στην θεση του text ενα edit text για αλλαγη
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText1 = new EditText(getContext());
                editText1.setText(profile.getUsername());
                editText1.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(400 , ViewGroup.LayoutParams.WRAP_CONTENT);
                editText1.setLayoutParams(params);

                ViewGroup parent = (ViewGroup) view.getParent();
                int index = parent.indexOfChild(view);
                parent.removeView(view);
                parent.addView(editText1 , index);
            }
        });

        //αντιστοιχα για την σχολη
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText2 = new EditText(getContext());
                editText2.setText(profile.getUniversity());
                editText2.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(400 , ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,getPx(30),0,0);
                editText2.setLayoutParams(params);

                ViewGroup parent = (ViewGroup) view.getParent();
                int index = parent.indexOfChild(view);
                parent.removeView(view);
                parent.addView(editText2 , index);
            }
        });

        //αντιστοιχα για το εξαμηνο
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText3 = new EditText(getContext());
                editText3.setText(profile.getSemester());
                editText3.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(400 , ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,getPx(30),0,0);
                editText3.setLayoutParams(params);

                ViewGroup parent = (ViewGroup) view.getParent();
                int index = parent.indexOfChild(view);
                parent.removeView(view);
                parent.addView(editText3 , index);
            }
        });

        //αντιστοιχα για τα χρωστουμενα
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText4 = new EditText(getContext());
                editText4.setText(profile.getOwed());
                editText4.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(400 , ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,getPx(30),0,0);
                editText4.setLayoutParams(params);

                ViewGroup parent = (ViewGroup) view.getParent();
                int index = parent.indexOfChild(view);
                parent.removeView(view);
                parent.addView(editText4 , index);
            }
        });

        final LinearLayout linearLayout = (LinearLayout) myInflatedView.findViewById(R.id.prof_layout);
        //κουμπι αποθηκευσης
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText1 != null){
                    profile.setUsername(editText1.getText().toString());

                    tv1.setText("Username: "+editText1.getText().toString());

                    int index = linearLayout.indexOfChild(editText1);
                    linearLayout.removeView(editText1);
                    linearLayout.addView(tv1,index);
                    editText1=null;
                }

                if (editText2 != null){
                    profile.setUniversity(editText2.getText().toString());

                    tv2.setText("Σχολή: "+editText2.getText().toString());

                    int index = linearLayout.indexOfChild(editText2);
                    linearLayout.removeView(editText2);
                    linearLayout.addView(tv2,index);
                    editText2=null;
                }

                if (editText3 != null){
                    profile.setSemester(editText3.getText().toString());

                    tv3.setText("Εξάμηνο: "+editText3.getText().toString());

                    int index = linearLayout.indexOfChild(editText3);
                    linearLayout.removeView(editText3);
                    linearLayout.addView(tv3,index);
                    editText3=null;
                }

                if (editText4 != null){
                    profile.setOwed(editText4.getText().toString());

                    tv4.setText("Χρωστούμενα: "+editText4.getText().toString());

                    int index = linearLayout.indexOfChild(editText4);
                    linearLayout.removeView(editText4);
                    linearLayout.addView(tv4,index);
                    editText4=null;
                }

                //αποθηκευση του profile
                SharedPreferences.Editor editorN = prefs.edit();
                String json1 = gsonN.toJson(profile);
                editorN.putString("DATES" , json1);
                editorN.apply();

                Toast.makeText(getContext() , "Οι αλλαγές αποθηκεύτηκαν" , Toast.LENGTH_LONG).show();

                hideKeyboard(view);
            }
        });

        return myInflatedView;
    }

    public int getPx(int dimensionDp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
