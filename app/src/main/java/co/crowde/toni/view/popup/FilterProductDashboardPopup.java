package co.crowde.toni.view.popup;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import co.crowde.toni.R;
import co.crowde.toni.controller.network.CategoryRequest;

public class FilterProductDashboardPopup {

    public static TextView tvHeaderFilter, tvCategoryLabel;
    public static ImageView imgBtnBack;
    public static ChipGroup chipCategory;

    public static void showFilterCategory(final Activity activity) {
        final LayoutInflater inflater = LayoutInflater.from(activity);
        final View dialogView = inflater.inflate(
                R.layout.layout_filter_product_dashboard, null);

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimateSlide;
        dialog.show();

        //Get View Id
        tvHeaderFilter = dialogView.findViewById(R.id.tvHeaderFilter);
        tvCategoryLabel = dialogView.findViewById(R.id.tvCategoryLabel);
        imgBtnBack = dialogView.findViewById(R.id.imgBtnBack);
        chipCategory = dialogView.findViewById(R.id.chipCategory);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        CategoryRequest.getCategoryList(activity);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

        }
}
