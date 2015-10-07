package boss.boss_controller;


import java.io.File;
import java.io.FileInputStream;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import boss.boss_rs.BossUserService;
import boss.data.entities.BossAccount;
import boss.data.entities.BossExtTransaction;
import boss.data.entities.BossUser;
import boss.utilities.GenerateCertificate;


@Controller
public class UserController {


@SuppressWarnings("unused")
private static final String String = null;

@Autowired
private BossUserService userService;

	
	@RequestMapping(value = "/extuser", method = RequestMethod.GET)
	public ModelAndView extuserPage() {

		ModelAndView model = new ModelAndView();
		model.setViewName("extuser/extuserhome");

		return model;

	}
	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public ModelAndView welcomePage() {

		ModelAndView model = new ModelAndView();
		
		model.setViewName("BossHome");
		
		return model;

	}
	@RequestMapping(value = "/extusertransactiondetails")
	public String transactiondetails(Model model,Principal prin) {

		String str=prin.getName();
		List<BossExtTransaction> result=userService.viewtransactiondetails(str);
		
		if (null==result){
			
			model.addAttribute("piiautho","Oops!Something went wrong try again");
			
			return "extuser/extuseraction";
			}
		
		int r=result.size();
		
		if(r==0)
		{
			model.addAttribute("piiautho","No exisiting transactions");
		}
		else
			model.addAttribute("extusertrandetails",result);
			
		return "extuser/extusertrandetails";
	
	}
	
		
	@RequestMapping(value = "/extusertransrequest/extuserequest")
	public String requestdetails(Principal prin,Model model) {

		String str=prin.getName();
		String r=userService.Authorize(str);
		
		String s="fail";
		
		if(r.equals(s))
			
		{
			model.addAttribute("piiautho","Oops!Something went wrong try again");
		}
		
		else
				model.addAttribute("tex","Authorized Successfully");
		
		
		
		return "extuser/extuseraction";
	
	}
			
	
	@RequestMapping(value = "/extuserprofiledetails")
	public String profiledetails(Model model,Principal prin) {

		 	      
		 String name=prin.getName();
	      
		List<BossUser> result =userService.viewprofiledetails(name);
		
		if (null==result){
			
			model.addAttribute("piiautho","Oops!Something went wrong try again");
			
			return "extuser/extuseraction";
			}
		int r=result.size();
		
		if(r==0)
		
		{
			model.addAttribute("piiautho","No records found");
			
		}
		
		else
			
			model.addAttribute("userprofiledetails",result);
		
		return "extuser/extuserprofiledetails";

	}
	
	@RequestMapping(value = "/extusercreditdebit")
	public ModelAndView creditdebit() {

		ModelAndView model = new ModelAndView();
		
		model.setViewName("extuser/extusercreditdebit");

		return model;

	}
	
	
	
	@RequestMapping(value ="/extusertransrequest")
	public ModelAndView tranrequest(Principal prin) {
	
		String name=prin.getName();
	ModelAndView model=new ModelAndView();
		
	String status=null;
	
	int count=0;
		
	List<BossExtTransaction> result=userService.viewtransactiondetail(name);
	
	if (null==result){
		
	model.addObject("piiautho","Oops!Something went wrong try again");
	
	model.setViewName("extuser/extuseraction");
	}
	
	count=result.size();
		
	if(count==0)
		
	status="F";
	
	else
	{
		status="T";
			
		model.addObject("extusertranrequest",result);
		
	}
		model.addObject("status",status);
		
		model.setViewName("extuser/extusertranrequest");
		
		return model;
	
	}
	
	@RequestMapping(value = "/extusertransferfunds")
	public ModelAndView transferfunds() {

		ModelAndView model = new ModelAndView();
		model.setViewName("extuser/extuserfundstransfer");

		return model;

	}
	

	@RequestMapping(value = "/extusertransferfunds/fundtransfer", method = RequestMethod.POST)
	public String transferfund(@RequestParam("account") String account,@RequestParam("amount") String amount,Model model,Principal prin,@RequestParam("file") MultipartFile [] files,
			HttpServletRequest request) 
	
