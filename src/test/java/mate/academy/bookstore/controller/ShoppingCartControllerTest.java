package mate.academy.bookstore.controller;

import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockJwtAuth;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.shopingcart.CartItemRequestDto;
import mate.academy.bookstore.model.dto.shopingcart.CartItemResponseDto;
import mate.academy.bookstore.model.dto.shopingcart.ShoppingCartResponseDto;
import mate.academy.bookstore.service.ShoppingCartService;
import mate.academy.bookstore.service.ShoppingCartServiceImpl;
import mate.academy.bookstore.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockJwtAuth(authorities = { "admin", "ROLE_ADMIN" },
        claims = @OpenIdClaims(preferredUsername = "user1@example.com"))
class ShoppingCartControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static User user;
    private static Book book;
    private static ShoppingCart shoppingCart;
    private static CartItem cartItem;
    private static ShoppingCartResponseDto shoppingCartDto;
    private static CartItemResponseDto cartItemDto;
    private static CartItemRequestDto cartItemRequestDto;

    @BeforeAll
    static void setup(){
        user = new User();
        user.setId(1L);
        user.setEmail("user1@example.com");
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
    @BeforeEach
    void beforeAll(
            @Autowired WebApplicationContext applicationContext,
            @Autowired DataSource dataSource
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/sql-shopping-cart/controller"
                            + "/add-cartitem-to-shoppingcart.sql")
            );
        }
    }

    @AfterEach
    void afterEach(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown (DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/sql-shopping-cart/controller/delete-user.sql")
            );
        }
    }


    @Test
    void getShoppingCart() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/cart")
                                .with(jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ShoppingCartResponseDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(shoppingCartDto, actual);

    }

    @Test
    void addItem() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void deleteItemFromCart() {
    }

    @Test
    void deleteAllItemsFromCart() {
    }

    @Test
    void getUserByAuth() {
    }
}