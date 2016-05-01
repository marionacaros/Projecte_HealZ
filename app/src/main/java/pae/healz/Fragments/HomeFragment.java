package pae.healz.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pae.healz.Activities.Calculs;
import pae.healz.Activities.Proba_Database;
import pae.healz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Button button, buttonToDB;

    public HomeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_home,container, false);
        //Enllaç button_calculs:

        button = (Button) view.findViewById(R.id.button_C);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Calculs.class);
                startActivity(intent);
            }
        });

        //Per fer la proba base de dades
        buttonToDB = (Button) view.findViewById(R.id.button_database);
        buttonToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(getActivity(), Proba_Database.class);
                startActivity(intent2);
            }
        });
        return view;
    }



    public void onAttach(Context activity) {
        super.onAttach(activity);


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}
