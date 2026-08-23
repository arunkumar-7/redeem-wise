package com.redeemwise.reward.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI Configuration for Swagger UI.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RedeemWise Reward Service API")
                        .version("v1.0")
                        .description("RedeemWise Reward Redemption Options Catalog Service API"));
    }
}
