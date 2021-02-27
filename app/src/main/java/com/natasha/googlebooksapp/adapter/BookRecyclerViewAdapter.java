package com.natasha.googlebooksapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.natasha.googlebooksapp.activities.DisplayBookDetailsActivity;
import com.natasha.googlebooksapp.model.Book;
import java.util.List;

import com.natasha.googlebooksapp.R;


public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Book> bookList;
    private RequestOptions options;

/*    private int layoutResource;
    private Activity activity;*/

    public BookRecyclerViewAdapter(Context context, List<Book> books) {
        this.context = context;
        bookList = books;

        //Request option for Glide Image
        options = new RequestOptions().centerCrop().placeholder(android.R.drawable.ic_menu_gallery).error(android.R.drawable.ic_menu_gallery);
    }

/*    public BookRecyclerViewAdapter(@NonNull Activity act, int resource, List<Book> data) {

        layoutResource = resource;
        activity = act;
        bookList = data;
        notifyDataSetChanged(); *//*this makes everything refreshed correctly*//*
    }*/

    @NonNull
    @Override
    public BookRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_row, parent, false);
        return new ViewHolder(view, context);

    }

    @Override
    public void onBindViewHolder(@NonNull BookRecyclerViewAdapter.ViewHolder holder, int position) {
        Book book = bookList.get(position);
        String thumbnail = book.getThumbnail();

        holder.title.setText(book.getTitle());
        holder.categories.setText(book.getCategories());
        holder.price.setText(book.getBookPrice());
        holder.authors.setText(book.getAuthors());

        //load image from internet and set it into imageView using Glide
        Glide.with(context).load(thumbnail).apply(options).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }


    //inner class
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView thumbnail;
        TextView categories;
        TextView price;
        TextView authors;
        MaterialFavoriteButton favoriteButton;


        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            title = (TextView) itemView.findViewById(R.id.bookTitleID);
            thumbnail = (ImageView) itemView.findViewById(R.id.bookImageID);
            categories = (TextView) itemView.findViewById(R.id.bookCategoryID);
            price = (TextView) itemView.findViewById(R.id.bookPriceID);
            authors = (TextView) itemView.findViewById(R.id.bookAuthorID);
            favoriteButton = (MaterialFavoriteButton) itemView.findViewById(R.id.faveButton);


            //This will take users to the next screen which is the book details screen
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context , DisplayBookDetailsActivity.class);
                    int pos = getAdapterPosition();
                    i.putExtra("book_title" ,bookList.get(pos).getTitle());
                    i.putExtra("book_author" ,bookList.get(pos).getAuthors());
                    i.putExtra("book_desc" ,bookList.get(pos).getDescription());
                    i.putExtra("book_categories" ,bookList.get(pos).getCategories());
                    i.putExtra("book_publish_date" ,bookList.get(pos).getPublishedDate());
                    i.putExtra("buy_link" ,bookList.get(pos).getBuy());
                    i.putExtra("price" ,bookList.get(pos).getBookPrice());
                    i.putExtra("book_preview" ,bookList.get(pos).getPreviewLink());
                    i.putExtra("book_thumbnail" ,bookList.get(pos).getThumbnail());
                    i.putExtra("page_count", bookList.get(pos).getPageCount());

                    context.startActivity(i);
                }
            });
        }

    }

}

