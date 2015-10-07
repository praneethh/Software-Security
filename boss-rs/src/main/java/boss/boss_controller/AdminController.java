package boss.boss_controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import boss.boss_rs.BossAdminService;
import boss.boss_rs.BossInternalUserService;
import boss.data.entities.BossExtTransaction;
import boss.data.entities.BossExtTransactionTemp;
import boss.data.entities.BossUser;
import boss.data.entities.BossUserTemp;


@Controller
public class AdminController 
{
	@Autowired
	private BossAdminService adminService;
	
//	// Admin Homepage
//	@RequestMapping(value = "/admin", method = RequestMethod.GET)
//	public ModelAndView apage() {
//
//		ModelAndView model = new ModelAndView();
//		
//		model.setViewName("admin");
//
//		return model;
//
//	}
	
	// Internal User Part
	
	
	
	@Autowired
	private BossInternalUserService userService;
		
		// Internal User View External Users
		@RequestMapping(value = "/adminViewUsers") 
		public String InternalUserViewUsers(Model model) 
		{
			String result;
			List<String> extusers = userService.viewExternalUsers();
			if(extusers.size()<=0)
			result = "F";
			
			else
			{
			result = "S";
			model.addAttribute("InternalUserViewUsers",extusers);
			}
			model.addAttribute("Status",result);
			return "adminViewUsers";

		}
		
		//Display options for Request Transactions and View Transactions
		@RequestMapping(value = "/adminTransReq",method = RequestMethod.POST) // View Trnasactions of external users
		public ModelAndView ViewExteranlUserTrans(@RequestParam("username") String username) 
		{
			ModelAndView model = new ModelAndView();
			model.addObject("username",username);
			model.setViewName("adminTransReq");
			return model;
		
		}
		
		/* Internal user : Request external user transactions*/
		 @RequestMapping(value = "/adminTransRequest", method = RequestMethod.POST)
		public ModelAndView RequestViewTransactions(@RequestParam("username") String username, @RequestParam("loginuser") String loginuser)
		{

			ModelAndView model = new ModelAndView();
			String status="";					
			String result = userService.requestViewExtTransactions(username, loginuser);
			if(result.equals("S"))
			status = "View Transaction request created successfully"	;
			else if(result.equals("N"))
			status = "No existing transactions for the user " + username;
			else if(result.equals("O"))
			status = "Request already raised for the user  " + username;
			else
			status = "Could not create request. Please try after sometime";
			
			model.addObject("status",status);
			model.addObject("username",username);
			return model;
		}
		 
		 /* Internal user : view external user transactions*/
		 @RequestMapping(value = "/adminExtTransactions", method = RequestMethod.POST)
		public ModelAndView ViewTransactions(@RequestParam("username") String username,@RequestParam("loginuser") String loginuser)
		{

			ModelAndView model = new ModelAndView();		
			String result="";
									
		String permission = userService.viewExtTransactionsPermission(username, loginuser);
		if(permission.equals("E"))
		{
			 result = "E";	 
		}
		else if (permission.equals("N"))
		{
			result = "N";
		}
		else if (permission.equals("F"))
		{
			result = "F";
		}
		else
		{
			result = "S";
			List<BossExtTransaction> transList = userService.retrieveExtTransactions(username, loginuser);
			if(transList.size() < 0)
			result = "E";
			else
			{
			model.addObject("RetrieveExtTransactions",transList);
			boolean status = userService.revertExtTransactionPermissions(username,loginuser);
			if (!status)
			result = "E";		
			}
		}
			
			model.addObject("status",result);
			model.addObject("username",username);
			return model;

		}
		 
		 //Internal User View External User Change Profile Request
		 @RequestMapping(value = "/adminViewExtProfileReq") 
			public ModelAndView InternalUserViewExtProfileReq() 
			{
			 	ModelAndView model = new ModelAndView();
			 	List<BossUserTemp> usersreq = userService.viewExtUsersProfileReq();
			 	int count = usersreq.size();
			 	//System.out.println(usersreq.get(0).getEmail());
			 	//System.out.println(usersreq.get(0).getPhoneno());
			 	String status = null;
			 	if(count == 0)
			 	status = "F";
			 	else
			 	{
			 	status = "S";
				model.addObject("InternalUserViewExtProfileReq",usersreq);
			 	}
			 	model.addObject("status",status);
			 	return model;

			}
		 
