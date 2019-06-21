package co.crowde.toni.view.fragment.transaction;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.evolve.backdroplibrary.BackdropContainer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import co.crowde.toni.R;
import co.crowde.toni.adapter.TransaksiWaktuPelangganAdapter;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.helper.volley.AppController;
import co.crowde.toni.model.TransaksiModel;
import co.crowde.toni.network.API;
import co.crowde.toni.view.fragment.modul.ReportFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {
    private Toolbar toolbar;
    private BackdropContainer backdropContainer;
    ConstraintLayout btnlaporan,btntransaksi;
    RecyclerView recyclerView;
    TransaksiWaktuPelangganAdapter transaksiWaktuPelangganAdapter;
    LinearLayoutManager layoutManager;
    RecyclerView detailRecycler;
    RecyclerView.LayoutManager detailLayoutManager;
//    DetailTransaksiWaktuPelangganAdapter detailTransaksiWaktuPelangganAdapter;
    Spinner spinnertransaksi;
    TextView todays;
    TextView weeks;
    TextView threedays;
    TextView month;
    TextView range;
    EditText tglsebelum;
    EditText tglsesudah;
    TextView tvTransaksiNamaToko;
    TextView tvTransaksiNamaPemilikToko;
    CardView todaysbg,weeksbg, threedaysbg, monthbg,rangebg;
    Calendar myCalendar;
    Calendar myCalendar1;
    RelativeLayout emptyText;
    List<TransaksiModel> transaksiModelList;
    ImageView imgLaporanToko;
    Date currentTime;
    Dialog loadingData,dataKosong,dialogLoad;
    TextView tvLoader;
    String ubahTanggal;
    TextView tanggalLaporan1,tanggalLaporan2;
    String day1,day2,day3,dayweek,daymonth,dayrange1,dayrange2;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private static int page = 0;
    private static int pageTransaksi;

    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_transaction, container, false);

        toolbar=(Toolbar)rootView.findViewById(R.id.testToolbarTransaksi);

        backdropContainer =(BackdropContainer)rootView.findViewById(R.id.backdrop_transaksi);

        int height= this.getResources().getDimensionPixelSize(R.dimen.sneek_height);
        backdropContainer.attachToolbar(toolbar)
                .dropInterpolator(new LinearInterpolator())
                .dropHeight(height)
                .build();

        todays  = rootView.findViewById(R.id.btn_transaksi_hari_ini);
        threedays = rootView.findViewById(R.id.btn_transaksi_3hari_ini);
        weeks = rootView.findViewById(R.id.btn_transaksi_seminggu);
        month = rootView.findViewById(R.id.btn_transaksi_sebulan);
        todaysbg = rootView.findViewById(R.id.bg_btn_transaksi_hari_ini);
        threedaysbg = rootView.findViewById(R.id.bg_btn_transaksi_3hari_ini);
        weeksbg = rootView.findViewById(R.id.bg_btn_transaksi_seminggu);
        monthbg = rootView.findViewById(R.id.bg_btn_transaksi_sebulan);
        range = rootView.findViewById(R.id.btn_range_pilihan_transaksi);
        rangebg = rootView.findViewById(R.id.bg_btn_range_pilihan_transaksi);
        tanggalLaporan1 = (TextView)rootView.findViewById(R.id.tanggallaporan1);
        tanggalLaporan2 = (TextView)rootView.findViewById(R.id.tanggallaporan2);

        recyclerView = rootView.findViewById(R.id.rc_daftar_transaksi);
