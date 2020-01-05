package com.resources.store.Services;

import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.resources.store.ConnectionAdapter;
import com.resources.store.models.SiteHit;

public class HitsService {

	
	public HitsService() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Boolean addHit(SiteHit hit) {
		Boolean isAdded = false;
		 ConnectionAdapter adapter = new ConnectionAdapter();
		Connection conn = (Connection) adapter.initConnection();
		String SQL = "insert into sitehits (id, user_id, country, region, language) VALUES(?,?,?,?,?)";
		try {
			PreparedStatement st = (PreparedStatement) conn.prepareStatement(SQL);
			st.setString(1, hit.getId());
			st.setString(2, hit.getUser());
			st.setString(3, hit.getCountry());
			st.setString(4, hit.getRegion());
			st.setString(5, hit.getLanguage());
			int result = st.executeUpdate();
			if(result > 0) {
				isAdded = true;
			}
			st.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		return isAdded;
	}

}
