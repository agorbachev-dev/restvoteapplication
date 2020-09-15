package restvoteapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import restvoteapp.AuthorizedUser;
import restvoteapp.model.User;
import restvoteapp.util.exception.NotFoundException;

class UserServiceTest extends AbstractServiceTest{

    @Autowired
    private UserService userService;

    @Test
    void loadUserByUsername() {
        AuthorizedUser founded = userService.loadUserByUsername("user1@example.com");
        assertEquals(founded.getUsername(), "user1@example.com");
    }

    @Test
    void loadUserByUsernameNotFound(){
        assertThrows(UsernameNotFoundException.class, ()->userService.loadUserByUsername("test@test.test"));
    }

    @Test
    void getById() {
        User founded = userService.getById(100000);
        assertTrue(
                founded.getId() == 100000
                        && founded.getEmail().equals("user1@example.com")
        );
    }

    @Test
    void getByIdNotFound() {
        assertThrows(NotFoundException.class, ()->userService.getById(999));
    }
}