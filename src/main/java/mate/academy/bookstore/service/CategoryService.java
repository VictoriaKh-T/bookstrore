package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.model.dto.category.CategoryDto;
import mate.academy.bookstore.model.dto.category.CategoryRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryRequestDto categoryRequestDto);

    CategoryDto update(Long id, CategoryRequestDto categoryRequestDto);

    void deleteById(Long id);

}
