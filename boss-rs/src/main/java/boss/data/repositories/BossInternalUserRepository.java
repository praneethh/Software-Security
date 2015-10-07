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

@Repository
public class BossInternalUserRepository 
{

	@Value("test")
	protected String tableSchema;

	@Autowired
	private SessionFactory sessionFactory;
	
	public BossInternalUserRepository() 
	{
		
	}
	//View External Users
	@SuppressWarnings("unchecked")
	@Transactional
	public synchronized List<String> viewExternalUsers()
	{
		Session session = null;	
		List<String> results = new ArrayList<String>();
		List<String> results1 = new ArrayList<String>();
		List<String> results2 = new ArrayList<String>();
		try
		{
		session = this.sessionFactory.openSession();
		String hib = "SELECT u.uname from BossUser u, BossRole r WHERE u.uname = r.uname and r.roletype = :roletype";
		org.hibernate.Query que = session.createQuery(hib);
		que.setParameter("roletype", "ROLE_USER");
		results1 = que.list();
		hib = "SELECT u.uname from BossUser u, BossRole r WHERE u.uname = r.uname and r.roletype = :roletype";
		que = session.createQuery(hib);
		que.setParameter("roletype", "ROLE_MERCHANT");
		results2 = que.list();
		results.addAll(results1);
		results.addAll(results2);
		return results;		
		}
		catch(Exception ex){
			ex.printStackTrace();
		}finally {
			session.flush();
			session.close();
		}
		return results;

	}

	@Transactional
	// Change value of flag of particular external user transactions from N to R
	public synchronized String requestViewExtTransactions(String username, String loginuser)
	{
	Session session = null;	
	try
	{
	session = this.sessionFactory.openSession();
	String hql = "from BossExtTransaction where FLAG = :flag  and UNAME = :uname";
	Query query = session.createQuery(hql);
	query.setParameter("flag", "R");
	query.setParameter("uname", username);
	List results = query.list();
	int c1 = results.size();
	
	hql = "from BossExtTransaction where FLAG = :flag  and UNAME = :uname";
	query = session.createQuery(hql);
	query.setParameter("flag", "RA");
	query.setParameter("uname", username);
	results = query.list();
	int c2 = results.size();
	
	if(c1 == 0 && c2 == 0)
	{
	hql = "UPDATE BossExtTransaction set FLAG = :flag, OPUSER =:opuser " +
	            "WHERE UNAME = :uname";
	
	query = session.createQuery(hql);
	query.setParameter("flag", "R");
	query.setParameter("opuser",loginuser);
	query.setParameter("uname", username);
	int count = query.executeUpdate();
	if(count == 0)
	return "N";
	
	else
	return "S";	
	}
	else
	return "O";
	}
	catch(Exception ex){
		ex.printStackTrace();
	}finally {
		session.flush();
		session.close();
	}
	return "E";
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	// Check if internal user has permission to view external user transactions
	public synchronized String viewExtTransactionsPermission(String username, String loginuser)
	{
		Session session = null;	
		try
		{
		String hql = "FROM BossExtTransaction WHERE UNAME = :uname";
		session = this.sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("uname", username);
		List<BossExtTransaction> transList = query.list();
		int count = transList.size();
		System.out.println("Count of total transactions" + username+ "::" + count);
		if(count >0)
		{
		hql = "FROM BossExtTransaction WHERE UNAME = :uname and FLAG =:flag and OPUSER =:loginuser" ;
		session = this.sessionFactory.openSession();
		query = session.createQuery(hql);
		query.setParameter("flag", "RA");
		query.setParameter("uname", username);
		query.setParameter("loginuser", loginuser);
		transList = query.list();
		count = transList.size();
		if(count>0)
		return "S";
		
		else 
		return "F";
		}
		else
		return "N";
		}
		catch(Exception ex){
			ex.printStackTrace();
		}finally {
			session.flush();
			session.close();
		}
		return "E";
	}

	@SuppressWarnings("unchecked")
	@Transactional
	// Retrieve External User Transactions
	public synchronized List<BossExtTransaction> retrieveExtTransactions(String username, String loginuser)
	{
		Session session = null;
		Masking stringmask = new Masking();
		List<BossExtTransaction> transList = new ArrayList<BossExtTransaction>();
		BossExtTransaction transactionBank = new BossExtTransaction();
		try
		{
		String hql = "FROM BossExtTransaction WHERE UNAME = :uname and FLAG =:flag and OPUSER = :loginuser" ;
		session = this.sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("flag", "RA");
		query.setParameter("uname", username);
		query.setParameter("loginuser", loginuser);
		
		transList = query.list(); 
		
		for(int i=0; i < transList.size();i++)
		{
			String output1 = "";
			String output2 = "";
			transactionBank = transList.get(i);
			output1 = stringmask.mask(""+transactionBank.getTransfrom());
			transactionBank.setTransfrom(output1);
			output2 = stringmask.mask(""+transactionBank.getTransto());
			transactionBank.setTransto(output2);		
		}
		
		return transList;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}finally 
		{
		
		}
		return transList;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	// Revert permissions to view External User Transactions
	public synchronized boolean revertExtTransactionPermissions(String username,String loginuser)
	{
		Session session = null;
		try
		{
			String hql = "UPDATE BossExtTransaction set FLAG = :flag "  + 
		            "WHERE UNAME = :uname and OPUSER = :opuser";
		session = this.sessionFactory.openSession();
		Query query = session.createQuery(hql);
		query.setParameter("flag", "N");
		query.setParameter("uname", username);
		query.setParameter("opuser", loginuser);
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
	@SuppressWarnings("unchecked")
	@Transactional
	// View External Users Profile Request
	public synchronized List<BossUserTemp> viewExtUserProfileReq()
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
				      .add( Restrictions.eq("flag", "R" ) )
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

	//Approve change profile request

	@SuppressWarnings("unchecked")
	@Transactional
	// Update External User Profile
	public synchronized boolean approveExtPofileReq(String username, String fname, String lname, String address, long phoneno, String email)
	{
		Session session = null;
		try
		{
		session = this.sessionFactory.openSession();
		session.beginTransaction();
		Query query = session.createQuery("from BossUser where UNAME = :uname ");
		query.setParameter("uname", username);
		   BossUser user = (BossUser)query.list().get(0);
		   user.setFname(fname);
		   user.setLname(lname);
		   user.setAddress(address);
		   user.setEmail(email);
		   user.setPhoneno(phoneno);
		   session.save(user);
		   session.getTransaction().commit();
		
		String hql = "delete BossUserTemp where UNAME = :uname";
		query = session.createQuery(hql);
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

}
