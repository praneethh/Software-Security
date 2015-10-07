package boss.boss_controller;

import java.security.Principal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import boss.boss_rs.BossMerchantService;
import boss.data.entities.BossAccount;
import boss.data.entities.BossExtTransaction;
import boss.data.entities.BossExtTransactionTemp;
import boss.data.entities.BossUser;

@Controller
public class MerchantController {
	
	@Autowired
	private BossMerchantService merchantService;
	
	@RequestMapping(value = "/merchant", method = RequestMethod.GET)
	public ModelAndView extuserPage() {

		ModelAndView model = new ModelAndView();
		model.setViewName("merchant/merchanthome");

		return model;

	}
	
	@RequestMapping(value = "/merchantaccountdetails")
	public String merchantaccountdetails(Model model,Principal prin) {
		
		String str=prin.getName();
							
		List<BossAccount> result=merchantService.viewaccountdetails(str);
		
		if (null==result){
			
			model.addAttribute("piiautho","Oops!Something went wrong try again");
			
			return "merchantuser/merchantaction";
			}
		
		int r= result.size();
		
		if(r==0)
		
		{
		model.addAttribute("condition","No transactions found");
			
		}
		
		else
			
			model.addAttribute("merchantaccountdetails",result);
		
		return "merchant/merchantaccountdetails";

	}

	

@RequestMapping(value = "/merchanttransactiondetails")
public String merchanttransactiondetails(Model model,Principal prin) {
	
	String str=prin.getName();

	List<BossExtTransaction> result=merchantService.viewtransactiondetails(str);
	
	if (null==result){
		
		model.addAttribute("piiautho","Oops!Something went wrong try again");
		
		return "merchantuser/merchantaction";
		}
	
	int r=result.size();
	
	if(r==0)
	{
		model.addAttribute("piiautho","No transactions found");
	}
	else
		model.addAttribute("merchanttrandetails",result);
		
	return "merchant/merchanttrandetails";

}

@RequestMapping(value = "/merchantprofiledetails")
public String merchantprofiledetails(Model model,Principal prin) {

	String str=prin.getName();
	List<BossUser> result =merchantService.viewprofiledetails(str);
	
	if (null==result){
		
		model.addAttribute("piiautho","Oops!Something went wrong try again");
		
		return "merchantuser/merchantaction";
		}
	
	int r=result.size();
	
	if(r==0)
	
	{
		model.addAttribute("piiautho","No details found");
		
	}
	
	else
		
		model.addAttribute("merchantprofiledetails",result);
	
	return "merchant/merchantprofiledetails";

}

@RequestMapping(value = "/merchantprofiledetails/merchantupdate",method = RequestMethod.GET)
public String merchantupdateaccountdetails(Model model)
	{

	model.addAttribute("update","Update Profile");
			
	return "merchant/merchantupdateprofile";

	}

@RequestMapping(value = "/merchantprofiledetails/merchantupdate/merchants", method = RequestMethod.POST)
public String merchantupdatep(Principal prin,@RequestParam("fname") String Fname,@RequestParam("lname") String Lname,
			@RequestParam("address") String Address,@RequestParam("phoneno") String Phoneno,@RequestParam("email") String Email,Model model) {

	String name=prin.getName();
if(!valfnames(Fname))
	{
		
		model.addAttribute("Fname","Please enter a valid FirstName");
		return "merchant/merchantaction";
	}
	if(!valfnames(Lname))
	{
		
		model.addAttribute("Lname","Please enter a valid LastName");
		return "merchant/merchantaction";
	}
	if(!valadds(Address))
	{
		
		model.addAttribute("Address","Please enter a valid Address");
		return "merchant/merchantaction";
	}
	if(!valemails(Email))
	{
		
		model.addAttribute("email","Please enter a valid EMAIL ID");
		return "merchant/merchantaction";
	}
	
	if(!valphnos(Phoneno))
	{
		model.addAttribute("phone","Please enter a 10 digit phone number");
		return "merchant/merchantaction";
	}
	
	long Phone = Long.parseLong(Phoneno);
	
	String b=merchantService.updateprofiledetails(name,Fname,Lname,Address,Phone,Email);		
	
	String s1="fail";
	
	String s2="true";
	
	if(b.equals(s1))
	
	model.addAttribute("piiauth","Oops!Something went wrong try again");
		
	else if(b.equals(s2))
	{
		model.addAttribute("masge","Request Submitted,awaiting Approval");	
	
	}
	
	else
	{
				
		model.addAttribute("masge","You can't raise a new request,old request is pending for approval");
	}
	
	return "merchant/merchantaction";

}

@RequestMapping(value = "/merchantcreditdebit/merchantcredit", method = RequestMethod.POST)
public String merchantCredit(@RequestParam("credit") String credit,Principal prin,Model model)
{

	String name=prin.getName();
	
	String a=null;
	
	String status=null;
	
	if(!valnums(credit))
	{
		model.addAttribute("number","Please enter the amount correctly(EX:xxxxx)");
		
		return "merchant/merchantaction";			
	}
	
	double amnt = Double.parseDouble(credit);
	
			
	String b=merchantService.credit(amnt,name);
	
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
				
		merchantService.saveTranscredit(amnt,name,status);
		
	}
	else
	{
		a="Crediting Failed";
		
		status="Failed";
		
		merchantService.saveTranscredit(amnt,name,status);
				
		
	}
	model.addAttribute("status",a);
	
		
	return "merchant/merchantaction";

}


