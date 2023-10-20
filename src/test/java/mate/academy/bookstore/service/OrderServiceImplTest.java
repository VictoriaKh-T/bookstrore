package mate.academy.bookstore.service;

import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.mapper.OrderMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.OrderItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.order.OrderDto;
import mate.academy.bookstore.model.dto.order.OrderItemDto;
import mate.academy.bookstore.model.dto.order.OrderRequestDto;
import mate.academy.bookstore.repository.order.OrderItemRepository;
import mate.academy.bookstore.repository.order.OrderRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstore.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private ShoppingCart shoppingCart;
    private CartItem cartItem;
    private OrderItemDto orderItemDto;
    private OrderDto orderDto;
    private Book book;
    private Order order;
    private OrderItem orderItem;
    private OrderRequestDto orderRequestDto;

    @BeforeEach
    public void setup(){
        user = new User();
        user.setId(1L);
        user.setEmail("user3@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("1234");
        user.setDelete(false);

        book = Book.builder()
                .id(1L)
                .author("Author test")
                .price(BigDecimal.valueOf(200))
                .title("Book test")
                .isbn("978-0547928227")
                .build();

        shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setId(1L);

        cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItem.setQuantity(10);
        cartItem.setId(1L);

        orderItemDto = new OrderItemDto();
        orderItemDto.setBookId(book.getId());
        orderItemDto.setQuantity(10);
        orderItemDto.setId(cartItem.getId());

        orderItem = new OrderItem();
        orderItem.setPrice(BigDecimal.valueOf(450));
        orderItem.setBook(book);
        orderItem.setOrder(order);
        orderItem.setQuantity(10);
        orderItem.setId(1L);

        order = new Order();

        order.setOrderItems(List.of(orderItem));
        order.setShippingAddress(" some address");
        order.setUser(user);
        order.setStatus(Order.Status.NEW);
        order.setOrderDate(LocalDateTime.now());

        orderDto = new OrderDto();
        orderDto.setTotal(BigDecimal.valueOf(450));
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setOrderItems(List.of(orderItemDto));
        orderDto.setUserId(user.getId());
        orderDto.setStatus("New");

        orderRequestDto = new OrderRequestDto();
        orderRequestDto.setShippingAddress("some address");
    }

    @Test
    @DisplayName("test method createOrder return Order Dto")
    void createOrder_ReturnOk() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        Mockito.lenient().when(orderMapper.mapToDto(any())).thenReturn(orderDto);
        Mockito.lenient().when(orderItemMapper.mapToDto(any())).thenReturn(orderItemDto);

        OrderDto result = orderService.createOrder(orderRequestDto, userId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderDto, result);

        Mockito.verify(shoppingCartRepository, times(1)).findByUser(user);
        Mockito.verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("test method findAllByUser and return Order dto")
    void findAllByUser_ReturnOrderDto() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findOrdersByUser(user)).thenReturn(orders);
        Mockito.lenient().when(orderMapper.mapToDto(any())).thenReturn(orderDto);

        List<OrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(orderDto);

        List<OrderDto> resultList = orderService.findAllByUser(user);
        Assertions.assertNotNull(resultList);
        Assertions.assertEquals(orderDtos, resultList);

        Mockito.verify(orderRepository, times(1)).findOrdersByUser(user);
        Mockito.verify(orderMapper, times(orders.size())).mapToDto(any());
    }

    @Test
    @DisplayName("test method findItemsByOrderId return List<OrderItemDto>")
    void findItemsByOrderId_ReturnOk() {
        Long orderId = 1L;
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        when(orderItemRepository.findAllByOrderId(orderId)).thenReturn(orderItems);
        Mockito.lenient().when(orderItemMapper.mapToDto(any())).thenReturn(orderItemDto);

        List<OrderItemDto> actualList = new ArrayList<>();
        actualList.add(orderItemDto);

        List<OrderItemDto> resultList = orderService.findItemsByOrderId(orderId);

        Assertions.assertNotNull(resultList);
        Assertions.assertEquals(actualList, resultList);

        Mockito.verify(orderItemRepository, times(1)).findAllByOrderId(orderId);
    }

    @Test
    @DisplayName("test method updateStatus")
    void updateStatus_ReturnOrderDto() {
        Long orderId = 1L;
        Order.Status status = Order.Status.COMPLETED;
        order.setStatus(status);
        orderDto.setStatus("Completed");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.lenient().when(orderMapper.mapToDto(any())).thenReturn(orderDto);

        OrderDto result = orderService.updateStatus(orderId, status);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderDto, result);

        Mockito.verify(orderRepository, times(1)).findById(orderId);
    }
}