package mate.academy.bookstore.service;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CartItemMapper;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.dto.shopingcart.CartItemRequestDto;
import mate.academy.bookstore.model.dto.shopingcart.ShoppingCartResponseDto;
import mate.academy.bookstore.repository.shoppingcart.CartItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto addCartItem(CartItemRequestDto requestDto, Long userId) {
        boolean isPresent = false;
        ShoppingCart shoppingCartByUser
                = shoppingCartRepository.findShoppingCartByUserId(userId);
        for (CartItem item : shoppingCartByUser.getCartItems()) {
            if (item.getBook().getId() == requestDto.getBookId()) {
                isPresent = true;
                break;
            }
        }
        CartItem cartItem = cartItemMapper.mapToModel(requestDto);
        if (!isPresent) {
            shoppingCartByUser.getCartItems().add(cartItemRepository.save(cartItem));
        } else {
            cartItem.setQuantity(requestDto.getQuantity());
            cartItemRepository.save(cartItem);
        }
        shoppingCartRepository.save(shoppingCartByUser);
        return shoppingCartMapper.mapToDto(shoppingCartByUser);
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(CartItemRequestDto requestDto,
                                                  Long cartItemId,
                                                  Long userId) {
        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("item is not found"));
        item.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(item);
        ShoppingCart shoppingCart
                = shoppingCartRepository.save(shoppingCartRepository
                .findShoppingCartByUserId(userId));
        return shoppingCartMapper.mapToDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto deleteCartItem(Long cartItemId, Long userId) {
        cartItemRepository.deleteById(cartItemId);
        ShoppingCart shoppingCart
                = shoppingCartRepository.save(shoppingCartRepository
                .findShoppingCartByUserId(userId));
        return shoppingCartMapper.mapToDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto clear(Long shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId).orElseThrow(
                () -> new EntityNotFoundException("shopping cart is not found"));
        shoppingCart.getCartItems().stream()
                        .forEach(item -> cartItemRepository.deleteById(item.getId()));
        return shoppingCartMapper.mapToDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartResponseDto findByUserId(Long userId) {
        ShoppingCart shoppingCartByUser
                = shoppingCartRepository.findShoppingCartByUserId(userId);
        return shoppingCartMapper.mapToDto(shoppingCartByUser);
    }
}
