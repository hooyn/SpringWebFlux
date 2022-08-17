package twim.reactor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;
import twim.reactor.domain.Item;
import twim.reactor.repository.ItemReactiveRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class ServiceTest {

    @Autowired
    CartService carService;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public Long itemCnt;

    //테스트 시작 전 진행할 SetUp
    @BeforeEach
    void setUp(){
        //비동기로 처리를 위한 메서드 : StepVerifier
        StepVerifier.create(
                itemReactiveRepository.deleteAll()
        ).verifyComplete();

        //Item 데이터 입력
        Item item1 = new Item("legoC", "made in china", 20.00);
        Item item2 = new Item("legoK", "made in korea", 25.00);
        Item item3 = new Item("rc carU", "made in usa", 30.00);
        Item item4 = new Item("rc carUU", "made in usa", 40.00);
        Item item5 = new Item("droneK", "made in korea", 75.00);
        Item item6 = new Item("droneKK", "made in korea", 90.00);
        List<Item> itemList = Arrays.asList(item1, item2, item3, item4, item5, item6);

        //비동기로 생성한 Item DB에 저장
        itemCnt = Long.valueOf(itemList.size());
        StepVerifier.create(
                itemReactiveRepository.saveAll(itemList)
        ).expectNextMatches(item -> {
            System.out.println(item.toString());
            return true;
        }).expectNextCount(itemCnt - 1).verifyComplete();
    }

    @Test
    public void itemSearchNameT(){
        StepVerifier.create(
                carService.itemSearchName("droneK", "made in korea", true)
        ).expectNextMatches(item -> {
            assertThat(item.getId()).isNotNull();
            assertThat(item.getName()).isEqualTo("droneK");
            assertThat(item.getDescription()).isEqualTo("made in korea");
            assertThat(item.getPrice()).isEqualTo(75.00);
            return true;
        }).verifyComplete();
    }
}
