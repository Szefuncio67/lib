package com.example.lib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class HomeScreen extends AppCompatActivity {
    public static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

    TextView tName;
    SearchView searchView;
    Button btnFavorites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tName = findViewById(R.id.name);
        String name = getIntent().getStringExtra("name");
        tName.setText(name);
        btnFavorites = findViewById(R.id.xdd);
        btnFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = getIntent().getStringExtra("userId");
                Intent intent = new Intent(HomeScreen.this, FavoritesActivityZ.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        searchView = findViewById(R.id.search_book);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchBooksData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Tu możesz obsłużyć zmiany tekstu w SearchView
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    private void fetchBooksData(String query) {
        String finalQuery = prepareQuery(query);
        BookService bookService = RetrofitInstance.getRetrofitInstance().create(BookService.class);

        Call<BookContainer> booksApiCall = bookService.findBooks(finalQuery);

        booksApiCall.enqueue(new Callback<BookContainer>() {
            @Override
            public void onResponse(@NonNull Call<BookContainer> call, @NonNull Response<BookContainer> response) {
                if(response.body() != null)
                    setupBookListView(response.body().getBookList());
            }

            @Override
            public void onFailure(@NonNull Call<BookContainer> call, @NonNull Throwable t) {
                Snackbar.make(findViewById(R.id.drawer_layout),
                        getString(R.string.book_download_failure),
                        BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }

    private String prepareQuery(String query) {
        String[] queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);
    }

    private void setupBookListView(List<Book> books) {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final BookAdapter adapter = new BookAdapter();
        adapter.setBooks(books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public boolean checkNullOrEmpty(String text) {
        return text != null && !TextUtils.isEmpty(text);
    }

    private class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView bookTitleTextView;
        private TextView bookAuthorTextView;
        private TextView numberOfPagesTextView;
        private ImageView bookCover;
        private Book book;
        private Button btnAddToFavorites;

        public BookHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.book_list_item, parent, false));
            itemView.setOnClickListener(this);

            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookAuthorTextView = itemView.findViewById(R.id.book_author);
            numberOfPagesTextView = itemView.findViewById(R.id.number_of_pages);
            bookCover = itemView.findViewById(R.id.img_cover);
            btnAddToFavorites = itemView.findViewById(R.id.btn_add_to_favorites);
            btnAddToFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obsługa dodawania do ulubionych
                    addToFavorites(book);
                }
            });

        }

        private void addToFavorites(Book book) {
            UserDatabase userDatabase = UserDatabase.getUserDatabase(itemView.getContext());
            final UserDao userDao = userDatabase.userDao();

            // Assuming you have the userId from the logged-in user
            String userId = getIntent().getStringExtra("userId"); // Replace with the actual userId

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Check if the book is already in favorites
                    if (userDao.isBookInFavorites(userId, book.getTitle()) == null) {
                        // If not, add the book to favorites
                        FavoriteBook favoriteBook = new FavoriteBook();
                        favoriteBook.setUserId(userId);
                        favoriteBook.setBookTitle(book.getTitle());

                        userDao.addToFavorites(favoriteBook);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(itemView.getContext(), "Book added to favorites", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(itemView.getContext(), "Book is already in favorites", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }

        public void bind(Book book) {
            if (book != null && checkNullOrEmpty(book.getTitle()) && book.getAuthors() != null) {
                this.book = book;
                bookTitleTextView.setText(book.getTitle());
                bookAuthorTextView.setText(TextUtils.join(", ", book.getAuthors()));
                String numberOfPages = getString(R.string.number_of_pages, book.getNumberOfPages());
                numberOfPagesTextView.setText(numberOfPages);
                if (book.getCover() != null)
                    Picasso.with(itemView.getContext())
                            .load(IMAGE_URL_BASE + book.getCover() + "-S.jpg")
                            .placeholder(R.drawable.baseline_menu_book_24)
                            .into(bookCover);
                else
                    bookCover.setImageResource(R.drawable.baseline_menu_book_24);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HomeScreen.this, BookDetails.class);
            intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_TITLE, bookTitleTextView.getText());
            intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_AUTHOR, bookAuthorTextView.getText());
            intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_NUMBER_OF_PAGES, numberOfPagesTextView.getText());
            intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_COVER_ID, book.getCover());
            startActivity(intent);
        }
    }


    private class BookAdapter extends RecyclerView.Adapter<BookHolder> {
        private List<Book> books;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            if (books != null) {
                Book book = books.get(position);
                holder.bind(book);
            }
            else
                Log.d("MainActivity", "No books");
        }

        @Override
        public int getItemCount() {
            if (books != null)
                return books.size();
            return 0;
        }

        @SuppressLint("NotifyDataSetChanged")
        void setBooks(List<Book> books) {
            this.books = books;
            notifyDataSetChanged();
        }
    }
}