//        pageTransaksi = 1;
//        initAdapterCustomerMonths(getActivity());
//        getDatamonth(pageTransaksi);
//        initScrollListenerCustomerMonths(getActivity());
        transaksiModelList = new ArrayList<>();
        transaksiWaktuPelangganAdapter = new TransaksiWaktuPelangganAdapter(getActivity(),transaksiModelList,getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transaksiWaktuPelangganAdapter);

        btnlaporan = (ConstraintLayout)rootView.findViewById(R.id.tablaporan);


        btnlaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportFragment fragment = new ReportFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFrameLayout,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        tglsebelum = rootView.findViewById(R.id.range_sebelum_transaksi);
        tglsesudah = rootView.findViewById(R.id.range_sesudah_transaksi);

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateSebelum = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                tglsebelum.setText(sdf.format(myCalendar.getTime()));
            }
        };
        myCalendar1 = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateSesudah = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                tglsesudah.setText(sdf.format(myCalendar1.getTime()));
            }
        };
        tglsebelum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), dateSebelum, myCalendar.get(
                        Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
        tglsesudah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),dateSesudah,myCalendar1.get(
                        Calendar.YEAR), myCalendar1.get(Calendar.MONTH),myCalendar1.get(Calendar.DAY_OF_MONTH
                )).show();
            }
        });


        //todays date
        Date currentDateTimeString = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String formattedDate = dateFormat.format(currentDateTimeString);
        Date dateString = Calendar.getInstance().getTime();
        SimpleDateFormat datedFormat = new SimpleDateFormat(" dd MMM yyyy");
        final String todaysDate = datedFormat.format(dateString);

        //3 days
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDateTimeString);
        calendar.add(Calendar.DAY_OF_YEAR,-3);
        Date newDate = calendar.getTime();
        final String date = dateFormat.format(newDate);
        final String date3 = datedFormat.format(newDate);

        //Week
        Calendar calendarWeek = Calendar.getInstance();
        calendarWeek.setTime(currentDateTimeString);
        calendarWeek.add(Calendar.DAY_OF_YEAR,-7);
        Date newDateweek = calendarWeek.getTime();
        final String dateWeek = dateFormat.format(newDateweek);
        final String date7 = datedFormat.format(newDateweek);

        //Month
        Calendar calendarMonth= Calendar.getInstance();
        calendarMonth.setTime(currentDateTimeString);
        calendarMonth.add(Calendar.DAY_OF_YEAR,-30);
        Date newDateMonth= calendarMonth.getTime();
        final String dateMonth = dateFormat.format(newDateMonth);
        final String date30 = datedFormat.format(newDateMonth);

        day1 = formattedDate;
        day3 = date;
        dayweek = dateWeek;
        daymonth= dateMonth;
        dayrange1 =tglsebelum.getText().toString();
        dayrange2 = tglsesudah.getText().toString();

        todays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadDialog();
                todays.setTextColor(getResources().getColor(R.color.colorWhite));
                todaysbg.setBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
                threedays.setTextColor(getResources().getColor(R.color.colorBlack));
                threedaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                weeks.setTextColor(getResources().getColor(R.color.colorBlack));
                weeksbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                month.setTextColor(getResources().getColor(R.color.colorBlack));
                monthbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                range.setTextColor(getResources().getColor(R.color.colorBlack));
                rangebg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                tanggalLaporan1.setText(todaysDate);
                tanggalLaporan2.setText(todaysDate);
                transaksiWaktuPelangganAdapter.clearModels();
                if (transaksiWaktuPelangganAdapter != null){
                    emptyText.setVisibility(View.GONE);
                }else{
                    emptyText.setVisibility(View.VISIBLE);
                }
//                getDataOneDay(pageTransaksi);

                pageTransaksi=1;
                initAdapterCustomer(getActivity());
                getDataOneDay(pageTransaksi);
                initScrollListenerCustomer(getActivity());

            }
        });

        threedays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialog();
                threedays.setTextColor(getResources().getColor(R.color.colorWhite));
                threedaysbg.setBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
                todays.setTextColor(getResources().getColor(R.color.colorBlack));
                todaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                weeks.setTextColor(getResources().getColor(R.color.colorBlack));
                weeksbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                month.setTextColor(getResources().getColor(R.color.colorBlack));
                monthbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                range.setTextColor(getResources().getColor(R.color.colorBlack));
                rangebg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                Toast.makeText(getActivity(), "Mohon Tunggu", Toast.LENGTH_SHORT).show();
                transaksiWaktuPelangganAdapter.clearModels();
                tanggalLaporan1.setText(date3);
                tanggalLaporan2.setText(todaysDate);
                if (transaksiWaktuPelangganAdapter != null){
                    emptyText.setVisibility(View.GONE);
                }else{
                    emptyText.setVisibility(View.VISIBLE);
                }
                initAdapterCustomer3days(getActivity());
                getData3day(pageTransaksi);
                initScrollListenerCustomer3days(getActivity());
            }
        });

        weeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialog();
                weeks.setTextColor(getResources().getColor(R.color.colorWhite));
                weeksbg.setBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
                threedays.setTextColor(getResources().getColor(R.color.colorBlack));
                threedaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                todays.setTextColor(getResources().getColor(R.color.colorBlack));
                todaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                month.setTextColor(getResources().getColor(R.color.colorBlack));
                monthbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                range.setTextColor(getResources().getColor(R.color.colorBlack));
                rangebg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                Toast.makeText(getActivity(), "Mohon Tunggu", Toast.LENGTH_SHORT).show();
                transaksiWaktuPelangganAdapter.clearModels();
                tanggalLaporan1.setText(date7);
                tanggalLaporan2.setText(todaysDate);
                if (transaksiWaktuPelangganAdapter != null){
                    emptyText.setVisibility(View.GONE);
                }else{
                    emptyText.setVisibility(View.VISIBLE);
                }
                initAdapterCustomerWeeks(getActivity());
                getDataweek(pageTransaksi);
                initScrollListenerCustomerWeeks(getActivity());

            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialog();
