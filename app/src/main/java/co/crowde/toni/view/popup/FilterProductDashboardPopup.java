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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.controller.main.ProductController;
import co.crowde.toni.controller.network.CategoryRequest;
import co.crowde.toni.helper.ChipsCategory;
import co.crowde.toni.view.fragment.Dashboard;

public class FilterProductDashboardPopup {

    public static TextView tvHeaderFilter, tvCategoryLabel;
    public static ImageView imgBtnBack;
    public static ChipsInput chipsInput;
    public static List<ChipsCategory> categories = new ArrayList<>();
//    public static ChipGroup chipCategory;

    public static void showFilterCategory(final Activity activity) {
        final LayoutInflater inflater = LayoutInflater.from(activity);
        final View dialogView = inflater.inflate(
                R.layout.layout_filter_product_dashboard, null);

        final Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimateSlide;
        dialog.show();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //Get View Id
        tvHeaderFilter = dialogView.findViewById(R.id.tvHeaderFilter);
        tvCategoryLabel = dialogView.findViewById(R.id.tvCategoryLabel);
        imgBtnBack = dialogView.findViewById(R.id.imgBtnBack);
//        chipCategory = dialogView.findViewById(R.id.chipCategory);
        chipsInput = (ChipsInput) dialogView.findViewById(R.id.chipsCategory);

        imgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        CategoryRequest.getCategoryList(activity);
        chipsInput.addChipsListener(new ChipsInput.ChipsListener() {
            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {
                // chip added
                // newSize is the size of the updated selected chip list
            }

            @Override
            public void onChipRemoved(ChipInterface chip, int newSize) {
                // chip removed
                // newSize is the size of the updated selected chip list
            }

            @Override
            public void onTextChanged(CharSequence text) {
                // text changed
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ProductController.filterProductDashboard(activity);

            }
        });

        }
}