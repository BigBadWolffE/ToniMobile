package co.crowde.toni.adapter.chips;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.model.CategoryModel;

public class CategoryChipsFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Activity activity ;
    private List<CategoryModel> categoryModelList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView imgClose;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_chips_name);
            imgClose = itemView.findViewById(R.id.img_chips_close);

        }
    }

    public CategoryChipsFilterAdapter(Context context,
                           List<CategoryModel> categoryModels,
                           Activity activity) {
        this.context = context;
        this.categoryModelList.clear();
        this.categoryModelList.addAll(categoryModels);
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chips_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final CategoryModel model = categoryModelList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        if (model != null) {
            viewHolder.tvName.setText(model.getCategoryName());
//                viewHolder.tvName.setText(model.getCustomerName());
            viewHolder.imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

    }

    @Override
    public int getItemCount(){
        return categoryModelList != null ? categoryModelList.size() : 0;
    }
}
