package boss.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BOSS_USERLOGIN")

public class BossUserLogin {
	@Id
	@Column(name = "USER_ID",nullable = false)
    private long userId;
    @Column(name = "USER_NAME",nullable = false)
    private String userName;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "PASSWORD_SALT")
    private String passwordSalt;
	@Column(name="OTP_EXPIRATION")
    private Date otpExpiration;
    
    public Date getOtpExpiration() {
		return otpExpiration;
	}
	public void setOtpExpiration(Date otpExpiration) {
		this.otpExpiration = otpExpiration;
	}
	public Date getPasswordExpiration() {
		return passwordExpiration;
	}
	public void setPasswordExpiration(Date passwordExpiration) {
		this.passwordExpiration = passwordExpiration;
	}
	@Column(name="PASSWORD_EXPIRATION")
    private Date passwordExpiration;
    public String getPasswordSalt() {
		return passwordSalt;
	}
	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}
	@Column(name= "OTP")
    private long otp;
    @Column(name= "OTP_FLAG")
    private String otpFlag;
    @Column(name= "LOGIN_ATTEMPTS")
    private int loginAttempts;
    
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getOtpFlag() {
		return otpFlag;
	}
	public void setOtpFlag(String otpFlag) {
		this.otpFlag = otpFlag;
	}
	public int getLoginAttempts() {
		return loginAttempts;
	}
	public void setLoginAttempts(int loginAttempts) {
		this.loginAttempts = loginAttempts;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getOtp() {
		return otp;
	}
	public void setOtp(long otp) {
		this.otp = otp;
	}

 
}
