package co.crowde.toni.intent;//package co.crowde.toni.view.intent;
//
//
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.DialogInterface;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.widget.ArrayAdapter;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.google.gson.Gson;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.lang.reflect.Method;
//import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.UUID;
//
//import co.crowde.toni.R;
//import co.crowde.toni.adapter.DetailTransaksiWaktuPelangganAdapter;
//import co.crowde.toni.controller.network.API;
//import co.crowde.toni.helper.AppController;
//import co.crowde.toni.helper.PrinterCommands;
//import co.crowde.toni.helper.SavePref;
//import co.crowde.toni.model.DetailTransaksiModel;
//import co.crowde.toni.model.UserDetailModel;
//
//public class TransactionDetailActivity extends AppCompatActivity {
//    protected static final String TAG = "TAG";
//    RecyclerView recyclerView;
//    RecyclerView.LayoutManager layoutManager;
//    DetailTransaksiWaktuPelangganAdapter detailTransaksiWaktuPelangganAdapter;
//    TextView todays;
//    TextView weeks;
//    TextView threedays;
//    TextView month;
//    TextView range;
//    private TextView tvidTransaksi;
//    private TextView tvnamaPembeli;
//    private TextView tvtanggalTransaksi;
//    private TextView tvjenisPembayaran;
//    private TextView tvtotalTransaksi;
//    private TextView tvtotalquantity;
//    TextView konfirmya,konfirmtidak;
//    TextView btnCetakUlang;
//    Date time;
//    String strtext;
//    int quantity;
//    int totalquantity;
//    int totalbelanja;
//    DetailTransaksiModel detailTransaksiModel;
//    ImageButton btnback;
//    public static final int PAGE_START = 1;
//    private int CURRENT_PAGE = PAGE_START;
//    private boolean isLoading = false, isLastPage = false;
//    public static BluetoothAdapter mBluetoothAdapter= null;
//    private UUID applicationUUID = UUID
//            .fromString("00001101-0000-1000-8000-00805F9B34FB");
//    private ProgressDialog mBluetoothConnectProgressDialog;
//    public static BluetoothSocket mBluetoothSocket = null;
//    public static BluetoothDevice mBluetoothDevice;
//    public static OutputStream outputStream = null;
//    String bluetoothAddress;
//    DecimalFormat formatNumber;
//    Dialog dialogcetakstruk;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_transaction_detail);
//        detailTransaksiWaktuPelangganAdapter = new DetailTransaksiWaktuPelangganAdapter(TransactionDetailActivity.this);
//        recyclerView = (RecyclerView) findViewById(R.id.rc_detail_pembelian);
//        recyclerView.setLayoutManager(new LinearLayoutManager(TransactionDetailActivity.this));
//        recyclerView.setAdapter(detailTransaksiWaktuPelangganAdapter);
//        tvnamaPembeli = (TextView) findViewById(R.id.header_nama_pelanggan_detail);
//        tvjenisPembayaran = (TextView) findViewById(R.id.header_jenis_pembayaran_detail);
//        tvtanggalTransaksi = (TextView) findViewById(R.id.header_tanggal_transaksi_detail);
//        tvtotalTransaksi = (TextView) findViewById(R.id.header_total_transaksi_detail);
//        tvidTransaksi = (TextView)findViewById(R.id.header_id_transaksi);
////        tvtotalquantity=(TextView) findViewById(R.id.detail_total);
//        btnCetakUlang = (TextView) findViewById(R.id.btn_cetak_ulang);
//        btnCetakUlang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cetakStruk();
//            }
//        });
//        final String detailToko = SavePref.readDetailToko(TransactionDetailActivity.this);
//        final UserDetailModel tokoDetailModel = new Gson().fromJson(detailToko, UserDetailModel.class);
//        tvtanggalTransaksi.setText(getIntent().getStringExtra("tanggaltransaksi"));
//        tvnamaPembeli.setText(getIntent().getStringExtra("namapelanggan"));
//        String jenisBayar = null;
//        if (getIntent().getStringExtra("pembayaran").equals("Credit")){
//            jenisBayar = "Hutang";
//        }else {
//            jenisBayar = "Tunai";
//        }
//        tvjenisPembayaran.setText(jenisBayar);
//        tvtotalTransaksi.setText(getIntent().getStringExtra("totaltransaksi"));
//        tvidTransaksi.setText(getIntent().getStringExtra("idtransaksi"));
//        strtext = getIntent().getStringExtra("idtransaksi");
//        btnback = (ImageButton)findViewById(R.id.back_transaksi);
//        btnback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//
//        String testUrl = API.DETAILTRANSAKSI_URL + SavePref.readShopId(TransactionDetailActivity.this)+ "/" + strtext;
//        Log.e("res ", testUrl);
//        StringRequest request = new StringRequest(
//                Request.Method.GET,
//                testUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONObject obj = new JSONObject(response);
//
//                            boolean status = obj.getBoolean("status");
//                            JSONArray data = obj.getJSONArray("data");
//                            String message = obj.getString("message");
////                            handler();
//                            if (status) {
//
////                                String quantity = obj.getString("data");
////                                JSONObject object1 = new JSONObject(quantity);
////                                String init = object1.getString("quantity");
////                                JSONObject object2 = new JSONObject(init);
////                                String total ;
//
//                                List<DetailTransaksiModel> models = new ArrayList<>();
//
//                                for (int i = 0; i < data.length(); i++) {
//                                    DetailTransaksiModel model = new Gson().fromJson(data.getString(i), DetailTransaksiModel.class);
//                                    models.add(model);
////                                    totalquantity = 0;
////                                    for (int y = 0; y <data.length(); y++){
////                                        totalquantity= totalquantity+model.getQuantity();
////
////                                    }
////                                    tvtotalquantity.setText(Integer.toString(totalquantity));
//
//                                }
//                                detailTransaksiWaktuPelangganAdapter.setModels(models);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(TransactionDetailActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
//                detailTransaksiWaktuPelangganAdapter.clearModels();
//
//            }
//        }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", SavePref.readToken(TransactionDetailActivity.this));
//                return params;
//            }
//        };
//
//        AppController.getInstance().addToRequestQueue(request);
//    }
//
//    private void cetakStruk() {
//
//        final LayoutInflater inflaterUser = LayoutInflater.from(TransactionDetailActivity.this);
//        final View dialogView = inflaterUser.inflate(R.layout.layout_konfirmasi_cetak_ulang_struk, null);
//        DisplayMetrics displayMetricsPW = this.getResources().getDisplayMetrics();
//        dialogcetakstruk = new Dialog(TransactionDetailActivity.this);
//        dialogcetakstruk.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialogcetakstruk.setContentView(dialogView);
//        dialogcetakstruk.setCanceledOnTouchOutside(true);
//        int dialogWidth = (int)(displayMetricsPW.widthPixels * 0.9);
//        int dialogHeight = (int)(displayMetricsPW.heightPixels * 0.4);
//        dialogcetakstruk.getWindow().setLayout(dialogWidth, dialogHeight);
//        konfirmya =  (TextView)dialogView.findViewById(R.id.confirmyacetak);
//        konfirmtidak = (TextView)dialogView.findViewById(R.id.confirmtidakcetak);
//        final String detailToko = SavePref.readDetailToko(TransactionDetailActivity.this);
//        final UserDetailModel tokoDetailModel = new Gson().fromJson(detailToko, UserDetailModel.class);
//
//        //todays date
//        final Date currentDateTimeString = Calendar.getInstance().getTime();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd ");
//        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM yyyy");
//        final String formattedDate = dateFormat1.format(currentDateTimeString);
//
//        String jenisBayar = null;
//        if (getIntent().getStringExtra("jenisPembayaran").equals("Credit")){
//            jenisBayar = "Hutang";
//        }else {
//            jenisBayar = "Tunai";
//        }
//
//
//        String testUrl = API.DETAILTRANSAKSI_URL + SavePref.readShopId(TransactionDetailActivity.this)+ "/" +getIntent().getStringExtra("idTransaksi");
//        Log.e("res ", testUrl);
//        final String finalJenisBayar = jenisBayar;
//        final StringRequest request = new StringRequest(
//                Request.Method.GET,
//                testUrl,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONObject obj = new JSONObject(response);
//
//                            boolean status = obj.getBoolean("status");
//                            JSONArray data = obj.getJSONArray("data");
//                            String message = obj.getString("message");
//
//
//                            final List<DetailTransaksiModel> models = new ArrayList<>();
//
//                            for (int i = 0; i < data.length(); i++) {
//                                DetailTransaksiModel model = new Gson().fromJson(data.getString(i), DetailTransaksiModel.class);
//                                models.add(model);
//                            }
//
//                            detailTransaksiWaktuPelangganAdapter.setModels(models);
//                            konfirmya.setOnClickListener(new View.OnClickListener() {
//                                public void onClick(View mView) {
//                                    Thread t = new Thread() {
//                                        public void run() {
//                                            try {
//                                                resetConnection();
//                                                if(SavePref.readDeviceAddress(TransactionDetailActivity.this)!=null){
//                                                    bluetoothAddress = SavePref.readDeviceAddress(TransactionDetailActivity.this);
//                                                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                                                    mBluetoothDevice= mBluetoothAdapter.getRemoteDevice(bluetoothAddress);
//                                                    try {
//                                                        mBluetoothSocket = createBluetoothSocket(mBluetoothDevice);
//                                                        mBluetoothSocket.connect();
//                                                        if (mBluetoothSocket.isConnected()){
//
//                                                            outputStream = mBluetoothSocket
//                                                                    .getOutputStream();
//
//                                                            printNewLine();
//
//                                                            printCustom(tokoDetailModel.getShopName(),2,0);
//                                                            printCustom(""+tokoDetailModel.getStreet()+", "+
//                                                                    tokoDetailModel.getVillage()+", "+
//                                                                    tokoDetailModel.getDistrict()+", "+
//                                                                    tokoDetailModel.getProvince(),0,0);
//
//                                                            printNewLine();
//                                                            printText("Nomor Transaksi : "+ getIntent().getStringExtra("idTransaksi")+"\n");
//                                                            printText("Pelanggan : "+ getIntent().getStringExtra("namaPembeli")+"\n");
//                                                            printText("--------------------------------\n");
//                                                            printText("Tanggal : "+ getIntent().getStringExtra("tanggalTransaksi")+"\n");
//                                                            printText("--------------------------------\n");
//                                                            printText("Pembayaran: "+ finalJenisBayar +"\n");
//                                                            printText("--------------------------------\n");
//
//                                                            for(DetailTransaksiModel model : models  ){
//                                                                int sellingprice = Integer.parseInt(model.getSellingPrice());
//                                                                printText(model.getProductName()+"\n");
//                                                                printText(String.format("%2s %1s %1s ",model.getQuantity(), "X","Rp."+formatNumber.format(sellingprice)+"\n"));
//
//                                                            }
//                                                            printText("-------------------------------\n");
//                                                            int amount = Integer.parseInt(getIntent().getStringExtra("totalTransaksi"));
//                                                            printText("Total  : "+"Rp."+formatNumber.format(amount));
//                                                            printNewLine();
//                                                            printText("--------------------------------\n");
//                                                            printText("--------------------------------\n");
//                                                            printText("TERIMAKASIH SUDAH MEMBELI PRODUK PERTANIAN DI TOKO KAMI.\n\n");
//                                                            printCustom("====LAYANAN KONSUMEN KAMI ====",0,1);
//                                                            printText("SMS/TELP "+tokoDetailModel.getPhoneNumber()+"\n");
//                                                            printText("Dicetak Tanggal :"+formattedDate+"\n");
//                                                            printText("------------------------------------------\n");
//                                                            printNewLine();
//                                                            printNewLine();
//                                                            printNewLine();
//                                                            printNewLine();
//                                                            outputStream.flush();
//
//                                                        }
//                                                    } catch (IOException e) {
//                                                        try {
//                                                            Toast.makeText(TransactionDetailActivity.this, "Tidak dapat terhubung dengan Bluetooth Printer", Toast.LENGTH_SHORT).show();
//                                                            mBluetoothSocket.close();
//                                                            Log.e("Bluetooth","Can't Connect");
//                                                        } catch (IOException e1) {
//                                                            e1.printStackTrace();
//                                                            Toast.makeText(TransactionDetailActivity.this, "Tidak dapat terhubung dengan Bluetooth Socket", Toast.LENGTH_SHORT).show();
//                                                            Log.e("Bluetooth","Socket can't closed");
//                                                        }
//                                                    }
//                                                } else {
//                                                    pairingBluetooth();
//                                                }
//                                            } catch (Exception e) {
//                                                Log.e("Main", "Exe ", e);
//                                            }
//                                        }
//                                    };
//                                    t.start();
//                                    TransactionDetailActivity.this.finish();
//                                }
//
//                            });
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(TransactionDetailActivity.this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
//                detailTransaksiWaktuPelangganAdapter.clearModels();
//            }
//        }
//        ){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Authorization", SavePref.readToken(TransactionDetailActivity.this));
//                return params;
//            }
//        };
//
//        AppController.getInstance().addToRequestQueue(request);
//
//
//
//    }
//
//
//    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
//        if(Build.VERSION.SDK_INT >= 10){
//            try {
//                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
//                return (BluetoothSocket) m.invoke(device, applicationUUID);
//            } catch (Exception e) {
//                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
//            }
//        }
//        return  device.createRfcommSocketToServiceRecord(applicationUUID);
//    }
//    private void resetConnection() {
//        if (outputStream != null) {
//            try {outputStream.close();} catch (Exception e) {}
//            outputStream = null;
//        }
//        if (mBluetoothSocket != null) {
//            try {mBluetoothSocket.close();} catch (Exception e) {}
//            mBluetoothSocket = null;
//        }
//    }
//    private void printCustom(String msg, int size, int align) {
//        //Print config "mode"
//        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
//        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
//        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
//        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
//        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
//        try {
//            switch (size){
//                case 0:
//                    outputStream.write(cc);
//                    break;
//                case 1:
//                    outputStream.write(bb);
//                    break;
//                case 2:
//                    outputStream.write(bb2);
//                    break;
//                case 3:
//                    outputStream.write(bb3);
//                    break;
//            }
//
//            switch (align){
//                case 0:
//                    //left align
//                    outputStream.write(co.crowde.toni.helper.PrinterCommands.ESC_ALIGN_LEFT);
//                    break;
//                case 1:
//                    //center align
//                    outputStream.write(co.crowde.toni.helper.PrinterCommands.ESC_ALIGN_CENTER);
//                    break;
//                case 2:
//                    //right align
//                    outputStream.write(co.crowde.toni.helper.PrinterCommands.ESC_ALIGN_RIGHT);
//                    break;
//            }
//            outputStream.write(msg.getBytes());
//            outputStream.write(co.crowde.toni.helper.PrinterCommands.LF);
//            //outputStream.write(cc);
//            //printNewLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//    private void printNewLine() {
//        try {
//            outputStream.write(co.crowde.toni.helper.PrinterCommands.FEED_LINE);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//    private void printText(String msg) {
//        try {
//            // Print normal text
//            outputStream.write(msg.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//    private void printText(byte[] msg) {
//        try {
//            // Print normal text
//            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
//            outputStream.write(msg);
//            printNewLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void pairingBluetooth() {
//        ArrayList bluetoothList = new ArrayList();
//        final ArrayList<String> bluetoothDeviceList = new ArrayList();
//        resetConnection();
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        Set pairedDevices = mBluetoothAdapter.getBondedDevices();
//        if (pairedDevices.size() > 0) {
//            for (Object device : pairedDevices) {
//                mBluetoothDevice = (BluetoothDevice) device;
//                bluetoothList.add(mBluetoothDevice.getName() + "\n" + mBluetoothDevice.getAddress());
//                bluetoothDeviceList.add(mBluetoothDevice.getAddress());
//            }
//        }
//
//        // show list
//        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(TransactionDetailActivity.this);
//        alertDialog.setTitle("Pilih Device");
//
//        ArrayAdapter adapter = new ArrayAdapter(TransactionDetailActivity.this, android.R.layout.select_dialog_singlechoice,
//                bluetoothList.toArray(new String[bluetoothList.size()]));
//
//        alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
//                final String deviceAddress = bluetoothDeviceList.get(position);
//                SavePref.saveDeviceAddress(TransactionDetailActivity.this, deviceAddress);
//
//                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                mBluetoothDevice= mBluetoothAdapter.getRemoteDevice(deviceAddress);
//
//                try {
//                    mBluetoothSocket = createBluetoothSocket(mBluetoothDevice);
//                    mBluetoothSocket.connect();
//                    Toast.makeText(TransactionDetailActivity.this, "Berhasil terhubung dengan Bluetooth Printer", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//
//                } catch (IOException e) {
//                    try {
//                        Toast.makeText(TransactionDetailActivity.this, "Tidak dapat terhubung dengan Bluetooth Printer", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                        mBluetoothSocket.close();
//                        Log.e("Bluetooth","Can't Connect");
//                    } catch (IOException e1) {
//                        e1.printStackTrace();
//                        Toast.makeText(TransactionDetailActivity.this, "Tidak dapat terhubung dengan Bluetooth Socket", Toast.LENGTH_SHORT).show();
//                        Log.e("Bluetooth","Socket can't closed");
//                    }
//                }
//
//            }
//        });
//        alertDialog.show();
//    }
//
////    private void handler() {
////        if(!isAdded())
////        {
////            Toast.makeText(TransactionDetailActivity.this,"WOW",Toast.LENGTH_SHORT).show();
////        }
////    }
//}
