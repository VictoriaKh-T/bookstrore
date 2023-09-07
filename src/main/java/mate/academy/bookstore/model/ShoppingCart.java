package mate.academy.bookstore.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.Set;
import lombok.Data;

@Entity
@Data
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    private Long id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "shoppingCart", cascade = CascadeType.REMOVE)
    private Set<CartItem> cartItems;

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }
}
