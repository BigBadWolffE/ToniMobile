package co.crowde.toni.view.activity.auth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import co.crowde.toni.R;
import co.crowde.toni.base.BaseActivity;
import co.crowde.toni.constant.Const;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.AdminModel;
import co.crowde.toni.model.CityModel;
import co.crowde.toni.model.DistrictModel;
import co.crowde.toni.model.NewAccountModel;
import co.crowde.toni.model.NewShopModel;
import co.crowde.toni.model.ProvinceModel;
import co.crowde.toni.model.VillageModel;
import co.crowde.toni.network.LocationRequest;
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.otp.SendOtpRegisterActivity;

import static co.crowde.toni.network.LocationRequest.generateToken;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    AppBarLayout appBarLayout;
    Toolbar toolbar;

    public static EditText etShopName, etShopAddress,
            etShopOwner, etShopPhone, etShopPass, etShopRetypePass,etUserName,etEmail;
    public static Spinner etShopRegency, etShopDistrict, etShopVillage;
    public static CardView cvBtnRegister, cvBtnGenerateToken,cvBtnCancelDialog;
    public static ProgressDialog progressDialog;

    public static String email ;
    public static String phoneNumber;


    public static String emailPattern;
    public static String phonePattern;

    public static Spinner etShopType;

    public static String patternEmail = "[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+";
    public static String patternPhone = "08[0-9]{9,12}";


    // Province
    public static String idProvince, paramsProvince;
    public static List<ProvinceModel> provinceModelList;
    public static AutoCompleteTextView actProvince;
    public static ConstraintLayout clProvince;
    public static TextView tvLabelProvince;
    public static ImageView imgProvinceRemove;


    // City
    public static String idCity, paramsCity;
    public static List<CityModel> cityModelList;
    public static AutoCompleteTextView actCity;
    public static ConstraintLayout clCity;
    public static TextView tvLabelCity;
    public static ImageView imgCityRemove;

    // District
    public static String idDistrict, paramsDistrict;
    public static List<DistrictModel> districtModelList;
    public static AutoCompleteTextView actDistrict;
    public static ConstraintLayout clDistrict;
    public static TextView tvLabelDistrict;
    public static ImageView imgDistrictRemove;

    // Village
    public static String idVillage, paramsVillage;
    public static List<VillageModel> villageModelList;
    public static AutoCompleteTextView actVillage;
    public static ConstraintLayout clVillage;
    public static TextView tvLabelVillage;
    public static ImageView imgVillageRemove;

    //Pattern
    public static Pattern emailCustom;
    public static Pattern phoneCustom;
    public static Pattern ownerNameCustom;

    public static boolean isValidEmailId(String email){
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isValidPhone(String phone){
        return (!TextUtils.isEmpty(phone) && Patterns.PHONE.matcher(phone).matches());
    }



    String usernameAdmin, passwordAdmin;

    public static String pass;
    public static String retypepass;


    boolean isEmpty;


    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        cvBtnRegister = findViewById(R.id.cv_btn_register);
        etUserName = findViewById(R.id.et_user_name);
        etEmail = findViewById(R.id.et_email_user);


        pass = etShopPass.getText().toString();
        retypepass = etShopRetypePass.getText().toString();

        emailCustom
                = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,500}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,500}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,500}" +
                        ")+"
        );

        phoneCustom
                = Pattern.compile("08"+"[0-9]{9,13}");

        ownerNameCustom
                = Pattern.compile("^[a-zA-Z\\s]*$");

//        cvBtnGenerateToken = findViewById(R.id.cv_btn_generate_token);

        progressDialog = new ProgressDialog(this);

//        etEmail.setError("ex:toni@example.com");
//        etShopPhone.setError("Gunakan format(08)");




