package co.crowde.toni.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.model.CustomerModel;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.view.fragment.CartListItem;
import co.crowde.toni.view.popup.InventoryDetailedPopup;

public class CustomerAdapter
        extends RecyclerView.Adapter<CustomerAdapter.ViewHolder>
        implements Filterable {

    private Context context;
    private Activity activity ;
    private List<CustomerModel> customerModels;
    private List<CustomerModel> customerModelsFiltered;

    private int lastPosition = -1;

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
    public CustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_customer_item, parent, false);

        CustomerAdapter.ViewHolder mViewHolder = new CustomerAdapter.ViewHolder(view);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomerAdapter.ViewHolder holder, final int position) {
        final CustomerModel model = customerModelsFiltered.get(position);
        CustomerAdapter.ViewHolder viewHolder = (CustomerAdapter.ViewHolder) holder;


        holder.tvName.setText(model.getCustomerName());
        holder.tvPhone.setText(model.getPhone());

        holder.layoutCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String customer = new Gson().toJson(model);
                SavePref.saveCustomerId(activity,model.getCustomerId());
                SavePref.saveCustomer(activity, customer);

                activity.finish();
                CartListItem.tvCustomer.setText(model.getCustomerName()+"\n"
                        +model.getPhone());
                CartListItem.imgCheck.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_check_box_white_24dp));
            }
        });

        setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return customerModelsFiltered!=null? customerModelsFiltered.size():0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    customerModelsFiltered = customerModels;
                } else {
                    List<CustomerModel> filteredList = new ArrayList<>();

                    for (CustomerModel row : customerModels) {
                        if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getPhone().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    customerModelsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = customerModelsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                customerModelsFiltered = (ArrayList<CustomerModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
