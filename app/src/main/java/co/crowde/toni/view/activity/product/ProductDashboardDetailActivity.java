package co.crowde.toni.view.activity.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;


import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import co.crowde.toni.R;
import co.crowde.toni.controller.cart.CartController;
import co.crowde.toni.database.Cart;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.CartModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.network.API;
import co.crowde.toni.network.ProductRequest;
import co.crowde.toni.utils.InputFilterMinMax;
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.view.activity.filter.InventoryFilterActivity;
import co.crowde.toni.view.fragment.modul.DashboardFragment;

public class ProductDashboardDetailActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    AppBarLayout appBarLayout;
    Toolbar toolbar;
    ImageView imgProduct, imgDecrease, imgIncrease, imgIsDiscount;
    TextView tvProductPrice, tvProductCategory, tvProductStock, tvAddAmount, tvAmount, tvAmountDiscount, tvTotal, tvDiscountLabel;
    Group groupDiscountExpand, groupAmountDiscount;
    CardView cvBtnAddCart;
    EditText etQty, etDiscount;
    ConstraintLayout layoutDiscount;
    WebView webView;

    ProductModel productModel;
    CartModel cartModel;

    int amount, qty, price, discount, sub_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_dashboard_detail);

        imgProduct = findViewById(R.id.img_product);
        imgDecrease = findViewById(R.id.imgDecrease);
        imgIncrease = findViewById(R.id.imgIncrease);
        imgIsDiscount = findViewById(R.id.img_is_discount);
        tvProductPrice = findViewById(R.id.tv_product_price);
        tvProductCategory = findViewById(R.id.tv_product_category);
        tvProductStock = findViewById(R.id.tv_product_stock);
        tvAddAmount = findViewById(R.id.tv_add);
        groupDiscountExpand = findViewById(R.id.group_discount_expand);
        groupAmountDiscount = findViewById(R.id.group_discount_amount);
        cvBtnAddCart = findViewById(R.id.cv_btn_add_cart);
        etQty = findViewById(R.id.et_qty);
        layoutDiscount = findViewById(R.id.layout_discount);
        etDiscount = findViewById(R.id.et_discount);
        tvAmount = findViewById(R.id.tv_amount_product);
        tvAmountDiscount = findViewById(R.id.tv_amount_discount);
        tvTotal = findViewById(R.id.tv_amount_price);
        webView = findViewById(R.id.tv_product_desc);
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appBar);
        tvDiscountLabel = findViewById(R.id.label_manual_discount);

        cvBtnAddCart.setEnabled(false);

        SetHeader.isLolipop(ProductDashboardDetailActivity.this, appBarLayout);
        DecimalFormatRupiah.changeFormat(ProductDashboardDetailActivity.this);

        productModel = getIntent().getParcelableExtra(ProductModel.class.getSimpleName());

        price = productModel.getSellingPrice();
        qty = 0;
        amount = 0;
        sub_total = 0;
        discount = 0;
        getProductCart();
