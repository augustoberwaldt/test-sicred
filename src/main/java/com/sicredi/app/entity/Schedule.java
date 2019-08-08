package com.sicredi.app.entity;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;

@Entity
public class Schedule {

    @Id
    @GeneratedValue
    private  Long id;

    @NotBlank(message = "Title e obrigatorio")
    private  String title;

    private  String description;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private  Date dtStarted;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private  Date dtFinish;

    private int time;

    @Transient
    private String timeend;

    @Transient
    private String timestart;

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public String getTimestart() {
        return timestart;
    }

    public void setTimestart(String timestart) {
        this.timestart = timestart;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDtStarted() {
        return dtStarted;
    }

    public void setDtStarted(Date dtStarted) {
        this.dtStarted = dtStarted;
    }

    public Date getDtFinish() {
        return dtFinish;
    }

    public void setDtFinish(Date dtFinish) {
        this.dtFinish = dtFinish;
    }



    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dtCreated=" + dtStarted +
                ", dtFinish=" + dtFinish +
                '}';
    }
}
