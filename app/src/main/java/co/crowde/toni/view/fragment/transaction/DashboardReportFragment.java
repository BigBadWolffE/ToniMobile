package co.crowde.toni.view.fragment.transaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import co.crowde.toni.R;
import co.crowde.toni.adapter.ReportCustomerFavoriteAdapter;
import co.crowde.toni.adapter.ReportListTransactionAdapter;
import co.crowde.toni.adapter.ReportProductFavoriteAdapter;
import co.crowde.toni.adapter.TransaksiWaktuPelangganAdapter;
import co.crowde.toni.constant.Const;
import co.crowde.toni.helper.CloseSoftKeyboard;
import co.crowde.toni.helper.DateTimeFormater;
import co.crowde.toni.model.response.list.CustomerFavoriteModel;
import co.crowde.toni.model.response.list.ProductFavoriteModel;
import co.crowde.toni.network.ProductRequest;
import co.crowde.toni.network.ReportRequest;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;

import static co.crowde.toni.view.fragment.modul.ReportFragment.progressDialog;

public class DashboardReportFragment extends Fragment implements View.OnClickListener{

    public static TextView labelSelectedDate, tvStrDate, tvEndDate, tvProductEmpty, tvCustomerEmpty;
    public static ImageView imgShowSelectedDate;
    public static ConstraintLayout layoutSelectDate, layoutSelectRangeDate;
    public static RecyclerView rcProductFavorite, rcCustomerFavorite;
    public static CardView cvBtnToday, cvBtnThreeDays, cvBtnWeek, cvBtnMonth, cvBtnRange;

    public static TextView tvTotalTransaction, tvFrequencyTransaction, tvMeanTransaction;

    static Drawable show;
    static Drawable hide;

    public static ReportProductFavoriteAdapter productFavoriteAdapter;
    public static List<ProductFavoriteModel> productFavoriteModels;

    public static ReportCustomerFavoriteAdapter customerFavoriteAdapter;
    public static List<CustomerFavoriteModel> customerFavoriteModels;

    static DividerItemDecoration itemDecorator;

    public static String strDate, endDate;
    public static boolean isToday, isThreeDay, isWeek, isMonth, isDateRange;

