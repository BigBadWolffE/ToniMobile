package co.crowde.toni.view.fragment.transaction;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.crowde.toni.R;
import co.crowde.toni.adapter.ProductDashboardAdapter;
import co.crowde.toni.adapter.ReportCustomerFavoriteAdapter;
import co.crowde.toni.adapter.ReportListTransactionAdapter;
import co.crowde.toni.adapter.ReportProductFavoriteAdapter;
import co.crowde.toni.constant.Const;
import co.crowde.toni.helper.DateTimeFormater;
import co.crowde.toni.model.ProductModel;
import co.crowde.toni.model.response.list.CustomerFavoriteModel;
import co.crowde.toni.model.response.list.ProductFavoriteModel;
import co.crowde.toni.model.response.list.TransactionModel;
import co.crowde.toni.network.ProductRequest;
import co.crowde.toni.network.ReportRequest;
import co.crowde.toni.network.TransactionRequest;
import co.crowde.toni.utils.analytics.AnalyticsToniUtils;

import static co.crowde.toni.view.fragment.modul.ReportFragment.progressDialog;

public class ListTransactionReportFragment extends Fragment implements View.OnClickListener {

    public static TextView labelSelectedDate, tvStrDate, tvEndDate, tvTransactionEmpty;
    public static ImageView imgShowSelectedDate;
    public static ConstraintLayout layoutSelectDate, layoutSelectRangeDate;
    public static RecyclerView rcTransaction;
    public static CardView cvBtnToday, cvBtnThreeDays, cvBtnWeek, cvBtnMonth, cvBtnRange;

    static Drawable show;
    static Drawable hide;

    public static ReportListTransactionAdapter reportListTransactionAdapter;
    public static List<TransactionModel> transactionModels;

    static DividerItemDecoration itemDecorator;

    public static String strDate, endDate, type;
    public static boolean isToday, isThreeDay, isWeek, isMonth, isDateRange;

    public ListTransactionReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_list_transaction_report, container, false);

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
        rcTransaction = view.findViewById(R.id.rc_list_transaction);
        tvTransactionEmpty = view.findViewById(R.id.tv_empty_field);

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

        initAdapterTransaction(getActivity());
        requestReport(getActivity());
        initScrollListener(getActivity());
        enableDateRange(getActivity());

        return view;
    }

    private void initAdapterTransaction(Activity activity) {
        transactionModels = new ArrayList<>();
        reportListTransactionAdapter = new ReportListTransactionAdapter(getActivity(), transactionModels, getActivity());

        rcTransaction.setLayoutManager(new LinearLayoutManager(activity));
        rcTransaction.addItemDecoration(itemDecorator);
        rcTransaction.setAdapter(reportListTransactionAdapter);
    }

    public static void updateDataTransaction(List<TransactionModel> transactionModelList, int page) {
        if (transactionModels.size() != 0) {
            transactionModels.remove(transactionModels.size() - 1);
            int scrollPosition = transactionModels.size();
            reportListTransactionAdapter.notifyItemRemoved(scrollPosition);
        }

        if (page == 1)
            transactionModels.clear();
        transactionModels.addAll(transactionModelList);
        reportListTransactionAdapter.replaceItemFiltered(transactionModels);
//        inventoryAdapter.notifyDataSetChanged();
        isLoading = false;

        if (page == 1)
            if (transactionModels.size() > 0)
                rcTransaction.scrollToPosition(0);
    }

    static boolean isLoading = false;

    public static void initScrollListener(final Activity activity) {
        rcTransaction.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == transactionModels.size() - 1) {
                        //bottom of list!
                        loadMoreProduct(activity);
                        isLoading = true;
                    }
                }
            }
        });
    }

    public static void loadMoreProduct(Activity activity) {
        transactionModels.add(null);
        reportListTransactionAdapter.notifyItemInserted(transactionModels.size() - 1);

        TransactionRequest.page = TransactionRequest.page + 1;
        TransactionRequest.getTransaction(activity, strDate, endDate);
    }

    private void requestReport(Activity activity) {
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(isToday){
            AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_TRANSACTION,Const.LABEL_TRANSACTION_TODAY);

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
            AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_TRANSACTION,Const.LABEL_TRANSACTION_THREE_DAY);

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
            AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_TRANSACTION,Const.LABEL_TRANSACTION_WEEK);

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
            AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_TRANSACTION,Const.LABEL_TRANSACTION_MONTH);

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
            AnalyticsToniUtils.getEvent(Const.CATEGORY_TRANSACTION,Const.MODUL_TRANSACTION,Const.LABEL_TRANSACTION_DATE_RANGE);

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

        TransactionRequest.page = 1;
        transactionModels.clear();
        TransactionRequest.getTransaction(activity, strDate, endDate);
//        ReportRequest.getReport(activity, strDate, endDate);

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
                DateTimeFormater.getStrDateRangeList(getActivity());

                break;

            case R.id.tv_last_date:
                DateTimeFormater.getEndDateRangeList(getActivity());

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

    public static void showListField(Activity activity) {
        if(ListTransactionReportFragment.transactionModels.size()!=0){
            tvTransactionEmpty.setVisibility(View.GONE);
        } else {
            tvTransactionEmpty.setVisibility(View.VISIBLE);
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