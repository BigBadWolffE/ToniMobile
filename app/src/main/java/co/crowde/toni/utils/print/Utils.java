package co.crowde.toni.utils.print;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static boolean calculateDateBetweenTwoDays(String strDate){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date productDate = sdf.parse(strDate);
//            String formattedTime = output.format(d);

            Date currentTime = Calendar.getInstance().getTime();

////            String dateStr = "2/3/2017";
//            String dateStr = "2/3/2017";
//            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//            Date startDate = sdf.parse(dateStr);

            long diff = currentTime.getTime() - productDate.getTime();
            float days = (diff / (1000*60*60*24));
            System.out.println("jumlah Days : " + days);

            return (int) days == 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
