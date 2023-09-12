package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.order.OrderDto;
import mate.academy.bookstore.model.dto.order.OrderItemDto;
import mate.academy.bookstore.model.dto.order.OrderRequestDto;

public interface OrderService {
    OrderDto createOrder(OrderRequestDto requestDto,Long userId);

    List<OrderDto> findAllByUser(User user);

    OrderDto updateStatus(Long orderId, Order.Status status);

    List<OrderItemDto> findItemsByOrderId(Long orderId);

    OrderItemDto getByOrderIdAndItemId(Long orderId, Long itemId);

}