    public DashboardReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_dashboard_report, container, false);

        DateTimeFormater.getCurrentDate(getActivity());
        DateTimeFormater.getCurrentThreeDays();
        DateTimeFormater.getCurrentWeek();
        DateTimeFormater.getCurrentMonth();

        tvStrDate = view.findViewById(R.id.tv_begin_date);
        tvEndDate = view.findViewById(R.id.tv_last_date);
        labelSelectedDate = view.findViewById(R.id.tv_label_date_selected);
        imgShowSelectedDate = view.findViewById(R.id.img_show_or_hide);
        layoutSelectDate = view.findViewById(R.id.layout_select_date);
        layoutSelectRangeDate = view.findViewById(R.id.layout_select_range_date);
        cvBtnToday = view.findViewById(R.id.cv_btn_today);
        cvBtnThreeDays = view.findViewById(R.id.cv_btn_three_days);
        cvBtnWeek = view.findViewById(R.id.cv_btn_week);
        cvBtnMonth = view.findViewById(R.id.cv_btn_month);
        cvBtnRange = view.findViewById(R.id.cv_select_range_date);
        rcProductFavorite = view.findViewById(R.id.rc_product_favorite);
        rcCustomerFavorite = view.findViewById(R.id.rc_customer_favorite);

        tvProductEmpty = view.findViewById(R.id.tv_empty_field);
        tvCustomerEmpty = view.findViewById(R.id.tv_empty_field_customer);

        tvTotalTransaction = view.findViewById(R.id.tv_total_transaction);
        tvFrequencyTransaction = view.findViewById(R.id.tv_frequency_transaction);
        tvMeanTransaction = view.findViewById(R.id.tv_mean_transaction);

        imgShowSelectedDate.setOnClickListener(this);
        cvBtnToday.setOnClickListener(this);
        cvBtnThreeDays.setOnClickListener(this);
        cvBtnWeek.setOnClickListener(this);
        cvBtnMonth.setOnClickListener(this);
        cvBtnRange.setOnClickListener(this);
        tvStrDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);

        showImgSelected(getActivity(), getContext());

        itemDecorator = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.divider_line_item));

        isToday = true;
        isThreeDay = false;
        isWeek = false;
        isMonth = false;
        isDateRange = false;

        initAdapterProduct(getActivity());
        initAdapterCustomer(getActivity());
        requestReport(getActivity());
        enableDateRange(getActivity());

        return  view;
    }

    private void initAdapterProduct(Activity activity) {
        productFavoriteModels = new ArrayList<>();
        productFavoriteAdapter = new ReportProductFavoriteAdapter(getActivity(), productFavoriteModels, getActivity());

        rcProductFavorite.setLayoutManager(new LinearLayoutManager(activity));
        rcProductFavorite.addItemDecoration(itemDecorator);
        rcProductFavorite.setAdapter(productFavoriteAdapter);
    }

    private void initAdapterCustomer(Activity activity) {
        customerFavoriteModels = new ArrayList<>();
        customerFavoriteAdapter = new ReportCustomerFavoriteAdapter(getActivity(), customerFavoriteModels, getActivity());

        rcCustomerFavorite.setLayoutManager(new LinearLayoutManager(activity));
        rcCustomerFavorite.addItemDecoration(itemDecorator);
        rcCustomerFavorite.setAdapter(customerFavoriteAdapter);
    }

    private void requestReport(Activity activity) {
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(isToday){
            AnalyticsToniUtils.getEvent(Const.CATEGORY_REPORT,Const.MODUL_REPORT,Const.LABEL_REPORT_TODAY);

            cvBtnToday.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreen));
            cvBtnThreeDays.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnWeek.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnMonth.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnRange.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));

            cvBtnToday.setEnabled(false);
            cvBtnThreeDays.setEnabled(true);
            cvBtnWeek.setEnabled(true);
            cvBtnMonth.setEnabled(true);
            cvBtnRange.setEnabled(false);

            labelSelectedDate.setText(DateTimeFormater.currentDateIndo);

            strDate = DateTimeFormater.currentDatePlusOneDay;
            endDate = DateTimeFormater.currentDate;

        } else if(isThreeDay){
            AnalyticsToniUtils.getEvent(Const.CATEGORY_REPORT,Const.MODUL_REPORT,Const.LABEL_REPORT_THREE_DAY);

            cvBtnToday.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnThreeDays.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreen));
            cvBtnWeek.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnMonth.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnRange.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));

            cvBtnToday.setEnabled(true);
            cvBtnThreeDays.setEnabled(false);
            cvBtnWeek.setEnabled(true);
            cvBtnMonth.setEnabled(true);
            cvBtnRange.setEnabled(false);

            labelSelectedDate.setText(DateTimeFormater.currentThreeDaysIndo+" - "+DateTimeFormater.currentDateIndo);

            strDate = DateTimeFormater.currentThreeDays;
            endDate = DateTimeFormater.currentDate;

        } else if(isWeek){
            AnalyticsToniUtils.getEvent(Const.CATEGORY_REPORT,Const.MODUL_REPORT,Const.LABEL_REPORT_WEEK);

            cvBtnToday.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnThreeDays.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnWeek.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreen));
            cvBtnMonth.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnRange.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));

            labelSelectedDate.setText(DateTimeFormater.currentWeekIndo+" - "+DateTimeFormater.currentDateIndo);

            cvBtnToday.setEnabled(true);
            cvBtnThreeDays.setEnabled(true);
            cvBtnWeek.setEnabled(false);
            cvBtnMonth.setEnabled(true);
            cvBtnRange.setEnabled(false);

            strDate = DateTimeFormater.currentWeek;
            endDate = DateTimeFormater.currentDate;

        } else if(isMonth){
            AnalyticsToniUtils.getEvent(Const.CATEGORY_REPORT,Const.MODUL_REPORT,Const.LABEL_REPORT_MONTH);

            cvBtnToday.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnThreeDays.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnWeek.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnMonth.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreen));
            cvBtnRange.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));

            labelSelectedDate.setText(DateTimeFormater.currentMonthIndo+" - "+DateTimeFormater.currentDateIndo);

            cvBtnToday.setEnabled(true);
            cvBtnThreeDays.setEnabled(true);
            cvBtnWeek.setEnabled(true);
            cvBtnMonth.setEnabled(false);
            cvBtnRange.setEnabled(false);

            strDate = DateTimeFormater.currentMonth;
            endDate = DateTimeFormater.currentDate;

        } else if(isDateRange){
            AnalyticsToniUtils.getEvent(Const.CATEGORY_REPORT,Const.MODUL_REPORT,Const.LABEL_REPORT_DATE_RANGE);

            cvBtnToday.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnThreeDays.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnWeek.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnMonth.setCardBackgroundColor(getResources().getColor(R.color.colorThemeGreyLight));
            cvBtnRange.setCardBackgroundColor(getResources().getColor(R.color.colorThemeOrange));

            labelSelectedDate.setText(DateTimeFormater.getStrDateIndo+" - "+DateTimeFormater.getEndDateIndo);

            cvBtnToday.setEnabled(true);
            cvBtnThreeDays.setEnabled(true);
            cvBtnWeek.setEnabled(true);
            cvBtnMonth.setEnabled(true);
            cvBtnRange.setEnabled(false);

            strDate = DateTimeFormater.getStrDate;
            endDate = DateTimeFormater.getEndDate;
        }

        ReportRequest.getReport(activity, strDate, endDate);

    }

    public void showLayout(){
        if (layoutSelectDate.getVisibility() == View.GONE &&
                layoutSelectRangeDate.getVisibility() == View.GONE) {

            layoutSelectDate.setVisibility(View.VISIBLE);
            layoutSelectRangeDate.setVisibility(View.VISIBLE);

        } else {
            layoutSelectDate.setVisibility(View.GONE);
            layoutSelectRangeDate.setVisibility(View.GONE);
        }
        showImgSelected(getActivity(), getContext());
    }

    public void showImgSelected(final Activity activity, final Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            hide = context.getDrawable(R.drawable.ic_close_black_24dp);
            show = context.getDrawable(R.drawable.ic_menu_24px);
        } else {
            hide = activity.getResources().getDrawable(R.drawable.ic_close_black_24dp);
            show = activity.getResources().getDrawable(R.drawable.ic_menu_24px);
        }

        if (layoutSelectDate.getVisibility() == View.GONE &&
                layoutSelectRangeDate.getVisibility() == View.GONE) {

            imgShowSelectedDate.setImageDrawable(show);

        } else {
            imgShowSelectedDate.setImageDrawable(hide);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_show_or_hide:
                showLayout();

                break;

            case R.id.cv_btn_today:
                isToday = true;
                isThreeDay = false;
                isWeek = false;
                isMonth = false;
                isDateRange = false;

                tvStrDate.setText("dd-MM-yyyy");
                tvEndDate.setText("dd-MM-yyyy");

                requestReport(getActivity());

                break;

            case R.id.cv_btn_three_days:
                isToday = false;
                isThreeDay = true;
                isWeek = false;
                isMonth = false;
                isDateRange = false;

                tvStrDate.setText("dd-MM-yyyy");
                tvEndDate.setText("dd-MM-yyyy");

                requestReport(getActivity());

                break;

            case R.id.cv_btn_week:
                isToday = false;
                isThreeDay = false;
                isWeek = true;
                isMonth = false;
                isDateRange = false;

                tvStrDate.setText("dd-MM-yyyy");
                tvEndDate.setText("dd-MM-yyyy");

                requestReport(getActivity());

                break;

            case R.id.cv_btn_month:
                isToday = false;
                isThreeDay = false;
                isWeek = false;
                isMonth = true;
                isDateRange = false;

                tvStrDate.setText("dd-MM-yyyy");
                tvEndDate.setText("dd-MM-yyyy");

                requestReport(getActivity());

                break;

            case R.id.tv_begin_date:
                DateTimeFormater.getStrDateRange(getActivity());

                break;

            case R.id.tv_last_date:
                DateTimeFormater.getEndDateRange(getActivity());

                break;

            case R.id.cv_select_range_date:
                isToday = false;
                isThreeDay = false;
                isWeek = false;
                isMonth = false;
                isDateRange = true;

                requestReport(getActivity());

                break;

            default:
                break;
        }

    }

    public static void enableDateRange(Activity activity){
        if(tvStrDate.getText().toString().equals("dd-MM-yyyy")
                || tvEndDate.getText().toString().equals("dd-MM-yyyy")){
            cvBtnRange.setEnabled(false);
            cvBtnRange.setCardBackgroundColor(activity.getResources().getColor(R.color.colorThemeGreyLight));
        } else {
            cvBtnRange.setEnabled(true);
            cvBtnRange.setCardBackgroundColor(activity.getResources().getColor(R.color.colorThemeOrange));
        }
    }
}
