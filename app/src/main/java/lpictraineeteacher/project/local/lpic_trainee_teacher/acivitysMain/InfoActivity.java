package lpictraineeteacher.project.local.lpic_trainee_teacher.acivitysMain;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import lpictraineeteacher.project.local.lpic_trainee_teacher.R;

public class InfoActivity extends Activity {

    private WebView webView;
    public String fileName = "info.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        webView = (WebView) findViewById(R.id.infoWebView);
        webView.loadUrl("file:///android_asset/" + fileName);
    }
}