//        else if (!phoneCustom.matcher(etShopPhone.getText().toString()).matches()) {
//            etShopPhone.setError("Nomor Telpon Salah");
//        }else if (!emailCustom.matcher(etEmail.getText().toString()).matches()){
//            etEmail.setError("Email Salah");
//        }

        email = etEmail.getText().toString();
        phoneNumber = etShopPhone.getText().toString();


        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (source != null && !emailPattern.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };

//        if (etEmail.getText().toString().length() != 0 ){
//
//        }
        //Phone and Email Watcher


        etEmail.addTextChangedListener(emailWatcher);
        etEmail.requestFocus();
        etEmail.setError(null);

//        etEmail.setError(null);
//        etEmail.clearFocus();
        etShopPhone.addTextChangedListener(phoneWatcher);
        etShopOwner.addTextChangedListener(ownerNameWatcher);




        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String[] arraySpinner = new String[]{
                "Pilih Jenis Bisnis","Besar", "Sedang", "Kecil"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        etShopType.setAdapter(adapter);

//        TextView errorText = (TextView)etShopType.getSelectedView();
//        errorText.setError("");
//        errorText.setTextColor(Color.RED);//just to highlight that this is an error
//        errorText.setText("my actual error text");

        cvBtnRegister.setOnClickListener(this);

//        cvBtnGenerateToken.setOnClickListener(this);

        // Province
        actProvince = findViewById(R.id.act_province);
        clProvince = findViewById(R.id.cl_province);
        tvLabelProvince = findViewById(R.id.label_selected_province);
        imgProvinceRemove = findViewById(R.id.img_label_province_remove);
        LocationRequest.getProvinceList(RegisterActivity.this);

        // City
        actCity = findViewById(R.id.act_city);
        clCity = findViewById(R.id.cl_city);
        tvLabelCity = findViewById(R.id.label_selected_city);
        imgCityRemove = findViewById(R.id.img_label_city_remove);

        // District
        actDistrict = findViewById(R.id.act_district);
        clDistrict = findViewById(R.id.cl_district);
        tvLabelDistrict = findViewById(R.id.label_selected_district);
        imgDistrictRemove = findViewById(R.id.img_label_district_remove);

        // Village
        actVillage = findViewById(R.id.act_village);
        clVillage = findViewById(R.id.cl_village);
        tvLabelVillage = findViewById(R.id.label_selected_village);
        imgVillageRemove = findViewById(R.id.img_label_village_remove);
    }



