package hexlet.code.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class AppApplicationTests {

    @Autowired
    AppApplication appApplication;

    @Test
    void contextLoads() {
        assertThat(appApplication).isNotNull();
    }

}
