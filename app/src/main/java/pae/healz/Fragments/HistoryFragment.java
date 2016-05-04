package pae.healz.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pae.healz.Activities.GraphCreator;
import pae.healz.R;
import pae.healz.SQLite.Atribute;
import pae.healz.SQLite.DataSourceDAO;
import pae.healz.SQLite.ModelClassSQL;


public class HistoryFragment extends Fragment {

    //Provisional
    /*
    private float heartRate;//type=0
    private float fatFreeMass;//type=1
    private float bodyWater;//type=2
    private float weight;//type=3
    private float aux; //type=4
    */

    private GraphCreator graph;
    private ModelClassSQL model;
    private DataSourceDAO DB;
    private List<Atribute> atr = null;
    private Atribute atribute;
    private GraphView graphview;

    private Button buttonW, buttonTBW, buttonFFM, buttonFM;
    private int numberpoints=0;
    private int i=0;
    private Context ctx;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        onAttach(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,container, false);

        DB = new DataSourceDAO(getActivity());
        graph = new GraphCreator();

        graphview = (GraphView) view.findViewById(R.id.graphView);

        buttonW = (Button) view.findViewById(R.id.button_weight);//Type 1
        buttonTBW = (Button) view.findViewById(R.id.button_Total_Body_Water);//Type 2
        buttonFFM = (Button) view.findViewById(R.id.buttonFatfreemass);//Type 3
        buttonFM = (Button) view.findViewById(R.id.buttonFatmass);//Type 4


        buttonW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BUTTON WEIGHT
                //Aqui accedim a la base de dades, recuperant Weight i posantho en una grafica
                try {
                    atr = DB.getAtributes(3); //taula body water
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                double[] information = new double[atr.size()];
                for(int i=0; i<atr.size(); i++){
                    information[i]= atr.get(i).getVar();
                }
                int type =1;
                graphview = graph.crearGraficoSimpleLineal(graphview, information, information.length, type);
            }
        });
        buttonTBW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BUTTON WEIGHT
                //Aqui accedim a la base de dades, recuperant Weight i posantho en una grafica
                try {
                    atr = DB.getAtributes(2);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                double[] information = new double[atr.size()];
                for(int i=0; i<atr.size(); i++){
                    information[i]= atr.get(i).getVar();
                }
                int type =2;
                graphview = graph.crearGraficoSimpleLineal(graphview, information, information.length, type);
            }
        });
        buttonFFM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BUTTON WEIGHT
                //Aqui accedim a la base de dades, recuperant Weight i posantho en una grafica
                try {
                    atr = DB.getAtributes(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                double[] information = new double[atr.size()];
                for(int i=0; i<atr.size(); i++){
                    information[i]= atr.get(i).getVar();
                }
                int type =3;
                graphview = graph.crearGraficoSimpleLineal(graphview, information, information.length, type);
            }
        });
        buttonFM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BUTTON WEIGHT
                //Aqui accedim a la base de dades, recuperant Weight i posantho en una grafica
                try {
                    atr = DB.getAtributes(4);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                double[] information = new double[atr.size()];
                for(int i=0; i<atr.size(); i++){
                    information[i]= atr.get(i).getVar();
                }
                int type =4;
                graphview = graph.crearGraficoSimpleLineal(graphview, information, information.length, type);
            }
        });

        return view;
    }






    public void onAttach(Context activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
     /*   try {
            mListener = (FragmentIterationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


/*                ArrayList<Double> info = new ArrayList<Double>();
                info.clear();
                for(Atribute att : atr)
                    info.add((double) att.getVar());
*/


}
