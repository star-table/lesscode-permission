package com.polaris.lesscode.permission.filter;

import com.polaris.lesscode.filter.RequestContextFilter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;

/**
 * request context filter
 *
 * @author roamer
 * @version v1.0
 * @date 2020-08-31 10:30
 */
@Component
@WebFilter(filterName = "roleRequestContextFilter", urlPatterns = "/*")
@Order(1)
public class RoleRequestContextFilter extends RequestContextFilter implements Filter {

}
