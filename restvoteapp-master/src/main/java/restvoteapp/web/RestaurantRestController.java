package restvoteapp.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import restvoteapp.View;
import restvoteapp.model.Restaurant;
import restvoteapp.service.RestaurantService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class RestaurantRestController {

    public static final String URL = "/api/restaurants";
    private final RestaurantService restaurantService;


    @PostMapping(value = "/api/admin/restaurants", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Validated(View.Web.class) @RequestBody Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        Restaurant created = restaurantService.create(restaurant);
        URI uriOfNewResource = fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/api/admin/restaurants/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Validated(View.Web.class) @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update restaurant {} with id {}", restaurant, id);
        restaurantService.update(restaurant, id);
    }

    @GetMapping(value = "/api/restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantService.getAll();
    }

    @GetMapping(value = "/api/restaurants/{id}")
    public Restaurant get(@PathVariable("id") int id) {
        log.info("get restaurant by id {}", id);
        return restaurantService.get(id);
    }

    @GetMapping("/api/restaurants/with-today-menus")
    public List<Restaurant> getAllWithTodayMenus() {
        log.info("get restaurants with today menus");
        return restaurantService.getAllOnDateWithMenus(LocalDate.now());
    }

    @GetMapping("/api/restaurants/today-winner")
    public List<Restaurant> getWinners() {
        log.info("get restaurants with today menus");
        return restaurantService.getWinners(LocalDate.now());
    }

}
