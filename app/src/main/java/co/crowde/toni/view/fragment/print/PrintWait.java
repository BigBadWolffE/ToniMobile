package co.crowde.toni.view.fragment.print;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.crowde.toni.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrintWait extends Fragment {


    public PrintWait() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view =  inflater.inflate(R.layout.fragment_print_wait, container, false);

        return view;
    }

}