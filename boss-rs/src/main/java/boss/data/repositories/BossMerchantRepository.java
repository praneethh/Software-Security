package boss.data.repositories;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import boss.data.entities.BossAccount;
import boss.data.entities.BossExtTransactionTemp;

@Repository
public class BossMerchantRepository {
	
	@Value("test")
	protected String tableSchema;

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public synchronized List<BossExtTransactionTemp> viewRequests(String username)
	{
		List<BossExtTransactionTemp> account = new ArrayList<BossExtTransactionTemp>();
		
		Session session=null;
		try
		{
		session = this.sessionFactory.openSession();
				
		String hib = "FROM BossAccount WHERE UNAME = :username";

		org.hibernate.Query que = session.createQuery(hib);
		
		que.setParameter("username", username);
			
		List<BossAccount> results = que.list();
		
		BossAccount bu = results.get(0);
		
		String dat=bu.getAccountId();
		
		String dats=dat;
		
		String hibs = "FROM BossExtTransactionTemp WHERE TRANSTO=:user AND PAYFLAG=:flag";

		org.hibernate.Query ques = session.createQuery(hibs);
		
		ques.setParameter("user", dats);
		
		ques.setParameter("flag", "PI");
		
		List li = ques.list();
				
		
		for(int i=0;i<li.size();i++)
		{
			BossExtTransactionTemp accountObj = (BossExtTransactionTemp)li.get(i);
			
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
	public synchronized String transferRequests(long tranid)
	{
			
		Session session=null;
		try
		{
		session = this.sessionFactory.openSession();
				
		String hib = "UPDATE BossExtTransactionTemp set PAYFLAG=:pay WHERE TRANSID = :username";

		org.hibernate.Query que = session.createQuery(hib);
		
		que.setParameter("username", tranid);
		
		que.setParameter("pay", "PT");
			
		int t=que.executeUpdate();
		
		if(t==1)
		{
		return "true";
		}
		else
		{
		return "false";	
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
}
