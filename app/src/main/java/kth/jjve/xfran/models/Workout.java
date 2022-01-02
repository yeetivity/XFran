package kth.jjve.xfran.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Workout constructor
 */

public class Workout implements Serializable {

    private String title, tag, type, description;
    private ArrayList<String> details, equipment;
    //Todo: exercise object --> needs exercise constructor

    public Workout(String title){
        this.title = title;
    }

    public Workout(){
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<String> details) {
        this.details = details;
    }

    public ArrayList<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(ArrayList<String> equipment) {
        this.equipment = equipment;
    }
}