	{
			
		String name=prin.getName();
		
		String a=null;
		
		String status=null;
		
		String s1="fail";
		
		String s2="true";
		
		if(!valacc(account))
		{
			
				model.addAttribute("account","Please enter valid account number ");
				
				return "extuser/extuseraction";			
		}	 
		
		if(!valnum(amount))
		{
			
				model.addAttribute("amount","Please enter valid amount(Max:XXXXX)");
				
				return "extuser/extuseraction";			
		}
				
			
		double amnt = Double.parseDouble(amount);
		
//		long accnt = Long.parseLong(account);
		MultipartFile signedCertiFile=null, keyFile = null;
		boolean isSignVerified = false;

		try
		{			
			signedCertiFile=files[0];
			//keyFile=files[1];
		}
		catch(Exception e){
			e.printStackTrace();
		}

		if(signedCertiFile.getSize()==0){
			

		model.addAttribute("fileerror","Please upload valid files");
		return "extuser/extuseraction";
		}

		try{
			byte[] signedcertificate = signedCertiFile.getBytes();
			byte[] certiFile = IOUtils.toByteArray(new FileInputStream(new File("./"+prin.getName()+"certificate")));
			if(signedcertificate!=null && certiFile!=null){
				byte[] pubKey = IOUtils.toByteArray(new FileInputStream(new File("./"+prin.getName()+"DSAPublicKey.key")));
				isSignVerified = GenerateCertificate.Verify(signedcertificate, certiFile,pubKey);
				
			}
				else{
					
					model.addAttribute("encodingerror","Error encoding the certificate. Please input valid files");
					return "extuser/extuseraction";
			}

			
			 
				
				//boolean certVerify= Arrays.equals(certiByteArray,certKeyBytes) ;
				if(isSignVerified){
				
					String r=userService.getaccount(account,name);
					
					if(r.equals(s1))
					{
						
			model.addAttribute("piiautho","Oops!Something went wrong try again");
						
						return "extuser/extuseraction";
					}
					
					if(r.equals(s2))
					{
						model.addAttribute("accnu","Amount cannot be transfered to the self acccount");
						
						return "extuser/extuseraction";
					}
					else
					{
					String b=userService.fundtransfer(account,amnt,name);
				
		if(b.startsWith("false"))
					{
			String[] errArgs = b.split(",");
			model.addAttribute("wrongs",errArgs[1]);
						
						return "extuser/extuseraction";
					}
					
					if(b.equals(s1))
					{
			model.addAttribute("piiautho","Oops!Something went wrong try again");
						
						return "extuser/extuseraction";
					}
					
					else if(b.equals(s2))
					{
						a="Transfer Successful";
						
						status="Transfer Success";
						
						userService.transactionFunds(account,amnt,name,status); // Priya changes
						
						
					}
					else
					{				
						a="Transfer Failed";
						
						status="Failed Transfer";
						
						userService.transactionFunds(account,amnt,name,status); // Priya changes
						
						
					}	
					
					}
					model.addAttribute("statu",a);
							
					return "extuser/extuseraction";
					
					}

				else{
					model.addAttribute("invalidFile","Encrypted file is invalid. You might have used the wrong key or certificate. ");
					return "extuser/extuseraction";
				}
		}
		catch(Exception e){
			e.printStackTrace();
			model.addAttribute("tranferfailed","Sorry");
			return "extuser/extuseraction";
		}

		

	}
	
	@RequestMapping(value = "/extusercreditdebit/extusercredit", method = RequestMethod.POST)
	public String Credit(@RequestParam("credit") String credit,Model model,Principal prin)
	{

		String name=prin.getName();				
		String a=null;
		
		String status=null;
		
		if(!valnum(credit))
		{
			model.addAttribute("number","Please enter valid amount(Ex:xxxxx)");
			
			return "extuser/extuseraction";			
		}
		
		double amnt = Double.parseDouble(credit);
		
			
		String b=userService.credit(amnt,name);
		
		String str="fail";
		
		String str1="true";
		
		if(b.equals(str))
		{
			model.addAttribute("piiautho","Oops!Something went wrong try again");
		}
		
		else if(b.equals(str1))
		{
			a="Credited Successfully";
			
			status="Success";
					
			userService.saveTranscredit(amnt,name,status);
			
		}
		else
		{
			a="Crediting Failed";
			
			status="Failed";
			
			userService.saveTranscredit(amnt,name,status);
			
						
		}
		model.addAttribute("status",a);
					
		return "extuser/extuseraction";

	}
	

