package com.animalbarf.api_gateway.filter;

import com.animalbarf.jwt.TokenProvider;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Фильтр проверки токена до допуска к МС
 */
@Order(0)
@Component
@RequiredArgsConstructor
public class TokenGlobalFilter implements GlobalFilter {

    private final TokenProvider accessTokenProvider;
    private final List<String> PUBLIC_PATHS = List.of("/login", "/signup");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();

        // Пропускаем публичные ключи
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        // Проверяем на неверный формат токена
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return handleUnauthorized(response, "Invalid or expired token");
        }

        String token = authHeader.substring(7);

        // Проверяем валидность токена
        if (!accessTokenProvider.validateToken(token))
            return handleUnauthorized(response, "Invalid or expired token");

        try {
            Claims claims = accessTokenProvider.extractClaims(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            if (username == null) {
                return handleUnauthorized(response, "Invalid token: missing user info");
            }

            // 5. Build new request with user headers
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Name", username)
                    .header("X-User-Role", role != null ? role : "USER")
                    .header("X-User-Id", claims.get("id", String.class))
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(mutatedRequest)
                    .build();

            return chain.filter(mutatedExchange);
        } catch (Exception e) {
            return handleUnauthorized(response, "Invalid token");
        }
    }

    /**
     * Проверяет, является ли URL общедоступным
     *
     * @param path URL
     * @return флаг общедоступности
     */
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private Mono<Void> handleUnauthorized(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String json = String.format("{\"error\":\"%s\"}", message);
        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }
}
