package boss.boss_rs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import boss.data.entities.BossAccount;
import boss.data.entities.BossExtTransaction;
import boss.data.entities.BossUser;
import boss.data.entities.BossUserTemp;
import boss.data.repositories.BossInternalUserRepository;
import boss.data.repositories.BossUserRepository;

@Service
public class BossInternalUserService 
{
	@Autowired
	private BossInternalUserRepository userRepo;
	
	// Internal User View External Users
	@Transactional
	public synchronized List<String> viewExternalUsers()
	{
		List<String> users = userRepo.viewExternalUsers();
		return users;
	}
	
	@Transactional
	//Request to review transactions of a particular user
	public synchronized String requestViewExtTransactions(String username, String loginuser)
	{
		String result = userRepo.requestViewExtTransactions(username,loginuser);
		return result;
	}
	@Transactional
	// Check if the Internal User has permission to view external user transactions
	public synchronized String viewExtTransactionsPermission(String username , String loginuser)
	{
	String result = userRepo.viewExtTransactionsPermission(username,loginuser);
	return result;
	}
	
	@Transactional
	//Retrieve External User Transactions
	public synchronized List<BossExtTransaction> retrieveExtTransactions(String username, String loginuser)
	{
		List<BossExtTransaction> transactions = userRepo.retrieveExtTransactions(username, loginuser);
		return transactions;
	}
	
	@Transactional
	//Revert permissions to view external user transactions
	public synchronized boolean revertExtTransactionPermissions(String username, String loginuser)
	{
		boolean result = userRepo.revertExtTransactionPermissions(username,loginuser);
		return result;
	}
	
	@Transactional
	// View External users profile request
	public synchronized List<BossUserTemp> viewExtUsersProfileReq()
	{
		List<BossUserTemp> users = userRepo.viewExtUserProfileReq();
		return users;
	}
	@Transactional
	// Authorize External User profile request
	public synchronized boolean approveExtPofileReq(String username, String fname, String lname, String address, long phoneno, String email)
	{
		System.out.println("Inside service" + fname);
		/*System.out.println("Inside service" + username);
		System.out.println("Inside service" + lname);
		System.out.println("Inside service" + address);
		System.out.println("Inside service" + phoneno);
		System.out.println("Inside service" + email);*/
		boolean status = userRepo.approveExtPofileReq(username, fname, lname, address, phoneno, email);
		return status;
	}
}
