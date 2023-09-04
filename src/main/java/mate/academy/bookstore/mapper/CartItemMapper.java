package mate.academy.bookstore.mapper;

import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.dto.shopingcart.CartItemRequestDto;
import mate.academy.bookstore.model.dto.shopingcart.CartItemResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {

    @Mapping(target = "bookId", source = "book", qualifiedByName = "mapBookToId")
    CartItemResponseDto mapToDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem mapToModel(CartItemRequestDto requestDto);

    @Named("mapBookToId")
    default Long mapBookToId(Book book) {
        return book.getId();
    }
}
