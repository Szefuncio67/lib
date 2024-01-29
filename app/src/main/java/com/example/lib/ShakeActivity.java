package com.example.lib;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShakeActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private ShakeDetector shakeDetector;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        shakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                progressBar.setProgress(count * 20);

                if (count >= 5) {
                    fetchRandomBook();


                }
            }
        });

        sensorManager.registerListener(shakeDetector, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
    }

    private void fetchRandomBook() {
        BookService bookService = RetrofitInstance.getRetrofitInstance().create(BookService.class);

        int randomI = new Random().nextInt(10);
        List<String> list_books = new ArrayList<>(Arrays.asList("Dziady", "Harry+Potter", "Wiedźmin", "Pan+Tadeusz", "Lalka", "Krzyżacy", "Ogniem+i+mieczem", "W+pustyni+i+w+puszczy", "Chłopi", "Lalka"));

        Call<BookContainer> allBooksApiCall = bookService.findBooks(list_books.get(randomI));

        allBooksApiCall.enqueue(new Callback<BookContainer>() {
            @Override
            public void onResponse(@NonNull Call<BookContainer> call, @NonNull Response<BookContainer> response) {
                if (response.body() != null) {
                    List<Book> books = response.body().getBookList();
                    if (books != null && !books.isEmpty()) {
                        int randomIndex = new Random().nextInt(books.size());
                        Book randomBook = books.get(randomIndex);
                        showRandomBookDetails(randomBook);
                    } else {
                        Snackbar.make(findViewById(R.id.drawer_layout),
                                "No books found",
                                BaseTransientBottomBar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookContainer> call, @NonNull Throwable t) {
                Snackbar.make(findViewById(R.id.drawer_layout),
                        getString(R.string.book_download_failure),
                        BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }

    private void showRandomBookDetails(Book randomBook) {

        Intent intent = new Intent(this, BookDetails.class);
        intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_TITLE, randomBook.getTitle());
        if(randomBook.getAuthors() != null){
            intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_AUTHOR, TextUtils.join(", ", randomBook.getAuthors()));
        }else {
            intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_AUTHOR, "Unknown");
        }
        String numberOfPages = getString(R.string.number_of_pages, randomBook.getNumberOfPages());
        intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_NUMBER_OF_PAGES, numberOfPages);
        intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_COVER_ID, randomBook.getCover());
        intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_FIRST_PUBLISH_YEAR, randomBook.getFirstPublishYear());
        if(randomBook.getFirstSentence() != null){
            intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_FIRST_SENTENCE, TextUtils.join(", ", randomBook.getFirstSentence()));
        }else {
            intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_FIRST_SENTENCE, "Unknown");
        }
        if(randomBook.getLanguage() != null){
            intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_LANGUAGE, TextUtils.join(", ", randomBook.getLanguage()));
        }else {
            intent.putExtra(BookDetails.EXTRA_BOOK_DETAILS_LANGUAGE, "Unknown");
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(shakeDetector);
    }
}