package com.hegp.core.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hgp
 * @date 20-5-27
 */
@Configuration
public class FilterOrderConfig {
    @Value("${rate-limiter.one-second.limit:1000}")
    private Double oneSecondRateLimiter = 1000d;
    @Value("${rate-limiter.one-second.one-url.limit:100}")
    private Double oneSecondOneUrlRateLimiter = 100d;

    @Bean
    public FilterRegistrationBean<RateLimiterFilter> rateLimiterFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RateLimiterFilter(oneSecondRateLimiter, oneSecondOneUrlRateLimiter));
        registration.setName("rateLimiterFilter");
        registration.setOrder(Integer.MIN_VALUE);
        return registration;
    }
}
