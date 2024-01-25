package com.example.lib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
        adapter = new FavoriteBookAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        loadFavoriteBooks();
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
