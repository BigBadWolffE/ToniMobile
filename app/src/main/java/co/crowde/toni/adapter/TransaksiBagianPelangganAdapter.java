package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.constant.Const;
import co.crowde.toni.helper.CallCenter;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;
import co.crowde.toni.view.activity.auth.ForgotPassActivity;
import co.crowde.toni.view.activity.customer.CustomerHutangActivity;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.utils.print.Utils;

public class TransaksiBagianPelangganAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<CustomerModel> customerModelsFiltered = new ArrayList<>();

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView namapelanggan, telpon, hutang;

        public ViewHolder(View itemView) {
            super(itemView);

            namapelanggan = itemView.findViewById(R.id.tvTabCustomerName);
            telpon = itemView.findViewById(R.id.tvTabCustomerPhone);
            hutang = itemView.findViewById(R.id.tvTabCustomerCredit);

        }

    }

    public TransaksiBagianPelangganAdapter(Context context,
                                           List<CustomerModel> models,
                                           Activity activity) {
        this.context = context;
        this.customerModelsFiltered.clear();
        this.customerModelsFiltered.addAll(models);
        this.activity = activity;
    }

    public void replaceItemFiltered(List<CustomerModel> models) {
        this.customerModelsFiltered.clear();
        this.customerModelsFiltered.addAll(models);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pelanggan, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final CustomerModel model = customerModelsFiltered.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            if (model != null) {
                String text = "<font color=#52575C><b>" + model.getCustomerName() + "</b></font>" + (!Utils.calculateDateBetweenTwoDays(model.getCreatedAt())?
                        "" : "<font color=#F7931D> (Baru!)</font>");
                viewHolder.namapelanggan.setText(Html.fromHtml(text));

                viewHolder.telpon.setText(model.getPhone());

                DecimalFormatRupiah.changeFormat(activity);
                viewHolder.hutang.setText("Rp. "+
                        DecimalFormatRupiah.formatNumber.format(model.getSaldo())+",-");
                if(model.getSaldo()!=0){
                    viewHolder.hutang.setTextColor(activity.getResources().getColor(R.color.colorThemeOrange));
                } else {
                    viewHolder.hutang.setTextColor(activity.getResources().getColor(R.color.TextColorBlack));
                }

                viewHolder.telpon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CallCenter.showDialCustomer(activity, model.getPhone());
                    }
                });

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AnalyticsToniUtils.getEvent(Const.CATEGORY_CUSTOMER,Const.MODUL_CUSTOMER,Const.LABEL_CUSTOMER_DETAIL);

                        Intent detailPelanggan = new Intent(activity, CustomerHutangActivity.class);
                        detailPelanggan.putExtra(CustomerModel.class.getSimpleName(), model);
                        ((Activity) context).startActivityForResult(detailPelanggan, 5);

                    }
                });

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
    public int getItemCount(){
        return customerModelsFiltered != null ? customerModelsFiltered.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return customerModelsFiltered.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

}
