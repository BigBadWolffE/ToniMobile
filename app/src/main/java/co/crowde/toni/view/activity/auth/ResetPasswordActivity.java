package co.crowde.toni.view.activity.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import co.crowde.toni.R;
import co.crowde.toni.view.fragment.cart.CartListItemFragment;
import co.crowde.toni.view.fragment.reset_password.ResetNewPassFragment;
import co.crowde.toni.view.fragment.reset_password.ResetPassOTPFragment;
import co.crowde.toni.view.fragment.reset_password.ResetPassPhoneFragment;

public class ResetPasswordActivity extends AppCompatActivity {

    public static TextView tvDot1, tvDot2, tvDot3;
    public static FrameLayout layout;
    public static String frames;
    public static String[] dots = {"one", "two", "three"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        tvDot1 = findViewById(R.id.tv_dot_1);
        tvDot2 = findViewById(R.id.tv_dot_2);
        tvDot3 = findViewById(R.id.tv_dot_3);
        layout = findViewById(R.id.layout_reset_password);

        frames = dots[0];

        changeLayout(ResetPasswordActivity.this, "");
    }

    public static void changeLayout(Activity activity, String phoneNumber) {
        switch (frames) {
            case "one":
                tvDot1.setTextColor(activity.getResources().getColor(R.color.colorThemeGreen));
                tvDot2.setTextColor(activity.getResources().getColor(R.color.TextColorLight));
                tvDot3.setTextColor(activity.getResources().getColor(R.color.TextColorLight));

                ResetPassPhoneFragment phone = new ResetPassPhoneFragment();
                FragmentTransaction phoneTransaction = ((FragmentActivity) activity).getSupportFragmentManager()
                        .beginTransaction();
//                phoneTransaction.setCustomAnimations(
//                        android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                phoneTransaction.replace(R.id.layout_reset_password, phone);
                phoneTransaction.commit();


                break;
            case "two":
                tvDot1.setTextColor(activity.getResources().getColor(R.color.TextColorLight));
                tvDot2.setTextColor(activity.getResources().getColor(R.color.colorThemeGreen));
                tvDot3.setTextColor(activity.getResources().getColor(R.color.TextColorLight));

                Bundle bundle = new Bundle();
                bundle.putString("phone",phoneNumber); // Put anything what you want

                ResetPassOTPFragment otp = new ResetPassOTPFragment();
                otp.setArguments(bundle);
                FragmentTransaction otpTransaction = ((FragmentActivity) activity).getSupportFragmentManager()
                        .beginTransaction();
//                otpTransaction.setCustomAnimations(
//                        android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                otpTransaction.replace(R.id.layout_reset_password, otp);
                otpTransaction.commit();


                break;
            case "three":
                tvDot1.setTextColor(activity.getResources().getColor(R.color.TextColorLight));
                tvDot2.setTextColor(activity.getResources().getColor(R.color.TextColorLight));
                tvDot3.setTextColor(activity.getResources().getColor(R.color.colorThemeGreen));

                ResetNewPassFragment pass = new ResetNewPassFragment();
                FragmentTransaction passTransaction = ((FragmentActivity) activity).getSupportFragmentManager()
                        .beginTransaction();
//                passTransaction.setCustomAnimations(
//                        android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                passTransaction.replace(R.id.layout_reset_password, pass);
                passTransaction.commit();
                break;
        }
    }
}
