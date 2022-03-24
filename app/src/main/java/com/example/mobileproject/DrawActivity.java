package com.example.mobileproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;

import com.example.mobileproject.constants.GlobalConstants;
import com.example.mobileproject.draw_config.Config;
import com.example.mobileproject.models.Player;
import com.example.mobileproject.models.RoomState;
import com.example.mobileproject.utils.CloudFirestore;

public class DrawActivity extends Activity {
    private ImageView i;
    private int thickness = 10;
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
    private RoomState roomState;
    private ImageButton btnClose;
    private ImageButton btnHint;

    TextView txtVocab;

    int timeout;
    String roomID;
    String vocab;
    public void notiDraw(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();

        //this is custom dialog
        //custom_popup_dialog contains textview only
        View customView = layoutInflater.inflate(R.layout.popup_notidraw, null);
        // reference the textview of custom_popup_dialog
        Button buttonDrawTurn = customView.findViewById(R.id.buttonDrawTurn);



        builder.setView(customView);
        AlertDialog alert = builder.create();
        buttonDrawTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawscreen);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // tell that it's time to draw
        notiDraw();

        timeout = bundle.getInt("Timeout", GlobalConstants.TIME_FOR_A_GAME);
        roomID = bundle.getString("roomID");
        vocab = bundle.getString("vocab");

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


        // get Room Information and update room state
        roomState = new RoomState(roomID, timeout,
                CloudFirestore.encodeBitmap(b), vocab);
        CloudFirestore.sendData("RoomState", roomState.getRoomID(), roomState);

        txtVocab = (TextView) findViewById(R.id.txtVocab);
        txtVocab.setText(roomState.getVocab());


        btnHint = (ImageButton) findViewById(R.id.btnHint);
        btnHint.setOnClickListener(view -> {
            roomState.nextHint();
            roomState.setShowHint(true);

            Thread updateThread = new Thread(() -> {
                CloudFirestore.updateField("RoomState", roomState.getRoomID(), "hint", roomState.getHint());
                CloudFirestore.updateField("RoomState", roomState.getRoomID(), "showHint", true);
            });
            updateThread.start();
       });

        btnClose = (ImageButton) findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog exitDialog = new AlertDialog.Builder(DrawActivity.this)
                        .setTitle("Exit the game")
                        .setMessage("Are you sure want to quit this game?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // remove player from database ?

                                Intent intent = new Intent(DrawActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .setIcon(R.drawable.ic_baseline_sad_face_24)
                        .show(); // do nothing
            }
        });

        Thread killActivity = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < timeout;i++){
                    try {
                        Thread.sleep(1000);
                        roomState.setTimeLeft(timeout-i-1);
                        Thread updateThread = new Thread(() -> {
                            CloudFirestore.updateField("RoomState", roomState.getRoomID(),
                                    "timeLeft", roomState.getTimeLeft());
                        });
                        updateThread.start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(DrawActivity.this, GameplayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        killActivity.start();
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
        synchronized (new Object()){
            if (returnShape != null) {
                returnShape.interrupt();
            }
            switch(penMode) {
                case DRAW:
                    p.setColor(PaintColor);
                    break;
                case ERASE:
                    p.setColor(Color.WHITE);
                    break;
                case ORANGE:
                    p.setColor(Color.rgb(255,165,0));
                    break;
                case RED:
                    p.setColor(Color.RED);
                    break;
                case YELLOW:
                    p.setColor(Color.YELLOW);
                    break;
                case GREEN:
                    p.setColor(Color.GREEN);
                case BLUE:
                    p.setColor(Color.BLUE);
                case PURPLE:
                    p.setColor(Color.rgb(128,0,128));
                    break;
                case GRAY:
                    p.setColor(Color.GRAY);
                    break;
                case BROWN:
                    p.setColor(Color.rgb(148,83,5));
                    break;
            }
            if (prevX != 0) {
                p.setStrokeWidth((float) thickness);
                c.drawLine((float) prevX, (float) prevY, e.getX(), e.getY() - Config.offset, p);
            }
            prevX = (int) e.getX();
            prevY = (int) (e.getY() - Config.offset);
            System.out.println(e.getX() + "," + e.getY());
            Log.d("Dot", Integer.toString(Config.offset));

            returnShape = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                        prevX = 0;

                        // send current drawing to firebase
                        roomState.setImgBitmap(CloudFirestore.encodeBitmap(b.copy(b.getConfig(), false)));
                        CloudFirestore.updateField("RoomState", roomState.getRoomID(),
                                "imgBitmap",
                                roomState.getImgBitmap());

                    } catch (InterruptedException ignored) {
                    }
                }
            };
            returnShape.start();
        }
        return false;
    }
    public void setEraser(View v){
        penMode = Config.PenType.ERASE;
        thickness = 30;
    }
    public void setDraw(View v){
        penMode = Config.PenType.DRAW;
        thickness = 10;
    }
    public void setColorOrange(View v){
        penMode = Config.PenType.ORANGE;
        thickness = 10;
    }
    public void setColorRed(View v){
        penMode = Config.PenType.RED;
        thickness = 10;
    }
    public void setColorYellow(View v){
        penMode = Config.PenType.YELLOW;
        thickness = 10;
    }
    public void setColorGreen(View v){
        penMode = Config.PenType.GREEN;
        thickness = 10;
    }
    public void setColorBlue(View v){
        penMode = Config.PenType.BLUE;
        thickness = 10;
    }
    public void setColorPurple(View v){
        penMode = Config.PenType.PURPLE;
        thickness = 10;
    }
    public void setColorGray(View v){
        penMode = Config.PenType.GRAY;
        thickness = 10;
    }
    public void setColorBrown(View v){
        penMode = Config.PenType.BROWN;
        thickness = 10;
    }
}
