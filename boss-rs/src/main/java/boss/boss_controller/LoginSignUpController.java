package boss.boss_controller;
import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import boss.boss_form.ForgotPassword;
import boss.boss_form.ResetPassword;
import boss.boss_rs.BossUserDetailsService;
import boss.boss_rs.BossUserLoginService;
import boss.boss_rs.BossUserService;
import boss.data.entities.BossUser;
import boss.data.repositories.BossUserRepository;


@Controller
public class LoginSignUpController {
	@Autowired
	//private BossUserRepository userRepo;
	private BossUserService userService;
	@Autowired
	private BossUserDetailsService userDetailsService;
	@Autowired
	private BossUserLoginService userLoginService;
	
	
	
	
		@RequestMapping(value = "/admin", method = RequestMethod.GET)
		public ModelAndView adminPage() {

			ModelAndView model = new ModelAndView();
			model.addObject("title", "Welcome BOSS Admin");
			model.addObject("message", "Here's what all you can do!");
			model.setViewName("admin");

			return model;

		}
		

		@RequestMapping(value = "/admincreateExternalUser", method = RequestMethod.GET)
		public String userSignUp(Model model,@RequestParam(value = "error", required = false) String error) {

			//ModelAndView model = new ModelAndView();
			//Model model = new Model();
			//String userDet = userService.saveNewUser();
			  //model.addAttribute("UserSignUp", new BossUser());
			 if(error!=null)
			  {
				  
				  	if(error.equals("ie"))
				  	model.addAttribute("error", "Invalid Email");
				  	else if(error.equals("if"))
					  	model.addAttribute("error", "Invalid First Name");
				  	else if(error.equals("il"))
					  	model.addAttribute("error", "Invalid Last Name");
				  	else if(error.equals("iu"))
					  	model.addAttribute("error", "Invalid UserName");
				  	else if(error.equals("is"))
					  	model.addAttribute("error", "Invalid SSN");
				  	else if(error.equals("ia"))
					  	model.addAttribute("error", "Invalid Address");
				  	else if(error.equals("ip"))
					  	model.addAttribute("error", "Invalid Phonenumber");
					else {
				  		model.addAttribute("error", error);	
				  	}
				  	
			  }
			model.addAttribute("UserSignUp", new BossUser());
			//model.setViewName("UserSignUp");

			return "UserSignUp";

		}
		
		
		@RequestMapping(value = "/admincreateInternalUser", method = RequestMethod.GET)
		public String internalUserSignUp(Model model,@RequestParam(value = "error", required = false) String error) {

			//ModelAndView model = new ModelAndView();
			//Model model = new Model();
			//String userDet = userService.saveNewUser();
			  //model.addAttribute("UserSignUp", new BossUser());
			if(error!=null)
			  {
				  
				  	if(error.equals("ie"))
				  	model.addAttribute("error", "Invalid Email");
				  	else if(error.equals("if"))
					  	model.addAttribute("error", "Invalid First Name");
				  	else if(error.equals("il"))
					  	model.addAttribute("error", "Invalid Last Name");
				  	else if(error.equals("iu"))
					  	model.addAttribute("error", "Invalid UserName");
				  	else if(error.equals("is"))
					  	model.addAttribute("error", "Invalid SSN");
				  	else if(error.equals("ia"))
					  	model.addAttribute("error", "Invalid Address");
				  	else if(error.equals("ip"))
					  	model.addAttribute("error", "Invalid Phonenumber");
					else {
				  		model.addAttribute("error", error);	
				  	}
				  	
			  }
			model.addAttribute("InternalUserSignUp", new BossUser());
			//model.setViewName("UserSignUp");

			return "InternalUserSignUp";

		}
		
