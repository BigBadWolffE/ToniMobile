package co.crowde.toni.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

import co.crowde.toni.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartPayment extends Fragment
implements View.OnClickListener{

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static TextView tvCreditLabel , tvCredit,
            tvPaymentLabel, tvPayment,
            tvAmountLabel, tvAmount,
            tvChangeLabel, tvChange;
    public static CardView cvCash, cvCredit, cvCashCredit,
            cvConfirm;

    public static boolean
            cash = false,
            credit = false,
            cashCredit = false;

    private static DecimalFormat formatNumber;

    public CartPayment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View  view =  inflater.inflate(R.layout.fragment_cart_payment, container, false);

        formatNumber = new DecimalFormat("###,###,###,###,###,###");

        tvCreditLabel = view.findViewById(R.id.tvCreditLabel);
        tvCredit = view.findViewById(R.id.tvCredit);
        tvPaymentLabel = view.findViewById(R.id.tvPaymentLabel);
        tvPayment = view.findViewById(R.id.tvPayment);
        tvAmountLabel = view.findViewById(R.id.tvAmountLabel);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvChangeLabel = view.findViewById(R.id.tvChangeLabel);
        tvChange = view.findViewById(R.id.tvChange);
        cvCash = view.findViewById(R.id.cvCash);
        cvCredit = view.findViewById(R.id.cvCredit);
        cvCashCredit = view.findViewById(R.id.cvCashCredit);
        cvConfirm = view.findViewById(R.id.cvConfirm);

        tvAmount.setText("Rp. "
                +String.valueOf(formatNumber.format(Dashboard.totalAmount))+",-");

        if(!cash){
            tvCreditLabel.setVisibility(View.GONE);
            tvCredit.setVisibility(View.GONE);

        } else if(!credit){
            tvCreditLabel.setVisibility(View.GONE);
            tvCredit.setVisibility(View.GONE);

        } else if(!cashCredit){
            tvCreditLabel.setVisibility(View.GONE);
            tvCredit.setVisibility(View.GONE);

        } else {
            tvCreditLabel.setVisibility(View.GONE);
            tvCredit.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.cvCash:

                // do your code
                break;

            case R.id.cvCredit:
                // do your code
                break;

            case R.id.cvCashCredit:
                // do your code
                break;

            default:
                break;
        }

    }


}
