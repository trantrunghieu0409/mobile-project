package com.example.mobileproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.Nullable;

import com.example.mobileproject.draw_config.Config;

public class DrawActivity extends Activity {
    private ImageView i;
    private int thickness = 1;
    private SeekBar thick;
    private int PaintColor = Color.argb(0xFF, 0, 0, 0);
    private int prevX = 0;
    private int prevY = 0;
    private Paint p;
    private Canvas c;
    private Bitmap b;
    private View pw;
    private Config.PenType penMode = Config.PenType.DRAW;
    private Thread returnShape = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawscreen);
        i = findViewById(R.id.imageView);
        p = new Paint();
        c = new Canvas();
        p.setStrokeCap(Paint.Cap.ROUND);
        b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        i.post(new Runnable() {
            @Override
            public void run() {
                b = Bitmap.createBitmap(i.getWidth(), i.getHeight(), Bitmap.Config.ARGB_8888);
                c = new Canvas(b);
                i.setImageBitmap(b);
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Config.height = displayMetrics.heightPixels;
        Config.width = displayMetrics.widthPixels;
        Config.offset = Config.height/10 + Config.height/30;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        if (pw != null){
            if (pw.getVisibility() != View.GONE) {
                pw.setVisibility(View.GONE);
            }
        }
        c = new Canvas(b);
        i.setImageBitmap(b);
        switch(e.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                switch(penMode) {
                    case DRAW:
                        synchronized (new Object()){
                            if (returnShape != null) {
                                returnShape.interrupt();
                            }
                            p.setColor(PaintColor);
                            if (prevX != 0) {
                                p.setStrokeWidth((float) thickness);
                                c.drawLine((float) prevX, (float) prevY, e.getX(), e.getY() - Config.offset, p);
                            }
                            prevX = (int) e.getX();
                            prevY = (int) (e.getY() - Config.offset);
                            Log.d("Doot", Integer.toString(Config.offset));

                            returnShape = new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(100);
                                        prevX = 0;
                                    } catch (InterruptedException e) {
                                    }
                                }
                            };
                            returnShape.start();
                        }
                        break;

                }
        }
        return false;
    }
}
