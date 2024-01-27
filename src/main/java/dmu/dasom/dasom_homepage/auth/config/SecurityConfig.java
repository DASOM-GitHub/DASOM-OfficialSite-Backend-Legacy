package dmu.dasom.dasom_homepage.auth.config;

import dmu.dasom.dasom_homepage.auth.filter.CustomAuthenticationFilter;
import dmu.dasom.dasom_homepage.auth.filter.JwtFilter;
import dmu.dasom.dasom_homepage.auth.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_ADMIN > ROLE_PRESIDENT > ROLE_BOARD > ROLE_GROUPLEADER > ROLE_MEMBER");

        return hierarchy;
    }

    // 비밀번호 암호화 및 대조 역할
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // cors 설정
        http
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                                CorsConfiguration configuration = new CorsConfiguration();

                                // 프론트엔드 서버에서 오는 요청을 허용한다
                                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);

                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                                return configuration;
                            }
                        }));
        // csrf 비활성
        http
                .csrf(AbstractHttpConfigurer::disable);
        // form login 방식 비활성
        http
                .formLogin(AbstractHttpConfigurer::disable);
        // http basic 인증 방식 비활성
        http
                .httpBasic(AbstractHttpConfigurer::disable);
        // 경로별 권한 인가
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(HttpMethod.POST,
                                "/recruit/{recNo}/applicants",
                                "/signup",
                                "/signup/verify",
                                "/login").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/recruit",
                                "/recruit/{recNo}").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/recruit").hasAnyRole("BOARD")
                        .requestMatchers(HttpMethod.GET,
                                "/recruit/{recNo}",
                                "/recruit/{recNo}/applicants/**",
                                "/admin",
                                "/admin/**").hasAnyRole("BOARD")
                        .requestMatchers(HttpMethod.PUT,
                                "/recruit",
                                "/recruit/**").hasAnyRole("BOARD")
                        .requestMatchers(HttpMethod.DELETE,
                                "/recruit",
                                "/recruit/**").hasAnyRole("BOARD")
                        .anyRequest().authenticated()
                        // 테스트 시에는 위 모두 주석 처리 후 아래 주석 해제
                        // .anyRequest().permitAll()
                );
        // 커스텀 필터 등록
        http
                .addFilterBefore(new JwtFilter(jwtUtil), CustomAuthenticationFilter.class);
        http
                .addFilterAt(new CustomAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);
        // jwt 사용을 위해선 세션을 stateless로 설정해야 함
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
