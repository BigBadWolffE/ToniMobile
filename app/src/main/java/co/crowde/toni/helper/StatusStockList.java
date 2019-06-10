package co.crowde.toni.helper;

import android.app.Activity;

import java.util.ArrayList;

public class StatusStockList {

    public static ArrayList<Boolean> booleanArrayList = new ArrayList<>();
    public static ArrayList<String> categoryList = new ArrayList<>();
    public static ArrayList<String> statusList = new ArrayList<>();
    public static String[] status = {"Semua","Tersedia", "Mulai habis", "Habis"};

    public static void getStatusList(final Activity activity){

//        for (int i=0;i<statusList.size();i++){
//            final Chip chip = new Chip(activity);
//            chip.setId(i);
//            chip.setTag(i);
//
//            chip.setText(statusList.get(i).getCategoryName());
//            chip.setCheckable(true);
//            booleanArrayList.add(false);
//
//            final int finalI = i;
//            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                    int tag = (int) compoundButton.getTag();
//                    booleanArrayList.set(tag, b);
//
//                    if(b){
//                        categoryList.add(statusList.get(finalI).getCategoryName());
//                        chip.setChipBackgroundColor(ColorStateList
//                                .valueOf(activity.getResources()
//                                        .getColor(R.color.colorThemeGreen)));
//                        chip.setTextColor(ColorStateList
//                                .valueOf(activity.getResources()
//                                        .getColor(R.color.colorWhite)));
//                    } else {
//                        categoryList.remove(categoryModels.get(finalI).getCategoryName());
//                        chip.setChipBackgroundColor(ColorStateList
//                                .valueOf(activity.getResources()
//                                        .getColor(R.color.colorThemeGreyLight)));
//                        chip.setTextColor(ColorStateList
//                                .valueOf(activity.getResources()
//                                        .getColor(R.color.colorBlack)));
//                    }
//                }
//            });
//            FilterProductDashboardPopup.chipCategory.addView(chip);
//
//            FilterProductDashboardPopup.tvHeaderFilter.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(activity, new Gson().toJson(categoryList), Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }
    }
}
