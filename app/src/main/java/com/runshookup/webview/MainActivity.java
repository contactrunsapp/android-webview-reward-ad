package com.runshookup.webview;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private RewardedAd rewardedAd;
    private final String REWARD_AD_UNIT_ID = "ca-app-pub-7103278164868369/8435726584";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JSInterface(), "Android");

        // Load your swipe page (HTTPS required for cookies/session)
        webView.loadUrl("https://RunsHookup.online/swipe.php");

        MobileAds.initialize(this, initializationStatus -> loadRewardAd());
    }

    private void loadRewardAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, REWARD_AD_UNIT_ID, adRequest, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd ad) {
                rewardedAd = ad;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                rewardedAd = null;
            }
        });
    }

    private class JSInterface {
        @JavascriptInterface
        public void showRewardAd() {
            runOnUiThread(() -> {
                if (rewardedAd != null) {
                    rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            loadRewardAd();
                        }
                    });
                    rewardedAd.show(MainActivity.this, rewardItem -> {
                        giveReward();
                    });
                } else {
                    // Try to load and notify page to retry later
                    loadRewardAd();
                    runOnUiThread(() -> webView.post(() -> webView.evaluateJavascript("window.dispatchEvent(new CustomEvent('reward_ad_unavailable'))", null)));
                }
            });
        }
    }

    private void giveReward() {
        // After ad completes, call reward_coin.php in the WebView context so session cookie is used
        runOnUiThread(() -> webView.evaluateJavascript(
                "fetch('https://RunsHookup.online/reward_coin.php', {credentials: 'include'})" +
                        ".then(res => res.json())" +
                        ".then(data => {" +
                        "if(data.success){" +
                        "var cc = document.getElementById('coin-count'); if(cc) cc.innerText = data.coins;" +
                        "var cur = document.getElementById('currentCoin'); if(cur) cur.innerText = data.coins;" +
                        "}" +
                        "})", null));
    }
}
