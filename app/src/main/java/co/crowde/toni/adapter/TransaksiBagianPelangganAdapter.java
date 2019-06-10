package co.crowde.toni.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import co.crowde.toni.R;
import co.crowde.toni.intent.CustomerHutangActivity;
import co.crowde.toni.model.CustomerModel;

//import soedja.crowde.tokotani.R;
//import soedja.crowde.tokotani.model.CustomerModel;
//import soedja.crowde.tokotani.popup.PopUpBayarHutangTransaksi;
//import soedja.crowde.tokotani.popup.PopUpCetakHutangActivity;


public class TransaksiBagianPelangganAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    //Bluetooth
    public static BluetoothAdapter mBluetoothAdapter = null;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    public static BluetoothSocket mBluetoothSocket = null;
    public static BluetoothDevice mBluetoothDevice;
    public static OutputStream os = null;
    String bluetoothAddress;
    private Context context;
    private List<CustomerModel> models;
    private List<CustomerModel> filteredModels;
    private Activity activity;
    //    private final int REQUEST_CODE = 13;
    Context mContext;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public TransaksiBagianPelangganAdapter(Context context, List<CustomerModel> modelsList, Activity activity) {
        this.activity = activity;
        this.context = context;
        this.models = modelsList;
        this.filteredModels = modelsList;
        this.mContext = context;
    }

    //    public void setModels(List<CustomerModel> models){
//        this.models = models;
//        notifyDataSetChanged();
//    }
    public void clearModels() {
        models.clear();
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



        CustomerModel model = filteredModels.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        if(model !=null){

            LinearLayout btnbayarhutang, btncetakhutang;
            Resources resources = viewHolder.itemView.getContext().getResources();
            Date time;
            String total;
            int sum;
            sum = model.getSaldo();
            total = Integer.toString(sum);
            ;

            viewHolder.namapelanggan.setText(model.getCustomerName());
            //date setup
            String tanggal = model.getCreatedAt();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            Date date = null;
            try {
                date = dateFormat.parse(tanggal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //If you need time just put specific format for time like 'HH:mm:ss'
            String dateStr = formatter.format(date);
//        viewHolder.tanggalterdaftar.setText(dateStr);
            viewHolder.telpon.setText(model.getPhone());
            DecimalFormat money = new DecimalFormat("#,###,###");
            int amount = Integer.parseInt(total);
            viewHolder.hutang.setText("Rp." + money.format(amount));
//        if (total.equals("0")){
//            viewHolder.btncetakhutang.setVisibility(View.GONE);
//            viewHolder.btnbayarhutang.setVisibility(View.GONE);
//        }else{
//            viewHolder.btnbayarhutang.setVisibility(View.VISIBLE);
//            viewHolder.btncetakhutang.setVisibility(View.VISIBLE);
//        }




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
    public int getItemCount() { return filteredModels.size(); }

    @Override
    public int getItemViewType(int position) {
        return filteredModels.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredModels = models;
                } else {
                    List<CustomerModel> filteredList = new ArrayList<>();
                    for (CustomerModel row : models) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (Integer.toString(row.getCredit()).toLowerCase().contains(charString.toLowerCase()) ||
                                row.getCustomerName().toLowerCase().contains(charSequence) ||
                                row.getPhone().toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row);
                        }
                    }

                    filteredModels = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredModels;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredModels = (ArrayList<CustomerModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setItems(List<CustomerModel> newList) {
        models.clear();
        models.addAll(newList);
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        Typeface montserratReg,
                montserratSemiBold,
                montserratBold,
                openSansReg,
                openSansSemiBold,
                openSansBold;
        TextView namapelanggan, tanggalterdaftar, telpon, hutang;
        CardView btnbayarhutang, btncetakhutang;


        public ViewHolder(View itemView) {
            super(itemView);

            montserratReg = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Montserrat-Regular.ttf");
            montserratSemiBold = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Montserrat-SemiBold.ttf");
            montserratBold = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/Montserrat-Bold.ttf");
            openSansReg = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
            openSansSemiBold = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/OpenSans-SemiBold.ttf");
            openSansBold = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/OpenSans-Bold.ttf");


            namapelanggan = itemView.findViewById(R.id.transaksinamapelanggan);
//            tanggalterdaftar = itemView.findViewById(R.id.transaksitglterdaftarpelanggan);
            telpon = itemView.findViewById(R.id.transaksitelponpelanggan);
            hutang = itemView.findViewById(R.id.transaksihutangpelanggan);
//            btnbayarhutang = itemView.findViewById(R.id.btntransaksihutangpelanggan);
//            btncetakhutang = itemView.findViewById(R.id.btncetakstrukhutang);
//
//
//
//            btnbayarhutang.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intentHutang = new Intent(activity, PopUpBayarHutangTransaksi.class);
//                    intentHutang.putExtra("namapelanggan",models.get(getAdapterPosition()).getCustomerName());
//                    intentHutang.putExtra("hutang",models.get(getAdapterPosition()).getCredit());
//                    intentHutang.putExtra("creditPaid",models.get(getAdapterPosition()).getCreditPaid());
//                    intentHutang.putExtra("customerId",models.get(getAdapterPosition()).getCustomerId());
//                    activity.startActivityForResult(intentHutang, REQUEST_CODE);
//
//                }
//            });
//
//            btncetakhutang.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent intentHutang = new Intent(activity, PopUpCetakHutangActivity.class);
//                    intentHutang.putExtra("namapelanggan",models.get(getAdapterPosition()).getCustomerName());
//                    intentHutang.putExtra("hutang",models.get(getAdapterPosition()).getCredit());
//                    intentHutang.putExtra("creditPaid",models.get(getAdapterPosition()).getCreditPaid());
//                    intentHutang.putExtra("customerId",models.get(getAdapterPosition()).getCustomerId());
//                    activity.startActivityForResult(intentHutang, REQUEST_CODE);
//
//                }
//            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent detailPelanggan = new Intent(activity, CustomerHutangActivity.class);
                    detailPelanggan.putExtra(CustomerModel.class.getSimpleName(), models.get(getAdapterPosition()));

//                    detailPelanggan.putExtra("namapelanggan",models.get(getAdapterPosition()).getCustomerName());
//                    detailPelanggan.putExtra("nomortelpon",models.get(getAdapterPosition()).getPhone());
//                    detailPelanggan.putExtra("tanggalterdaftar",models.get(getAdapterPosition()).getCreatedAt());
//                    detailPelanggan.putExtra("totalhutang",models.get(getAdapterPosition()).getCredit());
//                    detailPelanggan.putExtra("hutangterbayar",models.get(getAdapterPosition()).getCreditPaid());
//                    detailPelanggan.putExtra("customerid",models.get(getAdapterPosition()).getCustomerId());

//                    detailPelanggan.putExtra("totalhutang",models.get(getAdapterPosition()).getCredit());
//                    detailPelanggan.putExtra("hutangterbayar",models.get(getAdapterPosition()).getCreditPaid());
                    ((Activity) mContext).startActivityForResult(detailPelanggan, 5);


                }
            });

        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent status) {

//        try {
//            super.onActivityResult(requestCode,resultCode,status);
//            if (requestCode == 5 && resultCode == RESULT_OK){
//
//                String requiredValue = status.getStringExtra("lock");
//
//                if (requiredValue!=null){
////                    if(transaksiBagianPelangganAdapter.getItemCount()>0){}
//                    transaksiBagianPelangganAdapter.clearModels();
//                    getData();
//
//                }

    }


}
