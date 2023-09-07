package mate.academy.bookstore.exception;

import java.util.function.Supplier;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String message) {
        super(message);
    }

    public static Supplier<BookAlreadyExistsException> supplier(String message) {
        return () -> new BookAlreadyExistsException(message);
    }
}
