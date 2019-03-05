package io.pivotal.pal.tracker;

import java.time.LocalDate;

public class TimeEntryBuilder {
    private long projectId;
    private long userId;
    private LocalDate date;
    private int hours;
    private long timeEntryId;


    public TimeEntryBuilder() {
    }

    public TimeEntryBuilder(TimeEntry copyFrom) {
                this
                .projectId(copyFrom.getProjectId())
                .userId(copyFrom.getUserId())
                .date(copyFrom.getDate())
                .hours(copyFrom.getHours());
    }

    public TimeEntryBuilder projectId(long projectId) {
        this.projectId = projectId;
        return this;
    }

    public TimeEntryBuilder userId(long userId) {
        this.userId = userId;
        return this;
    }

    public TimeEntryBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public TimeEntryBuilder hours(int hours) {
        this.hours = hours;
        return this;
    }

    public TimeEntryBuilder timeEntryId(long id) {
        this.timeEntryId = id;
        return this;
    }

    public TimeEntry build() {
        return new TimeEntry(timeEntryId, projectId, userId, date, hours);
    }
}