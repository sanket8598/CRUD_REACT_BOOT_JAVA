package com.learn.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.learn.security.service.CustomUserDetails;
import com.learn.security.util.JwtHelper;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	private JwtHelper helper;

	@Autowired
	private CustomUserDetails customUserDetails;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String reqHeader = request.getHeader("Authorization");
		String token = null;
		String userName = null;
		// null & format check
		if (reqHeader != null && reqHeader.startsWith("Bearer ")) {
			token = reqHeader.substring(7);
			try {

				userName = this.helper.extractUsername(token);

			} catch (Exception e) {
				e.printStackTrace();
			}

			UserDetails userByUsername = this.customUserDetails.loadUserByUsername(userName);
			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userByUsername, null, userByUsername.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			} else {
				System.out.println("Token is not validated !!");
			}
		}
		filterChain.doFilter(request, response);
	}

}
