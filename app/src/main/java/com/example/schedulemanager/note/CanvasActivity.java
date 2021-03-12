package com.example.schedulemanager.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.schedulemanager.R;

public class CanvasActivity extends AppCompatActivity {
    RelativeLayout relativeLayout;
    CustomEditText editText;
    EditText editText2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        relativeLayout = findViewById(R.id.canvas);
        editText = new CustomEditText(this);
        relativeLayout.addView(editText);
    }

    public static class MyView extends View {

        Paint mPaint, otherPaint;
        RectF checkbox;
        Drawable tick;

        public MyView(Context context) {
            super(context);

            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(5);

            otherPaint = new Paint();

            DisplayMetrics displayMetrics = new DisplayMetrics();

            ((Activity) getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);


            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;
            tick = getResources().getDrawable(R.drawable.ic_done);
            checkbox= new RectF(5,5, screenWidth/15,screenWidth/15);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawRoundRect(checkbox, 10,10, mPaint);
            mPaint.setColor(getResources().getColor(R.color.colorAccent));
            if (tick!=null) {
                tick.setBounds(0, 0, 64, 64);
                canvas.translate(0, 0);
                tick.draw(canvas);
            } else Toast.makeText(getContext(), "Null Bitmap", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("AppCompatCustomView")
    public class CustomEditText extends EditText {

        Drawable checkedBox, uncheckedBox;

        public CustomEditText(Context context) {
            super(context);

            checkedBox = getResources().getDrawable(R.drawable.ic_checked_checkbox);
            uncheckedBox = getResources().getDrawable(R.drawable.ic_unchecked_checkbox);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Toast.makeText(CanvasActivity.this, "Drawing Started", Toast.LENGTH_SHORT).show();
            checkedBox.setBounds(0, 0, 64, 64);
            canvas.translate(0, 0);
            checkedBox.draw(canvas);
        }
    }
}

