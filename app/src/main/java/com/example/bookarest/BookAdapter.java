package com.example.bookarest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends BaseAdapter {
    private ArrayList<Book> books;
    private Context         context;
    private LayoutInflater inflater;
    private List<Author> authors;


    public BookAdapter(ArrayList<Book> books, Context context, List<Author> authors) {
        this.books = books;
        this.context = context;
        this.authors = authors;
        inflater=LayoutInflater.from(context);
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View item = inflater.inflate(R.layout.book_item, parent,false);

        TextView tvTitle = item.findViewById((R.id.book_item_title));
        TextView tvAuthor = item.findViewById((R.id.book_item_author));
        RadioGroup rgOptions = item.findViewById(R.id.rg_options);
        RadioButton rb_wtr = item.findViewById(R.id.rb_wtr);
        RadioButton rb_cr = item.findViewById(R.id.rb_cr);
        RadioButton rb_r = item.findViewById(R.id.rb_r);

        tvTitle.setText(books.get(position).getTitle());
        String authorName;
        for(Author a: authors){
            if(a.getAuthorId()== books.get(position).getAuthorOfBookId()){
                tvAuthor.setText(a.getName());
            }
        }

        rb_wtr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    fragment_my_books.answers.set(position, 1);
                else
                    fragment_my_books.answers.set(position, 0);
            }
        });
        rb_cr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    fragment_my_books.answers.set(position, 2);
                else
                    fragment_my_books.answers.set(position, 0);
            }
        });
        rb_r.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    fragment_my_books.answers.set(position, 3);
                else
                    fragment_my_books.answers.set(position, 0);
            }
        });
        return item;
    }

}