	@RequestMapping(value = "/extusercreditdebit/extuserdebit", method = RequestMethod.POST)
	public String debit(@RequestParam("debit") String debit,Model model,Principal prin)
	{

		String name=prin.getName();
		
		String a=null;
		
		String status=null;
		
		if(!valnum(debit))
		{
			model.addAttribute("number","Please enter valid amount(Ex:xxxxx)");
			
			return "extuser/extuseraction";			
		}
		
		double amnt = Double.parseDouble(debit);
		
		String b=userService.debit(amnt,name);
		
		String s1="fail";
		
		String s2="true";
		
		if(b.equals(s1))
		{
			model.addAttribute("piiautho","Oops!Something went wrong try again");
		}		
		else if(b.equals(s2))
		{
			a="Debited Successfully";
			
			status="Success";
			
			userService.saveTransdebit(amnt,name,status);
		
		}
		else
		{
			a="Insufficient amount";
			
			status="Debiting Failed";
			
			userService.saveTransdebit(amnt,name,status);
						
		}
		model.addAttribute("mesg",a);
	
		return "extuser/extuseraction";

	}
	
	@RequestMapping(value = "/extuseraccountdetails")
	public String accountdetails(Model model,Principal prin) {
			
		String str=prin.getName();
				
		List<BossAccount> result=userService.viewaccountdetails(str);
		if (null==result){
			
			model.addAttribute("piiautho","Oops!Something went wrong try again");
			
			return "extuser/extuseraction";
			}
		
		int r= result.size();
		
		if(r==0)
		
		{
			
			model.addAttribute("condition","No details found");
			
		}
		
		else{
			
			model.addAttribute("useraccountdetails",result);
		}
		
		return "extuser/extuserAccountdetails";

	}
	
	@RequestMapping(value = "/extuserprofiledetails/update",method = RequestMethod.GET)
	public String updateaccountdetails(Model model)
		{
	
		model.addAttribute("update","Update Profile");
				
		return "extuser/extuserupdateprofile";

		}
	
	
	
	@RequestMapping(value = "/extuserpiiauth",method = RequestMethod.GET)
	public String piiauthentication(Principal prin,Model model)
		{
		
		String name=prin.getName();
		
		String b=userService.viewpiiauthentication(name);
		
		String status=null;
		
		String r1="true";
				
		String r3="failed";
		
		if(b.equals(r3))
		{
			model.addAttribute("piiauth","Oops!Something went wrong try again");
		}
		
		else if(b.equals(r1))
		{
			status="T";
		}
		
		else
		{
			status="F";
			
		}
			
		model.addAttribute("status",status);
		
		
		return "extuser/extuserPII";

		}
		
	
	
	@RequestMapping(value = "/extuserpiiauth/piiauthorize",method = RequestMethod.GET)
	public String piiautherize(Principal prin,Model model)
		{
		
		String name=prin.getName();
		
		String b=userService.piiauthentication(name);
		
		String status=null;
		
		String r1="true";
		
		String r2="fail";
		
		if(b.equals(r2))
		{
			model.addAttribute("piiautho","Oops!Something went wrong try again");
		}
		else if(b.equals(r1))
		{
			status="Authorized Successfully";
		}
		
		else
		{
			status="Authorization Failed";
			
		}
			
		model.addAttribute("pii",status);
		
		
		return "extuser/extuseraction";

		}
	
	@ModelAttribute
	public void greetings(Model model)
	{
		model.addAttribute("welcome","WELCOME TO BOSS");
		model.addAttribute("message","THE SECURED BANKING SYSTEM");
	}
	
	@RequestMapping(value = "/adminview", method = RequestMethod.GET)
	public ModelAndView adminPag() {

		ModelAndView model = new ModelAndView();
		
		model.setViewName("extuser/account");

		return model;

	}
	
