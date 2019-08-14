package co.crowde.toni.base;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by WayangForce on 11/14/17.
 */

public class BaseFragment extends Fragment {

    protected LayoutInflater mInflater;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mInflater = LayoutInflater.from(activity);
    }

    @Override
    public void onAttach(Context context) {
//        super.onAttach(CalligraphyContextWrapper.wrap(context));
        super.onAttach(context);
    }

    public void showLoading() {
        ((BaseActivity) getActivity()).showLoading();
    }

    public void dismissLoading() {
        ((BaseActivity) getActivity()).dismissLoading();
    }

    public static void disableSoftInputFromAppearing(EditText editText) {
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextIsSelectable(true);
    }

    public void hideSoftKeyboard(){
//        ((BaseActivity) getActivity()).hideSoftKeyboard();
    }

    public void replaceFragment(int container, BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
//        fragmentTransaction.commitNow();
    }

    /***** Check connectivity *****/
    protected boolean isNetworkConnected() {
        return ((BaseActivity) getActivity()).isNetworkConnected();
    }
}
