package co.crowde.toni.listener;

import android.view.View;

public interface ProductListener {

    void onItemClick(View v, int position);

    void onIncreaseItem(View v, int position);

    void onDecreaseItem(View v, int position);

    void onChangeQty(View v, int position);
}
