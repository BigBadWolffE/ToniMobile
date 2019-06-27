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

    private TextView totalTransaksi;
    private TextView totalFrekuensiTransaksi;
    private TextView rataTotalTransaksi;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView1;
    RecyclerView.LayoutManager layoutManager1;
    BarangTerlarisAdapter barangTerlarisAdapter;
    PelangganTerbanyakAdapter pelangganTerbanyakAdapter;
    TextView tvLaporanNamaToko;
    TextView tvLaporanNamaPemilikToko;
    TextView tanggalLaporan1, tanggalLaporan2;
    TextView todays;
    TextView weeks;
    TextView threedays;
    TextView month;
    TextView range;
    EditText tglsebelum;
    EditText tglsesudah;
    CardView todaysbg, weeksbg, threedaysbg, monthbg, rangebg;
    RelativeLayout idDashboardLayoutInfoToko;
    //    TransaksiFragment transaksiFragment;
    Calendar myCalendar;
    Calendar myCalendar1;
    private TextView emptyView;
    private TextView emptyView2;
    List<LaporanAmbilModel> laporanAmbilModels;
    ImageView imgLaporanToko;
    Date currentTime;
    Dialog loadingData, dialogLoad, dataKosong;
    TextView tvLoader;
    String ubahTanggal, total;
    TransactionReport transactionReport;
    ConstraintLayout btnlaporan, btntransaksi;
    private Toolbar toolbar;
    private BackdropContainer backdropContainer;
    static DividerItemDecoration itemDecorator;


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

