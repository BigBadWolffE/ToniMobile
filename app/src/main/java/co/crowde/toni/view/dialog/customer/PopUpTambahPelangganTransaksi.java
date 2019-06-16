package co.crowde.toni.view.dialog.customer;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

import co.crowde.toni.R;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.helper.volley.AppController;
import co.crowde.toni.network.API;
//import soedja.crowde.tokotani.R;
//import soedja.crowde.tokotani.app.AppConst;
//import soedja.crowde.tokotani.app.AppController;
//import soedja.crowde.tokotani.helpers.Save;

public class PopUpTambahPelangganTransaksi extends AppCompatActivity {

    EditText et_namapelanggan;
    EditText et_nomortelpon;
    LinearLayout tambahbutton;
    TextView textbutton;
    Dialog sudahada;
    CardView btntutup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_tambah_pelanggan_transaksi);

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.9),(int) (height*.5));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        et_namapelanggan = (EditText) findViewById(R.id.et_tambahpelanggannama);
        et_nomortelpon = (EditText) findViewById(R.id.et_tambahpelanggantelpon);
        tambahbutton = (LinearLayout) findViewById(R.id.btn_tambahpelanggan);
        textbutton = (TextView)findViewById(R.id.textbutton);

        tambahbutton.setEnabled(false);
        et_namapelanggan.addTextChangedListener(textWatcher);
        et_nomortelpon.addTextChangedListener(textWatcher);




        tambahbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                textbutton.setTextColor(getResources().getColor(R.color.colorWhite));
                tambahbutton.setBackgroundColor(getResources().getColor(R.color.colorThemeOrange));

                if (TextUtils.isEmpty(et_namapelanggan.getText().toString())){
                    Toast.makeText(PopUpTambahPelangganTransaksi.this, "Nama Tidak Kosong !", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(et_nomortelpon.getText().toString())){
                    Toast.makeText(PopUpTambahPelangganTransaksi.this,"Nomor Telpon Tidak Kosong !", Toast.LENGTH_SHORT).show();
                }else{

                   String testUrl = API.CUSTOMERLIST_URL;

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


                                        if(status){


                                            Toast.makeText(PopUpTambahPelangganTransaksi.this, "Pelanggan Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                                            Intent intent = getIntent();
                                            intent.putExtra("key","sukses");
                                            setResult(RESULT_OK,intent);
                                            finish();


                                        }else  {
                                            if (message.equals("Internal Error") ){
                                                Toast.makeText(PopUpTambahPelangganTransaksi.this,"Pelanggan Sudah Ditambahkan", Toast.LENGTH_SHORT).show();
                                                sudahada();
                                            }


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
                        public String getBodyContentType() {
                            return "application/x-www-form-urlencoded; charset=UTF-8";
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Authorization", SavePref.readToken(PopUpTambahPelangganTransaksi.this));
                            return params;
                        }

                        @Override
                        protected Map<String, String> getParams()throws AuthFailureError {
                            Map<String, String> params1 = new HashMap<String, String>();

                            params1.put("shopId", SavePref.readShopId(PopUpTambahPelangganTransaksi.this));
                            params1.put("customerName",et_namapelanggan.getText().toString());
                            params1.put("phone",et_nomortelpon.getText().toString());
                            params1.put("address","");
                            Log.e("params sent",new Gson().toJson(params1));

                            return params1;
                        }



                    };
                    AppController.getInstance().addToRequestQueue(request);


                }
            }
        });

    }

    private void sudahada() {

        final LayoutInflater inflaterUser = LayoutInflater.from(PopUpTambahPelangganTransaksi.this);
        final View dialogView = inflaterUser.inflate(R.layout.dialog_sudah_tambah_pelanggan, null);
        DisplayMetrics displayMetricsPW = this.getResources().getDisplayMetrics();
        sudahada = new Dialog(PopUpTambahPelangganTransaksi.this);
        sudahada.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sudahada.setContentView(dialogView);
        sudahada.setCanceledOnTouchOutside(true);
        int dialogWidth = (int) (displayMetricsPW.widthPixels * 0.9);
        int dialogHeight = (int) (displayMetricsPW.heightPixels * 0.4);
        sudahada.getWindow().setLayout(dialogWidth, dialogHeight);
        btntutup = (CardView)dialogView.findViewById(R.id.btntutup);
        btntutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_namapelanggan.getText().length() > 0 && et_nomortelpon.getText().length() > 0) {
                tambahbutton.setBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
                tambahbutton.setEnabled(true);
            } else {
                tambahbutton.setBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
                tambahbutton.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
