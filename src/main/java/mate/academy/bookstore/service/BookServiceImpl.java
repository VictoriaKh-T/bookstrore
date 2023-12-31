package mate.academy.bookstore.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.model.dto.book.BookDto;
import mate.academy.bookstore.model.dto.book.BookSearchParametersDto;
import mate.academy.bookstore.model.dto.book.CreateBookRequestDto;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.book.BookSpecificationBuilder;
import mate.academy.bookstore.repository.category.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.mapToModel(requestDto);
        return bookMapper.mapToDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::mapToDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                    .map(bookMapper::mapToDto)
                    .orElseThrow(() -> new EntityNotFoundException("can`t find book by id " + id));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("can`t find book by id" + id));
        book.setPrice(bookRequestDto.getPrice());
        book.setTitle(bookRequestDto.getTitle());
        book.setAuthor(bookRequestDto.getAuthor());
        book.setIsbn(bookRequestDto.getIsbn());
        book.setCoverImage(bookRequestDto.getCoverImage());
        book.setDescription(bookRequestDto.getDescription());
        Set<Category> collect = bookRequestDto.getCategoryIds().stream()
                .map(categoryRepository::findById)
                .map(Optional::orElseThrow)
                .collect(Collectors.toSet());
        book.setCategories(collect);
        return bookMapper.mapToDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification, pageable)
                .stream()
                .map(bookMapper::mapToDto)
                .toList();
    }

    @Override
    public List<Book> findBooksByCategoryId(Long id) {
        return bookRepository.findBooksByCategoryId(id);
    }
}
