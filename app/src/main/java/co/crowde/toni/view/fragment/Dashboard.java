package co.crowde.toni.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.crowde.toni.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {


    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View  rootView =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

}
