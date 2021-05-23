package com.platform.cdcs.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.platform.cdcs.R;
import com.platform.cdcs.activity.ArbitraryFragmentActivity;

/**
 * Created by Trueway on 2015/7/14.
 */
public class FragmentUtil {

    public static final String ARGUMENTS_KEY_NO_BACK_STACK = "noBackStack";

    public static void navigateTo(FragmentManager manager, Fragment fragment,
                                  Bundle bundle) {
        FragmentTransaction transaction = manager.beginTransaction();
        if (bundle != null && bundle.getBoolean(ARGUMENTS_KEY_NO_BACK_STACK)) {
            bundle.remove("ARGUMENTS_KEY_NO_BACK_STACK");
        } else {
            transaction.addToBackStack(null);
        }
        fragment.setArguments(bundle);
        transaction.setCustomAnimations(R.anim.push_left_in,
                R.anim.push_left_out, R.anim.push_right_in,
                R.anim.push_right_out);
        transaction.replace(R.id.frame, fragment);
        transaction.commit();
        manager.executePendingTransactions();
    }

    public static void navigateToInNewActivity(Context activity,
                                               Class fragment, Bundle bundle) {
        Intent intent = new Intent(activity, ArbitraryFragmentActivity.class);
        intent.putExtra(ArbitraryFragmentActivity.EXTRAS_BUNDLE, bundle);
        intent.putExtra(ArbitraryFragmentActivity.EXTRAS_FRAGMENT_CLASS_NAME,
                fragment.getName());
        activity.startActivity(intent);
        ((Activity) activity).overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }
}
