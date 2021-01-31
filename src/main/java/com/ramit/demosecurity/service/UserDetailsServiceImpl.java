package com.ramit.demosecurity.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Make DB call to fetch user by user name, 
		//and convert it into UserDetails (Spring provided class) object
		UserDetails user = new UserDetails() {
			@Override
			public boolean isEnabled() {
				return true;
			}
			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}
			@Override
			public boolean isAccountNonLocked() {
				return true;
			}
			@Override
			public boolean isAccountNonExpired() {
				return true;
			}
			@Override
			public String getUsername() {
				return "user";
			}
			@Override
			public String getPassword() {
				//BCrypt of 'password'
				return "$2a$04$AjFEmZeX7mN8zSn57PUEZeJgBeoKMvwteZMBiP57Jb4AGFsUORmLC";
			}
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				GrantedAuthority auth = new GrantedAuthority() {
					@Override
					public String getAuthority() {
						return "USER";
					}
				};
				//Authorities of the user, usually kept in separate Db tables than User
				//are returned here.
				List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
				authList.add(auth);
				return authList;
			}
		};
		return user;
	}
	
	
}
