package smart.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class MyConfig {

	@Bean
	public UserDetailsService getUserDetailService() {

		return new UserDetailsServiceImpl();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoauthenticationprovider = new DaoAuthenticationProvider();
		daoauthenticationprovider.setUserDetailsService(this.getUserDetailService());
		daoauthenticationprovider.setPasswordEncoder(passwordEncoder());
		return daoauthenticationprovider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
        .csrf()
        .disable()
        .httpBasic()
        .and()
        .authorizeHttpRequests()
        /*.requestMatchers("/user/**").hasRole("USER")*/
        .requestMatchers("/user/**").hasAuthority("ROLE_USER")
        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
        .requestMatchers("/").permitAll()
       .anyRequest().authenticated()
        .and()
        .formLogin()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);;

return http.build();


	}

}
