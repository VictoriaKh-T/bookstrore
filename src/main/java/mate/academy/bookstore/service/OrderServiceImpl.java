package mate.academy.bookstore.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.mapper.OrderMapper;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.OrderItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.order.OrderDto;
import mate.academy.bookstore.model.dto.order.OrderItemDto;
import mate.academy.bookstore.model.dto.order.OrderRequestDto;
import mate.academy.bookstore.model.dto.order.StatusRequestDto;
import mate.academy.bookstore.repository.order.OrderItemRepository;
import mate.academy.bookstore.repository.order.OrderRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstore.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;

    @Override
    public OrderDto createOrder(OrderRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException.supplier("User not found"));
        Order order = new Order();
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setUser(user);
        order.setStatus(Order.Status.NEW);
        order.setOrderDate(LocalDateTime.now());

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(EntityNotFoundException.supplier("shopping cart is not found"));

        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());

        BigDecimal total = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        OrderDto orderDto = orderMapper.mapToDto(order);
        orderDto.setOrderItems(orderItems.stream()
                .map(orderItemMapper::mapToDto)
                .toList());

        return orderDto;
    }

    @Override
    public List<OrderDto> findAllByUserId(User user) {
        return orderRepository.findOrdersByUser(user).stream()
                .map(orderMapper::mapToDto)
                .toList();
    }

    @Override
    public OrderDto updateStatus(Long orderId, StatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                EntityNotFoundException.supplier("order not found"));
        order.setStatus(Order.Status.valueOf(requestDto.getStatus()));
        return orderMapper.mapToDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> findItemsByOrderId(Long orderId) {
        return orderItemRepository.findItemsByOrderId(orderId).stream()
                .map(orderItemMapper::mapToDto)
                .toList();
    }

    @Override
    public OrderItemDto getByOrderIdAndItemId(Long orderId, Long itemId) {
        return orderItemMapper.mapToDto(
                orderItemRepository.findByOrderIdAndItemId(orderId, itemId).orElseThrow(
                        EntityNotFoundException.supplier("item is not found")
                ));
    }
}
