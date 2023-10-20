package mate.academy.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.order.OrderDto;
import mate.academy.bookstore.model.dto.order.OrderItemDto;
import mate.academy.bookstore.model.dto.order.OrderRequestDto;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    protected MockMvc mockMvc;

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
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/sql-order/controller"
                            + "/add-order.sql")
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
                    new ClassPathResource("database/sql-shopping-cart/controller/delete-data.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/sql-order/controller"
                            + "/delete-order.sql")
            );
        }
    }

    @Test
    void createOrder_ReturnOrderDto() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("user3@example.com");

        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setShippingAddress("test address");

        Authentication authentication = getAuthorisation(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestDto);

        OrderDto response = getOrderByUser(user);

        MvcResult result = mockMvc.perform(
                        post("/orders")
                                .with(authentication(authentication))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andReturn();

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        OrderDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                OrderDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(response.getOrderItems().size(), actual.getOrderItems().size());
    }

    @Test
    @DisplayName("test method getOrderHistory return list of orders")
    void getOrderHistory_ReturnOk() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("user3@example.com");

        Authentication authentication = getAuthorisation(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        OrderDto response = getOrderByUser(user);
        List<OrderDto> responsesList = new ArrayList<>();
        responsesList.add(response);

        MvcResult result = mockMvc.perform(get("/orders")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        List<OrderDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, OrderDto.class)
        );

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(responsesList.size(), actual.size());
    }

    private Authentication getAuthorisation(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(
                user, "password",authorities);
    }

    @NotNull
    private static OrderDto getOrderByUser(User user) {
        OrderItemDto orderItemDto1 = new OrderItemDto();
        orderItemDto1.setId(1L);
        orderItemDto1.setBookId(1L);
        orderItemDto1.setQuantity(10);

        OrderItemDto orderItemDto2 = new OrderItemDto();
        orderItemDto2.setId(2L);
        orderItemDto2.setBookId(2L);
        orderItemDto2.setQuantity(15);

        OrderItemDto orderItemDto3 = new OrderItemDto();
        orderItemDto3.setId(3L);
        orderItemDto3.setBookId(3L);
        orderItemDto3.setQuantity(10);

        OrderDto orderDto = new OrderDto();
        orderDto.setOrderItems(List.of(orderItemDto1, orderItemDto2, orderItemDto3));
        orderDto.setUserId(user.getId());
        orderDto.setId(1L);
        return orderDto;
    }
}