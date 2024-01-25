package com.example.lib;

import android.media.Image;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteBookAdapter extends RecyclerView.Adapter<FavoriteBookAdapter.FavoriteBookHolder> {
    public static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";
    private List<FavoriteBook> favoriteBooks;
    private boolean showUnread = true;
    private boolean showInProgress = true;
    private boolean showRead = true;

    public void setFilters(boolean showUnread, boolean showInProgress, boolean showRead) {
        this.showUnread = showUnread;
        this.showInProgress = showInProgress;
        this.showRead = showRead;
        notifyDataSetChanged(); // Wymaga ponownego odświeżenia adaptera po zmianie filtrów
    }

    @NonNull
    @Override
    public FavoriteBookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_book_item, parent, false);
        return new FavoriteBookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteBookHolder holder, int position) {
        FavoriteBook favoriteBook = favoriteBooks.get(position);

        // Sprawdź stan książki i czy powinna być wyświetlana
        if ((showUnread && "Unread".equals(favoriteBook.getState())) ||
                (showInProgress && "In Progress".equals(favoriteBook.getState())) ||
                (showRead && "Read".equals(favoriteBook.getState()))) {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            holder.bind(favoriteBook);
        } else {
            // Ukryj element, jeśli nie spełnia warunków filtrów
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }



    @Override
    public int getItemCount() {
        return favoriteBooks != null ? favoriteBooks.size() : 0;
    }

    public void setFavoriteBooks(List<FavoriteBook> favoriteBooks) {
        this.favoriteBooks = favoriteBooks;
        notifyDataSetChanged();
    }

    class FavoriteBookHolder extends RecyclerView.ViewHolder {
        private TextView bookTitleTextView;
        private TextView bookAuthorTextView;
        private TextView numberOfPagesTextView;
        private ImageView coverImageView;
        private EditText editTextCurrentPage;

        private RadioGroup radioGroup;

        FavoriteBookHolder(@NonNull View itemView) {
            super(itemView);
            radioGroup = itemView.findViewById(R.id.radioGroup_status);
            editTextCurrentPage = itemView.findViewById(R.id.editText_current_page);
            bookTitleTextView = itemView.findViewById(R.id.book_title);
            bookAuthorTextView = itemView.findViewById(R.id.book_author);
            numberOfPagesTextView = itemView.findViewById(R.id.number_of_pages);
            coverImageView = itemView.findViewById(R.id.img_cover);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    RadioButton radioButton = itemView.findViewById(checkedId);
                    if (radioButton != null) {
                        String state = radioButton.getText().toString();
                        editTextCurrentPage.setVisibility(state.equals("In Progress") ? EditText.VISIBLE : EditText.GONE);


                        // Set the state in the corresponding FavoriteBook object
                        int adapterPosition = getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            FavoriteBook favoriteBook = favoriteBooks.get(adapterPosition);
                            favoriteBook.setState(state);
                            editTextCurrentPage.setText(favoriteBook.getActualPage());

                            // Update the state in the database
                            updateFavoriteBookInDatabase(favoriteBook);
                        }
                    }
                }
            });

            editTextCurrentPage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        // Handle the action (e.g., save the entered value to the database)
                        handleEditTextAction();
                        return true;
                    }
                    return false;
                }
            });

        }
        void updateFavoriteBookInDatabase(FavoriteBook favoriteBook) {
            UserDatabase userDatabase = UserDatabase.getUserDatabase(itemView.getContext());
            UserDao userDao = userDatabase.userDao();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    userDao.updateFavoriteBook(favoriteBook);
                }
            }).start();
        }
        void handleEditTextAction() {
            String currentPage = editTextCurrentPage.getText().toString();
            int adapterPosition = getAdapterPosition();

            if (adapterPosition != RecyclerView.NO_POSITION && !currentPage.isEmpty()) {
                FavoriteBook favoriteBook = favoriteBooks.get(adapterPosition);
                favoriteBook.setActualPage(currentPage);

                // Update the actual page in the database
                updateFavoriteBookInDatabase(favoriteBook);
            }
        }

        void bind(FavoriteBook favoriteBook) {
            bookTitleTextView.setText(favoriteBook.getBookTitle());
            bookAuthorTextView.setText(favoriteBook.getBookAuthor());
            numberOfPagesTextView.setText(favoriteBook.getNumberOfPages());
            if (favoriteBook.getCover() != null)
                Picasso.with(itemView.getContext())
                        .load(IMAGE_URL_BASE + favoriteBook.getCover() + "-S.jpg")
                        .placeholder(R.drawable.baseline_menu_book_24)
                        .into(coverImageView);
            else
                coverImageView.setImageResource(R.drawable.baseline_menu_book_24);
            String state = favoriteBook.getState();
            if (state != null) {
                switch (state) {
                    case "Unread":
                        radioGroup.check(R.id.radioButton_unread);
                        break;
                    case "In Progress":
                        radioGroup.check(R.id.radioButton_in_progress);
                        editTextCurrentPage.setText(favoriteBook.getActualPage());
                        break;
                    case "Read":
                        radioGroup.check(R.id.radioButton_read);
                        break;
                }
            }
        }
    }
}

