package restvoteapp.util;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static restvoteapp.util.json.JsonUtil.readValue;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import restvoteapp.model.Menu;
import restvoteapp.model.User;

import java.io.UnsupportedEncodingException;

public class TestUtil {
    public static RequestPostProcessor userHttpBasic(User user) {
        return httpBasic(user.getEmail(), user.getPassword());
    }

    public static <T> T parseObjectFromJson(ResultActions action, Class<T> clazz) throws UnsupportedEncodingException {
        return readValue(action.andReturn().getResponse().getContentAsString(), clazz);
    }
}
