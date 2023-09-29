package mate.academy.bookstore.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import javax.sql.DataSource;
import mate.academy.bookstore.model.dto.book.BookDto;
import mate.academy.bookstore.model.dto.book.CreateBookRequestDto;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(username = "admin", roles = {"ADMIN"})
class BookControllerTest {
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
                    new ClassPathResource("database/sql-book/add-three-books.sql")
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
                    new ClassPathResource("database/sql-book/delete-all-books.sql")
            );
        }
    }

    @Test
    @DisplayName("get all books")
    void findAll_ReturnAllBooks() throws Exception {
        BookDto bookDto1 = BookDto.builder().id(1L).price(BigDecimal.valueOf(200))
                .title("To Kill a Mockingbird").isbn("978-0061120084").author("Harper Lee")
                .build();
        BookDto bookDto2 = BookDto.builder().id(2L).price(BigDecimal.valueOf(120))
                .title("The Great Gatsby").isbn("978-0743273565").author("F. Scott Fitzgerald")
                .build();
        BookDto bookDto3 = BookDto.builder().id(3L).price(BigDecimal.valueOf(400))
                .title("The Hobbit").isbn("978-0547928227").author("J.R.R. Tolkien")
                .build();

        List<BookDto> expected = new ArrayList<>();
        expected.add(bookDto1);
        expected.add(bookDto2);
        expected.add(bookDto3);

        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto[] actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto[].class);
        Assertions.assertEquals(3, actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());

    }

    @Test
    void getBookById_ReturnBook() throws Exception {
        BookDto expected = BookDto.builder().id(1L).price(BigDecimal.valueOf(200))
                .title("To Kill a Mockingbird").isbn("978-0061120084").author("Harper Lee")
                .build();
        MvcResult result = mockMvc.perform(
                        get("/books/{id}", expected.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @DisplayName("create new book")
    @Sql(
            scripts = "classpath:database/sql-book/delete-book-by-isn.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setIsbn("978-0590353427");
        bookRequestDto.setPrice(BigDecimal.valueOf(100));
        bookRequestDto.setTitle("Book1");
        bookRequestDto.setAuthor("Author test");

        BookDto expected = BookDto.builder()
                .isbn(bookRequestDto.getIsbn())
                .author(bookRequestDto.getAuthor())
                .title(bookRequestDto.getTitle())
                .build();

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult result = mockMvc.perform(
                post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @DisplayName("update book")
    @Test
    void updateBook_ReturnBookDto() throws Exception {
        CreateBookRequestDto updatedBook = new CreateBookRequestDto();
        updatedBook.setPrice(BigDecimal.valueOf(200));
        updatedBook.setAuthor("J. Rowling");
        updatedBook.setIsbn("978-0590353427");
        updatedBook.setTitle("Harry Potter");
        updatedBook.setCategoryIds(new ArrayList<>());

        MvcResult result = mockMvc.perform(
                        put("/books/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedBook))
                )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        Assertions.assertEquals(actual.getTitle(), updatedBook.getTitle());
    }
}