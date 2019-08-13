package co.crowde.toni.view.activity.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
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
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.network.API;
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.utils.print.Utils;
import co.crowde.toni.view.dialog.message.product.UpdateProductDialog;

public class InventoryDetailActivity extends AppCompatActivity implements View.OnClickListener {

    AppBarLayout appBarLayout;
    Toolbar toolbar;
    ImageView imgProduct, imgIncrease, imgDecrease;
    TextView tvProductName, tvProductCategory, tvProductStock, tvProductSupplier, tvProductDesc,
                tvRpPurchase, tvRpSelling;
    EditText etPurchase, etSelling, etQty;
    CardView cvBtnSave;

    ProductModel productModel;

    ProgressDialog progressDialog;

    WebView webView;

    int qty, purchase, selling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap Tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);

        productModel = getIntent().getParcelableExtra(ProductModel.class.getSimpleName());

        appBarLayout = findViewById(R.id.appBar);
        SetHeader.isLolipop(InventoryDetailActivity.this, appBarLayout);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgProduct = findViewById(R.id.img_product);
        imgDecrease = findViewById(R.id.imgDecrease);
        imgIncrease = findViewById(R.id.imgIncrease);
        tvProductName = findViewById(R.id.tv_product_name);
        tvProductCategory = findViewById(R.id.tv_product_category);
        tvProductStock = findViewById(R.id.tv_product_stock);
        tvProductSupplier = findViewById(R.id.tv_product_supplier);
        tvRpPurchase = findViewById(R.id.label_rp_purchase);
        tvRpSelling = findViewById(R.id.label_rp_selling);
        etPurchase = findViewById(R.id.et_purchase_price);
        etSelling = findViewById(R.id.et_selling_price);
        etQty = findViewById(R.id.et_qty);
        cvBtnSave = findViewById(R.id.cv_btn_save_product);
        webView = findViewById(R.id.tv_product_desc);

        Picasso.with(InventoryDetailActivity.this).load(API.Host+productModel.getPicture())
                .into(imgProduct);

        DecimalFormatRupiah.changeFormat(InventoryDetailActivity.this);

        String product = productModel.getProductName();
        String nama;
        String varian;
        if(product.contains("_")){
            nama = StringUtils.substringBeforeLast(product, "_");
            varian = StringUtils.substringAfterLast(product, "_");

            tvProductName.setText(nama+" ("+varian+")");
        } else {
            nama = product;
            varian = productModel.getUnit();

            tvProductName.setText(nama);
        }
        tvProductCategory.setText(productModel.getCategoryName());
        tvProductStock.setText(productModel.getStock()+" "+productModel.getUnit());
        tvProductSupplier.setText(productModel.getSupplierName());

        String description = "<html><body style='text-align:justify;font-size:12px;'>"+productModel.getDescription()+"</body></html>";
        String dataString = String.format(Locale.US, description, "my html with text justification");
        webView.loadDataWithBaseURL("", dataString, "text/html", "UTF-8", "");

        if(productModel.getPurchasePrice()!=0){
            etPurchase.setText(String.valueOf(
                    DecimalFormatRupiah.formatNumber.format(productModel.getPurchasePrice())));
            showRpPurchase();
        }
        if(productModel.getSellingPrice()!=0){
            etSelling.setText(String.valueOf(
                    DecimalFormatRupiah.formatNumber.format(productModel.getSellingPrice())));
            showRpSelling();
        }

        imgDecrease.setOnClickListener(this);
        imgIncrease.setOnClickListener(this);
        cvBtnSave.setOnClickListener(this);
        etQty.setOnClickListener(this);
        etPurchase.setOnClickListener(this);
        etSelling.setOnClickListener(this);

        etPurchase.addTextChangedListener(purchaseWatcher());
        etSelling.addTextChangedListener(sellingWatcher());
        etQty.addTextChangedListener(qtyWatcher);
    }

    private void showRpPurchase() {
        if (etPurchase.getText().length() > 3) {
            tvRpPurchase.setVisibility(View.VISIBLE);
        } else {
            tvRpPurchase.setVisibility(View.GONE);
        }
    }

    private void showRpSelling() {
        if (etSelling.getText().length() > 3) {
            tvRpSelling.setVisibility(View.VISIBLE);
        } else {
            tvRpSelling.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgDecrease:
                if(qty!=0){
                    qty = qty-1;
                }
                setQty();
                break;

            case R.id.imgIncrease:
                qty=qty+1;
                setQty();
                break;

            case R.id.et_selling_price:
                etSelling.setSelection(etSelling.getText().length());
                break;

            case R.id.et_purchase_price:
                etPurchase.setSelection(etPurchase.getText().length());
                break;

            case R.id.et_qty:
                if(etQty.getText().toString().equals("0")){
                    etQty.setText("");
                }
                etQty.setSelection(etQty.getText().length());
                break;

            case R.id.cv_btn_save_product:
                if(etPurchase.getText().length()>0){
                    purchase = Integer.parseInt(etPurchase.getText().toString().replaceAll("[,.]",""));
                } else {
                    purchase = 0;
                }
                if(etSelling.getText().length()>0){
                    selling = Integer.parseInt(etSelling.getText().toString().replaceAll("[,.]",""));
                } else {
                    selling = 0;
                }
                progressDialog.show();
                UpdateProductDialog.showDialog(this, productModel.getProductId(), qty, purchase, selling, progressDialog);
                break;
        }
    }

    private void setQty() {
        etQty.setText(String.valueOf(qty));
        etQty.setSelection(etQty.getText().length());
    }

    public TextWatcher qtyWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(etQty.getText().length()==0) {
                qty = 0;
            } else if (etQty.getText().length() > 0){
                qty = Integer.parseInt(etQty.getText().toString());
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public TextWatcher purchaseWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPurchase.getText().length() > 0) {
                    tvRpPurchase.setVisibility(View.VISIBLE);
                } else {
                    tvRpPurchase.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                etPurchase.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();

                    long longval;
                    if (originalString.contains(",")|| originalString.contains(".")) {
                        originalString = originalString.replaceAll("[.,]", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("###,###,###,###,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    etPurchase.setText(formattedString);
                    etPurchase.setSelection(etPurchase.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                etPurchase.addTextChangedListener(this);

            }
        };
    }

    public TextWatcher sellingWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etSelling.getText().length() > 3) {
                    tvRpSelling.setVisibility(View.VISIBLE);
                } else {
                    tvRpSelling.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                etSelling.removeTextChangedListener(this);
                try {
                    String originalString = s.toString();

                    long longval;
                    if (originalString.contains(",")|| originalString.contains(".")) {
                        originalString = originalString.replaceAll("[.,]", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("###,###,###,###,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    etSelling.setText(formattedString);
                    etSelling.setSelection(etSelling.getText().length());

                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                etSelling.addTextChangedListener(this);

            }
        };
    }
}
