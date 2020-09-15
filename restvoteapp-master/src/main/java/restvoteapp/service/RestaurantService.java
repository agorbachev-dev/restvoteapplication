package restvoteapp.service;

import static org.springframework.util.Assert.notNull;
import static restvoteapp.util.ValidationUtil.assureIdConsistent;
import static restvoteapp.util.ValidationUtil.checkNew;
import static restvoteapp.util.ValidationUtil.checkNotFound;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restvoteapp.model.Menu;
import restvoteapp.model.Restaurant;
import restvoteapp.repository.RestaurantRepository;
import restvoteapp.util.exception.NotFoundException;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuService menuService;


    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant get(int id) {
        return checkNotFound(restaurantRepository.getById(id), "Not found restaurant with id " + id);
    }

    @Transactional
    public Restaurant create(Restaurant restaurant) {
        notNull(restaurant, "restaurant must not be null");
        checkNew(restaurant);
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public void update(Restaurant restaurant, int id) {
        notNull(restaurant, "restaurant must not be null");
        assureIdConsistent(restaurant, id);
        checkNotFound(get(id), "not found with id" + id);
        restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllOnDateWithMenus(LocalDate date) {
        List<Menu> menusOnDate =  menuService.getAllWithRestaurants(date);
        List<Restaurant> restaurants = new ArrayList<>();
        menusOnDate.forEach(m->{
            Restaurant r = m.getRestaurant();
            r.setMenus(List.of(m));
            restaurants.add(r);
        });
        return restaurants;
    }

    public List<Restaurant> getWinners(LocalDate date) {
        return restaurantRepository.getWinnersOnDate(date, date);
    }
}
