package restvoteapp.util;

import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import restvoteapp.model.User;

import static org.springframework.util.StringUtils.hasText;

@NoArgsConstructor
public class UserUtil {
    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        user.setPassword(hasText(password) ? passwordEncoder.encode(password) : password);
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
