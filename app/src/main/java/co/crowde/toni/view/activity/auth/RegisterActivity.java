package co.crowde.toni.view.activity.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.constant.Const;
import co.crowde.toni.model.DistrictModel;
import co.crowde.toni.model.ProvinceModel;
import co.crowde.toni.model.RegencyModel;
import co.crowde.toni.model.VillageModel;
import co.crowde.toni.network.LocationRequest;
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.otp.SendOtpRegisterActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    AppBarLayout appBarLayout;
    Toolbar toolbar;

    EditText etShopName, etShopAddress, etShopType,
            etShopOwner, etShopPhone, etShopPass, etShopRetypePass;
    static AutoCompleteTextView etShopProvince, etShopRegency, etShopDistrict, etShopVillage;
    CardView cvBtnRegister;
    ProgressDialog progressDialog;

    public static List<ProvinceModel> provinceModelList;
    public static List<RegencyModel> regencyModels;
    public static List<DistrictModel> districtModels;
    public static List<VillageModel> villageModels;

    public static String idProvince, idRegency, idDisctrict;

    boolean isEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        appBarLayout = findViewById(R.id.appBar);
        SetHeader.isLolipop(RegisterActivity.this, appBarLayout);

        toolbar = findViewById(R.id.toolbar);
        etShopName = findViewById(R.id.et_shop_name);
        etShopAddress = findViewById(R.id.et_shop_address);
        etShopType = findViewById(R.id.et_shop_type);
        etShopOwner = findViewById(R.id.et_shop_owner);
        etShopPhone = findViewById(R.id.et_shop_phone);
        etShopPass = findViewById(R.id.et_shop_set_pass);
        etShopRetypePass = findViewById(R.id.et_shop_retype_pass);
        etShopProvince = findViewById(R.id.et_shop_province);
        etShopRegency = findViewById(R.id.et_shop_regency);
        etShopDistrict = findViewById(R.id.et_shop_district);
        etShopVillage = findViewById(R.id.et_shop_village);
        cvBtnRegister = findViewById(R.id.cv_btn_register);

        progressDialog = new ProgressDialog(this);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cvBtnRegister.setOnClickListener(this);

        idProvince = "";
        idRegency = "";
        idDisctrict = "";

        regencyModels = new ArrayList<>();
        districtModels = new ArrayList<>();
        villageModels = new ArrayList<>();

        getProvince(RegisterActivity.this);
    }

    private void getProvince(Activity activity) {
        provinceModelList = new ArrayList<>();
        LocationRequest.getProvinceList(RegisterActivity.this);

        final ArrayAdapter<ProvinceModel> provinceAdapter =
                new ArrayAdapter<ProvinceModel>(activity, android.R.layout.simple_list_item_1, provinceModelList);
        etShopProvince.setAdapter(provinceAdapter);

        etShopProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getId = null, getName = null;
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(etShopProvince.getAdapter().getItem(position)));
                    getId = obj.getString("id");
                    getName = obj.getString("nama");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                idProvince = getId;
                LocationRequest.getRegencyList(activity, idProvince);
            }
        });
    }

    public static void getRegency(Activity activity){

        final ArrayAdapter<RegencyModel> regencyAdapter =
                new ArrayAdapter<RegencyModel>(activity, android.R.layout.simple_list_item_1, regencyModels);
        etShopRegency.setAdapter(regencyAdapter);

        etShopRegency.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getId = null, getName = null;
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(etShopRegency.getAdapter().getItem(position)));
                    getId = obj.getString("id");
                    getName = obj.getString("nama");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                idRegency = getId;
                LocationRequest.getDistrictList(activity, idRegency);
            }
        });

    }

    public static void getDistrict(Activity activity){

        final ArrayAdapter<DistrictModel> districtAdapter =
                new ArrayAdapter<DistrictModel>(activity, android.R.layout.simple_list_item_1, districtModels);
        etShopDistrict.setAdapter(districtAdapter);

        etShopDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getId = null, getName = null;
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(etShopDistrict.getAdapter().getItem(position)));
                    getId = obj.getString("id");
                    getName = obj.getString("nama");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                idDisctrict = getId;
                LocationRequest.getVillageList(activity, idDisctrict);
            }
        });

    }

    public static void getVillage(Activity activity){

        final ArrayAdapter<VillageModel> villageAdapter =
                new ArrayAdapter<VillageModel>(activity, android.R.layout.simple_list_item_1, villageModels);
        etShopVillage.setAdapter(villageAdapter);

        etShopVillage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getId = null, getName = null;
                try {
                    JSONObject obj = new JSONObject(new Gson().toJson(etShopVillage.getAdapter().getItem(position)));
                    getId = obj.getString("id");
                    getName = obj.getString("nama");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv_btn_register:
                register();
                break;
        }

    }

    private void register() {

        isEmpty = false;

        if(etShopName.getText().toString().length()==0){
            isEmpty = true;
        } else if(etShopAddress.getText().toString().isEmpty()) {
            isEmpty = true;
        } else if(etShopType.getText().toString().isEmpty()) {
            isEmpty = true;
        } else if(etShopOwner.getText().toString().isEmpty()) {
            isEmpty = true;
        } else if(etShopPhone.getText().toString().isEmpty()) {
            isEmpty = true;
        } else if(etShopPass.getText().toString().isEmpty()) {
            isEmpty = true;
        } else if(etShopRetypePass.getText().toString().isEmpty()) {
            isEmpty = true;
        } else if(etShopProvince.getText().toString().isEmpty()) {
            isEmpty = true;
        } else if(etShopVillage.getText().toString().isEmpty()) {
            isEmpty = true;
        } else if(etShopRegency.getText().toString().isEmpty()) {
            isEmpty = true;
        } else if(etShopDistrict.getText().toString().isEmpty()) {
            isEmpty = true;
        }

        new Handler().postDelayed(() -> {
            if(!isEmpty){
                progressDialog.setMessage("Harap tunggu...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_LOGIN, Const.LABEL_LOGIN_FAILED_NETWORK);

                Intent register = new Intent(RegisterActivity.this, SendOtpRegisterActivity.class);
                startActivity(register);
                progressDialog.dismiss();
            }
        }, 1000);
    }
}
