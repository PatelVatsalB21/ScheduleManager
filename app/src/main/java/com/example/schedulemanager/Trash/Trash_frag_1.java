package com.example.schedulemanager.Trash;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulemanager.MainFragments.Fragment_3;
import com.example.schedulemanager.R;

public class Trash_frag_1 extends Fragment {

    public static RecyclerView trash_task_rec_view;
    Context mContext;
    Trash_Task_rec_adapter adapter;
    public static TextView null_view;

    public Trash_frag_1() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.trash_frag_1, container, false);
        if (mContext == null) {
            mContext = container.getContext();
        }
        trash_task_rec_view = view.findViewById(R.id.trash_frag_1_rec_view);
        null_view = view.findViewById(R.id.trash_frag_1_null_View);
        Fragment_3.NotesItemDecoration spacesItemDecoration = new Fragment_3.NotesItemDecoration(
                10);

        adapter = new Trash_Task_rec_adapter(mContext, UtilsArray_Trash.getTrash_tasks());
        trash_task_rec_view.setAdapter(adapter);
        trash_task_rec_view.addItemDecoration(spacesItemDecoration);
        trash_task_rec_view.setLayoutManager(new LinearLayoutManager(mContext));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            trash_task_rec_view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        adapter.notifyDataSetChanged();
        trashTaskNullViewUpdater(adapter);
        return view;
    }

    public static void trashTaskNullViewUpdater(Trash_Task_rec_adapter adapter) {
        if (UtilsArray_Trash.getTrash_tasks() == null
                || UtilsArray_Trash.getTrash_tasks().size() == 0 || adapter.getItemCount() == 0) {
            null_view.setVisibility(View.VISIBLE);
            trash_task_rec_view.setVisibility(View.INVISIBLE);
        } else {
            null_view.setVisibility(View.INVISIBLE);
            trash_task_rec_view.setVisibility(View.VISIBLE);
        }
    }
}
