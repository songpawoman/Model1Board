package org.sp.model1board.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sp.model1board.domain.Board;
import org.sp.model1board.util.PoolManager;

public class BoardDAO {
	PoolManager pool=PoolManager.getInstance();
	
	public List selectAll() {
		
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List list = new ArrayList();
		
		try {
			con=pool.getConnection(); //대여
			
			String sql="select * from board order by board_idx desc";
			pstmt=con.prepareStatement(sql);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				Board board = new Board();
				board.setBoard_idx(rs.getInt("board_idx"));
				board.setTitle(rs.getString("title"));
				board.setWriter(rs.getString("writer"));
				board.setRegdate(rs.getString("regdate"));
				board.setHit(rs.getInt("hit"));
				board.setContent(rs.getString("content"));
				
				list.add(board);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			pool.release(con, pstmt, rs);
		}
		return list;
	}
	
	public int insert(Board board) {
		String url="jdbc:oracle:thin:@localhost:1521:XE";
		String user="jsp";
		String pass="1234";
		
		Connection con=null;
		PreparedStatement pstmt=null;
		int result=0;
		
		try {
			con=pool.getConnection(); //대여
			
			String sql="insert into board(board_idx, title, writer ,content)";
			sql+=" values(seq_board.nextval, ?,?,?)";
			
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getWriter());
			pstmt.setString(3, board.getContent());

			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			pool.release(con, pstmt);
		}
		return result;
	}
}
