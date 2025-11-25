package Tproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")      // Разрешить CORS для всех путей
                        .allowedOrigins("*")    // Разрешить все домены
                        .allowedMethods("*")    // Разрешить все HTTP методы (GET, POST, PUT, DELETE и т.д.)
                        .allowedHeaders("*");   // Разрешить все заголовки
            }
        };
    }
}
