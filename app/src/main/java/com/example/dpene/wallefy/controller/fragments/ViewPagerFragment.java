package com.example.dpene.wallefy.controller.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dpene.wallefy.R;
import com.example.dpene.wallefy.controller.fragments.interfaces.IPieChartCommunicator;

public class ViewPagerFragment extends Fragment {

    //     Start   Tabbed test ---- >
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;
    //        End Tabbed  <----

    private IPieChartCommunicator parent;

    public ViewPagerFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.parent = (IPieChartCommunicator) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        //     Start   Tabbed test ---- >

//        To work with backstack use getChildFragmentManager instead of getFragmentManager
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        End Tabbed  <----
        return view;
    }

    //     Start   Tabbed test ---- >

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                Fragment fr = new MainInfoFragment();
                Bundle bundle = new Bundle();
//                bundle.putSerializable("user", user);
                fr.setArguments(bundle);
                return fr;
            }
            if (position == 1) {
                Fragment fr = new PieChartFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("accountPosition", parent.getPosition());
                fr.setArguments(bundle);
                return fr;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            To show the labels or not to show
            switch (position) {
                case 0:
                    return "History";
                case 1:
                    return "Chart";
            }
            return null;
        }
    }
//        End Tabbed  <----

}
