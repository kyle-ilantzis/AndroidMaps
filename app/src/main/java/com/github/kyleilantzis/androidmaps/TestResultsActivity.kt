package com.github.kyleilantzis.androidmaps

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import java.io.FileInputStream
import java.io.InputStreamReader

class TestResultsActivity : AppCompatActivity() {

    companion object {
        val FILE_NAME = "test_results.txt"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_test_results)

        val webview = findViewById(R.id.webview) as WebView

        val file = getFileStreamPath(FILE_NAME)

        if (!file.exists()) {
            Snackbar.make(webview, "File $FILE_NAME not found, run tests first.", Snackbar.LENGTH_SHORT).show()
        } else {
            val fileContents = InputStreamReader(FileInputStream(file)).readLines().fold(StringBuilder(), { acc, s -> acc.append(s) }).toString()
            webview.loadData(fileContents, "text/html", "UTF-8")
        }
    }
}
