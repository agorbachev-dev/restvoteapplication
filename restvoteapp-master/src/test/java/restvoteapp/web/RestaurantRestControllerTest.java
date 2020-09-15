package restvoteapp.web;

import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import restvoteapp.model.Restaurant;
import restvoteapp.service.RestaurantService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static restvoteapp.util.TestData.*;
import static restvoteapp.util.TestUtil.parseObjectFromJson;
import static restvoteapp.util.TestUtil.userHttpBasic;
import static restvoteapp.util.ValidationUtil.getRootCause;
import static restvoteapp.util.json.JsonUtil.*;

class RestaurantRestControllerTest extends AbstractRestControllerTest {

    private static final String URL = RestaurantRestController.URL + "/";

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void createWithLocation() throws Exception {
        ResultActions action =
                perform(post("/api/admin/restaurants/")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(writeValue(newRestForWeb()))
                        .with(userHttpBasic(ADMIN1)));
        Restaurant created = parseObjectFromJson(action, Restaurant.class);
        Restaurant createdFromDb = restaurantService.get(created.getId());
        assertEquals(createdFromDb.getName(), created.getName());
    }

    @Test
    void createInvalid() throws Exception {
        perform(post("/api/admin/restaurants/")
                .contentType(APPLICATION_JSON_VALUE)
                .content(writeValue(new Restaurant(null, null, "")))
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDouble() {
        assertThrows(JdbcSQLIntegrityConstraintViolationException.class, () -> {
            try {
                perform(post("/api/admin/restaurants/")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(writeValue(new Restaurant("REST2")))
                        .with(userHttpBasic(ADMIN1)));
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }

    @Test
    void update() throws Exception {
        perform(put("/api/admin/restaurants/" + REST1_ID)
                .contentType(APPLICATION_JSON_VALUE)
                .content(writeValue(updatedRestForWeb()))
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().isNoContent());
        Restaurant updated = restaurantService.get(REST1_ID);
        assertEquals(REST1_ID, updated.getId());
        assertEquals("UPDATED REST", updated.getName());
    }

    @Test
    void updateInvalid() throws Exception {
        Restaurant updated = new Restaurant(REST1_ID, null, "");
        updated.setId(REST1_ID);
        perform(put("/api/admin/restaurants/" + REST2_ID)
                .contentType(APPLICATION_JSON_VALUE)
                .content(writeValue(updated))
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateIdNotMatches() throws Exception {
        Restaurant updated = new Restaurant(REST1_ID, null, "INVALID");
        updated.setId(REST1_ID);
        perform(put("/api/admin/restaurants/" + REST2_ID)
                .contentType(APPLICATION_JSON_VALUE)
                .content(writeValue(updated))
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAll() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders
                .get(URL)
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE));
        List<Restaurant> allTodayWithMenus = readValues(action.andReturn().getResponse().getContentAsString(), Restaurant.class);
        assertEquals(2, allTodayWithMenus.size());
    }

    @Test
    void get() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders
                .get(URL + REST1_ID)
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE));
        Restaurant got = readValue(action.andReturn().getResponse().getContentAsString(), Restaurant.class);
        assertEquals(REST1_ID, got.getId());
        assertEquals(REST1.getName(), got.getName());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL + REST1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL + 99999)
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getWinners() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders
                .get(URL + "today-winner")
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().isOk());
        List<Restaurant> winners = readValues(action.andReturn().getResponse().getContentAsString(), Restaurant.class);
        assertEquals(2, winners.size());
        assertEquals(REST1_ID, winners.get(0).getId());
        assertEquals(REST2_ID, winners.get(1).getId());
    }
}