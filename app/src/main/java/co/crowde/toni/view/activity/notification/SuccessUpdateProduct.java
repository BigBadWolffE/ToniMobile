package co.crowde.toni.view.activity.notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import co.crowde.toni.R;

public class SuccessUpdateProduct extends AppCompatActivity {

    CardView cvBtnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_update_product);

        cvBtnHome = findViewById(R.id.cvBtnHome);

        cvBtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
