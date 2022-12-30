package com.szt.bandCMS.security;

import com.szt.bandCMS.models.User;
import com.szt.bandCMS.repositories.UserRepository;
import com.szt.bandCMS.services.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthProvider implements AuthenticationProvider {
    @Autowired
    private SecurityUserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<User> user = userRepository.findUserByUsername(name);
        if (!user.isPresent()) {
            throw new BadCredentialsException("invalid login details");
        }
        if (passwordEncoder().matches(password, user.get().getPassword())) {
            return createSuccessfulAuthentication(authentication, user.get());
        }
        throw new BadCredentialsException("Bad credentials");
    }

    private Authentication createSuccessfulAuthentication(final Authentication authentication, final User user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), authentication.getCredentials(), user.getAuthorities());
        token.setDetails(authentication.getDetails());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    public PasswordEncoder passwordEncoder() {
        DelegatingPasswordEncoder delPasswordEncoder=  (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        BCryptPasswordEncoder bcryptPasswordEncoder =new BCryptPasswordEncoder();
        delPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
        return delPasswordEncoder;
    }
}