//        showDiscount();

        String product = productModel.getProductName();
        String nama;
        String varian;
        if (product.contains("_")) {
            nama = StringUtils.substringBeforeLast(product, "_");
            varian = StringUtils.substringAfterLast(product, "_");

            toolbar.setTitle(nama + " (" + varian + ")");
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

        Picasso.with(ProductDashboardDetailActivity.this).load(API.Host + productModel.getPicture())
                .into(imgProduct);
        tvProductPrice.setText("Rp. " + DecimalFormatRupiah.formatNumber.format(productModel.getSellingPrice()));
        tvProductCategory.setText(productModel.getCategoryName());
        tvProductStock.setText("Stok: " + productModel.getStock());

        if(productModel.getDescription()!=null){
            String description = "<html><body style='text-align:justify;font-size:12px;'>" + productModel.getDescription().replaceAll("%", "persen") + "</body></html>";
            String dataString = String.format(Locale.US, description, "my html with text justification");
            webView.loadDataWithBaseURL("", dataString, "text/html", "UTF-8", "");
        }

        imgIncrease.setOnClickListener(this);
        imgDecrease.setOnClickListener(this);
        tvDiscountLabel.setOnClickListener(this);
        cvBtnAddCart.setOnClickListener(this);

        etDiscount.addTextChangedListener(discountWatcher());
        etDiscount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (Integer.parseInt(etDiscount.getText().toString().replaceAll("[,.]", "")) <= sub_total) {
                        if (etDiscount.getText().length() > 0 && etDiscount.getText().length() < 11) {
                            discount = Integer.parseInt(
                                    etDiscount.getText().toString().replaceAll("[,.]", ""));
                            setDiscount();
                            setDiscountAmount();
                            setPrice();
                            setTotal();
                            setButton();
                        } else {
                            discount = 0;
                        }

                        CloseSoftKeyboard.hideSoftKeyboard(v, ProductDashboardDetailActivity.this);
                    } else if (etDiscount.getText().length() == 0) {
                        etDiscount.setText("");
                    } else {
                        Toast.makeText(ProductDashboardDetailActivity.this, "Diskon melebihi total pembelian.", Toast.LENGTH_SHORT).show();

                    }
                    return true;
                }
                return false;
            }
        });
        etQty.setOnClickListener(this);
        etQty.addTextChangedListener(qtyWatcher);

    }

    private void getProductCart() {
        String querySelect = "SELECT * FROM " + CartModel.TABLE_NAME + " WHERE productId='" + productModel.getProductId() + "'";
        DashboardFragment.dbCart = new Cart(this);
        SQLiteDatabase db = DashboardFragment.dbCart.getWritableDatabase();

        try (Cursor cursor = db.rawQuery(querySelect, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                cartModel = new CartModel(
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(CartModel.KEY_SHOP_ID)),
                        cursor.getString(cursor.getColumnIndex(CartModel.KEY_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndex(CartModel.KEY_PRODUCT_NAME)),
                        cursor.getString(cursor.getColumnIndex(CartModel.KEY_UNIT)),
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_STOK)),
                        cursor.getString(cursor.getColumnIndex(CartModel.KEY_PICTURE)),
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_QUANTITY)),
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_SELLING_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_AMOUNT)),
                        cursor.getInt(cursor.getColumnIndex(CartModel.KEY_DISCOUNT)));

                qty = cursor.getInt(cursor.getColumnIndex(CartModel.KEY_QUANTITY));
                price = cursor.getInt(cursor.getColumnIndex(CartModel.KEY_SELLING_PRICE));
                amount = cursor.getInt(cursor.getColumnIndex(CartModel.KEY_AMOUNT));
                sub_total = qty * price;
                setQty();
                setPrice();
                if (amount != sub_total) {
                    discount = cursor.getInt(cursor.getColumnIndex(CartModel.KEY_DISCOUNT));
                    groupAmountDiscount.setVisibility(View.GONE);
                    setTotal();
                    setButton();
                } else {
                    discount = 0;
                    groupAmountDiscount.setVisibility(View.VISIBLE);
                }
                showDiscount();
                setDiscount();
                setDiscountAmount();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgDecrease:
                if (qty > 0) {
                    if (amount >= price) {
                        qty = qty - 1;
                        setQty();
                        setPrice();
                        setTotal();
                        setButton();
                    }
                }
                break;

            case R.id.imgIncrease:
                qty = qty + 1;
                setQty();
                setPrice();
                setTotal();
                setButton();
                break;

            case R.id.label_manual_discount:
                if (qty > 0) {
                    showDiscount();
                    setDiscount();
                    setDiscountAmount();
                }
                break;

            case R.id.et_qty:
                if (etQty.getText().toString().equals("0")) {
                    etQty.setText("");
                }
                etQty.setSelection(etQty.getText().length());
                break;

            case R.id.cv_btn_add_cart:
                if (amount != sub_total) {
                    if (discount != productModel.getDiscount()) {
                        ProductRequest.putProductDiscount(this, productModel.getProductId(), discount);
                    }
                    CartController.addFromDetail(ProductDashboardDetailActivity.this, productModel, qty, discount);
                } else {
                    ProductRequest.putProductDiscount(this, productModel.getProductId(), 0);
                    CartController.addFromDetail(ProductDashboardDetailActivity.this, productModel, qty, 0);
                }

                break;
        }

    }

    public TextWatcher qtyWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etQty.getText().length() == 0) {
                qty = 0;
            } else if (etQty.getText().length() > 0) {
                qty = Integer.parseInt(etQty.getText().toString());
            }
            setPrice();
            setTotal();
            setButton();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void setQty() {
        etQty.setText(String.valueOf(qty));
        etQty.setSelection(etQty.getText().length());
    }

    private void setPrice() {
        sub_total = qty * price;
        tvAmount.setText("Rp. " + DecimalFormatRupiah.formatNumber.format(sub_total));
        etDiscount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DecimalFormatRupiah.formatNumber.format(sub_total).length())});
    }

    private void showDiscount() {
        if (groupAmountDiscount.getVisibility() == View.VISIBLE) {
            discount = 0;
            groupDiscountExpand.setVisibility(View.GONE);
            groupAmountDiscount.setVisibility(View.GONE);
            imgIsDiscount.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
            setTotal();
            setButton();
        } else {
            groupDiscountExpand.setVisibility(View.VISIBLE);
            groupAmountDiscount.setVisibility(View.VISIBLE);
            imgIsDiscount.setImageResource(R.drawable.ic_check_box_black_24dp);
        }
    }

    private void setDiscount() {
        if (discount > 0) {
            etDiscount.setText(DecimalFormatRupiah.formatNumber.format(discount));
        } else {
            etDiscount.setText("");
        }
    }

    private void setDiscountAmount() {
        tvAmountDiscount.setText("- Rp. " + DecimalFormatRupiah.formatNumber.format(discount));
    }

    public TextWatcher discountWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                etDiscount.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();

                    long longval;
                    if (originalString.contains(",") || originalString.contains(".")) {
                        originalString = originalString.replaceAll("[.,]", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("###,###,###,###,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    etDiscount.setText(formattedString);
                    etDiscount.setSelection(etDiscount.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                etDiscount.addTextChangedListener(this);

            }
        };
    }

    private void setTotal() {
        if (discount > 0) {
            amount = sub_total - discount;
        } else {
            amount = sub_total;
        }
        tvTotal.setText("Rp. " + DecimalFormatRupiah.formatNumber.format(amount));
    }

    private void setButton() {
        if (qty > 0 && amount > 0) {
            cvBtnAddCart.setEnabled(true);
            cvBtnAddCart.setCardBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
        } else {
            cvBtnAddCart.setEnabled(false);
            cvBtnAddCart.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
        }
    }

//    private void setButton() {
//        if(amount<discount || qty==0 || isDiscount && discount==0){
//            cvBtnAddCart.setEnabled(false);
//            cvBtnAddCart.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
//        } else {
//            cvBtnAddCart.setEnabled(true);
//            cvBtnAddCart.setCardBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
//        }
//    }

//    private void enableBtnAddCart() {
//        if(qty>0){
//            if(isDiscount && etDiscount.getText().length()<1){
//                cvBtnAddCart.setEnabled(false);
//                cvBtnAddCart.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
//            } else {
//                cvBtnAddCart.setEnabled(true);
//                cvBtnAddCart.setCardBackgroundColor(getResources().getColor(R.color.colorThemeOrange));
//            }
//
//        } else {
//            cvBtnAddCart.setEnabled(false);
//            cvBtnAddCart.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGrey));
//        }
//    }
//
//
//    private void showAmount() {
//        if(qty>0){
//            groupAmountPayment.setVisibility(View.VISIBLE);
//            amount = price*qty;
//            tvAmount.setText("Rp. "+DecimalFormatRupiah.formatNumber.format(amount));
//            tvTotal.setText("Rp. "+DecimalFormatRupiah.formatNumber.format(amount));
//            tvAddAmount.setText("Tambah \nRp. "+DecimalFormatRupiah.formatNumber.format(amount));
//        } else {
//            groupAmountPayment.setVisibility(View.GONE);
//            amount=0;
//            tvAmount.setText("");
//            tvTotal.setText("");
//            tvAddAmount.setText("Tambah");
//        }
//    }
//
//    private void showDiscount(){
//        if(isDiscount){
//            groupDiscountExpand.setVisibility(View.VISIBLE);
//            imgIsDiscount.setImageResource(R.drawable.ic_check_box_black_24dp);
//            etDiscount.setText(String.valueOf(discount));
//            groupAmountDiscount.setVisibility(View.VISIBLE);
//        } else {
//            groupDiscountExpand.setVisibility(View.GONE);
//            groupAmountDiscount.setVisibility(View.GONE);
//            imgIsDiscount.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
//        }
//        setPrice();
//    }
//
//
//    public void setPrice(){
//        if(isDiscount){
//            amount = amount-discount;
//            tvAmountDiscount.setText("- Rp. "+DecimalFormatRupiah.formatNumber.format(discount));
//            tvAmount.setText("Rp. "+DecimalFormatRupiah.formatNumber.format(amount));
//            tvAddAmount.setText("Tambah \nRp. "+DecimalFormatRupiah.formatNumber.format(amount));
//        } else {
//            showAmount();
//        }
//
//    }
}
