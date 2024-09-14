package Dompoo.Hongpoong.api.config;

import Dompoo.Hongpoong.common.logging.LoggingInterceptor;
import Dompoo.Hongpoong.common.security.AuthInterceptor;
import Dompoo.Hongpoong.common.security.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
	
	private final LoggingInterceptor loggingInterceptor;
	private final AuthInterceptor authInterceptor;
	private final LoginUserArgumentResolver loginUserArgumentResolver;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loggingInterceptor);
		registry.addInterceptor(authInterceptor);
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginUserArgumentResolver);
		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
	}
}
