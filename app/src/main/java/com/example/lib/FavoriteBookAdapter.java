package com.example.lib;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoriteBookAdapter extends RecyclerView.Adapter<FavoriteBookAdapter.FavoriteBookHolder> {
    private List<FavoriteBook> favoriteBooks;

    @NonNull
    @Override
    public FavoriteBookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_book_item, parent, false);
        return new FavoriteBookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteBookHolder holder, int position) {
        FavoriteBook favoriteBook = favoriteBooks.get(position);
        holder.bind(favoriteBook);
    }

    @Override
    public int getItemCount() {
        return favoriteBooks != null ? favoriteBooks.size() : 0;
    }

    public void setFavoriteBooks(List<FavoriteBook> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
        notifyDataSetChanged();
    }

    static class FavoriteBookHolder extends RecyclerView.ViewHolder {
        private TextView bookTitleTextView;

        FavoriteBookHolder(@NonNull View itemView) {
            super(itemView);
            bookTitleTextView = itemView.findViewById(R.id.bookTitleFavorite);
        }

        void bind(FavoriteBook favoriteBook) {
            bookTitleTextView.setText(favoriteBook.getBookTitle());
        }
    }
}

