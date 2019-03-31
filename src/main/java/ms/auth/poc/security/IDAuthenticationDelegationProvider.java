package ms.auth.poc.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IDAuthenticationDelegationProvider implements AuthenticationProvider {
    private Logger logger = LoggerFactory.getLogger(IDAuthenticationDelegationProvider.class);

    @Autowired
    private IDAuthenticationDelegationService delegationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        try {

            logger.info("Attempting ID Token authentication");
            Assertion assertion = delegationService.getAssertion((String) authentication.getCredentials());

            if(assertion == null){
                throw new BadCredentialsException("Assertion is invalid");
            }

            if(!assertion.getSuccess()){
                throw new BadCredentialsException("ID Token is not valid");
            }

            String token = authentication.getCredentials().toString();
            DecodedJWT idToken = com.auth0.jwt.JWT.decode(token);
            String issuer = idToken.getIssuer();

            Collection<? extends GrantedAuthority> authorities = getAuthorities(idToken);

            // Client provided a valid token
            User user = new User(issuer, authentication.getCredentials().toString(), true, true, true, true, authorities);

            // return a trusted token
            return new UsernamePasswordAuthenticationToken(user, token, authorities);

        } catch (Exception e) {
            logger.error("Failed to authenticate ID Token",e);
            throw new AccountExpiredException("Failed to authenticate ID Token", e);
        }

    }

    Collection<? extends GrantedAuthority> getAuthorities(DecodedJWT idToken){
        String[] aut = idToken.getClaim("aut").asArray(String.class);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if(aut == null || aut.length == 0){
            return authorities;
        }

        for(int i = 0; i < aut.length; i++){
            authorities.add(new SimpleGrantedAuthority(aut[i]));
        }

        return authorities;
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
