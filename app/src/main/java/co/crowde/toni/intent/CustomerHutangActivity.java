package co.crowde.toni.intent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import co.crowde.toni.R;
import co.crowde.toni.adapter.TransaksiBagianPelangganAdapter;
import co.crowde.toni.controller.main.PrintController;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.helper.volley.AppController;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.ShopModel;
import co.crowde.toni.network.API;
import co.crowde.toni.utils.PrinterCommands;
import co.crowde.toni.utils.PrinterNetwork;
import co.crowde.toni.utils.UtilsImage;
import co.crowde.toni.view.dialog.message.printer.PrinterConnectivityDialog;

public class CustomerHutangActivity extends AppCompatActivity {

    TextView tv_namapelangganhutang;
    TextView tv_totalpelangganhutang;
    TextView tv_nomorponsel;
    TextView tv_tanggalterdaftar;
    CardView btn_bayarhutang,btncetakstrukdialog;
    RelativeLayout btn_cetakstruk;
    TextView totalhutang, sisahutang, konfirmya, konfirmtidak,totalhutangcetak,konfirmyacetak,konfirmtidakcetak;
    EditText bayarhutang;
    CardView btn_dialogbayar;
    WindowManager.LayoutParams params;
    RelativeLayout mainLayout;
    TextView tvnamapelangganhutangterbayar, tvsisahutangpelanggan;
    TransaksiBagianPelangganAdapter transaksiBagianPelangganAdapter;
    TextView kembali;
    MediaPlayer mp;
    List<CustomerModel> customerModelList;
    Dialog dialogBayarHutang, dialogconfirmhutang,dialoghutangcetak,dialogconfirmcetakhutang;
    //Bluetooth
    protected static final String TAG = "TAG";
    public static BluetoothAdapter mBluetoothAdapter= null;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    public static BluetoothSocket mBluetoothSocket = null;
    public static BluetoothDevice mBluetoothDevice;
    public static OutputStream outputStream = null;
    String bluetoothAddress;
    DecimalFormat formatNumber;

    int hutangtotal;
    ImageButton btn_back;
    CustomerModel customerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_hutang);

        customerModel = getIntent().getParcelableExtra(CustomerModel.class.getSimpleName());

