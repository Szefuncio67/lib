package com.example.lib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class FavoritesActivityZ extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FavoriteBookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_z);

        recyclerView = findViewById(R.id.recyclerViewFavorites);
        adapter = new FavoriteBookAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        loadFavoriteBooks();
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
