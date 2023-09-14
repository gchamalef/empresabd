/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author pc01
 */
public class Cliente extends Persona {
    private String nit;
    private int id;
    Conexion cn;
    public Cliente(){
    }

    public Cliente(String nit, int id, String nombres, String apellidos, String direccion, String telefono, String fecha_nacimiento) {
        super(nombres, apellidos, direccion, telefono, fecha_nacimiento);
        this.nit = nit;
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public DefaultTableModel leer(){
        DefaultTableModel tabla = new DefaultTableModel();
        try{
            cn = new Conexion();
            cn.abrir_conexion();
            String query;
            query = "SELECT id_cliente AS id, nit, nombres, apellidos, direccion, telefono, fecha_nacimiento FROM clientes;";
            ResultSet consulta = cn.conexionBD.createStatement().executeQuery(query);
            String encabezado[] = 
            {"id","Nit","Nombres","Apellidos","Direccion","Telefono","Nacimiento"};
            tabla.setColumnIdentifiers(encabezado);
            String datos[] = new String[7];
            while(consulta.next()){
                datos[0] = consulta.getString("id");
                datos[1] = consulta.getString("nit");
                datos[2] = consulta.getString("nombres");
                datos[3] = consulta.getString("apellidos");
                datos[4] = consulta.getString("direccion");
                datos[5] = consulta.getString("telefono");
                datos[6] = consulta.getString("fecha_nacimiento");
                tabla.addRow(datos);
            }
            cn.cerrar_conexion();
        } catch(SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
        
        return tabla;
    }

    @Override
    public void crear() {
        try{
            PreparedStatement parametro;
            cn = new Conexion();
            cn.abrir_conexion();
            String query;
            query = "insert into clientes(nit, nombres, apellidos, direccion, telefono, fecha_nacimiento) values(?,?,?,?,?,?);";
            parametro = (PreparedStatement) cn.conexionBD.prepareStatement(query);
            parametro.setString(1, getNit());
            parametro.setString(2, getNombres());
            parametro.setString(3, getApellidos());
            parametro.setString(4, getDireccion());
            parametro.setString(5, getTelefono());
            parametro.setString(6, getFecha_nacimiento());
            int executar = parametro.executeUpdate();
            System.out.println("Se inserto: " + Integer.toString(executar) + "Registro");
            cn.cerrar_conexion();
        } catch(SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    @Override
    public void actualizar() {
        try{
            PreparedStatement parametro;
            cn = new Conexion();
            cn.abrir_conexion();
            StringBuilder query = new StringBuilder("update clientes set ");
            List<String> parametros = new ArrayList<>();
            
            if(getNombres() != null && !getNombres().isEmpty()){
                query.append("nombres = ?, ");
                parametros.add(getNombres());
            }
            if(getApellidos() != null && !getApellidos().isEmpty()){
                query.append("apellidos = ?, ");
                parametros.add(getApellidos());
            }
            if(getDireccion() != null && !getDireccion().isEmpty()){
                query.append("direccion = ?, ");
                parametros.add(getDireccion());
            }
            if(getTelefono() != null && !getTelefono().isEmpty()){
                query.append("telefono = ?, ");
                parametros.add(getTelefono());
            }
            if(getFecha_nacimiento() != null && !getFecha_nacimiento().isEmpty()){
                query.append("fecha_nacimiento = ?, ");
                parametros.add(getFecha_nacimiento());
            }
            
            query.setLength(query.length() - 2);
            query.append(" where nit = ?;");
            parametros.add(getNit());
            parametro = (PreparedStatement) cn.conexionBD.prepareStatement(query.toString());
            
            for(int i = 0; i < parametros.size(); i++){
                parametro.setString(i + 1, parametros.get(i));
            }
            
            int executar = parametro.executeUpdate();
            System.out.println("Se actualizo: " + Integer.toString(executar) + " Registro");
            cn.cerrar_conexion();
        } catch(SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    @Override
    public void borrar(){
        try{
            PreparedStatement parametro;
            cn = new Conexion();
            cn.abrir_conexion();
            String query = "delete from clientes where nit = ?;";
            parametro = (PreparedStatement) cn.conexionBD.prepareStatement(query);
            parametro.setString(1, getNit());
            int executar = parametro.executeUpdate();
            
            if(executar > 0){
                System.out.println("Se borro correctamente el registro con el NIT: " + getNit());
            } else{
                System.out.println("No se encontro un registro con el NIT: " + getNit() + " para borrar.");
            }
            
            cn.cerrar_conexion();
        } catch(SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
