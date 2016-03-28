package pae.healz;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ImageButton button;

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

        button = (ImageButton) view.findViewById(R.id.button_measure);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Calculs.class);
                startActivity(intent);
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
