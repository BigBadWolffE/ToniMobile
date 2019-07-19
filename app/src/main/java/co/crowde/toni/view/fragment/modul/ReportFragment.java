package co.crowde.toni.view.fragment.modul;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.evolve.backdroplibrary.BackdropContainer;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import co.crowde.toni.R;
import co.crowde.toni.adapter.BarangTerlarisAdapter;
import co.crowde.toni.adapter.PelangganTerbanyakAdapter;
import co.crowde.toni.adapter.menu.ReportTabAdapter;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.helper.volley.AppController;
import co.crowde.toni.model.LaporanAmbilModel;
import co.crowde.toni.model.LaporanBestProductModel;
import co.crowde.toni.model.LaporanTopCustomer;
import co.crowde.toni.network.API;
import co.crowde.toni.view.fragment.transaction.TransactionReport;

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
