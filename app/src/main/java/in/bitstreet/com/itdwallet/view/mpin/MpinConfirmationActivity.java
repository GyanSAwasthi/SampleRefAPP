package in.bitstreet.com.itdwallet.view.mpin;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import in.bitstreet.com.itdwallet.R;
import me.philio.pinentry.PinEntryView;

public class MpinConfirmationActivity extends AppCompatActivity {
    private PinEntryView pinEntryView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_active_chat);
        pinEntryView = (PinEntryView) findViewById(R.id.pin_entry_simple);
       /* pinEntryView.setOnPinEnteredListener(new PinEntryView.OnPinEnteredListener() {
            @Override
            public void onPinEntered(String pin) {
                Toast.makeText(this, "Pin entered: " + pin, Toast.LENGTH_LONG).show();
            }
        });*/
    }
}
