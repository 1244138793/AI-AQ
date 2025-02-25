package com.aiaq.config;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 最紧要开心
 * @CreateTime 2024/9/30 18:13
 * @Description TODO
 */
@Configuration
@Data
public class AiConfig {

    @Bean
    public ClientV4 getClientV4() {
        return new ClientV4.Builder("3a6c7b01b704a19eff5e5123f102366e.uSljGelJG9Y2yz94").build();
    }
}
