package mate.academy.bookstore.repository.order;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>,
        JpaSpecificationExecutor<OrderItem> {

    List<OrderItem> findAllByOrderId(@Param("orderId") Long orderId);

    Optional<OrderItem> findByIdAndOrderId(@Param("orderId") Long orderId,
                                           @Param("itemId") Long itemId);
}
