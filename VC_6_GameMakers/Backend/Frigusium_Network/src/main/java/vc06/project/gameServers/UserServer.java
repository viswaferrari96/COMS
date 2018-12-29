package vc06.project.gameServers;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vc06.project.player.Player;

@Entity
@Table(name = "servers")
public class UserServer
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "serverID")
	@NotFound(action = NotFoundAction.IGNORE)
	private Integer serverid;

	@Column(name = "servername")
	@NotFound(action = NotFoundAction.IGNORE)
	private String servername;

	@Column(name = "hostname")
	@NotFound(action = NotFoundAction.IGNORE)
	private String hostname;
	
	@Column(name = "IPaddress")
	@NotFound(action = NotFoundAction.IGNORE)
	private String hostIPaddress;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    @JoinColumn(name = "serverID")
	private List<Player> players;	
	
	boolean isNew() { return this.serverid == null; }
	
	public Integer getId() { return serverid; }
	public void setId(Integer serverid) { this.serverid = serverid; }

	public String getServername() { return this.servername; }
	public void setServername(String servername) { this.servername = servername; }
	
	public String getHostname() { return this.hostname; }
	public void setHostname(String hostname) { this.hostname = hostname; }

	public String getHostIPaddress() { return this.hostIPaddress; }
	public void setHostIPaddress(String hostIPaddress) { this.hostIPaddress = hostIPaddress; }
	
    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}