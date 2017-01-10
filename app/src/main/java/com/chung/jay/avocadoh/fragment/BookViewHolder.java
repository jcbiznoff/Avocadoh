package com.chung.jay.avocadoh.fragment;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chung.jay.avocadoh.R;
import com.chung.jay.avocadoh.model.Book;
import com.squareup.picasso.Picasso;

/**
 * Created by jaychung on 5/29/16.
 */
public class BookViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public ImageView coverPhoto;

    public BookViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.post_title);
//        authorView = (TextView) itemView.findViewById(R.id.post_author);
        coverPhoto = (ImageView) itemView.findViewById(R.id.book_cover_photo);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
    }

    public void bindToBook(Uri uri, Book book, View.OnClickListener starClickListener) {
        titleView.setText(book.getName());
//        authorView.setText(book.author);
        numStarsView.setText(String.valueOf(book.getStarCount()));
        bodyView.setText(book.getSummary());


        Picasso.with(itemView.getContext()).load(uri).into(coverPhoto);

        starView.setOnClickListener(starClickListener);
    }
}