//        int totalHutang = getIntent().getIntExtra("totalhutang",0);
//        int hutangTerbayar = getIntent().getIntExtra("hutangterbayar",0);
//        String total;
//        int sum  ;
//        sum = totalHutang - hutangTerbayar;
//        total = Integer.toString(sum);
//        final int amount = Integer.parseInt(total);
        final int amount = customerModel.getCredit() - customerModel.getCreditPaid();
        hutangtotal = amount;

        tv_namapelangganhutang = (TextView) findViewById(R.id.namapelanggandetailhutang);
        tv_namapelangganhutang.setText(customerModel.getCustomerName());
        tv_nomorponsel = (TextView) findViewById(R.id.nomorponseldetailhutang);
        tv_nomorponsel.setText(customerModel.getPhone());
        tv_tanggalterdaftar = (TextView) findViewById(R.id.tanggalterdaftardetailhutang);

        //date setup
        String tanggal = customerModel.getCreatedAt();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date date = null;
        try {
            date = dateFormat.parse(tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DecimalFormat money = new DecimalFormat("#,###,###");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //If you need time just put specific format for time like 'HH:mm:ss'
        String dateStr = formatter.format(date);
        btn_back = (ImageButton) findViewById(R.id.back_hutang);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_tanggalterdaftar.setText(dateStr);
        tv_totalpelangganhutang = (TextView) findViewById(R.id.totalhutangdetailpelanggan);
        tv_totalpelangganhutang.setText("Rp." + money.format(amount));
        btn_bayarhutang = (CardView) findViewById(R.id.btntransaksihutangpelanggan);
        btn_bayarhutang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDialog();
            }
        });
        btn_cetakstruk = (RelativeLayout) findViewById(R.id.btncetakstrukhutang);
        btn_cetakstruk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCetakHutang();
            }
        });

        if (amount == 0) {
            btn_bayarhutang.setVisibility(View.GONE);
            btn_cetakstruk.setVisibility(View.GONE);
        } else {
            btn_bayarhutang.setVisibility(View.VISIBLE);
            btn_cetakstruk.setVisibility(View.VISIBLE);
        }
    }

    private void loadDialog() {
        final LayoutInflater inflaterUser = LayoutInflater.from(CustomerHutangActivity.this);
        final View dialogView = inflaterUser.inflate(R.layout.dialog_bayar_hutang, null);
        DisplayMetrics displayMetricsPW = this.getResources().getDisplayMetrics();
        dialogBayarHutang = new Dialog(CustomerHutangActivity.this);
        dialogBayarHutang.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBayarHutang.setContentView(dialogView);
        dialogBayarHutang.setCanceledOnTouchOutside(true);
        int dialogWidth = (int) (displayMetricsPW.widthPixels * 0.9);
        int dialogHeight = (int) (displayMetricsPW.heightPixels * 0.5);
        dialogBayarHutang.getWindow().setLayout(dialogWidth, dialogHeight);
//
//        int a = getIntent().getIntExtra("totalhutang",0);
//        int b = getIntent().getIntExtra("hutangterbayar",0);
//        String total1;
//        int sum  ;
//        sum = a - b;
//        total1 = Integer.toString(sum);
        DecimalFormat money = new DecimalFormat("#,###,###");
        final int amount1 = customerModel.getCredit() - customerModel.getCreditPaid();

        totalhutang = (TextView) dialogView.findViewById(R.id.tagihan);
        totalhutang.setText("Rp." + money.format(customerModel.getCredit() - customerModel.getCreditPaid()));
        bayarhutang = (EditText) dialogView.findViewById(R.id.input_bayar);

        btn_dialogbayar = (CardView) dialogView.findViewById(R.id.btnbayarhutang);
        btn_dialogbayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(bayarhutang.getText().toString()) > hutangtotal) {
                    Toast.makeText(CustomerHutangActivity.this, "Nilai Lebih Besar dari Hutang", Toast.LENGTH_SHORT).show();
                } else {

                    bayarhutang.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            CloseSoftKeyboard.hideSoftKeyboard(v, CustomerHutangActivity.this);
                        }
                    });
//                    hideKeyboard(CustomerHutangActivity.this);
                    konfirmasiBayar();
                }

            }
        });
