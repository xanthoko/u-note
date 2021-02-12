package com.uni.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.io.File;

//Ο χρηστης μπορει να δει το προφιλ του. Στοιχεια, εικονα
public class ProfileFrag extends Fragment {
    public static final String SHARED_PATH = "data/data/com.uni.notes/shared_prefs/";
    Profile profile;
    File file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myinflatedview = inflater.inflate(R.layout.fragment_profile, container, false);

        getActivity().setTitle("Προφίλ");

        SharedPreferences prefs = getContext().getSharedPreferences(SubsActivity.SubsPREFERENCES, 0);   // ανακτηση του profile
        Gson gsonN = new Gson();                                                          // αρχικα αποθηκευμενες στην subsActivity
        String jsonN = prefs.getString("DATES", "");
        profile = gsonN.fromJson(jsonN, Profile.class);

        TextView textUser = (TextView) myinflatedview.findViewById(R.id.textProfileUser);      // δημιουργια προβολης username
        textUser.setText(profile.getUsername());
        TextView textUni = (TextView) myinflatedview.findViewById(R.id.textProfileUni);         // δημιουργια προβολης uni
        textUni.setText(profile.getUniversity());
        TextView textSem = (TextView) myinflatedview.findViewById(R.id.textProfileSem);         // δημιουργια προβολης semester
        textSem.setText(profile.getSemester() + "ο εξάμηνο");
        TextView textAtt = (TextView) myinflatedview.findViewById(R.id.textProfileAtt);
        textAtt.setText("παρακολουθείς: " + profile.getSize());
        TextView textOw = (TextView) myinflatedview.findViewById(R.id.textProfileOw);
        textOw.setText("χρωστάς: " + profile.getOwed());

        ImageView im = (ImageView) myinflatedview.findViewById(R.id.imageDelete);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Διαγραφή προφίλ ?")
                        .setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delete();
                                Intent intent = new Intent(getContext() , LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Άκυρο", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //τιποτα
                            }
                        })
                        .show();
            }
        });

        TextView tv = (TextView) myinflatedview.findViewById(R.id.remove);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Διαγραφή προφίλ ?")
                        .setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delete();
                                Intent intent = new Intent(getContext() , LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Άκυρο", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //τιποτα
                            }
                        })
                        .show();
            }
        });

        return myinflatedview;
    }

    //επιστερρφει την κυκλικη εικονα
    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;

        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            float smallest = Math.min(bmp.getWidth(), bmp.getHeight());
            float factor = smallest / radius;
            sbmp = Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() / factor), (int) (bmp.getHeight() / factor), false);
        } else {
            sbmp = bmp;
        }

        Bitmap output = Bitmap.createBitmap(radius, radius,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, radius, radius);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(radius / 2 + 0.7f,
                radius / 2 + 0.7f, radius / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

    //διαγραφη οσων εχουν σχεση με την εφαρμογη
    public void delete() {

        //διαγραφη των μαθηματων
        SharedPreferences prefs = getContext().getSharedPreferences(SubsActivity.SubsPREFERENCES, 0);
        Gson gsonN = new Gson();
        String jsonN = prefs.getString("DATES", "");
        final Profile profile = gsonN.fromJson(jsonN, Profile.class);

        for (int i = 0; i < profile.subject.size(); i++) {
            file = new File(SHARED_PATH + profile.subject.get(i) + ".xml");
            SharedPreferences subPreferences = getContext().getSharedPreferences(profile.subject.get(i), 0);
            SharedPreferences.Editor editorS = subPreferences.edit();
            editorS.clear();
            editorS.commit();

            file.delete();
        }

        //διαγραφη ειδοποιήσεων
        for(int i=0;i<profile.code1.size();i++){
            CustomNotification cm = new CustomNotification(profile.code1.get(i), getContext(), profile.subject.get(i), profile.day.get(i));
            cm.deleteNotification();
            cm.setCode(profile.code2.get(i));
            if(profile.code2.get(i) < 50) {
                cm.setDay(profile.day2.get(i));
                cm.deleteNotification();
            }
        }

        //διαγραφη του προφιλ
        SharedPreferences.Editor editorN = prefs.edit();
        editorN.clear();
        editorN.commit();

        file = new File(SHARED_PATH + "subsPrefs.xml");
        file.delete();

        //διαγραφη του boolean has logged in
        file = new File(SHARED_PATH + MainActivity.PREFS_NAME + ".xml");
        SharedPreferences settings = getContext().getSharedPreferences(MainActivity.PREFS_NAME, 0); // 0 - for private mode
        SharedPreferences.Editor editorLog = settings.edit();
        editorLog.clear();
        editorLog.commit();

        file.delete();
    }
}
