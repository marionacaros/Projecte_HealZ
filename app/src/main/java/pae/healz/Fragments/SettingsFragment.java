package pae.healz.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pae.healz.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    //Algun dia aixo tindra vida


    public SettingsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        onAttach( getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container, false);
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }



    public void onAttach(Context activity) {
        super.onAttach(activity);


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}
