package com.firebasetest.octagono.firebasetest2.Fragments.MainActivityFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebasetest.octagono.firebasetest2.R;

/**
 * Created by OCTAGONO on 6/21/2017.
 */

public class RegisterFragment extends Fragment {
    public static RegisterFragment newInstance(){
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }
}
