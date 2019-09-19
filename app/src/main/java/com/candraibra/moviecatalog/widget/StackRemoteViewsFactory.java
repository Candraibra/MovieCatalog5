package com.candraibra.moviecatalog.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.model.Movie;

import java.util.concurrent.ExecutionException;

import static com.candraibra.moviecatalog.database.DbContract.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    int mAppWidgetId;
    private Context context;
    private Cursor cursor;


    public StackRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        int mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }


    @Override
    public void onCreate() {
        cursor = context.getContentResolver().query(
                CONTENT_URI, null, null, null, null
        );
    }

    private Movie getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Error");
        }

        return new Movie(cursor);
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Movie movies = getItem(position);
        String poster = movies.getPosterPathFav();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);

        Bitmap bmp = null;
        try {
            bmp = Glide.with(context)
                    .asBitmap()
                    .load(poster)
                    .apply(new RequestOptions().fitCenter())
                    .submit()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        remoteViews.setImageViewBitmap(R.id.imgWidget, bmp);

        Bundle bundle = new Bundle();
        bundle.putInt(AppsWidget.EXTRA_ITEM, position);
        Intent intent = new Intent();
        intent.putExtras(bundle);

        remoteViews.setOnClickFillInIntent(R.id.imgWidget, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
