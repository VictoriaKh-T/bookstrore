package mate.academy.bookstore.model.dto.book;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookDtoWithoutCategoryIds {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;
}
