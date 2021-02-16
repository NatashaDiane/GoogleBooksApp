package com.natasha.googlebooksapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.natasha.googlebooksapp.R;
import com.natasha.googlebooksapp.adapter.BookRecyclerViewAdapter;
import com.natasha.googlebooksapp.data.DatabaseHandler;
import com.natasha.googlebooksapp.model.Book;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class DisplayBooksFavoritesActivity extends AppCompatActivity {

    private List<Book> bookList;
    private RecyclerView recyclerView;
    private BookRecyclerViewAdapter bookRecyclerViewAdapter;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_book_favorites);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFavorites);
        bookList = new ArrayList<>();
        bookRecyclerViewAdapter = new BookRecyclerViewAdapter(this, bookList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(bookRecyclerViewAdapter);
        bookRecyclerViewAdapter.notifyDataSetChanged();
        db = new DatabaseHandler(DisplayBooksFavoritesActivity.this);
        getAllFavorite();
    }

    @SuppressLint("StaticFieldLeak")
    private void getAllFavorite(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params){
                bookList.clear();
                bookList.addAll(db.getFavorites());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                bookRecyclerViewAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_favorites, menu);
        return true;
    }

    //favorites menu button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_back) {
            Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goToNextActivity);

        }

        return super.onOptionsItemSelected(item);
    }

}
