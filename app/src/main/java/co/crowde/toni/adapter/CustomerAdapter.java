package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.view.fragment.cart.CartListItem;
import co.crowde.toni.view.activity.cart.CartList;

public class CustomerAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Activity activity ;
    private List<CustomerModel> customerModels;
    private List<CustomerModel> customerModelsFiltered;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPhone;
        ConstraintLayout layoutCustomer;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            layoutCustomer = itemView.findViewById(R.id.layoutCustomer);

        }
    }

    public CustomerAdapter(Context context,
                           List<CustomerModel> customerModels,
                           Activity activity) {
        this.customerModels = customerModels;
        this.customerModelsFiltered = customerModels;
        this.context = context;
        this.activity = activity;
//        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_customer_item, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress_bar, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final CustomerModel model = customerModelsFiltered.get(position);
            CustomerAdapter.ViewHolder viewHolder = (CustomerAdapter.ViewHolder) holder;
            if (model != null) {
                viewHolder.tvName.setText(model.getCustomerName());
                viewHolder.tvPhone.setText(model.getPhone());

                viewHolder.layoutCustomer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String customer = new Gson().toJson(model);
                        SavePref.saveCustomerId(activity,model.getCustomerId());
                        SavePref.saveCustomer(activity, customer);

                        activity.finish();
                        CartListItem.tvCustomer.setText(model.getCustomerName()+"\n"
                                +model.getPhone());
                        CartListItem.imgCheck.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_check_box_white_24dp));
                        CartList.enabledButton(activity);
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
        return customerModelsFiltered!=null? customerModelsFiltered.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        return customerModelsFiltered.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

}