//        toolbar=(Toolbar)rootView.findViewById(R.id.testToolbar);
//
//        backdropContainer =(BackdropContainer)rootView.findViewById(R.id.backdrop_laporan);
//
//        int height= this.getResources().getDimensionPixelSize(R.dimen.sneek_height);
//        backdropContainer.attachToolbar(toolbar)
//                .dropInterpolator(new LinearInterpolator())
//                .dropHeight(height)
//                .build();
//
//        itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.divider_line_item));
//        barangTerlarisAdapter = new BarangTerlarisAdapter(getActivity());
//        pelangganTerbanyakAdapter = new PelangganTerbanyakAdapter(getActivity());
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.rc_produk_laris);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(barangTerlarisAdapter);
//        recyclerView.addItemDecoration(itemDecorator);
//        recyclerView1 = (RecyclerView)rootView.findViewById(R.id.rc_pembeli_terbanyak);
//        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView1.setAdapter(pelangganTerbanyakAdapter);
//        recyclerView1.addItemDecoration(itemDecorator);
//        emptyView = (TextView)rootView.findViewById(R.id.empty_text1);
//        emptyView2 = (TextView)rootView.findViewById(R.id.empty_text2);
//        btnlaporan = (ConstraintLayout)rootView.findViewById(R.id.tablaporan);
//        btntransaksi = (ConstraintLayout)rootView.findViewById(R.id.tabtransaksi);
//
//        totalTransaksi = (TextView) rootView.findViewById(R.id.totaltransaksilaporan);
//        totalFrekuensiTransaksi = (TextView)rootView.findViewById(R.id.frekuensitransaksilaporan);
//        rataTotalTransaksi = (TextView)rootView.findViewById(R.id.ratapenjualan);
//        tanggalLaporan1 = (TextView)rootView.findViewById(R.id.tanggallaporan1);
//        tanggalLaporan2 = (TextView)rootView.findViewById(R.id.tanggallaporan2);
//        todays  = rootView.findViewById(R.id.btn_transaksi_hari_ini_laporan);
//        threedays = rootView.findViewById(R.id.btn_transaksi_3hari_ini_laporan);
//        weeks = rootView.findViewById(R.id.btn_transaksi_seminggu_laporan);
//        month = rootView.findViewById(R.id.btn_transaksi_sebulan_laporan);
//        range = rootView.findViewById(R.id.btn_range_pilihan_laporan);
//        rangebg = rootView.findViewById(R.id.bg_btn_range_pilihan_laporan);
//        todaysbg = rootView.findViewById(R.id.bg_btn_transaksi_hari_ini_laporan);
//        threedaysbg = rootView.findViewById(R.id.bg_btn_transaksi_3hari_ini_laporan);
//        weeksbg = rootView.findViewById(R.id.bg_btn_transaksi_seminggu_laporan);
//        monthbg = rootView.findViewById(R.id.bg_btn_transaksi_sebulan_laporan);
//
//        tglsebelum = rootView.findViewById(R.id.range_sebelum);
//        tglsesudah = rootView.findViewById(R.id.range_sesudah);
//
//        myCalendar = Calendar.getInstance();
//        final DatePickerDialog.OnDateSetListener dateSebelum = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//                updateLabel();
//            }
//
//            private void updateLabel() {
//                String myFormat = "yyyy-MM-dd"; //In which you need put here
//                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//
//                tglsebelum.setText(sdf.format(myCalendar.getTime()));
//            }
//        };
//        myCalendar1 = Calendar.getInstance();
//        final DatePickerDialog.OnDateSetListener dateSesudah = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
//                myCalendar1.set(Calendar.YEAR, year);
//                myCalendar1.set(Calendar.MONTH, monthOfYear);
//                myCalendar1.set(Calendar.DAY_OF_MONTH,dayOfMonth);
//                updateLabel();
//            }
//
//            private void updateLabel() {
//                String myFormat = "yyyy-MM-dd"; //In which you need put here
//                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//
//                tglsesudah.setText(sdf.format(myCalendar1.getTime()));
//            }
//        };
//        tglsebelum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new DatePickerDialog(getActivity(), dateSebelum, myCalendar.get(
//                        Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//
//
//            }
//        });
//        tglsesudah.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new DatePickerDialog(getActivity(),dateSesudah,myCalendar1.get(
//                        Calendar.YEAR), myCalendar1.get(Calendar.MONTH),myCalendar1.get(Calendar.DAY_OF_MONTH
//                )).show();
//            }
//        });
//
//        btntransaksi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TransactionReport fragment = new TransactionReport();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.mainFrameLayout,fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }
//        });
//
//
//
//        //todays date
//        Date currentDateTimeString = Calendar.getInstance().getTime();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        final String formattedDate = dateFormat.format(currentDateTimeString);
//        Date dateString = Calendar.getInstance().getTime();
//        SimpleDateFormat datedFormat = new SimpleDateFormat("EE, dd MM yyyy");
//        final String todaysDate = datedFormat.format(dateString);
//
//        //3 days
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(currentDateTimeString);
//        calendar.add(Calendar.DAY_OF_YEAR,-3);
//        Date newDate = calendar.getTime();
//        final String date = dateFormat.format(newDate);
//        final String date3 = datedFormat.format(newDate);
//
//        //Week
//        Calendar calendarWeek = Calendar.getInstance();
//        calendarWeek.setTime(currentDateTimeString);
//        calendarWeek.add(Calendar.DAY_OF_YEAR,-7);
//        Date newDateweek = calendarWeek.getTime();
//        final String dateWeek = dateFormat.format(newDateweek);
//        final String date7 = datedFormat.format(newDateweek);
//
//        //Month
//        Calendar calendarMonth= Calendar.getInstance();
//        calendarMonth.setTime(currentDateTimeString);
//        calendarMonth.add(Calendar.DAY_OF_YEAR,-30);
//        Date newDateMonth= calendarMonth.getTime();
//        final String dateMonth = dateFormat.format(newDateMonth);
//        final String date30 = datedFormat.format(newDateMonth);
//
//        todays.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View view) {
//
//                loadDialog();
//
//                todays.setTextColor(getResources().getColor(R.color.colorWhite));
//                todaysbg.setBackgroundColor(getResources().getColor(R.color.colorThemeGreen));
//                threedays.setTextColor(getResources().getColor(R.color.colorBlack));
//                threedaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                weeks.setTextColor(getResources().getColor(R.color.colorBlack));
//                weeksbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                month.setTextColor(getResources().getColor(R.color.colorBlack));
//                monthbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                range.setTextColor(getResources().getColor(R.color.colorBlack));
//                rangebg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                tanggalLaporan1.setText(todaysDate);
//                tanggalLaporan2.setText(todaysDate);
////
//
//                String testUrl = API.LAPORAN_URL + SavePref.readShopId(getActivity()) + "/"+formattedDate+"/"+formattedDate;
//                Log.e("res ", testUrl);
//                StringRequest request = new StringRequest(
//                        Request.Method.GET,
//                        testUrl,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("recap ", response);
//
//                                try {
//
//                                    JSONObject obj = new JSONObject(response);
//                                    String message = obj.getString("message");
//
//                                    boolean status = obj.getBoolean("status");
//
//                                    if (status){
//                                        String data = obj.getString("data");
//                                        JSONObject object2 = new JSONObject(data);
//                                        String recap = object2.getString("recapTransaction");
//                                        JSONObject object3 = new JSONObject(recap);
//                                        DecimalFormat formatter = new DecimalFormat("#,###,###");
//                                        //total
//                                        String total = object3.getString("amount") ;
//                                        if (total.equals("null")){
//                                            total = "0";
//                                        }
//                                        int totalInteger= Integer.parseInt(total);
//                                        String totalfinal = formatter.format(totalInteger);
//                                        //counts
//                                        String counts = object3.getString("count");
//                                        int countsInteger = Integer.parseInt(counts);
//                                        String countsFinal = formatter.format(countsInteger);
//                                        //average
//                                        String average = object3.getString("avarage");
//                                        if (average.equals("null")){
//                                            average="0";
//                                        }
//                                        StringTokenizer tokens = new StringTokenizer(average, ".");
//                                        String first = tokens.nextToken();
//                                        int averageInteger = Integer.parseInt(first);
//                                        String averageFinal = formatter.format(averageInteger);
//                                        //to text view
//                                        totalTransaksi.setText("Rp."+String.valueOf(totalfinal));
//                                        totalFrekuensiTransaksi.setText(String.valueOf(countsFinal));
//                                        rataTotalTransaksi.setText("Rp."+averageFinal);
//
//                                        if(total.equals("0")){
//                                            totalTransaksi.setText("Tidak Ada");
//                                            totalFrekuensiTransaksi.setText("Tidak Ada");
//                                            rataTotalTransaksi.setText("Tidak Ada");
//                                            recyclerView.setVisibility(View.GONE);
//                                            recyclerView1.setVisibility(View.GONE);
//                                            emptyView.setVisibility(View.VISIBLE);
//                                            emptyView.setVisibility(View.VISIBLE);
//                                            dialogNoData();
//                                        }else {
//                                            recyclerView.setVisibility(View.VISIBLE);
//                                            recyclerView1.setVisibility(View.VISIBLE);
//                                            emptyView.setVisibility(View.GONE);
//                                            emptyView2.setVisibility(View.GONE);
//                                        }
//
//                                        JSONArray data1 = object2.getJSONArray("bestSellingProduct");
//                                        List<LaporanBestProductModel> models = new ArrayList<>();
//                                        for (int i = 0; i < data1.length(); i++) {
//                                            LaporanBestProductModel model = new Gson().fromJson(data1.getString(i), LaporanBestProductModel.class);
//                                            models.add(model);
//                                        }
//                                        barangTerlarisAdapter.setModels(models);
//
//                                        JSONArray data2 = object2.getJSONArray("bestCustomer");
//                                        List<LaporanTopCustomer> models1 = new ArrayList<>();
//                                        for (int i = 0; i < data2.length();i++){
//                                            LaporanTopCustomer model1 = new Gson().fromJson(data2.getString(i), LaporanTopCustomer.class);
//                                            models1.add(model1);
//                                        }
//                                        pelangganTerbanyakAdapter.setModels(models1);
//                                        loadingData.dismiss();
//                                    }
//
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        },new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                } ){
//
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Authorization", SavePref.readToken(getActivity()));
//                        return params;
//                    }
//
//                };
//
//                AppController.getInstance().addToRequestQueue(request);
//
//
//            }
//        });
//
//        threedays.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View view) {
//                loadDialog();
//
//                threedays.setTextColor(getResources().getColor(R.color.colorWhite));
//                threedaysbg.setBackgroundColor(getResources().getColor(R.color.colorThemeGreen));
//                todays.setTextColor(getResources().getColor(R.color.colorBlack));
//                todaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                weeks.setTextColor(getResources().getColor(R.color.colorBlack));
//                weeksbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                month.setTextColor(getResources().getColor(R.color.colorBlack));
//                monthbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                range.setTextColor(getResources().getColor(R.color.colorBlack));
//                rangebg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                tanggalLaporan1.setText(date3);
//                tanggalLaporan2.setText(todaysDate);
//
//                String testUrl = API.LAPORAN_URL + SavePref.readShopId(getActivity()) + "/"+date+"/"+formattedDate;
//                Log.e("res ", testUrl);
//                StringRequest request = new StringRequest(
//                        Request.Method.GET,
//                        testUrl,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("recap ", response);
//
//                                try {
//
//                                    JSONObject obj = new JSONObject(response);
//                                    String message = obj.getString("message");
//
//                                    boolean status = obj.getBoolean("status");
//                                    String data = obj.getString("data");
//                                    JSONObject object2 = new JSONObject(data);
//                                    String recap = object2.getString("recapTransaction");
//                                    JSONObject object3 = new JSONObject(recap);
//                                    DecimalFormat formatter = new DecimalFormat("#,###,###");
//                                    //total
//                                    String total = object3.getString("amount") ;
//                                    if (total.equals("null")){
//                                        total = "0";
//                                    }
//                                    int totalInteger= Integer.parseInt(total);
//                                    String totalfinal = formatter.format(totalInteger);
//                                    //counts
//                                    String counts = object3.getString("count");
//                                    int countsInteger = Integer.parseInt(counts);
//                                    String countsFinal = formatter.format(countsInteger);
//                                    //average
//                                    String average = object3.getString("avarage");
//                                    if (average.equals("null")){
//                                        average="0";
//                                    }
//                                    StringTokenizer tokens = new StringTokenizer(average, ".");
//                                    String first = tokens.nextToken();
//                                    int averageInteger = Integer.parseInt(first);
//                                    String averageFinal = formatter.format(averageInteger);
//                                    //to text view
//                                    totalTransaksi.setText("Rp."+String.valueOf(totalfinal));
//                                    totalFrekuensiTransaksi.setText(String.valueOf(countsFinal));
//                                    rataTotalTransaksi.setText("Rp."+averageFinal);
//
//                                    if(total.equals("0")){
//                                        totalTransaksi.setText("Tidak Ada");
//                                        totalFrekuensiTransaksi.setText("Tidak Ada");
//                                        rataTotalTransaksi.setText("Tidak Ada");
//                                        recyclerView.setVisibility(View.GONE);
//                                        recyclerView1.setVisibility(View.GONE);
//                                        emptyView.setVisibility(View.VISIBLE);
//                                        emptyView2.setVisibility(View.VISIBLE);
//                                        dialogNoData();
//                                    }else {
//                                        recyclerView.setVisibility(View.VISIBLE);
//                                        recyclerView1.setVisibility(View.VISIBLE);
//                                        emptyView.setVisibility(View.GONE);
//                                        emptyView2.setVisibility(View.GONE);
//                                    }
//
//                                    JSONArray data1 = object2.getJSONArray("bestSellingProduct");
//                                    List<LaporanBestProductModel> models = new ArrayList<>();
//                                    for (int i = 0; i < data1.length(); i++) {
//                                        LaporanBestProductModel model = new Gson().fromJson(data1.getString(i), LaporanBestProductModel.class);
//                                        models.add(model);
//                                    }
//                                    barangTerlarisAdapter.setModels(models);
//
//                                    JSONArray data2 = object2.getJSONArray("bestCustomer");
//                                    List<LaporanTopCustomer> models1 = new ArrayList<>();
//                                    for (int i = 0; i < data2.length();i++){
//                                        LaporanTopCustomer model1 = new Gson().fromJson(data2.getString(i), LaporanTopCustomer.class);
//                                        models1.add(model1);
//                                    }
//                                    pelangganTerbanyakAdapter.setModels(models1);
//                                    loadingData.dismiss();
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        },new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                } ){
//
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Authorization", SavePref.readToken(getActivity()));
//                        return params;
//                    }
//
//                };
//
//                AppController.getInstance().addToRequestQueue(request);
//
//
//            }
//        });
//
//        weeks.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View view) {
//                loadDialog();
//
//                weeks.setTextColor(getResources().getColor(R.color.colorWhite));
//                weeksbg.setBackgroundColor(getResources().getColor(R.color.colorThemeGreen));
//                threedays.setTextColor(getResources().getColor(R.color.colorBlack));
//                threedaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                todays.setTextColor(getResources().getColor(R.color.colorBlack));
//                todaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                month.setTextColor(getResources().getColor(R.color.colorBlack));
//                monthbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                range.setTextColor(getResources().getColor(R.color.colorBlack));
//                rangebg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                tanggalLaporan1.setText(date7);
//                tanggalLaporan2.setText(todaysDate);
//
//                String testUrl = API.LAPORAN_URL + SavePref.readShopId(getActivity()) + "/"+dateWeek+"/"+formattedDate;
//                Log.e("res ", testUrl);
//                StringRequest request = new StringRequest(
//                        Request.Method.GET,
//                        testUrl,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("recap ", response);
//
//                                try {
//
//                                    JSONObject obj = new JSONObject(response);
//                                    String message = obj.getString("message");
//
//                                    boolean status = obj.getBoolean("status");
//                                    String data = obj.getString("data");
//                                    JSONObject object2 = new JSONObject(data);
//                                    String recap = object2.getString("recapTransaction");
//                                    JSONObject object3 = new JSONObject(recap);
//                                    DecimalFormat formatter = new DecimalFormat("#,###,###");
//                                    //total
//                                    String total = object3.getString("amount") ;
//                                    if (total.equals("null")){
//                                        total = "0";
//                                    }
//                                    int totalInteger= Integer.parseInt(total);
//                                    String totalfinal = formatter.format(totalInteger);
//                                    //counts
//                                    String counts = object3.getString("count");
//                                    int countsInteger = Integer.parseInt(counts);
//                                    String countsFinal = formatter.format(countsInteger);
//                                    //average
//                                    String average = object3.getString("avarage");
//                                    if (average.equals("null")){
//                                        average="0";
//                                    }
//                                    StringTokenizer tokens = new StringTokenizer(average, ".");
//                                    String first = tokens.nextToken();
//                                    int averageInteger = Integer.parseInt(first);
//                                    String averageFinal = formatter.format(averageInteger);
//                                    //to text view
//                                    totalTransaksi.setText("Rp."+String.valueOf(totalfinal));
//                                    totalFrekuensiTransaksi.setText(String.valueOf(countsFinal));
//                                    rataTotalTransaksi.setText("Rp."+averageFinal);
//
//                                    if(total.equals("0")){
//                                        totalTransaksi.setText("Tidak Ada");
//                                        totalFrekuensiTransaksi.setText("Tidak Ada");
//                                        rataTotalTransaksi.setText("Tidak Ada");
//                                        recyclerView.setVisibility(View.GONE);
//                                        recyclerView1.setVisibility(View.GONE);
//                                        emptyView.setVisibility(View.VISIBLE);
//                                        emptyView.setVisibility(View.VISIBLE);
//                                        dialogNoData();
//                                    }else {
//                                        recyclerView.setVisibility(View.VISIBLE);
//                                        recyclerView1.setVisibility(View.VISIBLE);
//                                        emptyView.setVisibility(View.GONE);
//                                        emptyView2.setVisibility(View.GONE);
//                                    }
//
//                                    JSONArray data1 = object2.getJSONArray("bestSellingProduct");
//                                    List<LaporanBestProductModel> models = new ArrayList<>();
//                                    for (int i = 0; i < data1.length(); i++) {
//                                        LaporanBestProductModel model = new Gson().fromJson(data1.getString(i), LaporanBestProductModel.class);
//                                        models.add(model);
//                                    }
//                                    barangTerlarisAdapter.setModels(models);
//
//                                    JSONArray data2 = object2.getJSONArray("bestCustomer");
//                                    List<LaporanTopCustomer> models1 = new ArrayList<>();
//                                    for (int i = 0; i < data2.length();i++){
//                                        LaporanTopCustomer model1 = new Gson().fromJson(data2.getString(i), LaporanTopCustomer.class);
//                                        models1.add(model1);
//                                    }
//                                    pelangganTerbanyakAdapter.setModels(models1);
//                                    loadingData.dismiss();
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        },new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                } ){
//
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Authorization", SavePref.readToken(getActivity()));
//                        return params;
//                    }
//
//                };
//
//                AppController.getInstance().addToRequestQueue(request);
//
//
//            }
//        });
//
//
//        month.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View view) {
//                loadDialog();
//
//                month.setTextColor(getResources().getColor(R.color.colorWhite));
//                monthbg.setBackgroundColor(getResources().getColor(R.color.colorThemeGreen));
//                threedays.setTextColor(getResources().getColor(R.color.colorBlack));
//                threedaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                weeks.setTextColor(getResources().getColor(R.color.colorBlack));
//                weeksbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                todays.setTextColor(getResources().getColor(R.color.colorBlack));
//                todaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                range.setTextColor(getResources().getColor(R.color.colorBlack));
//                rangebg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                tanggalLaporan1.setText(date30);
//                tanggalLaporan2.setText(todaysDate);
//
//                String testUrl = API.LAPORAN_URL + SavePref.readShopId(getActivity()) + "/"+dateMonth+"/"+formattedDate;
//                Log.e("res ", testUrl);
//                StringRequest request = new StringRequest(
//                        Request.Method.GET,
//                        testUrl,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("recap ", response);
//
//                                try {
//
//                                    JSONObject obj = new JSONObject(response);
//                                    String message = obj.getString("message");
//
//                                    boolean status = obj.getBoolean("status");
//                                    String data = obj.getString("data");
//                                    JSONObject object2 = new JSONObject(data);
//                                    String recap = object2.getString("recapTransaction");
//                                    JSONObject object3 = new JSONObject(recap);
//                                    DecimalFormat formatter = new DecimalFormat("#,###,###");
//                                    //total
//                                    String total = object3.getString("amount") ;
//                                    if (total.equals("null")){
//                                        total = "0";
//                                    }
//                                    int totalInteger= Integer.parseInt(total);
//                                    String totalfinal = formatter.format(totalInteger);
//                                    //counts
//                                    String counts = object3.getString("count");
//                                    int countsInteger = Integer.parseInt(counts);
//                                    String countsFinal = formatter.format(countsInteger);
//                                    //average
//                                    String average = object3.getString("avarage");
//                                    if (average.equals("null")){
//                                        average="0";
//                                    }
//                                    StringTokenizer tokens = new StringTokenizer(average, ".");
//                                    String first = tokens.nextToken();
//                                    int averageInteger = Integer.parseInt(first);
//                                    String averageFinal = formatter.format(averageInteger);
//                                    //to text view
//                                    totalTransaksi.setText("Rp."+String.valueOf(totalfinal));
//                                    totalFrekuensiTransaksi.setText(String.valueOf(countsFinal));
//                                    rataTotalTransaksi.setText("Rp."+averageFinal);
//
//                                    if(total.equals("0")){
//                                        totalTransaksi.setText("Tidak Ada");
//                                        totalFrekuensiTransaksi.setText("Tidak Ada");
//                                        rataTotalTransaksi.setText("Tidak Ada");
//                                        recyclerView.setVisibility(View.GONE);
//                                        recyclerView1.setVisibility(View.GONE);
//                                        emptyView.setVisibility(View.VISIBLE);
//                                        emptyView.setVisibility(View.VISIBLE);
//                                        dialogNoData();
//                                    }else {
//                                        recyclerView.setVisibility(View.VISIBLE);
//                                        recyclerView1.setVisibility(View.VISIBLE);
//                                        emptyView.setVisibility(View.GONE);
//                                        emptyView2.setVisibility(View.GONE);
//                                    }
//
//                                    JSONArray data1 = object2.getJSONArray("bestSellingProduct");
//                                    List<LaporanBestProductModel> models = new ArrayList<>();
//                                    for (int i = 0; i < data1.length(); i++) {
//                                        LaporanBestProductModel model = new Gson().fromJson(data1.getString(i), LaporanBestProductModel.class);
//                                        models.add(model);
//                                    }
//                                    barangTerlarisAdapter.setModels(models);
//
//                                    JSONArray data2 = object2.getJSONArray("bestCustomer");
//                                    List<LaporanTopCustomer> models1 = new ArrayList<>();
//                                    for (int i = 0; i < data2.length();i++){
//                                        LaporanTopCustomer model1 = new Gson().fromJson(data2.getString(i), LaporanTopCustomer.class);
//                                        models1.add(model1);
//                                    }
//                                    pelangganTerbanyakAdapter.setModels(models1);
//
//                                    loadingData.dismiss();
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        },new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                } ){
//
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Authorization", SavePref.readToken(getActivity()));
//                        return params;
//                    }
//
//                };
//
//                AppController.getInstance().addToRequestQueue(request);
//
//
//            }
//        });
//
//        range.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View view) {
//                loadDialog();
//
//                range.setTextColor(getResources().getColor(R.color.colorWhite));
//                rangebg.setBackgroundColor(getResources().getColor(R.color.colorThemeGreen));
//                threedays.setTextColor(getResources().getColor(R.color.colorBlack));
//                threedaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                todays.setTextColor(getResources().getColor(R.color.colorBlack));
//                todaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                weeks.setTextColor(getResources().getColor(R.color.colorBlack));
//                weeksbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                month.setTextColor(getResources().getColor(R.color.colorBlack));
//                monthbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                String myFormat = "dd MMM yyyy"; //In which you need put here
//                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//                String range1 = sdf.format(myCalendar.getTime());
//                String range2 = sdf.format(myCalendar1.getTime());
//                tanggalLaporan1.setText(range1);
//                tanggalLaporan2.setText(range2);
//
//                String testUrl = API.LAPORAN_URL + SavePref.readShopId(getActivity()) + "/"+tglsebelum.getText().toString()+"/"+tglsesudah.getText().toString();;
//                Log.e("res ", testUrl);
//                StringRequest request = new StringRequest(
//                        Request.Method.GET,
//                        testUrl,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                Log.d("recap ", response);
//
//                                try {
//
//                                    JSONObject obj = new JSONObject(response);
//                                    String message = obj.getString("message");
//
//                                    boolean status = obj.getBoolean("status");
//
//                                    if(status){
//                                        String data = obj.getString("data");
//                                        JSONObject object2 = new JSONObject(data);
//                                        String recap = object2.getString("recapTransaction");
//                                        JSONObject object3 = new JSONObject(recap);
//                                        DecimalFormat formatter = new DecimalFormat("#,###,###");
//                                        //total
//                                        String total = object3.getString("amount") ;
//                                        if (total.equals("null")){
//                                            total = "0";
//                                        }
//                                        int totalInteger= Integer.parseInt(total);
//                                        String totalfinal = formatter.format(totalInteger);
//                                        //counts
//                                        String counts = object3.getString("count");
//                                        int countsInteger = Integer.parseInt(counts);
//                                        String countsFinal = formatter.format(countsInteger);
//                                        //average
//                                        String average = object3.getString("avarage");
//                                        if (average.equals("null")){
//                                            average="0";
//                                        }
//                                        StringTokenizer tokens = new StringTokenizer(average, ".");
//                                        String first = tokens.nextToken();
//                                        int averageInteger = Integer.parseInt(first);
//                                        String averageFinal = formatter.format(averageInteger);
//                                        //to text view
//                                        totalTransaksi.setText("Rp."+String.valueOf(totalfinal));
//                                        totalFrekuensiTransaksi.setText(String.valueOf(countsFinal));
//                                        rataTotalTransaksi.setText("Rp."+averageFinal);
//
//                                        if(total.equals("0")){
//                                            totalTransaksi.setText("Tidak Ada");
//                                            totalFrekuensiTransaksi.setText("Tidak Ada");
//                                            rataTotalTransaksi.setText("Tidak Ada");
//                                            recyclerView.setVisibility(View.GONE);
//                                            recyclerView1.setVisibility(View.GONE);
//                                            emptyView.setVisibility(View.VISIBLE);
//                                            emptyView2.setVisibility(View.VISIBLE);
//                                            dialogNoData();
//                                        }else {
//                                            recyclerView.setVisibility(View.VISIBLE);
//                                            recyclerView1.setVisibility(View.VISIBLE);
//                                            emptyView.setVisibility(View.GONE);
//                                            emptyView2.setVisibility(View.GONE);
//                                        }
//
//                                        JSONArray data1 = object2.getJSONArray("bestSellingProduct");
//                                        List<LaporanBestProductModel> models = new ArrayList<>();
//                                        for (int i = 0; i < data1.length(); i++) {
//                                            LaporanBestProductModel model = new Gson().fromJson(data1.getString(i), LaporanBestProductModel.class);
//                                            models.add(model);
//                                        }
//                                        barangTerlarisAdapter.setModels(models);
//
//                                        JSONArray data2 = object2.getJSONArray("bestCustomer");
//                                        List<LaporanTopCustomer> models1 = new ArrayList<>();
//                                        for (int i = 0; i < data2.length();i++){
//                                            LaporanTopCustomer model1 = new Gson().fromJson(data2.getString(i), LaporanTopCustomer.class);
//                                            models1.add(model1);
//                                        }
//                                        pelangganTerbanyakAdapter.setModels(models1);
//
//                                        loadingData.dismiss();
//
//
//                                    }
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        },new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                } ){
//
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Authorization", SavePref.readToken(getActivity()));
//                        return params;
//                    }
//
//                };
//
//                AppController.getInstance().addToRequestQueue(request);
//
//
//            }
//        });
//
//
//
//
//        getData();
//
//        return rootView;
//    }
//
//    private void getData (){
//
//        //todays date
//        Date currentDateTimeString = Calendar.getInstance().getTime();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        final String formattedDate = dateFormat.format(currentDateTimeString);
//        Date dateString = Calendar.getInstance().getTime();
//        SimpleDateFormat datedFormat = new SimpleDateFormat("EE, dd MM yyyy");
//        final String todaysDate = datedFormat.format(dateString);
//
//        tanggalLaporan1.setText(todaysDate);
//        tanggalLaporan2.setText(todaysDate);
//        loadDialog();
//        String testUrl = API.LAPORAN_URL + SavePref.readShopId(getActivity()) + "/"+formattedDate+"/"+formattedDate;
//        Log.e("res ", testUrl);
//        StringRequest request = new StringRequest(
//                Request.Method.GET,
//                testUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("recap ", response);
//
//                        try {
//
//                            JSONObject obj = new JSONObject(response);
//                            String message = obj.getString("message");
//
//                            boolean status = obj.getBoolean("status");
//
//                            if (status){
//                                String data = obj.getString("data");
//                                JSONObject object2 = new JSONObject(data);
//                                String recap = object2.getString("recapTransaction");
//                                JSONObject object3 = new JSONObject(recap);
//                                DecimalFormat formatter = new DecimalFormat("#,###,###");
//                                //total
//                                String total = object3.getString("amount") ;
//                                if (total.equals("null")){
//                                    total = "0";
//                                }
//                                int totalInteger= Integer.parseInt(total);
//                                String totalfinal = formatter.format(totalInteger);
//                                //counts
//                                String counts = object3.getString("count");
//                                int countsInteger = Integer.parseInt(counts);
//                                String countsFinal = formatter.format(countsInteger);
//                                //average
//                                String average = object3.getString("avarage");
//                                if (average.equals("null")){
//                                    average="0";
//                                }
//                                StringTokenizer tokens = new StringTokenizer(average, ".");
//                                String first = tokens.nextToken();
//                                int averageInteger = Integer.parseInt(first);
//                                String averageFinal = formatter.format(averageInteger);
//                                //to text view
//                                totalTransaksi.setText("Rp."+String.valueOf(totalfinal));
//                                totalFrekuensiTransaksi.setText(String.valueOf(countsFinal));
//                                rataTotalTransaksi.setText("Rp."+averageFinal);
//
//                                if(total.equals("0")){
//                                    totalTransaksi.setText("Tidak Ada");
//                                    totalFrekuensiTransaksi.setText("Tidak Ada");
//                                    rataTotalTransaksi.setText("Tidak Ada");
//                                    recyclerView.setVisibility(View.GONE);
//                                    recyclerView1.setVisibility(View.GONE);
//                                    emptyView.setVisibility(View.VISIBLE);
//                                    emptyView.setVisibility(View.VISIBLE);
//                                    dialogNoData();
//                                }else {
//                                    recyclerView.setVisibility(View.VISIBLE);
//                                    recyclerView1.setVisibility(View.VISIBLE);
//                                    emptyView.setVisibility(View.GONE);
//                                    emptyView2.setVisibility(View.GONE);
//                                }
//
//                                JSONArray data1 = object2.getJSONArray("bestSellingProduct");
//                                List<LaporanBestProductModel> models = new ArrayList<>();
//                                for (int i = 0; i < data1.length(); i++) {
//                                    LaporanBestProductModel model = new Gson().fromJson(data1.getString(i), LaporanBestProductModel.class);
//                                    models.add(model);
//                                }
//                                barangTerlarisAdapter.setModels(models);
//
//                                JSONArray data2 = object2.getJSONArray("bestCustomer");
//                                List<LaporanTopCustomer> models1 = new ArrayList<>();
//                                for (int i = 0; i < data2.length();i++){
//                                    LaporanTopCustomer model1 = new Gson().fromJson(data2.getString(i), LaporanTopCustomer.class);
//                                    models1.add(model1);
//                                }
//                                pelangganTerbanyakAdapter.setModels(models1);
//                                loadingData.dismiss();
//                            }
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        } ){
//
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", SavePref.readToken(getActivity()));
//                return params;
//            }
//
//        };
//
//        AppController.getInstance().addToRequestQueue(request);
//
//
//
//    }
//
//    private void loadDialog() {
//        final LayoutInflater inflater = LayoutInflater.from(getActivity());
//        final View dialogView = inflater.inflate(R.layout.layout_loading_data_popup, null);
//
//        loadingData = new Dialog(getActivity());
//        loadingData.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        loadingData.setContentView(dialogView);
//        loadingData.setCanceledOnTouchOutside(true);
//
//
//
//        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
//        int dialogWidth = (int)(displayMetrics.widthPixels * 0.8);
//        int dialogHeight = (int)(displayMetrics.heightPixels * 0.2);
//        loadingData.getWindow().setLayout(dialogWidth, dialogHeight);
//
//        loadingData.show();
//        loadingData.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        loadingData.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//
//
//    }
//
//
//    private void getCurrentDate() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm | EEE, dd MMMM yyyy", Locale.UK);
//        currentTime = new Date();
//        ubahTanggal = dateFormat.format(currentTime);
//    }
//
//    private void dialogNoData() {
//
//        final LayoutInflater inflater = LayoutInflater.from(getActivity());
//        final View dialogView = inflater.inflate(R.layout.layout_no_data, null);
//        TextView tutup = (TextView) dialogView.findViewById(R.id.btn_nodata_dismiss);
//
//        dataKosong = new Dialog(getActivity());
//        dataKosong.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dataKosong.setContentView(dialogView);
//        dataKosong.setCanceledOnTouchOutside(true);
//
//
//
//        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
//        int dialogWidth = (int)(displayMetrics.widthPixels * 0.6);
//        int dialogHeight = (int)(displayMetrics.heightPixels * 0.7);
//        dataKosong.getWindow().setLayout(dialogWidth, dialogHeight);
//
//        dataKosong.show();
//        dataKosong.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        dataKosong.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//
//        tutup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dataKosong.dismiss();
//                loadingData.dismiss();
//            }
//        });
//
//
//
//    }
//
//}
