package vc06.project.player;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vc06.project.gameServers.UserServer;


/**
 * @author Jiwoo Kim
 * @author Geonhee Cho
 */
@Entity
@Table(name = "player")
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "playerID")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer id;

	@Column(name = "username")
	@NotFound(action = NotFoundAction.IGNORE)
	private String username;

	@Column(name = "password")
	@NotFound(action = NotFoundAction.IGNORE)
	private String password;

	@Column(name = "win")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer win;

	@Column(name = "lost")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer lost;

	@Column(name = "kills")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer kills;
	
	@Column(name = "deaths")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer deaths;

	@Column(name = "ranks")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer ranks;
	
	@Column(name = "IPaddress")
	@NotFound(action = NotFoundAction.IGNORE)
	private String ipaddress; 
	
	@Column(name = "serverID")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer joinedServerID;
	
//    @ManyToOne
//    @JoinColumn(name = "joinedServerID")
//	private UserServer joinedUserServer;
	
	boolean isNew() { return this.id == null; }
	
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }

	public String getUsername() { return this.username; }
	public void setUsername(String username) { this.username = username; }

	public String getPassword() { return this.password; }
	public void setPassword(String password) { this.password = password; }

	public Integer getWin() { return this.win; }
	public void setWin(Integer win) { this.win = win; }

	public Integer getLost() { return this.lost; }
	public void setLost(Integer lost) { this.lost = lost; }
	
	public Integer getKill() { return this.kills; }
	public void setKill(Integer kills) { this.kills = kills; }

	public Integer getDeath() { return this.deaths; }
	public void setDeath(Integer deaths) { this.deaths = deaths; }
	
	public Integer getRank() { return this.ranks; }
	public void setRank(Integer ranks) { this.ranks = ranks; }

	public String getIPaddress() { return this.ipaddress; }
	public void setIPaddress(String ipaddress) { this.ipaddress = ipaddress; }

	public Integer getJoinedServerID() { return this.joinedServerID; }
	public void setJoinedServerID( Integer joinedServerID) { this.joinedServerID = joinedServerID; }


//    public UserServer getJoinedUserServer() {
//        return joinedUserServer;
//    }
//
//    public void setBookCategory(UserServer joinedUserServer) {
//        this.joinedUserServer = joinedUserServer;
//    }
}
