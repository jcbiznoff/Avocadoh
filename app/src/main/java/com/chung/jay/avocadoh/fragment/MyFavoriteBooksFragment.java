package com.chung.jay.avocadoh.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by jaychung on 5/29/16.
 */
public class MyFavoriteBooksFragment extends Fragment {
    public static MyFavoriteBooksFragment newInstance() {

        Bundle args = new Bundle();

        MyFavoriteBooksFragment fragment = new MyFavoriteBooksFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