//                hilanginFragment();
                month.setTextColor(getResources().getColor(R.color.colorWhite));
                monthbg.setBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
                threedays.setTextColor(getResources().getColor(R.color.colorBlack));
                threedaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                weeks.setTextColor(getResources().getColor(R.color.colorBlack));
                weeksbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                todays.setTextColor(getResources().getColor(R.color.colorBlack));
                todaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                range.setTextColor(getResources().getColor(R.color.colorBlack));
                rangebg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                Toast.makeText(getActivity(), "Mohon Tunggu", Toast.LENGTH_SHORT).show();
//                transaksiWaktuPelangganAdapter.clearModels();
                tanggalLaporan1.setText(date30);
                tanggalLaporan2.setText(todaysDate);
                if (transaksiWaktuPelangganAdapter != null){
                    emptyText.setVisibility(View.GONE);
                }else{
                    emptyText.setVisibility(View.VISIBLE);
                }

                pageTransaksi=1;
                initAdapterCustomerMonths(getActivity());
                getDatamonth(pageTransaksi);
                initScrollListenerCustomerMonths(getActivity());
            }
        });

        range.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialog();
                range.setTextColor(getResources().getColor(R.color.colorWhite));
                rangebg.setBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
                threedays.setTextColor(getResources().getColor(R.color.colorBlack));
                threedaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                todays.setTextColor(getResources().getColor(R.color.colorBlack));
                todaysbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                weeks.setTextColor(getResources().getColor(R.color.colorBlack));
                weeksbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                month.setTextColor(getResources().getColor(R.color.colorBlack));
                monthbg.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                String myFormat = "dd MMM yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String range1 = sdf.format(myCalendar.getTime());
                String range2 = sdf.format(myCalendar1.getTime());
                tanggalLaporan1.setText(range1);
                tanggalLaporan2.setText(range2);
                Toast.makeText(getActivity(), "Mohon Tunggu", Toast.LENGTH_SHORT).show();
                transaksiWaktuPelangganAdapter.clearModels();
                if (transaksiWaktuPelangganAdapter != null){
                    emptyText.setVisibility(View.GONE);
                }else{
                    emptyText.setVisibility(View.VISIBLE);
                }
                initAdapterCustomerRange(getActivity());
                getDataRange(pageTransaksi);
                initScrollListenerCustomerRange(getActivity());
            }
        });


        return rootView;
    }



    private void getDataOneDay(int page){
        String testUrl = API.TRANSAKSI_URL2+SavePref.readShopId(getActivity())+"?limit=5&page="+page+"&type=today";
        Log.e("res ", testUrl);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                testUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            boolean status = obj.getBoolean("status");
                            JSONArray data = obj.getJSONArray("data");
                            String message = obj.getString("message");
                            if(status){
                                List<TransaksiModel> models = new Gson().fromJson(data.toString(),new TypeToken<List<TransaksiModel>>(){}.getType());
                                updateDataCustomer(models);
                            }else if (message.equals("Data transaksi tidak ditemukan!")){
                                dialogNoData();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }

                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", SavePref.readToken(getActivity()));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(request);


    }

    private void initAdapterCustomer(Activity activity) {
        transaksiModelList = new ArrayList<>();
        transaksiWaktuPelangganAdapter = new TransaksiWaktuPelangganAdapter(getActivity(), transaksiModelList, getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transaksiWaktuPelangganAdapter);
    }

    public void updateDataCustomer(List<TransaksiModel> transaksiModels) {
        if (transaksiModelList.size() != 0){
            transaksiModelList.remove(transaksiModelList.size() - 1);
            int scrollPosition = transaksiModelList.size();
            transaksiWaktuPelangganAdapter.notifyItemRemoved(scrollPosition);
        }

        transaksiModelList.addAll(transaksiModels);
        transaksiWaktuPelangganAdapter.notifyDataSetChanged();
        isLoadingCustomer = false;
    }

    boolean isLoadingCustomer = false;

    private void initScrollListenerCustomer(final Activity activity) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoadingCustomer) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == transaksiModelList.size() - 1) {
                        //bottom of list!
                        loadMoreCustomer(activity);
                        isLoadingCustomer = true;
                    }
                }
            }
        });
    }

    private void loadMoreCustomer(Activity activity) {
        transaksiModelList.add(null);
        transaksiWaktuPelangganAdapter.notifyItemInserted(transaksiModelList.size() - 1);

        pageTransaksi = pageTransaksi+ 1;
        getDataOneDay(pageTransaksi);

    }

    private void getData3day(int page){


        String testUrl = API.TRANSAKSI_URL2+SavePref.readShopId(getActivity())+"?limit=5&page="+page+"&type=three-days";
        Log.e("res ", testUrl);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                testUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ;
                        try {
                            JSONObject obj = new JSONObject(response);

                            boolean status = obj.getBoolean("status");
                            JSONArray data = obj.getJSONArray("data");
                            String message = obj.getString("message");

                            if(status){

                                List<TransaksiModel> models = new Gson().fromJson(data.toString(),new TypeToken<List<TransaksiModel>>(){}.getType());

//                                for (int i = 0; i < data.length(); i++) {
//                                    TransaksiModel model = new Gson().fromJson(data.getString(i), TransaksiModel.class);
//                                    models.add(model);
//                                }

//                                transaksiWaktuPelangganAdapter.setModels(models);
                                updateDataCustomer3days(models);

                                loadingData.dismiss();

                            }else if(message.equals("Data transaksi tidak ditemukan!")){
                                dialogNoData();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }

                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", SavePref.readToken(getActivity()));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getDataweek(int page){
        String testUrl = API.TRANSAKSI_URL+SavePref.readShopId(getActivity())+"/"+dayweek+"/"+day1+"?limit=5&page="+page;
        Log.e("res ", testUrl);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                testUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject obj = new JSONObject(response);

                            boolean status = obj.getBoolean("status");
                            JSONArray data = obj.getJSONArray("data");
                            String message = obj.getString("message");

                            if(status){
                                List<TransaksiModel> models = new Gson().fromJson(data.toString(),new TypeToken<List<TransaksiModel>>(){}.getType());

//                                for (int i = 0; i < data.length(); i++) {
//                                    TransaksiModel model = new Gson().fromJson(data.getString(i), TransaksiModel.class);
//                                    models.add(model);
//                                }

//                                transaksiWaktuPelangganAdapter.setModels(models);
                                updateDataCustomerWeeks(models);

                                loadingData.dismiss();



                            }else if (message.equals("Data transaksi tidak ditemukan!")){
                                dialogNoData();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }

                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", SavePref.readToken(getActivity()));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getDatamonth(int page){
        String testUrl = API.TRANSAKSI_URL2+SavePref.readShopId(getActivity())+"?limit=5&page="+page+"&type=month";
        Log.e("res ", testUrl);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                testUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject  obj = new JSONObject(response);
                            boolean status = obj.getBoolean("status");
                            JSONArray data = obj.getJSONArray("data");
                            String message = obj.getString("message");
                            if(status){
                                List<TransaksiModel> models = new Gson().fromJson(data.toString(),new TypeToken<List<TransaksiModel>>(){}.getType());
                                updateDataCustomerMonths(models);
                                Log.e("Response", new Gson().toJson(models));
                                loadingData.dismiss();
                            }else if (message.equals("Fetching data transaksi berhasil")){
                                dialogNoData();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", SavePref.readToken(getActivity()));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    private void initAdapterCustomerMonths(Activity activity) {
        transaksiModelList = new ArrayList<>();
        transaksiWaktuPelangganAdapter = new TransaksiWaktuPelangganAdapter(activity, transaksiModelList, activity);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(transaksiWaktuPelangganAdapter);
    }

    public void updateDataCustomerMonths(List<TransaksiModel> transaksiModels) {
        if (transaksiModelList.size() != 0){
            transaksiModelList.remove(transaksiModelList.size() - 1);
            int scrollPosition = transaksiModelList.size();
            transaksiWaktuPelangganAdapter.notifyItemRemoved(scrollPosition);
        }
        transaksiModelList.addAll(transaksiModels);
        transaksiWaktuPelangganAdapter.notifyDataSetChanged();
        isLoadingCustomerMonths = false;
    }

    boolean isLoadingCustomerMonths = false;

    private void initScrollListenerCustomerMonths(final Activity activity) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoadingCustomerMonths) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == transaksiModelList.size() - 1) {
                        //bottom of list!
                        loadMoreCustomerMonths(activity);
                        isLoadingCustomerMonths = true;
                    }
                }
            }
        });
    }

    private void loadMoreCustomerMonths(Activity activity) {
        transaksiModelList.add(null);
        transaksiWaktuPelangganAdapter.notifyItemInserted(transaksiModelList.size() - 1);

        pageTransaksi = pageTransaksi+ 1;
        getDatamonth(pageTransaksi);

    }

    private void getDataRange(int page){
        String testUrl = API.TRANSAKSI_URL+SavePref.readShopId(getActivity())+"/"+tglsebelum.getText().toString()+"/"+tglsesudah.getText().toString()+"?limit=5&page="+page;
        Log.e("res ", testUrl);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                testUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject  obj = new JSONObject(response);

                            boolean status = obj.getBoolean("status");
                            JSONArray data = obj.getJSONArray("data");
                            String message = obj.getString("message");

                            if(status){
                                List<TransaksiModel> models = new Gson().fromJson(data.toString(),new TypeToken<List<TransaksiModel>>(){}.getType());

//                                for (int i = 0; i < data.length(); i++) {
//                                    TransaksiModel model = new Gson().fromJson(data.getString(i), TransaksiModel.class);
//                                    models.add(model);
//                                }

//                                transaksiWaktuPelangganAdapter.setModels(models);
                                updateDataCustomerRange(models);

                                loadingData.dismiss();




                            }else if (message.equals("Data transaksi tidak ditemukan!")){
                                dialogNoData();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();




                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {




                    }

                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", SavePref.readToken(getActivity()));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(request);
    }






    private void initAdapterCustomer3days(Activity activity) {

        transaksiModelList = new ArrayList<>();
        transaksiWaktuPelangganAdapter = new TransaksiWaktuPelangganAdapter(getActivity(), transaksiModelList, getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transaksiWaktuPelangganAdapter);
    }

    public void updateDataCustomer3days(List<TransaksiModel> transaksiModels) {
        if (transaksiModelList.size() != 0){
            transaksiModelList.remove(transaksiModelList.size() - 1);
            int scrollPosition = transaksiModelList.size();
            transaksiWaktuPelangganAdapter.notifyItemRemoved(scrollPosition);
        }

        transaksiModelList.addAll(transaksiModels);
        transaksiWaktuPelangganAdapter.notifyDataSetChanged();
        isLoadingCustomer3days = false;
    }

    boolean isLoadingCustomer3days = false;

    private void initScrollListenerCustomer3days(final Activity activity) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoadingCustomer3days) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == transaksiModelList.size() - 1) {
                        //bottom of list!
                        loadMoreCustomer3days(activity);
                        isLoadingCustomer3days = true;
                    }
                }
            }
        });
    }

    private void loadMoreCustomer3days(Activity activity) {
        transaksiModelList.add(null);
        transaksiWaktuPelangganAdapter.notifyItemInserted(transaksiModelList.size() - 1);

        pageTransaksi = pageTransaksi+ 1;
        getData3day(pageTransaksi);

    }

    private void initAdapterCustomerWeeks(Activity activity) {

        transaksiModelList = new ArrayList<>();
        transaksiWaktuPelangganAdapter = new TransaksiWaktuPelangganAdapter(getActivity(), transaksiModelList, getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transaksiWaktuPelangganAdapter);
    }

    public void updateDataCustomerWeeks(List<TransaksiModel> transaksiModels) {
        if (transaksiModelList.size() != 0){
            transaksiModelList.remove(transaksiModelList.size() - 1);
            int scrollPosition = transaksiModelList.size();
            transaksiWaktuPelangganAdapter.notifyItemRemoved(scrollPosition);
        }

        transaksiModelList.addAll(transaksiModels);
        transaksiWaktuPelangganAdapter.notifyDataSetChanged();
        isLoadingCustomerWeeks= false;
    }

    boolean isLoadingCustomerWeeks= false;

    private void initScrollListenerCustomerWeeks(final Activity activity) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoadingCustomerWeeks) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == transaksiModelList.size() - 1) {
                        //bottom of list!
                        loadMoreCustomerWeeks(activity);
                        isLoadingCustomerWeeks = true;
                    }
                }
            }
        });
    }

    private void loadMoreCustomerWeeks(Activity activity) {
        transaksiModelList.add(null);
        transaksiWaktuPelangganAdapter.notifyItemInserted(transaksiModelList.size() - 1);

        pageTransaksi = pageTransaksi+ 1;
        getDataweek(pageTransaksi);
    }



    private void initAdapterCustomerRange(Activity activity) {

        transaksiModelList = new ArrayList<>();
        transaksiWaktuPelangganAdapter = new TransaksiWaktuPelangganAdapter(getActivity(), transaksiModelList, getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transaksiWaktuPelangganAdapter);
    }

    public void updateDataCustomerRange(List<TransaksiModel> transaksiModels) {
        if (transaksiModelList.size() != 0){
            transaksiModelList.remove(transaksiModelList.size() - 1);
            int scrollPosition = transaksiModelList.size();
            transaksiWaktuPelangganAdapter.notifyItemRemoved(scrollPosition);
        }

        transaksiModelList.addAll(transaksiModels);
        transaksiWaktuPelangganAdapter.notifyDataSetChanged();
        isLoadingCustomerRange= false;
    }

    boolean isLoadingCustomerRange= false;

    private void initScrollListenerCustomerRange(final Activity activity) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoadingCustomerRange) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == transaksiModelList.size() - 1) {
                        //bottom of list!
                        loadMoreCustomerRange(activity);
                        isLoadingCustomerRange = true;
                    }
                }
            }
        });
    }

    private void loadMoreCustomerRange(Activity activity) {
        transaksiModelList.add(null);
        transaksiWaktuPelangganAdapter.notifyItemInserted(transaksiModelList.size() - 1);

        pageTransaksi = pageTransaksi+ 1;
        getDataRange(pageTransaksi);

    }


    private void getData(int page){

        //todays date
        Date currentDateTimeString = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String formattedDate = dateFormat.format(currentDateTimeString);
        Date dateString = Calendar.getInstance().getTime();
        SimpleDateFormat datedFormat = new SimpleDateFormat(" dd MMM yyyy");
        final String todaysDate = datedFormat.format(dateString);

                loadDialog();
//                transaksiWaktuPelangganAdapter.clearModels();
                if (transaksiWaktuPelangganAdapter!=null){
                    emptyText.setVisibility(View.INVISIBLE);
                }

        String testUrl = API.TRANSAKSI_URL2+SavePref.readShopId(getActivity())+"?limit=5&page="+page+"&type=today";
        Log.e("res ", testUrl);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                testUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);

                            boolean status = obj.getBoolean("status");
                            JSONArray data = obj.getJSONArray("data");
                            String message = obj.getString("message");

                            if(status){

                                List<TransaksiModel> models = new Gson().fromJson(data.toString(),new TypeToken<List<TransaksiModel>>(){}.getType());

//                                for (int i = 0; i < data.length(); i++) {
//                                    TransaksiModel model = new Gson().fromJson(data.getString(i), TransaksiModel.class);
//                                    models.add(model);
//                                }

//                                transaksiWaktuPelangganAdapter.setModels(models);
                                updateDataCustomer(models);

                                loadingData.dismiss();



                            }else if (message.equals("Data transaksi tidak ditemukan!")){
                                dialogNoData();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }

                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", SavePref.readToken(getActivity()));
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(request);







    }

    private void dialogNoData() {

        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View dialogView = inflater.inflate(R.layout.layout_no_data, null);
        TextView tutup = (TextView) dialogView.findViewById(R.id.btn_nodata_dismiss);

        dataKosong = new Dialog(getActivity());
        dataKosong.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dataKosong.setContentView(dialogView);
        dataKosong.setCanceledOnTouchOutside(true);



        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels * 0.8);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.4);
        dataKosong.getWindow().setLayout(dialogWidth, dialogHeight);

        dataKosong.show();
        dataKosong.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dataKosong.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        tutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingData.dismiss();
                dataKosong.dismiss();


            }
        });



    }

    private void loadDialog() {

        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View dialogView = inflater.inflate(R.layout.layout_loading_data_popup, null);

        loadingData = new Dialog(getActivity());
        loadingData.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingData.setContentView(dialogView);
        loadingData.setCanceledOnTouchOutside(true);



        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels * 0.9);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.3);
        loadingData.getWindow().setLayout(dialogWidth, dialogHeight);

        loadingData.show();
        loadingData.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        loadingData.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }



}
