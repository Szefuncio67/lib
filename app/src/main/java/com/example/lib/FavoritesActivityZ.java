package com.example.lib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

public class FavoritesActivityZ extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FavoriteBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_z);

        Toolbar toolbar = findViewById(R.id.toolbarz);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.recyclerViewFavorites);
        CheckBox unreadCheckbox = findViewById(R.id.checkbox_unread);
        CheckBox inProgressCheckbox = findViewById(R.id.checkbox_in_progress);
        CheckBox readCheckbox = findViewById(R.id.checkbox_read);

        unreadCheckbox.setChecked(true);
        inProgressCheckbox.setChecked(true);
        readCheckbox.setChecked(true);



        adapter = new FavoriteBookAdapter();
        adapter.setFilters(unreadCheckbox.isChecked(), inProgressCheckbox.isChecked(), readCheckbox.isChecked());
        adapter.setOnDeleteClickListener(new FavoriteBookAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                // Obsługa kliknięcia przycisku delete
                deleteFavoriteBook(position);
            }
        });

        adapter.setOnClickListener(new FavoriteBookAdapter.OnClickListener() {
            @Override
            public void onItemClick(FavoriteBook favoriteBook) {
                // Launch BookDetails activity with book details
                Intent intent = new Intent(FavoritesActivityZ.this, BookDetails.class);
                intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_TITLE, favoriteBook.getBookTitle());
                intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_AUTHOR, favoriteBook.getBookAuthor());
                intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_NUMBER_OF_PAGES, favoriteBook.getNumberOfPages());
                intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_COVER_ID, favoriteBook.getCover());
                intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_FIRST_PUBLISH_YEAR, favoriteBook.getFirstPublishYear());
                intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_FIRST_SENTENCE, favoriteBook.getFirstSentence());
                intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_LANGUAGE, favoriteBook.getLanguage());
                // Add other book details here
                startActivity(intent);
            }
        });



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        loadFavoriteBooks();

        unreadCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.setFilters(isChecked, inProgressCheckbox.isChecked(), readCheckbox.isChecked());
            }
        });

        inProgressCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.setFilters(unreadCheckbox.isChecked(), isChecked, readCheckbox.isChecked());
            }
        });

        readCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.setFilters(unreadCheckbox.isChecked(), inProgressCheckbox.isChecked(), isChecked);
            }
        });
    }



    private Handler handler = new Handler(Looper.getMainLooper());

    private void deleteFavoriteBook(int position) {
        if (position >= 0 && position < adapter.getItemCount()) {
            FavoriteBook favoriteBook = adapter.getFavoriteBooks().get(position);
            UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
            UserDao userDao = userDatabase.userDao();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    userDao.deleteFavoriteBook(favoriteBook);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.removeFavoriteBook(position);

                            // Refresh the list after removal
                            loadFavoriteBooks();
                        }
                    });
                }
            }).start();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.home){
            finish();
            return true;
        } else {

            return super.onOptionsItemSelected(item);
        }
    }

    private void loadFavoriteBooks() {
        UserDatabase userDatabase = UserDatabase.getUserDatabase(getApplicationContext());
        UserDao userDao = userDatabase.userDao();

        // Assuming you have the userId from the logged-in user
        String userId = getIntent().getStringExtra("userId");

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FavoriteBook> favoriteBooks = userDao.getFavoriteBooks(userId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setFavoriteBooks(favoriteBooks);
                    }
                });
            }
        }).start();
    }
}
