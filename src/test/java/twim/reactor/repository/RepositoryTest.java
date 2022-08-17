package twim.reactor.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;
import twim.reactor.domain.Item;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//테스트 할 때 자동으로 MongoDB를 설정을 해주는 설정
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class RepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Autowired
    CartReactiveRepository cartReactiveRepository;

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
    public void itemSearchName(){
        StepVerifier.create(
                itemReactiveRepository.findByNameContaining("rc")
        ).expectNextMatches(item -> { //찾은 항목들을 하나씩 꺼내서 검사
            System.out.println(item.toString());
            return true;
        }).expectNextCount(1) //마지막 Item의 Index가 1이다. 즉 Item의 개수가 2개이다.
                .verifyComplete();
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

    @Test
    public void itemSearchNameDrone(){
        StepVerifier.create(
                itemReactiveRepository.findByName("droneK")
        ).expectNextMatches(item -> {
            assertThat(item.getId()).isNotNull();
            assertThat(item.getName()).isEqualTo("droneK");
            assertThat(item.getDescription()).isEqualTo("made in korea");
            assertThat(item.getPrice()).isEqualTo(75.00);
            return true;
        }).verifyComplete();
    }
}
