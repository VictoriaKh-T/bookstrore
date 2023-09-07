package mate.academy.bookstore.model.dto.shopingcart;

import lombok.Data;

@Data
public class CartItemRequestDto {
    private Long bookId;
    private int quantity;
}
