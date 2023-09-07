package mate.academy.bookstore.model.dto.shopingcart;

import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartRequestDto {
    private Long userId;
    private Set<CartItemRequestDto> cartItems;
}
