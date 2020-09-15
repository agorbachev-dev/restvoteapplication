package restvoteapp.service;

import org.hibernate.TransientPropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import restvoteapp.model.Restaurant;
import restvoteapp.model.Vote;
import restvoteapp.util.exception.NotFoundException;
import restvoteapp.util.exception.TimeExpiredException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static restvoteapp.util.TestData.*;
import static restvoteapp.util.ValidationUtil.getRootCause;

class VoteServiceTest extends AbstractServiceTest {

    @Autowired
    private VoteService voteService;

    @Test
    void getByIdAndUserId() {
        Vote founded = voteService.getByIdAndUserId(VOTE1_ID, USER1_ID);
        assertEquals(VOTE1_ID, founded.getId());
        assertEquals(REST1_ID, founded.getRestaurant().getId());
    }

    @Test
    void getByIdAndWrongUserId() {
        assertThrows(NotFoundException.class, () -> voteService.getByIdAndUserId(VOTE1_ID, USER2_ID));
    }

    @Test
    void create() {
        Vote created = voteService.create(newVoteForService());
        Vote createdFromDb = voteService.getByIdAndUserId(created.getId(), USER2_ID);
        assertEquals(LocalDate.now(), createdFromDb.getVotedate());
        assertEquals(REST2_ID, createdFromDb.getRestaurant().getId());
        assertEquals(USER2_ID, createdFromDb.getUser().getId());
    }

    @Test
    void createNotValid() {
        assertThrows(TransientPropertyValueException.class, () -> {
            try {
                voteService.create(new Vote(new Restaurant("TEST NOT FOUND")));
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }

    @Test
    void getToday() {
        Vote today = voteService.getToday(LocalDate.now(), ADMIN1_ID);
        assertEquals(LocalDate.now(), today.getVotedate());
        assertEquals(REST2_ID, today.getRestaurant().getId());
        assertEquals(ADMIN1_ID, today.getUser().getId());
    }

    @Test
    void delete() {
        voteService.setCheckTime(LocalTime.now().plusMinutes(1));
        voteService.delete(USER1_ID);
        assertThrows(NotFoundException.class, () -> voteService.getByIdAndUserId(VOTE1_ID, USER1_ID));
    }

    @Test
    void deleteTimeExpired() {
        voteService.setCheckTime(LocalTime.now().minusMinutes(1));
        assertThrows(TimeExpiredException.class, () -> voteService.delete(USER1_ID));
    }

    @Test
    void update() {
        voteService.setCheckTime(LocalTime.now().plusMinutes(2));
        voteService.update(updatedVoteForService(), USER1);
        Vote today = voteService.getToday(LocalDate.now(), USER1_ID);
        assertEquals(REST2_ID, today.getRestaurant().getId());
        assertEquals(USER1_ID, today.getUser().getId());
    }

    @Test
    void updateTimeExpired() {
        voteService.setCheckTime(LocalTime.now().minusMinutes(1));
        assertThrows(TimeExpiredException.class, () -> voteService.update(updatedVoteForService(), ADMIN1));
    }

    @Test
    void updateNotFound() {
        voteService.setCheckTime(LocalTime.now().plusMinutes(1));
        assertThrows(NotFoundException.class, () -> voteService.update(updatedVoteForService(), USER2));
    }
}