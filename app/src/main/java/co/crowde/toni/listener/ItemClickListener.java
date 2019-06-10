package co.crowde.toni.listener;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Firiyah on 1/25/2019.
 */

public interface ItemClickListener {
    void onItemClick(View v, int position);

    void onDeleteItemClick(View v, int position);

    void onIncreaseItem(View v, int position);

    void onDecreaseItem(View v, int position);

    void onChangeQty(View v, int position, TextView tv);
}