		 //Internal User approve external user profile change request
		 @RequestMapping(value = "/adminExtProfileApprove", method = RequestMethod.POST)
		public ModelAndView ApproveProfileReq(@RequestParam("username") String username, @RequestParam("fname") String fname, @RequestParam("lname") String lname, @RequestParam("address") String address, @RequestParam("phoneno") long phoneno, @RequestParam("email") String email)
		{

			ModelAndView model = new ModelAndView();
					
			String result="";
									
		boolean status = userService.approveExtPofileReq(username,fname,lname,address,phoneno,email);
		if(!status)
		{
			 result = "Failure in updating the user profile. Please try again after sometime";	 
		}
		else
			result = "Profile successfully updated";
			model.addObject("status",result);
			return model;

		}
		 
	//Admin Part
	//View request for external user delete account
	
	// Delete External User Request
			@RequestMapping(value = "/adminDeleteUserReq") 
			public ModelAndView viewDeleteRequests() 
			{
				ModelAndView model = new ModelAndView();
				List <BossUserTemp> users = adminService.viewDeleteRequests();
				
				int count = users.size();
			 	String status = "";
			 	if(count == 0)
			 	status = "F";
			 	else
			 	{
			 	status = "S";
				model.addObject("DeleteExtUserReq",users);
			 	}
			 	model.addObject("status",status);
			 	return model;
			}
			
			//Admin approves external user delete request
			 @RequestMapping(value = "/adminDeleteExtUserApprove", method = RequestMethod.POST)
			public ModelAndView ApproveDeleteExtUserReq(@RequestParam("username") String username)
			{

				ModelAndView model = new ModelAndView();
						
				String result="";
										
			String status = adminService.approveDeleteUserReq(username);
			if(status.equals("N"))
			{
				 result = "Failure in deleting the profile. Please try again after sometime";	 
			}
			else if(status.equals("F"))
			{
				 result = "Pending requests for the user. It cannot be deleted";	 
			}
			else
				result = "External User successfully deleted";
				model.addObject("status",result);
				//model.addObject("username",username);
				//model.setViewName("InternalUserExtTransactions");
				return model;

			}
	
			 
// Admin access PII
			 
			 /* Admin : Request external user PII*/
			 @RequestMapping(value = "/adminExtPIIRequest", method = RequestMethod.POST)
			public ModelAndView RequestPII(@RequestParam("username") String username)
			{

				ModelAndView model = new ModelAndView();
				String status=null;					
				boolean result = adminService.requestViewExtPII(username);
				if(result)
				status = "Request created successfully"	;
				else
				status = "Could not create request. Please try after sometime";
				
				model.addObject("status",status);
				model.addObject("username",username);
				return model;
			}
			 
			 /* Admin : view external user PII*/
			 @RequestMapping(value = "/adminExtViewPII", method = RequestMethod.POST)
			public ModelAndView ViewPII(@RequestParam("username") String username)
			{

				ModelAndView model = new ModelAndView();		
										
			String permission = adminService.viewExtPIIPermission(username);
			if(permission.equals("S"))
			{
				BossUser user = adminService.retrieveExtPII(username);
				String ssn = user.getSsn();
				if(ssn == null)
				permission = "NU";
				model.addObject("RetrievePII",ssn);
				boolean result = adminService.revertExtPIIPermissions(username);
				if(!result)
				{
					permission = "N";
				}
			}
				
				model.addObject("status",permission);
				model.addObject("username",username);
				return model;

			}
		
