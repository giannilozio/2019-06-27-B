package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.crimes.model.Event;
import it.polito.tdp.crimes.model.PD;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<String> getCategory() {
		String sql = "SELECT DISTINCT e.offense_category_id as category " + 
					 "FROM EVENTS e" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					
					list.add(res.getString("category"));
					
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Month> getMonth() {
		String sql = "SELECT DISTINCT MONTH (e.reported_date) AS mese " + 
					 "FROM EVENTS e " + 
					 "ORDER BY mese ASC" ;
	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		
		List<Month> list = new ArrayList<>() ;
		
		ResultSet res = st.executeQuery() ;
		
		while(res.next()) {
			try {
				
				list.add(Month.of(res.getInt("mese")));
				
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
		conn.close();
		return list ;

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null ;
	}
	}

	public List<String> getVertici(Month m, String s) {
		String sql = "SELECT DISTINCT e.offense_type_id AS category " + 
					 "FROM EVENTS e " + 
					 "WHERE e.offense_category_id = ? " + 
					 "AND MONTH(e.reported_date) = ?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;

			List<String> list = new ArrayList<>() ;
			
			st.setInt(2,m.getValue());
			st.setString(1, s);
			
			ResultSet res = st.executeQuery() ;

			while(res.next()) {
				try {

					list.add(res.getString("category") );

				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}

	public List<PD> getArchi(Month m, String s) {
		String sql = "SELECT DISTINCT e1.offense_type_id AS v1, e2.offense_type_id AS v2, COUNT(distinct e1.neighborhood_id) AS peso " + 
					 "FROM EVENTS e1, EVENTS e2 " + 
					 "WHERE e1.offense_category_id = ? " + 
					 "AND e2.offense_category_id=e1.offense_category_id " + 
					 "AND MONTH(e1.reported_date) = ? " + 
					 "AND MONTH(e1.reported_date)=MONTH(e2.reported_date) " + 
					 "AND e1.neighborhood_id = e2.neighborhood_id " + 
					 "AND e1.incident_id <> e2.incident_id " + 
					 "AND e1.offense_type_id <> e2.offense_type_id " + 
					 "GROUP BY e1.offense_type_id, e2.offense_type_id" ;
	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;

		List<PD> list = new ArrayList<>() ;
		
		st.setInt(2,m.getValue());
		st.setString(1, s);
		
		ResultSet res = st.executeQuery() ;

		while(res.next()) {
			try {

				PD p = new PD (res.getString("v1"),res.getString("v2"),res.getDouble("peso"));
				list.add(p);

			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		conn.close();
		return list ;

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null ;
	}
	}

}
