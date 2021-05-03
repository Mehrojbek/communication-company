package uz.pdp.appcommunicationcompany.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.appcommunicationcompany.service.AuthService;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthService authService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("Authorization");

        if (token != null) {
            if (token.startsWith("Bearer")) {
                token = token.substring(7);

                String username = jwtProvider.getUsername(token);
                if (username != null) {
                    UserDetails userDetails = authService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }

            } else if (token.startsWith("Basic Payment")) {
                token = token.substring(14);
                byte[] decode = Base64.getDecoder().decode(token);
                String usernamePassword = new String(decode, StandardCharsets.UTF_8);
                String[] split = usernamePassword.split(":");
                String username = split[0];
                String password = split[1];
                UserDetails userDetails = authService.loadUserByUsername(username);
                if (passwordEncoder.matches(password, userDetails.getPassword())) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }
            }else if (token.startsWith("Basic SimCard")) {
                token = token.substring(14);
                byte[] decode = Base64.getDecoder().decode(token);
                String usernamePassword = new String(decode, StandardCharsets.UTF_8);
                String[] split = usernamePassword.split(":");
                String username = split[0];
                String password = split[1];
                UserDetails userDetails = authService.loadUserByUsernameForSimCard(username);
                if (passwordEncoder.matches(password, userDetails.getPassword())) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

}