	@RequestMapping(value = "/extuserprofiledetails/update/extusers", method = RequestMethod.POST)
	public String adminPa(Principal prin,@RequestParam("fname") String Fname,@RequestParam("lname") String Lname,
				@RequestParam("address") String Address,@RequestParam("phoneno") String Phoneno,@RequestParam("email") String Email,Model model) {

		String name=prin.getName();
		
	if(!valfname(Fname))
		{
			
			model.addAttribute("Fname","Please enter a valid FirstName");
			return "extuser/extuseraction";
		}
		if(!valfname(Lname))
		{
			
			model.addAttribute("Lname","Please enter a valid LastName");
			return "extuser/extuseraction";
		}
		if(!valadd(Address))
		{
			
			model.addAttribute("Address","Please enter a valid Address");
			return "extuser/extuseraction";
		}
		if(!valemail(Email))
		{
			
			model.addAttribute("email","Please enter a valid EMAIL ID");
			return "extuser/extuseraction";
		}
		
		if(!valphno(Phoneno))
		{
			model.addAttribute("phone","Please enter a 10 digit number");
			return "extuser/extuseraction";
		}
		
		long Phone = Long.parseLong(Phoneno);
		
		String b=userService.updateprofiledetails(name,Fname,Lname,Address,Phone,Email);		
		
		String s1="fail";
		
		String s2="true";
		
		if(b.equals(s1))
			
		{
			model.addAttribute("piiauth","Oops!Something went wrong try again");
		}
		
		else if(b.equals(s2))
		{
			
			model.addAttribute("masge","Request submitted pending approval");
			
		
		}
		
		else
		{
						
			model.addAttribute("masge","You can't raise a new request,old request is pending for approval");
		}
		
		
		return "extuser/extuseraction";

	}
	

	
	@RequestMapping(value = "/extusertransactiondetails/modifytransaction", method = RequestMethod.GET)
	public String modifytransaction(Model model)
		{
							
		return "extuser/extuserrevoke";

		}
	
	
	@RequestMapping(value = "/extuserdelete", method = RequestMethod.GET)
	public String deleteUser(Principal prin,Model model)
		{
		
		String name=prin.getName();
		
		String b=userService.deleteuser(name);
		
		String r1="fail";
		
		if(b.equals(r1))
			
		{
			model.addAttribute("deluse","Oops!Something went wrong try again");
		}
		else if(b.equals("true"))
			
		{
			model.addAttribute("delete","Request Submitted");
		}
		
		else
						
			model.addAttribute("delete","Request Submission failed due to pending account requests,contact bank directly");
		
		return "extuser/extuseraction";

		}	
	
	@RequestMapping(value = "/extuserevoke", method = RequestMethod.GET)
	public ModelAndView revokeTransaction(Principal prin)
		{
		
		String name=prin.getName();
		
		ModelAndView model=new ModelAndView();
		
		List <BossExtTransaction> result=userService.viewtransactiondetailss(name);
		
		if (null==result){
			
			model.addObject("piiautho","Oops!Something went wrong try again");
			
			model.setViewName("extuser/extuseraction");
			}
		
		int count=result.size();
		
		String status = null;
		
	 	if(count == 0)
	 		
	 	status = "F";
	 	
	 	else
	 	{
	 	status = "S";
	 	
		model.addObject("revoke",result);
	 	}
	 	model.addObject("status",status);
	 	
	 	model.setViewName("extuser/extuserrevoke");
	 	
	 	return model;

		}
	
	
	@RequestMapping(value = "/extuserevoke/revoke", method = RequestMethod.POST)
	public ModelAndView revoketransaction(Principal prin,@RequestParam("tranid") long tranid)
			
		{
		
		String name=prin.getName();
		
		ModelAndView model=new ModelAndView();
		
		String b=userService.revoketrans(name,tranid);
		
		String r="fail";
		
		String r1="true";
		
		if(b.equals(r))
		{
			model.addObject("piiautho","Oops!Something went wrong try again");
		}
		else if(b.equals(r1))
				
		model.addObject("revoke","Request Submitted");
							
		else
			
		model.addObject("revoke","Failed submission");
			 	 	
		model.setViewName("extuser/extuseraction");
				
	 	return model;

		}

	
	
	
	@RequestMapping(value = "/extuserPayment", method = RequestMethod.GET)
	public String extuserpayment(Model model)
		{
		
		List<String[]> result=userService.extmerchantview();
		
		int i=result.size();
		
		if(i==0)
		{
			model.addAttribute("helo","There are no merchants available");
			
			return "extuser/extuseraction";
		}
		else
		{
	    				
	 	model.addAttribute("hi",result);
		}
		
	 	return "extuser/extuserpayment";

		}
	
