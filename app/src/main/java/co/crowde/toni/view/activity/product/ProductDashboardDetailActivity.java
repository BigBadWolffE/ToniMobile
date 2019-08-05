package co.crowde.toni.view.activity.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;


import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import co.crowde.toni.R;
import co.crowde.toni.controller.cart.CartController;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.network.API;
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.view.activity.filter.InventoryFilterActivity;

public class ProductDashboardDetailActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    AppBarLayout appBarLayout;
    Toolbar toolbar;
    ImageView imgProduct, imgDecrease, imgIncrease, imgIsDiscount;
    TextView tvProductPrice, tvDiscount, tvProductCategory, tvProductStock ,tvProductDesc, tvAddAmount, tvRp, tvAmount, tvTotal;
    Group groupTextDiscount, groupDiscountExpand, groupAmount;
    CardView cvBtnAddCart;
    EditText etQty, etDiscount;
    ConstraintLayout layoutDiscount;
    WebView webView;

    ProductModel productModel;

    int amount, qty, price, discount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_dashboard_detail);

        productModel = getIntent().getParcelableExtra(ProductModel.class.getSimpleName());

        amount = 0;
        qty = 0;
        price = productModel.getSellingPrice();
//        discount = 0;

        appBarLayout = findViewById(R.id.appBar);
        SetHeader.isLolipop(ProductDashboardDetailActivity.this, appBarLayout);

        imgProduct = findViewById(R.id.img_product);
        imgDecrease = findViewById(R.id.imgDecrease);
        imgIncrease = findViewById(R.id.imgIncrease);
        imgIsDiscount = findViewById(R.id.img_is_discount);
        tvProductPrice = findViewById(R.id.tv_product_price);
        tvDiscount = findViewById(R.id.tv_product_discount);
        tvProductCategory = findViewById(R.id.tv_product_category);
        tvProductStock = findViewById(R.id.tv_product_stock);
        tvAddAmount = findViewById(R.id.tv_add);
        groupTextDiscount = findViewById(R.id.group_text_discount);
        groupDiscountExpand = findViewById(R.id.group_discount_expand);
        cvBtnAddCart = findViewById(R.id.cv_btn_add_cart);
        etQty = findViewById(R.id.et_qty);
        layoutDiscount = findViewById(R.id.layout_discount);
        etDiscount = findViewById(R.id.et_discount);
        tvRp = findViewById(R.id.label_rp);
        groupAmount = findViewById(R.id.group_amount);
        tvAmount = findViewById(R.id.tv_amount_product);
        tvTotal = findViewById(R.id.tv_amount_price);
        webView = findViewById(R.id.tv_product_desc);

        toolbar = findViewById(R.id.toolbar);

        String product = productModel.getProductName();
        String nama;
        String varian;
        if(product.contains("_")){
            nama = StringUtils.substringBeforeLast(product, "_");
            varian = StringUtils.substringAfterLast(product, "_");

            toolbar.setTitle(nama+" ("+varian+")");
        } else {
            nama = product;
            varian = productModel.getUnit();

            toolbar.setTitle(nama);
        }
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Picasso.with(ProductDashboardDetailActivity.this).load(API.Host+productModel.getPicture())
                .into(imgProduct);

        DecimalFormatRupiah.changeFormat(ProductDashboardDetailActivity.this);
        tvProductPrice.setText("Rp. "+
                String.valueOf(DecimalFormatRupiah.formatNumber.format(productModel.getSellingPrice())));
        tvProductCategory.setText(productModel.getCategoryName());
        tvProductStock.setText("Stok: "+productModel.getStock());

        String description = "<html><body style='text-align:justify;font-size:12px;'>"+productModel.getDescription()+"</body></html>";
        String dataString = String.format(Locale.US, description, "my html with text justification");
        webView.loadDataWithBaseURL("", dataString, "text/html", "UTF-8", "");

        tvAddAmount.setText("Tambah \n"+amount);
        etQty.setText("0");

        imgIncrease.setOnClickListener(this);
        imgDecrease.setOnClickListener(this);
//        layoutDiscount.setOnClickListener(this);
        cvBtnAddCart.setOnClickListener(this);

