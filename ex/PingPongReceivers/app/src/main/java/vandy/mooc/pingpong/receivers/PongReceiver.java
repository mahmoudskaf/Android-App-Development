package vandy.mooc.pingpong.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import vandy.mooc.pingpong.utils.UiUtils;

/**
 * A broadcast receiver that handles "pong" intents.
 */
public class PongReceiver
       extends BroadcastReceiver {
    /**
     * Debugging tag used by the Android logger.
     */
    protected final String TAG =
        getClass().getSimpleName();

    /**
     * Intent sent to the PongReceiver.
     */
    public final static String ACTION_VIEW_PONG =
            "vandy.mooc.action.VIEW_PONG";

    /**
     * Hook method called by the Android ActivityManagerService
     * framework after a broadcast has been sent.
     *
     * @param context The caller's context.
     * @param pong  An intent containing pong data.
     */
    @Override
    public void onReceive(Context context,
                          Intent pong) {
        // Get the count from the PingReceiver.
        Integer count = pong.getIntExtra("COUNT", 0);
        Log.d(TAG, "onReceive() called with count of "
              + count);

        // Inform send that we're "pong'd".
        UiUtils.showToast(context,
                          "Pong " + count);

        // "Go Async"! (must be "final").  It's overkill to use
        // this feature for the PongReceiver - we just do this to
        // show how it works.
        final PendingResult result = goAsync();

        // Start a background thread with a lambda that broadcasts the
        // pong intent (could also use a HandlerThread to amortize
        // thread creation).
        new Thread(() -> {
                // Broadcast a "ping", incrementing the count by one.
                context.sendBroadcast(PingReceiver.makePingIntent(context,
                                                                  count + 1));
                if (result != null)
                    result.finish();
        }).start();
    }

    /**
     * Factory method that makes a "pong" intent with the given @a
     * count as an extra.
     */
    public static Intent makePongIntent(Context context, int count) {
        return new Intent(PongReceiver.ACTION_VIEW_PONG)
                .putExtra("COUNT", count)
                // Limit receivers to components in this app's package.
                .setPackage(context.getPackageName());
    }
}