	@RequestMapping(value = "/extuserPayment/extuserMerchant", method = RequestMethod.POST)
	
	public String merchantSelect(Model model,@RequestParam("initpay") String merSel)
		{
			    		
		
		model.addAttribute("merchant",merSel);
		
	 	return "extuser/extuserPaymentForm";

		}

	
	@RequestMapping(value = "/extuserPayment/extuserMerchant/fillForm", method = RequestMethod.POST)
	
	public String extusermerchantpay(Principal prin,Model model,@RequestParam("uname") String Uname,@RequestParam("account") String Account,@RequestParam("amount") String Amount,@RequestParam("merchant") String Merchant)
		{
		
		String name=prin.getName();
		
		if(!valfname(Uname))
		{
			model.addAttribute("usernames","Enter Valid Username");
			
			return "extuser/extuseraction";
		}
		
		if(!valacc(Account)) //praneeth changed
		{
			model.addAttribute("accounts","Enter Valid Account");
			
			return "extuser/extuseraction";
		}
		
		if(!valnum(Amount))
		{
			model.addAttribute("numbers","Enter Valid Amount");
			
			return "extuser/extuseraction";
		}
		
		if(name.equals(Uname))
		{
						    			
		double Amounts=Double.parseDouble(Amount);
		
		//long Accounts = Long.parseLong(Account);
		
					String r=userService.getaccount(Account,name);
					
					String r1="false";
					String r2="fail";
					
					if(r.equals(r2))
							
							{
						model.addAttribute("piiautho","Oops!Something went wrong try again");
						return "extuser/extuseraction";
						
						
							}
					
					else if(r.equals(r1))
					
					{
						model.addAttribute("wrong","Wrong account number");
						
						return "extuser/extuseraction";
					}
					else
					{
		boolean b=userService.extMerchantPay(Uname,Account,Amounts,Merchant);
		
		if(b==true)
			
			model.addAttribute("pay","Request Submitted");
				
		else
			model.addAttribute("piiautho","Oops!Something went wrong try again");
					}
		}
		
		else
			
		{
			model.addAttribute("devuda","WrongUserName please try again");
		}
	 	return "extuser/extuseraction";

		}
	
	
	
		
	private boolean valemail(String email) {
					 
		 String type="\\b[A-Za-z0-9._%+-]+[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,4}\\b";
				 
		 Pattern pat = Pattern.compile(type);
		 Matcher mat = pat.matcher(email);
		 
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
	
	private boolean valfname(String fname) {
		 
		 String type="\\b^[a-zA-Z0-9_]+$\\b";
				 	 
		 Pattern pat = Pattern.compile(type);
		 
		 Matcher mat = pat.matcher(fname);
		 
		 if(mat.find()) 
		 {
		  return true;
		  }
		 return false;
		 }
			
	private boolean valadd(String fname) {
		 
		 String type="\\b^[a-zA-Z0-9_./#&+-:]+$\\b";
				 	 
		 Pattern pat = Pattern.compile(type);
		 
		 Matcher mat = pat.matcher(fname);
		 
		 if(mat.find()) 
		 {
		  return true;
		  }
		 return false;
		 }
	
	private boolean valnum(String number) {
		 
		 String type="\\b^[0-9.]{1,5}$\\b";
				 	 
		 Pattern pat = Pattern.compile(type);
		 
		 Matcher mat = pat.matcher(number);
		 
		 if(mat.find()) 
		 {
		  return true;
		  }
		 return false;
		 }
	
	private boolean valacc(String number) {
		 
		 String type="\\b^[0-9]+$\\b";
				 	 
		 Pattern pat = Pattern.compile(type);
		 
		 Matcher mat = pat.matcher(number);
		 
		 if(mat.find()) 
		 {
		  return true;
		  }
		 return false;
		 }
		}