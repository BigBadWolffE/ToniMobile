package co.crowde.toni.view.popup;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import co.crowde.toni.R;
import co.crowde.toni.controller.main.ProductController;
import co.crowde.toni.controller.network.CategoryRequest;

public class FilterInventoryPopup {

    public static TextView tvHeaderFilter, tvCategoryLabel, tvStatusLabel;
    public static ImageView imgBtnBack;
    public static ChipGroup chipCategory, chipStatus;
    public static ArrayList<Boolean> booleanArrayList = new ArrayList<>();
    public static ArrayList<String> statusList = new ArrayList<>();
    public static String[] status = {"Tersedia", "Mulai habis", "Habis"};

    public static void showFilterInventory(final Activity activity) {
        final LayoutInflater inflater = LayoutInflater.from(activity);
        final View dialogView = inflater.inflate(
                R.layout.layout_filter_inventory, null);

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimateSlide;
        dialog.show();

        //Get View Id
        tvHeaderFilter = dialogView.findViewById(R.id.tvHeaderFilter);
        tvCategoryLabel = dialogView.findViewById(R.id.tvCategoryLabel);
        tvStatusLabel = dialogView.findViewById(R.id.tvStatusLabel);
        imgBtnBack = dialogView.findViewById(R.id.imgBtnBack);
        chipCategory = dialogView.findViewById(R.id.chipCategory);
        chipStatus = dialogView.findViewById(R.id.chipStatus);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        CategoryRequest.getCategoryInventory(activity);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ProductController.filterInventory(activity);
            }
        });

        filterStatusInventory(activity);

        tvStatusLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, new Gson().toJson(statusList), Toast.LENGTH_SHORT).show();

            }
        });



        }

    private static void filterStatusInventory(final Activity activity) {
        for (int i=0;i<status.length;i++){
            final Chip chip = new Chip(activity);
            chip.setId(i);
            chip.setTag(i);

            chip.setText(status[i]);
            chip.setCheckable(true);
            booleanArrayList.add(false);

            for(int j=0; j<statusList.size();j++){
                if(chip.getText().equals(statusList.get(j))){
                    chip.setChecked(true);
                    chip.setChipBackgroundColor(ColorStateList
                            .valueOf(activity.getResources()
                                    .getColor(R.color.colorThemeGreen)));
                    chip.setTextColor(ColorStateList
                            .valueOf(activity.getResources()
                                    .getColor(R.color.colorWhite)));
                }

            }

            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    int tag = (int) compoundButton.getTag();
                    booleanArrayList.set(tag, b);

                    if(b){
                        statusList.add(chip.getText().toString());
                        ProductController.statusInventory.add(chip.getText().toString());
                        chip.setChipBackgroundColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorThemeGreen)));
                        chip.setTextColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorWhite)));
                    } else {
                        statusList.remove(chip.getText().toString());
                        ProductController.statusInventory.remove(chip.getText().toString());
                        chip.setChipBackgroundColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorThemeGreyLight)));
                        chip.setTextColor(ColorStateList
                                .valueOf(activity.getResources()
                                        .getColor(R.color.colorBlack)));
                    }

                    Log.e("StatusStock",new Gson().toJson(ProductController.statusInventory));
                }
            });
            chipStatus.addView(chip);
        }
    }
}