//        final int amount2 = Integer.parseInt(bayarhutang.getText().toString());
//        sisahutang.setText("Rp."+ money.format(amount1-amount2));

        btn_dialogbayar.setEnabled(false);
        bayarhutang.addTextChangedListener(textWatcher);

        dialogBayarHutang.show();
        dialogBayarHutang.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialogBayarHutang.getWindow().setBackgroundDrawableResource(android.R.color.white);

    }

    private void konfirmasiBayar() {
        customerModelList = new ArrayList<>();
        final LayoutInflater inflaterUser = LayoutInflater.from(CustomerHutangActivity.this);
        final View dialogView = inflaterUser.inflate(R.layout.layout_konfirmasi_bayar, null);
        DisplayMetrics displayMetricsPW = this.getResources().getDisplayMetrics();
        dialogconfirmhutang = new Dialog(CustomerHutangActivity.this);
        dialogconfirmhutang.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogconfirmhutang.setContentView(dialogView);
        dialogconfirmhutang.setCanceledOnTouchOutside(true);
        int dialogWidth = (int) (displayMetricsPW.widthPixels * 0.9);
        int dialogHeight = (int) (displayMetricsPW.heightPixels * 0.4);
        dialogconfirmhutang.getWindow().setLayout(dialogWidth, dialogHeight);
        konfirmya = (TextView) dialogView.findViewById(R.id.confirmya);
        konfirmtidak = (TextView) dialogView.findViewById(R.id.confirmtidak);
        transaksiBagianPelangganAdapter = new TransaksiBagianPelangganAdapter(CustomerHutangActivity.this, customerModelList, this);

        konfirmya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(bayarhutang.getText().toString())) {
                    Toast.makeText(CustomerHutangActivity.this, "Hutang Tidak Boleh Kosong !", Toast.LENGTH_SHORT).show();
                } else {
                    resetConnection();
                    String testUrl = API.BAYARHUTANG_URL;

                    StringRequest request = new StringRequest(
                            Request.Method.POST,
                            testUrl,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {

                                        JSONObject objLogin = new JSONObject(response);
                                        boolean status = objLogin.getBoolean("status");
                                        String data = objLogin.getString("data");
                                        String message = objLogin.getString("message");
                                        if (status) {

                                            Toast.makeText(CustomerHutangActivity.this, "Hutang Berhasil Di Update", Toast.LENGTH_SHORT).show();
                                            loadDialog();
                                            Intent intent = getIntent();
                                            intent.putExtra("lock", "good");
                                            setResult(RESULT_OK, intent);
                                            finish();
//                                            transaksiBagianPelangganAdapter.notifyDataSetChanged();
//                                            Intent selesai = new Intent(CustomerHutangActivity.this, CustomerDiversionActivity.class);
//                                            startActivity(selesai);
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
                            params.put("Authorization", SavePref.readToken(CustomerHutangActivity.this));
                            return params;
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params1 = new HashMap<String, String>();

                            params1.put("shopId", SavePref.readShopId(CustomerHutangActivity.this));
                            params1.put("customerId", customerModel.getCustomerId());
                            params1.put("amount", bayarhutang.getText().toString());
                            Log.e("params sent", new Gson().toJson(params1));

                            return params1;
                        }
                    };
                    AppController.getInstance().addToRequestQueue(request);
                }

            }
        });


        dialogconfirmhutang.show();
        dialogconfirmhutang.getWindow().setBackgroundDrawableResource(android.R.color.white);


    }
    private void dialogCetakHutang(){
        final LayoutInflater inflaterUser = LayoutInflater.from(CustomerHutangActivity.this);
        final View dialogView = inflaterUser.inflate(R.layout.layout_cetak_hutang, null);
        DisplayMetrics displayMetricsPW = this.getResources().getDisplayMetrics();
        dialoghutangcetak = new Dialog(CustomerHutangActivity.this);
        dialoghutangcetak.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialoghutangcetak.setContentView(dialogView);
        dialoghutangcetak.setCanceledOnTouchOutside(true);
        int dialogWidth = (int) (displayMetricsPW.widthPixels * 0.9);
        int dialogHeight = (int) (displayMetricsPW.heightPixels * 0.5);
        dialoghutangcetak.getWindow().setLayout(dialogWidth, dialogHeight);

        DecimalFormat money = new DecimalFormat("#,###,###");
        final int amount1 = customerModel.getCredit() - customerModel.getCreditPaid();

        totalhutangcetak = (TextView) dialogView.findViewById(R.id.tagihancetak);
        totalhutangcetak.setText("Rp." + money.format(customerModel.getCredit() - customerModel.getCreditPaid()));

        btncetakstrukdialog = (CardView)dialogView.findViewById(R.id.btncetakstrukdialog);
        btncetakstrukdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cetakHutang(CustomerHutangActivity.this);
            }
        });



        dialoghutangcetak.show();
        dialoghutangcetak.getWindow().setBackgroundDrawableResource(android.R.color.white);

    }
    private void cetakHutang(Activity activity){

        final LayoutInflater inflaterUser = LayoutInflater.from(CustomerHutangActivity.this);
        final View dialogView = inflaterUser.inflate(R.layout.layout_konfirmasi_cetak_hutang, null);
        DisplayMetrics displayMetricsPW = this.getResources().getDisplayMetrics();
        dialogconfirmcetakhutang = new Dialog(CustomerHutangActivity.this);
        dialogconfirmcetakhutang.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogconfirmcetakhutang.setContentView(dialogView);
        dialogconfirmcetakhutang.setCanceledOnTouchOutside(true);
        int dialogWidth = (int) (displayMetricsPW.widthPixels * 0.9);
        int dialogHeight = (int) (displayMetricsPW.heightPixels * 0.5);
        dialogconfirmcetakhutang.getWindow().setLayout(dialogWidth, dialogHeight);
        final String detailToko = SavePref.readDetailToko(CustomerHutangActivity.this);
        final ShopModel shopModel = new Gson().fromJson(detailToko, ShopModel.class);
        konfirmyacetak = (TextView) dialogView.findViewById(R.id.confirmyacetakhutang);
        konfirmyacetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetConnection();
                if(SavePref.readDeviceAddress(activity)!=null){
                    PrinterNetwork.bluetoothAddress = SavePref.readDeviceAddress(activity);
                    PrinterNetwork.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    PrinterNetwork.mBluetoothDevice= PrinterNetwork.mBluetoothAdapter.getRemoteDevice(PrinterNetwork.bluetoothAddress);
                    try {
                        PrinterNetwork.mBluetoothSocket = PrinterNetwork.createBluetoothSocket(PrinterNetwork.mBluetoothDevice);
                        PrinterNetwork.mBluetoothSocket.connect();
                        if (PrinterNetwork.mBluetoothSocket.isConnected()){
                            PrintController.printCustomerCredit(activity, customerModel);
                        }
                    } catch (IOException e) {
                        PrinterConnectivityDialog.showDialog(activity);
//                                                ConfirmTransactionDialog.progressDialog.dismiss();
                        Log.e("Bluetooth","Can't Connect");
                    }

                } else {
                    PrinterNetwork.pairingBluetooth(activity);
                }
            }
        });

        konfirmtidakcetak = (TextView)dialogView.findViewById(R.id.confirmtidakcetakhutang);
        konfirmtidakcetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });









        dialogconfirmcetakhutang.show();
        dialogconfirmcetakhutang.getWindow().setBackgroundDrawableResource(android.R.color.white);


    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (bayarhutang.getText().length() > 0) {
                btn_dialogbayar.setBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
                btn_dialogbayar.setEnabled(true);
            } else {
                btn_dialogbayar.setBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
                btn_dialogbayar.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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

    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if(bmp!=null){
                byte[] command = UtilsImage.decodeBitmap(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
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
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerHutangActivity.this);
        alertDialog.setTitle("Pilih Device");

        ArrayAdapter adapter = new ArrayAdapter(CustomerHutangActivity.this, android.R.layout.select_dialog_singlechoice,
                bluetoothList.toArray(new String[bluetoothList.size()]));

        alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                final String deviceAddress = bluetoothDeviceList.get(position);
                SavePref.saveDeviceAddress(CustomerHutangActivity.this, deviceAddress);

                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                mBluetoothDevice= mBluetoothAdapter.getRemoteDevice(deviceAddress);

                try {
                    mBluetoothSocket = createBluetoothSocket(mBluetoothDevice);
                    mBluetoothSocket.connect();
                    Toast.makeText(CustomerHutangActivity.this, "Berhasil terhubung dengan Bluetooth Printer", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                } catch (IOException e) {
                    try {
                        Toast.makeText(CustomerHutangActivity.this, "Tidak dapat terhubung dengan Bluetooth Printer", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        mBluetoothSocket.close();
                        Log.e("Bluetooth","Can't Connect");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        Toast.makeText(CustomerHutangActivity.this, "Tidak dapat terhubung dengan Bluetooth Socket", Toast.LENGTH_SHORT).show();
                        Log.e("Bluetooth","Socket can't closed");
                    }
                }

            }
        });
        alertDialog.show();
    }


}