		@RequestMapping(value = "/admincreateMerchant", method = RequestMethod.GET)
		public String merchantSignUp(Model model,@RequestParam(value = "error", required = false) String error) {

			//ModelAndView model = new ModelAndView();
			//Model model = new Model();
			//String userDet = userService.saveNewUser();
			  //model.addAttribute("UserSignUp", new BossUser());
			if(error!=null)
			  {
				  
				  	if(error.equals("ie"))
				  	model.addAttribute("error", "Invalid Email");
				  	else if(error.equals("if"))
					  	model.addAttribute("error", "Invalid Organisation Name");
				  	else if(error.equals("il"))
					  	model.addAttribute("error", "Invalid Owner Name");
				  	else if(error.equals("iu"))
					  	model.addAttribute("error", "Invalid Merchant UserName");
				  	else if(error.equals("is"))
					  	model.addAttribute("error", "Invalid License No");
				  	else if(error.equals("ia"))
					  	model.addAttribute("error", "Invalid Address");
				  	else if(error.equals("ip"))
					  	model.addAttribute("error", "Invalid Contact");
				  	else {
				  		model.addAttribute("error", error);	
				  	}
				  	
			  }
			model.addAttribute("MerchantSignUp", new BossUser());

			return "MerchantSignUp";

		}
		
