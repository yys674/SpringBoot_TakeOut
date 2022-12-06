package com.bupt.TakeOut.filter;

import com.alibaba.fastjson.JSON;
import com.bupt.TakeOut.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@WebFilter(filterName = "LoginCheckFilter",urlPatterns = {"/"})//拦截所有请求，过滤器名称设为LoginCheckFilter
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = {"/*"})//拦截所有请求，/*,(只有/的话只会拦截到/，说明/下面的均不拦截
public class LoginCheckFilter implements Filter {//实现filter，编辑doFilter写过滤逻辑

    //为何采用路径匹配器?----支持通配符的匹配：如/backend/** 和/backend/index.html相匹配
    //关键字final作用？----当final关键字修饰一个类，则该类会成为最终类，即该类不能被继承,因此不会被修改
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;//将传入类型强转为HttpServletRequest,才可调用getRequestURI
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        log.info("拦截到请求：{}",request.getRequestURI());

        //设置不需要处理的请求路径
        String[] urls = new String[] {"/backend/**","/front/**",
//                                        "/employee/login","/employee/logout","/employee/page"};
                                        "/employee/login","/employee/logout"};
        //获取前端的URI
        String requestURI = request.getRequestURI();

        //比对获取到的URI是否需要处理
        boolean check = check(requestURI, urls);
        //如果匹配成功，则不需要处理直接放行
        if (check) {
            log.info("本次请求{}不需要处理，直接放行",requestURI);
            filterChain.doFilter(request, response);
            return;
// 需要return，否则报java.io.IOException: Stream closed；return直接结束该方法，如果不return的话，还会继续向下执行

        }
        //如果匹配失败，首先判断登录状态，如果已经登录，则放行
        //登录状态判断，参见EmployeeController中，登录成功时，在session中存入employee属性，如果不为空，则说明登录成功
        if(request.getSession().getAttribute("employee")!=null){

           /* * 参见controller中
            * request.getSession().setAttribute("employee",emp.getId());//将成功的结果存入session中
            * 存入的也是id
             **/
            log.info("用户登录，id为：{}",request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }
        //若用户未登录,则进行拦截
        //拦截方法为：
        log.info("用户未登录");
//        response.getWriter().write(JSON.toJSONString(R.error("未登录")));
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    public boolean check(String requestURI,String[] urls){
        for (String url : urls) {
            if(PATH_MATCHER.match(url,requestURI)){
                //只要有一个符合，就匹配成功
                return true;
            }
        }
        return false;
    }
}


