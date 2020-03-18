package in.bitstreet.com.itdwallet.view.DashBoard.fragmentadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import in.bitstreet.com.itdwallet.view.DashBoard.fragments.ReceiveFragment;
import in.bitstreet.com.itdwallet.view.DashBoard.fragments.SendFragment;
import in.bitstreet.com.itdwallet.view.DashBoard.fragments.TransactionFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TransactionFragment tab1 = new TransactionFragment();
                return tab1;
            case 1:
                SendFragment tab2 = new SendFragment();
                return tab2;
            case 2:
                ReceiveFragment tab3 = new ReceiveFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}