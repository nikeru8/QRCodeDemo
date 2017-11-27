package com.hello.kaiser.qrcodedemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private Activity activity;

    private ProgressDialog loadingDialog;

    //要生成的QRCode內容
    String QRCodeContent = "http://nikeru8.blogspot.tw/2017/04/third-partyxzingcore-qrcodejar.html";
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        //參數
        imageView = (ImageView) findViewById(R.id.imageView);

        //設定loading Dialog
        loadingDialog = new ProgressDialog(activity);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(true);
        loadingDialog.setMessage("loading please wait...");
    }

    public void onbuttonclick(View view) {
        new GetImage().execute(QRCodeContent);
    }


    private class GetImage extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected void onPreExecute() {
            //執行前 設定可以在這邊設定

            loadingDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //執行中 在背景做事情
            int QRCodeWidth = 600;
            int QRCodeHeight = 600;
            //QRCode內容編碼
            Map hints = new EnumMap(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                //ErrorCorrectionLevel容錯率分四級：L(7%) M(15%) Q(25%) H(30%)
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

                //建立QRCode的資料矩陣
                BitMatrix result = writer.encode(params[0], BarcodeFormat.QR_CODE, QRCodeWidth, QRCodeHeight, hints);

                //建立矩陣圖
                Bitmap bitmap = Bitmap.createBitmap(QRCodeWidth, QRCodeHeight, Bitmap.Config.ARGB_4444);
                for (int y = 0; y < QRCodeHeight; y++) {
                    for (int x = 0; x < QRCodeWidth; x++) {
                        bitmap.setPixel(x, y, result.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }

                return bitmap;

            } catch (WriterException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //執行中 可以在這邊告知使用者進度
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            //執行後 完成背景任務
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
            loadingDialog.dismiss();
        }
    }
}
