package mate.academy.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@Table(name = "cart_items")
@SQLDelete(sql = "UPDATE cart_items SET is_delete = true WHERE id = ?")
@Where(clause = "is_delete = false")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private boolean isDelete = false;
    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    @Override
    public int hashCode() {
        return Objects.hash(id, book, quantity, isDelete);
    }

}
