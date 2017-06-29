import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO {
	
	Connection connection;
	
	public static UserDAO getInstance()
	{
		return new UserDAO();
	}
	
	public UserDAO()
	{
		connection=ConnectionUtil.getConnection();
	}
	
	public int saveUser(User user)
	{
		int count = 0;
		String sql="{call sp_user_insert(?,?,?,?)}";
		
		try {
			CallableStatement statement=connection.prepareCall(sql);
			statement.setString(1, user.getName());
			statement.setLong(2, user.getMobile());
			statement.setString(3, user.getEmail());
			statement.registerOutParameter(4, java.sql.Types.INTEGER);
			statement.executeUpdate();
			count=statement.getInt(4);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	public int updateUser(User user)
	{
		int count=0;
		String sql="{call sp_user_update(?,?,?,?,?)}";
		try {
			CallableStatement statement=connection.prepareCall(sql);
			statement.setInt(1, user.getId());
			statement.setString(2, user.getName());
			statement.setLong(3, user.getMobile());
			statement.setString(4, user.getEmail());
			statement.registerOutParameter(5, java.sql.Types.INTEGER);
			count=statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	public int deleteUser(int id)
	{
		int count=0;
		String sql="{call sp_user_delete(?,?)}";
		try {
			CallableStatement statement=connection.prepareCall(sql);
			statement.setInt(1, id);
			statement.registerOutParameter(2, java.sql.Types.INTEGER);
			count=statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
	public ArrayList<User> allUsers()
	{
		ArrayList<User> users=new ArrayList<>();
		String sql="{call sp_user_select()}";
		try {
			PreparedStatement statement=connection.prepareStatement(sql);
			ResultSet resultSet=statement.executeQuery();
			if(resultSet.first())
			{
				do
				{
					User user=new User();
					user.setId(resultSet.getInt("id"));
					user.setName(resultSet.getString("username"));
					user.setMobile(resultSet.getLong("mobile"));
					user.setEmail(resultSet.getString("email"));
					users.add(user);
				}while(resultSet.next());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}
}
