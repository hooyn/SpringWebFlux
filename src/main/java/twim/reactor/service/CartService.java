package twim.reactor.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twim.reactor.domain.Cart;
import twim.reactor.domain.Item;

public interface CartService {
    public Flux<Item> itemSearchName(String name, String description, boolean isSuit);

    public Mono<Cart> delToCartCount(String cartId, String id);

    public Mono<Cart> delToCartAll(String cartId, String id);

    public Mono<Cart> addToCart(String cartId, String id);
}
