package co.crowde.toni.zHackaton.fragment;


import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.crowde.toni.R;
import co.crowde.toni.zHackaton.activity.ProjectInformationActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectOwnerFragment extends Fragment {

    CardView cvBtnNext;


    public ProjectOwnerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_project_owner, container, false);

        cvBtnNext = view.findViewById(R.id.cv_btn_continue);

        cvBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectInformationActivity.toolbar.setTitle("Daftar Produk RAB");
                ProjectInformationActivity.toolbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                ProjectInformationActivity.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
                ProjectInformationActivity.toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
                ProjectDescriptionFragment descriptionFragment = new ProjectDescriptionFragment();
                FragmentTransaction descriptionTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                descriptionTransaction.setCustomAnimations(
                        R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
                descriptionTransaction.replace(R.id.layout_project, descriptionFragment);
                descriptionTransaction.commit();
                ProjectInformationActivity.show=true;
            }
        });

        return view;
    }

}
