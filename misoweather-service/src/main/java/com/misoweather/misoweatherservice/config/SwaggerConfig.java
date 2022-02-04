package com.misoweather.misoweatherservice.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Collections.singletonList;

/**
 * url/swagger-ui/
 *
 * @author yeon
 **/
@Configuration
@RequiredArgsConstructor
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
    private final Environment env;

    String apiTitle = "MisoWeather REST API Documnet";
    String apiDescription = "@Author tmddusgood | Yeon";
    String name = "Seungyeon Kang";
    String github = "https://github.com/tmddusgood";
    String email = "tmddusgood@gmail.com";

    String referenceName = "Server Token";
    String keyName = "serverToken";
    String header = "header";
    String version = "1.0.0";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData())
                .securityContexts(securityContext())
                .securitySchemes(singletonList(apiKey()));
    }

    /**
     * Swagger와 Actuator 가 함께 사용되고 있을 때, 엔드포인트 path pattern 방식이 달라서 아래 설정으로 override 합니다.
     *
     * @author yeon
    **/
    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
                                                                         ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier,
                                                                         EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties,
                                                                         WebEndpointProperties webEndpointProperties, Environment environment) {

        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping =
                webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
                        || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
                corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
                shouldRegisterLinksMapping, null);
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title(apiTitle)
                .description(apiDescription)
                .version(version)
                .contact(new Contact(name, github, email))
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey(referenceName, keyName, header);
    }

    private List<SecurityReference> securityReference = singletonList(SecurityReference.builder()
            .reference(referenceName)
            .scopes(new AuthorizationScope[0])
            .build()
    );

    private List<SecurityContext> securityContext() {
        List<SecurityContext> lsc = new ArrayList<>();

        lsc.add(SecurityContext.builder()
                .securityReferences(securityReference)
                .forPaths(PathSelectors.ant("/api/member"))
                .forHttpMethods(Predicate.isEqual(HttpMethod.DELETE))
                .build()
        );

        lsc.add(SecurityContext.builder()
                .securityReferences(securityReference)
                .forPaths(PathSelectors.ant("/api/member"))
                .forHttpMethods(Predicate.isEqual(HttpMethod.GET))
                .build()
        );

        lsc.add(SecurityContext.builder()
                .securityReferences(securityReference)
                .forPaths(PathSelectors.ant("/api/comment"))
                .forHttpMethods(Predicate.isEqual(HttpMethod.POST))
                .build()
        );

        lsc.add(SecurityContext.builder()
                .securityReferences(securityReference)
                .forPaths(PathSelectors.ant("/api/survey"))
                .forHttpMethods(Predicate.isEqual(HttpMethod.POST))
                .build()
        );

        lsc.add(SecurityContext.builder()
                .securityReferences(securityReference)
                .forPaths(PathSelectors.ant("/api/survey/member"))
                .forHttpMethods(Predicate.isEqual(HttpMethod.GET))
                .build()
        );

        lsc.add(SecurityContext.builder()
                .securityReferences(securityReference)
                .forPaths(PathSelectors.ant("/api/survey/precheck"))
                .forHttpMethods(Predicate.isEqual(HttpMethod.GET))
                .build()
        );

        lsc.add(SecurityContext.builder()
                .securityReferences(securityReference)
                .forPaths(PathSelectors.ant("/api/member-region-mapping/default"))
                .forHttpMethods(Predicate.isEqual(HttpMethod.PUT))
                .build()
        );

        return lsc;
    }
}