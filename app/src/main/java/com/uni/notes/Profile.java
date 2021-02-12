package com.uni.notes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

// το προφιλ του χρηστη . Εχει τα στοιχεια και τους getters, setters
 class Profile implements Serializable{
    protected String username;
    private String university;
    private String semester;
    private String owed;
    private String attending;
    public int c=0;
    ArrayList<String> subject = new ArrayList<String>(9);
    ArrayList<Integer> day = new ArrayList<Integer>(9);
    ArrayList<Integer> day2 = new ArrayList<Integer>(9);
    HashMap<Integer , String> week = new HashMap<Integer, String>(5);
    String[][] subsOfWeek = new String[5][subject.size()];
    ArrayList<Integer> code1 = new ArrayList<Integer>(9);
    ArrayList<Integer> code2 = new ArrayList<Integer>(9);

    Profile(String username, String university, String semester , String owed , String attending) {
        this.username=username;
        this.university=university;
        this.semester=semester;
        this.owed=owed;
        this.attending=attending;

        week.put(0 , "Δευτέρα");
        week.put(1 , "Τρίτη");
        week.put(2 , "Τετάρτη");
        week.put(3 , "Πέμπτη");
        week.put(4 , "Παρασκευή");
    }

    public void setUsername(String u) {username=u;}
    void setUniversity(String uni) {university=uni;}
    void setSemester(String s) {semester = s;}
    void setOwed(String o) {owed=o;}
    void setSubject(String[] mathimata) {subject = new ArrayList<String>(Arrays.asList(mathimata));}
    public String getUsername() {return username;}
    String getUniversity() {return university;}
    String getSemester() {return semester;}
    String getAttending() {return attending;}
    int getSize() {return subject.size();}
    String getOwed() {return owed;}

    void generateWeekSchedule(){
        for(int i=0 ; i<5 ; i++){
            String[] d = new String[subject.size()];
            subsOfWeek[i] = d;
            for(int j=0 ; j<subject.size() ; j++){
                if(day.get(j) == i || day2.get(j) == i){
                    d[j] = subject.get(j);
                }
                else{
                    d[j] = "";
                }
            }
        }
    }
}

