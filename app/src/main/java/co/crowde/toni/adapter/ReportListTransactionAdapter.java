package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.crowde.toni.R;
import co.crowde.toni.helper.DecimalFormatRupiah;
import co.crowde.toni.model.response.list.TransactionModel;

public class ReportListTransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<TransactionModel> transactionModels = new ArrayList<>();
    public static Locale lokal = new Locale("id");

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTransactionCustomerName, tvTransactionDate,
                tvTransactionAmount, tvTransactionId;
        LinearLayout layoutProductInventory;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvTransactionCustomerName = itemView.findViewById(R.id.tv_transaction_customer_name);
            tvTransactionDate = itemView.findViewById(R.id.tv_transaction_date);
            tvTransactionAmount = itemView.findViewById(R.id.tv_transaction_amount);
            tvTransactionId = itemView.findViewById(R.id.tv_transaction_id);

        }
    }

    public ReportListTransactionAdapter(Context context,
                                   List<TransactionModel> transactionModels,
                                   Activity activity) {
        this.context = context;
        this.transactionModels.clear();
        this.transactionModels.addAll(transactionModels);
        this.activity = activity;
    }

    public void replaceItemFiltered(List<TransactionModel> transactionModels) {
        this.transactionModels.clear();
        this.transactionModels.addAll(transactionModels);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transaksi_waktu, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ReportListTransactionAdapter.ViewHolder) {
            final TransactionModel model = transactionModels.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            if (model != null) {
                String transactionId = model.getTransactionId();
                String trId = "<font color=#52575C>Transaksi Id: </font><font color=#F7931D>"+ transactionId +"</font>";

                //date setup
                String tanggal = model.getCreatedAt();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                Date date = null;
                try {
                    date = dateFormat.parse(tanggal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat formatter = new SimpleDateFormat("dd EEEE yyyy", lokal); //If you need time just put specific format for time like 'HH:mm:ss'
                String dateStr = formatter.format(date);


                viewHolder.tvTransactionCustomerName.setText(model.getCustomerName());
                viewHolder.tvTransactionDate.setText(dateStr);

                DecimalFormatRupiah.changeFormat(activity);
                viewHolder.tvTransactionAmount.setText("Rp. "+
                        DecimalFormatRupiah.formatNumber.format(model.getAmount())+",-");
                viewHolder.tvTransactionId.setText(Html.fromHtml(trId));

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent detail = new Intent(activity, DetailTransactionActivity.class);
//                        detail.putExtra(TransactionModel.class.getSimpleName(), model);
//                        activity.startActivityForResult(detail, 123);
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
    public int getItemCount() {
        return transactionModels != null ? transactionModels.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return transactionModels.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }
}
