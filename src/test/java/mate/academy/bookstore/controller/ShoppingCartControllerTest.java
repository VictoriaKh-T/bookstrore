package mate.academy.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.shopingcart.CartItemRequestDto;
import mate.academy.bookstore.model.dto.shopingcart.CartItemResponseDto;
import mate.academy.bookstore.model.dto.shopingcart.ShoppingCartResponseDto;
import mate.academy.bookstore.repository.book.BookRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ShoppingCartControllerTest {

    @Autowired
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    BookRepository bookRepository;

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
                            + "/add-data.sql")
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
    @DisplayName("test method getShoppingCart")
    void getShoppingCart_IsOk() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("user3@example.com");

        ShoppingCartResponseDto shoppingCartDto = getShoppingCartResponseDto(user);
        Authentication authentication = getAuthorisation(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MvcResult result = mockMvc.perform(get("/cart")
                                .with(authentication(authentication))
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
    @DisplayName("test method addItem ")
    @Sql(scripts = {"classpath:database/sql-shopping-cart/controller/insert-new-book.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(scripts = {"classpath:database/sql-shopping-cart/controller/"
                    + "delete-cart-item-by-book_id.sql",
            "classpath:database/sql-shopping-cart/controller/delete-book.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void addItem_To_ShoppingCart_ReturnOk() throws Exception {

       Book book = Book.builder()
                .id(5L)
                .author("Author test")
                .price(BigDecimal.valueOf(200))
                .title("Book test")
                .isbn("978-1400067885")
                .build();
       when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        User user = new User();
        user.setId(1L);
        user.setEmail("user3@example.com");

        ShoppingCartResponseDto shoppingCartDto = getShoppingCartResponseDto(user);
        Authentication authentication = getAuthorisation(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CartItemRequestDto requestDto = new CartItemRequestDto();
        requestDto.setBookId(5L);
        requestDto.setQuantity(15);

        CartItemResponseDto cartItemDto4 = new CartItemResponseDto();
        cartItemDto4.setBookId(book.getId());
        cartItemDto4.setQuantity(15);
        cartItemDto4.setId(4L);

        Set<CartItemResponseDto> cartItems = new HashSet<>(shoppingCartDto.getCartItems());
        cartItems.add(cartItemDto4);
        shoppingCartDto.setCartItems(cartItems);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestDto);


        MvcResult result = mockMvc.perform(
                post("/cart")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isCreated())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ShoppingCartResponseDto.class);

        Assertions.assertNotNull(actual);
        assertThat(actual.getCartItems())
                .containsExactlyInAnyOrder(shoppingCartDto.getCartItems().toArray(new CartItemResponseDto[0]));

    }

    @Test
    @DisplayName("test method updateItem ")
    void updateItem_ReturnOk() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setEmail("user3@example.com");

        ShoppingCartResponseDto shoppingCartDto = getShoppingCartResponseDto(user);
        Authentication authentication = getAuthorisation(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CartItemRequestDto requestDto = new CartItemRequestDto();
        requestDto.setQuantity(2);

        for (CartItemResponseDto c: shoppingCartDto.getCartItems()) {
            if (c.getId().equals(1L)) {
                c.setQuantity(requestDto.getQuantity());
                break;
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        put("/cart/cart-items/{cartItemId}", 1L)
                                .with(authentication(authentication))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ShoppingCartResponseDto.class);

        Assertions.assertNotNull(actual);
        assertThat(actual.getCartItems())
                .containsExactlyInAnyOrder(shoppingCartDto.getCartItems().toArray(new CartItemResponseDto[0]));

    }


    private Authentication getAuthorisation(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(
                user, "password",authorities);
    }

    @NotNull
    private static ShoppingCartResponseDto getShoppingCartResponseDto(User user) {
        CartItemResponseDto cartItemDto1 = new CartItemResponseDto();
        cartItemDto1.setBookId(1L);
        cartItemDto1.setQuantity(10);
        cartItemDto1.setId(1L);

        CartItemResponseDto cartItemDto2 = new CartItemResponseDto();
        cartItemDto2.setBookId(2L);
        cartItemDto2.setQuantity(10);
        cartItemDto2.setId(2L);

        CartItemResponseDto cartItemDto3 = new CartItemResponseDto();
        cartItemDto3.setBookId(4L);
        cartItemDto3.setQuantity(10);
        cartItemDto3.setId(3L);

        ShoppingCartResponseDto shoppingCartDto = new ShoppingCartResponseDto();
        shoppingCartDto.setCartItems(Set.of(cartItemDto1, cartItemDto2, cartItemDto3));
        shoppingCartDto.setUserId(user.getId());
        shoppingCartDto.setId(1L);
        return shoppingCartDto;
    }
}