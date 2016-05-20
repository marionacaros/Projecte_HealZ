package pae.healz.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import pae.healz.Activities.Calculs;
import pae.healz.Activities.Proba_Database;
import pae.healz.R;
import pae.healz.SQLite.Atribute;
import pae.healz.SQLite.DataSourceDAO;
import pae.healz.SQLite.ModelClassSQL;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Button button, buttonToDB;
    private DataSourceDAO BD;
    private List<Atribute> atrWeight = null;
    private List<Atribute> atrBodyWater = null;
    private List<Atribute> atrFatFreeMass = null;
    private List<Atribute> atrFatMass = null;
    private List<Atribute> atrHeartRate = null;
    private Atribute atributWeight=null;
    private Atribute atributBodyWater=null;
    private Atribute atributFatFreeMass=null;
    private Atribute atributFatMass=null;
    private Atribute atributHeartRate=null;

    private TextView weight, bodywater, fatfreemass, fatmass, heartrate;

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

        //Inicializacion edittext
        weight = (TextView) view.findViewById(R.id.text_weight);
        bodywater= (TextView) view.findViewById(R.id.text_BodyWater);
        fatmass = (TextView) view.findViewById(R.id.text_FatMass);
        fatfreemass = (TextView) view.findViewById(R.id.text_FatFreeMass);
        heartrate = (TextView) view.findViewById(R.id.text_HeartRate);

        //Inicialitzacio de tot lo relacionat amb la base de dades:
        BD = new DataSourceDAO(view.getContext());

        //Lectura base de dades per emplenar el recuadre:
        try {
            atrWeight = BD.getAtributes(3);
            atrBodyWater = BD.getAtributes(2);
            atrFatFreeMass = BD.getAtributes(1);
            atrFatMass = BD.getAtributes(4);
            atrHeartRate = BD.getAtributes(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Recogemos el utlimo atributo de cada tabla:
        Iterator it=atrWeight.iterator();
        while(it.hasNext()){
            atributWeight=(Atribute)it.next();
        }
        Iterator it2=atrBodyWater.iterator();
        while(it2.hasNext()){
            atributBodyWater=(Atribute)it2.next();
        }
        Iterator it3=atrFatFreeMass.iterator();
        while(it3.hasNext()){
            atributFatFreeMass=(Atribute)it3.next();
        }
        Iterator it4=atrFatMass.iterator();
        while(it4.hasNext()){
            atributFatMass=(Atribute)it4.next();
        }
        Iterator it5=atrHeartRate.iterator();
        while(it5.hasNext()){
            atributHeartRate=(Atribute)it5.next();
        }

        if(atributWeight == null)weight.setText("No Info");
        else weight.setText(""+atributWeight.getVar()+" Kg");
        if(atributBodyWater==null)bodywater.setText("No Info");
        else bodywater.setText(""+atributBodyWater.getVar()+" %");
        if(atributFatFreeMass==null)fatfreemass.setText("No Info");
        else fatfreemass.setText(""+atributFatFreeMass.getVar()+" %");
        if(atributFatMass==null)fatmass.setText("No Info");
        else fatmass.setText(""+atributFatMass.getVar()+" %");
        //if(atributHeartRate==null)heartrate.setText("No Info");
        //else heartrate.setText(""+atributHeartRate.getVar()+" bpm");

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
       // buttonToDB = (Button) view.findViewById(R.id.button_database);
       /* buttonToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(getActivity(), Proba_Database.class);
                startActivity(intent2);
            }
        });*/
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
