package com.natasha.googlebooksapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.natasha.googlebooksapp.R;
import com.natasha.googlebooksapp.adapter.BookRecyclerViewAdapter;
import com.natasha.googlebooksapp.data.DatabaseHandler;
import com.natasha.googlebooksapp.model.Book;
import com.natasha.googlebooksapp.utils.Constants;
import com.natasha.googlebooksapp.utils.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplayBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookRecyclerViewAdapter bookRecyclerViewAdapter;
    private List<Book> bookList;

    //volley library
    private RequestQueue queue;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_books);

        //Volley
        queue = Volley.newRequestQueue(this);

        //floating search button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        //setting up the recycleview
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //instantiate booklist
        bookList = new ArrayList<>();

        Prefs prefs = new Prefs(DisplayBooksActivity.this);
        Bundle extras = getIntent().getExtras();
        String userInput;

        if (extras != null) {
            userInput = extras.getString("UserInput");
            String search = userInput;
            prefs.setSearch(search);
            bookList = getBooks(search);

            bookRecyclerViewAdapter = new BookRecyclerViewAdapter(this, bookList);
            recyclerView.setAdapter(bookRecyclerViewAdapter);
            bookRecyclerViewAdapter.notifyDataSetChanged();
        }
    }


    //method that gets books from the api
    public List<Book> getBooks(String searchTerm){
        //Everytime this method is called it clears everything from the list and repopulate it
        bookList.clear();

        //Our JsonObject request, this is from the volley library
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL_LEFT + searchTerm + Constants.URL_RIGHT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String title ="";
                String author ="";
                String publishedDate = "Not Available";
                String description = "Description: Not Available";
                String pageCount = "Not Available";
                String categories = "No Category";
                String buy ="";
                String price = "Currently Unavailable!";

                try {
                    //all of the arrays inside the jsonobject are now inside booksArray
                    JSONArray booksArray = response.getJSONArray("items");

                    //iterate through those arrays
                    for (int i = 0; i < booksArray.length(); i++){
                        JSONObject bookObj = booksArray.getJSONObject(i);
                        JSONObject volumeInfo = bookObj.getJSONObject("volumeInfo");

                        try{
                            title = volumeInfo.getString("title");
                            JSONArray authors = volumeInfo.getJSONArray("authors");
                            if(authors.length() == 1){
                                author = authors.getString(0);
                            }else {
                                author = authors.getString(0) + " | " +authors.getString(1);
                            }
                            publishedDate = volumeInfo.getString("publishedDate");
                            pageCount = volumeInfo.getString("pageCount");

                            JSONObject saleInfo = bookObj.getJSONObject("saleInfo");
                            JSONObject listPrice = saleInfo.getJSONObject("retailPrice");
                            price = listPrice.getString("amount") + " " +listPrice.getString("currencyCode");
                            description = volumeInfo.getString("description");
                            buy = saleInfo.getString("buyLink");
                            categories = volumeInfo.getJSONArray("categories").getString(0);
                        }catch (Exception e){}

                        String thumbnail = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                        String previewLink = volumeInfo.getString("previewLink");
                        String infoLink = volumeInfo.getString("infoLink");

                        Book book = new Book();
                        book.setTitle(title);
                        book.setAuthors("Author/s: " +author);
                        book.setPublishedDate("Published date: " + publishedDate);
                        book.setDescription(description);
                        book.setCategories("Category: "+ categories);
                        book.setThumbnail(thumbnail);
                        book.setInfoLink(infoLink);
                        book.setPreviewLink(previewLink);
                        book.setBookPrice(price);
                        book.setBuy(buy);
                        book.setPageCount("Page count: " + pageCount + " pages");

                        Log.d("Books: ", book.getTitle());
                        bookList.add(book);
                    }

                    /**
                     * Very important!! Otherwise, nothing will be displayed.
                     */
                    bookRecyclerViewAdapter.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Ooops, Error fetching data!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
        return bookList;
    }


    public void showInputDialog(){
        alertDialogBuilder = new AlertDialog.Builder(this);
        //inflate the dialog search view
        View view = getLayoutInflater().inflate(R.layout.dialog_search_view, null);
        final EditText newSearchEdit = (EditText) view.findViewById(R.id.searchEdit);
        Button submitButton = (Button) view.findViewById(R.id.submitButton);

        alertDialogBuilder.setView(view);
        dialog = alertDialogBuilder.create();
        dialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs prefs = new Prefs(DisplayBooksActivity.this);

                if (!newSearchEdit.getText().toString().isEmpty()){
                    String search = newSearchEdit.getText().toString();
                    prefs.setSearch(search);
                    bookList.clear();
                    getBooks(search);

                    //important, otherwise it wont show anything or repopulate the recycler view with new books
                    bookRecyclerViewAdapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_books, menu);
        return true;
    }

    //favorites menu button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorites_display_books_page) {
            Intent goToNextActivity = new Intent(getApplicationContext(), DisplayBooksFavoritesActivity.class);
            startActivity(goToNextActivity);
        }else if (id == R.id.action_search_display_books_page) {
            showInputDialog();
        }

        return super.onOptionsItemSelected(item);
    }

}
