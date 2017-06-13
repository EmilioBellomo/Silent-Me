package com.bellomo.emilio.silentme;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SilentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_CHECK = "com.bellomo.emilio.silentme.action.check";
    private static final String ACTION_START = "com.bellomo.emilio.silentme.action.start";
    private static final String ACTION_STOP = "com.bellomo.emilio.silentme.action.stop";

    private static final String EXTRA_PARAM_ID = "com.bellomo.emilio.silentme.extra.id";

    public SilentService() {
        super("SilentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionCheck(Context context) {
        Intent intent = new Intent(context, SilentService.class);
        intent.setAction(ACTION_CHECK);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionStart(Context context, int id) {
        Intent intent = new Intent(context, SilentService.class);
        intent.setAction(ACTION_START);
        intent.putExtra(EXTRA_PARAM_ID, id);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionStop(Context context, int id) {
        Intent intent = new Intent  (context, SilentService.class);
        intent.setAction(ACTION_STOP);
        intent.putExtra(EXTRA_PARAM_ID, id);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CHECK.equals(action)) {
                handleActionCheck();
            } else if (ACTION_START.equals(action)) {
                final int id = Integer.parseInt(intent.getStringExtra(EXTRA_PARAM_ID));
                handleActionStart(id);
            } else if (ACTION_STOP.equals(action)) {
                final int id = Integer.parseInt(intent.getStringExtra(EXTRA_PARAM_ID));
                handleActionStop(id);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionCheck() {

        Cursor cursor = getContentResolver().query(Contract.Silent.CONTENT_URI, new String[]{
                Contract.Silent.COLUMN_ID,
                Contract.Silent.COLUMN_START,
                Contract.Silent.COLUMN_END,
                Contract.Silent.COLUMN_DAY,
                Contract.Silent.COLUMN_REPEAT,
        }, null, null, null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent startActivity = new Intent(SilentService.this, SilentService.class);
                startActivity.setAction(SilentService.ACTION_START);
                startActivity.putExtra(SilentService.EXTRA_PARAM_ID, id);
                startActivity(startActivity);
            }
        }, 2000);


        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionStart(int id) {

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Silent Me")
                .setContentText("Questo è un service ad alta priorità!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        startForeground(1, notification);

        Cursor cursor = getContentResolver().query(Contract.Silent.buildUri(id), new String[]{
                Contract.Silent.COLUMN_END,
                Contract.Silent.COLUMN_DAY,
                Contract.Silent.COLUMN_REPEAT,
        }, null, null, null);

        cursor.moveToPosition(0);

        String end_date = cursor.getString(cursor.getColumnIndex(Contract.Silent.COLUMN_END));

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        formatter.setLenient(false);

        Date endDate = formatter.parse(end_date, 0);
        long alarmTime = endDate.getTime();

        Intent intent = new Intent(getBaseContext(), SilentService.class);
        intent.setAction(ACTION_STOP);
        intent.putExtra(EXTRA_PARAM_ID, id);

        /*
         * chiamando getBroadcast sul PendingIntent, verrà eseguito un BroadcastReceiver,
         * di fatto potremmo anche eseguire un'activity o un service
         */
        PendingIntent pIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pIntent);

        /*
         * questo viene eseguito sul momento
         */
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            alarm.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pIntent);

        /*
         * questo viene eseguito sul momento
         */
        else
            alarm.set(AlarmManager.RTC_WAKEUP, alarmTime, pIntent);

        cursor.close();

        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionStop(int id) {
        stopForeground(true);
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
