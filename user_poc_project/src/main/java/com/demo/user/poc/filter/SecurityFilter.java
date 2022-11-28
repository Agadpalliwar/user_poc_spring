package com.demo.user.poc.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.user.poc.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class SecurityFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                        throws ServletException, IOException
    {
       
       String token = request.getHeader("Authorization");
       System.out.println("doFilterInternal:"+token);

       if(token != null){
           

         String username = jwtUtil.getUsername(token);
           System.out.println("doFilterInternal:"+username);

        
         if(username != null &&
                 SecurityContextHolder
                         .getContext()
                         .getAuthentication() == null)
         {

           UserDetails userDetails = userDetailsService.loadUserByUsername(username);
             System.out.println("doFilterInternal:"+userDetails.getUsername());
             System.out.println("doFilterInternal:"+userDetails.getPassword());

          
            boolean isValid = jwtUtil.validateToken(token, userDetails.getUsername());
             System.out.println("doFilterInternal:"+isValid);
            if(isValid){

                UsernamePasswordAuthenticationToken   authToken =
                        new UsernamePasswordAuthenticationToken(
                                username, userDetails.getPassword(),
                                userDetails.getAuthorities()

                        );
                System.out.println("doFilterInternal: authToken"+authToken);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
         }
       }
        filterChain.doFilter(request, response);

    }
}
