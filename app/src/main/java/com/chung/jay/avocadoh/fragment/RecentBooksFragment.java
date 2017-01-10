package com.chung.jay.avocadoh.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by jaychung on 5/29/16.
 */
public class RecentBooksFragment extends BookListFragment {
    public static RecentBooksFragment newInstance() {

        Bundle args = new Bundle();

        RecentBooksFragment fragment = new RecentBooksFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("books");
    }
}
