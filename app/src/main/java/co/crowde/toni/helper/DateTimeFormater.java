package co.crowde.toni.helper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import co.crowde.toni.view.fragment.transaction.DashboardReportFragment;
import co.crowde.toni.view.fragment.transaction.ListTransactionReportFragment;


public class DateTimeFormater {
    public static String currentDate, currentDatePlusOneDay, currentDateIndo, currentDateOpen, currentDateClosed;
    public static String currentThreeDays, currentWeek, currentMonth, getStrDate, getEndDate,
            currentThreeDaysIndo, currentWeekIndo, currentMonthIndo, getStrDateIndo, getEndDateIndo;
    public static Locale lokal = new Locale("id");

    public static void getCurrentDateOpen(Activity activity) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm | EEEE, dd MMMM yyyy", lokal);
        Date currentTime = new Date();
        currentDateOpen = dateFormat.format(currentTime);
        SavePref.saveOpenTime(activity, currentDateOpen);
    }

    public static void getCurrentDateClosed(Activity activity) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm | EEEE, dd MMMM yyyy", lokal);
        Date currentTime = new Date();
        currentDateClosed = dateFormat.format(currentTime);
        SavePref.saveClosedTime(activity, currentDateClosed);
    }

    public static void getCurrentDate(Activity activity) {
        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.DAY_OF_YEAR,+1);

        SimpleDateFormat dateFormatIndo = new SimpleDateFormat("EEEE, dd MMMM yyyy", lokal);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", lokal);
        Date today = calendar.getTime();
        currentDate = dateFormat.format(today);
        currentDatePlusOneDay = dateFormat.format(currentTime);
        currentDateIndo = dateFormatIndo.format(currentTime);
    }

    public static void getCurrentThreeDays(){
        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.DAY_OF_YEAR,+1);

        Calendar calendarThree = Calendar.getInstance();
        calendarThree.setTime(currentTime);
        calendarThree.add(Calendar.DAY_OF_YEAR,-3);

        SimpleDateFormat dateFormatIndo = new SimpleDateFormat("EEEE, dd MMMM yyyy", lokal);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", lokal);
        Date threeDays = calendarThree.getTime();
        currentThreeDays = dateFormat.format(threeDays);
        currentThreeDaysIndo = dateFormatIndo.format(threeDays);
    }

    public static void getCurrentWeek(){
        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.DAY_OF_YEAR,+1);

        Calendar calendarWeek = Calendar.getInstance();
        calendarWeek.setTime(currentTime);
        calendarWeek.add(Calendar.DAY_OF_YEAR,-6);

        SimpleDateFormat dateFormatIndo = new SimpleDateFormat("EEEE, dd MMMM yyyy", lokal);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", lokal);
        Date week = calendarWeek.getTime();
        currentWeek = dateFormat.format(week);
        currentWeekIndo = dateFormatIndo.format(week);
    }

    public static void getCurrentMonth(){
        Date currentTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        calendar.add(Calendar.DAY_OF_YEAR,+1);

        Calendar calendarMonth = Calendar.getInstance();
        calendarMonth.setTime(currentTime);
        calendarMonth.add(Calendar.DAY_OF_YEAR,-30);

        SimpleDateFormat dateFormatIndo = new SimpleDateFormat("EEEE, dd MMMM yyyy", lokal);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", lokal);
        Date month = calendarMonth.getTime();
        currentMonth = dateFormat.format(month);
        currentMonthIndo = dateFormatIndo.format(month);
    }

    public static void getEndDateRange(Activity activity){
        getEndDate = "";
        getEndDateIndo = "";
        Calendar endDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                endDate.set(Calendar.YEAR, year);
                endDate.set(Calendar.MONTH, monthOfYear);
                endDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", lokal);
                SimpleDateFormat dateFormatIndo = new SimpleDateFormat("EEEE, dd MMMM yyyy", lokal);

                getEndDate = format.format(endDate.getTime());
                getEndDateIndo = dateFormatIndo.format(endDate.getTime());
                DashboardReportFragment.tvEndDate.setText(getEndDate);
                DashboardReportFragment.enableDateRange(activity);

            }
        };

        new DatePickerDialog(activity, endDatePicker, endDate.get(
                Calendar.YEAR), endDate.get(Calendar.MONTH),endDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static void getStrDateRange(Activity activity){
        getStrDate = "";
        getStrDateIndo = "";
        Calendar strDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener strDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                strDate.set(Calendar.YEAR, year);
                strDate.set(Calendar.MONTH, monthOfYear);
                strDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", lokal);
                SimpleDateFormat dateFormatIndo = new SimpleDateFormat("EEEE, dd MMMM yyyy", lokal);

                getStrDate = format.format(strDate.getTime());
                getStrDateIndo = dateFormatIndo.format(strDate.getTime());
                DashboardReportFragment.tvStrDate.setText(getStrDate);
                DashboardReportFragment.enableDateRange(activity);

            }
        };

        new DatePickerDialog(activity, strDatePicker, strDate.get(
                Calendar.YEAR), strDate.get(Calendar.MONTH),strDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static void getEndDateRangeList(Activity activity){
        getEndDate = "";
        getEndDateIndo = "";
        Calendar endDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener endDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                endDate.set(Calendar.YEAR, year);
                endDate.set(Calendar.MONTH, monthOfYear);
                endDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", lokal);
                SimpleDateFormat dateFormatIndo = new SimpleDateFormat("EEEE, dd MMMM yyyy", lokal);

                getEndDate = format.format(endDate.getTime());
                getEndDateIndo = dateFormatIndo.format(endDate.getTime());
                ListTransactionReportFragment.tvEndDate.setText(getEndDate);
                ListTransactionReportFragment.enableDateRange(activity);

            }
        };

        new DatePickerDialog(activity, endDatePicker, endDate.get(
                Calendar.YEAR), endDate.get(Calendar.MONTH),endDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static void getStrDateRangeList(Activity activity){
        getStrDate = "";
        getStrDateIndo = "";
        Calendar strDate = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener strDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                strDate.set(Calendar.YEAR, year);
                strDate.set(Calendar.MONTH, monthOfYear);
                strDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", lokal);
                SimpleDateFormat dateFormatIndo = new SimpleDateFormat("EEEE, dd MMMM yyyy", lokal);

                getStrDate = format.format(strDate.getTime());
                getStrDateIndo = dateFormatIndo.format(strDate.getTime());
                ListTransactionReportFragment.tvStrDate.setText(getStrDate);
                ListTransactionReportFragment.enableDateRange(activity);

            }
        };

        new DatePickerDialog(activity, strDatePicker, strDate.get(
                Calendar.YEAR), strDate.get(Calendar.MONTH),strDate.get(Calendar.DAY_OF_MONTH)).show();
    }

}
