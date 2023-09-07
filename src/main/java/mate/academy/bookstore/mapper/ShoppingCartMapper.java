package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.dto.shopingcart.ShoppingCartResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    @Mapping(source = "user", target = "userId", qualifiedByName = "mapUserToUserId")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartResponseDto mapToDto(ShoppingCart shoppingCart);

    @Named("mapUserToUserId")
    default Long mapUserToUserId(User user) {
        return user.getId();
    }
}
