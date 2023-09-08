package syh.common;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import syh.pojo.Result;
import syh.utils.MyThreadLocal;

import java.io.IOException;



@Component
@Slf4j
public class LoginCheckFiter implements Filter {
    public static final AntPathMatcher pathMacther =new AntPathMatcher();
    public boolean checkURL(String[] urls, String requstURL)
    {
        for(String url :urls){
            if (pathMacther.match(url,requstURL))
                return true;
        }
        return false;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/user/logout"
        };
        HttpServletRequest httpServletRequest= (HttpServletRequest) servletRequest;
        String url=httpServletRequest.getRequestURI();

        if(checkURL(urls,url))
        {
            filterChain.doFilter(servletRequest,servletResponse);
            log.info("拦截到请求"+url+"登录放行");
        }
        else if(httpServletRequest.getSession().getAttribute("employee")!=null )
        {
            MyThreadLocal.setCurrentId((Long) httpServletRequest.getSession().getAttribute("employee"));
            filterChain.doFilter(servletRequest,servletResponse);
            log.info("拦截到请求"+url+"已登陆放行");
        }
        else if(httpServletRequest.getSession().getAttribute("user")!=null)
        {
            MyThreadLocal.setCurrentId((Long) httpServletRequest.getSession().getAttribute("user"));
            filterChain.doFilter(servletRequest,servletResponse);
            log.info("拦截到请求"+url+"已登陆放行");
        }
        else
        {
            servletResponse.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
            log.info("拦截到请求"+url+"登录跳转");
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
