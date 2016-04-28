package pae.healz.Json;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 28/04/2016.
 */
public class ParseJson {


    public List<User> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readUserArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<User> readUserArray(JsonReader reader) throws IOException {
        List<User> recetas = new ArrayList<User>();

        reader.beginArray();
        while (reader.hasNext()) {
            recetas.add(readUser(reader));
        }
        reader.endArray();
        return recetas;
    }

    public User readUser(JsonReader reader) throws IOException {
        String nombre = null;
        String apellido = null;
        String edad = null;
        String sexo = null;
        String peso = null;
        String altura = null;

        reader.beginObject();
        while (reader.hasNext()) {
                nombre = reader.nextString();
                apellido = reader.nextString();
                edad = reader.nextString();
                sexo = reader.nextString();
                peso = reader.nextString();
                altura = reader.nextString();
        }
        reader.endObject();
        return new User(nombre, apellido, edad, sexo, peso, altura);
    }

}
