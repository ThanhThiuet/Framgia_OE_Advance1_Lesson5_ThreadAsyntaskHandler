package framgia.uet.nguyenthanhthi.advance1_lesson5_threadasyntaskhandler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class HandlerActivity extends AppCompatActivity {

    private ImageHandler mImageHandler;
    private TextView mTextTitle;
    private Button mButtonDownload;
    private ImageView mImageDownload;
    private ProgressDialog mProgressDialog;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        getSupportActionBar().setTitle("Thread Asynctask Handler");

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item1 = menu.findItem(R.id.menu_item_1);
        MenuItem item2 = menu.findItem(R.id.menu_item_2);
        item1.setTitle("Thread Demo");
        item2.setTitle("Asynctask Demo");
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
                intent = new Intent(this, AsynctaskActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mImageHandler = new ImageHandler(this);

        mTextTitle = (TextView) findViewById(R.id.text_title);
        mButtonDownload = (Button) findViewById(R.id.button_download);
        mImageDownload = (ImageView) findViewById(R.id.image_download);

        mTextTitle.setText("Download Image with Handler");

        mButtonDownload.setOnClickListener(v -> {
            mProgressDialog = ProgressDialog.show(HandlerActivity.this, "Loading...", "Downloading Image");

            Thread thread = new Thread(() -> {
                mBitmap = downloadBitmap("http://i.imgur.com/h5YqScl.jpg");
                mImageHandler.sendEmptyMessage(0);
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

    private static class ImageHandler extends Handler {

        private final WeakReference<HandlerActivity> mWealRef;
        private HandlerActivity mActivity;

        public ImageHandler(HandlerActivity activity) {
            mWealRef = new WeakReference<>(activity);
            mActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            mActivity.mImageDownload.setImageBitmap(mActivity.mBitmap);
            mActivity.mProgressDialog.dismiss();
            Toast.makeText(mActivity, "Download Image Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}
