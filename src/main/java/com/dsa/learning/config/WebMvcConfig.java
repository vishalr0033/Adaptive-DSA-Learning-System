package com.dsa.learning.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/login").setViewName("forward:/login.html");
        registry.addViewController("/register").setViewName("forward:/register.html");
        registry.addViewController("/dashboard").setViewName("forward:/dashboard.html");
        registry.addViewController("/problems").setViewName("forward:/problems.html");
        registry.addViewController("/submissions").setViewName("forward:/submissions.html");
        registry.addViewController("/analysis").setViewName("forward:/analysis.html");
        registry.addViewController("/recommendations").setViewName("forward:/recommendations.html");
        registry.addViewController("/improvement").setViewName("forward:/improvement.html");
        registry.addViewController("/profile").setViewName("forward:/profile.html");
        registry.addViewController("/admin").setViewName("forward:/admin.html");
        registry.addViewController("/editor").setViewName("forward:/editor.html");
    }
}
