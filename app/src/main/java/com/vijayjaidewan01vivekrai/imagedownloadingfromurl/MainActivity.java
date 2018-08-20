package com.vijayjaidewan01vivekrai.imagedownloadingfromurl;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute("https://firebasestorage.googleapis.com/v0/b/clientapptest-ac307.appspot.com/o/Gallery%2Fpexels-photo-1295941.jpeg?alt=media&token=5517c604-a4d8-48a6-8a02-aa4e7b6ec477");

            }
        });


    }

    class DownloadTask extends AsyncTask<String, Integer, String>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Download in Progress!");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(100);
            dialog.setProgress(0);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... voids) {
            String path = voids[0];
            int fileLength = 0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                fileLength = urlConnection.getContentLength();
                File new_folder = new File("sdcard/MeraNaamIshq");
                if (!new_folder.exists()){
                    new_folder.mkdir();
                }

                File input_file = new File(new_folder, "imageName.jpg");
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;
                OutputStream outputStream = new FileOutputStream(input_file);
                while ((count = inputStream.read(data))!=-1){
                    total+=count;
                    outputStream.write(data, 0, count);
                    int progress = (int) total*100/fileLength;
                    publishProgress(progress);
                }
                inputStream.close();
                outputStream.close();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download Started...";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            dialog.hide();
            Toast.makeText(MainActivity.this, aVoid, Toast.LENGTH_SHORT).show();

            String path = "sdcard/MeraNaamIshq/imageName.jpg";
            imageView.setImageDrawable(Drawable.createFromPath(path));
        }




    }
}
