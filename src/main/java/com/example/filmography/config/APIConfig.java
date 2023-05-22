package com.example.filmography.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class APIConfig {

        @Bean
        public OpenAPI filmography() {
            return new OpenAPI()
                    .info(new Info()
                            .description("Filmography app MVC")
                            .title("Filmography")
                            .version("version 0.1")
                            .contact(new Contact().name("Andrey. S"))
                    );
        }

    }
