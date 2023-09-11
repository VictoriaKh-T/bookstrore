package mate.academy.bookstore.model.dto.order;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemRequestDto {
    private Long bookId;
    private int quantity;
    private BigDecimal price;
}
