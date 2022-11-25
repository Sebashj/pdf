package Dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Conexion.Conexion;
import Modelo.Categoria;

public class DaoCategoria {
	Conexion cx =null;
	
	public DaoCategoria() {
		cx=new Conexion();
	}
	
	public boolean insertarcategoria(Categoria user) {
		PreparedStatement ps=null;
		try {
			ps=cx.conectar().prepareStatement("INSERT INTO categoria VALUES(null,?)");
			ps.setString(1, user.getCategoria());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		    
	}
	public ArrayList<Categoria> fetchcategorias(){
		ArrayList<Categoria> lista=new ArrayList<Categoria>();
		PreparedStatement ps=null;
		ResultSet rs =null;
		try {
			ps=cx.conectar().prepareStatement("SELECT * FROM categoria");
			rs=ps.executeQuery();
			while(rs.next()) {
				Categoria u=new Categoria();
				u.setIdCategoria(rs.getInt("idcategoria"));
				u.setCategoria(rs.getString("categoria"));
				
				lista.add(u);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return lista;
		
	}
	public boolean eliminarcategoria(int idCategoria) {
		PreparedStatement ps=null;
		try {
			ps=cx.conectar().prepareStatement("DELETE FROM categoria WHERE idcategoria=?");
			ps.setInt(1, idCategoria);
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean editarcategoria(Categoria user) {
		PreparedStatement ps=null;
		try {
			ps=cx.conectar().prepareStatement("UPDATE categoria SET categoria=? WHERE idcategoria=?");
			ps.setString(1, user.getCategoria());
			ps.setInt(2, user.getIdCategoria());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		
	}

}

