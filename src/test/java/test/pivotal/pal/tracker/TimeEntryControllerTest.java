package test.pivotal.pal.tracker;

import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.TimeEntryBuilder;
import io.pivotal.pal.tracker.TimeEntryController;
import io.pivotal.pal.tracker.TimeEntryRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TimeEntryControllerTest {
    private TimeEntryRepository timeEntryRepository;
    private TimeEntryController controller;

    @Before
    public void setUp() throws Exception {
        timeEntryRepository = mock(TimeEntryRepository.class);
        controller = new TimeEntryController(timeEntryRepository);
    }

    @Test
    public void testCreate() throws Exception {
        long projectId = 123L;
        long userId = 456L;
        TimeEntry timeEntryToCreate = new TimeEntryBuilder().projectId(projectId).userId(userId).date(LocalDate.parse("2017-01-08")).hours(8).build();

        long timeEntryId = 1L;
        TimeEntry expectedResult = new TimeEntryBuilder().timeEntryId(timeEntryId).projectId(projectId).userId(userId).date(LocalDate.parse("2017-01-08")).hours(8).build();
        doReturn(expectedResult)
            .when(timeEntryRepository)
            .create(any(TimeEntry.class));


        ResponseEntity response = controller.create(timeEntryToCreate);


        verify(timeEntryRepository).create(timeEntryToCreate);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(expectedResult);
    }

    @Test
    public void testRead() throws Exception {
        long timeEntryId = 1L;
        long projectId = 123L;
        long userId = 456L;
        TimeEntry expected = new TimeEntryBuilder().timeEntryId(timeEntryId).projectId(projectId).userId(userId).date(LocalDate.parse("2017-01-08")).hours(8).build();
        doReturn(expected)
            .when(timeEntryRepository)
            .find(timeEntryId);

        ResponseEntity<TimeEntry> response = controller.read(timeEntryId);

        verify(timeEntryRepository).find(timeEntryId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void testRead_NotFound() throws Exception {
        long nonExistentTimeEntryId = 1L;
        doReturn(null)
            .when(timeEntryRepository)
            .find(nonExistentTimeEntryId);

        ResponseEntity<TimeEntry> response = controller.read(nonExistentTimeEntryId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testList() throws Exception {
        List<TimeEntry> expected = asList(
                new TimeEntryBuilder().timeEntryId(1L).projectId(123L).userId(456L).date(LocalDate.parse("2017-01-08")).hours(8).build(),
                new TimeEntryBuilder().timeEntryId(2L).projectId(789L).userId(321L).date(LocalDate.parse("2017-01-07")).hours(4).build()
        );
        doReturn(expected).when(timeEntryRepository).list();

        ResponseEntity<List<TimeEntry>> response = controller.list();

        verify(timeEntryRepository).list();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void testUpdate() throws Exception {
        long timeEntryId = 1L;
        long projectId = 987L;
        long userId = 654L;
        TimeEntry expected = new TimeEntryBuilder().timeEntryId(timeEntryId).projectId(projectId).userId(userId).date(LocalDate.parse("2017-01-07")).hours(4).build();
        doReturn(expected)
            .when(timeEntryRepository)
            .update(eq(timeEntryId), any(TimeEntry.class));

        ResponseEntity response = controller.update(timeEntryId, expected);

        verify(timeEntryRepository).update(timeEntryId, expected);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void testUpdate_NotFound() throws Exception {
        long nonExistentTimeEntryId = 1L;
        doReturn(null)
            .when(timeEntryRepository)
            .update(eq(nonExistentTimeEntryId), any(TimeEntry.class));

        ResponseEntity response = controller.update(nonExistentTimeEntryId, new TimeEntryBuilder().build());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDelete() throws Exception {
        long timeEntryId = 1L;
        ResponseEntity<TimeEntry> response = controller.delete(timeEntryId);
        verify(timeEntryRepository).delete(timeEntryId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
