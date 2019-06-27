package co.crowde.toni.view.dialog.popup.customer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
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
import co.crowde.toni.adapter.TransaksiBagianPelangganAdapter;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.helper.volley.AppController;
import co.crowde.toni.view.activity.customer.CustomerHutangActivity;
import co.crowde.toni.listener.ChangeClickListener;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.UserDetailModel;
import co.crowde.toni.network.API;
import co.crowde.toni.utils.print.PrinterCommands;
//import soedja.crowde.tokotani.HalamanAwalTransaksiPelangganActivity;
//import soedja.crowde.tokotani.R;
//import soedja.crowde.tokotani.adapter.TransaksiBagianPelangganAdapter;
//import soedja.crowde.tokotani.app.AppConst;
//import soedja.crowde.tokotani.app.AppController;
//import soedja.crowde.tokotani.helpers.PrinterCommands;
//import soedja.crowde.tokotani.helpers.Save;
//import soedja.crowde.tokotani.interface_.ChangeClickListener;
//import soedja.crowde.tokotani.model.CustomerModel;
//import soedja.crowde.tokotani.model.UserDetailModel;

public class PopUpBayarHutangTransaksi extends AppCompatActivity implements ChangeClickListener {
    protected static final String TAG = "TAG";
    TextView tv_namapelangganhutang;
    TextView tv_totalpelangganhutang;
    EditText et_inputhutang;
    CardView btn_bayarhutang;
    WindowManager.LayoutParams params;
    Dialog popUpBerhasil,dialogBerhasil;
    RelativeLayout mainLayout;
    TextView tvnamapelangganhutangterbayar,tvsisahutangpelanggan;
    TransaksiBagianPelangganAdapter transaksiBagianPelangganAdapter;
    TextView kembali;
    MediaPlayer mp;
    List<CustomerModel> customerModelList;
    //Bluetooth
    public static BluetoothAdapter mBluetoothAdapter= null;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    public static BluetoothSocket mBluetoothSocket = null;
    public static BluetoothDevice mBluetoothDevice;
    public static OutputStream outputStream = null;
    String bluetoothAddress;
    DecimalFormat formatNumber;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_bayar_hutang_transaksi);
        int a = Integer.parseInt(getIntent().getStringExtra("hutang"));
        int b= Integer.parseInt(getIntent().getStringExtra("creditPaid"));
        String total;
        int sum  ;
        sum = a - b;
        total = Integer.toString(sum);
        DecimalFormat money = new DecimalFormat("#,###,###");
        final int amount = Integer.parseInt(total);

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.5),(int) (height*.6));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);



        tv_namapelangganhutang = (TextView)findViewById(R.id.nama_pelanggan_hutang);
       tv_namapelangganhutang.setText(getIntent().getStringExtra("namapelanggan"));
       tv_totalpelangganhutang = (TextView)findViewById(R.id.total_hutang_pelanggan);
       tv_totalpelangganhutang.setText("Rp."+money.format(amount));
       et_inputhutang = (EditText)findViewById(R.id.input_hutang);
       btn_bayarhutang = (CardView)findViewById(R.id.button_bayar_hutang);
       customerModelList = new ArrayList<>();
        final Context context = this;
       btn_bayarhutang.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (TextUtils.isEmpty(et_inputhutang.getText().toString())) {
                   Toast.makeText(PopUpBayarHutangTransaksi.this, "Hutang Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show();
               } else if(Integer.parseInt(et_inputhutang.getText().toString())> amount){
                    Toast.makeText(PopUpBayarHutangTransaksi.this, "Nilai Lebih Besar dari Hutang", Toast.LENGTH_SHORT).show();
               } else{
                    resetConnection();
                   String testUrl = API.BAYARHUTANG_URL;

                   StringRequest request = new StringRequest(
                           Request.Method.POST,
                           testUrl,
                           new Response.Listener<String>() {
                               @Override
                               public void onResponse(String response) {

                                   try{

                                       JSONObject objLogin = new JSONObject(response);
                                       boolean status = objLogin.getBoolean("status");
                                       String data = objLogin.getString("data");
                                       String message = objLogin.getString("message");
                                       if(status){
                                           Toast.makeText(PopUpBayarHutangTransaksi.this, "Hutang Berhasil Di Update", Toast.LENGTH_SHORT).show();
                                           loadDialog();
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
                   ) {
                       @Override
                       public String getBodyContentType() {
                           return "application/x-www-form-urlencoded; charset=UTF-8";
                       }

                       @Override
                       public Map<String, String> getHeaders() throws AuthFailureError {
                           Map<String, String> params = new HashMap<String, String>();
                           params.put("Authorization", SavePref.readToken(PopUpBayarHutangTransaksi.this));
                           return params;
                       }

                       @Override
                       protected Map<String, String> getParams() throws AuthFailureError {
                           Map<String, String> params1 = new HashMap<String, String>();

                           params1.put("shopId", SavePref.readShopId(PopUpBayarHutangTransaksi.this));
                           params1.put("customerId", String.valueOf(getIntent().getStringExtra("customerId")));
                           params1.put("amount", et_inputhutang.getText().toString());
                           Log.e("params sent", new Gson().toJson(params1));

                           return params1;
                       }
                   };
                   AppController.getInstance().addToRequestQueue(request);
               }
           }
       });
    }
    private void loadDialog() {
        final LayoutInflater inflaterUser = LayoutInflater.from(PopUpBayarHutangTransaksi.this);
        final View dialogView = inflaterUser.inflate(R.layout.activity_hutang_terbayar_pop_up, null);
        int a = Integer.parseInt(getIntent().getStringExtra("hutang"));
        int b= Integer.parseInt(getIntent().getStringExtra("creditPaid"));
        String total;
        int sum  ;
        sum = a - b;
        total = Integer.toString(sum);
        DecimalFormat money = new DecimalFormat("#,###,###");
        int amount = Integer.parseInt(total);
        String lastMoney = money.format(amount);

        dialogBerhasil = new Dialog(PopUpBayarHutangTransaksi.this);
        dialogBerhasil.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBerhasil.setContentView(dialogView);
        dialogBerhasil.setCanceledOnTouchOutside(true);

        transaksiBagianPelangganAdapter = new TransaksiBagianPelangganAdapter(this,customerModelList,this);

        tvnamapelangganhutangterbayar = dialogView.findViewById(R.id.nama_pelanggan_hutang_terbayar);
        tvsisahutangpelanggan = dialogView.findViewById(R.id.sisa_hutang_pelanggan);
        tvnamapelangganhutangterbayar.setText(getIntent().getStringExtra("namapelanggan"));
        tvsisahutangpelanggan.setText("Rp."+money.format(amount));

        kembali = dialogView.findViewById(R.id.btn_hutang_terbayar_kembali);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                playClickSound();
                dialogBerhasil.dismiss();
//                transaksiBagianPelangganAdapter.clearModels();
//                transaksiBagianPelangganAdapter.setItems(customerModelList);
//                transaksiBagianPelangganAdapter.notifyDataSetChanged();
                ((TransaksiBagianPelangganAdapter)transaksiBagianPelangganAdapter).notifyDataSetChanged();

                Intent intent = getIntent();
                intent.putExtra("MyAdapter", "sukses");
                setResult(RESULT_OK, intent);
                Intent intentBalik = new Intent(PopUpBayarHutangTransaksi.this, CustomerHutangActivity.class);
                startActivity(intentBalik);
//                Intent intentHutang = new Intent(PopUpBayarHutangTransaksi.this, PopUpCetakHutangActivity.class);
//                startActivity(intentHutang);
                cetakStruk();
            }
        });

        DisplayMetrics displayMetricsPW = this.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetricsPW.widthPixels * 0.5);
        int dialogHeight = (int)(displayMetricsPW.heightPixels * 0.6);
        dialogBerhasil.getWindow().setLayout(dialogWidth, dialogHeight);

        dialogBerhasil.show();
        dialogBerhasil.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialogBerhasil.getWindow().setBackgroundDrawableResource(android.R.color.white);
    }

    private void cetakStruk(){
        final String detailToko = SavePref.readDetailToko(PopUpBayarHutangTransaksi.this);
        final UserDetailModel tokoDetailModel = new Gson().fromJson(detailToko, UserDetailModel.class);
        int a = Integer.parseInt(getIntent().getStringExtra("hutang"));
        int b= Integer.parseInt(getIntent().getStringExtra("creditPaid"));
        String total;
        int sum  ;
        sum = a - b;
        total = Integer.toString(sum);
        final DecimalFormat format = new DecimalFormat("#,###,###");


        final int amount = Integer.parseInt(total);
        Thread t = new Thread() {
            public void run() {
                try {
                    resetConnection();
                    if(SavePref.readDeviceAddress(PopUpBayarHutangTransaksi.this)!=null){
                        bluetoothAddress = SavePref.readDeviceAddress(PopUpBayarHutangTransaksi.this);
                        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        mBluetoothDevice= mBluetoothAdapter.getRemoteDevice(bluetoothAddress);
                        try {
                            Date current = Calendar.getInstance().getTime();
                            SimpleDateFormat dateFormat1 = new SimpleDateFormat("EEE, dd MMM yyyy");
                            final String formattedDate = dateFormat1.format(current);
                            mBluetoothSocket = createBluetoothSocket(mBluetoothDevice);
                            mBluetoothSocket.connect();
                            if (mBluetoothSocket.isConnected()){

                                outputStream = mBluetoothSocket
                                        .getOutputStream();

                                printNewLine();

                                printCustom(tokoDetailModel.getShopName(),2,0);
                                printCustom(""+tokoDetailModel.getStreet()+", "+
                                        tokoDetailModel.getVillage()+", "+
                                        tokoDetailModel.getDistrict()+", "+
                                        tokoDetailModel.getProvince(),0,0);
                                printText("Telp. "+tokoDetailModel.getPhoneNumber()+"\n");
                                printNewLine();
                                printText(getIntent().getStringExtra("namapelanggan")+"\n");
                                printText("------------------------------------------\n");
                                printText("Hutang Sebelumnya : Rp."+format.format(amount)+"\n");
                                printText("Bayar : Rp."+format.format(Integer.parseInt(String.valueOf(et_inputhutang.getText())))+"\n");
                                printText("Sisa Hutang Anda : Rp."+format.format(amount-(Integer.parseInt(String.valueOf(et_inputhutang.getText()))))+"\n");
                                printText("------------------------------------------\n");
                                printText("------------------------------------------\n");


                                printNewLine();
                                printText("TERIMAKASIH SUDAH MEMBELI PRODUK PERTANIAN DI TOKO KAMI.\n\n");
                                printCustom("=== LAYANAN KONSUMEN KAMI ===",0,1);
                                printText("SMS/TELP "+tokoDetailModel.getPhoneNumber()+"\n");
                                printText("Dicetak Tanggal :"+formattedDate+"\n");
                                printNewLine();
                                printNewLine();
                                printNewLine();
                                outputStream.flush();

                            }
                        } catch (IOException e) {
                            try {
                                Toast.makeText(PopUpBayarHutangTransaksi.this, "Tidak dapat terhubung dengan Bluetooth Printer", Toast.LENGTH_SHORT).show();
                                mBluetoothSocket.close();
                                Log.e("Bluetooth","Can't Connect");
                            } catch (IOException e1) {
                                e1.printStackTrace();
                                Toast.makeText(PopUpBayarHutangTransaksi.this, "Tidak dapat terhubung dengan Bluetooth Socket", Toast.LENGTH_SHORT).show();
                                Log.e("Bluetooth","Socket can't closed");
                            }
                        }
                    } else {
                        pairingBluetooth();
                    }

                } catch (Exception e) {
                    Log.e("Main", "Exe ", e);
                }
            }
        };
        t.start();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
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

    private void printCustom(String msg, int size, int align) {
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
    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void printText(String msg) {
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

    private void pairingBluetooth() {
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
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PopUpBayarHutangTransaksi.this);
        alertDialog.setTitle("Pilih Device");

        ArrayAdapter adapter = new ArrayAdapter(PopUpBayarHutangTransaksi.this, android.R.layout.select_dialog_singlechoice,
                bluetoothList.toArray(new String[bluetoothList.size()]));

        alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                final String deviceAddress = bluetoothDeviceList.get(position);
                SavePref.saveDeviceAddress(PopUpBayarHutangTransaksi.this, deviceAddress);

                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothDevice= mBluetoothAdapter.getRemoteDevice(deviceAddress);

                try {
                    mBluetoothSocket = createBluetoothSocket(mBluetoothDevice);
                    mBluetoothSocket.connect();
                    Toast.makeText(PopUpBayarHutangTransaksi.this, "Berhasil terhubung dengan Bluetooth Printer", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                } catch (IOException e) {
                    try {
                        Toast.makeText(PopUpBayarHutangTransaksi.this, "Tidak dapat terhubung dengan Bluetooth Printer", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        mBluetoothSocket.close();
                        Log.e("Bluetooth","Can't Connect");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        Toast.makeText(PopUpBayarHutangTransaksi.this, "Tidak dapat terhubung dengan Bluetooth Socket", Toast.LENGTH_SHORT).show();
                        Log.e("Bluetooth","Socket can't closed");
                    }
                }

            }
        });
        alertDialog.show();
    }

    private void resetConnection() {
        if (outputStream != null) {
            try {outputStream.close();} catch (Exception e) {}
            outputStream = null;
        }
        if (mBluetoothSocket != null) {
            try {mBluetoothSocket.close();} catch (Exception e) {}
            mBluetoothSocket = null;
        }
    }

//    private void playClickSound() {
//        if (mp != null) {mp.stop(); mp.release(); mp = null;}
//        mp = MediaPlayer.create(PopUpBayarHutangTransaksi.this, R.raw.tap1);
//        mp.start();
//    }

    @Override
    public void onChangeClicker() {
    }
}