		@RequestMapping(value="externalRegisterationAction",method = RequestMethod.POST)
		public String registerMerchant(@ModelAttribute("MerchantSignUp")BossUser userEntity,  
				BindingResult result,Model model,
				//@RequestParam("recaptcha_challenge_field") String challangeField,
				//@RequestParam("recaptcha_response_field") String responseField,
				HttpServletRequest servletRequest,HttpServletResponse response) {
			
String r1=userEntity.getAddress();
			
			String r2=userEntity.getEmail();
			
			String r3=userEntity.getFname();
			
			String r4=userEntity.getLname();
			
			String r5=userEntity.getUname();
			
			String r6=userEntity.getSsn();
			
			String r8="";
			if (userEntity.getPhoneno()!=null){
			long r7=userEntity.getPhoneno();
			r8=String.valueOf(r7);
			}
			else {
				model.addAttribute("error","ip");
				return "redirect:/admincreateMerchant";
			}
			
			if(!valphno(r8))
			{
			model.addAttribute("error","ip");
			return "redirect:/admincreateMerchant";
			}
			
			if(!valadd(r1))
			{
			model.addAttribute("error","ia");
			return "redirect:/admincreateMerchant";
			}
			
			if(!valssn(r6))
			{
			model.addAttribute("error","is");
			return "redirect:/admincreateMerchant";
			}
			
			if(!valemails(r2))
			{
			model.addAttribute("error","ie");
			return "redirect:/admincreateMerchant";
			}
			
			if(!valfn(r3))
			{
			model.addAttribute("error","if");
			return "redirect:/admincreateMerchant";
			}
	
			if(!valfn(r4))
			{
			model.addAttribute("error","il");
			return "redirect:/admincreateMerchant";
			}
			
			if(!valnumss(r5))
			{
			model.addAttribute("error","iu");
			return "redirect:/admincreateMerchant";
			}
			
			
			 String remoteAddr = servletRequest.getRemoteAddr();
		        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		        reCaptcha.setPrivateKey("6LeHaP0SAAAAALdnTmrUmOH4BBvrWEt8KfBFZYrQ");
		        String challenge = servletRequest.getParameter("recaptcha_challenge_field");
		        String uresponse = servletRequest.getParameter("recaptcha_response_field");
		        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
		      
		        if (reCaptchaResponse.isValid()) {
		        	if (userService.userExists(userEntity.getUname())||userService.userSSNExists(userEntity.getSsn())){
		        		model.addAttribute("error","Merchant Already Exists"); 
			        	 return "redirect:/admincreateMerchant";
		        	}
		        	String status=userService.createMerchant(userEntity);
					//if (status=="success"||status.equals("success")){
		        	if (status=="sent"||status.equals("sent")){
						//model.add
		        		model.addAttribute("error","Mechant Created");
			return "redirect:/admincreateMerchant";}
					else {
						model.addAttribute("error","Sorry Merchant not Created.Try Again"); 
			        	 return "redirect:/admincreateMerchant";
					}
		        } else {
		        	 model.addAttribute("error","Invalid Captcha"); 
		        	 return "redirect:/admincreateMerchant";
		        }
			
			}
		
		
		@RequestMapping(value="internalRegisterationPage", method = RequestMethod.POST)
		public String registerInternalUser(@ModelAttribute("InternalUserSignUp")BossUser userEntity,  
				BindingResult result,Model model,
				//@RequestParam("recaptcha_challenge_field") String challangeField,
				//@RequestParam("recaptcha_response_field") String responseField,
				HttpServletRequest servletRequest,HttpServletResponse response) {
			
String r1=userEntity.getAddress();
			
			String r2=userEntity.getEmail();
			
			String r3=userEntity.getFname();
			
			String r4=userEntity.getLname();
			
			String r5=userEntity.getUname();
			
			String r6=userEntity.getSsn();
			
			String r8="";
			if (userEntity.getPhoneno()!=null){
			long r7=userEntity.getPhoneno();
			r8=String.valueOf(r7);
			}
			else {
				model.addAttribute("error","ip");
				return "redirect:/admincreateInternalUser";
			}
			
			if(!valphno(r8))
			{
			model.addAttribute("error","ip");
			return "redirect:/admincreateInternalUser";
			}
			
			if(!valadd(r1))
			{
			model.addAttribute("error","ia");
			return "redirect:/admincreateInternalUser";
			}
			
			if(!valssn(r6))
			{
			model.addAttribute("error","is");
			return "redirect:/admincreateInternalUser";
			}
			
			if(!valemails(r2))
			{
			model.addAttribute("error","ie");
			return "redirect:/admincreateInternalUser";
			}
			
			if(!valfn(r3))
			{
			model.addAttribute("error","if");
			return "redirect:/admincreateInternalUser";
			}
	
			if(!valfn(r4))
			{
			model.addAttribute("error","il");
			return "redirect:/admincreateInternalUser";
			}
			
			if(!valnumss(r5))
			{
			model.addAttribute("error","iu");
			return "redirect:/admincreateInternalUser";
			}
			
			
			 String remoteAddr = servletRequest.getRemoteAddr();
		        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		        reCaptcha.setPrivateKey("6LeHaP0SAAAAALdnTmrUmOH4BBvrWEt8KfBFZYrQ");
		        String challenge = servletRequest.getParameter("recaptcha_challenge_field");
		        String uresponse = servletRequest.getParameter("recaptcha_response_field");
		        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
		      
		        if (reCaptchaResponse.isValid()) {
		        	if (userService.userExists(userEntity.getUname())||userService.userSSNExists(userEntity.getSsn())){
		        		model.addAttribute("error","User Already Exists"); 
			        	 return "redirect:/admincreateInternalUser";
		        	}
		        	String status=userService.createInternalUser(userEntity);
					//if (status=="success"||status.equals("success")){
		        	if (status=="sent"||status.equals("sent")){
						//model.add
		        		model.addAttribute("error","Internal User Created"); 
			return "redirect:/admincreateInternalUser";}
					else {
						model.addAttribute("error","Sorry User not Created.Try Again"); 
			        	 return "redirect:/admincreateInternalUser";
					}
		        } else {
		        	 model.addAttribute("error","Invalid Captcha"); 
		        	 return "redirect:/admincreateInternalUser";
		        }
			
			}
		
		
		
		
		@RequestMapping(value="registrationPage", method = RequestMethod.POST)
		public String registerUser(@ModelAttribute("UserSignUp")BossUser userEntity,  
				BindingResult result,Model model,
				//@RequestParam("recaptcha_challenge_field") String challangeField,
				//@RequestParam("recaptcha_response_field") String responseField,
				HttpServletRequest servletRequest,HttpServletResponse response) {
			
String r1=userEntity.getAddress();
			
			String r2=userEntity.getEmail();
			
			String r3=userEntity.getFname();
			
			String r4=userEntity.getLname();
			
			String r5=userEntity.getUname();
			
			String r6=userEntity.getSsn();
			
			String r8="";
			if (userEntity.getPhoneno()!=null){
			long r7=userEntity.getPhoneno();
			r8=String.valueOf(r7);
			}
			else {
				model.addAttribute("error","ip");
				return "redirect:/admincreateExternalUser";
			}
			
			
			
			
			if(!valphno(r8))
			{
			model.addAttribute("error","ip");
			return "redirect:/admincreateExternalUser";
			}
			
			if(!valadd(r1))
			{
			model.addAttribute("error","ia");
			return "redirect:/admincreateExternalUser";
			}
			
			if(!valssn(r6))
			{
			model.addAttribute("error","is");
			return "redirect:/admincreateExternalUser";
			}
			
			if(!valemails(r2))
			{
			model.addAttribute("error","ie");
			return "redirect:/admincreateExternalUser";
			}
			
			if(!valfn(r3))
			{
			model.addAttribute("error","if");
			return "redirect:/admincreateExternalUser";
			}
	
			if(!valfn(r4))
			{
			model.addAttribute("error","il");
			return "redirect:/admincreateExternalUser";
			}
			
			if(!valnumss(r5))
			{
			model.addAttribute("error","iu");
			return "redirect:/admincreateExternalUser";
			}
			
			
			 String remoteAddr = servletRequest.getRemoteAddr();
		        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		        reCaptcha.setPrivateKey("6LeHaP0SAAAAALdnTmrUmOH4BBvrWEt8KfBFZYrQ");
		        String challenge = servletRequest.getParameter("recaptcha_challenge_field");
		        String uresponse = servletRequest.getParameter("recaptcha_response_field");
		        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
		      
		        if (reCaptchaResponse.isValid()) {
		        	if (userService.userExists(userEntity.getUname())||userService.userSSNExists(userEntity.getSsn())){
		        		model.addAttribute("error","User Already Exists"); 
			        	 return "redirect:/admincreateExternalUser";
		        	}
		        	String status=userService.createUser(userEntity);
					//if (status=="success"||status.equals("success")){
		        	if (status=="sent"||status.equals("sent")){
						//model.add
		        		model.addAttribute("error","Customer Created");
			return "redirect:/admincreateExternalUser";}
					else {
						model.addAttribute("error","Sorry User not Created.Try Again"); 
			        	 return "redirect:/admincreateExternalUser";
					}
		        } else {
		        	 model.addAttribute("error","Invalid Captcha"); 
		        	 return "redirect:/admincreateExternalUser";
		        }
			
			}
		
		
		