//        etDiscount.addTextChangedListener(discountWatcher);
        etQty.setOnClickListener(this);
        etQty.addTextChangedListener(qtyWatcher);

        enableBtnAddCart();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgDecrease:
                if(etQty.getText().length()>0){
                    qty = Integer.parseInt(etQty.getText().toString());
                    qty = qty-1;
                    if(qty>0){
                        etQty.setText(""+qty);
                        etQty.setSelection(etQty.getText().length());
                    } else {
                        qty=0;
                        etQty.setText("0");
                        etQty.setSelection(etQty.getText().length());
                    }
                } else if(etQty.getText().length()==0){
                    etQty.setText("0");
                    qty = Integer.parseInt(etQty.getText().toString());
                    qty = qty-1;
                    if(qty>0){
                        etQty.setText(""+qty);
                        etQty.setSelection(etQty.getText().length());
                    } else {
                        qty=0;
                        etQty.setText("0");
                        etQty.setSelection(etQty.getText().length());
                    }
                }
                showAmount();
                enableBtnAddCart();
                break;

            case R.id.imgIncrease:
                if(etQty.getText().length()>0){
                    qty = Integer.parseInt(etQty.getText().toString());
                    qty = qty+1;
                    if(qty>0){
                        etQty.setText(""+qty);
                        etQty.setSelection(etQty.getText().length());
                    } else {
                        qty=0;
                        etQty.setText("0");
                        etQty.setSelection(etQty.getText().length());
                    }
                } else if(etQty.getText().length()==0){
                    etQty.setText("0");
                    qty = Integer.parseInt(etQty.getText().toString());
                    qty = qty+1;
                    if(qty>0){
                        etQty.setText(""+qty);
                        etQty.setSelection(etQty.getText().length());
                    } else {
                        qty=0;
                        etQty.setText("0");
                        etQty.setSelection(etQty.getText().length());
                    }
                }
                showAmount();
                enableBtnAddCart();
                break;

            case R.id.layout_discount:
                if(groupDiscountExpand.getVisibility()==View.VISIBLE){
                    groupDiscountExpand.setVisibility(View.GONE);
                    imgIsDiscount.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                    etDiscount.setText("");
                } else if(groupDiscountExpand.getVisibility()==View.GONE){
                    groupDiscountExpand.setVisibility(View.VISIBLE);
                    imgIsDiscount.setImageResource(R.drawable.ic_check_box_black_24dp);
                }
                break;

            case R.id.et_qty:
                if(etQty.getText().toString().equals("0")){
                    etQty.setText("");
                }
                etQty.setSelection(etQty.getText().length());
                break;

            case R.id.cv_btn_add_cart:
                CartController.addFromDetail(ProductDashboardDetailActivity.this, productModel, qty);
                break;
        }

    }

    private void enableBtnAddCart() {
        if(qty>0){
            cvBtnAddCart.setEnabled(true);
            cvBtnAddCart.setCardBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
        } else {
            cvBtnAddCart.setEnabled(false);
            cvBtnAddCart.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
        }
    }

    public TextWatcher qtyWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etQty.getText().length() > 0) {
                qty = Integer.parseInt(etQty.getText().toString());
            } else if(etQty.getText().length()==0){
                qty = 0;
            }
            showAmount();
            enableBtnAddCart();
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void showAmount() {
        if(qty>0){
            groupAmount.setVisibility(View.VISIBLE);
            amount = price*qty;
            tvAmount.setText("Rp. "+
                    String.valueOf(DecimalFormatRupiah.formatNumber.format(amount)));
            tvTotal.setText("Rp. "+
                    String.valueOf(DecimalFormatRupiah.formatNumber.format(amount)));

        } else {
            groupAmount.setVisibility(View.GONE);
            amount=0;
            tvAmount.setText("");
            tvTotal.setText("");
        }
    }

    public TextWatcher discountWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etDiscount.getText().length() > 3) {

                int disc = Integer.parseInt(etDiscount.getText().toString());

                if(disc<productModel.getSellingPrice()){
                    groupTextDiscount.setVisibility(View.VISIBLE);
                    tvRp.setVisibility(View.VISIBLE);
                    setPrice();
                }

            } else {
                groupTextDiscount.setVisibility(View.GONE);
                tvRp.setVisibility(View.GONE);
                setPrice();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void setPrice(){
        if(etDiscount.getText().length()>3){
            discount = Integer.parseInt(etDiscount.getText().toString());
            price = productModel.getSellingPrice();
            price = price-discount;

            tvProductPrice.setText("Rp. "+price);
            tvDiscount.setText("Rp. "+productModel.getSellingPrice());
            tvProductPrice.setTextColor(getResources().getColor(R.color.color_DEFF3D3D));
        } else {
            price = productModel.getSellingPrice();
            discount = 0;

            tvProductPrice.setText("Rp. "+price);
            tvProductPrice.setTextColor(getResources().getColor(R.color.colorThemeOrange));
        }

    }
}
