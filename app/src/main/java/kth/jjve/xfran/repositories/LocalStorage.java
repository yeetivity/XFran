package kth.jjve.xfran.repositories;
/*
Function: save information to the local storage
Used by:
Jitse van Esch, Mariah Sabioni & Elisa Perini
 */

import java.io.Serializable;
import java.time.LocalDate;

public class LocalStorage implements Serializable {

    private LocalDate mDate;
    private Integer previousWOD;

    public LocalStorage(LocalDate date){
        mDate = date;
    }

    public LocalDate getDate(){return mDate;}

    public Integer getPreviousWOD() {return previousWOD;}

    public void setDateStorage(LocalDate date){mDate = date;}

    public void setPreviousWOD(int wodChooser){previousWOD = wodChooser;}
}
