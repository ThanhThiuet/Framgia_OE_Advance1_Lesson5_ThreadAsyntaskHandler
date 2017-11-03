package framgia.uet.nguyenthanhthi.advance1_lesson5_threadasyntaskhandler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class AsynctaskActivity extends AppCompatActivity {

    private TextView mTextTitle;
    private Button mButtonDownload;
    private ProgressBar mProgressBarDownload;
    private ImageView mImageDownload;
    private AsyncTask mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asynctask);
        getSupportActionBar().setTitle("Thread Asynctask Handler");

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item1 = menu.findItem(R.id.menu_item_1);
        MenuItem item2 = menu.findItem(R.id.menu_item_2);
        item1.setTitle("Thread Demo");
        item2.setTitle("Handler Demo");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_item_1:
                intent = new Intent(this, ThreadActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_item_2:
                intent = new Intent(this, HandlerActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mButtonDownload = (Button) findViewById(R.id.button_download);
        mProgressBarDownload = (ProgressBar) findViewById(R.id.progressbar_download);
        mImageDownload = (ImageView) findViewById(R.id.image_download);

        mTextTitle.setText("Download Image with Asynctask");

        mButtonDownload.setOnClickListener(v -> {
            mAsyncTask = new DownloadAsynctask().execute();
            Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show();
            mButtonDownload.setEnabled(false);
        });
    }

    private class DownloadAsynctask extends AsyncTask<Void, String, Bitmap> {

        /**
         * Download file in background thread
         */
        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = null;
            int count;
            try {
                URL url = new URL("http://i.imgur.com/3CBxbOj.jpg");
                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream inputStreamDownload = url.openStream();
                InputStream inputStreamPercentDownload = url.openStream();
                bitmap = BitmapFactory.decodeStream(inputStreamDownload);

                byte[] data = new byte[1024];
                long total = 0;
                int lengthOfFile = connection.getContentLength();

                while ((count = inputStreamPercentDownload.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        /**
         * Update progress bar
         */
        @Override
        protected void onProgressUpdate(String... values) {
            mProgressBarDownload.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageDownload.setImageBitmap(bitmap);
            Toast.makeText(AsynctaskActivity.this, "Download Image Successfully", Toast.LENGTH_SHORT).show();
            mButtonDownload.setEnabled(true);
        }
    }
}
