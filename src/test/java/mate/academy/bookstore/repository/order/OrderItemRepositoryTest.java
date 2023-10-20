package mate.academy.bookstore.repository.order;

import mate.academy.bookstore.model.OrderItem;
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
class OrderItemRepositoryTest {

    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("test method indByIdAndOrderId return List")
    void findAllByOrderId_ReturnListOrderItems() {
        Long orderId = 1L;
        int actaul = 3;
        List<OrderItem> resultList = orderItemRepository.findAllByOrderId(orderId);
        Assertions.assertEquals(actaul, resultList.size());
    }
}