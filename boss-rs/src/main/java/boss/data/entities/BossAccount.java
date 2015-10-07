package boss.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 

@Entity
@Table(name = "BOSS_ACCOUNT")

public class BossAccount {

	@Id
    @Column(name = "ACCTNUM", nullable = false)
    private String accountId;

    @Column(name = "ACCTBAL", nullable = false)
    private double accountbal;
    public double getAccountbal() {
		return accountbal;
	}
	public void setAccountbal(double accountbal) {
		this.accountbal = accountbal;
	}    
    public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Date getCreateddate() {
		return createddate;
	}
	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}
	@Column(name = "ACCTCREATEDDATE")
    private Date createddate;
   

	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	@Column(name = "UNAME")
    private String uname;

	
    
    
}
