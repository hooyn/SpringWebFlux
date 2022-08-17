package twim.reactor;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoOperations;
import twim.reactor.domain.Item;

@SpringBootApplication
@RequiredArgsConstructor
public class ReactorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactorApplication.class, args);
	}

	private final MongoOperations mongoOperations;

	@EventListener(ApplicationReadyEvent.class)
	public void doSomething(){
		Item item1 = new Item("legoC", "made in china", 20.00);
		Item item2 = new Item("legoK", "made in korea", 25.00);
		Item item3 = new Item("rc carU", "made in usa", 30.00);
		Item item4 = new Item("rc carUU", "made in usa", 40.00);
		Item item5 = new Item("droneK", "made in korea", 75.00);
		Item item6 = new Item("droneKK", "made in korea", 90.00);
		mongoOperations.save(item1);
		mongoOperations.save(item2);
		mongoOperations.save(item3);
		mongoOperations.save(item4);
		mongoOperations.save(item5);
		mongoOperations.save(item6);
	}
}
