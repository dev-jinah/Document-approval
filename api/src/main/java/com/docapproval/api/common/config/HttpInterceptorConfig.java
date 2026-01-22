package com.docapproval.api.common.config;
import com.docapproval.api.common.ApiKeyInterceptor;
import com.docapproval.api.common.HttpInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@RequiredArgsConstructor
public class HttpInterceptorConfig implements WebMvcConfigurer {

    private final ApiKeyInterceptor apiKeyInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        // 요청 ID Interceptor
        registry.addInterceptor(new HttpInterceptor());

        // API Key 인증 Interceptor
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login/**",           // 로그인
                        "/swagger-ui/**",      // Swagger UI
                        "/swagger-ui.html",    // Swagger UI
                        "/v3/api-docs/**",     // OpenAPI docs
                        "/swagger-resources/**", // Swagger resources
                        "/webjars/**",         // Swagger webjars
                        "/error"               // 에러 페이지
                );
    }

    @Override()
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "DELETE");
    }
}
