package pae.healz.SQLite;

/**
 * Created by Marc on 27/04/2016.
 */
public class ModelClassSQL {
    /*This class contains the data we will save in the database and show in the
    user interface*/

    private long id;
    private float valor;
    private long date;
    public int type;

    public ModelClassSQL(int type, long id, float var,long date){
        this.type=type;
        this.id=id;
        this.date=date;
        this.valor=var;
    }
    public Atribute getAtribute(){
      return new Atribute(id, valor, date);
    }
    public long getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

}
