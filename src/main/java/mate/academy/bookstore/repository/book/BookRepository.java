package mate.academy.bookstore.repository.book;

import java.util.List;
import mate.academy.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("FROM Book b INNER JOIN FETCH b.categories c WHERE c.id = :category_id")
    List<Book> findBooksByCategoryId(@Param("category_id") Long categoryId);
}
