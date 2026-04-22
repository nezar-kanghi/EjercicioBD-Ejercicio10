import java.sql.*;
import java.util.Scanner;

//EJERCICIO 10
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String usuario = "RIBERA";
        String password = "ribera";

        try (Connection conexion = DriverManager.getConnection(url, usuario, password)) {

            // 1. Desactivar autocommit
            conexion.setAutoCommit(false);

            String sql = "INSERT INTO empleadoEjemplo (id, nombre, salario) VALUES (?,?,?)";
            PreparedStatement ps = conexion.prepareStatement(sql);

            System.out.println("¿Cuántos empleados quieres insertar?");
            int cantidad = sc.nextInt();
            sc.nextLine();

            //bucle for que añade el numero de empleados que decida el usuario
            for (int i = 0; i < cantidad; i++) { 
                System.out.println("Empleado " + (i + 1));

                System.out.print("ID: ");
                int id = sc.nextInt();
                sc.nextLine();

                System.out.print("Nombre: ");
                String nombre = sc.nextLine();

                System.out.print("Salario: ");
                double salario = sc.nextDouble();
                sc.nextLine();

                ps.setInt(1, id);
                ps.setString(2, nombre);
                ps.setDouble(3, salario);

                ps.executeUpdate();
            }


            // 2. Si todo va bien commit
            conexion.commit();
            System.out.println("Todos los empleados insertados correctamente");

            mostrarDatos(url, usuario, password); //llamamos al metodo para mostrar los datos

        } catch (SQLException e) {
            System.out.println("Error detectado, haciendo rollback: " + e.getMessage());

            try {
                // 3. Si falla algo rollback
                Connection conexion = DriverManager.getConnection(url, usuario, password);
                conexion.rollback();
            } catch (SQLException ex) {
                System.out.println("Error en rollback: " + ex.getMessage());
            }
        }
    }

    //metodo para mostrar los datos
    public static void mostrarDatos(String url, String usuario, String password){

        try(Connection conexion = DriverManager.getConnection(url, usuario, password);
            Statement statement = conexion.createStatement()){

            String sql = "SELECT * FROM empleadoEjemplo";

            ResultSet rs = statement.executeQuery(sql);

            System.out.println("--- LISTA DE EMPLEADOS (actualizada)  ---");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double salario = rs.getDouble("salario");

                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Salario: " + salario);
            }

        } catch (SQLException e){
            System.out.println("Error al mostrar datos: " + e.getMessage());
        }
    }
}
