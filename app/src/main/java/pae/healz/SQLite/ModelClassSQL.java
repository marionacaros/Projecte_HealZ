package pae.healz.SQLite;

/**
 * Created by Marc on 27/04/2016.
 */
public class ModelClassSQL {
    /*This class contains the data we will save in the database and show in the
    user interface*/

    private long id;
    private float heartRate;//type=0
    private float fatFreeMass;//type=1
    private float bodyWater;//type=2
    private float weight;//type=3
    private long date;
    public int type;

    public ModelClassSQL(int type, long id, float var,long date){
        this.type=type;
        this.id=id;
        this.date=date;
        if(type==0) heartRate=var;
        else if(type==1)fatFreeMass=var;
        else if(type==2)bodyWater=var;
        else weight=var;
    }
    public Atribute getAtribute(){
        Atribute atribut=null;

        if(type==0) atribut=new Atribute(id,heartRate,date);
        else if(type==1)atribut=new Atribute(id,fatFreeMass,date);
        else if(type==2)atribut=new Atribute(id,bodyWater,date);
        else atribut=new Atribute(id,weight,date);
        return atribut;
    }
    public long getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }


    public float getBodyWater() {
        return bodyWater;
    }
    public void setBodyWater(float bodywater){
        this.bodyWater = bodywater;
    }


    public float getFatFreeMass() {
        return fatFreeMass;
    }
    public void setFatFreeMass(float mass){
        this.fatFreeMass=mass;
    }


    public float getHeartRate() {
        return heartRate;
    }
    public void setHeartRate(float Heartrate){
        this.heartRate = Heartrate;
    }


    public float getWeight() {
        return weight;
    }
    public void setWeight(float Weight){
        this.weight = Weight;
    }
}
