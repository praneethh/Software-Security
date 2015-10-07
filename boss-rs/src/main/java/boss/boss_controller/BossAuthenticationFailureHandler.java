package boss.boss_controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import boss.boss_rs.BossUserLoginService;

@Component
public class BossAuthenticationFailureHandler implements AuthenticationFailureHandler  {
	@Autowired
	private BossUserLoginService userLoginService;
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authenticationEx)
			throws IOException, ServletException {
	
		
	
		String targetUrl="";
		String error="";
		//Class<? extends AuthenticationException > exClass= authenticationEx.getClass();
			//String classname=exClass.getCanonicalName();
			String msgg=authenticationEx.getLocalizedMessage();
			
			if(msgg!=null){
			if (msgg.equals("Maximum sessions of 1 for this principal exceeded")){
				targetUrl="/login?msg=Already Logged In";
				redirectStrategy.sendRedirect(request, response, targetUrl);
				return;	
			}
			}
	
		
		//UserDetails userDet=(UserDetails) authenticationEx.getExtraInformation();
				String userName=authenticationEx.getAuthentication().getName();
	if (userName.equals(null)||userName.equals("")){
		targetUrl="/login?error";
		redirectStrategy.sendRedirect(request, response, targetUrl);
		return;
		}
	if (!userLoginService.userExists(userName)){
		targetUrl="/login?error";
		redirectStrategy.sendRedirect(request, response, targetUrl);
		return;
		}
	String otpFlagIni=userLoginService.getOTPFlag(userName);
	   if(otpFlagIni=="Y"||otpFlagIni.equals("Y")){
		   error="OTP already generated. Use it or Generate Again using Forgot Password Link.";
		   targetUrl="/login?error";
			redirectStrategy.sendRedirect(request, response, targetUrl);
			return;
	   }
		int loginAttempts=userLoginService.getLoginAttempts(userName);
		if (loginAttempts>=3){
			
			String otpFlag=userLoginService.getOTPFlag(userName);
			if(otpFlag.equalsIgnoreCase("N")||otpFlag=="N"){
				if (loginAttempts==3){
                userLoginService.sendOTP(userName);
				// model.addObject("error", "Account Locked. Generate OTP and Reset Password");
				error="Account Locked.OTP Generated , Reset Password";
				 targetUrl= "/resetPassword?error";
				}
				else {
					error="Account Locked.Use Forgot Password Link to Reset Password";
					 targetUrl= "/resetPassword?error";
				}
				 //return model;
			}
			
			else{
				//model.setViewName("resetPassword");
				//model.addObject("error", "Account Locked. Use OTP and Reset Password");
				error="Account Locked. Use OTP and Reset Password";
				targetUrl= "/resetPassword?error";
			}
		}
		
		else{
		userLoginService.updateLoginAttempts(userName);
		//model.addObject("error", "Invalid username and password!");
		error="Invalid username and password!";
		targetUrl="/login?error";
		}
	
		
		
		//targetUrl="/login?error=true";
	
		
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

}
