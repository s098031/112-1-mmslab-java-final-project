package com.example.a112_1_mmslab_java_final_project;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;

public class GodGame extends AppCompatActivity {

    private WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_god_game);

        requestAppPermissions();
        webView = findViewById(R.id.webView);


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);


        webView.setWebViewClient(new WebViewClient());



        //url="http://112.104.128.194";
        //url="http://112.104.128.194/dashboard/Degrees%20of%20Lewdity/Degrees%20of%20Lewdity.html";
        //url="file:///android_asset/Degrees of Lewdity/Degrees of Lewdity.html";
        url = "file:///android_asset/Degrees%20of%20Lewdity/Degrees%20of%20Lewdity.html";

        webView.loadUrl(url);
        //new NetworkTask().execute("http://112.104.128.194/");
        //new NetworkTask().execute("112.104.128.194:80");
        //new NetworkTask().execute("https://www.youtube.com/watch?v=SAL_htf83XU&ab_channel=%E9%AD%9A%E4%B9%BE");
    }
    private static final int MY_PERMISSIONS_REQUEST = 123;


    private void requestAppPermissions() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                // 列舉你需要的其他權限
        };

        ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // 檢查每一個權限是否都已經授予
                boolean allPermissionsGranted = true;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false;
                        break;
                    }
                }
                if (allPermissionsGranted) {
                    // 所有權限都已經授予，執行相應的操作
                } else {
                    // 用戶拒絕了某些權限請求，可能需要提供相應的提示
                }
                return;
            }
        }
    }



}