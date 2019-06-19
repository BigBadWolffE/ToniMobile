package co.crowde.toni.view.dialog.popup.transaction;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import co.crowde.toni.R;
import co.crowde.toni.adapter.DetailTransaksiWaktuPelangganAdapter;
import co.crowde.toni.controller.main.PrintController;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.helper.volley.AppController;
import co.crowde.toni.model.DetailTransaksiModel;
import co.crowde.toni.model.ShopModel;
import co.crowde.toni.model.TransaksiModel;
import co.crowde.toni.model.UserDetailModel;
import co.crowde.toni.network.API;
import co.crowde.toni.utils.PrinterCommands;
import co.crowde.toni.utils.PrinterNetwork;
import co.crowde.toni.view.dialog.message.printer.PrinterConnectivityDialog;


public class TransactionPopUpDetail {


    protected static final String TAG = "TAG";
    public static RecyclerView recyclerView;
    public static RecyclerView.LayoutManager layoutManager;
    public static DetailTransaksiWaktuPelangganAdapter detailTransaksiWaktuPelangganAdapter;
    TextView todays;
    TextView weeks;
    TextView threedays;
    TextView month;
    TextView range;
    public static TextView tvidTransaksi;
    public static TextView tvnamaPembeli;
    public static TextView tvtanggalTransaksi;
    public static TextView tvjenisPembayaran;
    public static TextView tvtotalTransaksi;
    public static TextView tvtotalquantity;
    public static TextView konfirmya,konfirmtidak;
    public static TextView btnCetakUlang;
    public static Date time;
    public static String strtext;
    public static int quantity;
    public static int totalquantity;
    public static int totalbelanja;
    public static DetailTransaksiModel detailTransaksiModel;
    public static ImageButton btnback;
    public static final int PAGE_START = 1;
    private int CURRENT_PAGE = PAGE_START;
    private boolean isLoading = false, isLastPage = false;
    public static BluetoothAdapter mBluetoothAdapter= null;
    private static UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    public static BluetoothSocket mBluetoothSocket = null;
    public static BluetoothDevice mBluetoothDevice;
    public static OutputStream outputStream = null;
    public static String bluetoothAddress;
    public static DecimalFormat formatNumber;
    public static Dialog dialogcetakstruk;
    public static String jenisBayar;

    public static AlertDialog dialogMessage, dialogDetail;

    public static void showPopup(final Activity activity, final TransaksiModel model){
        final LayoutInflater inflater = LayoutInflater.from(activity);
        final View dialogView = inflater.inflate(
                R.layout.layout_popup_transaction_detail, null);

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_NoTitleBar);
        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimateFade;
        dialog.setCanceledOnTouchOutside(true);


