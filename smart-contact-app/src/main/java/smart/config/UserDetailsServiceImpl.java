package smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import smart.dao.UserRepository;
import smart.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService  {

	@Autowired
	private UserRepository userepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//fetching data from database
	    User user=userepo.getUserByUsername(username);
	    if(user==null) {
	    	throw new UsernameNotFoundException("could not found user");
	    }
	    CustomUserDetails customdetails=new CustomUserDetails(user);
	    return customdetails;
	}
}
