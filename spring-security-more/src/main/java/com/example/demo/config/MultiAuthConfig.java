//package com.example.demo.config;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class MultiAuthConfig {
//
//    @Bean("userAuthManager")
//    public AuthenticationManager userAuthManager(
//            @Qualifier("userDetailsService") UserDetailsService uds,
//            PasswordEncoder encoder) {
//        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
//        p.setUserDetailsService(uds);
//        p.setPasswordEncoder(encoder);
//        return new ProviderManager(p);
//    }
//
//    @Bean("adminAuthManager")
//    public AuthenticationManager adminAuthManager(
//            @Qualifier("adminDetailsService") UserDetailsService uds,
//            PasswordEncoder encoder) {
//        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
//        p.setUserDetailsService(uds);
//        p.setPasswordEncoder(encoder);
//        return new ProviderManager(p);
//    }
//
//}
