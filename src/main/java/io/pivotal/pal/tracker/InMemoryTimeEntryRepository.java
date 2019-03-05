package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    private long idSequence = 1;
    private Map<Long, TimeEntry> timeEntryMap = new HashMap<>();
    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        TimeEntry timeEty=new TimeEntry(idSequence,timeEntry.getProjectId(),
                timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());

        timeEntryMap.put(idSequence++, timeEty);


        return timeEty;
    }

    @Override
    public TimeEntry find(long id) {
        return timeEntryMap.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return new ArrayList<>(timeEntryMap.values());

    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        if(!timeEntryMap.containsKey(id))
            return null;

        TimeEntry timeEty=new TimeEntry(id,timeEntry.getProjectId(),
                timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());

        timeEntryMap.put(id, timeEty);
        return timeEty;

    }

    @Override
    public void delete(long id) {
        timeEntryMap.remove(id);

    }
}
