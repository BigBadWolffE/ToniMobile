package co.crowde.toni.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.model.LaporanBestProductModel;

//import soedja.crowde.tokotani.R;
//import soedja.crowde.tokotani.model.LaporanBestProductModel;


public class BarangTerlarisAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<LaporanBestProductModel> laporanBestProductModels;
    Activity activity;

    public BarangTerlarisAdapter(Activity activity) {
        this.activity = activity;
        this.laporanBestProductModels = new ArrayList<>();
    }

    public void setModels(List<LaporanBestProductModel> models) {
        this.laporanBestProductModels = models;
        notifyDataSetChanged();
    }

    public void clearModels(){
        laporanBestProductModels.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_produk_laris,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LaporanBestProductModel model = laporanBestProductModels.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.namaBarang.setText(model.getProductName());
        viewHolder.produsen.setText(model.getSupplierName());
        viewHolder.terjual.setText(model.getCount());

    }

    @Override
    public int getItemCount() {
         return laporanBestProductModels.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        Typeface montserratReg,
                montserratSemiBold,
                montserratBold,
                openSansReg,
                openSansSemiBold,
                openSansBold;

        TextView namaBarang, satuan, produsen,terjual;
        public ViewHolder (View itemView){
            super(itemView);

            montserratReg = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Montserrat-Regular.ttf");
            montserratSemiBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Montserrat-SemiBold.ttf");
            montserratBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/Montserrat-Bold.ttf");
            openSansReg = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/OpenSans-Regular.ttf");
            openSansSemiBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/OpenSans-SemiBold.ttf");
            openSansBold = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/OpenSans-Bold.ttf");



//            namaBarang = itemView.findViewById(R.id.laporannamabarang);
////            satuan = itemView.findViewById(R.id.laporansatuan);
//            produsen = itemView.findViewById(R.id.laporanprodusen);
//            terjual = itemView.findViewById(R.id.laporanterjual);


        }


    }

}
