package co.crowde.toni.zHackaton.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import co.crowde.toni.R;
import co.crowde.toni.helper.OnSwipeTouchListener;
import co.crowde.toni.helper.SavePref;
import co.crowde.toni.view.activity.cart.CartListActivity;
import co.crowde.toni.zHackaton.fragment.ProjectDescriptionFragment;
import co.crowde.toni.zHackaton.fragment.ProjectOwnerFragment;

public class ProjectInformationActivity extends AppCompatActivity{

    public static FrameLayout layout;
    public static Toolbar toolbar;

    public static boolean show;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_information);

        layout = findViewById(R.id.layout_project);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        show  = false;

        ProjectOwnerFragment ownerFragment = new ProjectOwnerFragment();
        FragmentTransaction ownerTransaction = getSupportFragmentManager()
                .beginTransaction();
        ownerTransaction.setCustomAnimations(
                R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
        ownerTransaction.replace(R.id.layout_project, ownerFragment);
        ownerTransaction.commit();

        layout.setOnTouchListener(new OnSwipeTouchListener(ProjectInformationActivity.this) {
            public void onSwipeTop() {
                if(!show){
                    toolbar.setTitle("Daftar Produk RAB");
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
                    ProjectDescriptionFragment descriptionFragment = new ProjectDescriptionFragment();
                    FragmentTransaction descriptionTransaction = getSupportFragmentManager()
                            .beginTransaction();
                    descriptionTransaction.setCustomAnimations(
                            R.anim.anim_slide_in_down, R.anim.anim_slide_out_up);
                    descriptionTransaction.replace(R.id.layout_project, descriptionFragment);
                    descriptionTransaction.commit();
                    show=true;
                }

            }
            public void onSwipeRight() {
            }
            public void onSwipeLeft() {
            }
            public void onSwipeBottom() {
                if(show){
                    toolbar.setTitle("Informasi Pelaksana Proyek");
                    toolbar.setBackgroundColor(getResources().getColor(R.color.colorThemeGreen));
                    toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
                    ProjectOwnerFragment ownerFragment = new ProjectOwnerFragment();
                    FragmentTransaction ownerTransaction = getSupportFragmentManager()
                            .beginTransaction();
                    ownerTransaction.setCustomAnimations(
                            R.anim.anim_slide_in_up, R.anim.anim_slide_out_down);
                    ownerTransaction.replace(R.id.layout_project, ownerFragment);
                    ownerTransaction.commit();

                    show=false;
                }

            }

        });
    }
}
