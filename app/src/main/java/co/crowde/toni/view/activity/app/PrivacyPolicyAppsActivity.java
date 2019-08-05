package co.crowde.toni.view.activity.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.google.android.material.appbar.AppBarLayout;

import co.crowde.toni.R;
import co.crowde.toni.utils.SetHeader;

public class PrivacyPolicyAppsActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    AppBarLayout appBarLayout;
    Toolbar toolbar;
    WebView viewPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy_apps);

        appBarLayout = findViewById(R.id.appBar);
        SetHeader.isLolipop(PrivacyPolicyAppsActivity.this, appBarLayout);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        viewPrivacy = findViewById(R.id.view_privacy_policy);
////        viewPrivacy.getSettings().setJavaScriptEnabled(true);
////        viewPrivacy.getSettings().setSupportZoom(true);
////        viewPrivacy.getSettings().setJavaScriptEnabled(true);
//        String pdf = "http://www.pdf995.com/samples/pdf.pdf";
//        viewPrivacy.loadUrl("https://drive.google.com/file/d/1qFmvz7OVqIJmf896l6xx9TmTXQ5nJwYe/view?ts=5d395250");
    }
}
