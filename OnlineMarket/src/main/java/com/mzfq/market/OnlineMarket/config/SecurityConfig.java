package com.mzfq.market.OnlineMarket.config;

import com.mzfq.market.OnlineMarket.models.entities.SellersEntity;
import com.mzfq.market.OnlineMarket.services.SellersService;
import com.mzfq.market.OnlineMarket.utils.HashUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private SellersService sellersService;

    public SecurityConfig(SellersService sellersService) {
        this.sellersService = sellersService;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return cif -> {
            SellersEntity seller = sellersService.getSellerByCif(cif);

            if(seller==null){
                throw new UsernameNotFoundException("Seller cif not found");
            }else{
                return User.withUsername(seller.getCif())
                        .password(seller.getPassword())
                        .roles("SELLER") // not really necessary
                        //.passwordEncoder(passwordEncoder::encode) // uncomment this line if password is not stored encoded
                        .build();
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                HashUtil hashUtil = new HashUtil();
                return hashUtil.hashPassword(rawPassword.toString()).toUpperCase();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equals(encodedPassword);
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login","/css/**","/js/**","/icons/**").permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .maximumSessions(3)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/login?expired")
                .and()
                .invalidSessionUrl("/login?expired")
                .sessionFixation().none()
                .and()
                .csrf().disable();
    }
}