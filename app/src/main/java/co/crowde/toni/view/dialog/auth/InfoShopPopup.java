package co.crowde.toni.view.dialog.auth;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import co.crowde.toni.R;
import co.crowde.toni.controller.main.UserController;
import co.crowde.toni.network.API;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.ShopModel;
import co.crowde.toni.view.activity.home.MainActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class InfoShopPopup {

    public static TextView tvInfoShopName, tvInfoOwnername;
    public static CircleImageView imgInfoShop;
    public static ArrayAdapter<CharSequence> adapter;

    public static void showInfoShop(final Activity activity){

        final LayoutInflater inflater = LayoutInflater.from(activity);
        final View dialogView = inflater.inflate(
                R.layout.layout_popup_info_user, null);

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
        dialog.getWindow().setGravity(Gravity.TOP | Gravity.END);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnime3;
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);

        tvInfoShopName = dialogView.findViewById(R.id.tvInfoShopName);
        tvInfoOwnername = dialogView.findViewById(R.id.tvInfoOwnername);
        imgInfoShop = dialogView.findViewById(R.id.imgInfoShop);
        ListView listInfoUser = dialog.findViewById(R.id.listInfoUser);
        adapter = ArrayAdapter.createFromResource(activity,
                R.array.info_user,
                android.R.layout.simple_list_item_1);
        listInfoUser.setAdapter(adapter);

        listInfoUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        infouser(activity);
                        break;

                    case 1:
                        pairingBluetooth(activity);
                        break;

                    case 2:
                        offlineMode(activity);
                        break;

                    case 3:
                        sync(activity);
                        break;

                    case 4:
                        closeShop(activity);
                        break;
                }
            }
        });

        ShopModel shopModel = new Gson()
                .fromJson(SavePref.readUserDetail(activity), ShopModel.class);
        tvInfoShopName.setText(shopModel.getShopName());
        tvInfoOwnername.setText(shopModel.getOwnerName());

        Picasso.with(activity.getBaseContext())
                .load(API.Host+SavePref.readPicture(activity))
                .into(imgInfoShop);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public static void showUserPopup(final Activity activity){
        final PopupMenu popup = new PopupMenu(activity, MainActivity.imgShop);
        popup.getMenuInflater().inflate(R.menu.left_menu_nav, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuInfoToko:
                        infouser(activity);
                        break;

                    case R.id.menuBluetooth:
                        pairingBluetooth(activity);
                        break;

                    case R.id.menuOffMode:
                        offlineMode(activity);
                        break;

                    case R.id.menuSync:
                        sync(activity);
                        break;

                    case R.id.menuLogout:
                        closeShop(activity);
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    public static void infouser(Activity activity) {
//        Toast.makeText(activity, "Informasi Pengguna", Toast.LENGTH_SHORT).show();
        ShopDetailPopup.showDetailShop(activity);
    }

    public static void pairingBluetooth(Activity activity) {
        Toast.makeText(activity, "Koneksi Bluetooth", Toast.LENGTH_SHORT).show();
    }

    public static void offlineMode(Activity activity) {
        Toast.makeText(activity, "Mode Offline", Toast.LENGTH_SHORT).show();
    }

    public static void sync(Activity activity) {
        Toast.makeText(activity, "Sinkronisasi", Toast.LENGTH_SHORT).show();
    }

    public static void closeShop(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Tutup Toko")
                .setMessage("Apakah Anda ingin menutup Toko sekarang?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        UserController.closedShop(activity);
                    }
                }).create().show();
    }
}