	//Delete Internal Users Functions:
	// View Internal Users
			 @RequestMapping(value = "/adminViewInternalUser") 
				public String AdminViewInternalUsers(Model model) 
				{
				 	String result = "";
					List<String> empusers = adminService.viewInternalUsers();
					if(empusers.size()<=0)
					result = "F";
					
					else
					{
					result = "S";
					model.addAttribute("adminViewInternalUser",empusers);
					}
					model.addAttribute("Status",result);
					return "adminViewInternalUser";
					//model.addAttribute("adminViewInternalUser",adminService.viewInternalUsers());
					//return "adminViewInternalUser";

				}
	
	//Delete Internal User adminDeleteInternalUser
			 @RequestMapping(value = "/adminDeleteInternalUser", method = RequestMethod.POST)
				public ModelAndView adminDeleteInternalUser(@RequestParam("username") String username)
				{

					ModelAndView model = new ModelAndView();
					String status="";					
					boolean result = adminService.adminDeleteInternalUser(username);
					if(result)
					status = "User Deleted Successfully"	;
					else
					status = "Could not delete user. Please try after sometime";
					
					model.addObject("status",status);
					model.addObject("username",username);
					//model.setViewName("InternalUserTransRequest");
					return model;
				}
				 
	// Revoke functions adminViewRevokeTransReq
	// View Revoke requests
			 @RequestMapping(value = "/adminViewRevokeTransReq") 
				public String adminViewRevokeTransReq(Model model) 
				{
					
				 	List<BossExtTransaction> transactions = adminService.viewRevokeTransReq();
				 	String result;
				 	
				 	if(!(transactions.size()>0))
					{
						 result = "F";	 
					}
					else
					{
						result = "S";
						model.addAttribute("RetrieveRevokeTransactions",transactions);
					}
						
						model.addAttribute("status",result);
						return "adminViewRevokeTransReq";

				}
	//Approve Revoke Request
			 @RequestMapping(value = "/adminRevokeTransApprove", method = RequestMethod.POST)
				public ModelAndView adminRevokeTransAuth(@RequestParam("transid") String transid,@RequestParam("username") String username)
				{

					ModelAndView model = new ModelAndView();
					String status; 
					String result;
					
					result = adminService.approveRevokeReq(transid,username);
					
					if(result.equals("NT"))
					status = "No such transaction exists now";
					
					else if (result.equals("NB"))
					status = "No sufficient balance in the reciever's account to revoke the transaction";
					
					else if (result.equals("N"))
					status = "Transaction cannot be revoked. Please try after sometime";
					
					else
					status = "Transaction successfully revoked";
					
					model.addObject("status",status);
					
					return model;
				}
			 
	// Make Payment Authorization functions 
		// View MakePayment authorization requests
				 @RequestMapping(value = "/adminViewAuthMakePayment") 
					public String adminViewMakePaymentAuthReq(Model model) 
					{
						
					 	List<BossExtTransactionTemp> transactions = adminService.viewMakePaymentAuthReq();
					 	String result;
					 	
					 	if(!(transactions.size()>0))
						{
							 result = "F";	 
						}
						else
						{
							result = "S";
							model.addAttribute("RetrieveMakePaymentAuthReq",transactions);
						}
							
							model.addAttribute("status",result);
							return "adminViewAuthMakePayment";

					}	
				 
			// Authorize MakePayment authorization requests 
				 @RequestMapping(value = "/adminAuthMakePaymentApprove", method = RequestMethod.POST)
					public ModelAndView adminAuthMakePaymentApprove(@RequestParam("transid") String transid,@RequestParam("username") String username)
					{

						ModelAndView model = new ModelAndView();
						String status; 
						String result;
						
						result = adminService.adminAuthMakePaymentApprove(transid,username);
						
						if(result.equals("NT"))
						status = "No such transaction exists now";
						
						else if (result.equals("NB"))
						status = "No sufficient balance in the reciever's account to process the transaction";
						
						else if (result.equals("NM"))
						status = "No such merchant exists";
						
						else if (result.equals("NU"))
						status = "No such user exists";
						
						else if (result.equals("N"))
						status = "Transaction cannot be authorized. Please try after sometime";
						
						else
						status = "Transaction successfully authorized";
						
						model.addObject("status",status);
						
						return model;
					}
				 
}
