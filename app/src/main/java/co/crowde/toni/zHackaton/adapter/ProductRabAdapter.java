package co.crowde.toni.zHackaton.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.CatalogProductAdapter;
import co.crowde.toni.listener.ItemClickListener;
import co.crowde.toni.model.CatalogModel;
import co.crowde.toni.view.activity.catalog.CatalogProductActivity;
import co.crowde.toni.view.fragment.modul.DashboardFragment;
import co.crowde.toni.zHackaton.model.ProductRabModel;

public class ProductRabAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private Context context;
    private Activity activity ;
    private List<ProductRabModel> productModels = new ArrayList<>();
    ArrayList<ProductRabModel> arrayList = new ArrayList<>();
    ItemClickListener listener;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkProduct;
        TextView tvTabProductName, tvTabProductCategory,
                tvTabProductQty;
        ImageView imgIncrease, imgDecrease;

        public ViewHolder(final View itemView) {
            super(itemView);
            checkProduct = itemView.findViewById(R.id.check_product);

            tvTabProductName = itemView.findViewById(R.id.product_name);
            tvTabProductCategory = itemView.findViewById(R.id.product_category);

            imgDecrease = itemView.findViewById(R.id.imgDecrease);
            tvTabProductQty = itemView.findViewById(R.id.tvQty);
            imgIncrease = itemView.findViewById(R.id.imgIncrease);


        }
    }

    public ProductRabAdapter(Context context,
                             List<ProductRabModel> ProductModelList,
                             Activity activity,
                             ItemClickListener listener) {
        this.context = context;
        this.productModels.clear();
        this.productModels.addAll(ProductModelList);
        this.activity = activity;
        this.listener = listener;
    }

    public void replaceItemFiltered(List<ProductRabModel> ProductModelList) {
        this.productModels.clear();
        this.productModels.addAll(ProductModelList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_rab, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final ProductRabModel model = productModels.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            if (model != null) {

                viewHolder.tvTabProductName.setText(model.getProductName());
                viewHolder.tvTabProductCategory.setText(model.getCategory());
                viewHolder.tvTabProductQty.setText(String.valueOf(model.getQty()));
                viewHolder.checkProduct.setOnCheckedChangeListener(null);

                //if true, your checkbox will be selected, else unselected
                viewHolder.checkProduct.setChecked(model.isChecked());

                viewHolder.checkProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            model.setChecked(true);
                            arrayList.add(model);
//                            CatalogProductActivity.setEnabledButton(activity, getCount());

                        } else {
                            model.setChecked(false);
                            arrayList.remove(model);
//                            CatalogProductActivity.setEnabledButton(activity, getCount());
                        }
                    }
                });

                viewHolder.imgDecrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onDecreaseItem(v, position);
//                        DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
                    }
                });

                viewHolder.imgIncrease.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onIncreaseItem(v, position);
//                        DashboardFragment.productDashboardAdapter.notifyDataSetChanged();
                    }
                });


            }
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }

    }

    public int getCount() {
        return arrayList.size();
    }

    public ArrayList<ProductRabModel> getSelectList(){
        ArrayList<ProductRabModel> list = new ArrayList<>();
        for(int i=0;i<arrayList.size();i++){
            if(arrayList.get(i).isChecked())
                list.add(arrayList.get(i));
        }
        return list;
    }

    private class LoadingViewHolder extends ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    @Override
    public int getItemCount() {
        return productModels!=null? productModels.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        return productModels.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

}
