package in.bitstreet.com.itdwallet.view.DashBoard.eventbus;

import org.greenrobot.eventbus.EventBus;
 
public class GlobalBus {
    public static EventBus sBus;
    public static EventBus getBus() {
        if (sBus == null)
            sBus = EventBus.getDefault();
        return sBus;
    }
}