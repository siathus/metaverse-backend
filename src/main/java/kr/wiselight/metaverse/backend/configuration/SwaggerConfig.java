package kr.wiselight.metaverse.backend.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        List<Response> responses = new ArrayList<>();
        responses.add(new ResponseBuilder()
                .code("400")
                .description("잘못된 인자가 전달됨 (ex. 존재하지 않는 유저 조회)")
                .build());
        responses.add(new ResponseBuilder()
                .code("401")
                .description("잘못된 토큰 혹은 인증되지 않은 사용자")
                .build());
        responses.add(new ResponseBuilder()
                .code("403")
                .description("잘못된 접근")
                .build());

        return new Docket(DocumentationType.SWAGGER_2)
                .produces(Set.of(MediaType.APPLICATION_JSON_VALUE))
                .consumes(Set.of(MediaType.APPLICATION_JSON_VALUE))
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("kr.wiselight.metaverse.backend.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.POST, responses)
                .globalResponses(HttpMethod.GET, responses)
                .globalResponses(HttpMethod.PUT, responses)
                .globalResponses(HttpMethod.DELETE, responses);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

        private ApiKey apiKey() {
        return new ApiKey("JWT", HttpHeaders.AUTHORIZATION, ParameterType.HEADER.name());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference("JWT", authorizationScopes));
    }
}
