package com.example.schedulemanager.HomeFrag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulemanager.MainFragments.Fragment_Home;
import com.example.schedulemanager.R;

import java.util.ArrayList;
import java.util.Calendar;

public class All_In_One_Main_Adapter extends RecyclerView.Adapter<All_In_One_Main_Adapter.ViewHolder> {

    ArrayList<Section> sections = new ArrayList<>();
    Context context;
    Calendar calToday = Calendar.getInstance();
    Calendar calTomorrow = Calendar.getInstance();

    public All_In_One_Main_Adapter(Context c, ArrayList<Section>s) {
        context = c;
        sections = s;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.section_row_layout,parent,false);

        if (context==null){
            context = parent.getContext();
        }


        UtilsArray_All.ReloadCategoryItems();
        sections = UtilsArray_All.sectionsArraylist;

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Section section  = sections.get(position);

            String Head = section.getHead();
            ArrayList<All_In_One> items = section.getItems();

        if(items!= null&&items.size()!=0) {
            holder.Heading.setText(Head);

            All_Rec_Adapter adapter = new All_Rec_Adapter(context, items);
            holder.childRecView.setAdapter(adapter);
            holder.childRecView.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        Fragment_Home.allItemsNullViewUpdater(sections);
        return sections.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView Heading;
        RecyclerView childRecView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Heading = itemView.findViewById(R.id.section_row_heading);
            childRecView = itemView.findViewById(R.id.section_row_rec_view);
        }
    }




}
