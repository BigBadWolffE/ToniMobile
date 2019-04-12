package co.crowde.toni.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.crowde.toni.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Inventory extends Fragment {

    public static TextView tvInventory;


    public Inventory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        tvInventory = view.findViewById(R.id.tvInventory);

        return view;
    }

}
