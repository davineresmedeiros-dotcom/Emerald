package dev.emerald.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/** Entry point of the launcher. For now it only reports the state of the native core. */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView status = new TextView(this);
        int padding = (int) (24 * getResources().getDisplayMetrics().density);
        status.setPadding(padding, padding, padding, padding);
        status.setTextSize(16);
        status.setFitsSystemWindows(true);

        if (NativeBridge.load()) {
            status.setText(getString(R.string.native_loaded, NativeBridge.getVersion()));
        } else {
            status.setText(getString(R.string.native_failed,
                    String.valueOf(NativeBridge.getLoadError())));
        }

        setContentView(status);
    }
}
