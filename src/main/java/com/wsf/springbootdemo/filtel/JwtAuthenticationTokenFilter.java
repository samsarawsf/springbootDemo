package com.wsf.springbootdemo.filtel;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.wsf.springbootdemo.pojo.LoginUser;
import com.wsf.springbootdemo.pojo.ResponseResult;
import com.wsf.springbootdemo.utils.JWTUtil;
import com.wsf.springbootdemo.utils.RedisCache;
import com.wsf.springbootdemo.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author wsfstart
 * @create 2022-05-19 21:33
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String id=null;
        try {
            JWTUtil.verify(token);
            Map<String, Claim> payloadFromToken = JWTUtil.getPayloadFromToken(token);
            Claim claim = payloadFromToken.get("id");
            id = claim.asString();
        } catch (SignatureVerificationException e) {
            e.printStackTrace();

            WebUtils.renderString(response,new ResponseResult(500,"签名不⼀致"));
            return;
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            WebUtils.renderString(response,"令牌过期");
            return;
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            WebUtils.renderString(response,"算法不匹配");
            return;
        } catch (InvalidClaimException e) {
            e.printStackTrace();
            WebUtils.renderString(response,"失效的payload");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            WebUtils.renderString(response,"token⽆效");
            return;
        }
        //从redis中获取用户信息
        String redisKey = "login:" + id;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        if(Objects.isNull(loginUser)){
//            throw new RuntimeException("用户未登录");
            WebUtils.renderString(response,"用户未登录");
            return;
        }
        //存入SecurityContextHolder
        //获取权限信息封装到AuthenticationToken
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
