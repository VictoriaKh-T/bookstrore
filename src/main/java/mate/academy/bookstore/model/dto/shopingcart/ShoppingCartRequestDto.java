package mate.academy.bookstore.model.dto.shopingcart;

import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShoppingCartRequestDto {
    private Long userId;
    private Set<CartItemRequestDto> cartItems;
}