//    public static void getProvince(Activity activity,List<ProvinceModel> provinceList) {
//        idProvince = "";
//        paramsProvince = "";
//        provinceModelList = new ArrayList<>();
//        provinceModelList.addAll(provinceList);
//
//        final ArrayAdapter<ProvinceModel> provinceAdapter =
//                new ArrayAdapter<ProvinceModel>(activity, android.R.layout.simple_list_item_1, provinceModelList);
//        spinnerProvince.setAdapter(provinceAdapter);
//
//        etShopProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String getId = null, getName = null;
//                try {
//                    JSONObject obj = new JSONObject(new Gson().toJson(etShopProvince.getAdapter().getItem(position)));
//                    getId = obj.getString("id");
//                    getName = obj.getString("nama");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                idProvince = getId;
//                LocationRequest.getCitiesList(activity, idProvince);
//            }
//        });
//    }


    public static void setProvinceAdapter(Activity activity, List<ProvinceModel> provinceList) {
        idProvince = "";
        paramsProvince = "";
        provinceModelList = new ArrayList<>();
        provinceModelList.addAll(provinceList);
        ArrayAdapter<ProvinceModel> adapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_dropdown_item_1line, provinceModelList);
        actProvince.setAdapter(adapter);

        actProvince.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    actProvince.showDropDown();

            }
        });

        actProvince.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                actProvince.showDropDown();
                return false;
            }
        });


        actProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject selectedProvince = new JSONObject(new Gson().toJson(actProvince.getAdapter().getItem(position)));
                    idProvince = selectedProvince.getString("province_id");
                    paramsProvince = selectedProvince.getString("province_id") + " - " + selectedProvince.getString("province");
                    System.out.println("Selected Province: " + idProvince);
                    System.out.println("Selected Province: " + paramsProvince);
                    actProvince.setVisibility(View.GONE);
                    tvLabelProvince.setText(selectedProvince.getString("province"));
                    clProvince.setVisibility(View.VISIBLE);

                    LocationRequest.getCitiesList(activity, idProvince);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        imgProvinceRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idProvince = "";
                paramsProvince = "";
                actProvince.setText("");
                actProvince.setVisibility(View.VISIBLE);
                clProvince.setVisibility(View.GONE);
                idCity = "";
                paramsCity = "";
                actCity.setText("");
                actCity.setVisibility(View.VISIBLE);
                clCity.setVisibility(View.GONE);
                idDistrict = "";
                paramsDistrict = "";
                actDistrict.setText("");
                actDistrict.setVisibility(View.VISIBLE);
                clDistrict.setVisibility(View.GONE);
                idVillage = "";
                paramsVillage = "";
                actVillage.setText("");
                actVillage.setVisibility(View.VISIBLE);
                clVillage.setVisibility(View.GONE);
            }
        });
    }

    public static void setCityAdapter(Activity activity, List<CityModel> cityList) {
        idCity = "";
        paramsCity = "";
        cityModelList = new ArrayList<>();
        cityModelList.addAll(cityList);
        ArrayAdapter<CityModel> adapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_dropdown_item, cityModelList);
        actCity.setAdapter(adapter);

        actCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    if(actProvince.getText().toString().trim().length()!=0){
                        actCity.showDropDown();
                    }

            }
        });

        actCity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(actProvince.getText().toString().trim().length()!=0){
                    actCity.showDropDown();
                }
                return false;
            }
        });
        actCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject selected = new JSONObject(new Gson().toJson(actCity.getAdapter().getItem(position)));
                    idCity = selected.getString("city_id");
                    paramsCity = selected.getString("city_id") + " - " + selected.getString("city");
                    System.out.println("Selected City: " + idCity);
                    System.out.println("Selected City: " + paramsCity);
                    actCity.setVisibility(View.GONE);
                    tvLabelCity.setText(selected.getString("city"));
                    clCity.setVisibility(View.VISIBLE);

                    LocationRequest.getDistrictList(activity, idCity);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        imgCityRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idCity = "";
                paramsCity = "";
                actCity.setText("");
                actCity.setVisibility(View.VISIBLE);
                clCity.setVisibility(View.GONE);
                idDistrict = "";
                paramsDistrict = "";
                actDistrict.setText("");
                actDistrict.setVisibility(View.VISIBLE);
                clDistrict.setVisibility(View.GONE);
                idVillage = "";
                paramsVillage = "";
                actVillage.setText(" ");
                actVillage.setVisibility(View.VISIBLE);
                clVillage.setVisibility(View.GONE);
            }
        });
    }

    public static void setDistrictAdapter(Activity activity, List<DistrictModel> districtList) {
        idDistrict = "";
        paramsDistrict = "";
        districtModelList = new ArrayList<>();
        districtModelList.addAll(districtList);
        ArrayAdapter<DistrictModel> adapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_dropdown_item, districtModelList);
        actDistrict.setAdapter(adapter);


        actDistrict.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    if(actCity.getText().toString().trim().length()!=0){
                        actDistrict.showDropDown();
                    }
            }
        });

        actDistrict.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(actCity.getText().toString().trim().length()!=0){
                    actDistrict.showDropDown();
                }
                return false;
            }
        });

        actDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject selected = new JSONObject(new Gson().toJson(actDistrict.getAdapter().getItem(position)));
                    idDistrict = selected.getString("district_id");
                    paramsDistrict = selected.getString("district_id") + " - " + selected.getString("district");
                    System.out.println("Selected District: " + idDistrict);
                    System.out.println("Selected District: " + paramsDistrict);
                    actDistrict.setVisibility(View.GONE);
                    tvLabelDistrict.setText(selected.getString("district"));
                    clDistrict.setVisibility(View.VISIBLE);

                LocationRequest.getVillageList(activity, idDistrict);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        imgDistrictRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idDistrict = "";
                paramsDistrict = "";
                actDistrict.setText("");
                actDistrict.setVisibility(View.VISIBLE);
                clDistrict.setVisibility(View.GONE);
                idVillage = "";
                paramsVillage = "";
                actVillage.setText(" ");
                actVillage.setVisibility(View.VISIBLE);
                clVillage.setVisibility(View.GONE);
            }
        });

    }

    public static void setVillageAdapter(Activity activity, List<VillageModel> villageList) {
        idVillage = "";
        paramsVillage = "";
        villageModelList = new ArrayList<>();
        villageModelList.addAll(villageList);
        ArrayAdapter<VillageModel> adapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_dropdown_item, villageModelList);
        actVillage.setAdapter(adapter);

        actVillage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    if(actDistrict.getText().toString().trim().length()!=0){
                        actVillage.showDropDown();
                    }
            }
        });

        actVillage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(actDistrict.getText().toString().trim().length()!=0){
                    actVillage.showDropDown();
                }
                return false;
            }
        });

        actVillage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject selected = new JSONObject(new Gson().toJson(actVillage.getAdapter().getItem(position)));
                    idVillage = selected.getString("idkel");
                    paramsVillage = selected.getString("idkel") + " - " + selected.getString("urban_village");
                    System.out.println("Selected Village: " + idCity);
                    System.out.println("Selected Village: " + paramsCity);
                    actVillage.setVisibility(View.GONE);
                    tvLabelVillage.setText(selected.getString("urban_village"));
                    clVillage.setVisibility(View.VISIBLE);

