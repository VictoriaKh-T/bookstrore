package mate.academy.bookstore.repository.order;

import java.util.List;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderRepository extends JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {

    @EntityGraph(attributePaths = {"orderItems", "orderItems.book"})
    List<Order> findOrdersByUser(User user);
}
