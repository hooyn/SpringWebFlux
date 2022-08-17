package twim.reactor.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;
import twim.reactor.domain.Item;
import twim.reactor.domain.repository.CartReactiveRepository;
import twim.reactor.domain.repository.ItemReactiveRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class RepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Autowired
    CartReactiveRepository cartReactiveRepository;

    public Long itemCnt;

    @BeforeEach
    void setUp(){
        //비동기로 처리 : StepVerifier
        StepVerifier.create(
                itemReactiveRepository.deleteAll()
        ).verifyComplete();

        Item item1 = new Item("lego", "made in china", 20.00);
        Item item2 = new Item("lego", "made in korea", 25.00);
        Item item3 = new Item("rc car", "made in usa", 30.00);
        Item item4 = new Item("rc car", "made in usa", 40.00);
        Item item5 = new Item("drone", "made in korea", 75.00);
        Item item6 = new Item("drone", "made in korea", 90.00);
        List<Item> itemList = Arrays.asList(item1, item2, item3, item4, item5, item6);

        itemCnt = Long.valueOf(itemList.size());
        StepVerifier.create(
                itemReactiveRepository.saveAll(itemList)
        ).expectNextMatches(item -> {
            System.out.println(item.toString());
            return true;
        }).expectNextCount(itemCnt - 1).verifyComplete();
    }

    @Test
    public void itemSearchName(){
        StepVerifier.create(
                itemReactiveRepository.findByNameContaining("rc")
        ).expectNextMatches(item -> {
            System.out.println(item.toString());
            return true;
        }).expectNextCount(1).verifyComplete();
    }

    @Test
    public void itemRepositoryCount(){
        StepVerifier.create(
                itemReactiveRepository.findAll().count()
        ).expectNextMatches(cnt ->{
            assertThat(cnt).isEqualTo(itemCnt);
            return true;
        }).verifyComplete();
    }

    @Test
    public void itemRepositoryRcCount(){
        StepVerifier.create(
                itemReactiveRepository.findByNameContaining("rc").count()
        ).expectNextMatches(cnt ->{
            assertThat(cnt).isEqualTo(2);
            return true;
        }).verifyComplete();
    }
}
