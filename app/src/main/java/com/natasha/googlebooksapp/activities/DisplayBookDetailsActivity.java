package com.natasha.googlebooksapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.snackbar.Snackbar;
import com.natasha.googlebooksapp.R;
import com.natasha.googlebooksapp.data.Contract;
import com.natasha.googlebooksapp.data.DatabaseHandler;
import com.natasha.googlebooksapp.model.Book;

public class DisplayBookDetailsActivity extends AppCompatActivity {

    private TextView bookTitle;
    private ImageView bookImage;
    private TextView bookCategory;
    private TextView bookAuthors;
    private TextView bookPrice;
    private TextView bookPageCount;
    private TextView bookDescription;
    private TextView prevLink;
    private TextView buyLink;
    private TextView bookPublishedDate;
    private Button shareButton;


    //database
    private SQLiteDatabase sDb;
    private DatabaseHandler databaseHandler;
    private Book favorite;

    //favebutton
    private int key_id;
    private String title = "", authors = "", description = "", categories = "", publishDate = "", price = "", buy = "", preview = "", thumbnail = "", pageCount = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_book_details);
        //hide the default actionBar
        getSupportActionBar().hide();

        //Adding to favorites
        DatabaseHandler faveDB = new DatabaseHandler(this);
        sDb = faveDB.getWritableDatabase();

        bookTitle = findViewById(R.id.bookTitleIdDets);
        bookAuthors = findViewById(R.id.bookAuthorsIdDets);
        bookPrice = findViewById(R.id.bookPriceIdDets);
        bookCategory = findViewById(R.id.bookCategoryIdDets);
        bookPageCount = findViewById(R.id.bookPageCountIdDets);
        bookDescription = findViewById(R.id.bookDescriptionIdDets);
        bookDescription.setMovementMethod(new ScrollingMovementMethod());
        bookImage = findViewById(R.id.bookImageIdDets);
        prevLink = findViewById(R.id.buttonPreviewIdDets);
        buyLink = findViewById(R.id.buttonBuyIdDets);
        bookPublishedDate = findViewById(R.id.bookPublishedIdDets);
        shareButton = findViewById(R.id.shareButton);

        //Bundles receive from BookRecyclerViewAdapter
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("book_title");
            authors = extras.getString("book_author");
            description = extras.getString("book_desc");
            categories = extras.getString("book_categories");
            publishDate = extras.getString("book_publish_date");
            price = extras.getString("price");
            buy = extras.getString("buy_link");
            preview = extras.getString("book_preview");
            thumbnail = extras.getString("book_thumbnail");
            pageCount = extras.getString("page_count");

            bookTitle.setText(title);
            bookAuthors.setText(authors);
            bookPrice.setText(price);
            bookCategory.setText(categories);
            bookPageCount.setText(pageCount);
            bookDescription.setText(description);
            bookPublishedDate.setText(publishDate);

            RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(android.R.drawable.ic_menu_gallery).error(android.R.drawable.ic_menu_gallery);
            Glide.with(this).load(thumbnail).apply(requestOptions).into(bookImage);
        }


        //Buy Button
        final String finalInfo = buy;
        if (!finalInfo.isEmpty()) {
            buyLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(finalInfo));
                    startActivity(i);
                }
            });
        } else {
            buyLink.setText(null);
            buyLink.setBackground(null);
        }

        //Preview Button
        final String finalPreview = preview;
        prevLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(finalPreview));
                startActivity(i);
            }
        });

        //Share Button
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBook();
            }
        });

        //Fave Button
        MaterialFavoriteButton materialFavoriteButton = (MaterialFavoriteButton) findViewById(R.id.faveButton);
        if (Exists(title)) {
            materialFavoriteButton.setFavorite(true);
            materialFavoriteButton.setOnFavoriteChangeListener(
                    new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override
                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            if (favorite == true) {
                                saveFavorite();
                                Snackbar.make(buttonView, "Added to Favorite",
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                databaseHandler = new DatabaseHandler(DisplayBookDetailsActivity.this);
                                databaseHandler.deleteFavorite(title);
                                Snackbar.make(buttonView, "Removed from Favorite",
                                        Snackbar.LENGTH_SHORT).show();
                                startActivity(new Intent(DisplayBookDetailsActivity.this, DisplayBooksFavoritesActivity.class));
                            }
                        }
                    });


        } else {
            materialFavoriteButton.setFavorite(false);
            materialFavoriteButton.setOnFavoriteChangeListener(
                    new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override
                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            if (favorite == true) {
                                saveFavorite();
                                Snackbar.make(buttonView, "Added to Favorite",
                                        Snackbar.LENGTH_SHORT).show();
                            } else {
                                String title = getIntent().getExtras().getString("book_title");
                                databaseHandler = new DatabaseHandler(DisplayBookDetailsActivity.this);
                                databaseHandler.deleteFavorite(title);
                                Snackbar.make(buttonView, "Removed from Favorite",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean Exists(String searchItem) {
        String[] projection = {
                Contract.KEY_ID,
                Contract.BOOK_TITLE,
                Contract.BOOK_CATEGORY,
                Contract.BOOK_PUBLISHED_DATE,
                Contract.BOOK_PAGE_COUNT,
                Contract.BOOK_PRICE,
                Contract.BOOK_AUTHOR,
                Contract.BOOK_PREV_LINK,
                Contract.BOOK_BUY_LINK,
                Contract.BOOK_DESCRIPTION,
                Contract.BOOK_IMAGE_URL
        };
        String selection = Contract.BOOK_TITLE + " =?";
        String[] selectionArgs = {searchItem};
        String limit = "1";

        Cursor cursor = sDb.query(Contract.TABLE_NAME, projection, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void saveFavorite() {
        databaseHandler = new DatabaseHandler(this);
        favorite = new Book();
        favorite.setTitle(title);
        favorite.setCategories(categories);
        favorite.setPublishedDate(publishDate);
        favorite.setPageCount(pageCount);
        favorite.setBookPrice(price);
        favorite.setAuthors(authors);
        favorite.setPreviewLink(preview);
        favorite.setBuy(buy);
        favorite.setDescription(description);
        favorite.setThumbnail(thumbnail);
        databaseHandler.addFavorite(favorite);
    }

    public void shareBook() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My favorite book");
            shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
            String title = bookTitle.getText().toString();
            String shareMessage= "Book Title: " + title + "\n\nPreview Link: \n";
            shareMessage = shareMessage + preview;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {

        }

    }
}

