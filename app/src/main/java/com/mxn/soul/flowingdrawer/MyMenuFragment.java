package com.mxn.soul.flowingdrawer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.heroliu.applications.User;
import com.heroliu.www.shark.R;
import com.mxn.soul.flowingdrawer_core.MenuFragment;
import com.squareup.picasso.Picasso;


public class MyMenuFragment extends MenuFragment {

    private ImageView ivMenuUserProfilePhoto;
    public NavigationView vNavigation;
    private BackListener mListener;
    private TextView tvMenuHeaderUsername;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        ivMenuUserProfilePhoto = (ImageView) view.findViewById(R.id.ivMenuUserProfilePhoto);
        tvMenuHeaderUsername = (TextView) view.findViewById(R.id.tvMenuHeaderUsername);
        vNavigation = (NavigationView) view.findViewById(R.id.vNavigation);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mListener.onBackWork(menuItem.getItemId());
                return false;
            }
        }) ;
        setupHeader();
        return  setupReveal(view) ;
    }

    public interface BackListener{
        public void onBackWork(int id);
    }

    private void setupHeader() {
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.global_menu_avatar_size);
        //String profilePhoto = getResources().getString(R.string.user_profile_photo);
        User user = (User) getActivity().getApplication();
        tvMenuHeaderUsername.setText(user.getUsername());
        String profilePhoto = "http://www.heroliu.com/banner.jpg";
        Picasso.with(getActivity())
                .load(profilePhoto)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivMenuUserProfilePhoto);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (BackListener) activity;
    }
}
