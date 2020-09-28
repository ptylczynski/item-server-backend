package cloud.ptl.itemserver.misc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                    // .antMatchers("/ping/**", "/user/register/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/user/register/").permitAll()
                    .antMatchers(HttpMethod.GET, "/user/register/activate/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/ping/").permitAll()
                    .anyRequest().authenticated()
                .and()
                .httpBasic(httpSecurityHttpBasicConfigurer ->
                        httpSecurityHttpBasicConfigurer.realmName("item-server")
                )
                .formLogin().disable()
                .csrf().disable();
        // allow CORS pre-flight OPTIONS requests
        http.cors();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsServiceImplementation);
    }

    @Autowired
    private UserDetailsServiceImplementation userDetailsServiceImplementation;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y,12);
    }
}