		@RequestMapping(value = "/login", method = RequestMethod.GET)
		public ModelAndView login(@RequestParam(value = "error", required = false) String error,
				@RequestParam(value = "logout", required = false) String logout,@RequestParam(value = "msg", required = false) String msg,Principal prin) {

			ModelAndView model = new ModelAndView();
			if (error != null) {
				if (!(error.equals("error")||error=="error")){
					model.addObject("error", error);
				}
				else{
				model.addObject("error", "Invalid username and password!");
				}
			}
			if (msg != null) {
				model.addObject("msg", msg);
			}
			if (logout != null) {
				model.addObject("msg", "You've been logged out successfully.");
			}
			model.setViewName("login");

			return model;

		}
		
		@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
		public String forgotPassword(Model model,HttpServletRequest servletRequest,@RequestParam(value = "error", required = false) String error) {

			//ModelAndView model = new ModelAndView();
			//Model model = new Model();
		//	String status = userService.forgotPassword();
			  model.addAttribute("forgotpassword", new ForgotPassword());
			//model.setViewName("UserSignUp");

			return "forgotPassword";

		}
		
		
		@RequestMapping(value="forgotPasswordAction", method = RequestMethod.POST)
		public String forgotPassword(@ModelAttribute("forgotpassword")ForgotPassword forgotPassword, 
				BindingResult result,
				//@RequestParam("recaptcha_challenge_field") String challangeField,
				//@RequestParam("recaptcha_response_field") String responseField,
				HttpServletRequest servletRequest,HttpServletResponse response,Model model) {
			
				String str=forgotPassword.getUserName();
				
				
				 String remoteAddr = servletRequest.getRemoteAddr();
			        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
			        reCaptcha.setPrivateKey("6LeHaP0SAAAAALdnTmrUmOH4BBvrWEt8KfBFZYrQ");
			        String challenge = servletRequest.getParameter("recaptcha_challenge_field");
			        String uresponse = servletRequest.getParameter("recaptcha_response_field");
			        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
			            
			        if (reCaptchaResponse.isValid()) {
			        	
			if(!valfn(str))
			{
				model.addAttribute("error","invalidusername");
				return "redirect:/login";
			}
			
			
			if (!userService.userExists(str)){
				model.addAttribute("error","User doesn't exists");
				return "redirect:/login";
			}
			int loginAttempts= userLoginService.getLoginAttempts(forgotPassword.getUserName());
			String status = userService.forgotPassword(forgotPassword.getUserName(),loginAttempts);
			if (status.equals("sent")||status=="sent")
		return "redirect:/resetPassword";
			else
				return "redirect:/login";
			        }
			        else{
			        	model.addAttribute("error","Invalid Captcha");
			        	return "redirect:/login";	
			        }
		}	

