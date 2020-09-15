package restvoteapp.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import restvoteapp.model.Menu;
import restvoteapp.service.MenuService;

import java.net.URI;
import java.util.List;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@RestController
@RequestMapping(value = MenuRestController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class MenuRestController {
    public static final String URL = "/api/admin/menus";
    private final MenuService menuService;


    @GetMapping()
    public List<Menu> getAll() {
        log.info("getAll");
        return menuService.getAll();
    }

    @GetMapping("/{id}")
    public Menu getById(@PathVariable int id) {
        log.info("get menu by id {}", id);
        return menuService.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Validated @RequestBody Menu menu) {
        log.info("create menu {}", menu);
        Menu created = menuService.create(menu);
        URI uriOfNewResource = fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Validated @RequestBody Menu menu, @PathVariable int id) {
        log.info("update menu {} with id {}", menu, id);
        menuService.update(menu, id);
    }
}
