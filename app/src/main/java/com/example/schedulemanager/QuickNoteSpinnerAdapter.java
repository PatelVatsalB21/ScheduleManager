package com.example.schedulemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class QuickNoteSpinnerAdapter extends ArrayAdapter<QuickNoteSpinnerItem> {

public QuickNoteSpinnerAdapter(Context context, ArrayList<QuickNoteSpinnerItem> ColorSet) {
        super(context, 0, ColorSet);
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
        R.layout.quick_note_spinner_item_row_layout, parent, false
        );
        }

        TextView color_name_txt_view = convertView.findViewById(R.id.Quick_Note_Spinner_Item_Row_Layout_Heading);
        ImageView colorDisplay = convertView.findViewById(R.id.Quick_Note_Spinner_Item_Row_Layout_Img_View);

        QuickNoteSpinnerItem currentItem = getItem(position);
        if (currentItem != null) {
                color_name_txt_view.setText(currentItem.getColor_Name());

//                if (position==0) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                colorDisplay.setBackground(getContext().getDrawable(currentItem.getColor_ID()));
//                        }
//                }else{
                        colorDisplay.setBackgroundColor(getContext().getResources().getColor(currentItem.getColor_ID()));
//                }

        }
        return convertView;
        }
        }
