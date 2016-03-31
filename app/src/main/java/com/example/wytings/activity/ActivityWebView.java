package com.example.wytings.activity;

import android.app.AlertDialog;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public class ActivityWebView extends BaseActivity {

    private WebView webView;

    @Override
    protected void initialize() {
        webView = new WebView(this);
        setExtraContent(webView);
        initWebView();
    }

    private void initWebView() {
        String htmlString = getFileStringFromAssets("js.html");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.addJavascriptInterface(new JavaScriptObject(), "AndroidInterface");
        webView.loadData(htmlString, "text/html", "GBK");
    }

    class JavaScriptObject {

        @JavascriptInterface
        public String HtmlCallJava(String param) {

            new AlertDialog.Builder(ActivityWebView.this).setTitle("native dialog").setMessage(param).setPositiveButton("confirm", null).show();

            return "reply from java -> native function is called";
        }

        @JavascriptInterface
        public void JavaCallHtml() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String param = " < this string is from java >";
                    webView.loadUrl("javascript: javaCallHtml('" + param + "')");
                    Toast.makeText(ActivityWebView.this, "Java call Html", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public String getFileStringFromAssets(String fileName) {
        String Result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null)
                Result += line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result;
    }
}
