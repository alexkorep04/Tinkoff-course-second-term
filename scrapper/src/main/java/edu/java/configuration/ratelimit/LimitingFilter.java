package edu.java.configuration.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class LimitingFilter extends OncePerRequestFilter {
    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();
    private final Bucket bucket;
    private final static int CODE = 429;
    private final static String MESSAGE = "Too many requests! Wait please!";

    @Autowired
    public LimitingFilter(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String ipAddress = request.getRemoteAddr();
        Bucket newBucket = ipBuckets.computeIfAbsent(ipAddress, k -> bucket);
        ConsumptionProbe probe = newBucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(CODE);
            response.getWriter().append(MESSAGE);
        }
    }
}
