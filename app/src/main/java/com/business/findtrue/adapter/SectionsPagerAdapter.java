package com.business.findtrue.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.business.findtrue.fragment.ChoosePostFragment;
import com.business.findtrue.fragment.ExperienceDetailFragment;
import com.business.findtrue.fragment.PersonalDetailsFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        // Show 6 total pages.
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
                return personalDetailsFragment;

            case 1:
                ChoosePostFragment choosePostFragment = new ChoosePostFragment();
                return choosePostFragment;

            case 2:
                ExperienceDetailFragment experienceDetailFragment = new ExperienceDetailFragment();
                return experienceDetailFragment;
            default:


        }
        return null;
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return PlaceholderFragment.newInstance(position + 1);
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return "SECTION "+(position+1);
//    }
}
