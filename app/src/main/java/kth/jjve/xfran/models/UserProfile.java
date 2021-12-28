package kth.jjve.xfran.models;

public class UserProfile {

    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private Double userWeight;
    private Integer userHeight;
    private boolean empty;

    public UserProfile(String firstName, String lastName, String email, double weight, int height){
        this.userFirstName = firstName;
        this.userLastName = lastName;
        this.userEmail = email;
        this.userWeight = weight;
        this.userHeight = height;
        this.empty = false;
    }

    public UserProfile(String fullName, String email){
        String[] nameParts = fullName.split(" ", 2);
        this.userFirstName = nameParts[0];
        this.userLastName = nameParts[1];
        this.userEmail = email;
        this.userWeight = 0.0;
        this.userHeight = 0;
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

    public Integer getHeight(){
        return userHeight;
    }

    public boolean checkEmpty(){return empty;}
}
