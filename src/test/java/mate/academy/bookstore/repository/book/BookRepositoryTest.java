package mate.academy.bookstore.repository.book;

import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.category.CategoryRepository;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {
        "classpath:database/sql/insert-book.sql",
        "classpath:database/sql/insert-category.sql",
        "classpath:database/sql/add-book-to-category.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/sql/delete-book.sql",
        "classpath:database/sql/delete-category.sql",
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Find all books by category")
    public void findAllBooksByCategory_ReturnEmpty() {
        Category category = categoryRepository.findById(8L).get();
        Assertions.assertNotNull(category);
        List<Book> books = bookRepository.findBooksByCategoryId(category.getId());
        Assertions.assertTrue(books.isEmpty());
    }

    @Test
    @DisplayName("Find all books by category")
    public void findAllBooksByCategory() {
        Category category = categoryRepository.findById(6L).get();
        Assertions.assertNotNull(category);
        List<Book> books = bookRepository.findBooksByCategoryId(category.getId());
        Assertions.assertEquals(2,books.size());
    }
}