package com.hegp.core.filter;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 限流的过滤器优先排在第一位
 * 世纪无奈, @Order执行顺序可能配置会不生效, 决定用FilterRegistrationBean配置
 * @author hgp
 * @date 20-5-27
 */
public class RateLimiterFilter extends OncePerRequestFilter {

    private String errorMsg = "{\"code\":500,\"msg\":\"限流\"}";

    private Double oneSecondOneUrlRateLimiter;

    //存入URL 和 rateLimiter 的实列，保证 每个请求都是单列的
    private Map<String, RateLimiter> urlRateMap = new ConcurrentHashMap();
    private RateLimiter rateLimiter = null;

    private final Logger logger = LoggerFactory.getLogger(RateLimiterFilter.class);

    public RateLimiterFilter(Double oneSecondRateLimiter, Double oneSecondOneUrlRateLimiter) {
        this.oneSecondOneUrlRateLimiter = oneSecondOneUrlRateLimiter;
        rateLimiter = RateLimiter.create(oneSecondRateLimiter);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 全局限流
        boolean result = rateLimiter.tryAcquire(500, TimeUnit.MICROSECONDS);
        if (result==false) {
            response.setStatus(500);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(errorMsg);
            logger.error("触发全局限流");
            return;
        }

        // 单个URL限流
        String url = request.getRequestURI();
        if (urlRateMap.get(url) == null) {
            urlRateMap.put(url, RateLimiter.create(oneSecondOneUrlRateLimiter));
        }
        RateLimiter urlRateLimiter = urlRateMap.get(url);
        // 等待500毫秒
        result = urlRateLimiter.tryAcquire(500, TimeUnit.MICROSECONDS);
        if (result==false) {
            response.setStatus(500);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().write(errorMsg);
            logger.error("触发URL限流");
            return;
        }
        filterChain.doFilter(request, response);
    }

}
