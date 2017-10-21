package only.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
	
import only.model.User;
import only.service.CustomUserDetailsService;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private CustomUserDetailsService userservice;
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.out.println("test");
		String user_id = (String) authentication.getPrincipal();
		String user_pw = (String) authentication.getCredentials();

		System.out.println("Welcome authenticate! {}"+ user_id + "/" + user_pw);

		String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        User member = null;
        Collection<GrantedAuthority> authorities = null;
        try {
            member =  userservice.loadUserByUsername(username);
            System.out.println(username + password +"done");
            
            // 이용자가 로그인 폼에서 입력한 비밀번호와 DB로부터 가져온 암호화된 비밀번호를 비교한다
            System.out.println("member pw: "+ member.getPassword() + user_pw);
            if (!member.getPassword().equals(user_pw)) {
            	throw new BadCredentialsException("비밀번호 불일치");
            }
            authorities = (Collection<GrantedAuthority>) member.getAuthorities();
        } catch(UsernameNotFoundException e) {
            System.out.println("없는 아이디입니다");
            throw new UsernameNotFoundException(e.getMessage());
        } catch(BadCredentialsException e) {
            System.out.println("비밀번호 불일치");
            throw new BadCredentialsException(e.getMessage());
        } catch(Exception e) {
        	System.out.println("Login error : " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        System.out.println("Add new Auth Token: " + username + ", " + password + ", " + authorities);
        return new UsernamePasswordAuthenticationToken(username, password, authorities);
	}

}
