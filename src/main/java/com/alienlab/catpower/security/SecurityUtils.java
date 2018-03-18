package com.alienlab.catpower.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * Spring安全实用工具类
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * 获取当前用户的登录名。
     * @return 当前用户的登录名
     */
    public static Optional<String> getCurrentUserLogin() {
        System.out.print("获取当前用户的登录名。");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> {
                if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                    String un=springSecurityUser.getUsername();
                    if(un.indexOf(",")>0){
                        return un.split(",")[0];
                    }else{
                        return un;
                    }

                } else if (authentication.getPrincipal() instanceof String) {
                    String un= (String) authentication.getPrincipal();
                    if(un.indexOf(",")>0){
                        return un.split(",")[0];
                    }else{
                        return un;
                    }
                }
                return null;
            });
    }

    /**
     * 获取当前用户的JWT
     * @return 当前用户的JWT
     */
    public static Optional<String> getCurrentUserJWT() {
        System.out.print("获取当前用户的JWT");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .filter(authentication -> authentication.getCredentials() instanceof String)
            .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * 检查用户是否经过身份验证
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        System.out.print("检查用户是否经过身份验证");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)))
            .orElse(false);
    }

    /**
     * 如果当前用户具有特定的权限（安全角色）。
     * <p>
     * The name of this method comes from the isUserInRole() method in the Servlet API
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     */
    public static boolean isCurrentUserInRole(String authority) {
        System.out.print("如果当前用户具有特定的权限");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)))
            .orElse(false);
    }
}
