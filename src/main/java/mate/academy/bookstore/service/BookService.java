package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.model.dto.BookDto;
import mate.academy.bookstore.model.dto.BookSearchParametersDto;
import mate.academy.bookstore.model.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto bookRequestDto);

    List<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable);
}
