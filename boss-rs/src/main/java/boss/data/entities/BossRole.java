package boss.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BOSS_ROLE")
public class BossRole 
{
	@Id
    @Column(name = "USER_ID", nullable = false)
    private long userId;
    @Column(name = "USER_NAME", nullable = false)
    private String uname;
    @Column(name = "ROLETYPE ", nullable = false)
    private String roletype;
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getRoletype() {
		return roletype;
	}
	public void setRoletype(String roletype) {
		this.roletype = roletype;
	}
}