        detailTransaksiWaktuPelangganAdapter = new DetailTransaksiWaktuPelangganAdapter(activity);
        recyclerView = (RecyclerView) dialogView.findViewById(R.id.rc_detail_pembelian);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(detailTransaksiWaktuPelangganAdapter);
        tvnamaPembeli = (TextView) dialogView.findViewById(R.id.header_nama_pelanggan_detail);
        tvjenisPembayaran = (TextView) dialogView.findViewById(R.id.header_jenis_pembayaran_detail);
        tvtanggalTransaksi = (TextView) dialogView.findViewById(R.id.header_tanggal_transaksi_detail);
        tvtotalTransaksi = (TextView) dialogView.findViewById(R.id.header_total_transaksi_detail);
        tvidTransaksi = (TextView)dialogView.findViewById(R.id.header_id_transaksi);
//        tvtotalquantity=(TextView) findViewById(R.id.detail_total);
        btnCetakUlang = (TextView) dialogView.findViewById(R.id.btn_cetak_ulang);
        btnCetakUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cetakStruk(activity, model);
            }
        });
        final String detailToko = SavePref.readDetailToko(activity);
        final UserDetailModel tokoDetailModel = new Gson().fromJson(detailToko, UserDetailModel.class);

        String tanggal = model.getCreatedAt();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = null;
        try {
            date = dateFormat.parse(tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat(" EEE, dd MMMM yyyy"); //If you need time just put specific format for time like 'HH:mm:ss'
        String dateStr = formatter.format(date);
        tvtanggalTransaksi.setText(dateStr);
        tvnamaPembeli.setText(model.getCostumerName());
        String jenisBayar = null;
        if (model.getPaymentType().equals("Credit")){
            jenisBayar = "Hutang";
        }else {
            jenisBayar = "Tunai";
        }
        tvjenisPembayaran.setText(jenisBayar);
        DecimalFormat money = new DecimalFormat("#,###,###");
        int total = Integer.parseInt(model.getAmount());
        tvtotalTransaksi.setText("Rp."+money.format(total));
        tvidTransaksi.setText(model.getTransactionId());
        strtext = model.getTransactionId();
        btnback = (ImageButton)dialogView.findViewById(R.id.back_transaksi);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        String testUrl = API.DETAILTRANSAKSI_URL + SavePref.readShopId(activity)+ "/" + strtext;
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
//                            handler();
                            if (status) {

//                                String quantity = obj.getString("data");
//                                JSONObject object1 = new JSONObject(quantity);
//                                String init = object1.getString("quantity");
//                                JSONObject object2 = new JSONObject(init);
//                                String total ;

                                List<DetailTransaksiModel> models = new ArrayList<>();

                                for (int i = 0; i < data.length(); i++) {
                                    DetailTransaksiModel model = new Gson().fromJson(data.getString(i), DetailTransaksiModel.class);
                                    models.add(model);
//                                    totalquantity = 0;
//                                    for (int y = 0; y <data.length(); y++){
//                                        totalquantity= totalquantity+model.getQuantity();
//
//                                    }
//                                    tvtotalquantity.setText(Integer.toString(totalquantity));

                                }
                                detailTransaksiWaktuPelangganAdapter.setModels(models);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(activity, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                detailTransaksiWaktuPelangganAdapter.clearModels();

            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", SavePref.readToken(activity));
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(request);

        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.overlayBackground);
    }



    public static void cetakStruk(final Activity activity, final TransaksiModel model) {

        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
//        final LayoutInflater inflaterUser = LayoutInflater.from(activity);
        final View dialogView = LayoutInflater.from(activity)
                .inflate(R.layout.layout_konfirmasi_cetak_ulang_struk, viewGroup ,false);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);

        dialogMessage = builder.create();

//        DisplayMetrics displayMetricsPW = activity.getResources().getDisplayMetrics();
//        dialogcetakstruk = new Dialog(activity);
//        dialogcetakstruk.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialogcetakstruk.setContentView(dialogView);
//        dialogcetakstruk.setCanceledOnTouchOutside(true);
//        int dialogWidth = (int)(displayMetricsPW.widthPixels * 0.9);
//        int dialogHeight = (int)(displayMetricsPW.heightPixels * 0.4);
//        dialogcetakstruk.getWindow().setLayout(dialogWidth, dialogHeight);

        konfirmya =  (TextView)dialogView.findViewById(R.id.confirmyacetak);
        konfirmtidak = (TextView)dialogView.findViewById(R.id.confirmtidakcetak);


        //todays date
        final Date currentDateTimeString = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM yyyy");
        final String formattedDate = dateFormat1.format(currentDateTimeString);

        if (model.getPaymentType().equals("Credit")){
            jenisBayar = "Hutang";
        }else {
            jenisBayar = "Tunai";
        }

        Log.e("Toko", new Gson().toJson(SavePref.readDetailToko(activity)));

        konfirmya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ShopModel shopModel= new Gson()
                        .fromJson(SavePref.readDetailToko(activity), ShopModel.class);
//                final ShopModel shopModel = new Gson().fromJson(activity,ShopModel.class);

                String testUrl = API.DETAILTRANSAKSI_URL + SavePref.readShopId(activity)+ "/" +model.getTransactionId();
                final StringRequest request = new StringRequest(
                        Request.Method.GET,
                        testUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject obj = new JSONObject(response);

                                    Log.e("Response", response);

                                    boolean status = obj.getBoolean("status");
                                    JSONArray data = obj.getJSONArray("data");
                                    String message = obj.getString("message");
                                    Log.d("tag", obj.getString("status"));

                                    final List<DetailTransaksiModel> models = new ArrayList<>();

                                    for (int i = 0; i < data.length(); i++) {
                                        DetailTransaksiModel model = new Gson().fromJson(data.getString(i), DetailTransaksiModel.class);
                                        models.add(model);
                                    }

                                    DecimalFormatRupiah.changeFormat(activity);

                                    detailTransaksiWaktuPelangganAdapter.setModels(models);
                                    if(status){
                                        resetConnection();
                                        if(SavePref.readDeviceAddress(activity)!=null){
                                            PrinterNetwork.bluetoothAddress = SavePref.readDeviceAddress(activity);
                                            PrinterNetwork.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                            PrinterNetwork.mBluetoothDevice= PrinterNetwork.mBluetoothAdapter.getRemoteDevice(PrinterNetwork.bluetoothAddress);
                                            try {
                                                PrinterNetwork.mBluetoothSocket = PrinterNetwork.createBluetoothSocket(PrinterNetwork.mBluetoothDevice);
                                                PrinterNetwork.mBluetoothSocket.connect();
                                                if (PrinterNetwork.mBluetoothSocket.isConnected()){
                                                    PrintController.printDetailTransaction(activity, model, models, formattedDate , jenisBayar);
                                                }
                                            } catch (IOException e) {
                                                PrinterConnectivityDialog.showDialog(activity);
//                                                ConfirmTransactionDialog.progressDialog.dismiss();
                                                Log.e("Bluetooth","Can't Connect");
                                            }

                                        } else {
//                                            ConfirmTransactionDialog.progressDialog.dismiss();
                                            PrinterNetwork.pairingBluetooth(activity);
                                        }
                                    }

//                                    konfirmya.setOnClickListener(new View.OnClickListener() {
//                                        public void onClick(View mView) {
//                                            Thread t = new Thread() {
//                                                public void run() {
//
//                                                }
//                                            };
//                                            t.start();
//                                            activity.finish();
//                                        }
//
//                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(activity, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
                        detailTransaksiWaktuPelangganAdapter.clearModels();
                    }
                }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Authorization", SavePref.readToken(activity));
                        return params;
                    }
                };

                AppController.getInstance().addToRequestQueue(request);
            }
        });

        konfirmtidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
            }
        });

        dialogMessage.show();
        dialogMessage.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }


    private static BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
                return (BluetoothSocket) m.invoke(device, applicationUUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(applicationUUID);
    }
    private static void resetConnection() {
        if (outputStream != null) {
            try {outputStream.close();} catch (Exception e) {}
            outputStream = null;
        }
        if (mBluetoothSocket != null) {
            try {mBluetoothSocket.close();} catch (Exception e) {}
            mBluetoothSocket = null;
        }
    }
    private static void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }

            switch (align){
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void pairingBluetooth(final Activity activity) {
        ArrayList bluetoothList = new ArrayList();
        final ArrayList<String> bluetoothDeviceList = new ArrayList();
        resetConnection();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (Object device : pairedDevices) {
                mBluetoothDevice = (BluetoothDevice) device;
                bluetoothList.add(mBluetoothDevice.getName() + "\n" + mBluetoothDevice.getAddress());
                bluetoothDeviceList.add(mBluetoothDevice.getAddress());
            }
        }

        // show list
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Pilih Device");

        ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.select_dialog_singlechoice,
                bluetoothList.toArray(new String[bluetoothList.size()]));

        alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                final String deviceAddress = bluetoothDeviceList.get(position);
                SavePref.saveDeviceAddress(activity, deviceAddress);

                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothDevice= mBluetoothAdapter.getRemoteDevice(deviceAddress);

                try {
                    mBluetoothSocket = createBluetoothSocket(mBluetoothDevice);
                    mBluetoothSocket.connect();
                    Toast.makeText(activity, "Berhasil terhubung dengan Bluetooth Printer", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                } catch (IOException e) {
                    try {
                        Toast.makeText(activity, "Tidak dapat terhubung dengan Bluetooth Printer", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        mBluetoothSocket.close();
                        Log.e("Bluetooth","Can't Connect");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        Toast.makeText(activity, "Tidak dapat terhubung dengan Bluetooth Socket", Toast.LENGTH_SHORT).show();
                        Log.e("Bluetooth","Socket can't closed");
                    }
                }

            }
        });
        alertDialog.show();
    }



}
