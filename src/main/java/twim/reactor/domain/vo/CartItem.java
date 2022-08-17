package twim.reactor.domain.vo;

import lombok.Data;
import twim.reactor.domain.Item;

@Data
public class CartItem {

    private Item item;
    private int quantity;

    public void increment(){
        this.quantity = this.quantity + 1;
    }

    public void decrement(){
        this.quantity = this.quantity - 1;
    }

    public boolean isOne(){
        if(this.quantity == 1){
            return true;
        } else return false;
    }
}