@RequestMapping(value = "/merchantcreditdebit/merchantdebit", method = RequestMethod.POST)
public String merchantdebit(@RequestParam("debit") String debit,Model model,Principal prin)
{
	
	String name=prin.getName();
					
	String a=null;
	
	String status=null;
	
	if(!valnums(debit))
	{
		model.addAttribute("number","Please enter the amount correctly(Ex:xxxxx)");
		
		return "merchant/merchantaction";			
	}
	
	double amnt = Double.parseDouble(debit);
	
	String b=merchantService.debit(amnt,name);
	
	String s1="fail";
	
	String s2="true";
	
	if(b.equals(s1))
	
		model.addAttribute("piiautho","Oops!Something went wrong try again");
		
	else if(b.equals(s2))
	{
		a="Debited Successfully";
		
		status="Success";
		
		merchantService.saveTransdebit(amnt,name,status);
		
	}
	else
	{
		a="Insufficient amount";
		
		status="Debiting Failed";
		
		merchantService.saveTransdebit(amnt,name,status);
				
	}
	model.addAttribute("mesg",a);
	
	
	
	return "merchant/merchantaction";

}


@RequestMapping(value = "/merchantcreditdebit")
public ModelAndView merchantcreditdebit() {

	ModelAndView model = new ModelAndView();
	
	model.setViewName("merchant/merchantcreditdebit");

	return model;

}


@RequestMapping(value = "/merchanttransferfunds")
public ModelAndView merchanttransferfunds() {

	ModelAndView model = new ModelAndView();
	model.setViewName("merchant/merchantfundstransfer");

	return model;

}

@RequestMapping(value = "/merchanttransferfunds/fundtransfer", method = RequestMethod.POST)
public String transferfund(@RequestParam("account") String account,@RequestParam("amount") String amount,Principal prin,Model model) 

{
	String name=prin.getName();
		
	String a=null;
	
	String status=null;
	
	String s1="fail";
	
	String s2="true";
	
	if(!valaccs(account))
	{
		
			model.addAttribute("account","Please enter valid account number");
			
			return "merchant/merchantaction";			
	}	
	
	if(!valnums(amount))
	{
		
			model.addAttribute("amount","Please enter valid amount");
			
			return "merchant/merchantaction";			
	}
			
		
	double amnt = Double.parseDouble(amount);
			
	String r=merchantService.getaccount(account,name);
	
	if(r.equals(s1))
	{
		
		model.addAttribute("piiautho","Oops!Something went wrong try again");
		
		return "merchant/merchantaction";
	}
	
	if(r.equals(s2))
	{
		model.addAttribute("accnu","Amount cannot be transfered to the self acccount");
		
		return "merchant/merchantaction";
	}
	
					
	String b=merchantService.fundtransfer(account,amnt,name);
	
	if(b==null)
	{
		model.addAttribute("wrongs","Wrong Account Details");
		
		return "extuser/merchantaction";
	}		
	
	if(b.equals(s1))
	{
		model.addAttribute("piiautho","Oops!Something went wrong try again");
	}
	
	else if(b.equals(s2))
	{
		a="Transfer Successful";
		
		status="Transfer Success";
		
		merchantService.transactionFunds(account,amnt,name,status);
		
	}
	else
	{				
		a="Transfer Failed";
		
		status="Failed Transfer";
		
		merchantService.transactionFunds(account,amnt,name,status);
					
	}	
	
	model.addAttribute("statu",a);
	
	return "merchant/merchantaction";

}


@RequestMapping(value ="/merchanttransrequest")
public ModelAndView merchanttranrequest(Principal prin) {

	String name=prin.getName();
	
	
	ModelAndView model=new ModelAndView();
	
	String status=null;
	
	int count=0;
	
	List<BossExtTransaction> result=merchantService.viewtransactiondetail(name);

	if (null==result){
	
	model.addObject("piiautho","Oops!Something went wrong try again");
	
	model.setViewName("merchantuser/merchantaction");
	}
	count=result.size();
	
	if(count==0)
	
		status="F";

	else
	{
	status="T";
		
	model.addObject("merchanttranrequest",result);
	
	}
	model.addObject("status",status);
	
	model.setViewName("merchant/merchanttranrequest");
	
	return model;


}


@RequestMapping(value = "/merchanttransrequest/merchantrequest")
public String merchantrequestdetails(Principal prin,Model model) {

	String str=prin.getName();
			
	String r=merchantService.Authorize(str);
	
	String s="fail";
	
	if(r.equals(s))
		
	{
		model.addAttribute("piiautho","Oops!Something went wrong try again");
	}
	
	else
	{
	model.addAttribute("tex","Authorized Successfully");
	
	}
	
	return "merchant/merchantaction";

}

