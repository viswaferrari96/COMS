package vc06.project.gameServers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vc06.project.player.Player;

/**
 * @author Jiwoo Kim
 */
@RestController
public class UserServerController
{
    @Autowired
    UserServerRepository userServerRepository;

    
    
   
	@RequestMapping(method = RequestMethod.GET, path = "/userServers/allServersList")
    public List<UserServer> getAllUserServers()
	{
        List<UserServer> results = userServerRepository.findAll();
        return results;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/userServers/allServersListAndPrint")
    public String getAllUserServersAndPrint()
	{
		List<UserServer> results = userServerRepository.findAll();
        String toReturn = "";
		int size = results.size();
        for ( int i = 0; i < size; i ++ )
        {
        	UserServer target = results.get(i);
        	toReturn += target.getServername() + " " + target.getId() + " " + target.getPlayers().size() + " ";
        }
        return toReturn;
    }
	@RequestMapping(method = RequestMethod.GET, path = "/userServers/searchById/{serverID}")
    public UserServer findUserServerById(@PathVariable("serverID") int id)
	{
        Optional<UserServer> results = userServerRepository.findById(id);
        
        if ( results.isPresent() ) { return results.get(); }
        return null;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/userServers/searchById/getServername/{serverID}")
    public String findServernameById(@PathVariable("serverID") int id)
	{
        Optional<UserServer> results = userServerRepository.findById(id);
        
        if ( results.isPresent() ) { return results.get().getServername(); }
        return null;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/userServers/searchByServername/{servername}")
    public UserServer findUserServerByServername(@PathVariable("servername") String servername)
	{
		UserServer userServer = null;
		// search the userServer by name
        List<UserServer> AllUserServer = getAllUserServers();
		for ( int i = 0; i < AllUserServer.size(); i++ )
		{
			userServer = AllUserServer.get(i);
	        if ( userServer.getServername().equals(servername)) { break; }
	        else { userServer = null; }
		}
        return userServer;
    }
	
	@RequestMapping(method = RequestMethod.GET, path = "/userServers/searchByServername/print/{servername}")
    public String findUserByServernameAndPrintout(@PathVariable("servername") String servername)
	{
    	UserServer userServer = findUserServerByServername(servername);
        String toReturn = "";
		
        if ( userServer != null )
        {
        	toReturn = "ServerID : " + userServer.getId() + " " +
        			   "ServerName : " + userServer.getServername() + " " + 
        			   "HostIPaddress : " + userServer.getHostIPaddress();
        }
        else
        {
        	 toReturn = "no such userServer";
        }
        
        return toReturn;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/userServers/searchByServername/getId/{servername}")
    public Integer findUserServerByServernameAndReturnId(@PathVariable("servername") String servername)
	{
		UserServer userServer = findUserServerByServername(servername);
        if ( userServer != null ) { return userServer.getId(); }
        return null;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/userServers/searchByServername/getServername/{servername}")
    public String findUserServerByServernameAndReturnServername(@PathVariable("servername") String servername)
	{
		UserServer userServer = findUserServerByServername(servername);
        if ( userServer != null ) { return userServer.getServername(); }
        return null;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/userServers/searchByServername/getHostIPaddress/{servername}")
    public String findUserServerByServernameAndReturnHostIPaddress(@PathVariable("servername") String servername)
	{
		UserServer userServer = findUserServerByServername(servername);
        if ( userServer != null ) { return userServer.getHostIPaddress(); }
        return null;
    }
	
	@RequestMapping(method = RequestMethod.GET, path = "/userServers/searchByServername/getHostname/{servername}")
    public String findUserServerByServernameAndReturnHostname(@PathVariable("servername") String servername)
	{
		UserServer userServer = findUserServerByServername(servername);
        if ( userServer != null ) { return userServer.getHostname(); }
        return null;
    }
	
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/userServers/searchByServername/setHostIPaddress/{servername}/{IPaddress}")
    public String findUserServerByServernameAndSetIPaddress(@PathVariable("servername") String servername,
    										 		 @PathVariable("IPaddress") String IPaddress)
	{
		UserServer userServer = findUserServerByServername(servername);
        userServer.setHostIPaddress(IPaddress);
        userServerRepository.save(userServer);
        
        return IPaddress;
    }
	
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/userServers/create/{servername}/{IPaddress}/{hostname}")
    public String saveUserServer(@PathVariable("servername") String servername,
    					   		 @PathVariable("IPaddress") String IPaddress,
    					   		 @PathVariable("hostname") String hostname )
	{
		UserServer userServer = new UserServer();
        userServer.setServername(servername);
        userServer.setHostIPaddress(IPaddress);
        userServer.setHostname(hostname);
        userServerRepository.save(userServer);
        return servername + " created";
    }

	
	
	@RequestMapping(method = RequestMethod.GET, path = "/userServers/searchByServername/getAllUsers/{servername}")
    public List<Player> findUserServerByServernameAndReturnAllUsers(@PathVariable("servername") String servername)
	{
		UserServer userServer = findUserServerByServername(servername);
        if ( userServer != null )
        {
        	return userServer.getPlayers();
        }
        else
        {
            return new ArrayList<>();
        }
    }
	
	@RequestMapping(method = RequestMethod.GET, path = "/userServers/searchByServername/getAllUsers/print/{servername}")
    public String findUserServerByServernameAndPrintAllUsers(@PathVariable("servername") String servername)
	{
		UserServer userServer = findUserServerByServername(servername);
		List<Player> playerList = new ArrayList<>();
		String toReturn = "";
		
        if ( userServer != null )
        {
        	playerList = userServer.getPlayers();
        }
        
        for ( int i = 0; i < playerList.size(); i++ )
        {
        	toReturn += playerList.get(i).getUsername() + " ";
        }
        
        return toReturn;
    }
	
	@RequestMapping(method = RequestMethod.GET, path = "/userServers/deleteById/{serverID}")
    public String deleteUserServerByID(@PathVariable("serverID") int Id)
	{
		UserServer userServer = findUserServerById(Id);
		if ( userServer != null )
		{
			List<Player> playerList = findUserServerByServernameAndReturnAllUsers(findServernameById(Id));
			
			for ( int i = 0; i < playerList.size(); i++ )
			{
				playerList.get(i).setJoinedServerID(-1);
			}
			
	        userServerRepository.delete(userServer);
	        return "serverID: " + Id + ", servername :" + userServer.getServername() + " deleted";
		}
		return "serverID [ " + Id + " ] does not exists on the database";
    }
	
	@RequestMapping(method = RequestMethod.GET, path = "/userServers/deleteByServername/{servername}")
    public String deleteUserServerByServername(@PathVariable("servername") String servername)
	{
		UserServer userServer = findUserServerByServername(servername);
		if ( userServer != null )
		{
	        userServerRepository.delete(userServer);
	        return servername + " deleted";
		}
		return "Username + [ " + servername + " ] does not exists in the database";
    }

	@RequestMapping(method = RequestMethod.GET, path = "/userServers/deleteAllUserServers")
    public String deleteAllUserServers()
	{
        List<UserServer> AllUserServers = getAllUserServers();
		for ( int i = 0; i < AllUserServers.size(); i++ )
		{
			List<Player> playerList = findUserServerByServernameAndReturnAllUsers(findServernameById(AllUserServers.get(i).getId()));
			
			for ( int j = 0; j < playerList.size(); j++ )
			{
				playerList.get(j).setJoinedServerID(-1);
			}
			
	        userServerRepository.delete(AllUserServers.get(i));
		}
		return "All Servers Deleted";
    }
}
