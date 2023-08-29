package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.dto.book.BookDto;
import mate.academy.bookstore.model.dto.book.CreateBookRequestDto;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto mapToDto(Book book);

    Book mapToModel(CreateBookRequestDto bookDto);
}
