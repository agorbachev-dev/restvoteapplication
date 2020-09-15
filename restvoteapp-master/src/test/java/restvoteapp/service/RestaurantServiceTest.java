package restvoteapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import restvoteapp.model.Restaurant;
import restvoteapp.util.exception.IllegalRequestDataException;
import restvoteapp.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static restvoteapp.util.TestData.*;
import static restvoteapp.util.ValidationUtil.getRootCause;

class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void get() {
        Restaurant restaurant = restaurantService.get(REST1_ID);
        assertEquals(REST1_ID, restaurant.getId());
        assertEquals("REST1", restaurant.getName());
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> restaurantService.get(999));
    }

    @Test
    void create() {
        Restaurant restaurant = restaurantService.create(newRestForService());
        assertEquals("NEW REST", restaurant.getName());


    }

    @Test
    void createNotValid() {
        assertThrows(ConstraintViolationException.class, () -> {
            try {
                restaurantService.create(new Restaurant(""));
            } catch (Exception e) {
                throw getRootCause(e);
            }
        });
    }

    @Test
    void update() {
        restaurantService.update(updatedRestForService(), REST1_ID);
        Restaurant updatedFromDb = restaurantService.get(REST1_ID);
        assertEquals("UPDATED REST", updatedFromDb.getName());
    }

    @Test
    void updateIdNotMatches() {
        Restaurant updatedRestaurant = newRestForService();
        updatedRestaurant.setId(REST2_ID);
        assertThrows(IllegalRequestDataException.class, () -> restaurantService.update(updatedRestaurant, REST1_ID));
    }

    @Test
    void updateNotFound() {
        assertThrows(NotFoundException.class, () -> restaurantService.update(updatedRestForService(), 999));
    }

    @Test
    void getAllOnDateWithMenus() {
        List<Restaurant> allOnDateWithMenus = restaurantService.getAllOnDateWithMenus(LocalDate.now());
        assertEquals(2, allOnDateWithMenus.size());
        assertEquals(1, allOnDateWithMenus.get(0).getMenus().size());
        assertEquals(MENU1_ID, allOnDateWithMenus.get(0).getMenus().get(0).getId());
        assertEquals(REST1_ID, allOnDateWithMenus.get(0).getMenus().get(0).getRestaurant().getId());
    }

    @Test
    void getWinners() {
        List<Restaurant> winners = restaurantService.getWinners(LocalDate.now());
        assertEquals(2,winners.size());
        assertEquals(REST1_ID, winners.get(0).getId());
        assertEquals(REST2_ID, winners.get(1).getId());
    }
}