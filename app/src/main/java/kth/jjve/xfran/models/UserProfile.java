package kth.jjve.xfran.models;

public class UserProfile {

    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private Double userWeight;
    private Integer userLength;
    private boolean empty;

    public UserProfile(String firstName, String lastName, String email, double weight, int length){
        this.userFirstName = firstName;
        this.userLastName = lastName;
        this.userEmail = email;
        this.userWeight = weight;
        this.userLength = length;
        this.empty = false;
    }

    public UserProfile(){
        this.empty = true;
    }

    public String getFullName(){
        return userFirstName + " " + userLastName;
    }

    public String getFirstName(){
        return userFirstName;
    }

    public String getLastName(){
        return userLastName;
    }

    public String getEmail() {return userEmail;}

    public Double getWeight(){
        return userWeight;
    }

    public Integer getLength(){
        return userLength;
    }

    public boolean checkEmpty(){return empty;}
}
