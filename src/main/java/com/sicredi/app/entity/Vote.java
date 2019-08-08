package com.sicredi.app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Vote {

    @Id
    @GeneratedValue
    private  Long id;

    @ManyToOne
    private Schedule  schedule;

    @ManyToOne
    private Associate associate;

    private int vote;

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Associate getAssociate() {
        return associate;
    }

    public void setAssociate(Associate associate) {
        this.associate = associate;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", schedule=" + schedule +
                ", associate=" + associate +
                '}';
    }
}
