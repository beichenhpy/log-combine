package cn.beichenhpy.sample;

import cn.beichenhpy.log.annotation.EnableLogCombine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <PRE>
 *
 * </PRE>
 * CREATE_TIME: 2022/4/23 16:44
 *
 * @author beichenhpy
 * @version 1.0.0
 */
@EnableLogCombine
@SpringBootApplication
public class SampleEnter {
    public static void main(String[] args) {
        SpringApplication.run(SampleEnter.class, args);
    }
}
