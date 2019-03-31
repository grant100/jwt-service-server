package ms.auth.poc;

import ms.auth.poc.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

@EnableWebSecurity(debug = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    public static final String CC_TOKEN_ENDPOINT = "/token";
    public static final String ID_TOKEN_ENDPOINT = "/authenticate";


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable().csrf().disable().rememberMe().disable().logout().disable().formLogin().disable().httpBasic().disable()
                //.anonymous().disable()
                .authorizeRequests()
                .antMatchers("/**").authenticated()
                .anyRequest().denyAll() // any url that has not been matched is denied by default
                .and()
                .addFilterAt(idAuthenticationFilter(), BasicAuthenticationFilter.class)

              //  .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
    }

    // Configure AuthenticationManager to use JWTAuthenticationProvider implementation
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(idAuthenticationDelegationProvider());
    }


    private IDAuthenticationFilter idAuthenticationFilter() throws Exception {
        return new IDAuthenticationFilter(authenticationManager());
    }

    /*
    private IDAuthenticationEntryPoint idAuthenticationEntryPoint() {
        return new IDAuthenticationEntryPoint();
    }
    */

    @Bean
    IDAuthenticationDelegationProvider idAuthenticationDelegationProvider() {
        return new IDAuthenticationDelegationProvider();
    }
}
