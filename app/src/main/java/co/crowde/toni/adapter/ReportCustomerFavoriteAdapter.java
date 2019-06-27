package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.response.list.CustomerFavoriteModel;
import co.crowde.toni.model.response.list.ProductFavoriteModel;

public class ReportCustomerFavoriteAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Activity activity ;
    private List<CustomerFavoriteModel> customerFavoriteModels = new ArrayList<>();

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTabCustomertNumber, tvTabCustomerName, tvTabCustomerFrequency,
                tvTabCustomerAmount;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvTabCustomertNumber = itemView.findViewById(R.id.tv_customer_number);
            tvTabCustomerName = itemView.findViewById(R.id.tv_customer_name);
            tvTabCustomerFrequency = itemView.findViewById(R.id.tv_customer_frequency);
            tvTabCustomerAmount = itemView.findViewById(R.id.tv_customer_amount);

        }
    }

    public ReportCustomerFavoriteAdapter(Context context,
                                        List<CustomerFavoriteModel> CustomerModelList,
                                        Activity activity) {
        this.context = context;
        this.customerFavoriteModels.clear();
        this.customerFavoriteModels.addAll(CustomerModelList);
        this.activity = activity;
    }

    public void replaceItemFiltered(List<CustomerFavoriteModel> CustomerModelList) {
        this.customerFavoriteModels.clear();
        this.customerFavoriteModels.addAll(CustomerModelList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pelanggan_terbanyak, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final CustomerFavoriteModel model = customerFavoriteModels.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            if (model != null) {

                viewHolder.tvTabCustomertNumber.setText(String.valueOf(position+1));
                viewHolder.tvTabCustomerName.setText(model.getCustomerName());
                viewHolder.tvTabCustomerFrequency.setText(String.valueOf(model.getCount()));
                DecimalFormatRupiah.changeFormat(activity);
                viewHolder.tvTabCustomerAmount.setText("Rp. "+
                        DecimalFormatRupiah.formatNumber.format(model.getTotalAmount())+",-");
            }
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }

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
        return customerFavoriteModels!=null? customerFavoriteModels.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        return customerFavoriteModels.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }
}
