package mate.academy.bookstore.repository.book.spec;

import jakarta.persistence.criteria.Join;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CategorySpecificationProvider implements SpecificationProvider {
    @Override
    public String getKey() {
        return "categories";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, Category> categoryJoin = root.join("categories");
            return categoryJoin.get("id").in((Object[]) params);
        };
    }
}
