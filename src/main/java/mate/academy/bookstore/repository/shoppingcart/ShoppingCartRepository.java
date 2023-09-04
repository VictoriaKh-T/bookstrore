package mate.academy.bookstore.repository.shoppingcart;

import mate.academy.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long>,
        JpaSpecificationExecutor<ShoppingCart> {
    @Query("SELECT sc FROM ShoppingCart sc "
            + "LEFT JOIN FETCH sc.cartItems item "
            + "INNER JOIN FETCH sc.user u "
            + "INNER JOIN FETCH u.roles r "
            + "WHERE u.id = :userId")
    ShoppingCart findShoppingCartByUserId(@Param("userId") Long userId);
}
