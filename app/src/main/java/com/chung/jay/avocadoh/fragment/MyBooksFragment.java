package com.chung.jay.avocadoh.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by jaychung on 5/29/16.
 */
public class MyBooksFragment extends Fragment {
    public static MyBooksFragment newInstance() {

        Bundle args = new Bundle();

        MyBooksFragment fragment = new MyBooksFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
