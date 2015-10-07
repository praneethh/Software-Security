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

import boss.boss_rs.BossInternalUserService;
import boss.boss_rs.BossUserService;
import boss.data.entities.BossExtTransaction;
import boss.data.entities.BossUserTemp;

@Controller
public class InternalUserController 
{
	@Autowired
	private BossInternalUserService userService;
	
		// Internal User Homepage
		@RequestMapping(value = "/InternalUser", method = RequestMethod.GET) 
		public ModelAndView InternalUserhomepage() 
		{
			ModelAndView model = new ModelAndView();
			model.setViewName("InternalUser");
			return model;

		}
		
		// Internal User View External Users
		@RequestMapping(value = "/InternalUserViewUsers") 
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
			return "InternalUserViewUsers";
		}
		
		//Display options for Request View Transactions and View Transactions
		@RequestMapping(value = "/InternalUserTransReq",method = RequestMethod.POST)
		public ModelAndView ViewExteranlUserTrans(@RequestParam("username") String username) 
		{
			ModelAndView model = new ModelAndView();
			model.addObject("username",username);
			model.setViewName("InternalUserTransReq");
			return model;
		
		}
		
		/* Internal user : Request external user view transactions*/
		 @RequestMapping(value = "/InternalUserTransRequest", method = RequestMethod.POST)
		public ModelAndView RequestViewTransactions(@RequestParam("username") String username, @RequestParam("loginuser") String loginuser)
		{
			
			ModelAndView model = new ModelAndView();
			String status="";					
			String result = userService.requestViewExtTransactions(username,loginuser);
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
			model.setViewName("InternalUserTransRequest");
			return model;
		}
		 
		 /* Internal user : view external user transactions*/
		 @RequestMapping(value = "/InternalUserExtTransactions", method = RequestMethod.POST)
		public ModelAndView ViewTransactions(@RequestParam("username") String username,@RequestParam("loginuser") String loginuser )
		{

			ModelAndView model = new ModelAndView();		
			String result="";
									
		String permission = userService.viewExtTransactionsPermission(username,loginuser);
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
			List<BossExtTransaction> transList = userService.retrieveExtTransactions(username,loginuser);
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
			model.setViewName("InternalUserExtTransactions");
			return model;

		}
		 
		 //Internal User View External User Change Profile Request
		 @RequestMapping(value = "/InternalUserViewExtProfileReq") 
			public ModelAndView InternalUserViewExtProfileReq() 
			{
			 	ModelAndView model = new ModelAndView();
			 	List<BossUserTemp> usersreq = userService.viewExtUsersProfileReq();
			 	int count = usersreq.size();
			 	String status = "";
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
		 @RequestMapping(value = "/InternalUserExtProfileApprove", method = RequestMethod.POST)
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
		 
}
