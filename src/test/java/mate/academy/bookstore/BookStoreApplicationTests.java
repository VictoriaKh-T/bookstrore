package mate.academy.bookstore;

import mate.academy.bookstore.config.CustomMySQLContainer;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookStoreApplicationTests {

    @ClassRule
    public static CustomMySQLContainer mySQLContainer = CustomMySQLContainer.getInstance();

    @Test
    void contextLoads() {
   }
}
