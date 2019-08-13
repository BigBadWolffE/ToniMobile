package co.crowde.toni.view.fragment.modul;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import co.crowde.toni.R;
import co.crowde.toni.adapter.menu.ReportTabAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment
        implements TabLayout.BaseOnTabSelectedListener {

    TabLayout tabLayout;
    ViewPager viewPager;

    public static ProgressDialog progressDialog;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_report, container, false);

        progressDialog = new ProgressDialog(getActivity());

        //Initializing the tablayout
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) rootView.findViewById(R.id.pagerOrder);

        //Creating our pager adapter
        ReportTabAdapter adapter = new ReportTabAdapter(getFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);

        return rootView;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
