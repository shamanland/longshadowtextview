package com.shamanland.longshadowtextview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        setContentView(R.layout.activity_main);

        LongShadowTextView logo = (LongShadowTextView) findViewById(R.id.logo);
        // NOTE convert 48dp to pixels
        logo.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()));
        logo.setTextColor(Color.YELLOW);
        logo.setShadowColor(Color.DKGRAY);
        logo.setText("Hello World");
    }
}
