package mate.academy.bookstore.repository.order;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>,
        JpaSpecificationExecutor<OrderItem> {
    @Query("FROM OrderItem o WHERE o.order.id = :orderId")
    List<OrderItem> findItemsByOrderId(@Param("orderId") Long orderId);

    @Query("FROM OrderItem oi WHERE oi.order.id = :orderId AND oi.id = :itemId")
    Optional<OrderItem> findByOrderIdAndItemId(@Param("orderId") Long orderId,
                                               @Param("itemId") Long itemId);
}
