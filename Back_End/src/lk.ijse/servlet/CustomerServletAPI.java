package lk.ijse.servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = {"/pages/customer"})
public class CustomerServletAPI extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {

            Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaeePosApp ", "root", "ushan1234");

            PreparedStatement pstm = connection.prepareStatement("select * from customer");
            ResultSet rst = pstm.executeQuery();
            PrintWriter writer = resp.getWriter();

            resp.addHeader("Content-Type","application/json");
            resp.addHeader("Access-Control-Allow-Origin","*");

            JsonArrayBuilder allCustomers = Json.createArrayBuilder();

            while (rst.next()) {
                String id = rst.getString(1);
                String name = rst.getString(2);
                String address = rst.getString(3);
                String salary = rst.getString(4);

                JsonObjectBuilder customer = Json.createObjectBuilder();

                customer.add("customerId", id);
                customer.add("customerName", name);
                customer.add("customerAddress", address);
                customer.add("customerSalary", salary);

                allCustomers.add(customer.build());

            }

            writer.print(allCustomers.build());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Content-Type","application/json");
        resp.addHeader("Access-Control-Allow-Origin","*");

        PrintWriter writer = resp.getWriter();

        JsonReader reader = Json.createReader(req.getReader());

        JsonObject jsonObject = reader.readObject();


        String cusId = jsonObject.getString("id");
        String cusName = jsonObject.getString("name");
        String cusAddress = jsonObject.getString("address");
        String salary = jsonObject.getString("salary");


        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaeePosApp", "root", "ushan1234");


            PreparedStatement pstm = connection.prepareStatement("insert into customer values(?,?,?,?)");
            pstm.setObject(1, cusId);
            pstm.setObject(2, cusName);
            pstm.setObject(3, cusAddress);
            pstm.setObject(4, Integer.parseInt(salary));

            if (pstm.executeUpdate() > 0) {

                resp.addHeader("Content-Type","application/json");

                JsonObjectBuilder m = Json.createObjectBuilder();
                m.add("state","OK");
                m.add("message","Succesfuly Added");
                m.add("data","Succesfuly Added");
                resp.setStatus(200);
                writer.print(m.build());

            }
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {

            resp.addHeader("Content-Type","application/json");

            JsonObjectBuilder m = Json.createObjectBuilder();
            m.add("state","NO");
            m.add("message",e.getMessage());
            m.add("data","Not Added");
            writer.print(m.build());
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Content-Type","application/json");
        resp.addHeader("Access-Control-Allow-Origin","*");

        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaeePosApp", "root", "ushan1234");
            PrintWriter writer = resp.getWriter();

            JsonReader reader = Json.createReader(req.getReader());

            JsonObject jsonObject = reader.readObject();


            String cusId = jsonObject.getString("id");
            String cusName = jsonObject.getString("name");
            String cusAddress = jsonObject.getString("address");
            String cusSalary = jsonObject.getString("salary");


            PreparedStatement pstm3 = connection.prepareStatement("update customer set customerName=?,customerAddress=?,customerSalary=? where customerId=?");
            pstm3.setObject(4, cusId);
            pstm3.setObject(1, cusName);
            pstm3.setObject(2, cusAddress);
            pstm3.setObject(3, cusSalary);
            if (pstm3.executeUpdate() > 0) {
                resp.addHeader("Content-Type","application/json");

                JsonObjectBuilder m = Json.createObjectBuilder();
                m.add("state","OK");
                m.add("message","Succesfuly Update");
                m.add("data","Succesfuly Update");
                writer.print(m.build());
            }


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Content-Type","application/json");
        resp.addHeader("Access-Control-Allow-Origin","*");

        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/JavaeePosApp", "root", "ushan1234");
            PrintWriter writer = resp.getWriter();

            JsonReader reader = Json.createReader(req.getReader());

            JsonObject jsonObject = reader.readObject();

            String cusId = jsonObject.getString("id");

            System.out.println(cusId);


            PreparedStatement pstm2 = connection.prepareStatement("delete from customer where customerId=?");
            pstm2.setObject(1, cusId);
            if (pstm2.executeUpdate() > 0) {
                resp.addHeader("Content-Type","application/json");

                JsonObjectBuilder m = Json.createObjectBuilder();
                m.add("state","OK");
                m.add("message","Succesfuly Delete");
                m.add("data","Succesfuly Delete");
                writer.print(m.build());
            }


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Access-Control-Allow-Methods","PUT");
        resp.addHeader("Access-Control-Allow-Methods","DELETE");
        resp.addHeader("Access-Control-Allow-Headers","content-type");
    }

}
