package com.candraibra.moviecatalog.notif;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.candraibra.moviecatalog.R;
import com.candraibra.moviecatalog.activity.DetailMovieActivity;
import com.candraibra.moviecatalog.model.Movie;
import com.candraibra.moviecatalog.network.MoviesRepository;
import com.candraibra.moviecatalog.network.OnGetReleaseMovie;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.candraibra.moviecatalog.activity.DetailMovieActivity.EXTRA_MOVIE;

public class ReleaseReceiver extends BroadcastReceiver {
    int NOTIFICATION_ID = 3;
    SimpleDateFormat dateFormat;
    MoviesRepository moviesRepository;
    private ArrayList<Movie> movieArrayList = new ArrayList<>();

    @Override
    public void onReceive(final Context context, Intent intent) {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        final String now = dateFormat.format(date);
        getReleaseMovie(context, now, now);
    }


    private void getReleaseMovie(final Context context, String gte, String lte) {
        moviesRepository = MoviesRepository.getInstance();
        moviesRepository.getRelease(gte, lte, new OnGetReleaseMovie() {
            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                String title = movies.get(0).getTitle() + "is Release now!!";
                String message = movies.get(0).getOverview();
                int id = movies.get(0).getId();
                movieArrayList.addAll(movies);
                for (Movie movie : movies) {
                    if (movie.getReleaseDate().equals(gte)) {
                        showNotif(context, title, message, id);

                        Log.d("releaseNotifReminder", "apa ora?" + movies);
                    }
                }
            }

            @Override
            public void onError() {
                String Toast = Integer.toString((R.string.toastmsg));

            }
        });
    }

    public void showNotif(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "channel_02";
        String CHANNEL_NAME = "AlarmManager channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, DetailMovieActivity.class);
        intent.putExtra(EXTRA_MOVIE, movieArrayList.get(0));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.ic_play)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setColorized(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);

            channel.enableVibration(true);
            channel.setLightColor(R.color.colorPrimary);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);


            Objects.requireNonNull(notificationManagerCompat).createNotificationChannel(channel);

        }

        Objects.requireNonNull(notificationManagerCompat).notify(notifId, builder.build());

    }

    public void cancelNotif(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0);
        Objects.requireNonNull(alarmManager).cancel(pendingIntent);
    }

    public void setAlarm(Context context, String type, String time, String message) {
        cancelNotif(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReceiver.class);
        intent.putExtra("message", message);
        intent.putExtra("type", type);
        String[] timeArray = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0);
        Objects.requireNonNull(alarmManager).setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }
}
