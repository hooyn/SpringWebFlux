package twim.reactor.domain.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import twim.reactor.domain.Cart;

public interface CartReactiveRepository extends ReactiveCrudRepository<Cart, String> {
}
