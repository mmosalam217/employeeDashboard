package com.resources.store.Services;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.resources.CustomExceptions.BadCredentialsException;
import com.resources.CustomExceptions.UserNotFoundException;
import com.resources.IOContainer.annotations.Inject;
import com.resources.IOContainer.annotations.Injectable;
import com.resources.auth.AuthenticationSessionDetails;
import com.resources.auth.SimpleAuthCredentials;
import com.resources.store.ConnectionAdapter;
import com.resources.store.models.User;


public class UserService {

	public UserService() {
		// TODO Auto-generated constructor stub
	}
	
	@Inject()
	private ConnectionAdapter adapter;
	
	// Abstract mapping the ResultSet into a User...
	public User getUserFromResultSet(ResultSet rs){
		User user = new User();

		try {
			if(!rs.isBeforeFirst()) {
				return null;
			}else {
				while(rs.next()) {
					user.setId(rs.getString(1));
					user.setFirstName(rs.getString(2));
					user.setLastName(rs.getString(3));
					user.setEmail(rs.getString(4));
					user.setPassword(rs.getString(5));
					user.setRoles(rs.getString(6));


				}	
			}
			
				

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}
	
	public User getUserById(String id) throws UserNotFoundException {
		Connection conn = (Connection) adapter.initConnection();
		User user = null;
		String SQL = "SELECT * FROM user WHERE id = ?";
		
		try {
			PreparedStatement st = (PreparedStatement) conn.prepareStatement(SQL);
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			 user = getUserFromResultSet(rs);
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
		
		if(user != null) {
			return user;
		}else {
			throw new UserNotFoundException("User Not Found!");
		}
	}
	
	public AuthenticationSessionDetails authenticate(SimpleAuthCredentials credentials) throws BadCredentialsException{
		Connection conn =  (Connection) adapter.initConnection();
		String SQL = "SELECT * FROM user WHERE email=? AND password=?";
		AuthenticationSessionDetails authDetails = null;
		try {
			PreparedStatement st = (PreparedStatement) conn.prepareStatement(SQL);
			st.setString(1, credentials.getEmail());
			st.setString(2, credentials.getPassword());
			ResultSet rs = st.executeQuery();
			if(!rs.isBeforeFirst()) {
				throw new BadCredentialsException("BAD_CREDENTIALS!");
			}
			 authDetails = new AuthenticationSessionDetails();
			while(rs.next()) {
				authDetails.setUser_id(rs.getString(1));
				authDetails.setRoles(rs.getString(6));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return authDetails;
	}
	
	public Boolean createUser(User user) {
		Boolean isCreated = false;
		Connection conn = (Connection) adapter.initConnection();
		String SQL = "INSERT INTO user(id, firstName, lastName, email, password, roles) VALUES(?,?,?,?,?,?)";
		try {
			PreparedStatement st = (PreparedStatement) conn.prepareStatement(SQL);
			st.setString(1, user.getId());
			st.setString(2, user.getFirstName());
			st.setString(3, user.getLastName());
			st.setString(4, user.getEmail());
			st.setString(5, user.getPassword());
			st.setString(6, user.getRoles());
			int result = st.executeUpdate();
				if(result > 0) {
					isCreated = true;
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isCreated;
	}

}
