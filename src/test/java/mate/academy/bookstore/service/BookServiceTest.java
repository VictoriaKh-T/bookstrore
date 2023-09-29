package mate.academy.bookstore.service;

import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.model.dto.book.BookDto;
import mate.academy.bookstore.model.dto.book.BookSearchParametersDto;
import mate.academy.bookstore.repository.book.BookRepository;
import mate.academy.bookstore.repository.book.BookSpecificationBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;
    private Book book;
    private BookDto bookDto;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @BeforeEach
    public void setup(){
        book = Book.builder()
                .id(1L)
                .author("Author test")
                .price(BigDecimal.valueOf(200))
                .title("Book test")
                .isbn("978-0547928227")
                .build();
        bookDto = BookDto.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .author(book.getAuthor())
                .title(book.getTitle())
                .build();
    }

    @DisplayName("JUnit for method findById")
    @Test
    public void findById_ShouldReturnValidBook() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.mapToDto(book)).thenReturn(bookDto);
        BookDto expectedDto = bookService.findById(book.getId());
        Assertions.assertEquals(book.getIsbn(), expectedDto.getIsbn());
        Mockito.verify(bookRepository, times(1)).findById(book.getId());
    }

    @DisplayName("JUnit for for method findById when book isn`t exist")
    @Test
    public void getBook_WithNonExistingBookId() {
        Long bookId = 100L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(bookId)
        );
        String expected = "can`t find book by id " + bookId;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("JUnit test for get all books")
    @Test
    void findAll_ReturnBookList() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title"));
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);
        when(bookRepository.findAll(pageable)).thenReturn((bookPage));
        when(bookMapper.mapToDto(book)).thenReturn(bookDto);
        List<BookDto> bookList = bookService.findAll(pageable);
        Assertions.assertEquals(1, bookList.size());
    }

    @DisplayName("JUnit test for getting books by category")
    @Test
    void findAllByCategoryId_ReturnList() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setName("Test Name");
        category.setId(categoryId);
        category.setDescription("some description");
        book.setCategories(Set.of(category));
        when(bookRepository.findBooksByCategoryId(categoryId)).thenReturn(List.of(book));
        List<Book> bookList = bookService.findBooksByCategoryId(categoryId);
        Assertions.assertEquals(1, bookList.size());
    }

    @DisplayName("JUnit test for searching books by search parameters")
    @Test
    void testSearch_ReturnListBookDto() {
        BookSearchParametersDto searchParameters = new BookSearchParametersDto(
                new String[]{},
                new String[]{"Author test"},
                new String[]{});

        Specification<Book> bookSpecification = mock(Specification.class);
        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(bookSpecification);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("title"));
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);
        when(bookRepository.findAll(bookSpecification, pageable)).thenReturn(bookPage);

        when(bookMapper.mapToDto(book)).thenReturn(bookDto);

        List<BookDto> result = bookService.search(searchParameters, pageable);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("978-0547928227", result.get(0).getIsbn());

        verify(bookRepository, times(1)).findAll(bookSpecification, pageable);
        verify(bookMapper, times(1)).mapToDto(book);
    }

    @DisplayName("JUnit test for searching books with incorrect params return empty")
    @Test
    void testSearch_ReturnEmptyListBookDto() {
        BookSearchParametersDto searchParameters = new BookSearchParametersDto(
                new String[]{},
                new String[]{"Author"},
                new String[]{});

        Specification<Book> bookSpecification = mock(Specification.class);
        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(bookSpecification);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("title"));
        Page<Book> bookPage = new PageImpl<>(new ArrayList<>(), pageable, 1);
        when(bookRepository.findAll(bookSpecification, pageable)).thenReturn(bookPage);

        List<BookDto> result = bookService.search(searchParameters, pageable);
        Assertions.assertEquals(0, result.size());
    }
}