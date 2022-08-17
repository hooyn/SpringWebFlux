package twim.reactor.domain.repository;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import twim.reactor.domain.Item;

public interface ItemReactiveRepository extends ReactiveCrudRepository<Item, String>, ReactiveQueryByExampleExecutor<Item> {
    //복수
    Flux<Item> findByNameContaining(String itemName);

    //단수
    Mono<Item> findByName(String name);
}
