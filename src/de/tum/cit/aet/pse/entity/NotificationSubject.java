package de.tum.cit.aet.pse.entity;

public interface NotificationSubject {
    void addObserver(CustomerObserver observer);
    void removeObserver(CustomerObserver observer);
    void notifyObservers(String message);
}
