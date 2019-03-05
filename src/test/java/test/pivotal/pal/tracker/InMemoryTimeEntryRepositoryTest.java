package test.pivotal.pal.tracker;

import io.pivotal.pal.tracker.InMemoryTimeEntryRepository;
import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.TimeEntryBuilder;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryTimeEntryRepositoryTest {
    @Test
    public void create() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        TimeEntry createdTimeEntry = repo.create(new TimeEntryBuilder().projectId(projectId).userId(userId).date(LocalDate.parse("2017-01-08")).hours(8).build());

        long timeEntryId = 1L;
        TimeEntry expected = new TimeEntryBuilder().timeEntryId(timeEntryId).projectId(projectId).userId(userId).date(LocalDate.parse("2017-01-08")).hours(8).build();
        assertThat(createdTimeEntry).isEqualTo(expected);

        TimeEntry readEntry = repo.find(createdTimeEntry.getId());
        assertThat(readEntry).isEqualTo(expected);
    }

    @Test
    public void find() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        repo.create(new TimeEntryBuilder().projectId(projectId).userId(userId).date(LocalDate.parse("2017-01-08")).hours(8).build());

        long timeEntryId = 1L;
        TimeEntry expected = new TimeEntryBuilder().timeEntryId(timeEntryId).projectId(projectId).userId(userId).date(LocalDate.parse("2017-01-08")).hours(8).build();
        TimeEntry readEntry = repo.find(timeEntryId);
        assertThat(readEntry).isEqualTo(expected);
    }

    @Test
    public void find_MissingEntry() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long timeEntryId = 1L;

        TimeEntry readEntry = repo.find(timeEntryId);
        assertThat(readEntry).isNull();
    }

    @Test
    public void list() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();
        repo.create(new TimeEntryBuilder().projectId(123L).userId(456L).date(LocalDate.parse("2017-01-08")).hours(8).build());
        repo.create(new TimeEntryBuilder().projectId(789L).userId(654L).date(LocalDate.parse("2017-01-07")).hours(4).build());

        List<TimeEntry> expected = asList(
                new TimeEntryBuilder().timeEntryId(1L).projectId(123L).userId(456L).date(LocalDate.parse("2017-01-08")).hours(8).build(),
                new TimeEntryBuilder().timeEntryId(2L).projectId(789L).userId(654L).date(LocalDate.parse("2017-01-07")).hours(4).build()
        );
        assertThat(repo.list()).isEqualTo(expected);
    }

    @Test
    public void update() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();
        TimeEntry created = repo.create(new TimeEntryBuilder().projectId(123L).userId(456L).date(LocalDate.parse("2017-01-08")).hours(8).build());

        TimeEntry updatedEntry = repo.update(
                created.getId(),
                new TimeEntryBuilder().projectId(321L).userId(654L).date(LocalDate.parse("2017-01-09")).hours(5).build());

        TimeEntry expected = new TimeEntryBuilder().timeEntryId(created.getId()).projectId(321L).userId(654L).date(LocalDate.parse("2017-01-09")).hours(5).build();
        assertThat(updatedEntry).isEqualTo(expected);
        assertThat(repo.find(created.getId())).isEqualTo(expected);
    }

    @Test
    public void update_MissingEntry() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        TimeEntry updatedEntry = repo.update(
                1L,
                new TimeEntryBuilder().projectId(321L).userId(654L).date(LocalDate.parse("2017-01-09")).hours(5).build());

        assertThat(updatedEntry).isNull();
    }

    @Test
    public void delete() throws Exception {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        TimeEntry created = repo.create(new TimeEntryBuilder().projectId(projectId).userId(userId).date(LocalDate.parse("2017-01-08")).hours(8).build());

        repo.delete(created.getId());
        assertThat(repo.list()).isEmpty();
    }

    @Test
    public void deleteKeepsTrackOfLatestIdProperly() {
        InMemoryTimeEntryRepository repo = new InMemoryTimeEntryRepository();

        long projectId = 123L;
        long userId = 456L;
        TimeEntry created = repo.create(new TimeEntryBuilder().projectId(projectId).userId(userId).date(LocalDate.parse("2017-01-08")).hours(8).build());

        assertThat(created.getId()).isEqualTo(1);

        repo.delete(created.getId());

        TimeEntry createdSecond = repo.create(new TimeEntryBuilder().projectId(projectId).userId(userId).date(LocalDate.parse("2017-01-08")).hours(8).build());

        assertThat(createdSecond.getId()).isEqualTo(2);
    }
}