//                LocationRequest.getDistrictList(activity, idCity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        imgVillageRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idVillage = "";
                paramsVillage = "";
                actVillage.setText(" ");
                actVillage.setVisibility(View.VISIBLE);
                clVillage.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_btn_register:
                regist();
                break;
        }

    }

    private void generatetoken() {

        usernameAdmin = "admin";
        passwordAdmin = "4dm1n_toko_tani";

        AdminModel adminModel = new AdminModel();
        adminModel.setUsername(usernameAdmin);
        adminModel.setPassword(passwordAdmin);

        generateToken(RegisterActivity.this, adminModel);
        gantitombol();

    }

    private void gantitombol() {

        if (SavePref.readTokenAdmin(RegisterActivity.this) != null) {

            cvBtnGenerateToken.setVisibility(View.GONE);
            cvBtnRegister.setVisibility(View.VISIBLE);


        }

    }

    public void regist(){
        isEmpty = false;
        AnalyticsToniUtils.getEvent(Const.CATEGORY_AUTHENTIFICATION, Const.MODUL_LOGIN, Const.LABEL_LOGIN);
        if (etShopName.getText().toString().trim().length() == 0){
            isEmpty = true;
            Toast.makeText(this,"Lengkapi Data",Toast.LENGTH_SHORT).show();
        }else if (etShopAddress.getText().toString().trim().length() == 0){
            isEmpty = true;
            Toast.makeText(this,"Lengkapi Data",Toast.LENGTH_SHORT).show();
        } else if (paramsProvince.trim().length() == 0){
            actProvince.setError("Data Provinsi Anda Tidak Sesuai");
            isEmpty = true;
            Toast.makeText(this,"Lengkapi Data Provinsi",Toast.LENGTH_SHORT).show();
        } else if (paramsCity.trim().length() == 0){
            actCity.setError("Data Kota/Kabupaten Anda Tidak Sesuai");
            isEmpty = true;
            Toast.makeText(this,"Lengkapi Data Kota/Kabupaten",Toast.LENGTH_SHORT).show();
        } else if (paramsDistrict.trim().length() == 0){
            actDistrict.setError("Data Kecamatan Anda Tidak Sesuai");
            isEmpty = true;
            Toast.makeText(this,"Lengkapi Data Kecamatan",Toast.LENGTH_SHORT).show();
        }else if (paramsVillage.trim().length() == 0) {
            actVillage.setError("Data Kelurahan Anda Harus Di isi ");
            isEmpty = true;
            Toast.makeText(this,"Lengkapi Data Kelurahan",Toast.LENGTH_SHORT).show();
        } else if (etShopOwner.getText().toString().trim().length()==0){
            isEmpty = true;
            etShopOwner.setError("Nama Pemilik Harus Sesuai KTP");
        }
        else if(etUserName.getText().toString().trim().length() == 0){
            etUserName.setError("User Name Harus Di isi");
            isEmpty = true;
            Toast.makeText(this,"Lengkapi Data User Name",Toast.LENGTH_SHORT).show();
        }else if (etShopType.getSelectedItem().equals("Pilih Jenis Bisnis")){
            ((TextView)etShopType.getSelectedView()).setError("Jenis Bisnis Salah");
            isEmpty = true;
            Toast.makeText(this,"Pilih Jenis Bisnis Anda!",Toast.LENGTH_SHORT).show();
        } else if(!phoneCustom.matcher(etShopPhone.getText().toString()).matches()) {
            isEmpty = true;
            etShopPhone.setError("Isi dengan nomor handphone kamu diawali dengan 08(08123456789)");
        } else if (etShopPass.getText().toString().length()<6){
            isEmpty = true;
            etShopPass.setError("Input minimal 6 karakter");
            Toast.makeText(this,"Input minimal 6 karakter",Toast.LENGTH_SHORT).show();
        }else if (!etShopRetypePass.getText().toString().equals(etShopPass.getText().toString())){
            isEmpty = true;
            etShopRetypePass.setError("Kata Sandi Tidak Sama");
        }new Handler().postDelayed(() -> {
                if(!isEmpty){
                    progressDialog.setMessage("Harap tunggu...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    NewShopModel newShopModel = new NewShopModel();
                    NewAccountModel newAccountModel = new NewAccountModel();

                    LocationRequest.postShopData(RegisterActivity.this,newShopModel,newAccountModel);
                }
            },1000);


        }


    public static void alertInvalid (final Context activity ){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage("Nomor Telpon atau Data Anda Sudah Terdaftar");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Dismiss",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        progressDialog.dismiss();
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void alertInvalidOtp (final Context activity ){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage("OTP Anda Salah!");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Dismiss",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SendOtpRegisterActivity.progressDialog.dismiss();
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void alertSuccessResendOtp (final Context activity ){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage("OTP SUDAH DIKIRIM ULANG");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Dismiss",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    public TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {


//                if (etEmail.getText().toString().trim().length()>0){
//                    etEmail.setError(null);
//                }else
                if(etEmail.getText().toString().trim().length() != 0)
                if (!emailCustom.matcher(etEmail.getText().toString()).matches()){
                    etEmail.setError("anda@kamu.com");
                }else {
                    etEmail.setError(null);
                }

        }
    };

    public TextWatcher phoneWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (etShopPhone.getText().toString()!=null){
                if (!phoneCustom.matcher(etShopPhone.getText().toString()).matches()){
                    etShopPhone.setError("Isi dengan nomor handphone kamu diawali dengan 08(08123456789)");
                }
            }else {
                etShopPhone.setError(null);
            }


        }

        @Override
        public void afterTextChanged(Editable s) {




        }
    };
    public TextWatcher ownerNameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {


//                if (etEmail.getText().toString().trim().length()>0){
//                    etEmail.setError(null);
//                }else
            if(etShopOwner.getText().toString().trim().length() != 0)
                if (!ownerNameCustom.matcher(etShopOwner.getText().toString()).matches()){
                    etShopOwner.setError("Format nama tidak tepat");
                }else {
                    etShopOwner.setError(null);
                }

        }
    };








}
