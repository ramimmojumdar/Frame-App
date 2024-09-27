package com.digitalapp.fathersdayphotoframeimageeditor.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.digitalapp.fathersdayphotoframeimageeditor.Fragment.LandscapeFragment;
import com.digitalapp.fathersdayphotoframeimageeditor.Fragment.PortraitFragment;
import com.digitalapp.fathersdayphotoframeimageeditor.Fragment.SquareFragment;


public class FragAdapter extends FragmentPagerAdapter {

    String[] names = {"Portrait", "Square","Landscape"};


    public FragAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:

                return new PortraitFragment();
            case 1:

                return new SquareFragment();

            case 2:

                return new LandscapeFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return names[position];
    }
}
