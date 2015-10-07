package boss.boss_controller;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import boss.boss_rs.BossUserLoginService;

public class BossAuthenticationSuccessHandler implements AuthenticationSuccessHandler  {
	// protected Log logger = LogFactory.getLog(this.getClass());

	
	    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	 
	    @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, 
	      HttpServletResponse response, Authentication authentication) throws IOException {
	        handle(request, response, authentication);
	        clearAuthenticationAttributes(request);
	    }
	 
	    protected void handle(HttpServletRequest request, 
	      HttpServletResponse response, Authentication authentication) throws IOException {
	        String targetUrl = determineTargetUrl(authentication);
	        request.getSession().setMaxInactiveInterval(60*60);
	        if (response.isCommitted()) {
	            //logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
	            return;
	        }
	 
	        redirectStrategy.sendRedirect(request, response, targetUrl);
	    }
	 
	    /** Builds the target URL according to the logic defined in the main class Javadoc. */
	    protected String determineTargetUrl(Authentication authentication) {
	        boolean isUser = false;
	        boolean isAdmin = false;
	        boolean isMerchant = false;
	        boolean isEmployee = false;
//	        String username = authentication.getName();
//	        String flag=userLoginService.getLoginFlagByName(username);
//	        if (flag!=null&&flag!=""){
//	        	if  (flag.equals("Y")||flag=="Y"){
//	        		userLoginService.updateLoginFlagN(username);
//	        		return "/login?logout";
//	        	}
//	        	else if (flag.equals("N")||flag=="N"){
//	        		userLoginService.updateLoginFlagY(username);
//	        	}
//	        }
	        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	        for (GrantedAuthority grantedAuthority : authorities) {
	            if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
	                isUser = true;
	                break;
	            } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
	                isAdmin = true;
	                break;
	            }
	            else if (grantedAuthority.getAuthority().equals("ROLE_MERCHANT")) {
	                isMerchant = true;
	                break;
	            }
	            else if (grantedAuthority.getAuthority().equals("ROLE_EMPLOYEE")) {
	                isEmployee = true;
	                break;
	            }
	        }
	 
	        if (isUser) {
	            return "/extuser";
	        } else if (isAdmin) {
	            return "/admin";
	        } else if (isMerchant) {
	            return "/merchant";
	        }
	        else if (isEmployee) {
	            return "/InternalUser";
	        } else {
	            throw new IllegalStateException();
	        }
	    }
	 
	    protected void clearAuthenticationAttributes(HttpServletRequest request) {
	        HttpSession session = request.getSession(false);
	        if (session == null) {
	            return;
	        }
	        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	    }
	 
	    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
	        this.redirectStrategy = redirectStrategy;
	    }
	    protected RedirectStrategy getRedirectStrategy() {
	        return redirectStrategy;
	    }

	
}
