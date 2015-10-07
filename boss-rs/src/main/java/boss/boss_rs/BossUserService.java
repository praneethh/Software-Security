package boss.boss_rs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import boss.data.entities.BossAccount;
import boss.data.entities.BossExtTransaction;
import boss.data.entities.BossUser;
import boss.data.repositories.BossUserRepository;

@Service
public class BossUserService {
	
	@Autowired
	private BossUserRepository userRepo;

	  
	@Transactional
	public synchronized List<BossAccount> viewaccountdetails(String username)
	{
		return userRepo.viewaccountdetails(username);
	}
	
	@Transactional
	public synchronized List<BossUser> viewprofiledetails(String username)
	{
		return userRepo.viewprofiledetails(username);
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
	public synchronized List<BossExtTransaction> viewtransactiondetails(String username)
	{
		return userRepo.viewtransactiondetails(username);
	}
	
	@Transactional
	public synchronized List<BossExtTransaction> viewtransactiondetailss(String username)
	{
		return userRepo.viewtransactiondetailss(username);
	}
	
	@Transactional
	public synchronized List<BossExtTransaction> viewtransactiondetail(String username)
	{
		return userRepo.viewtransactiondetail(username);
	}
	
	@Transactional
	public synchronized  String updateprofiledetails(String username,String Fname,String Lname,String Address,long Phoneno,String Email)
	{
		return userRepo.Updateprofiledetails(username,Fname,Lname,Address,Phoneno,Email);
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
	public synchronized  String getaccount(String account,String name)
	{
		return userRepo.getaccount(account,name);
	}
	
	@Transactional
	public synchronized  List<String[]> extmerchantview()
	{
		return userRepo.Extmerchantview();
	}
	
	@Transactional
	public synchronized  boolean extMerchantPay(String name,String account,double amount,String Merchant)
	{
		return userRepo.extUserPay(name,account,amount,Merchant);
	}
	@Transactional
	public String resetPassword(String userName, String password) {
		return userRepo.resetPassword(userName,password);
	}
@Transactional
	public String createUser(BossUser userEntity) {
		return userRepo.createCustomer(userEntity);
	}
@Transactional
	public String forgotPassword(String userName, int loginAttempts) {
		return userRepo.forgotPassword(userName,loginAttempts);
		
	}
	
	@Transactional
	public boolean userExists(String userName) {
		return userRepo.userExists(userName);
		
	}
	@Transactional
	public boolean userEmailExists(String email) {
		return userRepo.userEmailExists(email);
		
	}
	@Transactional
	public boolean userSSNExists(String ssn) {
		return userRepo.userSSNExists(ssn);
		
	}

	@Transactional
	public String createInternalUser(BossUser userEntity) {
		return userRepo.createInternalUser(userEntity);
	}
	@Transactional
	public String createMerchant(BossUser userEntity) {
		return userRepo.createMerchant(userEntity);
	}
	}
