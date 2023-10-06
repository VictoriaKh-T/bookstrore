package mate.academy.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mate.academy.bookstore.model.dto.shopingcart.ShoppingCartResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
class ShoppingCartControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static ShoppingCartResponseDto shoppingCartDto;

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

       /* shoppingCartDto = new ShoppingCartResponseDto();
        shoppingCartDto.setCartItems(new HashSet<>());
        shoppingCartDto.setUserId(3L);
        shoppingCartDto.setId(3L);*/

        Authentication auth = mock(Authentication.class);
        MvcResult result = mockMvc.perform(
                        get("/cart")
                                .contentType(MediaType.APPLICATION_JSON)
                                .principal(auth)
                )
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartResponseDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                ShoppingCartResponseDto.class);

        Assertions.assertNotNull(actual);
        //Assertions.assertEquals(shoppingCartDto, actual);

    }

}