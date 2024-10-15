package com.example.du_an_tot_nghiep.security;

import com.example.du_an_tot_nghiep.service.NguoiDungDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
            throws ServletException, IOException { // Thêm ServletException vào khai báo
        String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Lấy token
            username = jwtUtil.extractUsername(token); // Trích xuất username từ token
        }

        // Kiểm tra và xác thực token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            /*TODO:
             * Không load lại user từ database vì mình xác thực thông tin bằng token
             * trong trường hợp token lấy từ server khác thì sẽ không có bảng để lấy thông tin user
             * vì thế nên chỗ này chỉ sử dụng thông tin từ token
             */
//            UserDetails userDetails = nguoiDungDetailsService.loadUserByUsername(username);
            var authorites = jwtUtil.extractClaims(token);
            UserDetails userDetails = new User(username, "default", authorites);
            // Kiểm tra tính hợp lệ của token
            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                // Tạo đối tượng xác thực
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Thiết lập xác thực
            }
        }
        chain.doFilter(request, response); // Tiếp tục chuỗi bộ lọc
    }
}
