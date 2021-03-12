package com.example.schedulemanager.Widget;//package com.example.firebase.Widget;
//
//import android.app.PendingIntent;
//import android.appwidget.AppWidgetManager;
//import android.appwidget.AppWidgetProvider;
//import android.content.Context;
//import android.content.Intent;
//import android.widget.RemoteViews;
//
//import com.example.firebase.MainActivity;
//import com.example.firebase.R;
//
//public class WidgetProvider extends AppWidgetProvider {
//
//    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
//        for (int appWidgetId : appWidgetIds) {
//            Intent intent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_main);
//            views.setOnClickPendingIntent(R.id.widget_main_layout_title, pendingIntent);
//            views.setOnClickPendingIntent(R.id.widget_main_layout_rec_view, pendingIntent);
//            appWidgetManager.updateAppWidget(appWidgetId, views);
//        }
//    }
//}
