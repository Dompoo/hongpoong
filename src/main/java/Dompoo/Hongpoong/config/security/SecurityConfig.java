package Dompoo.Hongpoong.config.security;

import Dompoo.Hongpoong.config.handler.*;
import Dompoo.Hongpoong.domain.Member;
import Dompoo.Hongpoong.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN;

@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(toH2Console()).permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/chat").permitAll()
                        .requestMatchers("/ws/chat").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/signup").permitAll()
                        .requestMatchers("/auth/email").permitAll()
                        .requestMatchers("/auth/email/accept").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.addHeaderWriter(new XFrameOptionsHeaderWriter(SAMEORIGIN)))
                .addFilterBefore(emailPasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e
                        .accessDeniedHandler(new Http403Handler())
                        .authenticationEntryPoint(new Http401Handler()))
                .logout((logout) -> logout
                        .logoutUrl("/auth/logout")
                        .deleteCookies("SESSION")
                        .logoutSuccessHandler(new LogoutSuccessHandler())
                )
                .build();
    }

    @Bean
    public EmailPasswordAuthFilter emailPasswordAuthFilter() {
        EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter("/auth/login");
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationFailureHandler(new LoginFailHandler());
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setValiditySeconds(2592000);
        filter.setRememberMeServices(rememberMeServices);

        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService(memberRepository));
        provider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(provider);
    }

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository) {
        return email -> {
            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException(email + "을 찾을 수 없습니다."));

            return new UserPrincipal(member);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
