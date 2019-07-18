package co.crowde.toni.utils.analytics;

import co.crowde.toni.helper.analytics.AnalyticsApplication;

public class AnalyticsToniUtils {

    public static void getEvent(String category, String action, String label){
        AnalyticsApplication.getInstance().trackEvent(category, action, label);
    }
}
