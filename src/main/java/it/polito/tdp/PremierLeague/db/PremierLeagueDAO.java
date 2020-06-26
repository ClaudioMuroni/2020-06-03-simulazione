package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.PossibileArco;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<Integer, Player> getGiocatoriByMedia(double media) {
		
		String sql = "SELECT a.PlayerID, p.Name, AVG(a.Goals) AS media " + 
				"FROM actions a, players p " + 
				"WHERE a.PlayerID=p.PlayerID " + 
				"GROUP BY a.PlayerID " + 
				"HAVING media > ?";
		
		Map<Integer, Player> result = new TreeMap<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, media);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.put(player.getPlayerID() ,player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<PossibileArco> getAllPossibiliArchi() {
		
		String sql = "SELECT a1.PlayerID AS p1 , a2.PlayerID  AS p2, SUM(a1.TimePlayed - a2.TimePlayed) AS delta " + 
				"FROM actions a1, actions a2 " + 
				"WHERE a1.MatchID=a2.MatchID AND a1.PlayerID<>a2.PlayerID AND a1.`Starts`=1 AND a2.`Starts`=1 AND a1.TeamID<>a2.TeamID " + 
				"GROUP BY a1.PlayerID, a2.PlayerID " + 
				"HAVING delta>0";
		
		List<PossibileArco> result = new ArrayList<PossibileArco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				PossibileArco arco = new PossibileArco(res.getInt(1), res.getInt(2), res.getInt(3));
				
				result.add(arco);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
