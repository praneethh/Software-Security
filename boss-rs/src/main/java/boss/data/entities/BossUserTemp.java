package boss.data.entities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 

@Entity
@Table(name = "BOSS_USER_TEMP")

public class BossUserTemp {

	@Id
    @Column(name = "USERID", nullable = false)
    private long userid;
	@Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "UNAME",nullable = false)
    private String uname;
    @Column(name = "FNAME",nullable = false)
    private String fname;
    @Column(name = "LNAME",nullable = false)
    private String lname;
    @Column(name = "SSN")
    private String ssn; 
    @Column(name = "ADDRESS",nullable = false)
    private String address;
    @Column(name = "PHONENO",nullable = false)
    private long phoneno;
    @Column(name = "EMAIL", nullable=false)
    private String email;
    @Column(name = "STATUS", nullable=false)
    private String status;
    @Column(name = "FLAG", nullable=false)
    private String flag;
    @Column(name = "PIIFLAG", nullable=false)
    private String piiflag;
    
    
    

	public String getPiiflag() {
		return piiflag;
	}

	public void setPiiflag(String piiflag) {
		this.piiflag = piiflag;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getStatus() {
		return status;
	}
    
	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "ENABLED", nullable=false)
    private String enabled;
    
    
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getPhoneno() {
		return phoneno;
	}
	public void setPhoneno(long phoneno) {
		this.phoneno = phoneno;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
}
