package com.video.videomaster.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.video.videomaster.ClipboardService;
import com.video.videomaster.R;
import com.video.videomaster.utils.Utils;

import java.util.List;

import uk.breedrapps.vimeoextractor.OnVimeoExtractionListener;
import uk.breedrapps.vimeoextractor.VimeoExtractor;
import uk.breedrapps.vimeoextractor.VimeoVideo;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private WebView webView;
    private TextView playTheVideoTextView;
    private EditText instagramEditText;
    public static String URL = "url";
    public static String VIMEO = "https://vimeo.com";
    public static String FACEBOOK = "https://m.facebook.com";
    public static String Vine = "https://vimeo.com";
    public static String INSTAGRAM = "https://www.instagram.com/";
    public static String VUCLIP = "http://vuclip.com/";
    public static String YOUTUBE = "https://www.youtube.com/";
    public static String DAILYMOTION = "http://www.dailymotion.com/";
    public static String SPOTIFY = "https://www.spotify.com/";

    @Override
    protected void onDestroy() {
        webView.loadUrl(null);
        webView.destroy();
        super.onDestroy();
    }

    private Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Intent intent = getIntent();
        setViews();
        boolean fromNotification = intent.getBooleanExtra(ClipboardService.FROM_NOTIFICATION, false);
        String urlToLaunch = intent.getStringExtra(URL);
        switch (urlToLaunch) {
            case "https://vimeo.com":
                webView.loadUrl(urlToLaunch);
                webView.setWebViewClient(new MyWebClient());
                break;
            case "https://m.facebook.com":
                btnDownload.setVisibility(View.GONE);
                webView.loadUrl(urlToLaunch);


                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onLoadResource(WebView view, String url) {
                        Log.v("ZAQ", url);
                        if (url.contains("video-lhr3")) {
                            //Log.d("onLoadResource", url);
                            Utils.downloadFile(getApplicationContext(), url, "facebook" + ".mp4");
                        }
                        super.onLoadResource(view, url);
                    }
                });
                break;
            case "https://www.instagram.com/":
                instagramEditText.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) webView.getLayoutParams();
                //params.setMargins(0,200,0,0);
                btnDownload.setVisibility(View.GONE);
                if (fromNotification){
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

                    webView.loadUrl((String) item.getText());
                }else {
                    webView.loadUrl(urlToLaunch);
                }
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onLoadResource(WebView view, String url) {
                        Log.d("ZAQ", url);
                        if (url.contains(".mp4")) {
                            Utils.downloadFile(getApplicationContext(), url, "Instagram" + ".mp4");
                        }
                        super.onLoadResource(view, url);
                    }
                });
                break;
            case "http://vuclip.com/":
                btnDownload.setVisibility(View.GONE);
                webView.loadUrl(urlToLaunch);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onLoadResource(WebView view, String url) {
                        Log.v("ZAQ", url);
                        if (url.contains(".3gp")) {
                            Utils.downloadFile(getApplicationContext(), url, "Vu Clip" + ".mp4");
                        }
                        super.onLoadResource(view, url);
                    }
                });
                break;
            case "https://www.youtube.com/":
                btnDownload.setVisibility(View.GONE);
                playTheVideoTextView.setVisibility(View.GONE);
                webView.loadUrl(urlToLaunch);
                break;
            case "http://www.dailymotion.com/":
                btnDownload.setVisibility(View.GONE);
                playTheVideoTextView.setVisibility(View.GONE);
                webView.loadUrl(urlToLaunch);
                break;
            case "https://www.spotify.com/":
                btnDownload.setVisibility(View.GONE);
                playTheVideoTextView.setVisibility(View.GONE);
                webView.loadUrl(urlToLaunch);
                break;
        }
    }

    private void setViews() {
        webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setInitialScale(1);
        webView.getSettings().setUseWideViewPort(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setBuiltInZoomControls(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                settings.setMediaPlaybackRequiresUserGesture(true);
            }
        }
        settings.setDomStorageEnabled(true);
        settings.setSaveFormData(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLoadWithOverviewMode(true);

        playTheVideoTextView = (TextView)findViewById(R.id.textView);
        btnDownload = (Button) findViewById(R.id.buttonDownload);
        btnDownload.setOnClickListener(this);
        instagramEditText = (EditText)findViewById(R.id.instagramEditText);
        instagramEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                webView.loadUrl(instagramEditText.getText().toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        VimeoVideo video = (VimeoVideo) view.getTag();
        String[] arrayKeys = video.getStreams().keySet().toArray(new String[video.getStreams().keySet().size()]);
        String[] arrayValues = video.getStreams().values().toArray(new String[video.getStreams().keySet().size()]);

        Intent intent = new Intent(this, DownloadDialogActivity.class);
        intent.putExtra(DownloadDialogActivity.EXTRA_VIDEOS_STREAMS_KEYS, arrayKeys);
        intent.putExtra(DownloadDialogActivity.EXTRA_VIDEOS_STREAMS_LINKS, arrayValues);

        intent.putExtra(DownloadDialogActivity.EXTRA_VIDEO_TITLE, video.getTitle().replaceAll("[.]", ""));
        startActivity(intent);
        //Utils.youTubeDownload("",this);
        //Utils.getVideoFromInstagram("https://www.instagram.com/p/BNrzDK8jw_E/","");
    }

    class MyWebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url != null && Uri.parse(url).getLastPathSegment() != null) {
                checkString(Uri.parse(url).getLastPathSegment());
            }
            //downloadEnabled = false;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.v("ZAQ", url);
            if(url.contains("https://player.vimeo.com/log/play")){
                btnDownload.setVisibility(View.GONE);
            }
            List<String> list = Uri.parse(url).getPathSegments();
            int index = list.indexOf("video");
            if (index > 0) {
                checkString(list.get(index - 1));
            } else if (list.size() > 0) {
                checkString(list.get(0));
            }
        }

        private void checkString(String id) {
            if (id.matches("[0-9]{9}")) {
                //active = true;
                //downloadEnabled = false;
                loadVimeoId(id);
            }
        }
    }

    private void loadVimeoId(String id) {
        VimeoExtractor.getInstance().fetchVideoWithIdentifier(id, null, new OnVimeoExtractionListener() {
            @Override
            public void onSuccess(VimeoVideo video) {
                Log.d("TAG", "onSuccess: " + video.getTitle());
                btnDownload.setTag(video);
                btnDownload.post(new Runnable() {
                    @Override
                    public void run() {
                        //downloadEnabled = true;
                        btnDownload.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Video is not available for download", Toast.LENGTH_SHORT).show();
                }
        });
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
