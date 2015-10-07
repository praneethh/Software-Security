package boss.boss_rs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import boss.data.entities.BossAccount;
import boss.data.entities.BossExtTransaction;
import boss.data.entities.BossExtTransactionTemp;
import boss.data.entities.BossUser;
import boss.data.entities.BossUserTemp;
import boss.data.repositories.AdminRepository;
import boss.data.repositories.BossInternalUserRepository;
import boss.data.repositories.BossUserRepository;

@Service
public class BossAdminService 
{
	@Autowired
	private AdminRepository userRepo;
	
	@Transactional
	// View External users profile request
	public synchronized List<BossUserTemp> viewDeleteRequests()
	{
		List<BossUserTemp> users = userRepo.viewDeleteRequests();
		return users;
	}
	
	// Approve delete external user request
	@Transactional
	public synchronized String approveDeleteUserReq(String username)
	{
		String result = userRepo.approveDeleteUserReq(username);
		return result;
	}
	
	// Admin PII functions
	
	@Transactional
	//Request to review PII of a particular user
	public synchronized boolean requestViewExtPII(String username)
	{
		boolean result = userRepo.requestViewExtPII(username);
		return result;
	}
	@Transactional
	// Check if the Internal User has permission to view external user transactions
	public synchronized String viewExtPIIPermission(String username)
	{
	String result = userRepo.viewExtPIIPermission(username);
	return result;
	}
	
	@Transactional
	//Retrieve External User Transactions
	public synchronized BossUser retrieveExtPII(String username)
	{
		BossUser user = userRepo.retrieveExtPII(username);
		//System.out.println("In Service"+user.getSsn());
		return user;
	}
	
	@Transactional
	//Revert permissions to view external user transactions
	public synchronized boolean revertExtPIIPermissions(String username)
	{
		boolean result = userRepo.revertExtPIIPermissions(username);
		return result;
	}
	
	@Transactional
	// View External users profile request
	public synchronized List<String> viewInternalUsers()
	{
		List <String> users = userRepo.viewInternalUsers();
		return users;
	}
	
	@Transactional
	// Delete Internal User
	public synchronized boolean adminDeleteInternalUser(String username)
	{
		boolean result;
		result = userRepo.adminDeleteInternalUser(username);
		return result;
	}
	
	@Transactional
	// View Revoke Transactions Request
	public synchronized List<BossExtTransaction> viewRevokeTransReq()
	{
		List<BossExtTransaction> transactions = userRepo.viewRevokeTransReq();
		return transactions;
	}
	
	@Transactional
	// Approve Transaction
	public synchronized String approveRevokeReq(String transid,String username)
	{
		String result = userRepo.approveRevokeReq(transid, username);
		return result;
	}
	@Transactional
	//View MakePayment Authorization Requests
	public synchronized List<BossExtTransactionTemp> viewMakePaymentAuthReq()
	{
		List<BossExtTransactionTemp> transactions = userRepo.viewMakePaymentAuthReq();
		return transactions;
	}
	
	@Transactional
	// Approve Transaction
	public synchronized String adminAuthMakePaymentApprove (String transid,String username)
	{
		String result = userRepo.adminAuthMakePaymentApprove(transid, username);
		return result;
	}
	
	
}

