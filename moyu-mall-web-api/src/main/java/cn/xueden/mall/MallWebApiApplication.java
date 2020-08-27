package cn.xueden.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Auther:梁志杰
 * @Date:2020/8/27
 * @Description:cn.xueden.mall
 * @version:1.0
 */
@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
@EnableSwagger2
public class MallWebApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallWebApiApplication.class, args);
    }


}
