package restvoteapp.service;

import org.hibernate.TransientPropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import restvoteapp.model.Menu;
import restvoteapp.model.Restaurant;
import restvoteapp.util.exception.IllegalRequestDataException;
import restvoteapp.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static restvoteapp.util.TestData.*;
import static restvoteapp.util.ValidationUtil.getRootCause;

class MenuServiceTest extends AbstractServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    void get() {
        Menu menu = menuService.get(MENU1_ID);
        assertEquals(MENU1_ID, (int) menu.getId());
        assertEquals(REST1_ID, menu.getRestaurant().getId());
        assertEquals(LocalDate.now(), menu.getMenudate());
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> menuService.get(NOT_FOUND_ID));
    }

    @Test
    void update() {
        menuService.update(updatedMenuForService(), MENU1_ID);
        Menu updatedFromDb = menuService.get(MENU1_ID);
        assertEquals("REST1 DISHES UPDATED", updatedFromDb.getDishes());
    }

    @Test
    void updateNotFound() {
        assertThrows(DataIntegrityViolationException.class, () -> menuService.update(new Menu(null, REST1, "TEST STRING"), NOT_FOUND_ID));
    }

    @Test
    void updateIdNotMatches() {
        Menu updated = updatedMenuForService();
        updated.setId(MENU1_ID);
        assertThrows(IllegalRequestDataException.class, () -> menuService.update(updated, NOT_FOUND_ID));
    }

    @Test
    void updateNotValid() {
        assertThrows(ConstraintViolationException.class, () -> {
            try {
                menuService.update(new Menu(null, null, null), MENU1_ID);
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }

    @Test
    void create() {
        Menu menu = menuService.create(newMenuForService());
        Menu created = menuService.get(menu.getId());
        assertEquals(REST1_ID, created.getRestaurant().getId());
    }

    @Test
    void createNotValid() {
        assertThrows(ConstraintViolationException.class, () -> {
            try {
                menuService.create(new Menu(null, LocalDate.now().plusDays(1), REST1, null));
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }

    @Test
    void createRestaurantNotExists() {
        assertThrows(TransientPropertyValueException.class, () -> {
            try {
                menuService.create(new Menu(null, LocalDate.now().plusDays(1), new Restaurant(), null));
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }
}