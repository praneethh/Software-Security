package boss.data.repositories;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
//import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.xml.stream.XMLStreamException;

import net.openhft.lang.Maths;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.internal.SQLQueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import boss.data.entities.BossAccount;
import boss.data.entities.BossExtTransaction;
import boss.data.entities.BossExtTransactionTemp;
import boss.data.entities.BossLog;
import boss.data.entities.BossRole;
import boss.data.entities.BossUser;
import boss.data.entities.BossUserLogin;
import boss.data.entities.BossUserTemp;
import boss.utilities.GenerateCertificate;
import boss.utilities.SendEmail;

import com.fasterxml.jackson.databind.ObjectMapper;

//@Component
@Repository
public class  BossUserRepository{
	private static final Logger log = LoggerFactory
			.getLogger(BossUserRepository.class);

	@Value("test")
	protected String tableSchema;

	//@Autowired
    //private BossUser userDetails;
	//@Autowired
	//private EntityObjectMapper mapper;
	@Autowired
	private SessionFactory sessionFactory;
	
	public BossUserRepository() {
	
	}

	public Map<String, Object> addUser(EntityManager em, BossUser userDetails,boolean isEntityExists) {
		Map<String, Object> returnMap = new HashMap<String, Object>();

		try {
			EntityTransaction tx = em.getTransaction();
			try {
				tx.begin();
				if (isEntityExists) {
					
				} else {
					em.persist(userDetails);
				}
				tx.commit();
				returnMap.remove("result");
				returnMap.put("result", true);
				returnMap.put("userDetails",userDetails );
			} catch (Exception e) {
				return returnMap;
			} finally {
				if (tx.isActive())
					tx.rollback();
			}
		} finally {
		}
		return returnMap;
	}
	
	
	
	
	@Transactional
	public  String forgotPassword(String userName, int loginAttempts){

		Session session = null;
		try {
			session = sessionFactory.getCurrentSession();
		} catch (HibernateException  e1) {
			e1.printStackTrace();
		};

        
	String eMailId= getUserEmail(userName);
	//int loginAttempts=getLoginAttempts(userName);
	if (eMailId.equals("error")||eMailId=="error"){
		return "not sent";
	}
            String result="not sent";    
				try {
					SendEmail emailClient = new SendEmail();		
					 long otp = UUID.randomUUID().getMostSignificantBits();
				    result=emailClient.sendForgotPasswordMail(eMailId,otp);
				    if(result.equals("sent")||result=="sent"){
				    BossUserLogin userLogin= new BossUserLogin();
				    userLogin.setOtp(otp);
				    long userId = findIdByName(userName);
				    userLogin.setUserName(userName);
				    String password =UUID.randomUUID().toString();
				    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			        String encodedPassword = passwordEncoder.encode(password);
				    userLogin.setPassword(encodedPassword);
				    userLogin.setOtpFlag("Y");
				    userLogin.setPasswordSalt("ABC");
				    userLogin.setUserId(userId);
				    //userLogin.setLoginFlag("N");
				    Calendar c = Calendar.getInstance(); 
					c.setTime(new Date()); 
					c.add(Calendar.HOUR, 1);
					Date dt = c.getTime();
                    userLogin.setOtpExpiration(dt);
                   // int loginAttempts=getLoginAttempts(userName);
                    userLogin.setLoginAttempts(loginAttempts);
                    session.update(userLogin);
				    //session.save(userLogin);
				    }
				
					
				} catch (Exception e) {
					e.printStackTrace();
					return result;
				}
	
		
		return result;
	}
	
