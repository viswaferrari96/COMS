package vc06.project.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author Jiwoo Kim
 * @author Geonhee Cho
 */
@RestController
class PlayerController {

	/*
	 * COMMAND LIST
	 * 
	 * 	-  /players/allPlayersList
	 * 					ㄴ returns the list of all players
	 * 
	 *  -  /players/searchById/{playerId}
	 *					ㄴ returns the player searched by the ID number
	 *  
	 *  -  /players/searchById/print/{playerId}
	 *					ㄴ prints out the information about the player found by the id
	 *  
	 *  -  /players/searchByUsername/{username}
	 *					ㄴ returns the player searched by the username
	 *  
	 *  -  /players/searchByUsername/print/{username}
	 *					ㄴ prints out the information about the player found by the username
	 *  
	 *  -  /players/create/{username}/{password}
	 *					ㄴ  creates a new user
	 */
	
	
    @Autowired
    PlayerRepository playerRepository;
    
    
    
	@RequestMapping(method = RequestMethod.GET, path = "/players/allPlayersList")
    public List<Player> getAllUsers()
	{
        List<Player> results = playerRepository.findAll();
        return results;
    }
	
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/players/searchById/{playerId}")
    public Player findUserById(@PathVariable("playerId") int id)
	{
        Optional<Player> results = playerRepository.findById(id);
        
        if ( results.isPresent() ) { return results.get(); }
        return null;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/{username}")
    public Player findUserByUsername(@PathVariable("username") String username)
	{
		Player player = null;
		// search the player by name
        List<Player> AllPlayers = getAllUsers();
		for ( int i = 0; i < AllPlayers.size(); i++ )
		{
	    	player = AllPlayers.get(i);
	        if ( player.getUsername().equals(username)) { break; }
	        else { player = null; }
		}
        return player;
    }
	
	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/print/{username}")
    public String findUserByUserameAndPrintout(@PathVariable("username") String username)
	{
    	Player player = findUserByUsername(username);
        String toReturn = "";
		
        if ( player != null )
        {
        	toReturn = "ID : " + player.getId() + " " +
        			   "Username : " + player.getUsername() + " " + 
        			   "Password : " + player.getPassword() + " " +
        			   "Wins : " + player.getWin() + " " + 
        			   "Losts : " + player.getLost() + " " +
        			   "Kills : " + player.getKill() + " " +
        			   "Deaths : " + player.getDeath() + " " +
        			   "Ranks : " + player.getRank() + " " +
        			   "IPaddress : " + player.getIPaddress();
        }
        else
        {
        	 toReturn = "no such player";
        }
        
        return toReturn;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/getId/{username}")
    public Integer findUserByUsernameAndReturnId(@PathVariable("username") String username)
	{
		Player player = findUserByUsername(username);
        if ( player != null ) { return player.getId(); }
        return null;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/getUsername/{username}")
    public String findUserByUsernameAndReturnUsername(@PathVariable("username") String username)
	{
		Player player = findUserByUsername(username);
        if ( player != null ) { return player.getUsername(); }
        return null;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/getPassword/{username}")
    public String findUserByUsernameAndReturnPassword(@PathVariable("username") String username)
	{
		Player player = findUserByUsername(username);
        if ( player != null ) { return player.getPassword(); }
        return null;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/getWin/{username}")
    public Integer findUserByUsernameAndReturnWin(@PathVariable("username") String username)
	{
		Player player = findUserByUsername(username);
        if ( player != null ) { return player.getWin(); }
        return null;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/getLost/{username}")
    public Integer findUserByUsernameAndReturnLost(@PathVariable("username") String username)
	{
		Player player = findUserByUsername(username);
        if ( player != null ) { return player.getLost(); }
        return null;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/getKill/{username}")
    public Integer findUserByUsernameAndReturnKill(@PathVariable("username") String username)
	{
		Player player = findUserByUsername(username);
        if ( player != null ) { return player.getKill(); }
        return null;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/getDeath/{username}")
    public Integer findUserByUsernameAndReturnDeath(@PathVariable("username") String username)
	{
		Player player = findUserByUsername(username);
        if ( player != null ) { return player.getDeath(); }
        return null;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/getRank/{username}")
    public Integer findUserByUsernameAndReturnRank(@PathVariable("username") String username)
	{
		Player player = findUserByUsername(username);
        if ( player != null ) { return player.getRank(); }
        return null;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/getIPaddress/{username}")
    public String findUserByUsernameAndReturnIPaddress(@PathVariable("username") String username)
	{
		Player player = findUserByUsername(username);
        if ( player != null ) { return player.getIPaddress(); }
        return null;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/getJoinedServerID/{username}")
    public Integer findUserByUsernameAndReturnJoinedServerID(@PathVariable("username") String username)
	{
		Player player = findUserByUsername(username);
        if ( player != null ) { return player.getJoinedServerID(); }
        return null;
    }
	
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/setWin/{username}/{win}")
    public Integer findUserByUsernameAndSetWin(@PathVariable("username") String username,
    										 	@PathVariable("win") Integer win)
	{
		Player player = findUserByUsername(username);
        player.setWin(win);
        playerRepository.save(player);
        
        return win;
    }
	
	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/setLost/{username}/{lost}")
    public Integer findUserByUsernameAndSetLost(@PathVariable("username") String username,
    										 	@PathVariable("lost") Integer lost)
	{
		Player player = findUserByUsername(username);
        player.setLost(lost);
        playerRepository.save(player);
        
        return lost;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/setKill/{username}/{kills}")
    public Integer findUserByUsernameAndSetKill(@PathVariable("username") String username,
    										 	@PathVariable("kills") Integer kills)
	{
		Player player = findUserByUsername(username);
        player.setKill(kills);
        playerRepository.save(player);
        
        return kills;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/setDeath/{username}/{deaths}")
    public Integer findUserByUsernameAndSetDeath(@PathVariable("username") String username,
    										 	@PathVariable("deaths") Integer deaths)
	{
		Player player = findUserByUsername(username);
        player.setDeath(deaths);
        playerRepository.save(player);
        
        return deaths;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/setRank/{username}/{ranks}")
    public Integer findUserByUsernameAndSetRank(@PathVariable("username") String username,
    										 	@PathVariable("ranks") Integer ranks)
	{
		Player player = findUserByUsername(username);
        player.setRank(ranks);
        playerRepository.save(player);
        
        return ranks;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/setIPaddress/{username}/{IPaddress}")
    public String findUserByUsernameAndSetIPaddress(@PathVariable("username") String username,
    										 		 @PathVariable("IPaddress") String IPaddress)
	{
		Player player = findUserByUsername(username);
        player.setIPaddress(IPaddress);
        playerRepository.save(player);
        
        return IPaddress;
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/searchByUsername/setJoinedServerID/{username}/{serverID}")
    public Integer findUserByUsernameAndSetJoinedServerID(@PathVariable("username") String username,
    										 		 @PathVariable("serverID") Integer serverID)
	{
		Player player = findUserByUsername(username);
        player.setJoinedServerID(serverID);
        playerRepository.save(player);
        
        return serverID;
    }
	
	
	
	@RequestMapping(method = RequestMethod.GET, path = "/players/create/{username}/{password}")
    public String saveUser(@PathVariable("username") String username,
    					   @PathVariable("password") String password)
	{
		Player player = new Player();
        player.setUsername(username);
        player.setPassword(password);
        player.setWin(0);
        player.setLost(0);
        player.setKill(0);
        player.setDeath(0);
        player.setRank(0);
        player.setIPaddress("notConnected");
        player.setJoinedServerID(-1);
        playerRepository.save(player);
        return username + " created";
    }
	

	@RequestMapping(method = RequestMethod.GET, path = "/players/deleteById/{playerID}")
    public String deleteUserByID(@PathVariable("playerID") int Id)
	{
		Player player = findUserById(Id);
		if ( player != null )
		{
	        playerRepository.delete(player);
	        return "ID: " + Id + ", username :" + player.getUsername() + " deleted";
		}
		return "ID [ " + Id + " ] does not exists in the database";
    }
	@RequestMapping(method = RequestMethod.GET, path = "/players/deleteByUsername/{username}")
    public String deleteUserByUsername(@PathVariable("username") String username)
	{
		Player player = findUserByUsername(username);
		if ( player != null )
		{
	        playerRepository.delete(player);
	        return username + " deleted";
		}
		return "Username + [ " + username + " ] does not exists in the database";
    }

	@RequestMapping(method = RequestMethod.GET, path = "/players/deleteAllUser")
    public String deleteAllusers()
	{
        List<Player> AllPlayers = getAllUsers();
		for ( int i = 0; i < AllPlayers.size(); i++ )
		{
	        playerRepository.delete(AllPlayers.get(i));
		}
		return "All Users Deleted";
    }
}
