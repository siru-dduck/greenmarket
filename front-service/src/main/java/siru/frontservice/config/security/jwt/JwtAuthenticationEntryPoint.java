package siru.frontservice.config.security.jwt;


import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint {//implements AuthenticationEntryPoint {

//    @Override
//    public void commence(HttpServletRequest request,
//                         HttpServletResponse response,
//                         AuthenticationException authException) throws IOException {
//        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//    }
}