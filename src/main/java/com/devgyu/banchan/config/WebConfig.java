package com.devgyu.banchan.config;

import com.devgyu.banchan.AppProperties;
import com.devgyu.banchan.interceptor.CommonNavInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final AppProperties appProperties;
    private final CommonNavInterceptor commonNavInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadFolderPrefix = appProperties.getUploadFolderPrefix();
        registry.addResourceHandler("/upload/thumbnail/**")
                .addResourceLocations("file://" + uploadFolderPrefix + "/thumbnail/");
        registry.addResourceHandler("/upload/item/**")
                .addResourceLocations("file://" + uploadFolderPrefix + "/item/");
        registry.addResourceHandler("/upload/review/**")
                .addResourceLocations("file://" + uploadFolderPrefix + "/review/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration ir1 = registry.addInterceptor(commonNavInterceptor);
        ir1.addPathPatterns("/**")
        .excludePathPatterns("/api/**", "/orders/api/**", "/mystore/api/**", "/rider/api/**", "/cart/api/**", "/review/api", "/review/api/**"
                            , "/store/api/**", "/storelist/api/**");
    }
}