		@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
		public String resetPassword(Model model,@RequestParam(value = "error", required = false) String error) {

			//ModelAndView model = new ModelAndView();
			//Model model = new Model();
		//	String status = userService.forgotPassword();
			  model.addAttribute("resetpassword", new ResetPassword());
			//model.setViewName("UserSignUp");
			  if(error!=null)
			  {
				  	if(error.equals("ip"))
				  	model.addAttribute("error", "Invalid Password");
				  	else if(error.equals("id"))
				  	model.addAttribute("error", "Passwords doesn't match");
				  	else if(error.equals("iu"))
					  	model.addAttribute("error", "Invalid UserName");
					else if(error.equals("io"))
					  	model.addAttribute("error", "Invalid OTP");
				  	else {
				  		model.addAttribute("error", error);
				  	}
				  	
			  }
			return "resetPassword";

		}
		
		@RequestMapping(value = "/sendOTP", method = RequestMethod.GET)
		public String sendOTP(Model model) {

			//ModelAndView model = new ModelAndView();
			//Model model = new Model();
		//	String status = userService.forgotPassword();
			
			  model.addAttribute("msg","Invalid OTP. Sent Again. Retry");
			//model.setViewName("UserSignUp");
       
			return "redirect:/resetPassword";

		}
		
		
		@RequestMapping(value="resetPasswordAction", method = RequestMethod.POST)
		public String resetPassword(@ModelAttribute("resetpassword")ResetPassword resetPassword, 
				BindingResult result,
				//@RequestParam("recaptcha_challenge_field") String challangeField,
				//@RequestParam("recaptcha_response_field") String responseField,
				ServletRequest servletRequest,Model model) {
			String status="";
String r1=resetPassword.getConfirmPassword();
			
if (!(userService.userExists(resetPassword.getUserName()))){
	model.addAttribute("error","User doesn't exists");
	return "redirect:/resetPassword";
}
			String r2=resetPassword.getPassword();
			
			String r4="";
			if (resetPassword.getOtp()!=null){
			long r7=resetPassword.getOtp();
			r4=String.valueOf(r7);
			}
			else {
				model.addAttribute("error","io");
				return "redirect:/resetPassword";
			}
			
			String r5=resetPassword.getUserName();
			
			if(!valnums(r4))
			{
				model.addAttribute("error","io");
				return "redirect:/resetPassword";
			}
			
			if(!valnumss(r5))
			{
				model.addAttribute("error","iu");
				return "redirect:/resetPassword";
			}
			
			
			if(!valnum(r1))
			{
				model.addAttribute("error","ip");
				return "redirect:/resetPassword";
			}
			
			if(!valnum(r2))
			{
				model.addAttribute("error","ip");
				return "redirect:/resetPassword";
			}
			
			if(r1.equals(r2) || r1==r2)
			{
			boolean updateResult=userLoginService.validateOTP(resetPassword.getUserName(),resetPassword.getOtp());
			if (updateResult){
			status = userService.resetPassword(resetPassword.getUserName(),resetPassword.getPassword());
			}
			else{
				if(userLoginService.resetOTP(resetPassword.getUserName())){
					//userLoginService.sendOTP(resetPassword.getUserName());
					model.addAttribute("error","Wrong or Expired OTP, Retry with Forgot Password Link");
					return "redirect:/resetPassword";
				}
			}
			if (status.equals("success")||status=="success")
		return "redirect:/login";
			else
				return "redirect:/login";
			}
else
				
			{
				model.addAttribute("error","id");
				
				return "redirect:/resetPassword";
			
			}	
		}	
		//for 403 access denied page
		@RequestMapping(value = "/403", method = RequestMethod.GET)
		public ModelAndView accesssDenied() {

			ModelAndView model = new ModelAndView();
			
			//check if user is login
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (!(auth instanceof AnonymousAuthenticationToken)) {
				UserDetails userDetail = (UserDetails) auth.getPrincipal();
				System.out.println(userDetail);
			
				model.addObject("username", userDetail.getUsername());
				
			}
			
			model.setViewName("403");
			return model;

		}
		
