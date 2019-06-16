package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.model.DetailTransaksiModel;
//import co.crowde.toni.view.intent.TransactionDetailActivity;
//import soedja.crowde.tokotani.DetailTransaksiFragment;
//import soedja.crowde.tokotani.R;
//import soedja.crowde.tokotani.model.DetailTransaksiModel;

public class DetailTransaksiWaktuPelangganAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<DetailTransaksiModel> detailTransaksiModels;
    private Activity detailActivity;
    private Context context;
    private int lastPosition = -1;


    public DetailTransaksiWaktuPelangganAdapter(Activity activity) {
        this.detailActivity = activity;
        this.detailTransaksiModels = new ArrayList<>();
    }

    public void setModels(List<DetailTransaksiModel> models) {
        this.detailTransaksiModels = models;
        notifyDataSetChanged();
    }


    public void clearData(){
        detailTransaksiModels.clear();
        notifyDataSetChanged();
    }

    public void clearModels(){
        detailTransaksiModels.clear();
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detail_transaksi_waktu,parent, false);

        ViewHolder mViewHolder = new ViewHolder(view);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DetailTransaksiModel model = detailTransaksiModels.get(position);
        ViewHolder viewHolder =(ViewHolder) holder;
        int quantity = model.getQuantity();
        DecimalFormat money = new DecimalFormat("#,###,###");
        int amount = Integer.parseInt(model.getSellingPrice());
        viewHolder.productName.setText(model.getProductName());
        viewHolder.quantity.setText(Integer.toString(quantity));
        viewHolder.amount.setText(model.getUnit());
//        viewHolder.produsenName.setText(model.getSupplierName());
        viewHolder.sellingPrice.setText("Rp."+money.format(amount));


    }

    @Override
    public int getItemCount() { return detailTransaksiModels != null ? detailTransaksiModels.size() : 0; }
    Typeface montserratReg,
            montserratSemiBold,
            montserratBold,
            openSansReg,
            openSansSemiBold,
            openSansBold;

    @Override
    public Filter getFilter() {
        return null;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView quantity, productName,amount,sellingPrice,produsenName;
//        TransactionDetailActivity detailTransaksiFragment;

        public ViewHolder (View itemView){
            super(itemView);

            montserratReg = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Montserrat-Regular.ttf");
            montserratSemiBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Montserrat-SemiBold.ttf");
            montserratBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Montserrat-Bold.ttf");
            openSansReg = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/OpenSans-Regular.ttf");
            openSansSemiBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/OpenSans-SemiBold.ttf");
            openSansBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/OpenSans-Bold.ttf");



            quantity = itemView.findViewById(R.id.banyakpembelianpelanggan);
            productName = itemView.findViewById(R.id.namabarangpembelianpelanggan);
            amount = itemView.findViewById(R.id.ukuranpembelianpelanggan);
            sellingPrice = itemView.findViewById(R.id.hargapembelianpelanggan);
//            produsenName = itemView.findViewById(R.id.produsenpembelianpelanggan);
//            detailTransaksiFragment = new TransactionDetailActivity();
//            Bundle bundle = new Bundle();
//            bundle.putString("quantity",detailTransaksiModels.get(getAdapterPosition()).getQuantity());
//            detailTransaksiFragment.setArguments(bundle);


        }
    }

}
