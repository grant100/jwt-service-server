package ms.auth.poc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class IDAuthenticationDelegationProvider implements AuthenticationProvider {
    @Autowired
    IDAuthenticationDelegationService delegationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        try {
            Assertion assertion = delegationService.getAssertion((String) authentication.getCredentials());

            if(assertion == null){
                throw new BadCredentialsException("Assertion is invalid");
            }

            if(!assertion.getSuccess()){
                throw new BadCredentialsException("ID Token is not valid");
            }

            //TODO map user authorities
            //TODO create user object
            return null;
        } catch (Exception e) {
            throw new BadCredentialsException("Failed to authenticate ID Token", e);
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
