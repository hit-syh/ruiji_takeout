package syh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@MapperScan("syh.mapper")
public class RejiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RejiApplication.class,args);
    }

}
