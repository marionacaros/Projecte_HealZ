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
    }

    private void Button_GM() {
        button = (Button) findViewById(R.id.button_mesure);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Calculs.this, FC_FR.class);
                startActivity(intent);
            }
        });


    }


}
