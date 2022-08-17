package twim.reactor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import twim.reactor.domain.vo.CartItem;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Cart {

    @Id
    private String id;
    private List<CartItem> cartItems;

    public Cart(String id) {
        this(id, new ArrayList<CartItem>());
    }

    public void removeItem(CartItem cartItem){
        this.cartItems.remove(cartItem);
    }
}
