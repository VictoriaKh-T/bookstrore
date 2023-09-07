package mate.academy.bookstore.model.dto.shopingcart;

import lombok.Data;

@Data
public class CartItemResponseDto {
    private Long id;
    private Long bookId;
    private int quantity;
}
