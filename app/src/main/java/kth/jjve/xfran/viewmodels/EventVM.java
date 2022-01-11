package kth.jjve.xfran.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import kth.jjve.xfran.models.EventInApp;
import kth.jjve.xfran.repositories.EventRepo;

public class EventVM extends ViewModel {
    private MutableLiveData<List<EventInApp>> mEvents; //subclass of LiveData (mutable)
    private EventRepo mRepo;

    public void init(LocalDate date) {
        if (mEvents != null) {
            mEvents = mRepo.getEvents(date);
            return;
        }
        mRepo = EventRepo.getInstance();
        mEvents = mRepo.getEvents(date);
    }

    public void addNewEvent(String name, LocalDate date, LocalTime startTime, LocalTime endTime) {
        mRepo.addNewEvent(name, date, startTime, endTime);
    }

    public LiveData<List<EventInApp>> getEvents() {
        return mEvents;
    }

}