		private boolean valnum(String number) {
			 
			 String type="\\b^[a-zA-Z0-9_.@]{4,10}$\\b";
					 	 
			 Pattern pat = Pattern.compile(type);
			 
			 Matcher mat = pat.matcher(number);
			 
			 if(mat.find()) 
			 {
			  return true;
			  }
			 return false;
			 }
		
		private boolean valnums(String number) {
			 
			 String type="-?[0-9]+$";
					 	 
			 Pattern pat = Pattern.compile(type);
			 
			 Matcher mat = pat.matcher(number);
			 
			 if(mat.find()) 
			 {
			  return true;
			  }
			 return false;
			 }
		
		private boolean valnumss(String number) {
			 
			 String type="\\b^[a-zA-Z0-9_.@]{4,10}$\\b";
					 	 
			 Pattern pat = Pattern.compile(type);
			 
			 Matcher mat = pat.matcher(number);
			 
			 if(mat.find()) 
			 {
			  return true;
			  }
			 return false;
			 }
		
		private boolean valadd(String number) {
			 
			 String type="\\b^[a-zA-Z0-9_.#-]{4,20}$\\b";
					 	 
			 Pattern pat = Pattern.compile(type);
			 
			 Matcher mat = pat.matcher(number);
			 
			 if(mat.find()) 
			 {
			  return true;
			  }
			 return false;
			 }
		
		private boolean valemails(String email) {
			 
			 String type="\\b[A-Za-z0-9._%+-]+[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,4}\\b";
					 
			 Pattern pat = Pattern.compile(type);
			 Matcher mat = pat.matcher(email);
			 
			 if(mat.find()) 
			 {
			  return true;
			  }
			 return false;
		}
		
		private boolean valfn(String fname) {
			 
			 String type="\\b^[a-zA-Z0-9_]{4,10}$\\b";
					 	 
			 Pattern pat = Pattern.compile(type);
			 
			 Matcher mat = pat.matcher(fname);
			 
			 if(mat.find()) 
			 {
			  return true;
			  }
			 return false;
			 }
		
		private boolean valssn(String fname) {
			 
			 String type="\\b^[\\d{3}\\-\\d{2}\\-\\d{4}]+$\\b";
					 	 
			 Pattern pat = Pattern.compile(type);
			 
			 Matcher mat = pat.matcher(fname);
			 
			 if(mat.find()) 
			 {
			  return true;
			  }
			 return false;
			 }
		
		private boolean valphno(String email) {
			 
			 String type="\\b^[0-9]{10}\\b";
					 	 
			 Pattern pat = Pattern.compile(type);
			 Matcher mat = pat.matcher(email);
			 
			 if(mat.find()) 
			 {
			  return true;
			  }
			 return false;
			 }

}
