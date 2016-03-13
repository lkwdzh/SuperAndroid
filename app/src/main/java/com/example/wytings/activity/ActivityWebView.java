package com.example.wytings.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by Rex on 2016/3/12.
 * https://github.com/wytings
 */
public class ActivityWebView extends BaseActivity {

    private WebView webView;
    private FutureTask<String> futureTask;
    private ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    protected void initialize() {
        webView = new WebView(this);
        setExtraContent(webView);
        initWebView();
        futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000);
                return "1234567";
            }
        });
    }

    private void initWebView() {
        String htmlString = getFileStringFromAssets("js.html");
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.addJavascriptInterface(new JsObject(), "androidInterface");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
        webView.loadData(htmlString, "text/html", "GBK");
    }

    class JsObject {

        @JavascriptInterface
        public String HtmlcallJava() {
            return "Html call Java";
        }

        @JavascriptInterface
        public String HtmlcallJava2(final String name, final String param) {
            if (param == null) {
                Log.i("wytings", "------param is null---------");
            } else {
                Log.i("wytings", "------param is --------->" + param);
            }

            new AlertDialog.Builder(ActivityWebView.this).setTitle("native dialog").setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:" + name + "('" + "test callback" + "')");
                        }
                    });
                }
            }).show();

            return "Html call Java : " + param;
        }

        @JavascriptInterface
        public void JavacallHtml() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript: showFromHtml()");
                    Toast.makeText(ActivityWebView.this, "clickBtn", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @JavascriptInterface
        public void JavacallHtml2() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript: showFromHtml2('IT-homer blog')");
                    Toast.makeText(ActivityWebView.this, "clickBtn2", Toast.LENGTH_SHORT).show();
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
