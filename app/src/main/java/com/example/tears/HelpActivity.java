package com.example.tears;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class HelpActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        textView = findViewById(R.id.authorTextView);
        String authorInfo = "<br><br><a href = 'http://120.78.188.0/'>联系开发者</a><br><br><br>";
        textView.setText(Html.fromHtml(authorInfo));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}