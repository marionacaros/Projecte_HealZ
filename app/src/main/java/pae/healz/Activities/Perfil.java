package pae.healz.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pae.healz.UserData.User;
import pae.healz.R;

public class Perfil extends AppCompatActivity {

    private static final String TAG = "Error in writing info";
    private EditText name, secondName, sex, age, weight, height;
    private String nombre, apellido, sexo, edad, peso, altura;
    private Button button;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_perfil);

        name = (EditText) findViewById(R.id.Name);
        secondName = (EditText) findViewById(R.id.Second_Name);
        age = (EditText) findViewById(R.id.Age);
        sex = (EditText) findViewById(R.id.Sex);
        height = (EditText) findViewById(R.id.Height);
        weight = (EditText) findViewById(R.id.Weight);
        button = new Button(getApplicationContext());

        user = new User(getApplicationContext());

        if(user.getname() == null){
            name.setHint("Introduce Name");
        }
        else name.setText(user.getname());

        if(user.getsecondname() == null){
            secondName.setHint("Introduce Last Name");
        }
        else secondName.setText(user.getsecondname());

        if(user.getage() == null){
            age.setHint("16-85 years");
        }
        else age.setText(user.getage());

        if(user.getsex() == null){
            sex.setHint("Man or Woman");
        }
        else sex.setText(user.getsex());

        if(user.getweight() == null){
            weight.setHint("20 - 250 Kg");
        }
        else weight.setText(user.getweight());

        if(user.getheight() == null){
            height.setHint("50 - 250 cm");
        }
        else height.setText(user.getheight());

        button_save();
    }

    private void button_save(){
        button = (Button) findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                name = (EditText) findViewById(R.id.Name);
                secondName = (EditText) findViewById(R.id.Second_Name);
                age = (EditText) findViewById(R.id.Age);
                sex = (EditText) findViewById(R.id.Sex);
                height = (EditText) findViewById(R.id.Height);
                weight = (EditText) findViewById(R.id.Weight);
                button = new Button(getApplicationContext());

                nombre = name.getText().toString();
                apellido = secondName.getText().toString();
                edad = age.getText().toString();
                sexo = sex.getText().toString();
                altura = height.getText().toString();
                peso = weight.getText().toString();

                user.saveUserData(nombre, apellido, edad, sexo, peso, altura);

                CharSequence text = "Information saved perfectly";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        });
    }

}
