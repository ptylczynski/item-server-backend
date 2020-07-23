package cloud.ptl.itemserver.security;

import org.springframework.boot.autoconfigure.security.servlet.SpringBootWebSecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    .antMatchers(
                            "/type",
                            "/food",
                            "/address").fullyAuthenticated()
                    .antMatchers("/ping").permitAll()
                    .anyRequest().fullyAuthenticated()
                .and()
                .httpBasic(httpSecurityHttpBasicConfigurer ->
                    httpSecurityHttpBasicConfigurer.realmName("item-server realm")
                );
    }

    @Bean
    public UserDetailsService userDetails(){

    }
}
