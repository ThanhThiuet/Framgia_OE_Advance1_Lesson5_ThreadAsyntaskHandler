package framgia.uet.nguyenthanhthi.advance1_lesson5_threadasyntaskhandler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ThreadActivity extends AppCompatActivity {

    private Handler mHandler;
    private TextView mTextTitle;
    private Button mButtonDownload;
    private ImageView mImageDownload;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        getSupportActionBar().setTitle("Thread Asynctask Handler");

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item1 = menu.findItem(R.id.menu_item_1);
        MenuItem item2 = menu.findItem(R.id.menu_item_2);
        item1.setTitle("Asynctask Demo");
        item2.setTitle("Handler Demo");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menu_item_1:
                intent = new Intent(this, AsynctaskActivity.class);
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
        mHandler = new Handler();

        mTextTitle = (TextView) findViewById(R.id.text_title);
        mButtonDownload = (Button) findViewById(R.id.button_download);
        mImageDownload = (ImageView) findViewById(R.id.image_download);

        mTextTitle.setText("Download Image with Thread");

        mButtonDownload.setOnClickListener(v -> {
            mProgressDialog = ProgressDialog.show(ThreadActivity.this, "Loading...", "Downloading Image");

            //define a new Thread with Runnable
            Thread thread = new Thread(() -> {
                Bitmap bitmap = downloadBitmap("http://i.imgur.com/HR5QMOY.jpg");

                mHandler.post(() -> {
                    //update download result (bitmap) for ImageView
                    if (bitmap == null) return;
                    mImageDownload.setImageBitmap(bitmap);
                    mProgressDialog.dismiss();
                    Toast.makeText(this, "Download Image Successfully", Toast.LENGTH_SHORT).show();
                });
            });
            thread.start();
        });
    }

    private Bitmap downloadBitmap(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            return BitmapFactory.decodeStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}
