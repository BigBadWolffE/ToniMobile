package co.crowde.toni.view.dialog.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import co.crowde.toni.R;
import co.crowde.toni.network.API;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.ShopModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShopDetailPopup {

    public static TextView tvShopeName,
            labelUsername, labelPhoneNumber, labelAddress, labelBusinessType,
            tvUsername, tvPhoneNumber, tvAddress, tvBusinessType;
    public static CircleImageView imgShop;
    public static ConstraintLayout constraintLayout;

    @SuppressLint("SetTextI18n")
    public static void showDetailShop(Activity activity){
        final LayoutInflater inflater = LayoutInflater.from(activity);
        boolean tabletSize = activity.getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        final View dialogView = inflater.inflate(
                R.layout.layout_shop_detail_popup, null);

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
        dialog.setContentView(dialogView);
        dialog.show();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimateFade;
        dialog.getWindow().setBackgroundDrawableResource(R.color.overlayBackground);

        imgShop = dialogView.findViewById(R.id.imgShop);
        tvShopeName = dialogView.findViewById(R.id.tvShopeName);
        labelUsername = dialogView.findViewById(R.id.labelUsername);
        labelPhoneNumber = dialogView.findViewById(R.id.labelPhoneNumber);
        labelAddress = dialogView.findViewById(R.id.labelAddress);
        labelBusinessType = dialogView.findViewById(R.id.labelBusinessType);
        tvUsername = dialogView.findViewById(R.id.tvUsername);
        tvPhoneNumber = dialogView.findViewById(R.id.tvPhoneNumber);
        tvAddress = dialogView.findViewById(R.id.tvAddress);
        tvBusinessType = dialogView.findViewById(R.id.tvBusinessType);
        constraintLayout = dialogView.findViewById(R.id.constraintLayout);

        ShopModel model = new Gson().fromJson(SavePref.readUserDetail(activity),
                ShopModel.class);

        Picasso.with(activity.getBaseContext())
                .load(API.Host+SavePref.readPicture(activity))
                .into(imgShop);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvShopeName.setText(model.getShopName());
        tvUsername.setText(model.getOwnerName());
        tvPhoneNumber.setText(model.getPhoneNumber());
        tvAddress.setText(model.getDistrict()+", "+
                model.getRegency()+", "+
                model.getProvince());
        tvBusinessType.setText(model.getBusinessType());

    }
}
