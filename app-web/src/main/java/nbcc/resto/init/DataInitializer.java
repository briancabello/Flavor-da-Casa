package nbcc.resto.init;

import nbcc.resto.service.InitializeDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final InitializeDataService initializeDataService;

    public DataInitializer(InitializeDataService initializeDataService) {
        this.initializeDataService = initializeDataService;
    }

    @Override
    public void run(String... args) {
        var result = initializeDataService.initialize();

        if (result.isSuccessful()) {
            logger.info("Data initialization completed successfully");
        } else {
            logger.error("Data initialization failed");
        }
    }
}
