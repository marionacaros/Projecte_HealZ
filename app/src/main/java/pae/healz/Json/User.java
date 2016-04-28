package pae.healz.Json;

/**
 * Created by Marc on 28/04/2016.
 */
public class User {

    private String name, secondName, age, sex, height, weight;

    public User (String name, String secondName, String age, String sex, String weight, String height){
        this.name=name;
        this.secondName=secondName;
        this.age=age;
        this.sex=sex;
        this.weight=weight;
        this.height=height;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getSecondNameName(){
        return secondName;
    }
    public void setSecondNameName(String secondNamename){
        this.secondName=secondNamename;
    }
    public String getAge(){
        return age;
    }
    public void setAge(String age){
        this.age=age;
    }
    public String getSex(){
        return sex;
    }
    public void setSex(String sex){
        this.sex=sex;
    }
    public String getHeight(){
        return height;
    }
    public void setHeight(String height){
        this.height=height;
    }
    public String getWeight(){
        return weight;
    }
    public void setWeight(String weight){
        this.weight=weight;
    }
}
