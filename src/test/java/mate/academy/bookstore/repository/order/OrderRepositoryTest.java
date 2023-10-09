package mate.academy.bookstore.repository.order;

import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:database/sql-order/repository/add-user-add-order-add-items.sql"},
executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:database/sql-order/repository/delete-user-oder-and-items.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("test method findOrdersByUser and return List")
    void findOrdersByUser_ReturnListOfOrders() {
        User user = new User();
        user.setId(3L);
        user.setEmail("user3@example.com");
        user.setPassword("1234");
        user.setFirstName("John");
        user.setLastName("Sonny");
        user.setDelete(false);
        List<Order> resultList = orderRepository.findOrdersByUser(user);
        Assertions.assertEquals(1, resultList.size());
    }
}