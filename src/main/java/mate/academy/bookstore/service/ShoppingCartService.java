package mate.academy.bookstore.service;

import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.shopingcart.CartItemRequestDto;
import mate.academy.bookstore.model.dto.shopingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {

    ShoppingCartResponseDto addCartItem(CartItemRequestDto requestDto, User user);

    ShoppingCartResponseDto updateCartItem(CartItemRequestDto requestDto,
                                           Long cartItemId);

    ShoppingCartResponseDto deleteCartItem(Long cartItemId, User user);

    ShoppingCartResponseDto clear(Long shoppingCartId);

    ShoppingCartResponseDto findByUser(User user);

}
