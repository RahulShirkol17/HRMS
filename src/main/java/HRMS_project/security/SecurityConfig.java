package HRMS_project.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity


public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomLoginSuccessHandler loginSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.csrf().disable()
	        .authorizeRequests()
	        .antMatchers("/login",
	        	    "/auth/register",
	        	    "/register",
	        	    "/",
	        	    "/swagger-ui/**",
	        	    "/v3/api-docs/**",          // <-- important for OpenAPI 3
	        	    "/swagger-resources/**",
	        	    "/webjars/**").permitAll()
	        //.antMatchers("/admin/**").hasRole("ADMIN")
	        .antMatchers("/manager/leaves/review/**").hasAnyRole("MANAGER", "HR_MANAGER")
	        .antMatchers("/employee/leaves/review/**").hasAnyRole("MANAGER", "HR_MANAGER")
	        .antMatchers("/hr/**").hasRole("HR_MANAGER")
	        .antMatchers("/candidate/**").hasRole("CANDIDATE")
	        .antMatchers("/employee/**").hasRole("EMPLOYEE")
	        .antMatchers("/emp/**").hasRole("EMPLOYEE")
	        .antMatchers("/manager/**").hasRole("MANAGER")
	        .antMatchers("/uploads/**").permitAll() 
//	        .antMatchers("/mng/**").hasRole("MANAGER")
	        .anyRequest().authenticated()
	        .and()
	        .exceptionHandling()
	            .accessDeniedHandler((request, response, accessDeniedException) -> {
	                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	                response.setContentType("application/json");
	                response.getWriter().write("{\"error\": \"403 - Forbidden: You don't have permission to access this resource.\"}");
	            })
	        .and()
	        .formLogin()
	            .loginPage("/login")
	            .successHandler(loginSuccessHandler) // 👈 choose only one
	            .failureUrl("/login?error=true")
	            .permitAll()
	        .and()
	        .logout().permitAll();
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}