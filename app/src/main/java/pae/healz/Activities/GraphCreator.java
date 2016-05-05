package pae.healz.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

import pae.healz.UserData.User;

/**
 * Created by Marc on 03/05/2016.
 */
public class GraphCreator extends AppCompatActivity{

    LineGraphSeries<DataPoint> series;


    public GraphCreator(){
    }

    public GraphView crearGraficoSimpleLineal(GraphView graph, double[] data, int numberPoints, int type){
        int number = Math.min(numberPoints, data.length);
        DataPoint[] datapoint = new DataPoint[number];
        double valorRandom =0;

        //Creamos un array de datapoints con la informacion en cada uno de ellos
        for (int i=0; i<datapoint.length; i++) {
            datapoint[i] = new DataPoint(i, data[data.length - datapoint.length + i]);
            valorRandom = data[i];
        }
        //Creamos el LineGraph y le metemos el vector de datapoints
        series = new LineGraphSeries<DataPoint>(datapoint);


        //Caracteristiques gràfic:
        graph.addSeries(series);

        if(type == 1){
            series.setTitle("Weight evolution");
            int valor = (int)valorRandom+20;
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(valor);
            graph.getViewport().setMaxX(data.length);
            graph.getViewport().setMinX(0);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getGridLabelRenderer().setVerticalAxisTitle("Weight (Kilograms)");
            graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(20);
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
            graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        }
        else if (type == 2){
            series.setTitle("Total Body Water (%)");
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(50);
            graph.getViewport().setMaxX(data.length);
            graph.getViewport().setMinX(0);
            graph.getGridLabelRenderer().setVerticalAxisTitle("Percentage");
            graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(20);
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
            graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        }
        else if (type==3){
            series.setTitle("Fat Free Mass (%)");
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(80);
            graph.getViewport().setMaxX(data.length);
            graph.getViewport().setMinX(0);
            graph.getGridLabelRenderer().setVerticalAxisTitle("Percentage");
            graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(20);
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
            graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        }
        else{
            series.setTitle("Fat Mass (%)");
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(80);
            graph.getViewport().setMaxX(data.length);
            graph.getViewport().setMinX(0);
            graph.getGridLabelRenderer().setVerticalAxisTitle("Percentage");
            graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(20);
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.WHITE);
            graph.getGridLabelRenderer().setGridColor(Color.WHITE);
        }


        //Caracteristiques serie:
        series.setColor(Color.RED);
        series.setThickness(10);

        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.TRANSPARENT);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        //graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Temporal   evolution");
        graph.getGridLabelRenderer().setVerticalAxisTitleTextSize(30);
        graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLACK);
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.WHITE);

        //Caracteristiques llegenda
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setTextSize(25);
        graph.getLegendRenderer().setBackgroundColor(Color.argb(150, 50, 0, 0));
        graph.getLegendRenderer().setTextColor(Color.WHITE);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);

        return graph;

    }



}









