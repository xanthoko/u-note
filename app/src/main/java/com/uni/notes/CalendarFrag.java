package com.uni.notes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//δειχνει το ημερολογιο
public class CalendarFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Ημερολόγιο");

        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

}
