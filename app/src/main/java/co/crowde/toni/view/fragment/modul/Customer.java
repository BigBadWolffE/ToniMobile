package co.crowde.toni.view.fragment.modul;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import co.crowde.toni.R;
import co.crowde.toni.adapter.TransaksiBagianPelangganAdapter;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.helper.volley.AppController;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.UserDetailModel;
import co.crowde.toni.network.API;
import co.crowde.toni.view.dialog.customer.PopUpTambahPelangganTransaksi;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Customer extends Fragment {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public TransaksiBagianPelangganAdapter transaksiBagianPelangganAdapter;
    //    TransaksiFragment transaksiFragment;
    Spinner spinnerpelanggan;
    FloatingActionButton btntambahpelanggan;
    TextView tvTransaksiPelangganNamaToko;
    TextView tvTransaksiPelangganNamaPemilikToko;
    RelativeLayout idDashboardLayoutInfoToko;
    ImageView imgLaporanToko;
    Date currentTime;
    EditText etSearchPelanggan;
    List<CustomerModel> customerModelList;
    Date time;
    Dialog loadingData,dataKosong,dialogLoad;
    TextView tvLoader;
    String ubahTanggal;
    //Bluetooth
    public static BluetoothAdapter mBluetoothAdapter= null;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    public static BluetoothSocket mBluetoothSocket = null;
    public static BluetoothDevice mBluetoothDevice;
    public static OutputStream os = null;
    String bluetoothAddress;
    static DividerItemDecoration itemDecorator;

    private static int pageCustomer ;

    public Customer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_customer, container, false);

        pageCustomer = 1;

        etSearchPelanggan = (EditText)rootView.findViewById(R.id.et_search_pelanggan);
        itemDecorator = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.divider_line_item));
        customerModelList = new ArrayList<>();
        transaksiBagianPelangganAdapter = new TransaksiBagianPelangganAdapter(getActivity(),customerModelList,getActivity());
        recyclerView = (RecyclerView)rootView.findViewById(R.id.rc_list_pelanggan);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transaksiBagianPelangganAdapter);
        recyclerView.addItemDecoration(itemDecorator);
//        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.HORIZONTAL));

        initAdapterCustomer(getActivity());
        getData(pageCustomer);
        initScrollListenerCustomer(getActivity());

        //Info Toko
        String detailToko = SavePref.readDetailToko(getActivity());
        // parsing json object to model java
        UserDetailModel tokoDetailModel = new Gson().fromJson(detailToko, UserDetailModel.class);

        //Setup Button tambah pelanggan
        btntambahpelanggan = (FloatingActionButton) rootView.findViewById(R.id.btn_tambah_pelanggan);
        btntambahpelanggan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), PopUpTambahPelangganTransaksi.class);
                startActivityForResult(intent,12);
            }
        });


        transaksiBagianPelangganAdapter.notifyDataSetChanged();
        getData(pageCustomer);

        etSearchPelanggan.addTextChangedListener(watcher);


        return rootView;
    }


    private void resetConnection() {
        if (os != null) {
            try {os.close();} catch (Exception e) {}
            os = null;
        }
        if (mBluetoothSocket != null) {
            try {mBluetoothSocket.close();} catch (Exception e) {}
            mBluetoothSocket = null;
        }
    }



    public void getData(int page) {

        String testUrl = API.CUSTOMERLIST_URL + SavePref.readShopId(getActivity())+"?limit=10&page="+page;
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

                                List<CustomerModel> models = new Gson().fromJson( data.toString(), new TypeToken<List<CustomerModel>>() {
                                }.getType());
//                                customerModelList.addAll(models);
//                                transaksiBagianPelangganAdapter.notifyDataSetChanged();
//                                transaksiBagianPelangganAdapter.setItems(models);

                                updateDataCustomer(models);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
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
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        daftarBarangModelList = new ArrayList<>();
//        daftarBarangAdapter = new DaftarBarangAdapter(getActivity(),daftarBarangModelList, getActivity());
//        recyclerView.setAdapter(daftarBarangAdapter);

        customerModelList = new ArrayList<>();
        transaksiBagianPelangganAdapter = new TransaksiBagianPelangganAdapter(getActivity(), customerModelList, getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transaksiBagianPelangganAdapter);
    }

    public void updateDataCustomer(List<CustomerModel> customerModels) {
        if (customerModelList.size() != 0){
            customerModelList.remove(customerModelList.size() - 1);
            int scrollPosition = customerModelList.size();
            transaksiBagianPelangganAdapter.notifyItemRemoved(scrollPosition);
        }

        customerModelList.addAll(customerModels);
        transaksiBagianPelangganAdapter.notifyDataSetChanged();
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == customerModelList.size() - 1) {
                        //bottom of list!
                        loadMoreCustomer(activity);
                        isLoadingCustomer = true;
                    }
                }
            }
        });
    }

    private void loadMoreCustomer(Activity activity) {
        customerModelList.add(null);
        transaksiBagianPelangganAdapter.notifyItemInserted(customerModelList.size() - 1);

        pageCustomer = pageCustomer + 1;
        getData(pageCustomer);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            transaksiBagianPelangganAdapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent status){

        try {
            super.onActivityResult(requestCode,resultCode,status);
            if (requestCode == 12 && resultCode == RESULT_OK){

                String requiredValue = status.getStringExtra("key");

                if (requiredValue!=null){
                    if(transaksiBagianPelangganAdapter.getItemCount()>0){
                        transaksiBagianPelangganAdapter.clearModels();
                        getData(pageCustomer);
                    }


                }
            }else if(requestCode == 5 && resultCode == RESULT_OK){

                String requiredValue = status.getStringExtra("lock");

//                if (requiredValue!=null){}
                if(transaksiBagianPelangganAdapter.getItemCount()>0){
                    transaksiBagianPelangganAdapter.clearModels();
                    transaksiBagianPelangganAdapter.notifyDataSetChanged();
                    getData(pageCustomer);


                }else{

                    transaksiBagianPelangganAdapter.clearModels();
                    getData(pageCustomer);
                }
            }
        }catch (Exception ex){
            Toast.makeText(getActivity(),ex.toString(),Toast.LENGTH_SHORT).show();
        }

    }
}
