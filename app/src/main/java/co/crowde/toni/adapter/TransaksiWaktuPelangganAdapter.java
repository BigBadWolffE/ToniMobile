package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.model.TransaksiModel;
//import co.crowde.toni.view.intent.TransactionDetailActivity;
import co.crowde.toni.view.dialog.popup.transaction.TransactionPopUpDetail;

//import soedja.crowde.tokotani.DetailTransaksiFragment;
//import soedja.crowde.tokotani.R;
//import soedja.crowde.tokotani.model.TransaksiModel;

public class TransaksiWaktuPelangganAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<TransaksiModel> models;
    private Activity activity;
    Context mContext;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    class ViewHolder extends RecyclerView.ViewHolder {
        Typeface montserratReg,
                montserratSemiBold,
                montserratBold,
                openSansReg,
                openSansSemiBold,
                openSansBold;

        TextView idtransaksi, namapelanggan, tanggaltransaksi, pembayaran, totaltransaksi;
        //        DetailTransaksiFragment detailTransaksiFragment;
        RecyclerView recyclerView;
//        DetailTransaksiWaktuPelangganAdapter detailTransaksiWaktuPelangganAdapter;
        CardView cardView;

        public ViewHolder(final View itemView) {
            super(itemView);

            montserratReg = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Montserrat-Regular.ttf");
            montserratSemiBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Montserrat-SemiBold.ttf");
            montserratBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Montserrat-Bold.ttf");
            openSansReg = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/OpenSans-Regular.ttf");
            openSansSemiBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/OpenSans-SemiBold.ttf");
            openSansBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/OpenSans-Bold.ttf");


//            recyclerView = itemView.findViewById(R.id.rc_detail_pembelian);
            cardView = itemView.findViewById(R.id.cardview_list);
            idtransaksi = itemView.findViewById(R.id.idtransaksipelanggan);
            namapelanggan = itemView.findViewById(R.id.namapelanggantransaksiwaktu);
            tanggaltransaksi = itemView.findViewById(R.id.transaksitanggalpelangganwaktu);
//            pembayaran = itemView.findViewById(R.id.transaksipembayaranpelangganwaktu);
            totaltransaksi = itemView.findViewById(R.id.totaltransaksipelangganwaktu);
//            detailTransaksiFragment = new DetailTransaksiFragment();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intentDetail = new Intent(activity, TransactionDetailActivity.class);
//                    intentDetail.putExtra("idtransaksi",models.get(getAdapterPosition()).getTransactionId());
//                    intentDetail.putExtra("namapelanggan",models.get(getAdapterPosition()).getCostumerName());
//                    intentDetail.putExtra("tanggaltransaksi",tanggaltransaksi.getText().toString());
//                    intentDetail.putExtra("pembayaran",models.get(getAdapterPosition()).getPaymentType());
//                    intentDetail.putExtra("totaltransaksi",totaltransaksi.getText().toString());
//                    activity.startActivityForResult(intentDetail,13);
//                    TransactionPopUpDetail.showPopup(activity, model);

                }
            });



//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.layout_transaksi, detailTransaksiFragment,"first").addToBackStack(null).commit();
//
//
//                   Bundle bundle = new Bundle();
//                    bundle.putString("idtransaksi",models.get(getAdapterPosition()).getTransactionId());
//                    bundle.putString("namapelanggan",models.get(getAdapterPosition()).getCostumerName());
//                    bundle.putString("tanggaltransaksi",tanggaltransaksi.getText().toString());
//                    bundle.putString("pembayaran",models.get(getAdapterPosition()).getPaymentType());
//                    bundle.putString("totaltransaksi",models.get(getAdapterPosition()).getAmount());
////                    detailTransaksiFragment.setArguments(bundle);
//
//
//
//                }
//            });

        }

    }


    public TransaksiWaktuPelangganAdapter(Context context, List<TransaksiModel> modelList, Activity activity) {
        this.context = context;
        this.models = modelList;
        this.activity = activity;
    }



//    public void setModels(List<TransaksiModel> models) {
//        this.models = models;
//        notifyDataSetChanged();
//    }
//
    public void clearModels(){
        models.clear();
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            final TransaksiModel model = models.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;

            if (model != null){

                viewHolder.idtransaksi.setText(model.getTransactionId());
                viewHolder.namapelanggan.setText(model.getCostumerName());
//        viewHolder.totaltransaksi.setText(model.get());

                String tanggal = model.getCreatedAt();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                Date date = null;
                try {
                    date = dateFormat.parse(tanggal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateFormat formatter = new SimpleDateFormat(" EEE, dd MMMM yyyy"); //If you need time just put specific format for time like 'HH:mm:ss'
                String dateStr = formatter.format(date);
                viewHolder.tanggaltransaksi.setText(dateStr);
                DecimalFormat money = new DecimalFormat("#,###,###");
                int amount = Integer.parseInt(model.getAmount());
                viewHolder.totaltransaksi.setText("Rp."+money.format(amount));
                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TransactionPopUpDetail.showPopup(activity, model);
                    }
                });


            }
        } else if (holder instanceof LoadingViewHolder){

            showLoadingView((LoadingViewHolder)holder,position);
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
        return models!=null ? models.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        return models.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }





}
