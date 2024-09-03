package `in`.slanglabs.convaai.demo.webview

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import `in`.slanglabs.convaai.copilot.platform.ConvaAICopilot

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var jsInterface: ConvaJavascriptInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jsInterface = ConvaJavascriptInterface(object : JSFunctionListener {
            override fun onCopilotInitialized() {
                runOnUiThread {
                    webView.evaluateJavascript("javascript:onCopilotInitialized()", null);
                }
            }

            override fun onCopilotError(error: String) {
                runOnUiThread {
                    webView.evaluateJavascript("javascript:onCopilotError('$error')", null);
                }
            }

            override fun onCopilotResponse(response: String) {
                runOnUiThread {
                    webView.evaluateJavascript("javascript:onConvaAICopilotResponse('$response')", null);
                }
            }

            override fun onCopilotSuggestion(suggestion: String) {
                runOnUiThread {
                    webView.evaluateJavascript("javascript:onConvaAICopilotSuggestion('$suggestion')", null);
                }
            }
        }, application)
        setupWebView()
        ConvaAICopilot.attach(this)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView = findViewById(R.id.webview)
        webView.getSettings().javaScriptEnabled = true
        webView.addJavascriptInterface(jsInterface, "NativeInterface")
        webView.loadUrl("file:///android_asset/home.html")
    }
}