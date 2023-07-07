package com.polaris.lesscode.permission.filter;

import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.polaris.lesscode.filter.RequestLoggerFilter;

@Component
@WebFilter(filterName = "roleRequestLoggerFilter", urlPatterns = "/*")
@Order(2)
public class RoleRequestLoggerFilter extends RequestLoggerFilter implements Filter{

}
