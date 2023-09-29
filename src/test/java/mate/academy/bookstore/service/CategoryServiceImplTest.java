package mate.academy.bookstore.service;

import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CategoryMapper;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.model.dto.category.CategoryDto;
import mate.academy.bookstore.model.dto.category.CategoryRequestDto;
import mate.academy.bookstore.repository.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    private Category category;
    private CategoryDto categoryDto;
    private CategoryRequestDto categoryRequestDto;

    @BeforeEach
    public void setup(){
        Long categoryId = 1L;
        category = new Category();
        category.setName("Test Name");
        category.setId(categoryId);
        category.setDescription("some description");

        categoryDto = new CategoryDto();
        categoryDto.setDescription(category.getDescription());
        categoryDto.setName(category.getName());
        categoryDto.setId(categoryId);

    }

    @DisplayName("JUnit for method findAll")
    @Test
    void findAll_ReturnList() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Category> categoryPage = new PageImpl<>(List.of(category), pageable, 1);
        when(categoryRepository.findAll(pageable)).thenReturn((categoryPage));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        List<CategoryDto> categoryList = categoryService.findAll(pageable);
        Assertions.assertEquals(1, categoryList.size());
    }

    @DisplayName("JUnit for method getById")
    @Test
    void getById_ReturnCategory() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        CategoryDto expectedDto = categoryService.getById(category.getId());
        Assertions.assertEquals(category.getId(), expectedDto.getId());
    }
    @DisplayName("JUnit for method getById when return exception")
    @Test
    void getById_ReturnEntityError() {
        Long categoryId = 100L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(categoryId)
        );
        String expected = "can`t find category by id " + categoryId;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void saveCategoryReturnCategoryDto() {
        categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName(category.getName());
        categoryRequestDto.setDescription(category.getDescription());

        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);

        CategoryDto result = categoryService.save(categoryRequestDto);

        Assertions.assertNotNull(result);
    }
}