package co.crowde.toni.zHackaton.fragment;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.CatalogProductAdapter;
import co.crowde.toni.listener.ItemClickListener;
import co.crowde.toni.zHackaton.activity.ProjectInformationActivity;
import co.crowde.toni.zHackaton.adapter.ProductRabAdapter;
import co.crowde.toni.zHackaton.dialog.ConfirmProductRAB;
import co.crowde.toni.zHackaton.model.ProductRabModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectDescriptionFragment extends Fragment {

    CardView cvBtnBack, cvBtnConfirm;

    RecyclerView rcProductRAB;
    ProductRabAdapter adapter;
    List<ProductRabModel> productRabModels;

    DividerItemDecoration itemDecorator;

    ItemClickListener listener;


    public ProjectDescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_project_description, container, false);

        cvBtnBack = view.findViewById(R.id.cv_btn_continue);
        rcProductRAB = view.findViewById(R.id.rcProduct);
        cvBtnConfirm = view.findViewById(R.id.cvConfirm);

        cvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectInformationActivity.toolbar.setTitle("Informasi Pelaksana Proyek");
                ProjectInformationActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.colorThemeGreen));
                ProjectInformationActivity.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
                ProjectInformationActivity.toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

                ProjectOwnerFragment ownerFragment = new ProjectOwnerFragment();
                FragmentTransaction ownerTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                ownerTransaction.setCustomAnimations(
                        R.anim.anim_slide_in_up, R.anim.anim_slide_out_down);
                ownerTransaction.replace(R.id.layout_project, ownerFragment);
                ownerTransaction.commit();

                ProjectInformationActivity.show=false;
            }
        });

        itemDecorator = new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.divider_line_item));

        productRabModels = new ArrayList<>();
        addProduct();
        adapter = new ProductRabAdapter(getActivity(), productRabModels, getActivity(), listener);
        adapter = new ProductRabAdapter(getActivity(), productRabModels, getActivity(), new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }

            @Override
            public void onDeleteItemClick(View v, int position) {

            }

            @Override
            public void onDiscount(View v, int position) {

            }

            @Override
            public void onIncreaseItem(View v, int position) {
                int qty = productRabModels.get(position).getQty();
                productRabModels.get(position).setQty(qty+1);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onDecreaseItem(View v, int position) {
                int qty = productRabModels.get(position).getQty();
                if(qty!=0){
                    productRabModels.get(position).setQty(qty-1);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChangeQty(View v, int position, TextView tv) {

            }
        });

        rcProductRAB.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcProductRAB.addItemDecoration(itemDecorator);
        rcProductRAB.setAdapter(adapter);

        cvBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmProductRAB.showDialog(getActivity());

            }
        });

        return view;
    }

    public void addProduct(){
        productRabModels.add(new ProductRabModel(
                "Pupuk Kompos",
                "Pupuk",
                1));
        productRabModels.add(new ProductRabModel(
                "Pupuk Daur Ulang",
                "Pupuk",
                1));
        productRabModels.add(new ProductRabModel(
                "Pupuk Hama",
                "Pupuk",
                1));
        productRabModels.add(new ProductRabModel(
                "Bibit Sayur Kol",
                "Bibit",
                1));
        productRabModels.add(new ProductRabModel(
                "Bibit Sayur Bayam",
                "Bibit",
                1));
        productRabModels.add(new ProductRabModel(
                "Bibit Sayur Kangkung",
                "Bibit",
                1));
    }

}