@RequestMapping(value = "/merchantpiiauth",method = RequestMethod.GET)
public String merchantpiiauthentication(Principal prin,Model model)
	{
	
	String name=prin.getName();
	
	String b=merchantService.viewpiiauthentication(name);
	
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
	
	return "merchant/merchantPII";

	}


@RequestMapping(value = "/merchantpiiauth/merchantpiiauthorize",method = RequestMethod.GET)
public String merchantpiiautherize(Principal prin,Model model)
	{
	
	String name=prin.getName();
	
	String b=merchantService.piiauthentication(name);
	
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
	
	
	return "merchant/merchantaction";

	}


@RequestMapping(value = "/merchantrevoke", method = RequestMethod.GET)
public ModelAndView merchantrevokeTransaction(Principal prin)
	{
	
	String name=prin.getName();
	
	ModelAndView model=new ModelAndView();
	
	List <BossExtTransaction> result=merchantService.viewtransactiondetailss(name);
	
	if (null==result){
		
		model.addObject("piiautho","Oops!Something went wrong try again");
		
		model.setViewName("merchantuser/merchantaction");
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
 	
 	model.setViewName("merchant/merchantrevoke");
 	
 	return model;

	}



@RequestMapping(value = "/merchantrevoke/merchantsrevoke", method = RequestMethod.POST)
public ModelAndView revoketransaction(Principal prin,@RequestParam("tranid") long tranid)
		
	{
	
	String name=prin.getName();
	
	ModelAndView model=new ModelAndView();
	
	String b=merchantService.revoketrans(name,tranid);
	
	String r="fail";
	
	String r1="true";
	
	if(b.equals(r))
	{
		model.addObject("piiautho","Oops!Something went wrong try again");
	}
	else if(b.equals(r1))
		
	{
		model.addObject("revoke","Request Submitted");
		
	}
		 	
	else
		
		model.addObject("revoke","Failed submission");
		 	
	model.setViewName("merchant/merchantaction");
	
	
 	return model;

	}


@RequestMapping(value = "/merchantdelete", method = RequestMethod.GET)
public String merchantdeleteUser(Principal prin,Model model)
	{
	String name=prin.getName();
	
	String b=merchantService.deleteuser(name);
	
	String r1="fail";
	
	if(b.equals(r1))
		
	{
		model.addAttribute("piiiauth","Oops!Something went wrong try again");
	}
	else if(b.equals("true"))
		
	{
		model.addAttribute("delete","Request Submitted");
	}
	
	else
		
		model.addAttribute("delete","Request Submission failed due to pending account requests,contact bank directly");

	return "merchant/merchantaction";

	}


@RequestMapping(value = "/merchantpayrequests", method = RequestMethod.GET)
public String merchantViewPayRequests(Principal prin,Model model)
	{
	String name=prin.getName();
	
	List <BossExtTransactionTemp> result=merchantService.payRequests(name);
	
	if (null==result){
		
		model.addAttribute("piiautho","Oops!Something went wrong try again");
		
		return "merchantuser/merchantaction";
		}
	
	int count=result.size();
	
	if(count==0)
	{
		model.addAttribute("paymentreqs","NoRequestsFound");
	}
	else	
	{
		model.addAttribute("paymentreq",result);
	}
	
		
	return "merchant/merchantViewPay";

	}


@RequestMapping(value = "/merchantpayrequests/submit", method = RequestMethod.POST)
public String merchantViewPayRequests(Model model,@RequestParam("trainid") long TranID)
	{
	
	String result=merchantService.transferRequests(TranID);
	
	String r="fail";
	
	String r1="true";
	
	String r2="false";
	
	if(result.equals(r))
	
		model.addAttribute("piiautho","Oops!Something went wrong try again");
	
	else if(result.equals(r1))
		model.addAttribute("paytransfer","Awaiting Approval from Bank");
	
	else if(result.equals(r2))
	model.addAttribute("paytransfer","failed submitting request");
		
	return "merchant/merchantaction";

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

private boolean valphnos(String email) {
 
 String type="\\b^[0-9]{10}\\b";
		 	 
 Pattern pat = Pattern.compile(type);
 Matcher mat = pat.matcher(email);
 
 if(mat.find()) 
 {
  return true;
  }
 return false;
 }

private boolean valfnames(String fname) {
 
 String type="\\b^[a-zA-Z0-9_]+$\\b";
		 	 
 Pattern pat = Pattern.compile(type);
 
 Matcher mat = pat.matcher(fname);
 
 if(mat.find()) 
 {
  return true;
  }
 return false;
 }
	
private boolean valadds(String fname) {
 
 String type="\\b^[a-zA-Z0-9_./#&+-:]+$\\b";
		 	 
 Pattern pat = Pattern.compile(type);
 
 Matcher mat = pat.matcher(fname);
 
 if(mat.find()) 
 {
  return true;
  }
 return false;
 }

private boolean valnums(String number) {
 
 String type="\\b^[0-9.]{1,5}$\\b";
		 	 
 Pattern pat = Pattern.compile(type);
 
 Matcher mat = pat.matcher(number);
 
 if(mat.find()) 
 {
  return true;
  }
 return false;
 }

private boolean valaccs(String number) {
	 
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