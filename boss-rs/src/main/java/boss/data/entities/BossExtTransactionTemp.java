package boss.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "BOSS_EXT_TRANSACTION_TEMP")
public class BossExtTransactionTemp {
	@Id
    @Column(name = "TRANSID", nullable = false)
    private long transid;
    @Column(name = "TRANSTYPE", nullable = false)
    private String transtype;
    @Column(name = "TRANSAMT ", nullable = false)
    private double transamt ;
    @Column(name = "TRANSDESC ", nullable = false)
    private String transdesc;
    @Column(name = "TRANSSTATUS ", nullable = false)
    private String transstatus ;
   	@Column(name = "TRANSFROM ", nullable = false)
    private String transfrom ;
    @Column(name = "TRANSTO ", nullable = false)
    private String transto ;
    @Column(name = "TRANSDATE ", nullable = false)
    private Date transdate ;
    @Column(name = "UNAME ", nullable = false)
    private String username ;
    @Column(name = "FLAG", nullable = false)
    private String flag ;
    @Column(name = "PAYFLAG", nullable = false)
    private String payflag ; 
    @Column(name = "REVFLAG", nullable = false)
    private String revflag ;
    @Column(name = "OPUSER",nullable=true)
    private String opuser;
    
    public String getOpuser() {
		return opuser;
	}
	public void setOpuser(String opuser) {
		this.opuser = opuser;
	}
    
    public String getRevflag() {
		return revflag;
	}
	public void setRevflag(String revflag) {
		this.revflag = revflag;
	}
    
    public String getPayflag() {
		return payflag;
	}
	public void setPayflag(String payflag) {
		this.payflag = payflag;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getTransid() {
		return transid;
	}
	public void setTransid(long transid) {
		this.transid = transid;
	}
	public String getTranstype() {
		return transtype;
	}
	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}
	public double getTransamt() {
		return transamt;
	}
	public void setTransamt(double transamt) {
		this.transamt = transamt;
	}
	public String getTransdesc() {
		return transdesc;
	}
	public void setTransdesc(String transdesc) {
		this.transdesc = transdesc;
	}
	public String getTransstatus() {
		return transstatus;
	}
	public void setTransstatus(String transstatus) {
		this.transstatus = transstatus;
	}
	public String getTransfrom() {
		return transfrom;
	}
	public void setTransfrom(String accid) {
		this.transfrom = accid;
	}
	public String getTransto() {
		return transto;
	}
	public void setTransto(String transto) {
		this.transto = transto;
	}
	public Date getTransdate() {
		return transdate;
	}
	public void setTransdate(Date date) {
		this.transdate = date;
	}


}