	@Transactional
	private synchronized String getUserEmail(String userName) {
		String email="";
		try{
		Session session = this.sessionFactory.getCurrentSession();
		//BossUser user = (BossUser) session.get(BossUser.class,
			//	userName); 
		String hql= "from BossUser where UNAME=:userName ";
		Query  q = (org.hibernate.Query) session.createQuery(hql);
		q.setParameter("userName",userName);
		BossUser user = (BossUser) q.uniqueResult();
		 //email=(String) q.getSingleResult();
		 email=user.getEmail();
		 return email;
		}
		catch(HibernateException he){
			he.printStackTrace();
			return "error";
		}
		catch(Exception e){
			e.printStackTrace();
			return "error";
		}
		
	}
	
@Transactional
	public  String createCustomer(BossUser userEntity){

		Session session = null;
		String result="not sent";
		try {
			session = this.sessionFactory.getCurrentSession();
		} catch (HibernateException  e1) {
			e1.printStackTrace();
		};

        long userId =  UUID.randomUUID().getMostSignificantBits();
		try{
				BossUser customerEntity = new BossUser();
				customerEntity.setFname(userEntity.getFname());
				customerEntity.setLname(userEntity.getLname());
				customerEntity.setId(userId);
				customerEntity.setAddress(userEntity.getAddress());
				customerEntity.setPhoneno(userEntity.getPhoneno());
				//customerEntity.setCustomerType(customerType);
				customerEntity.setEmail(userEntity.getEmail());
				customerEntity.setSsn(userEntity.getSsn());
				customerEntity.setUname(userEntity.getUname());
				String pswd = UUID.randomUUID().toString();
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		        String encodedPassword = passwordEncoder.encode(pswd); 
				customerEntity.setPassword(encodedPassword);
				customerEntity.setFlag("N");
				customerEntity.setPiiflag("N");
				customerEntity.setStatus("active");
				//customerEntity.setFirstname(firstName);
                
				//try {

					session.save(customerEntity);
					//long accountId=UUID.randomUUID().getMostSignificantBits();
					//String accountNo= userEntity.getUname()+accountId;
					long accountId=UUID.randomUUID().getMostSignificantBits();
					accountId=Math.abs(accountId);
					String accountNo= ""+accountId;
					BossAccount accountEntity = new BossAccount();
					accountEntity.setAccountId(accountNo);
					accountEntity.setUname(userEntity.getUname());
					accountEntity.setAccountbal(1000.00);;
					accountEntity.setCreateddate(new Date());
                    BossRole roleEntity =new BossRole();
                    roleEntity.setRoletype("ROLE_USER");
                    roleEntity.setUname(userEntity.getUname());
                    roleEntity.setUserId(userId);
                    
					if(saveRole(roleEntity)&&saveAccount(accountEntity)){					
					SendEmail emailClient = new SendEmail();
					byte[] certificate=GenerateCertificate.PublicPrivateKeyGeneration(userEntity.getUname());			
				    FileOutputStream fos = new FileOutputStream(new File("./"+userEntity.getUname()+"certificate"));
				    fos.write(certificate);
				    long otp = UUID.randomUUID().getMostSignificantBits();

                    //String result=emailClient.sendSignUpMail(userEntity.getEmail(),otp,"C:\\Users\\rbh\\Documents\\workspace-sts-3.5.1.RELEASE\\boss-rs\\certificates\\"+userEntity.getUname()+"_cert");

				    BossUserLogin userLogin= new BossUserLogin();
				    userLogin.setUserId(userId);
				    userLogin.setOtp(otp);
				    userLogin.setUserName(userEntity.getUname());
				    userLogin.setPassword(encodedPassword);
				    userLogin.setLoginAttempts(0);
					userLogin.setOtpFlag("Y");
					//userLogin.setLoginFlag("N");
					Calendar c = Calendar.getInstance(); 
					c.setTime(new Date()); 
					c.add(Calendar.HOUR, 1);
					Date dt = c.getTime();
                    userLogin.setOtpExpiration(dt);
                    
                    
                    Calendar c2 = Calendar.getInstance(); 
					c2.setTime(new Date()); 
					c2.add(Calendar.MONTH, 1);
					Date dt2 = c2.getTime();
					userLogin.setPasswordExpiration(dt);
                    session.save(userLogin);
                    
                     //result=emailClient.sendSignUpMail(userEntity.getEmail(),otp,"C:\\Users\\rbh\\Documents\\workspace-sts-3.5.1.RELEASE\\boss-rs\\certificates\\"+userEntity.getUname()+"_cert","C:\\Users\\rbh\\Documents\\workspace-sts-3.5.1.RELEASE\\boss-rs\\keys\\"+userEntity.getUname()+"_DSAPrivateKey.key");
                    result=emailClient.sendSignUpMail(userEntity.getEmail(),otp,"./"+userEntity.getUname()+"certificate","./"+userEntity.getUname()+"DSAPrivateKey.key");

					}
				}
	
				catch(SQLGrammarException sge){
				//	regChk.setErrorMessage("SQLGrammerException");
					SQLException se = new SQLException();
					throw new SQLGrammarException("SQL Grammer Exception", se);
				}

				catch (Exception e) {
					e.printStackTrace();
					return result;
				}
				finally {
				}
			
		
		return result;
	}
	
	
	@SuppressWarnings({ "rawtypes" })
	@Transactional
	public synchronized List<BossAccount> viewaccountdetails(String username)
	{
		List<BossAccount> account = new ArrayList<BossAccount>();
		
		Session session=null;
		try
		{
		
		session = this.sessionFactory.openSession();

		String hib = "FROM BossAccount WHERE UNAME = :username";	

		org.hibernate.Query que = session.createQuery(hib);

		que.setParameter("username", username);

		List li = que.list();
		
		for(int i=0;i<li.size();i++)
		{
			BossAccount accountObj = (BossAccount)li.get(i);
			account.add(accountObj);
		}

		}
		
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally{
			session.flush();
			session.close();
		}
		return account;
	}


	
@Transactional
	public synchronized double account(long accountId)
	{
		Session session = this.sessionFactory.getCurrentSession();
		BossAccount accnt = (BossAccount) session.get(BossAccount.class,
				accountId); 
		return accnt.getAccountbal();
	}


@SuppressWarnings("unchecked")
@Transactional
public synchronized List<BossUser> viewaccountdetails2(String username)
{
	
Session session = this.sessionFactory.openSession();

String hql = "FROM BossUser WHERE UNAME = :username";

Query query = (Query) session.createQuery(hql);
query.setParameter("username", username);

List<BossUser> results = ((SQLQueryImpl) query).list();

return results;
}


@SuppressWarnings({ "rawtypes" })
@Transactional
public synchronized List<BossUser> viewprofiledetails(String username)
{
	
	List<BossUser> account = new ArrayList<BossUser>();
	
	Session session=null;
	try
	{
	session = this.sessionFactory.openSession();

	String hib = "FROM BossUser WHERE UNAME = :username";

	org.hibernate.Query que = session.createQuery(hib);
	
	que.setParameter("username", username);
	
	List li = que.list();
	
	for(int i=0;i<li.size();i++)
	{
		BossUser accountObj = (BossUser)li.get(i);
		
		account.add(accountObj);
	}

}
catch(Exception ex)
{
	ex.printStackTrace();
}
finally{
	session.flush();
	session.close();
}
return account;
}


@SuppressWarnings("unchecked")
@Transactional
public synchronized boolean approve(int rd)
{
	boolean status=false;
	Session session = this.sessionFactory.getCurrentSession();
	
	String hql = "FROM Bossupdate WHERE REQUESTID = :req";

	Query query = (Query) session.createQuery(hql);
	query.setParameter("req", rd);
	//List<Bossupdate> userList = query.list();
	//Bossupdate accnts = userList.get(0);
	
	
	//if(accnts.getStatus()=='a')
	if (true)
	{
	hql = "UPDATE BossUser set  address= :address "  + 
            "WHERE userid = :AccountID";
	 query = (Query) session.createQuery(hql);
	//query.setParameter("address",accnts.getName());
	//query.setParameter("AccountID", accnts.getId());

	query.executeUpdate();
	
status=true;
	}
else
{
status=false;		
}
	return status;
}
@Transactional
public synchronized boolean updateaccount(long accountID,double balance)
{
	boolean status=false;
	Session session = this.sessionFactory.getCurrentSession();
	BossAccount accnt = (BossAccount) session.get(BossAccount.class,accountID);
	double oldbal=accnt.getAccountbal();
	double newbal=balance;
	double total=newbal+oldbal;
	String hql = "UPDATE BossAccount set balance = :Balance "  + 
            "WHERE account_id = :AccountID";
	org.hibernate.Query query = session.createQuery(hql);
	query.setParameter("Balance",total);
	query.setParameter("AccountID", accountID);

	int result = query.executeUpdate();
	if (result==1)
	{
		status=true;
	}
else
	status=false;
	
return status;
	}

@Transactional
public boolean validateOTP(String userName,long otp) {
	
	long userOTP;
	String otpFlag="";
	try{
		Session session = this.sessionFactory.getCurrentSession();
		//BossUser user = (BossUser) session.get(BossUser.class,
			//	userName); 
		String hql= "from BossUserLogin where USER_NAME=:userName ";
		org.hibernate.Query  q = (org.hibernate.Query) session.createQuery(hql);
		q.setParameter("userName",userName);
		BossUserLogin userLogin = (BossUserLogin) q.uniqueResult();
		 //email=(String) q.getSingleResult();
		userOTP=userLogin.getOtp();
		otpFlag=userLogin.getOtpFlag();
		Calendar c = Calendar.getInstance(); 
		c.setTime(new Date()); 
		Date dt = c.getTime();
		Date dbdt =userLogin.getOtpExpiration();
		c.setTime(dbdt);
		Date databaseDate=c.getTime();
		if (dt.after(databaseDate)){
			return false;
		}
		if(userOTP==otp&&otpFlag.equals("Y")){
			return	true;
		  
		}
		else{
			return false;
			//userLogin.
		}
		}
		catch(HibernateException he){
			he.printStackTrace();
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	 
}

@Transactional
public boolean userExists(String userName) {
	try{
		Session session = this.sessionFactory.getCurrentSession();
		//BossUser user = (BossUser) session.get(BossUser.class,
			//	userName); 
		String hib = "from BossUserLogin WHERE USER_NAME = :username";

		org.hibernate.Query query = session.createQuery(hib);
		query.setParameter("username", userName);
		BossUserLogin userLogin=(BossUserLogin) query.uniqueResult();
		if(userLogin==null){
		return false;
		}
		
			return true;
		
		}
		catch(HibernateException he){
			he.printStackTrace();
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
}

@Transactional
public boolean userEmailExists(String email) {
	try{
		Session session = this.sessionFactory.getCurrentSession();
		//BossUser user = (BossUser) session.get(BossUser.class,
			//	userName); 
		String hib = "from BossUser WHERE EMAIL = :email";

		org.hibernate.Query query = session.createQuery(hib);
		query.setParameter("email", email);
		BossUser user=(BossUser) query.uniqueResult();
		if(user==null){
		return false;
		}
		
			return true;
		
	}
	
	
		catch(HibernateException he){
			he.printStackTrace();
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
}

@Transactional
public boolean userSSNExists(String ssn) {
	try{
		Session session = this.sessionFactory.getCurrentSession();
		//BossUser user = (BossUser) session.get(BossUser.class,
			//	userName); 
		String hib = "from BossUser WHERE SSN = :ssn";

		org.hibernate.Query query = session.createQuery(hib);
		query.setParameter("ssn", ssn);
		BossUser user=(BossUser) query.uniqueResult();
		if(user==null){
		return false;
		}
		
			return true;
		
	}
	
	
		catch(HibernateException he){
			he.printStackTrace();
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
}


@Transactional
public boolean resetOTP(String userName) {
	long userOTP;
	try{
		Session session = this.sessionFactory.getCurrentSession();
		//BossUser user = (BossUser) session.get(BossUser.class,
			//	userName); 
		String hib = "UPDATE BossUserLogin set OTP=:otp, OTP_FLAG=:flag WHERE USER_NAME = :username";

		org.hibernate.Query que = session.createQuery(hib);
		userOTP=UUID.randomUUID().getMostSignificantBits();
		que.setParameter("otp", userOTP);
		que.setParameter("flag", "N");
		que.setParameter("username", userName);
		int status=que.executeUpdate();
		if(status==1)
		return true;
		else{
			return false;
		}
		}
		catch(HibernateException he){
			he.printStackTrace();
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
}
@Transactional
public String resetPassword(String userName, String password) {
	long userOTP;
	try{
		Session session = this.sessionFactory.getCurrentSession();
		//BossUser user = (BossUser) session.get(BossUser.class,
			//	userName); 
		String hib = "UPDATE BossUserLogin set OTP=:otp, OTP_FLAG=:flag,password=:pswd,login_attempts=:LA,OTP_EXPIRATION=:otpExp WHERE USER_NAME = :username";

		org.hibernate.Query que = session.createQuery(hib);
		userOTP=UUID.randomUUID().getMostSignificantBits();
		que.setParameter("otp", userOTP);
		que.setParameter("flag", "N");
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password); 
		que.setParameter("pswd", encodedPassword);
		que.setParameter("LA", 0);
		Calendar c = Calendar.getInstance(); 
		c.setTime(new Date()); 
		c.add(Calendar.HOUR, 1);
		Date dt = c.getTime();
		que.setParameter("otpExp", dt);
		que.setParameter("username", userName);
		int status=que.executeUpdate();
//		BossUserLogin userLogin=new BossUserLogin();		
//		userLogin.setUserName(userName);
//		userLogin.setPassword(password);
//		userOTP=UUID.randomUUID().getMostSignificantBits();
//		userLogin.setOtp(userOTP);
//		userLogin.setLoginAttempts(0);
//		userLogin.setOtpFlag("N");
//		session.update(userLogin);
		if (status==1){
		return "success";
		}
		else{
			return "failure";
		
		}
	}
		catch(HibernateException he){
			he.printStackTrace();
			return "failure";
		}
		catch(Exception e){
			e.printStackTrace();
			return "failure";
		}
}
	
@SuppressWarnings({ "rawtypes" })
@Transactional
public synchronized List<BossExtTransaction> viewtransactiondetail(String username)
{
	
	List<BossExtTransaction> account = new ArrayList<BossExtTransaction>();
	
	Session session=null;
	try
	{
	 session = this.sessionFactory.openSession();
			
	String hib = "FROM BossExtTransaction WHERE UNAME = :username and FLAG =:flag";

	org.hibernate.Query que = session.createQuery(hib);
	
	que.setParameter("username", username);
	
	que.setParameter("flag", "R");

	List li = que.list();
	
	for(int i=0;i<li.size();i++)
	{
		BossExtTransaction accountObj = (BossExtTransaction)li.get(i);
		
		account.add(accountObj);
	}
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	
	return account;
	
	
}


@SuppressWarnings({"unchecked" })
@Transactional
public synchronized List<BossExtTransaction> viewtransactiondetails(String username)
{
	List<BossExtTransaction> account = new ArrayList<BossExtTransaction>();
	
	Session session=null;
	
	session = this.sessionFactory.openSession();
	
	try
	{
	
	String hibs = "FROM BossAccount WHERE UNAME = :username";
	
	org.hibernate.Query ques = session.createQuery(hibs);
	
	ques.setParameter("username", username);
	
	List<BossAccount> ba = ques.list();
	
	BossAccount bass=ba.get(0);
	
	String b=bass.getAccountId();
	
	String hiba = "FROM BossExtTransaction WHERE TRANSTO = :tranto";

	org.hibernate.Query quea = session.createQuery(hiba);
	
	quea.setParameter("tranto", b);
				
	String hib = "FROM BossExtTransaction WHERE UNAME = :username";

	org.hibernate.Query que = session.createQuery(hib);
	
	que.setParameter("username", username);
	
	List<BossExtTransaction> lis = que.list();
	
	List<BossExtTransaction> lis1 = quea.list();
			
    Set<BossExtTransaction> list4 = new LinkedHashSet<BossExtTransaction>();
      
    list4.addAll(lis);
      
    list4.addAll(lis1);
      
    account = new ArrayList<BossExtTransaction>(list4);
    
}
catch(Exception ex)
{
	ex.printStackTrace();
}
finally{
	session.flush();
	session.close();
}
      
return account;	
}

@SuppressWarnings({ "rawtypes" })
@Transactional
public synchronized List<BossExtTransaction> viewtransactiondetailss(String username)
{
	List<BossExtTransaction> account = new ArrayList<BossExtTransaction>();
	
	Session session=null;
	try
	{
	session = this.sessionFactory.openSession();
			
	String hib = "FROM BossExtTransaction WHERE UNAME = :username and TRANSTYPE=:transtype and REVFLAG=:flag";//praneeth changed

	org.hibernate.Query que = session.createQuery(hib);
	
	que.setParameter("username", username);
	
	que.setParameter("transtype", "transfer");
	
	que.setParameter("flag", "N");
		
	List li = que.list();
	
	for(int i=0;i<li.size();i++)
	{
		BossExtTransaction accountObj = (BossExtTransaction)li.get(i);
		
		account.add(accountObj);
	}
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	return account;
	
}


@SuppressWarnings("unchecked")
@Transactional
public synchronized String credit(double amount,String username)
{
	String status=null;
	
	Session session=null;
	
	try
	{
	session = this.sessionFactory.openSession();
			
	String hib = "FROM BossAccount WHERE UNAME = :username";

	Query que = session.createQuery(hib);
	
	que.setParameter("username", username);
	
	List<BossAccount> userList = que.list();
	
	BossAccount accnts = userList.get(0);

	double oldbal=accnts.getAccountbal();
			
	double total=amount+oldbal;
	
	if(total<100000)
	{
			
	String hibs = "UPDATE BossAccount set ACCTBAL =:Balances WHERE UNAME =:Username";
	
	que = session.createQuery(hibs);
	
	que.setParameter("Balances",total);
	
	que.setParameter("Username",username);

	int result = que.executeUpdate();
	
				
	if (result==1)
	{
		status="true";
	}
	else
	
	status="false";
				
return status;
	}
	
	else
	{
		status="false";
	}
	}
	
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	return "fail";
	}



@SuppressWarnings("unchecked")
public synchronized boolean saveTranscredit(double amount,String username,String status)
{
	boolean b=true;
	
	Session session=null;
	
	try
	{
	session = this.sessionFactory.openSession();
	
	session.beginTransaction();
	
	String hib = "FROM BossAccount WHERE UNAME =:use";
		
	Query que = session.createQuery(hib);
	
	que.setParameter("use", username);
	
	List<BossAccount> userList = que.list();
	
	BossAccount accnts = userList.get(0);
	
	String accid=accnts.getAccountId();
	
	BossExtTransaction bet=new BossExtTransaction();
	
	bet.setTransamt(amount);
	
	//Date myDate = new Date();
	
	bet.setTransdate(new Date());
	
	bet.setTransdesc("Credited money");
	
	bet.setTransfrom(accid);
	
	bet.setTransto(accid);
	
	bet.setTranstype("credit");
	
	bet.setUsername(username);
	
	bet.setTransstatus(status);
	
	bet.setFlag("N");
		
	bet.setRevflag("N");
	
	bet.setPayflag("N");
	
	session.save(bet);
	
	session.getTransaction().commit();
	
	return b;
	
	}
	
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	return false;
}


@SuppressWarnings("unchecked")
@Transactional
public synchronized boolean saveTransdebit(double amount,String username,String status)
{
boolean b=true;
	
	Session session=null;
	
	try
	{
	session = this.sessionFactory.openSession();
	
	session.beginTransaction();
	
	String hib = "FROM BossAccount WHERE UNAME =:use";
		
	Query que = session.createQuery(hib);
	
	que.setParameter("use", username);
	
	List<BossAccount> userList = que.list();
	
	BossAccount accnts = userList.get(0);
	
	String accid=accnts.getAccountId();
	
	BossExtTransaction bet=new BossExtTransaction();
	
	bet.setTransamt(amount);
	
	//Date myDate = new Date();
	
	bet.setTransdate(new Date());
		
	bet.setTransdesc("Debited money");
	
	bet.setTransfrom(accid);
	
	bet.setTransto(accid);
	
	bet.setTranstype("debit");
	
	bet.setUsername(username);
	
	bet.setTransstatus(status);
	
	bet.setFlag("N");
	
	bet.setRevflag("N");
	
	bet.setPayflag("N");
	
	session.save(bet);
	
	session.getTransaction().commit();
	
	return b;
	
	}
	
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	return false;
}



@SuppressWarnings("unchecked")
@Transactional
public synchronized String debit(double amount,String username)
{
	String status=null;
	
	Session session=null;
	try
	{
	session = this.sessionFactory.openSession();
	
	String hib = "FROM BossAccount WHERE UNAME = :username";

	Query que = session.createQuery(hib);
	
	que.setParameter("username", username);
	
	List<BossAccount> userList = que.list();
	
	BossAccount accnts = userList.get(0);

	double oldbal=accnts.getAccountbal();
	
	if(oldbal<amount)
	{
		status="false";
	}
	else
	{
	double total=oldbal-amount;
	
	String hibs = "UPDATE BossAccount set ACCTBAL =:Balances WHERE UNAME =:username";
	
	que = session.createQuery(hibs);
	
	que.setParameter("Balances",total);
	
	que.setParameter("username",username);

	que.executeUpdate();
	
		
	status="true";
	}
return status;

}
	
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	return "fail";
	}


@SuppressWarnings({ "unchecked", "null" })
@Transactional
public synchronized String fundtransfer(String account,double amount,String username)
{
	//String status=null;
	Session session=null;
	
	try
	{
		
		session = this.sessionFactory.openSession();
		
		String boss2 = "FROM BossAccount WHERE ACCTNUM = :account";
		Query	que = session.createQuery(boss2);
		que.setParameter("account",account);
		List<BossAccount> accountList = que.list();
		
		if(!accountList.isEmpty())
		{		
			String boss4 = "FROM BossAccount WHERE UNAME = :username";
			Query ques = session.createQuery(boss4);
			ques.setParameter("username", username);
			
			List<BossAccount> userList = ques.list();
			BossAccount accnts = userList.get(0);
			String fromAccID=accnts.getAccountId();
						
			if(!fromAccID.equals(account))
			{			
				
				String boss9 = "select a.accountId FROM BossAccount a,BossRole r WHERE a.uname=r.uname AND r.roletype=:roles AND a.accountId=:account";
				que = session.createQuery(boss9);
				que.setParameter("roles","ROLE_MERCHANT");
				que.setParameter("account",account); // added by rishabh
				List<String[]> list = que.list();
				
				if(list==null || list.isEmpty()){
					
					
					double fromAccBal = accnts.getAccountbal();
					
					
					if(fromAccBal > amount){
						
						 
						BossAccount accnt = accountList.get(0);
						double toaccount=accnt.getAccountbal();
						
						double fromamount=fromAccBal-amount;
						double toamount=toaccount+amount;
						
						
						String boss = "UPDATE BossAccount set ACCTBAL =:Balances WHERE UNAME =:Username";
						que = session.createQuery(boss);
						que.setParameter("Balances",fromamount);
						que.setParameter("Username",username);
						que.executeUpdate();

						
						String boss5 = "UPDATE BossAccount set ACCTBAL =:Balances WHERE ACCTNUM = :account";
						que = session.createQuery(boss5);
						que.setParameter("Balances",toamount);
						que.setParameter("account",account);
						que.executeUpdate();

						return "true";
						
					} else{
						return "false,Not Enough Funds available";
					}
					
				} else {
					return "false,Account cannot be Merchant's";
				}
				
			} else {
				return "false,Account cannot be User's";
			}
			
		} else {
			return "false,Account Number Doesn't Exists"; //added by praneeth
		}
	}

	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	return "fail";
}


@SuppressWarnings("unchecked")
@Transactional
public synchronized String transactionFunds(String account,double amount,String name,String status)
{
	
	Session session=null;
	try
	{
	session=this.sessionFactory.openSession();
	
	session.beginTransaction();
	
	String hq="FROM BossAccount WHERE UNAME  =:name";
	
	Query que=session.createQuery(hq);
	
	que.setParameter("name",name);
		
	List<BossAccount> userList = que.list();
	
	BossAccount accnts = userList.get(0);
	
	String fromaccntnum=accnts.getAccountId();
	
	String toaccntnum=account;
	
	BossExtTransaction bet=new BossExtTransaction();
	
	bet.setTransamt(amount);
	
	//Date myDate = new Date();
	
	bet.setTransdate(new Date());
	
	bet.setTransdesc("Transfer");
	
	bet.setTransfrom(fromaccntnum);
	
	bet.setTransto(toaccntnum);
	
	bet.setUsername(name);
	
	bet.setTransstatus(status);
	
	bet.setTranstype("transfer");
	
	bet.setFlag("N");
	
	bet.setRevflag("N");
	
	bet.setPayflag("N");
	
	session.save(bet);
	
	session.getTransaction().commit();
		
	return "true";
	}
	
catch(Exception ex)
{
	ex.printStackTrace();
}
finally{
	session.flush();
	session.close();
}
return "fail";
}


@SuppressWarnings("unchecked")
@Transactional
public synchronized String Updateprofiledetails(String username,String Fname,String Lname,String Address,long Phoneno,String Email)
{
	
	
	String b=null;
	
	Session session=null;
	
	try
	{
	
	session = this.sessionFactory.openSession();
	
	session.beginTransaction();

	String hib = "FROM BossUser WHERE UNAME = :Username";

	org.hibernate.Query que = session.createQuery(hib);
	
	que.setParameter("Username", username);

	List<BossUser> results = que.list();
	
	BossUser bu = results.get(0);
	
	long uid=bu.getId();
	
	String stat=bu.getStatus(); 
	
	String ssn=bu.getSsn();
	
	String psswd=bu.getPassword();
	
	String piiflag=bu.getPiiflag();
					
	String hibs = "FROM BossUserTemp WHERE UNAME = :username";
	
	org.hibernate.Query ques = session.createQuery(hibs);
	
	ques.setParameter("username", username);

	List<BossUserTemp> result = ques.list();
	
	
	if(!(result==null ||  result.isEmpty()))
	{
		b="false";
		
	}
	else
	{
	BossUserTemp temp=new BossUserTemp();
	
	temp.setAddress(Address);
	
	temp.setEmail(Email);
	
	temp.setEnabled("1");
	
	temp.setFlag("R");
	
	temp.setStatus("Pending");
	
	temp.setFname(Fname);
	
	temp.setLname(Lname);
	
	temp.setSsn(ssn);
	
	temp.setUname(username);
	
	temp.setPhoneno(Phoneno);
	
	temp.setPassword(psswd);
	
	temp.setStatus(stat);
	
	temp.setUserid(uid);
	
	temp.setPiiflag(piiflag);

	session.save(temp);
	
	session.getTransaction().commit();
	
	b="true";
	}
	
	return b;
	
}
	
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	
	b="fail";
	
	return b;
}

@Transactional
public synchronized String Authorizetransactions(String username)
	{

	Session session=null;
			
	try
	{
	session = this.sessionFactory.openSession();
	
	String boss = "UPDATE BossExtTransaction set FLAG = :flag where UNAME = :user";

	Query que = session.createQuery(boss);	
	
	que.setParameter("flag", "RA");
	
	que.setParameter("user", username);
	
	que.executeUpdate();
	
	return "true";	
}
	
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	return "fail";
		
		
	}



@SuppressWarnings("unchecked")
@Transactional
public synchronized String Viewpiiauthentication(String name)
{
	
	String r=null; 
	
	Session session=null;
	try
	{
	session = this.sessionFactory.openSession();
	
	session.beginTransaction();

	String hib = "FROM BossUser WHERE UNAME = :Username";

	org.hibernate.Query que = session.createQuery(hib);
	
	que.setParameter("Username", name);

	List<BossUser> results = que.list();
	
	BossUser bu = results.get(0);
	
	String dat=bu.getPiiflag();
	
	String str="R";
	
	if(dat.equals(str))
	{
		r="true";
	}
	else
	{
		r="false";
	}
	return r;
	}
	
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	r="failed";
	
	return r;
}

@Transactional
public synchronized String PiiAuthentication(String name)
{
	
	String b=null; 
	
	Session session=null;
	try
	{
	session = this.sessionFactory.openSession();
	
	session.beginTransaction();

	
		String hiber = "Update BossUser set PIIFLAG=:stat WHERE UNAME = :Username";
		
		org.hibernate.Query ques = session.createQuery(hiber);
		
		ques.setParameter("Username", name);
		
		ques.setParameter("stat", "RA");
		
		int r=ques.executeUpdate();
		
		if(r==1)
			
		b="true";
		else
			b="false";
		session.getTransaction().commit();
	
	return b;
	}
	
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	b="fail";
	
	return b;
}


@Transactional
public synchronized String Revoketrans(String name,long tranid)
{
			
	Session session=null;
	
	String b=null;
	
	try
	{
	session = this.sessionFactory.openSession();
	
	session.beginTransaction();
	
	String hiber = "Update BossExtTransaction set REVFLAG=:stat WHERE TRANSID = :tran";
	
	org.hibernate.Query ques = session.createQuery(hiber);
	
	ques.setParameter("tran", tranid);
	
	ques.setParameter("stat", "RE");
	
	int r=ques.executeUpdate();	
		
	session.getTransaction().commit();
	
	if(r==1)
	
	return "true";
	
	else
		
		return "false";
	}
	
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	
	b="fail";
	
	return b;
}

@SuppressWarnings("rawtypes")
@Transactional
public synchronized String getaccount(String account,String name)
{
			
	Session session=null;
	
	String b=null;
	
	try
	{
	session = this.sessionFactory.openSession();
	
	session.beginTransaction();
	
	String hiber = "FROM BossAccount WHERE ACCTNUM = :tran and UNAME =:username";
	
	org.hibernate.Query ques = session.createQuery(hiber);
	
	ques.setParameter("tran", account);
	
	ques.setParameter("username",name);
	
	List r=ques.list();
	
	int d=r.size();
		
	session.getTransaction().commit();
	
	if(d==1)
	
	return "true";
	
	else
		
		return "false";
	}
	
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	
	b="fail";
	
	return b;
}

@SuppressWarnings("unchecked")
@Transactional
public synchronized String Deleteuser(String name)
{
			
	Session session=null;
	
	String s=null;
	try
	{
	session = this.sessionFactory.openSession();
	String hql;
	Query query;
	session.beginTransaction();
	
	hql = "FROM BossUserTemp WHERE UNAME = :uname"; // Priya changes
	query = session.createQuery(hql);
	query.setParameter("uname", name);
	List<BossUserTemp> TempTransLists = query.list(); // Priya changes
	int count8 = TempTransLists.size();
	
	
	hql = "FROM BossAccount WHERE UNAME = :uname";
	query = session.createQuery(hql);
	query.setParameter("uname", name);
	List<BossAccount> TempAccounts = query.list();
	BossAccount TempAccount = TempAccounts.get(0);
	Criteria cr;
	
	/*Criteria cr = session.createCriteria(BossUserTemp.class)
		    .setProjection(Projections.projectionList()
		      .add(Projections.property("uname"), "uname"))
		      .add( Restrictions.ne("flag", "D" ) )
		      .add( Restrictions.eq("uname", name ) )
		    .setResultTransformer(Transformers.aliasToBean(BossUserTemp.class));

		  List<BossUserTemp> UserTempList = cr.list();
			int count2 = UserTempList.size();
			//System.out.println("Value of Count2 :: " + count2); */
	
	Criterion c1 = Restrictions.or(Restrictions.ne("piiflag", "N" ),Restrictions.ne("flag", "N" ) );
	Criterion c2 = Restrictions.eq("uname", name );
	cr = session.createCriteria(BossUser.class)
		.setProjection(Projections.projectionList()
		.add(Projections.property("uname"), "uname"))
		.add(Restrictions.and(c1,c2))
		.setResultTransformer(Transformers.aliasToBean(BossUser.class));

		List<BossUser> UserList = cr.list();
		int count1 = UserList.size();
	//	System.out.println("Value of Count1 :: " + count1);
	
		c1 = Restrictions.or(Restrictions.ne("revflag", "N" ),Restrictions.ne("payflag", "N" ) );
		c2 = Restrictions.eq("username", name );
		cr = session.createCriteria(BossExtTransaction.class)
		.setProjection(Projections.projectionList()
		.add(Projections.property("transid"), "transid"))
		.add(Restrictions.and(c1,c2))
		.setResultTransformer(Transformers.aliasToBean(BossExtTransaction.class));

		List<BossExtTransaction> TransList = cr.list();
		int count3 = TransList.size();
		System.out.println("Value of Count3 :: " + count3);
	
	hql = "FROM BossExtTransactionTemp WHERE UNAME = :uname";
	query = session.createQuery(hql);
	query.setParameter("uname", name);
	List<BossExtTransactionTemp> TempTransList = query.list();
	int count4 = TempTransList.size();
	//System.out.println("Value of Count4 :: " + count4);
	
	c2 = Restrictions.eq("transto", TempAccount.getAccountId() );
	cr = session.createCriteria(BossExtTransaction.class)
			.setProjection(Projections.projectionList()
			.add(Projections.property("transid"), "transid"))
			.add(Restrictions.and(c1,c2))
			.setResultTransformer(Transformers.aliasToBean(BossExtTransaction.class));

			TransList = cr.list();
			int count5 = TransList.size();
		//	System.out.println("Value of Count5 :: " + count5);
			
			cr = session.createCriteria(BossExtTransactionTemp.class)
					.setProjection(Projections.projectionList()
					.add(Projections.property("transid"), "transid"))
					.add(Restrictions.and(c1,c2))
					.setResultTransformer(Transformers.aliasToBean(BossExtTransactionTemp.class));

					TransList = cr.list();
					int count6 = TransList.size();
					//System.out.println("Value of Count6 :: " + count6);
	
	if(count1 > 0 || count8 > 0 || count3 > 0 || count4 >0 || count5 > 0 || count6 > 0)
	{
			
	return "false";
	}
	
	
	else
	{
		
	String hiber = "FROM BossUser WHERE UNAME = :tran";
	
	org.hibernate.Query ques = session.createQuery(hiber);
	
	ques.setParameter("tran", name);
	
	List<BossUser> results = ques.list();
	
	BossUser bus = results.get(0);
	
	String add=bus.getAddress();
	
	String  email=bus.getEmail();
	
	String finame=bus.getFname();
	
	String liname=bus.getLname();
	
	String pswd=bus.getPassword();
	
	long phn=bus.getPhoneno();
	
	String pii=bus.getPiiflag();
	
	String ssn=bus.getSsn();
	
	long uid=bus.getId();
	
	BossUserTemp temp=new BossUserTemp();
	
	temp.setAddress(add);
	
	temp.setEmail(email);
	
	temp.setEnabled("1");
	
	temp.setFlag("D");
	
	temp.setStatus("Pending");
	
	temp.setFname(finame);
	
	temp.setLname(liname);
	
	temp.setSsn(ssn);
	
	temp.setUname(name);
	
	temp.setPhoneno(phn);
	
	temp.setPassword(pswd);
	
	temp.setUserid(uid);
	
	temp.setPiiflag(pii);

	session.save(temp);
	
	session.getTransaction().commit();
	
		return "true";
	}
	}
	
	
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	
	s="fail";
	
	return s;
}



@SuppressWarnings({"unchecked"})
@Transactional
public long findIdByName(String username) {
	Session session = null;
	try {
		session = sessionFactory.getCurrentSession();
	} catch (HibernateException  e1) {
		e1.printStackTrace();
	};
	
	String hql= "from BossUser where UNAME=:userName ";
	Query  q = (org.hibernate.Query) session.createQuery(hql);
	q.setParameter("userName",username);
	BossUser user = (BossUser) q.uniqueResult();
	//session.get(BossUser.class, username);
	
	long userId = user.getId();
	return userId;
	
}

@Transactional
public String getPasswordById(long userId) {
	Session session = null;
	try {
		session = sessionFactory.getCurrentSession();
	} catch (HibernateException  e1) {
		e1.printStackTrace();
	};
	
	BossUserLogin userLogin = (BossUserLogin) session.get(BossUserLogin.class, userId);
	String password = userLogin.getPassword();
	return password;
}

@Transactional
public String getPasswordByName(String userName) {
	Session session = null;
	try {
		session = sessionFactory.getCurrentSession();

	
	String hib = "FROM BossUserLogin WHERE USER_NAME = :username";

	Query que = session.createQuery(hib);
	
	que.setParameter("username", userName);
	
	List<BossUserLogin> userList = que.list();
	
	BossUserLogin accnts = userList.get(0);
	
	String password=accnts.getPassword();
	
	//BossUserLogin userLogin = (BossUserLogin) session.get(BossUserLogin.class, userName);
	
	//String password = userLogin.getPassword();
	return password;
	} catch (HibernateException  e1) {
		e1.printStackTrace();
		return "";
	}
	catch (Exception  e1) {
		e1.printStackTrace();
		return "";
	}
	
	
}


//@Transactional
//public String getLoginFlagByName(String userName) {
//	Session session = null;
//	try {
//		session = sessionFactory.getCurrentSession();
//
//	
//	String hib = "FROM BossUserLogin WHERE USER_NAME = :username";
//
//	Query que = session.createQuery(hib);
//	
//	que.setParameter("username", userName);
//	
//	List<BossUserLogin> userList = que.list();
//	
//	BossUserLogin accnts = userList.get(0);
//	
//	//String loginFlag=accnts.getLoginFlag();
//	
//	//BossUserLogin userLogin = (BossUserLogin) session.get(BossUserLogin.class, userName);
//	
//	//String password = userLogin.getPassword();
//	return loginFlag;
//	} catch (HibernateException  e1) {
//		e1.printStackTrace();
//		return "";
//	}
//	catch (Exception  e1) {
//		e1.printStackTrace();
//		return "";
//	}
//	
//	
//}


@Transactional
public List<String> getRolesbyUserId(long userId) {
	Session session = null;
	List<String> roleList = new ArrayList<String>() ;
	try {
		session = sessionFactory.getCurrentSession();
	
	
	String hql= "from BossRole where USER_ID=:userId ";
	Query  q = (org.hibernate.Query) session.createQuery(hql);
	q.setParameter("userId",userId);
	List<BossRole> roles = (List<BossRole>) q.list();
	//session.get(BossRole.class, userId);
	
	for(BossRole role:roles){
		roleList.add(role.getRoletype());
	}
	} catch (HibernateException  e1) {
		e1.printStackTrace();
	};
	return roleList;
}

@Transactional
public int getLoginAttempts(String userName) {
	
	Session session = null;
	int loginAttempts=0;
	try {
		long userId=findIdByName(userName);
		session = sessionFactory.getCurrentSession();
	
	
	String hql= "from BossUserLogin where USER_ID=:userId ";
	Query  q = (org.hibernate.Query) session.createQuery(hql);
	q.setParameter("userId",userId);
    BossUserLogin userLogin2 = (BossUserLogin)q.uniqueResult();
    loginAttempts=userLogin2.getLoginAttempts();
    //userLogin2=null;
    
	} catch (HibernateException  e1) {
		e1.printStackTrace();
	}
	finally{
		
	}
	return loginAttempts;
}

@Transactional
public String getOTPFlag(String username) {
	Session session = null;
	String otpFlag="";
	try {
		long userId=findIdByName(username);
		session = sessionFactory.getCurrentSession();
	
	
	String hql= "from BossUserLogin where USER_ID=:userId ";
	Query  q = (org.hibernate.Query) session.createQuery(hql);
	q.setParameter("userId",userId);
    BossUserLogin userLogin = (BossUserLogin)q.uniqueResult();
    otpFlag=userLogin.getOtpFlag();
    
	} catch (HibernateException  e1) {
		e1.printStackTrace();
	};
	return otpFlag;
}



//@Transactional
//public int updateLoginFlagY(String username) {
//	Session session = null; 
//	int updated=0;
//	try {
//		long userId=findIdByName(username);
//		session = sessionFactory.getCurrentSession();
//		String hql= "update BossUserLogin set LOGIN_FLAG=:LF where USER_ID=:userId ";
//		Query  q = (org.hibernate.Query) session.createQuery(hql);
//		q.setParameter("userId",userId);
//		q.setParameter("LF","Y");
//		updated=q.executeUpdate();
//    
//	} catch (HibernateException  e1) {
//		e1.printStackTrace();
//		return updated;
//	}
//	catch (Exception  e) {
//		e.printStackTrace();
//		return updated;
//	}
//	return updated;
//}
//
//
//@Transactional
//public int updateLoginFlagN(String username) {
//	Session session = null; 
//	int updated=0;
//	try {
//		long userId=findIdByName(username);
//		session = sessionFactory.getCurrentSession();
//		String hql= "update BossUserLogin set LOGIN_FLAG=:LF where USER_ID=:userId ";
//		Query  q = (org.hibernate.Query) session.createQuery(hql);
//		q.setParameter("userId",userId);
//		q.setParameter("LF","N");
//		updated=q.executeUpdate();
//    
//	} catch (HibernateException  e1) {
//		e1.printStackTrace();
//		return updated;
//	}
//	catch (Exception  e) {
//		e.printStackTrace();
//		return updated;
//	}
//	return updated;
//}

@Transactional
public boolean saveRole(BossRole roleEntity) {
	Session session = null;
	
	try {
		
		session = sessionFactory.getCurrentSession();
	    session.save(roleEntity);
	    return true;
    
	} 
	catch (SQLGrammarException  sge) {
		sge.printStackTrace();
		return false;
			
	}
	catch (HibernateException  he) {
		he.printStackTrace();
		return false;
	}
	catch(Exception se){
		se.printStackTrace();
		return false;
	}
}	
	public boolean saveAccount(BossAccount accountEntity) {
		Session session = null;
		
		try {
			
			session = sessionFactory.getCurrentSession();
		    session.save(accountEntity);
		    return true;
	    
		} 
		catch (SQLGrammarException  sge) {
			sge.printStackTrace();
			return false;
				
		}
		catch (HibernateException  he) {
			he.printStackTrace();
			return false;
		}
		catch(Exception se){
			se.printStackTrace();
			return false;
		}
		
	
}

@Transactional
public int updateLoginAttempts(String username) {
	Session session = null;
	int updated=0;
	try {
		long userId=findIdByName(username);
		int loginAttempts=getLoginAttempts(username);
		//if(loginAttempts<3){
		if(loginAttempts<4){
			loginAttempts+=1;
		}
		session = sessionFactory.getCurrentSession();
	
	//session.update(arg0, arg1);
	String hql= "update BossUserLogin set login_attempts=:LA where USER_ID=:userId ";
	Query  q = (org.hibernate.Query) session.createQuery(hql);
	q.setParameter("userId",userId);
	q.setParameter("LA",loginAttempts);
	updated=q.executeUpdate();
    
	} catch (HibernateException  e1) {
		e1.printStackTrace();
	}
	return updated;
}

@Transactional
public String sendOTP(String userName) {
	Session session = null;
	String eMailId= getUserEmail(userName);
	if (eMailId.equals("error")||eMailId=="error"){
		return "not sent";
	}
            String result="not sent";    
				try {
					session = sessionFactory.getCurrentSession();
					SendEmail emailClient = new SendEmail();		
					 long otp = UUID.randomUUID().getMostSignificantBits();
				    result=emailClient.sendResetPasswordMail(eMailId,otp);
				    if(result.equals("sent")||result=="sent"){
				    	String hib = "UPDATE BossUserLogin set OTP=:otp, OTP_FLAG=:flag, OTP_EXPIRATION=:otpExp WHERE USER_NAME = :username";

						org.hibernate.Query que = session.createQuery(hib);
						que.setParameter("otp", otp);
						que.setParameter("flag", "Y");
						Calendar c = Calendar.getInstance(); 
						c.setTime(new Date()); 
						c.add(Calendar.HOUR, 1);
						Date dt = c.getTime();
						que.setParameter("otpExp", dt);
						que.setParameter("username", userName);
						int status=que.executeUpdate();
						if(status==1)
						return result;
						else{
							return "not sent";
						}
				    //session.save(userLogin);
				    }
				
					
				} catch (Exception e) {
					e.printStackTrace();
					return result;
				}
	
		
		return result;

}

@SuppressWarnings({"unchecked"})
@Transactional
public synchronized List<String[]> Extmerchantview()
{
	
	
	Session session=null;
	
	List<String[]> list=null;
	
	try
	{
	session = this.sessionFactory.openSession();
	
	session.beginTransaction();

	org.hibernate.Query que = session.createQuery("select b.uname FROM BossUser b,BossRole u WHERE b.uname=u.uname AND u.roletype=:roles");
	
	que.setParameter("roles", "ROLE_MERCHANT");

	list = que.list();
			
	}
	
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	
	return list;
}

@SuppressWarnings("unchecked")
@Transactional
public synchronized boolean extUserPay(String name,String account,double amount,String merchant)
{
			
	Session session=null;
	
	try
	{
	session = this.sessionFactory.openSession();
	
	session.beginTransaction();
		
	String hibs = "FROM BossAccount WHERE UNAME = :Username";

	org.hibernate.Query que = session.createQuery(hibs);
	
	que.setParameter("Username", merchant);

	List<BossAccount> results = que.list();
	
	BossAccount bu = results.get(0);
	
	String dat=bu.getAccountId();
	
	BossExtTransactionTemp temp=new BossExtTransactionTemp();
	
	temp.setFlag("N");
	
	temp.setRevflag("N");
	
	temp.setPayflag("PI");
	
	temp.setTransamt(amount);
	
	//Date myDate = new Date();
	
	temp.setTransdate(new Date());
	
	temp.setTransdesc("Payment Initiated");
	
	temp.setTransfrom(account);
	
	temp.setTransto(dat);
	
	temp.setTranstype("Payment");
	
	temp.setUsername(name);
	
	temp.setTransstatus("Pending");
	
	session.save(temp);
	
	session.getTransaction().commit();
	
	return true;

	
	}
	catch(Exception ex)
	{
		ex.printStackTrace();
	}
	finally{
		session.flush();
		session.close();
	}
	
	return false;
}

public String createInternalUser(BossUser userEntity) {

	Session session = null;
	String result="not sent";
	try {
		session = this.sessionFactory.getCurrentSession();
	} catch (HibernateException  e1) {
		e1.printStackTrace();
	};

    long userId =  UUID.randomUUID().getMostSignificantBits();
	try{
			BossUser customerEntity = new BossUser();
			customerEntity.setFname(userEntity.getFname());
			customerEntity.setLname(userEntity.getLname());
			customerEntity.setId(userId);
			customerEntity.setAddress(userEntity.getAddress());
			customerEntity.setPhoneno(userEntity.getPhoneno());
			//customerEntity.setCustomerType(customerType);
			customerEntity.setEmail(userEntity.getEmail());
			customerEntity.setSsn(userEntity.getSsn());
			customerEntity.setUname(userEntity.getUname());
			String pswd = UUID.randomUUID().toString();
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String encodedPassword = passwordEncoder.encode(pswd); 
			customerEntity.setPassword(encodedPassword);
			customerEntity.setFlag("N");
			customerEntity.setPiiflag("N");
			customerEntity.setStatus("active");
			//customerEntity.setFirstname(firstName);
            
			//try {

				session.save(customerEntity);
                BossRole roleEntity =new BossRole();
                roleEntity.setRoletype("ROLE_EMPLOYEE");
                roleEntity.setUname(userEntity.getUname());
                roleEntity.setUserId(userId);
                
				if(saveRole(roleEntity)){					
				SendEmail emailClient = new SendEmail();
			    long otp = UUID.randomUUID().getMostSignificantBits();

                //String result=emailClient.sendSignUpMail(userEntity.getEmail(),otp,"C:\\Users\\rbh\\Documents\\workspace-sts-3.5.1.RELEASE\\boss-rs\\certificates\\"+userEntity.getUname()+"_cert");

			    BossUserLogin userLogin= new BossUserLogin();
			    userLogin.setUserId(userId);
			    userLogin.setOtp(otp);
			    userLogin.setUserName(userEntity.getUname());
			    userLogin.setPassword(encodedPassword);
			    userLogin.setLoginAttempts(0);
				userLogin.setOtpFlag("Y");
				Calendar c = Calendar.getInstance(); 
				c.setTime(new Date()); 
				c.add(Calendar.HOUR, 1);
				Date dt = c.getTime();
                userLogin.setOtpExpiration(dt);
                
                
                Calendar c2 = Calendar.getInstance(); 
				c2.setTime(new Date()); 
				c2.add(Calendar.MONTH, 1);
				Date dt2 = c2.getTime();
				userLogin.setPasswordExpiration(dt);
                session.save(userLogin);
                
                 //result=emailClient.sendSignUpMail(userEntity.getEmail(),otp,"C:\\Users\\rbh\\Documents\\workspace-sts-3.5.1.RELEASE\\boss-rs\\certificates\\"+userEntity.getUname()+"_cert","C:\\Users\\rbh\\Documents\\workspace-sts-3.5.1.RELEASE\\boss-rs\\keys\\"+userEntity.getUname()+"_DSAPrivateKey.key");
                result=emailClient.sendResetPasswordMail(userEntity.getEmail(),otp);

				}
			} 
			catch(SQLGrammarException sge){
			//	regChk.setErrorMessage("SQLGrammerException");
				SQLException se = new SQLException();
				throw new SQLGrammarException("SQL Grammer Exception", se);
			}
			catch (Exception e) {
				e.printStackTrace();
				return result;
			}
			finally {
			}
		
	
	return result;
	
}

public String createMerchant(BossUser userEntity) {

	Session session = null;
	String result="not sent";
	try {
		session = this.sessionFactory.getCurrentSession();
	} catch (HibernateException  e1) {
		e1.printStackTrace();
	};

    long userId =  UUID.randomUUID().getMostSignificantBits();
	try{
			BossUser customerEntity = new BossUser();
			customerEntity.setFname(userEntity.getFname());
			customerEntity.setLname(userEntity.getLname());
			customerEntity.setId(userId);
			customerEntity.setAddress(userEntity.getAddress());
			customerEntity.setPhoneno(userEntity.getPhoneno());
			//customerEntity.setCustomerType(customerType);
			customerEntity.setEmail(userEntity.getEmail());
			customerEntity.setSsn(userEntity.getSsn());
			customerEntity.setUname(userEntity.getUname());
			String pswd = UUID.randomUUID().toString();
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String encodedPassword = passwordEncoder.encode(pswd); 
			customerEntity.setPassword(encodedPassword);
			customerEntity.setFlag("N");
			customerEntity.setPiiflag("N");
			customerEntity.setStatus("active");
			//customerEntity.setFirstname(firstName);
            
			//try {

				session.save(customerEntity);
				//long accountId=UUID.randomUUID().getMostSignificantBits();
				//String accountNo= userEntity.getUname()+accountId;
				long accountId=UUID.randomUUID().getMostSignificantBits();
				accountId=Math.abs(accountId);
				String accountNo= ""+accountId;
				BossAccount accountEntity = new BossAccount();
				accountEntity.setAccountId(accountNo);
				accountEntity.setUname(userEntity.getUname());
				accountEntity.setAccountbal(1000.00);;
				accountEntity.setCreateddate(new Date());
                BossRole roleEntity =new BossRole();
                roleEntity.setRoletype("ROLE_MERCHANT");
                roleEntity.setUname(userEntity.getUname());
                roleEntity.setUserId(userId);
                
				if(saveRole(roleEntity)&&saveAccount(accountEntity)){					
				SendEmail emailClient = new SendEmail();
				byte[] certificate=GenerateCertificate.PublicPrivateKeyGeneration(userEntity.getUname());			
			    FileOutputStream fos = new FileOutputStream(new File("./"+userEntity.getUname()+"certificate"));
			    fos.write(certificate);
			    long otp = UUID.randomUUID().getMostSignificantBits();

                //String result=emailClient.sendSignUpMail(userEntity.getEmail(),otp,"C:\\Users\\rbh\\Documents\\workspace-sts-3.5.1.RELEASE\\boss-rs\\certificates\\"+userEntity.getUname()+"_cert");

			    BossUserLogin userLogin= new BossUserLogin();
			    userLogin.setUserId(userId);
			    userLogin.setOtp(otp);
			    userLogin.setUserName(userEntity.getUname());
			    userLogin.setPassword(encodedPassword);
			    userLogin.setLoginAttempts(0);
				userLogin.setOtpFlag("Y");
				Calendar c = Calendar.getInstance(); 
				c.setTime(new Date()); 
				c.add(Calendar.HOUR, 1);
				Date dt = c.getTime();
                userLogin.setOtpExpiration(dt);
                
                
                Calendar c2 = Calendar.getInstance(); 
				c2.setTime(new Date()); 
				c2.add(Calendar.MONTH, 1);
				Date dt2 = c2.getTime();
				userLogin.setPasswordExpiration(dt);
                session.save(userLogin);
                
                 //result=emailClient.sendSignUpMail(userEntity.getEmail(),otp,"C:\\Users\\rbh\\Documents\\workspace-sts-3.5.1.RELEASE\\boss-rs\\certificates\\"+userEntity.getUname()+"_cert","C:\\Users\\rbh\\Documents\\workspace-sts-3.5.1.RELEASE\\boss-rs\\keys\\"+userEntity.getUname()+"_DSAPrivateKey.key");
                result=emailClient.sendSignUpMail(userEntity.getEmail(),otp,"./"+userEntity.getUname()+"certificate","./"+userEntity.getUname()+"DSAPrivateKey.key");


				}
			} 
			catch(SQLGrammarException sge){
			//	regChk.setErrorMessage("SQLGrammerException");
				SQLException se = new SQLException();
				throw new SQLGrammarException("SQL Grammer Exception", se);
			}
			catch (Exception e) {
				e.printStackTrace();
				return result;
			}
			finally {
			}
		
	
	return result;	
	
}

}