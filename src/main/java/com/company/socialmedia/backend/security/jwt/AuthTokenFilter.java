package com.company.socialmedia.backend.security.jwt;

import com.company.socialmedia.backend.api.user.UserService;
import com.company.socialmedia.backend.api.user.dto.User;
import com.company.socialmedia.backend.security.UserContext;
import com.company.socialmedia.backend.security.services.UserDetailsServiceImpl;
import com.company.socialmedia.backend.api.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final RequestMatcher ignoredPaths = new AntPathRequestMatcher("/swagger-ui");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        LOGGER.debug("URI: {}", uri);

        try {
            String jwt = parseJwt(request);
            LOGGER.debug("jwt IS NULL: {}", jwt == null  ? "YES" : "NO");
            LOGGER.debug("jwtUtils.validateJwtToken: {}", jwtUtils.validateJwtToken(jwt)  ? "YES" : "NO");
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                User user = userRepository.findByUsername(username);

                LOGGER.debug("in try block with jwt != null");
                LOGGER.debug("USERNAME: {}", username);
//                LOGGER.debug("USER: {}", user);

                /**
                 * User Context first creation. From now on available with UserContext.getCurrentUser()
                 */
                UserContext.create(user.getId(), user.getUsername());

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                LOGGER.debug("UserDetails: {}", userDetails);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());

                LOGGER.debug("UsernamePasswordAuthenticationToken authentication: {}", authentication);
                LOGGER.debug("UsernamePasswordAuthenticationToken authentication Principal: {}", authentication.getPrincipal());
                LOGGER.debug("UsernamePasswordAuthenticationToken authentication Credentials: {}", authentication.getCredentials());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("authorization");

        if (StringUtils.hasText(headerAuth)) {
            return headerAuth;
        }
//        String jwt = jwtUtils.getJwtFromCookies(request);

        return null;
    }
}
