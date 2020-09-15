package restvoteapp.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import restvoteapp.model.Restaurant;
import restvoteapp.model.Vote;
import restvoteapp.service.VoteService;
import restvoteapp.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static restvoteapp.util.TestData.*;
import static restvoteapp.util.TestUtil.userHttpBasic;
import static restvoteapp.util.json.JsonUtil.*;

class VoteRestControllerTest extends AbstractRestControllerTest {

    public static final String URL = VoteRestController.URL + "/";

    @Autowired
    private VoteService voteService;

    @Test
    void getToday() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders
                .get(URL + "today")
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE));
        Vote gotToday = readValue(action.andReturn().getResponse().getContentAsString(), Vote.class);
        assertEquals(USER1_ID, gotToday.getUser().getId());
        assertEquals(LocalDate.now(), gotToday.getVotedate());
        assertEquals(REST1_ID, gotToday.getRestaurant().getId());
    }

    @Test
    void getTodayNotFound() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL + "today")
                .with(userHttpBasic(USER2)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL + VOTE1_ID)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE));
    }

    @Test
    void getByIdNotFound() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL + 99999)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByIdNotOwner() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL + VOTE1_ID)
                .with(userHttpBasic(USER2)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByIdNotAuth() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL + VOTE1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createWithLocation() throws Exception {
        voteService.setCheckTime(LocalTime.now().plusMinutes(2));
        ResultActions action = perform(post(URL + "today")
                .contentType(APPLICATION_JSON_VALUE)
                .content(writeValue(newVoteForWeb()))
                .with(userHttpBasic(USER2)));
        Vote created = readValue(action.andReturn().getResponse().getContentAsString(), Vote.class);
        Vote createdFromDb = voteService.getByIdAndUserId(created.getId(), created.getUser().getId());
        assertEquals(USER2_ID, createdFromDb.getUser().getId());
        assertEquals(now(), createdFromDb.getVotedate());
        assertEquals(REST2_ID, createdFromDb.getRestaurant().getId());
    }

    @Test
    void delete() throws Exception {
        voteService.setCheckTime(LocalTime.now().plusMinutes(2));
        perform(MockMvcRequestBuilders
                .delete(URL + "today")
                .with(userHttpBasic(ADMIN1)));
        assertThrows(NotFoundException.class, () -> voteService.getToday(now(), ADMIN1_ID));
    }

    @Test
    void update() throws Exception {
        voteService.setCheckTime(LocalTime.now().plusMinutes(2));
        perform(MockMvcRequestBuilders
                .put(URL + "today")
                .contentType(APPLICATION_JSON_VALUE)
                .content(writeValue(updatedVoteForWeb()))
                .with(userHttpBasic(USER1)));
        Vote fromDb = voteService.getToday(now(), USER1_ID);
        assertEquals(USER1_ID, fromDb.getUser().getId());
        assertEquals(now(), fromDb.getVotedate());
        assertEquals(REST2_ID, fromDb.getRestaurant().getId());
    }
}