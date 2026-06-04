package sumitproject.SpringCart.Security;

import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sumitproject.SpringCart.Entity.User;
import sumitproject.SpringCart.Helper.Role;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final AuthUtil authUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

       final String tokenHeader = request.getHeader("Authorization");

       if(tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
           filterChain.doFilter(request, response);
           return;
       }

       try {
           String finalToken = tokenHeader.substring(7);

           String username = authUtil.getUsernameFromToken(finalToken);

           if(username != null && SecurityContextHolder.getContext().getAuthentication() == null
                   && authUtil.isTokenValid(finalToken)) {

               List<String> roles = authUtil.getRolesFromToken(finalToken);

               Set<SimpleGrantedAuthority> authorities = roles.stream()
                       .map(SimpleGrantedAuthority::new)
                       .collect(Collectors.toSet());


               UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                       username,
                       null,
                       authorities
               );

              auth.setDetails(
                      new WebAuthenticationDetailsSource().buildDetails(request)
              );

              SecurityContextHolder.getContext().setAuthentication(auth);
           }

       } catch (Exception ex) {
           log.error("JWT Authentication failed: {}", ex.getMessage());
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           response.setContentType("application/json");
           response.getWriter().write("{\"error\": \"Invalid or expired JWT token\"}");
           return;
       }

       filterChain.doFilter(request, response);
    }
}
