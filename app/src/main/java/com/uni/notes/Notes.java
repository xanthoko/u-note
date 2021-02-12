package com.uni.notes;

import java.util.ArrayList;
import java.util.Iterator;

//οι ημεροηνιες σημειωσεων για το καθε μαθημα.
 class Notes{

    private ArrayList<String> list =new ArrayList<String>();

    int getNum_dates() {return list.size();}  //παρνει το μεγεθος της λιστας ημερομηνιων
    void addNote(String date ) {list.add(date);} //αυξανει την λιστα κατα 1
    String getDate (int pos){return list.get(pos);}  //επιστρεφει την αντιστοιχη ημερομηνια
    //ψαχνει να βρει την ημερομηνια στην λιστα και την αφαιρει
    public void delete (String thing) {
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
            String value = iterator.next();
            if (value.equals(thing)) {
                iterator.remove();
            }
        }
    }
}

