package boss.data.repositories;

import org.hibernate.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.sql.*;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import boss.data.entities.BossAccount;
import boss.data.entities.BossExtTransaction;
import boss.data.entities.BossUser;
import boss.data.entities.BossUserTemp;
import boss.data.entities.BossRole;
import boss.data.entities.BossExtTransactionTemp;

import java.lang.*;

@Repository
public class AdminRepository 
{
	@Value("test")
	protected String tableSchema;

	@Autowired
	private SessionFactory sessionFactory;
	
	public AdminRepository() 
	{
		
	}
	
	// Delete User Requests
	@SuppressWarnings("unchecked")
	@Transactional
	public synchronized List<BossUserTemp> viewDeleteRequests()
	{
		Session session = null;
		List<BossUserTemp> list = new ArrayList<BossUserTemp>();
		try
		{
		 session = this.sessionFactory.openSession();
		 
		  Criteria cr = session.createCriteria(BossUserTemp.class)
				    .setProjection(Projections.projectionList()
				      .add(Projections.property("fname"), "fname")
				      .add(Projections.property("lname"), "lname")
				      .add(Projections.property("address"), "address")
				      .add(Projections.property("phoneno"), "phoneno")
				      .add(Projections.property("email"), "email")
				      .add(Projections.property("uname"), "uname"))
				      .add( Restrictions.eq("flag", "D" ) )
				    .setResultTransformer(Transformers.aliasToBean(BossUserTemp.class));

				  list = cr.list();
				  return list;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}finally {
			session.flush();
			session.close();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	// Delete external user
	public synchronized String approveDeleteUserReq(String username)
	{
		Session session = null;
		try
		{
		session = this.sessionFactory.openSession();
		
		String hql;
		Query query;
		
		hql = "FROM BossAccount WHERE UNAME = :uname";
		query = session.createQuery(hql);
		query.setParameter("uname", username);
		List<BossAccount> TempAccounts = query.list();
		BossAccount TempAccount = TempAccounts.get(0);
		
		Criteria cr = session.createCriteria(BossUserTemp.class)
			    .setProjection(Projections.projectionList()
			      .add(Projections.property("uname"), "uname"))
			      .add( Restrictions.ne("flag", "D" ) )
			      .add( Restrictions.eq("uname", username ) )
			    .setResultTransformer(Transformers.aliasToBean(BossUserTemp.class));

			  List<BossUserTemp> UserTempList = cr.list();
				int count2 = UserTempList.size();
				System.out.println("Value of Count2 :: " + count2);
		
		Criterion c1 = Restrictions.or(Restrictions.ne("piiflag", "N" ),Restrictions.ne("flag", "N" ) );
		Criterion c2 = Restrictions.eq("uname", username );
		cr = session.createCriteria(BossUser.class)
			.setProjection(Projections.projectionList()
			.add(Projections.property("uname"), "uname"))
			.add(Restrictions.and(c1,c2))
			.setResultTransformer(Transformers.aliasToBean(BossUser.class));

			List<BossUser> UserList = cr.list();
			int count1 = UserList.size();
			System.out.println("Value of Count1 :: " + count1);
		
			c1 = Restrictions.or(Restrictions.ne("revflag", "N" ),Restrictions.ne("payflag", "N" ));
			c2 = Restrictions.eq("username", username );
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
		query.setParameter("uname", username);
		List<BossExtTransactionTemp> TempTransList = query.list();
		int count4 = TempTransList.size();
		System.out.println("Value of Count4 :: " + count4);
		
		c2 = Restrictions.eq("transto", TempAccount.getAccountId() );
		cr = session.createCriteria(BossExtTransaction.class)
				.setProjection(Projections.projectionList()
				.add(Projections.property("transid"), "transid"))
				.add(Restrictions.and(c1,c2))
				.setResultTransformer(Transformers.aliasToBean(BossExtTransaction.class));

				TransList = cr.list();
				int count5 = TransList.size();
				System.out.println("Value of Count5 :: " + count5);
				
				cr = session.createCriteria(BossExtTransactionTemp.class)
						.setProjection(Projections.projectionList()
						.add(Projections.property("transid"), "transid"))
						.add(Restrictions.and(c1,c2))
						.setResultTransformer(Transformers.aliasToBean(BossExtTransactionTemp.class));

						TransList = cr.list();
						int count6 = TransList.size();
						System.out.println("Value of Count6 :: " + count6);
		
		if(count1 > 0 || count2 > 0 || count3 > 0 || count4 >0 || count5 > 0 || count6 > 0)
		{
			hql = "delete BossUserTemp where UNAME = :uname";
			query = session.createQuery(hql);
			query.setParameter("uname", username);
			int result = query.executeUpdate();	
		return "F";
		}
		
		else
		{
		query = session.createQuery("delete BossUser where UNAME = :uname ");
		query.setParameter("uname", username);
		int result = query.executeUpdate();
		
		hql = "delete BossUserTemp where UNAME = :uname";
		query = session.createQuery(hql);
		query.setParameter("uname", username);
		result = query.executeUpdate();	
		return "S";
		}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}finally {
			session.flush();
			session.close();
		}
		return "N";
	}

// PII functions
	
	@Transactional
	// Change status of particular user transactions from N to R
	public synchronized boolean requestViewExtPII(String username)
	{
	Session session = null;	
	try
	{
	//System.out.println(username);
	String hql = "UPDATE BossUser set PIIFLAG = :flag "  + 
	            "WHERE UNAME = :uname";
	session = this.sessionFactory.openSession();
	Query query = session.createQuery(hql);
	query.setParameter("flag", "R");
	query.setParameter("uname", username);
	query.executeUpdate();
	return true;	
	}
	catch(Exception ex){
		ex.printStackTrace();
	}finally {
		session.flush();
		session.close();
	}
	return false;
	}
	
	@Transactional
	// Check if internal user has permission to view external user transactions
	public synchronized String viewExtPIIPermission(String username)
	{
		Session session = null;	
		List<BossUser> userList = new ArrayList<BossUser>();
		try
		{
		String hql = "FROM BossUser WHERE UNAME = :uname and PIIFLAG =:flag" ;
		session = this.sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("flag", "RA");
		query.setParameter("uname", username);
		
		userList = query.list();
		int count = userList.size();
		if(count>0)
		return "S";
		else 
		return "F";
		}
		catch(Exception ex){
			ex.printStackTrace();
		}finally {
			session.flush();
			session.close();
		}
		return "N";
	}

	@SuppressWarnings("unchecked")
	@Transactional
	// Retrieve External User Transactions
	public synchronized BossUser retrieveExtPII(String username)
	{
		Session session = null;
		List <BossUser> userList = new ArrayList<BossUser>();
		BossUser user = new BossUser();
		try
		{
		String hql = "FROM BossUser WHERE UNAME = :uname and PIIFLAG =:flag" ;
		session = this.sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("flag", "RA");
		query.setParameter("uname", username);
		
		 userList = query.list();
		if(userList.size() >0)
		user = userList.get(0);
		return user;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}finally {
			session.flush();
			session.close();
		}
		return user;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	// Revert permissions to view External User Transactions
	public synchronized boolean revertExtPIIPermissions(String username)
	{
		Session session = null;
		try
		{
			String hql = "UPDATE BossUser set PIIFLAG = :flag "  + 
		            "WHERE UNAME = :uname";
		session = this.sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("flag", "N");
		query.setParameter("uname", username);
		//int result = 
		query.executeUpdate();
		//System.out.println("Rows affected: " + result);
		return true;	
		}
		catch(Exception ex){
			ex.printStackTrace();
		}finally {
			session.flush();
			session.close();
		}
		return false;
	}
	
	// Delete Internal User Transactions
	@SuppressWarnings("unchecked")
	@Transactional
	public synchronized List<String> viewInternalUsers()
	{
		Session session = null;	
		List<String> users = new ArrayList <String>();
		try
		{
		session = this.sessionFactory.openSession();
		String hib = "SELECT u.uname from BossUser u, BossRole r WHERE u.uname = r.uname and r.roletype = :roletype";
		org.hibernate.Query que = session.createQuery(hib);
		que.setParameter("roletype", "ROLE_EMPLOYEE");
		users = que.list();
		return users;
		
		}
		catch(Exception ex){
			ex.printStackTrace();
		}finally {
			session.flush();
			session.close();
		}
		return users;
		
	}
	
//Delete Internal User

	@SuppressWarnings("unchecked")
	@Transactional
	// Delete external user
	public synchronized boolean adminDeleteInternalUser(String username)
	{
		Session session = null;
		try
		{
		session = this.sessionFactory.openSession();
		Query query = session.createQuery("delete BossUser where UNAME = :uname ");
		query.setParameter("uname", username);
		query.executeUpdate();
		return true;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}finally {
			session.flush();
			session.close();
		}
		return false;
	}
// Revoke functions 
	
	@SuppressWarnings("unchecked")
	@Transactional
	// Retrieve Revoke Functions Request
	public synchronized List<BossExtTransaction> viewRevokeTransReq()
	{
		Session session = null;
		Masking stringmask = new Masking();
		List<BossExtTransaction> transactions = new ArrayList <BossExtTransaction>();
		BossExtTransaction transactionBank = new BossExtTransaction();
		try
		{
		String hql = "FROM BossExtTransaction WHERE REVFLAG =:flag" ;
		session = this.sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("flag", "RE");
		
		transactions = query.list();
		if(transactions.size() > 0)
		{
		for(int i=0; i < transactions.size();i++)
		{
			String output1 = "";
			String output2 = "";
			transactionBank = transactions.get(i);
			output1 = stringmask.mask(""+transactionBank.getTransfrom());
			transactionBank.setTransfrom(output1);
			output2 = stringmask.mask(""+transactionBank.getTransto());
			transactionBank.setTransto(output2);		
		}
		}
		return transactions;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}finally 
		{
			
		}
		return transactions;
	}
	// Approve Revoke Transaction 
	@SuppressWarnings("unchecked")
	@Transactional
	
	public synchronized String approveRevokeReq(String transid,String username)
	{
		Session session = null;
		BossExtTransaction transaction = new BossExtTransaction();
		try
		{
		session = this.sessionFactory.openSession();
		String hql = "From BossExtTransaction WHERE TRANSID = :transid and UNAME = :uname";
		Query query = session.createQuery(hql);
		query.setParameter("transid", transid );
		query.setParameter("uname", username );
		
		List list = query.list();
		
		if(list.size() <=0)
		{
			return "NT";
		}
		else
		{
			transaction = (BossExtTransaction)list.get(0);
			hql = "FROM BossAccount WHERE ACCTNUM =:accountnum" ;
			query = session.createQuery(hql);
			query.setParameter("accountnum", transaction.getTransto() );
			list = query.list();
			BossAccount toaccount = (BossAccount)list.get(0);
			double tobalance = toaccount.getAccountbal();
			
			hql = "FROM BossAccount WHERE ACCTNUM =:accountnum" ;
			query = session.createQuery(hql);
			query.setParameter("accountnum", transaction.getTransfrom() );
			list = query.list();
			BossAccount fromaccount = (BossAccount)list.get(0);
			double frombalance = fromaccount.getAccountbal();
			
			if(tobalance < transaction.getTransamt())
			{
				hql = "UPDATE BossExtTransaction set REVFLAG = :flag "  + 
			            "WHERE UNAME = :username and TRANSID = :transid";
				query = session.createQuery(hql);
				query.setParameter("flag","REA");
				query.setParameter("username",username);
				query.setParameter("transid",transid);
				query.executeUpdate();
				return "NB";
			}
			
			else
			{
				hql = "UPDATE BossAccount set ACCTBAL =:Balance WHERE UNAME =:username";
				query = session.createQuery(hql);
				query.setParameter("Balance",tobalance-transaction.getTransamt());
				query.setParameter("username",toaccount.getUname());
				query.executeUpdate();
				
				hql = "UPDATE BossAccount set ACCTBAL =:Balance WHERE UNAME =:username";
				query = session.createQuery(hql);
				query.setParameter("Balance",frombalance+transaction.getTransamt());
				query.setParameter("username",fromaccount.getUname());
				query.executeUpdate();
				
				hql = "UPDATE BossExtTransaction set REVFLAG = :flag "  + 
			            "WHERE UNAME = :username and TRANSID = :transid";
				query = session.createQuery(hql);
				query.setParameter("flag","REA");
				query.setParameter("username",username);
				query.setParameter("transid",transid);
				query.executeUpdate();
				
				return "S";
				
			}			
			
		}
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}finally {
			session.flush();
			session.close();
		}
		return "N";
	}
	
	
	// MakePayment functions 
	
		@SuppressWarnings("unchecked")
		@Transactional
		// Retrieve MakePayment Authorization Request
		public synchronized List<BossExtTransactionTemp> viewMakePaymentAuthReq()
		{
			Session session = null;
			Masking stringmask = new Masking();
			List<BossExtTransactionTemp> transactions = new ArrayList <BossExtTransactionTemp>();
			BossExtTransactionTemp transactionBank = new BossExtTransactionTemp();
			try
			{
			String hql = "FROM BossExtTransactionTemp WHERE PAYFLAG =:flag" ;
			session = this.sessionFactory.openSession();
			Query query = session.createQuery(hql);
			query.setParameter("flag", "PT");
			
			transactions = query.list();
			if(transactions.size() > 0)
			{
			for(int i=0; i < transactions.size();i++)
			{
				String output1 = "";
				String output2 = "";
				transactionBank = transactions.get(i);
				output1 = stringmask.mask(""+transactionBank.getTransfrom());
				transactionBank.setTransfrom(output1);
				output2 = stringmask.mask(""+transactionBank.getTransto());
				transactionBank.setTransto(output2);		
			}
			}
			return transactions;
			}
			catch(Exception ex){
				ex.printStackTrace();
			}finally 
			{
				
			}
			return transactions;
		}
		
		// Authorize MakePayment transaction request 
		
		public synchronized String adminAuthMakePaymentApprove(String transid,String username)
		{
			Session session = null;
			BossExtTransactionTemp transaction = new BossExtTransactionTemp();
			BossExtTransaction NewTransaction =new BossExtTransaction();
			try
			{
			session = this.sessionFactory.openSession();
			String hql = "From BossExtTransactionTemp WHERE TRANSID = :transid and UNAME = :uname";
			Query query = session.createQuery(hql);
			query.setParameter("transid", transid );
			query.setParameter("uname", username );
			
			List list = query.list();
			System.out.println("Size of transaction list:: " + list.size());
			if(list.size() <=0)
			{
				return "NT";
			}
			else
			{
				transaction = (BossExtTransactionTemp)list.get(0);
				hql = "FROM BossAccount WHERE ACCTNUM =:accountnum" ;
				query = session.createQuery(hql);
				query.setParameter("accountnum", transaction.getTransto() );
				list = query.list();
				if(list.size() <= 0)
				{
					hql = "delete BossExtTransactionTemp where TRANSID = :transid and UNAME =:uname";
					query = session.createQuery(hql);
					query.setParameter("transid", transid );
					query.setParameter("uname", username );
					query.executeUpdate();
					
					return "NM";
				}
				else
				{
				BossAccount toaccount = (BossAccount)list.get(0);
				double tobalance = toaccount.getAccountbal();
				
				hql = "FROM BossAccount WHERE ACCTNUM =:accountnum" ;
				query = session.createQuery(hql);
				query.setParameter("accountnum", transaction.getTransfrom() );
				list = query.list();
				if(list.size() <= 0)
				{
					hql = "delete BossExtTransactionTemp where TRANSID = :transid and UNAME =:uname";
					query = session.createQuery(hql);
					query.setParameter("transid", transid );
					query.setParameter("uname", username );
					query.executeUpdate();
					
					return "NU";
				}
				else
				{
				BossAccount fromaccount = (BossAccount)list.get(0);
				double frombalance = fromaccount.getAccountbal();
				
				if(frombalance < transaction.getTransamt())
				{
					hql = "delete BossExtTransactionTemp where TRANSID = :transid and UNAME =:uname";
					query = session.createQuery(hql);
					query.setParameter("transid", transid );
					query.setParameter("uname", username );
					query.executeUpdate();
					return "NB";
				}
				
				else
				{
					hql = "UPDATE BossAccount set ACCTBAL =:Balance WHERE UNAME =:username";
					query = session.createQuery(hql);
					query.setParameter("Balance",tobalance+transaction.getTransamt());
					query.setParameter("username",toaccount.getUname());
					query.executeUpdate();
					
					hql = "UPDATE BossAccount set ACCTBAL =:Balance WHERE UNAME =:username";
					query = session.createQuery(hql);
					query.setParameter("Balance",frombalance-transaction.getTransamt());
					query.setParameter("username",fromaccount.getUname());
					query.executeUpdate();
					
					hql = "delete BossExtTransactionTemp where TRANSID = :transid and UNAME =:uname";
					query = session.createQuery(hql);
					query.setParameter("transid", transid );
					query.setParameter("uname", username );
					query.executeUpdate();
					
					session.beginTransaction();
					
					NewTransaction.setTranstype("Payment");
					NewTransaction.setTransamt(transaction.getTransamt());
					NewTransaction.setTransdesc("Credited payment to merchant");
					NewTransaction.setTransstatus("Success");
					NewTransaction.setTransfrom(transaction.getTransfrom());
					NewTransaction.setTransto(transaction.getTransto());
					NewTransaction.setTransdate(new Date());
					NewTransaction.setUsername(username);
					NewTransaction.setFlag("N");
					NewTransaction.setOpuser("N");
					NewTransaction.setRevflag("N");
					NewTransaction.setPayflag("N");
				
					session.save(NewTransaction);
					session.getTransaction().commit();
					
					return "S";
					
				}			
				}
				}
			}
				
			}
			catch(Exception ex){
				ex.printStackTrace();
			}finally {
				session.flush();
				session.close();
			}
			return "N";
		}
		
}

