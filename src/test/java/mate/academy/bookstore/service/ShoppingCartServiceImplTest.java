package mate.academy.bookstore.service;

import mate.academy.bookstore.exception.BookAlreadyExistsException;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.shopingcart.CartItemRequestDto;
import mate.academy.bookstore.model.dto.shopingcart.CartItemResponseDto;
import mate.academy.bookstore.model.dto.shopingcart.ShoppingCartRequestDto;
import mate.academy.bookstore.model.dto.shopingcart.ShoppingCartResponseDto;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.shoppingcart.CartItemRepository;
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
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private User user;
    private Book book;
    private ShoppingCart shoppingCart;
    private CartItem cartItem;
    private ShoppingCartResponseDto shoppingCartDto;
    private CartItemResponseDto cartItemDto;
    private CartItemRequestDto cartItemRequestDto;
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

        cartItemDto = new CartItemResponseDto();
        cartItemDto.setBookId(book.getId());
        cartItemDto.setQuantity(10);
        cartItemDto.setId(cartItem.getId());

        cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(book.getId());
        cartItemRequestDto.setQuantity(10);

        shoppingCartDto = new ShoppingCartResponseDto();
        shoppingCartDto.setCartItems(Set.of(cartItemDto));
        shoppingCartDto.setUserId(user.getId());
        shoppingCartDto.setId(shoppingCart.getId());

    }

    @DisplayName("add cart item success")
    @Test
    void addCartItem_ReturnOk() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository
                .findByShoppingCartIdAndBookId(shoppingCart.getId(), book.getId())).thenReturn(
                        Optional.empty());

        when(shoppingCartMapper.mapToDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartResponseDto result
                = shoppingCartService.addCartItem(cartItemRequestDto, user.getId());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result, shoppingCartDto);
        Mockito.verify(bookRepository, times(1)).findById(book.getId());
        Mockito.verify(userRepository, times(1)).findById(user.getId());
        Mockito.verify(shoppingCartRepository, times(1)).findByUser(user);
    }

    @DisplayName("JUnit for method findById when return exception")
    @Test
    void findByUserId_ReturnEntityError() {
        Long userId = 100L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.findByUserId(userId)
        );
        String expected = "User not found";
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void updateCartItem() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(cartItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));

        CartItemRequestDto itemRequestDto = new CartItemRequestDto();
        itemRequestDto.setQuantity(25);
        itemRequestDto.setBookId(cartItem.getBook().getId());

        CartItemResponseDto cartItemDto = new CartItemResponseDto();
        cartItemDto.setId(cartItem.getId());
        cartItemDto.setQuantity(25);
        cartItemDto.setBookId(cartItemDto.getBookId());

        shoppingCartDto = new ShoppingCartResponseDto();
        shoppingCartDto.setCartItems(Set.of(cartItemDto));
        shoppingCartDto.setUserId(user.getId());
        shoppingCartDto.setId(shoppingCart.getId());

        when(shoppingCartMapper.mapToDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartResponseDto result = shoppingCartService.updateCartItem(itemRequestDto,
                cartItem.getId(), user.getId());

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getCartItems().contains(cartItemDto));

        Mockito.verify(userRepository, times(1)).findById(user.getId());
        Mockito.verify(cartItemRepository, times(1))
                .findById(cartItem.getId());
    }

}