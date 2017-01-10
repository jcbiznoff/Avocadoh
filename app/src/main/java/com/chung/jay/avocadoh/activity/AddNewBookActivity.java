package com.chung.jay.avocadoh.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.ImageView;

import com.chung.jay.avocadoh.R;
import com.chung.jay.avocadoh.model.Book;
import com.chung.jay.avocadoh.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by jaychung on 5/28/16.
 */
public class AddNewBookActivity extends BaseActivity {

    private static final int PICK_PHOTO_FOR_AVATAR = 10;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    private InputStream bookPhotoStream;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://project-1023283479541280095.appspot.com");
    }

    @Bind(R.id.new_book_title)
    EditText bookTitle;
    @Bind(R.id.new_book_author)
    EditText bookAuthor;
    @Bind(R.id.new_book_pageNum)
    EditText bookPageNum;
    @Bind(R.id.new_book_summary)
    EditText bookSummary;
    @Bind(R.id.new_book_photo)
    ImageView bookPhoto;

    @OnClick(R.id.new_book_photo)
    public void loadphoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                bookPhotoStream = getApplicationContext().getContentResolver().openInputStream(data.getData());

                //set image view from stream
                bookPhoto.setImageBitmap(BitmapFactory.decodeStream(bookPhotoStream));
                bookPhoto.setDrawingCacheEnabled(true);
                bookPhoto.buildDrawingCache();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }


    @OnClick(R.id.btnSubmit)
    public void submitNewBook() {
        final String userId = getUid();

        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                        } else {
                            // Write new post
                            String title = bookTitle.getText().toString();
                            String author = bookAuthor.getText().toString();
                            int pageNum = Integer.valueOf(bookPageNum.getText().toString());
                            String summary = bookSummary.getText().toString();

                            addNewBook(userId, user.getUsername(),
                                    author, title, pageNum, summary);
                        }

                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        // [END single_value_read]
    }

    private void addNewBook(final String userId, final String username, final String author, final String title, final int pagNum, final String summary) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        final String bookKey = mDatabase.child("books").push().getKey();

        if (bookPhotoStream == null) {
            updateBookInfo(bookKey, userId, username, author, title, pagNum, summary, null);
            return;
        }

        //Store photo
        StorageReference booksRef = mStorageRef.child("images").child("books").child(bookKey + ".jpg");

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();


        Bitmap bitmap2 = bookPhoto.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = booksRef.putBytes(data, metadata);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // Handle unsuccessful uploads
                Snackbar.make(getCurrentFocus(), "Failed to upload photo. Please try again", Snackbar.LENGTH_LONG).show();
                bookPhotoStream = null;
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri url = taskSnapshot.getDownloadUrl();
                updateBookInfo(bookKey, userId, username, author, title, pagNum, summary, url.getEncodedPath());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = 100.0 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        });;
    }

    private void updateBookInfo(String bookKey, String userId, String username, String author, String title, int pagNum, String summary, String photourl) {
        Book book = new Book(userId, username, author, title, pagNum, summary, photourl);
        Map<String, Object> postValues = book.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/books/" + bookKey, postValues);
        childUpdates.put("/user-books/" + userId + "/" + bookKey, postValues);
        mDatabase.updateChildren(childUpdates);
        //finish();
    }
}
