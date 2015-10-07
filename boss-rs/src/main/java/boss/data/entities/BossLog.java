package boss.data.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 

@Entity
@Table(name = "BOSS_LOG")


	public class BossLog {

		@Id
	    @Column(name = "ID", nullable = false)
	    private int id;
	    @Column(name = "TYPE", nullable = false)
	    private String type;
	    @Column(name = "DESCRIPTION", nullable = false)
	    private String	description;
	    @Column(name = "DATE ", nullable = false)
	    private String date;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
	    
		    
		    
	}
    
    
