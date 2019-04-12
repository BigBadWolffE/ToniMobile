package co.crowde.toni.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import co.crowde.toni.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    public static TextView tvDashboard;


    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View  view =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvDashboard = view.findViewById(R.id.tvDashboard);

        return view;
    }

}
