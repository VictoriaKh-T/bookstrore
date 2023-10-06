package mate.academy.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.shopingcart.CartItemResponseDto;
import mate.academy.bookstore.model.dto.shopingcart.ShoppingCartResponseDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class ShoppingCartControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void getShoppingCart_IsOk() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("user3@example.com");

        ShoppingCartResponseDto shoppingCartDto = getShoppingCartResponseDto(user);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, "password",authorities);

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