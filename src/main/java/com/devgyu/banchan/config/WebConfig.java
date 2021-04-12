package com.devgyu.banchan.config;

import com.devgyu.banchan.AppProperties;
import com.devgyu.banchan.interceptor.MypageInterceptor;
import com.devgyu.banchan.interceptor.ThumbnailInterceptor;
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
    private final ThumbnailInterceptor thumbnailInterceptor;
    private final MypageInterceptor mypageInterceptor;

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
        InterceptorRegistration ir1 = registry.addInterceptor(thumbnailInterceptor);
        ir1.addPathPatterns("/**")
        .excludePathPatterns("/api/**");
        InterceptorRegistration ir2 = registry.addInterceptor(mypageInterceptor);
        ir2.addPathPatterns("/mypage/**");
    }
}
