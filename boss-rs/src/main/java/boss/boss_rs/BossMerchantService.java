package boss.boss_rs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import boss.data.entities.BossAccount;
import boss.data.entities.BossExtTransaction;
import boss.data.entities.BossExtTransactionTemp;
import boss.data.entities.BossUser;
import boss.data.repositories.BossMerchantRepository;
import boss.data.repositories.BossUserRepository;

@Service
public class BossMerchantService {
	
	@Autowired
	private BossUserRepository userRepo;
	
	@Autowired
	private BossMerchantRepository merchantRepo;
	
	@Transactional
	public synchronized List<BossAccount> viewaccountdetails(String username)
	{
		return userRepo.viewaccountdetails(username);
	}

	@Transactional
	public synchronized List<BossExtTransaction> viewtransactiondetails(String username)
	{
		return userRepo.viewtransactiondetails(username);
	}
	
	@Transactional
	public synchronized List<BossUser> viewprofiledetails(String username)
	{
		return userRepo.viewprofiledetails(username);
	}
	
	@Transactional
	public synchronized  String updateprofiledetails(String username,String Fname,String Lname,String Address,long Phoneno,String Email)
	{
		return userRepo.Updateprofiledetails(username,Fname,Lname,Address,Phoneno,Email);
	}
	
	@Transactional
	public synchronized String credit(double amount,String username)
	{
		return userRepo.credit(amount,username);
	}
	
	@Transactional
	public synchronized String debit(double amount,String username)
	{
		return userRepo.debit(amount,username);
	}
	
	@Transactional
	public synchronized String fundtransfer(String account,double amount,String username)
	{
		return userRepo.fundtransfer(account,amount,username);
	}
	
	@Transactional
	public synchronized boolean saveTranscredit(double amount,String username,String status)
	{
		return userRepo.saveTranscredit(amount,username,status);
	}
	
	@Transactional
	public synchronized boolean saveTransdebit(double amount,String username,String status)
	{
		return userRepo.saveTransdebit(amount,username,status);
	}
	
	@Transactional
	public synchronized String transactionFunds(String account,double amount,String name,String status)
	{
		return userRepo.transactionFunds(account,amount,name,status);
	}
	
	@Transactional
	public synchronized List<BossExtTransaction> viewtransactiondetail(String username)
	{
		return userRepo.viewtransactiondetail(username);
	}
	
	@Transactional
	public synchronized  String Authorize(String username)
	{
		return userRepo.Authorizetransactions(username);
	}
	
	@Transactional
	public synchronized  String viewpiiauthentication(String name)
	{
		return userRepo.Viewpiiauthentication(name);
	}
	
	@Transactional
	public synchronized  String piiauthentication(String name)
	{
		return userRepo.PiiAuthentication(name);
	}
	
	@Transactional
	public synchronized List<BossExtTransaction> viewtransactiondetailss(String username)
	{
		return userRepo.viewtransactiondetailss(username);
	}
	
	@Transactional
	public synchronized String revoketrans(String name,long tranid)
	{
		return userRepo.Revoketrans(name,tranid);
	}

	@Transactional
	public synchronized  String deleteuser(String name)
	{
		return userRepo.Deleteuser(name);
	}
	
	@Transactional
	public synchronized  List<BossExtTransactionTemp> payRequests(String name)
	{
		return merchantRepo.viewRequests(name);
	}
	
	@Transactional
	public synchronized  String transferRequests(long tranid)
	{
		return merchantRepo.transferRequests(tranid);
	}
	
	
	@Transactional
	public synchronized  String getaccount(String account,String name)
	{
		return userRepo.getaccount(account,name);
	}
	
	
	
}
