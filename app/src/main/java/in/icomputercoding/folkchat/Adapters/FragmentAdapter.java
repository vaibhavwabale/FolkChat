package in.icomputercoding.folkchat.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import in.icomputercoding.folkchat.Fragments.NotificationFrag;
import in.icomputercoding.folkchat.Fragments.RequestFragment;


public class FragmentAdapter extends FragmentPagerAdapter {

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NotificationFrag();
        } else if (position == 1) {
            return new RequestFragment();
        }
        return new NotificationFrag();

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position == 0)
        {
            title = "Notification";
        }
        else if(position == 1)
        {
            title = "Requests";
        }
        return title;
    }
}
