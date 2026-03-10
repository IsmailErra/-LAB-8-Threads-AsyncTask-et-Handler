package com.example.lab8_dev;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // UI elements
    private TextView statusText;
    private ProgressBar loadingBar;
    private ImageView imagePreview;

    // Handler used to safely update UI from background threads
    private Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link XML views to Java variables
        statusText = findViewById(R.id.txtStatus);
        loadingBar = findViewById(R.id.progressBar);
        imagePreview = findViewById(R.id.img);

        Button btnLoadImage = findViewById(R.id.btnLoadThread);
        Button btnHeavyCalc = findViewById(R.id.btnCalcAsync);
        Button btnShowToast = findViewById(R.id.btnToast);

        // Handler attached to the main (UI) thread
        uiHandler = new Handler(Looper.getMainLooper());

        // Button that shows a Toast instantly
        btnShowToast.setOnClickListener(v ->
                Toast.makeText(getApplicationContext(),
                        "UI toujours réactive", Toast.LENGTH_SHORT).show()
        );

        // Start image loading using a background thread
        btnLoadImage.setOnClickListener(v -> startImageLoadingThread());

        // Start heavy calculation using AsyncTask
        btnHeavyCalc.setOnClickListener(v -> new HeavyCalculationTask().execute());
    }

    // -----------------------------------------
    // PART 1 : THREAD example
    // -----------------------------------------
    private void startImageLoadingThread() {

        // Update UI before starting work
        loadingBar.setVisibility(View.VISIBLE);
        loadingBar.setProgress(0);
        statusText.setText("Statut : chargement image...");

        // Background thread
        new Thread(() -> {

            // Simulate long work (ex: download)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Decode image from resources
            Bitmap loadedBitmap = BitmapFactory.decodeResource(
                    getResources(),
                    R.drawable.ic_launcher_background
            );

            // Switch back to UI thread to update views
            uiHandler.post(() -> {
                imagePreview.setImageBitmap(loadedBitmap);
                loadingBar.setVisibility(View.INVISIBLE);
                statusText.setText("Statut : image chargée");
            });

        }).start(); // Start thread
    }

    // -----------------------------------------
    // PART 2 : AsyncTask example
    // -----------------------------------------
    private class HeavyCalculationTask extends AsyncTask<Void, Integer, Long> {

        // Runs on UI thread before work starts
        @Override
        protected void onPreExecute() {
            loadingBar.setVisibility(View.VISIBLE);
            loadingBar.setProgress(0);
            statusText.setText("Statut : calcul en cours...");
        }

        // Runs in background thread
        @Override
        protected Long doInBackground(Void... voids) {

            long computationResult = 0;

            for (int progress = 1; progress <= 100; progress++) {

                // Simulate heavy calculation
                for (int k = 0; k < 200000; k++) {
                    computationResult += (progress * k) % 7;
                }

                // Send progress to UI thread
                publishProgress(progress);
            }

            return computationResult;
        }

        // Updates ProgressBar on UI thread
        @Override
        protected void onProgressUpdate(Integer... values) {
            loadingBar.setProgress(values[0]);
        }

        // Runs on UI thread after task finishes
        @Override
        protected void onPostExecute(Long result) {
            loadingBar.setVisibility(View.INVISIBLE);
            statusText.setText("Statut : calcul terminé = " + result);
        }
    }
}