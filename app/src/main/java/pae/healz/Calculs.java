package pae.healz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Calculs extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculs);
        Button_GM();
        Button_FCFR();
        Button_Composition();
    }

    private void Button_GM() {
        button = (Button) findViewById(R.id.button_GM);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calculs.this, FC_FR.class);
                startActivity(intent);
            }
        });


    }

    private void Button_FCFR() {
        button = (Button) findViewById(R.id.button_FCFR);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calculs.this, FC_FR.class);
                startActivity(intent);
            }
        });


    }

    private void Button_Composition() {
        button = (Button) findViewById(R.id.button_C);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calculs.this, Composition.class);
                startActivity(intent);
            }
        });


    }


}
