package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.shopingcart.CartItemRequestDto;
import mate.academy.bookstore.model.dto.shopingcart.ShoppingCartResponseDto;
import mate.academy.bookstore.service.ShoppingCartService;
import mate.academy.bookstore.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "shopping cart management", description = "Endpoints for managing shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    @GetMapping
    @Tag(name = "Get shopping cart",
            description = "This endpoint returns a shopping cart.")
    public ShoppingCartResponseDto getShoppingCart(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return shoppingCartService.findByUser(user);
    }

    @PostMapping
    @Tag(name = "add new shopping cart",
            description = "This endpoint add new item to shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartResponseDto addItem(@RequestBody CartItemRequestDto requestDto,
                                           Authentication auth) {
        User user = (User) auth.getPrincipal();
        return shoppingCartService.addCartItem(requestDto, user);
    }

    @PutMapping("cart-items/{cartItemId}")
    @Tag(name = "update item",
            description = "This endpoint update item in shopping cart")
    public ShoppingCartResponseDto updateItem(@PathVariable Long cartItemId,
                                              @RequestBody CartItemRequestDto requestDto,
                                              Authentication auth) {
        User user = (User) auth.getPrincipal();
        shoppingCartService.updateCartItem(requestDto, cartItemId);
        return shoppingCartService.findByUser(user);
    }

    @DeleteMapping("cart-items/{cartItemId}")
    @Tag(name = "delete item by id",
            description = "This endpoint delete item from shopping cart")
    public void deleteItemFromCart(@PathVariable Long cartItemId,
                                   Authentication auth) {
        User user = (User) auth.getPrincipal();
        shoppingCartService.deleteCartItem(cartItemId, user);
    }

    @DeleteMapping("/{id}")
    @Tag(name = "delete all items",
            description = "This endpoint delete all item from shopping cart")
    public ShoppingCartResponseDto deleteAllItemsFromCart(@PathVariable Long shoppingCartId) {
        return shoppingCartService.clear(shoppingCartId);
    }
}
