package twim.reactor.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twim.reactor.domain.Cart;
import twim.reactor.domain.Item;
import twim.reactor.domain.vo.CartItem;
import twim.reactor.repository.CartReactiveRepository;
import twim.reactor.repository.ItemReactiveRepository;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
@RequiredArgsConstructor
public class CartServiceOrigin implements CartService {

    private final ItemReactiveRepository itemReactiveRepository;
    private final CartReactiveRepository cartReactiveRepository;

    @Override
    public Flux<Item> itemSearchName(String name, String description, boolean isSuit) {

        Item item = new Item(name, description, 0.0);

        ExampleMatcher matcher = (isSuit
                ? ExampleMatcher.matchingAll().withIgnorePaths("price")
                : ExampleMatcher.matching()
                .withMatcher("name", contains())
                .withMatcher("description", contains())
                .withIgnorePaths("price"));

        Example<Item> itemExample = Example.of(item, matcher);
        return itemReactiveRepository.findAll(itemExample);
    }

    @Override
    public Mono<Cart> delToCartCount(String cartId, String id) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart ->
                    cart.getCartItems().stream()
                            .filter(cartItem -> cartItem.getItem().getId().equals(id)).findAny()
                            .map(cartItem -> {
                                if(cartItem.isOne()){
                                    cart.removeItem(cartItem);
                                } else {
                                    cartItem.decrement();;
                                }
                                return Mono.just(cart);
                            }).orElseGet(() ->{
                                return Mono.empty();
                            })).flatMap(cart -> cartReactiveRepository.save(cart));
    }

    @Override
    public Mono<Cart> delToCartAll(String cartId, String id) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart ->
                        cart.getCartItems().stream()
                                .filter(cartItem -> cartItem.getItem().getId().equals(id)).findAny()
                                .map(cartItem -> {
                                    cart.removeItem(cartItem);
                                    return Mono.just(cart);
                                }).orElseGet(()->{
                                    return Mono.empty();
                                })).flatMap(cart -> cartReactiveRepository.save(cart));
    }

    @Override
    public Mono<Cart> addToCart(String cartId, String id) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(cart ->
                        cart.getCartItems().stream()
                                .filter(cartItem -> cartItem.getItem().getId().equals(id)).findAny()
                                .map(cartItem -> {
                                    cartItem.increment();
                                    return Mono.just(cart);
                                }).orElseGet(()-> itemReactiveRepository.findById(id)
                                .map(cartItem -> new CartItem(cartItem)).doOnNext(cartItem -> cart.getCartItems().add(cartItem))
                                        .map(cartItem -> cart))).flatMap(cart -> cartReactiveRepository.save(cart));
    }
}
