package com.example.lib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BookDetails extends AppCompatActivity {
    public static final String EXTRA_BOOK_DETAILS_TITLE = "com.example.BOOK_DETAILS_TITLE";
    public static final String EXTRA_BOOK_DETAILS_AUTHOR = "com.example.BOOK_DETAILS_AUTHOR";
    public static final String EXTRA_BOOK_DETAILS_NUMBER_OF_PAGES = "com.example.BOOK_DETAILS_NUMBER_OF_PAGES";
    public static final String EXTRA_BOOK_DETAILS_COVER_ID = "com.example.BOOK_DETAILS_COVER_ID";
    public static final String EXTRA_BOOK_DETAILS_FIRST_PUBLISH_YEAR = "com.example.BOOK_DETAILS_FIRST_PUBLISH_YEAR";
    public static final String EXTRA_BOOK_DETAILS_FIRST_SENTENCE = "com.example.BOOK_DETAILS_FIRST_SENTENCE";
    public static final String EXTRA_BOOK_DETAILS_LANGUAGE = "com.example.BOOK_DETAILS_LANGUAGE";
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView numberOfPagesTextView;
    private ImageView cover;
    private TextView firstPublishYearTextView;
    private TextView firstSentenceTextView;
    private TextView languageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        titleTextView = findViewById(R.id.book_title);
        authorTextView = findViewById(R.id.book_author);
        numberOfPagesTextView = findViewById(R.id.number_of_pages);
        cover = findViewById(R.id.img_cover);
        firstPublishYearTextView = findViewById(R.id.first_publish_year);
        firstSentenceTextView = findViewById(R.id.first_sentence);
        languageTextView = findViewById(R.id.language);


        Intent starter = getIntent();
        String firstPublishYearText = getString(R.string.first_publish_year, starter.getStringExtra(EXTRA_BOOK_DETAILS_FIRST_PUBLISH_YEAR));
        String firstSentenceText = getString(R.string.first_sentence, starter.getStringExtra(EXTRA_BOOK_DETAILS_FIRST_SENTENCE));
        String languageText = getString(R.string.language, starter.getStringExtra(EXTRA_BOOK_DETAILS_LANGUAGE));
        titleTextView.setText(starter.getStringExtra(EXTRA_BOOK_DETAILS_TITLE));
        authorTextView.setText(starter.getStringExtra(EXTRA_BOOK_DETAILS_AUTHOR));
        numberOfPagesTextView.setText(starter.getStringExtra(EXTRA_BOOK_DETAILS_NUMBER_OF_PAGES));
        firstPublishYearTextView.setText(firstPublishYearText);
        firstSentenceTextView.setText(firstSentenceText);
        languageTextView.setText(languageText);
        String coverId = starter.getStringExtra(EXTRA_BOOK_DETAILS_COVER_ID);
        if (coverId != null)
            Picasso.with(this)
                    .load(HomeScreen.IMAGE_URL_BASE + coverId + "-L.jpg")
                    .placeholder(R.drawable.baseline_menu_book_24)
                    .into(cover);
        else
            cover.setImageResource(R.drawable.baseline_menu_book_24);
    }
}
