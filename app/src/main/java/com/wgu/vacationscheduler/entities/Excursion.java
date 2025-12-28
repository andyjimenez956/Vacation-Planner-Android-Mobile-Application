package com.wgu.vacationscheduler.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "excursions",
        foreignKeys = @ForeignKey(
                entity = Vacation.class,
                parentColumns = "id",
                childColumns = "vacationId",
                onDelete = ForeignKey.RESTRICT
        ),
        indices = {@Index("vacationId")}
)
public class Excursion {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int vacationId;
    private String title;
    private long date;

    public Excursion() { }

    @Ignore
    public Excursion( String title, long date,int vacationId) {
        this.title = title;
        this.date = date;
        this.vacationId = vacationId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVacationId() { return vacationId; }
    public void setVacationId(int vacationId) { this.vacationId = vacationId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
}
