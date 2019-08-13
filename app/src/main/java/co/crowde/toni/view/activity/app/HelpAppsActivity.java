package co.crowde.toni.view.activity.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.appbar.AppBarLayout;

import co.crowde.toni.R;
import co.crowde.toni.utils.SetHeader;
import co.crowde.toni.view.activity.auth.RegisterActivity;

public class HelpAppsActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    LinearLayout layoutGuide, layoutPrivacy, layoutAbout;
    AppBarLayout appBarLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_apps);

        appBarLayout = findViewById(R.id.appBar);
        SetHeader.isLolipop(HelpAppsActivity.this, appBarLayout);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        layoutGuide = findViewById(R.id.layout_guide);
        layoutPrivacy = findViewById(R.id.layout_privacy_police);
        layoutAbout = findViewById(R.id.layout_about);

        layoutGuide.setOnClickListener(this);
        layoutPrivacy.setOnClickListener(this);
        layoutAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_guide:
//
                break;

            case R.id.layout_privacy_police:
                Intent privacy = new Intent("android.intent.action.VIEW",
                        Uri.parse("https://tonipos.herokuapp.com/Privacy%20Policy%20&%20Terms%20of%20Use%20TONI.pdf"));
                startActivity(privacy);
                break;

            case R.id.layout_about:
                Intent about = new Intent(HelpAppsActivity.this, AboutAppsActivity.class);
                startActivity(about);
                break;
        }

    }
}
