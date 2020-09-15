package restvoteapp.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import restvoteapp.model.Menu;
import restvoteapp.service.MenuService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static restvoteapp.util.TestData.*;
import static restvoteapp.util.TestUtil.userHttpBasic;
import static restvoteapp.util.json.JsonUtil.readValue;
import static restvoteapp.util.json.JsonUtil.writeValue;

class MenuAdminRestControllerTest extends AbstractRestControllerTest {

    private static final String URL = MenuRestController.URL + "/";

    @Autowired
    private MenuService menuService;

    @Test
    void get() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders
                .get(URL + MENU1_ID)
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE));

        Menu menu = readValue(action.andReturn().getResponse().getContentAsString(), Menu.class);
        assertEquals(REST1_ID, (int) menu.getRestaurant().getId());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL)
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_VALUE));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL + MENU1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getWithNoAccess() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL + MENU1_ID)
                .with(userHttpBasic(USER1)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders
                .get(URL + NOT_FOUND_ID)
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createWithLocation() throws Exception {
        ResultActions action =
                perform(post(URL)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(writeValue(newMenuForWeb()))
                        .with(userHttpBasic(ADMIN1)));
        Menu created = readValue(action.andReturn().getResponse().getContentAsString(), Menu.class);
        Menu createdFromDb = menuService.get(created.getId());
        assertEquals(createdFromDb.getRestaurant().getId(), created.getRestaurant().getId());
    }

    @Test
    void createInvalid() throws Exception {
        perform(post(URL)
                .contentType(APPLICATION_JSON_VALUE)
                .content(writeValue(new Menu(null, null, null)))
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDouble() throws Exception {
        Menu newMenu = newMenuForWeb();
        perform(post(URL)
                .contentType(APPLICATION_JSON_VALUE)
                .content(writeValue(newMenu))
                .with(userHttpBasic(ADMIN1)));
        assertThrows(DataIntegrityViolationException.class, () -> {
            try {
                perform(post(URL)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(writeValue(newMenu))
                        .with(userHttpBasic(ADMIN1)))
                        .andExpect(status().is4xxClientError());
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void update() throws Exception {
        perform(put(URL + MENU1_ID)
                .contentType(APPLICATION_JSON_VALUE)
                .content(writeValue(updatedMenuForWeb()))
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().isNoContent());
        Menu updatedFromDb = menuService.get(MENU1_ID);
        assertEquals("REST1 DISHES UPDATED", updatedFromDb.getDishes());
    }

    @Test
    void updateInvalid() throws Exception {
        Menu updateInvalid = new Menu(MENU1_ID, REST1, "TEST STRING");
        perform(put(URL + MENU2_ID).contentType(APPLICATION_JSON_VALUE)
                .content(writeValue(updateInvalid))
                .with(userHttpBasic(ADMIN1)))
                .andExpect(status().is4xxClientError());
    }
}