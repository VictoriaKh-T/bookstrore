package mate.academy.bookstore.repository.shoppingcart;

import java.util.Optional;
import mate.academy.bookstore.model.CartItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {
        "classpath:database/sql-shopping-cart/repository/add-users-and-shopping-cart.sql",
        "classpath:database/sql-shopping-cart/repository/add-cartitem-to-shoppingcart.sql"

}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/sql-shopping-cart/repository/delete-shoppingcart.sql",
         "classpath:database/sql-shopping-cart/repository/delete-user.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CartItemRepositoryTest {

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Test
    void findByShoppingCartIdAndBookId_isPresent() {
        Long bookId = 1L;
        Long shoppingCartId = 3L;
        Optional<CartItem> result = cartItemRepository.findByShoppingCartIdAndBookId(shoppingCartId,
                bookId);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    void findCartItemByShoppingCartId() {
        Optional<CartItem> foundCartItem
                = cartItemRepository.findByShoppingCartIdAndBookId(2L, 2L);
        Assertions.assertTrue(foundCartItem.isEmpty());
    }
}