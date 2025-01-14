package com.example.du_an_tot_nghiep.security;

import com.example.du_an_tot_nghiep.service.NguoiDungDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter { // Kế thừa OncePerRequestFilter
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NguoiDungDetailsService nguoiDungDetailsService;

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;
        if("/api/xac-thuc/dang-nhap".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
        } else {
            // Kiểm tra xem header Authorization có tồn tại không và có phải là Bearer token không
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7); // Lấy token từ header
                username = jwtUtil.extractUsername(token); // Trích xuất username từ token
            }

            // Kiểm tra nếu username không null và không có xác thực người dùng trong SecurityContext
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Sử dụng NguoiDungDetailsService để lấy thông tin người dùng từ DB
                UserDetails userDetails = nguoiDungDetailsService.loadUserByUsername(username);

                // Kiểm tra tính hợp lệ của token
                if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                    // Tạo đối tượng xác thực với các quyền của người dùng
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Thiết lập đối tượng Authentication vào SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            // Tiếp tục chuỗi bộ lọc
            chain.doFilter(request, response);
        }
    }
}
