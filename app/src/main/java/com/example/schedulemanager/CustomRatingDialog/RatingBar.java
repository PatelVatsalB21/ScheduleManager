package com.example.schedulemanager.CustomRatingDialog;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.schedulemanager.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class RatingBar extends BottomSheetDialogFragment implements View.OnClickListener {
    public EditText feedbackDescription;
    public TextView starTextView;
    public ImageView star1, star2, star3, star4, star5;
    public Button submit, later;
    public static SharedPreferences FirstUseSharedPreferences;
    final private static String SESSION_COUNT_DB = "Session_count_DB";
    public Context context;
    Integer SessionCount = 0;
    private boolean doubleBackToExitPressedOnce = false;

    public RatingBar(Context c) {
        context = c;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_rating_dialog, container, false);
        feedbackDescription = view.findViewById(R.id.custom_rating_dialog_feedback_comment);
        starTextView = view.findViewById(R.id.custom_rating_dialog_star_description);
        star1 = view.findViewById(R.id.custom_dialog_star_1);
        star2 = view.findViewById(R.id.custom_dialog_star_2);
        star3 = view.findViewById(R.id.custom_dialog_star_3);
        star4 = view.findViewById(R.id.custom_dialog_star_4);
        star5 = view.findViewById(R.id.custom_dialog_star_5);
        submit = view.findViewById(R.id.custom_dialog_submit_btn);
        later = view.findViewById(R.id.custom_dialog_remind_later_btn);
        FirstUseSharedPreferences = context.getSharedPreferences("First.db", MODE_PRIVATE);
        SessionCount = FirstUseSharedPreferences.getInt(SESSION_COUNT_DB, 0);
        star1.setOnClickListener(this);
        star2.setOnClickListener(this);
        star3.setOnClickListener(this);
        star4.setOnClickListener(this);
        star5.setOnClickListener(this);
        submit.setOnClickListener(this);
        later.setOnClickListener(this);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.custom_dialog_star_1) {
            star1.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star2.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_empty_48dp));
            star3.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_empty_48dp));
            star4.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_empty_48dp));
            star5.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_empty_48dp));
            starTextView.setText("Not Satisfied");
        } else if (view.getId() == R.id.custom_dialog_star_2) {
            star1.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star2.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star3.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_empty_48dp));
            star4.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_empty_48dp));
            star5.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_empty_48dp));
            starTextView.setText("Needs Improvement");
        } else if (view.getId() == R.id.custom_dialog_star_3) {
            star1.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star2.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star3.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star4.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_empty_48dp));
            star5.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_empty_48dp));
            starTextView.setText("Quiet Ok");
        } else if (view.getId() == R.id.custom_dialog_star_4) {
            star1.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star2.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star3.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star4.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star5.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_empty_48dp));
            starTextView.setText("Very Good");
        } else if (view.getId() == R.id.custom_dialog_star_5) {
            star1.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star2.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star3.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star4.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            star5.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_star_full_48dp));
            starTextView.setText("Superb!");
        } else if (view.getId() == R.id.custom_dialog_remind_later_btn) {
            dismiss();
        } else if (view.getId() == R.id.custom_dialog_submit_btn) {
            if (!feedbackDescription.getText().toString().isEmpty()) {
                submitReview(context);
                dismiss();
            } else {
                Toast.makeText(context,
                        "Please tell us something about your experience with Schedule Manager",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void submitReview(Context context) {
        final Uri marketUri = Uri.parse("market://details?id=swati4star.createpdf");
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
        } catch (android.content.ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                    "https://play.google.com/store/apps/details?id=swati4star.createpdf"));
            context.startActivity(intent);
            Toast.makeText(context, "Couldn't find PlayStore on this device",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void dismiss() {
        if (doubleBackToExitPressedOnce) {
            super.dismiss();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(context, "Press again to dismiss Rate us dialog", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }
}
