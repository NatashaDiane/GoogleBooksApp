package com.natasha.googlebooksapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.natasha.googlebooksapp.model.Book;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    SQLiteDatabase db;
    private final List<Book> bookList = new ArrayList<>();

    public DatabaseHandler (@Nullable Context context) {
        super(context, Contract.DATABASE_NAME, null, Contract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + Contract.TABLE_NAME + "("
                + Contract.KEY_ID + " INTEGER PRIMARY KEY, "
                + Contract.BOOK_IMAGE_URL + " TEXT, "
                + Contract.BOOK_TITLE + " TEXT, "
                + Contract.BOOK_CATEGORY + " TEXT, "
                + Contract.BOOK_PUBLISHED_DATE + " TEXT, "
                + Contract.BOOK_PAGE_COUNT + " TEXT, "
                + Contract.BOOK_PRICE + " TEXT, "
                + Contract.BOOK_AUTHOR + " TEXT, "
                + Contract.BOOK_PREV_LINK + " TEXT, "
                + Contract.BOOK_BUY_LINK + " TEXT,"
                + Contract.BOOK_DESCRIPTION + " TEXT);";
        sqLiteDatabase.execSQL(CREATE_TABLE);
        Log.v("Database Created Successfully : ", "True");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Contract.TABLE_NAME);
        //create a new one
        onCreate(sqLiteDatabase);
    }

    public int getTotalFavorites(){
        int totalFavorites;

        String query = "SELECT * FROM " + Contract.TABLE_NAME;
        SQLiteDatabase dba = this.getReadableDatabase();

        //For selection argument im putting null because i want to get everything from the table
        Cursor cursor = dba.rawQuery(query, null);

        //because the cursor now have all the rows in the table i use the getCount() that the Cursor has which returns
        // the number of items in the table.
        totalFavorites = cursor.getCount();

        cursor.close();
        db.close();
        return totalFavorites;

    }

    //a method to insert contents to the database
    public void addFavorite(Book book){
        //getwritableDatabase allows to write into the database
        SQLiteDatabase db = this.getWritableDatabase();

        //ContentValues is a key value pair object
        ContentValues values = new ContentValues();
        values.put(Contract.BOOK_TITLE, book.getTitle());
        values.put(Contract.BOOK_CATEGORY, book.getCategories());
        values.put(Contract.BOOK_PUBLISHED_DATE, book.getPublishedDate());
        values.put(Contract.BOOK_PAGE_COUNT, book.getPageCount());
        values.put(Contract.BOOK_PRICE, book.getBookPrice());
        values.put(Contract.BOOK_AUTHOR, book.getAuthors());
        values.put(Contract.BOOK_PREV_LINK, book.getPreviewLink());
        values.put(Contract.BOOK_BUY_LINK, book.getBuy());
        values.put(Contract.BOOK_DESCRIPTION, book.getDescription());
        values.put(Contract.BOOK_IMAGE_URL, book.getThumbnail());

        db.insert(Contract.TABLE_NAME, null, values);

        //this will just let me know that something has been added
        Log.v("Added to favorites list", "Yeeeeeees!");
        db.close();
    }


    public void deleteFavorite(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contract.TABLE_NAME, Contract.BOOK_TITLE + " = ?",new String[]{String.valueOf(title)});
        //this will just let me know that something has been deleted
        Log.v("Deleted Book: " + title , "Yeeeeeees!");
        db.close();
    }

    //a method that returns what the user has saved in the app and retrieves everything
    //returns a list of book objects
    public List<Book> getFavorites(){
        bookList.clear();
        String[] columns = {
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

        String sortOrder = Contract.KEY_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Contract.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()){
            do{
                Book book = new Book();
                book.setBookID(cursor.getInt(cursor.getColumnIndex(Contract.KEY_ID)));
                book.setTitle(cursor.getString(cursor.getColumnIndex(Contract.BOOK_TITLE)));
                book.setCategories(cursor.getString(cursor.getColumnIndex(Contract.BOOK_CATEGORY)));
                book.setPublishedDate(cursor.getString(cursor.getColumnIndex(Contract.BOOK_PUBLISHED_DATE)));
                book.setPageCount(cursor.getString(cursor.getColumnIndex(Contract.BOOK_PAGE_COUNT)));
                book.setBookPrice(cursor.getString(cursor.getColumnIndex(Contract.BOOK_PRICE)));
                book.setAuthors(cursor.getString(cursor.getColumnIndex(Contract.BOOK_AUTHOR)));
                book.setPreviewLink(cursor.getString(cursor.getColumnIndex(Contract.BOOK_PREV_LINK)));
                book.setBuy(cursor.getString(cursor.getColumnIndex(Contract.BOOK_BUY_LINK)));
                book.setDescription(cursor.getString(cursor.getColumnIndex(Contract.BOOK_DESCRIPTION)));
                book.setThumbnail(cursor.getString(cursor.getColumnIndex(Contract.BOOK_IMAGE_URL)));

                bookList.add(book);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }
}


