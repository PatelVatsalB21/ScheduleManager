package com.example.schedulemanager.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.example.schedulemanager.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<TaskSpinnerRowItem> {

    LottieAnimationView lottieAnimationView;

    public SpinnerAdapter(Context context, ArrayList<TaskSpinnerRowItem> TaskCategory) {
        super(context, 0, TaskCategory);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.task_spinner_row_layout, parent, false
            );
        }
        lottieAnimationView = convertView.findViewById(R.id.task_spinner_row_lottie);
        TextView category = convertView.findViewById(R.id.task_spinner_row_category);

        TaskSpinnerRowItem currentItem = getItem(position);
        if (currentItem != null) {
            lottieAnimationView.setAnimation(currentItem.LottieRes);
            category.setText(currentItem.getCategory());
        }
        return convertView;
    }
}

