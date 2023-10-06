package mate.academy.bookstore.service;

import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.shopingcart.CartItemRequestDto;
import mate.academy.bookstore.model.dto.shopingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {

    ShoppingCartResponseDto addCartItem(CartItemRequestDto requestDto, Long userId);

    ShoppingCartResponseDto updateCartItem(CartItemRequestDto requestDto,
                                           Long cartItemId);

    ShoppingCartResponseDto deleteCartItem(Long cartItemId, Long userId);

    ShoppingCartResponseDto clear(Long shoppingCartId);

    ShoppingCartResponseDto findByUser(User user);

}
