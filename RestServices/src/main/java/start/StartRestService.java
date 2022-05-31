package start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@ComponentScan({"app", "repo"})
@SpringBootApplication
public class StartRestService {
    public static void main(String[] args) {
        SpringApplication.run(StartRestService.class, args);
    }

    @Bean(name="props")
    public Properties getBdProperties(){
        Properties props = new Properties();
        try {
            System.out.println("Searching db.config in directory "+((new File(".")).getAbsolutePath()));
            props.load(new FileReader("db.config"));
        } catch (IOException e) {
            System.err.println("Configuration file db.config not found" + e);
        }
        return props;
    }
}
