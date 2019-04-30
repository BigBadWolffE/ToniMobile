package co.crowde.toni.view.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.CatalogProductAdapter;
import co.crowde.toni.adapter.ProductInventoryAdapter;
import co.crowde.toni.controller.main.ProductController;
import co.crowde.toni.controller.network.CatalogRequest;
import co.crowde.toni.controller.network.ProductRequest;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.model.CatalogModel;
import co.crowde.toni.model.ProductModel;

public class CatalogProduct {

    public static TextView tvHeaderCatalog,
            tvTabName, tvTabCategory, tvTabSupplier,
            tvBtnAddProduct;
    public static ImageView imgBtnBack,
            imgBtnFilter;
    public static EditText etSearchProduct;
    public static RecyclerView rcProduct;
    public static RecyclerView.LayoutManager rcLayoutDashboard;
    public static CatalogProductAdapter catalogProductAdapter;
    public static List<CatalogModel> productModels;
    public static List<CatalogModel> productModelsFiltered;
    public static CardView cvBtnAddProduct;

    public static Dialog dialog;

    static DividerItemDecoration itemDecorator;

    public static void showCatalog(final Activity activity, Context context){
        final LayoutInflater inflater = LayoutInflater.from(activity);
        final View dialogView = inflater.inflate(
                R.layout.activity_catalog_product, null);

        dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar);
        dialog.setContentView(dialogView);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimateSlide;
        dialog.show();

        tvHeaderCatalog = dialogView.findViewById(R.id.tvHeaderCatalog);
        tvTabName = dialogView.findViewById(R.id.tvTabName);
        tvTabCategory = dialogView.findViewById(R.id.tvTabCategory);
        tvTabSupplier = dialogView.findViewById(R.id.tvTabSupplier);
        tvBtnAddProduct = dialogView.findViewById(R.id.tvBtnAddProduct);
        imgBtnBack = dialogView.findViewById(R.id.imgBtnBack);
        imgBtnFilter = dialogView.findViewById(R.id.imgBtnFilter);
        etSearchProduct = dialogView.findViewById(R.id.etSearchProduct);
        rcProduct = dialogView.findViewById(R.id.rcProduct);
        cvBtnAddProduct = dialogView.findViewById(R.id.cvBtnAddProduct);

        //Get Product List
        itemDecorator = new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context,
                R.drawable.divider_line_item));
        productList(activity);

        //Hide Keyboard
        hideKeyboard(activity);

    }

    public static void productList(Activity activity){
//        if(ProductController.categoryInventory.isEmpty()){
            CatalogRequest.getCatalogList(activity);
            productModels = new ArrayList<>();
            catalogProductAdapter = new CatalogProductAdapter(activity,
                    productModels, activity);
            rcProduct.addItemDecoration(itemDecorator);
            rcProduct.setLayoutManager(new LinearLayoutManager(activity));
            rcProduct.setAdapter(catalogProductAdapter);
//            imgBtnFilter.setBackground(
//                    activity.getResources().getDrawable(R.color.colorWhite));
//            imgBtnFilter.setImageDrawable(
//                    activity.getResources().getDrawable(R.drawable.ic_filter_list_black_24dp));
//
//        } else {
//            inventoryAdapter = new ProductInventoryAdapter(activity,
//                    productModelsFiltered, activity);
//            rcProduct.addItemDecoration(itemDecorator);
//            rcProduct.setLayoutManager(new LinearLayoutManager(activity));
//            rcProduct.setAdapter(catalogProductAdapter);
//            imgBtnFilter.setBackground(
//                    activity.getResources().getDrawable(R.drawable.bg_rec_radius_5dp_green));
//            imgBtnFilter.setImageDrawable(
//                    activity.getResources().getDrawable(R.drawable.ic_filter_list_white_24dp));
//        }
    }

    public static void setEnabledButton(final Activity activity, int count){
        if(count>0){
            cvBtnAddProduct.setEnabled(true);
            cvBtnAddProduct.setCardBackgroundColor(activity.getResources().getColor(R.color.colorThemeOrange));

            cvBtnAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CatalogRequest.addNewProduct(activity);
                }
            });

        } else {
            cvBtnAddProduct.setEnabled(false);
            cvBtnAddProduct.setCardBackgroundColor(activity.getResources().getColor(R.color.colorThemeGrey));
        }
    }

    public static void hideKeyboard(final Activity activity){
        etSearchProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CloseSoftKeyboard.hideSoftKeyboard(v, activity);
            }
        });

    }

}
