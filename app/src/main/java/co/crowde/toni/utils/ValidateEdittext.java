package co.crowde.toni.utils;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class ValidateEdittext {

    public static boolean validateLogin(EditText etUsername, EditText etPassword){
        if(etUsername.getText().toString().length()==0){
            return false;
        } else return etPassword.getText().toString().length() != 0;
    }

    public static boolean validateResetPass(EditText etPhone){
        return etPhone.getText().toString().length() > 9;
    }

    public static boolean validateCreateNewPass(EditText etNewPass, EditText etReTypePass){
        if(etNewPass.getText().toString().length()==0){
            return false;
        } else if(etNewPass.getText().toString().length()==0){
            return false;
        } else return etReTypePass.getText().toString().equals(etNewPass.getText().toString());
    }

    public static boolean validateRegister(EditText etShopName,
                                           EditText etShopAddress,
                                           AutoCompleteTextView etShopProvince,
                                           AutoCompleteTextView etShopRegency,
                                           AutoCompleteTextView etShopDistrict,
                                           AutoCompleteTextView etShopVillage,
                                           EditText etShopType,
                                           EditText etShopOwner,
                                           EditText etShopPhone,
                                           EditText etShopPass,
                                           EditText etShopRePass){
        if(etShopName.getText().toString().length()==0){
            return false;
        } else if(etShopAddress.getText().toString().length()==0){
            return false;
        } else if(etShopProvince.getText().toString().length()==0){
            return false;
        } else if(etShopRegency.getText().toString().length()==0){
            return false;
        } else if(etShopDistrict.getText().toString().length()==0){
            return false;
        } else if(etShopVillage.getText().toString().length()==0){
            return false;
        } else if(etShopType.getText().toString().length()==0){
            return false;
        } else if(etShopOwner.getText().toString().length()==0){
            return false;
        } else if(etShopPhone.getText().toString().length()==0){
            return false;
        } else if(etShopPass.getText().toString().length()==0){
            return false;
        } else return etShopRePass.getText().toString().length() != 0;
    }
}
