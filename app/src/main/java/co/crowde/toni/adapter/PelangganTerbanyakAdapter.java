package co.crowde.toni.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.model.LaporanTopCustomer;

//import soedja.crowde.tokotani.R;
//import soedja.crowde.tokotani.model.LaporanTopCustomer;

public class PelangganTerbanyakAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<LaporanTopCustomer> laporanTopCustomers;
    Activity activity;

    public PelangganTerbanyakAdapter(Activity activity) {
        this.activity = activity;
        this.laporanTopCustomers = new ArrayList<>();
    }

    public void setModels(List<LaporanTopCustomer> models) {
        this.laporanTopCustomers = models;
        notifyDataSetChanged();
    }
    public void clearModels(){
        laporanTopCustomers.clear();
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_pelanggan_terbanyak,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LaporanTopCustomer model = laporanTopCustomers.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.namaPelanggan.setText(model.getCustomerName());
//        viewHolder.telepon.setText(model.getPhone());
        viewHolder.frekuensi.setText(model.getCount());
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        int beliInteger = model.getTotalAmount();
        String beliFinal = formatter.format(beliInteger);
        viewHolder.totalbeli.setText("Rp."+beliFinal);

    }

    @Override
    public int getItemCount() {
        return laporanTopCustomers.size();    }



    class ViewHolder extends RecyclerView.ViewHolder {
        Typeface montserratReg,
                montserratSemiBold,
                montserratBold,
                openSansReg,
                openSansSemiBold,
                openSansBold;
        TextView namaPelanggan, telepon, frekuensi,totalbeli;
        public ViewHolder(View itemView) {
            super(itemView);
            montserratReg = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Montserrat-Regular.ttf");
            montserratSemiBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Montserrat-SemiBold.ttf");
            montserratBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Montserrat-Bold.ttf");
            openSansReg = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/OpenSans-Regular.ttf");
            openSansSemiBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/OpenSans-SemiBold.ttf");
            openSansBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/OpenSans-Bold.ttf");



            namaPelanggan = itemView.findViewById(R.id.namappelangganlaporan);
//            telepon = itemView.findViewById(R.id.telponlaporan);
            frekuensi = itemView.findViewById(R.id.frekuensilaporan);
            totalbeli = itemView.findViewById(R.id.totalbelilaporan);


        }
    }

}
