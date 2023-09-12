package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.order.OrderDto;
import mate.academy.bookstore.model.dto.order.OrderItemDto;
import mate.academy.bookstore.model.dto.order.OrderRequestDto;
import mate.academy.bookstore.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "order management", description = "Endpoints for managing order")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Tag(name = "add new order",
            description = "This endpoint add new order")
    public OrderDto addOrder(@RequestBody OrderRequestDto requestDto,
                            Authentication auth) {
        User user = getUserByAuth(auth);
        return orderService.createOrder(requestDto, user.getId());
    }

    @GetMapping
    @Tag(name = "get history of orders by user",
            description = "This endpoint gets orders")
    public List<OrderDto> getOrderHistory(Authentication auth) {
        User user = getUserByAuth(auth);
        return orderService.findAllByUser(user);
    }

    @GetMapping("/{id}/items")
    @Tag(name = "get all items",
            description = "This endpoint gets all items by orderId")
    public List<OrderItemDto> findItemsByOrderId(@PathVariable Long id) {
        return orderService.findItemsByOrderId(id);
    }

    @GetMapping("/{id}/items/{itemId}")
    @Tag(name = "get item",
            description = "This endpoint gets item by id and orderId")
    public OrderItemDto getOrderItemByItemId(@PathVariable Long id,
                                             @PathVariable Long itemId) {
        return orderService.getByOrderIdAndItemId(id, itemId);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Tag(name = "udate oder",
            description = "This endpoint update oder. Just for Admin role")
    public OrderDto updateOrderStatus(@PathVariable Long id,
                                      @RequestParam Order.Status status) {
        return orderService.updateStatus(id, status);
    }

    public User getUserByAuth(Authentication auth) {
        return (User) auth.getPrincipal();
    }
}
