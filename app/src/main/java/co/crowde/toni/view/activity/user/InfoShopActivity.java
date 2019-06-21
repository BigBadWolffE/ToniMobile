package co.crowde.toni.view.activity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import co.crowde.toni.R;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.ShopModel;
import co.crowde.toni.network.API;
import de.hdodenhof.circleimageview.CircleImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

public class InfoShopActivity extends AppCompatActivity {

    TextView tvShopName, tvOwnerName, tvShopPhone, tvShopAddress, tvBusinessType;
    CircleImageView imgShop;
    Toolbar toolbarShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_shop);

        tvShopName = findViewById(R.id.tv_shop_name);
        tvOwnerName = findViewById(R.id.tv_shop_owner);
        tvShopPhone = findViewById(R.id.tv_shop_phone);
        tvShopAddress = findViewById(R.id.tv_shop_address);
        tvBusinessType = findViewById(R.id.tv_shop_business);
        toolbarShop = findViewById(R.id.toolbar);
        imgShop = findViewById(R.id.img_shop);

        ShopModel model = new Gson().fromJson(SavePref.readUserDetail(InfoShopActivity.this),
                ShopModel.class);

        Picasso.with(getBaseContext())
                .load(API.Host+SavePref.readPicture(InfoShopActivity.this))
                .into(imgShop);

        toolbarShop.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbarShop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvShopName.setText(model.getShopName());
        tvOwnerName.setText(model.getOwnerName());
        tvShopPhone.setText(model.getPhoneNumber());
        tvShopAddress.setText(model.getDistrict()+", "+
                model.getRegency()+", "+
                model.getProvince());
        tvBusinessType.setText(model.getBusinessType());

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
