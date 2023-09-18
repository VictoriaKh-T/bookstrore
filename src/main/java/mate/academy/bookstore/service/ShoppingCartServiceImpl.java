package mate.academy.bookstore.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.exception.BookAlreadyExistsException;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.shopingcart.CartItemRequestDto;
import mate.academy.bookstore.model.dto.shopingcart.ShoppingCartResponseDto;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.shoppingcart.CartItemRepository;
import mate.academy.bookstore.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstore.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCartResponseDto addCartItem(CartItemRequestDto request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException.supplier("User not found"));
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(EntityNotFoundException.supplier("Book not found"));
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    ShoppingCart newShoppingCart = new ShoppingCart();
                    newShoppingCart.setUser(user);
                    shoppingCartRepository.save(newShoppingCart);
                    return newShoppingCart;
                });
        Optional<CartItem> cartItem = cartItemRepository
                .findByShoppingCartIdAndBookId(shoppingCart.getId(), book.getId());
        if (cartItem.isPresent()) {
            BookAlreadyExistsException.supplier("Book already exists in the cart");
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setBook(book);
            newCartItem.setShoppingCart(shoppingCart);
            newCartItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newCartItem);
        }
        return shoppingCartMapper.mapToDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(CartItemRequestDto requestDto,
                                                  Long cartItemId,
                                                  Long userId) {
        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow(
                EntityNotFoundException.supplier("item is not found"));
        ShoppingCart shoppingCart
                = shoppingCartRepository.save(shoppingCartRepository
                .findByUserId(userId).orElseThrow(
                        EntityNotFoundException.supplier("shopping cart is not found")));
        item.setQuantity(requestDto.getQuantity());
        item.setShoppingCart(shoppingCart);
        cartItemRepository.save(item);
        return shoppingCartMapper.mapToDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto deleteCartItem(Long cartItemId, Long userId) {
        cartItemRepository.deleteById(cartItemId);
        ShoppingCart shoppingCart
                = shoppingCartRepository.save(shoppingCartRepository
                .findByUserId(userId).orElseThrow(
                        EntityNotFoundException.supplier("shopping cart is not found")));
        return shoppingCartMapper.mapToDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto clear(Long shoppingCartId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(shoppingCartId).orElseThrow(
                EntityNotFoundException.supplier("shopping cart is not found"));
        shoppingCart.getCartItems().stream()
                .forEach(item -> cartItemRepository.deleteById(item.getId()));
        return shoppingCartMapper.mapToDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartResponseDto findByUserId(Long userId) {
        ShoppingCart shoppingCartByUser
                = shoppingCartRepository.findByUserId(userId).orElseThrow(
                        EntityNotFoundException.supplier("shopping cart is not found"));
        return shoppingCartMapper.mapToDto(shoppingCartByUser);
    }
}