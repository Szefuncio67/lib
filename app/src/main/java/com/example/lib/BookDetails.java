package com.example.lib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BookDetails extends AppCompatActivity implements SensorEventListener {
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
    private SensorManager sensorManager;
    private Sensor lightSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);


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

        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void onSensorChanged(SensorEvent event) {
        // Pobierz wartość natężenia światła
        float lightIntensity = event.values[0];

        // Zmień kolor tła w zależności od natężenia światła
        changeBackgroundColor(lightIntensity);
    }

    // Metoda interfejsu SensorEventListener - nie używana w tym przypadku
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    // Metoda do zmiany koloru tła w zależności od natężenia światła
    private void changeBackgroundColor(float lightIntensity) {
        LinearLayout layout = findViewById(R.id.book_details_layout); // Dodaj ID do layoutu w pliku XML
        TextView titleTextView = findViewById(R.id.book_title);
        TextView authorTextView = findViewById(R.id.book_author);
        TextView numberOfPagesTextView = findViewById(R.id.number_of_pages);
        TextView firstPublishYearTextView = findViewById(R.id.first_publish_year);
        TextView firstSentenceTextView = findViewById(R.id.first_sentence);
        TextView languageTextView = findViewById(R.id.language);
        // Dodaj pozostałe pola TextView

        int textColor;
        int backgroundColor;

        // Przykładowa zmiana koloru tła i tekstu w zależności od natężenia światła
        if (lightIntensity < 10) {
            backgroundColor = Color.BLACK;
            textColor = Color.WHITE;
        } else if (lightIntensity < 100) {
            backgroundColor = Color.DKGRAY;
            textColor = Color.WHITE;
        } else if (lightIntensity < 1000) {
            backgroundColor = Color.GRAY;
            textColor = Color.BLACK;
        } else {
            backgroundColor = Color.WHITE;
            textColor = Color.BLACK;
        }

        // Ustaw kolor tła
        layout.setBackgroundColor(backgroundColor);

        // Ustaw kolor tekstu dla wszystkich pól TextView
        titleTextView.setTextColor(textColor);
        authorTextView.setTextColor(textColor);
        numberOfPagesTextView.setTextColor(textColor);
        firstPublishYearTextView.setTextColor(textColor);
        firstSentenceTextView.setTextColor(textColor);
        languageTextView.setTextColor(textColor);
        // Ustaw kolor tekstu dla pozostałych pól TextView
    }

}
