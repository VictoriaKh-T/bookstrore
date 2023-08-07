package mate.academy.bookstore.repository;

import java.util.List;
import mate.academy.bookstore.model.Book;

public interface BookRepository {

    Book save(Book product);

    List<Book> findAll();

}
