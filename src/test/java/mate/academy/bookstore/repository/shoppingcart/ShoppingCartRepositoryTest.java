package mate.academy.bookstore.repository.shoppingcart;


import java.util.Optional;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
         "classpath:database/sql-shopping-cart/repository/delete-user.sql"},
executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("get shopping cart by user_id")
    @Test
    void findByUserId_ReturnShoppingCart() {
        Long userId = 3L;
        User user = new User();
        user.setId(userId);
        user.setEmail("user3@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("1234");
        user.setDelete(false);
        userRepository.save(user);

        Optional<ShoppingCart> result = shoppingCartRepository.findByUser(user);
        Assertions.assertTrue(result.isPresent());
        ShoppingCart shoppingCart = result.get();
        Assertions.assertNotNull(shoppingCart);
        Assertions.assertEquals(userId, shoppingCart.getUser().getId());
    }

    @DisplayName("find shopping cart bu user id and return empty")
    @Test
    public void testFindByUserId_WhenShoppingCartDoesNotExist_ShouldReturnEmptyOptional() {
        Optional<ShoppingCart> result = shoppingCartRepository.findByUser(null);
        Assertions.assertTrue(result.isEmpty());
    }

}