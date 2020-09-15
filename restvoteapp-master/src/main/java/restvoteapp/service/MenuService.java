package restvoteapp.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restvoteapp.model.Menu;
import restvoteapp.repository.MenuRepository;
import restvoteapp.util.exception.TimeExpiredException;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.util.Assert.notNull;
import static restvoteapp.util.ValidationUtil.*;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;

    public Menu get(int id) {
        return checkNotFound(menuRepository.getById(id), "Not found menu with id " + id);
    }

    @Transactional
    public void update(Menu menu, int id) {
        notNull(menu, "menu must not be null");
        checkDateExpired(menu);
        assureIdConsistent(menu, id);
        menuRepository.save(menu);
    }

    @Transactional
    public Menu create(Menu menu) {
        notNull(menu, "menu must not be null");
        checkDateExpired(menu);
        checkNew(menu);
        return menuRepository.save(menu);
    }

    public List<Menu> getAll() {
        return menuRepository.findAll();
    }


    public List<Menu> getAllWithRestaurants(LocalDate date) {
        return menuRepository.getAllByMenudate(date);
    }

    public void checkDateExpired(Menu menu) {
        if (menu.getMenudate().isBefore(LocalDate.now())) {
            throw new TimeExpiredException("menu can't be created or edited in past");
        }
    }
}
