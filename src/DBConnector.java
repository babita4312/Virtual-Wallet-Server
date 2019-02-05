import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

public class DBConnector {

    private String COLUMN_PIC;
    private File file;
    private Connection conn;
    private Statement statement;
    private ResultSet resultSet;
    private String DB_NAME;
    private String TABLE_DB;
    private String COLUMN_ACC;
    private String COLUMN_PASS;
    private String COLUMN_FNAME;
    private String COLUMN_LNAME;
    private String COLUMN_PNO;
    private String COLUMN_ADDR;
    private String COLUMN_ADHAAR;
    private String COLUMN_DOB;
    private String COLUMN_GENDER;
    private String COLUMN_NATIONALITY;
    private String COLUMN_OCC;
    private String COLUMN_AMT;
    private String COLUMN_NAME;
    private String COLUMN_EMAIL;
    private String image;
    private FileInputStream fstream;
    private byte[] imageData;
    private String iData;
    private String pathImg;
    //String first_name, last_name, address, mobile_no, adhaar, gender, Nationality, dob, occupation, Password


    public DBConnector() {
        DB_NAME = "jdbc:sqlite:bankDatabase.db";
        TABLE_DB = "BankDatabase";
        COLUMN_ACC = "acc_no";
        COLUMN_PASS = "password";
        COLUMN_NAME = "name";
        COLUMN_PNO = "phone";
        COLUMN_EMAIL = "email";
        COLUMN_AMT = "amount";
        COLUMN_FNAME = "fname";
        COLUMN_LNAME = "lname";
        COLUMN_ADDR = "address";
        COLUMN_ADHAAR = "adhaar";
        COLUMN_DOB = "dob";
        COLUMN_GENDER = "gender";
        COLUMN_NATIONALITY = "nationality";
        COLUMN_OCC = "occupation";
        COLUMN_PIC = "picture";
    }



     private static byte[] decodeImage(String imageDataString) {
        return Base64.getDecoder().decode(imageDataString);
    }

     private String encodeImage(byte[] imageByteArray) {
        return Base64.getEncoder().encodeToString(imageByteArray);
    }


    boolean check() {
        file = new File("bankDatabase.db");
        return file.exists();
    }

    boolean createDB() {
        try {
            conn = DriverManager.getConnection(DB_NAME);
            statement = conn.createStatement();
            //statement.execute("CREATE TABLE BankDatabase ( acc_no INTEGER PRIMARY KEY AUTOINCREMENT ,password TEXT,name TEXT,phone TEXT ,email TEXT,amount INTEGER )");
           /* String a = String.format("CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT %s TEXT %s TEXT %s TEXT %s TEXT %s TEXT )"
                    ,TABLE_DB,COLUMN_ACC,COLUMN_PASS,COLUMN_NAME,COLUMN_PNO,COLUMN_EMAIL,COLUMN_AMT);*/
           /* String query = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                            "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT)", TABLE_DB, COLUMN_ACC, COLUMN_PASS, COLUMN_FNAME, COLUMN_LNAME, COLUMN_ADDR, COLUMN_PNO,
                    COLUMN_ADHAAR, COLUMN_DOB, COLUMN_GENDER, COLUMN_NATIONALITY, COLUMN_OCC, COLUMN_AMT);*/
            String query = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT," +
                            "%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s BLOB)", TABLE_DB, COLUMN_ACC, COLUMN_PASS, COLUMN_FNAME, COLUMN_LNAME, COLUMN_ADDR, COLUMN_PNO,
                    COLUMN_ADHAAR, COLUMN_DOB, COLUMN_GENDER, COLUMN_NATIONALITY, COLUMN_OCC, COLUMN_AMT, COLUMN_PIC);
            statement.execute(query);

            /*statement.execute("CREATE TABLE IF NOT EXISTS " + TABLE_DB +
                    " (" + COLUMN_ACC + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    COLUMN_PASS + " TEXT ," +
                    COLUMN_NAME + " TEXT ," +
                    COLUMN_PNO + " TEXT ," +
                    COLUMN_EMAIL + " TEXT ," +
                    COLUMN_AMT + " TEXT" + ")");*/
            //insertUser(statement, "123456789", "Subhash", "7535053018", "rawatsubhash02@gmail.com", 1000000);

            statement.close();
            conn.close();

            //
            return true;
        } catch (Exception e) {
            // Main.appendToPane(new JTextPane(), "Error in Creating Database\n\n", Color.red);
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void insertUser(@NotNull Statement statement, String pass, String name, String phone, String email, int amt) throws SQLException {
        statement.execute("INSERT INTO " + TABLE_DB +
                " (" + COLUMN_PASS + ", " +
                COLUMN_NAME + ", " +
                COLUMN_PNO + ", " +
                COLUMN_EMAIL + ", " +
                COLUMN_AMT +
                " ) " +
                "VALUES('" + pass + "','" + name + "','" + phone + "','" + email + "'," + amt + ")");
        resultSet = statement.executeQuery("SELECT COUNT(*) from BankDatabase ");
        System.out.println(resultSet.getInt(1));


    }

    String checkLogin(String acc, String pass) {
        try {
            conn = DriverManager.getConnection(DB_NAME);
            statement = conn.createStatement();
            // System.out.println("SELECT "+ COLUMN_PASS + " FROM "+ TABLE_DB + " WHERE "+COLUMN_ACC+"= "+acc+"");
            resultSet = statement.executeQuery("SELECT " + COLUMN_PASS + " FROM " + TABLE_DB + " WHERE " + COLUMN_ACC + "= " + acc);
            System.out.println(resultSet.getString(COLUMN_PASS));
            if (resultSet.getString(COLUMN_PASS).equals(pass))
                return "Success";
            else
                return "Failed";
        } catch (SQLException e) {
            // Main.appendToPane(new JTextPane(), "Error in Creating Database\n\n", Color.red);
            System.out.println("I am " + e.getMessage());
            return "Failed";
        } finally {
            try {
                statement.close();
                conn.close();
            } catch (Exception e) {

            }
        }
    }

    String createUser(JSONObject accept_response) {
        try {
            conn = DriverManager.getConnection(DB_NAME);
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM BankDatabase");
            int id = (resultSet.getInt(1) + 1);
            char ch[] = "0123456789".toCharArray();
            onImage(accept_response.getString("image"), accept_response.getString("ext"));
            char[] c = new char[9];
            SecureRandom random = new SecureRandom();
            for (int i = 0; i < 9; i++) {
                c[i] = ch[random.nextInt(ch.length)];
            }
            String accno = new String(c);
            String acc = String.format("%s%s", accno, String.valueOf(id));
            String query = String.format("INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')", TABLE_DB,
                    COLUMN_ACC, COLUMN_PASS, COLUMN_FNAME, COLUMN_LNAME, COLUMN_ADDR, COLUMN_PNO, COLUMN_ADHAAR, COLUMN_DOB, COLUMN_GENDER, COLUMN_NATIONALITY, COLUMN_OCC, COLUMN_AMT, COLUMN_PIC,
                    acc, accept_response.getString("password"), accept_response.getString("fname"), accept_response.getString("lname"), accept_response.getString("addr"),
                    accept_response.getString("mno"), accept_response.getString("adhaar"), accept_response.getString("dob"), accept_response.getString("gender"),
                    accept_response.getString("nationality"), accept_response.getString("occuption"), "0", iData);
            System.out.println(query);
            statement.execute(query);
            return String.format("%s %s", acc, accept_response.getString("password"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return "Failed";

        } catch (JSONException e) {
            System.out.println(e.getMessage());
            return "Failed";
        } finally {
            try {
                statement.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("Already Closed");
            }
        }
    }

    private void onImage(String image, String ext) {
        try {
            byte[] imageByte = decodeImage(image);
            FileOutputStream imageOutFile = new FileOutputStream("imagefromclient" + ext);
            imageOutFile.write(imageByte);
            imageOutFile.close();

            Image img = resizeImage("imagefromclient" + ext, 114, 106).getImage();

            BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.SCALE_SMOOTH);

            Graphics2D g2 = bi.createGraphics();
            g2.drawImage(img, 0, 0, null);
            g2.dispose();
            ImageIO.write(bi, "jpg", new File("compimg" + ext));


            file = new File("compimg" + ext);
            fstream = new FileInputStream(file);
            imageData = new byte[(int) file.length()];
            fstream.read(imageData);

            iData = encodeImage(imageData);

            System.out.println("IMAGE DATA:  " + iData.length());
            fstream.close();
            new File("imagefromclient" + ext).delete();
            new File("compimg" + ext).delete();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    ImageIcon resizeImage(String image_path, int width, int height) {
        ImageIcon myImage = new ImageIcon(image_path);
        Image img = myImage.getImage();
        Image newimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newimg);
        return image;
    }

    String deposit(JSONObject deposit_response) {
        try {
            conn = DriverManager.getConnection(DB_NAME);
            statement = conn.createStatement();
            String amnt = String.format("SELECT amount FROM BankDatabase WHERE acc_no=%s", deposit_response.getString("ano"));
            resultSet = statement.executeQuery(amnt);
            String amm = resultSet.getString("amount");
            double amt = Double.parseDouble(amm);
            String da = deposit_response.getString("damount");
            double damt = Double.parseDouble(da);
            double tamnt = amt + damt;
            String dpquery = String.format("UPDATE BankDatabase SET amount=%s WHERE acc_no=%s", Double.toString(tamnt), deposit_response.getString("ano"));
            statement.execute(dpquery);
            return "success";
        } catch (SQLException s) {
            return "Failed";
        } catch (JSONException j) {
            return "Failed";
        }

    }

    String withdraw(String accno, String amount) {
        try {
            conn = DriverManager.getConnection(DB_NAME);
            statement = conn.createStatement();
            //resultSet=statement.executeQuery("SELECT"+COLUMN_AMT+"FROM"+ TABLE_DB + " WHERE "+COLUMN_ACC+"= "+Integer.parseInt(accno)+"");
            resultSet = statement.executeQuery("SELECT amount FROM BankDatabase WHERE acc_no=" + accno);
            String amt = resultSet.getString("amount");
            //int x=  Integer.parseInt(String.valueOf(resultSet));
            double a = Double.parseDouble(amt);
            double b = Double.parseDouble(amount);
//            if(x>=Integer.parseInt(String.valueOf(amount)))
//            {
//                int newamnt=x-Integer.parseInt(String.valueOf(amount));
//                String NEW_AMNT=new String();
//                NEW_AMNT=Integer.toString(newamnt);
//                statement.execute("UPDATE"+COLUMN_AMT+"SET "+ COLUMN_AMT +"="+NEW_AMNT+ " WHERE "+COLUMN_ACC+"="+2+"");
//                //System.out.println(statement.executeQuery("SELECT"+COLUMN_AMT+"FROM"+ TABLE_DB + " WHERE "+COLUMN_ACC+"= "+acc+""));
//                System.out.println("hi i rac");
//                return "success";
//            }
            if (b > a) {
                return "failed";
            } else {
                a = a - b;
                String NEW_AMNT;
                NEW_AMNT = Double.toString(a);
                statement.execute("UPDATE " + TABLE_DB + " SET " + COLUMN_AMT + " = " + NEW_AMNT + " WHERE " + COLUMN_ACC + " = " + accno);
                return "success";
            }
        } catch (Exception e) {
            System.out.println("error ");
        } finally {
            try {
                statement.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("Already Closed");
            }
        }
        return "failed";
    }

    String transfer(String accno, String amount, String otheraccno) {
        try {
            conn = DriverManager.getConnection(DB_NAME);
            statement = conn.createStatement();
            resultSet = statement.executeQuery("SELECT amount FROM BankDatabase WHERE acc_no=" + accno);
            String amt = resultSet.getString("amount");
            ResultSet resultSet1 = statement.executeQuery("SELECT amount FROM BankDatabase WHERE acc_no=" + otheraccno);
            String otheramt = resultSet1.getString("amount");
            double a = Double.parseDouble(amt);
            System.out.println("a" + a);
            double b = Double.parseDouble(amount);
            System.out.println("b" + b);
            double c = Double.parseDouble(otheramt);
            System.out.println("c" + c);
            if (b > a) {
                return "failed";
            } else {
                a = a - b;

                String NEW_AMNT;

                NEW_AMNT = Double.toString(a);
                String OTHER_AMT;
                c = c + b;
                OTHER_AMT = Double.toString(c);
                statement.execute("UPDATE " + TABLE_DB + " SET " + COLUMN_AMT + " = " + NEW_AMNT + " WHERE " + COLUMN_ACC + " = " + accno);
                statement.execute("UPDATE " + TABLE_DB + " SET " + COLUMN_AMT + " = " + OTHER_AMT + " WHERE " + COLUMN_ACC + " = " + otheraccno);
                return "success";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "failed";
        } finally {
            try {
                statement.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("Already Closed");
            }
        }
    }

    String display_profile(JSONObject displayProfile) {
        try {
            conn = DriverManager.getConnection(DB_NAME);
            statement = conn.createStatement();
            String query = String.format("SELECT * FROM BankDatabase WHERE acc_no=%s", displayProfile.getString("acc"));
            resultSet = statement.executeQuery(query);
            String spass = resultSet.getString("password");
            String sfname = resultSet.getString("fname");
            String slname = resultSet.getString("lname");
            String saddr = resultSet.getString("address");
            String smob = resultSet.getString("phone");
            String sadhaarno = resultSet.getString("adhaar");
            String sdob = resultSet.getString("dob");
            String sgender = resultSet.getString("gender");
            String snationality = resultSet.getString("nationality");
            String socc = resultSet.getString("occupation");
            String samount = resultSet.getString("amount");
            JSONObject jdisplay = new JSONObject();
            jdisplay.put("code", "510");
            jdisplay.put("cpassword", spass);
            jdisplay.put("cfname", sfname);
            jdisplay.put("clname", slname);
            jdisplay.put("caddr", saddr);
            jdisplay.put("cmob", smob);
            jdisplay.put("cadhaarno", sadhaarno);
            jdisplay.put("cdob", sdob);
            jdisplay.put("cgender", sgender);
            jdisplay.put("cnationality", snationality);
            jdisplay.put("cocc", socc);
            jdisplay.put("camount", samount);
            jdisplay.put("image", resultSet.getString("picture"));
            System.out.println(resultSet.getString("picture"));

            return jdisplay.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "failed";
        } finally {
            try {
                statement.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("Already Closed");
            }
        }
    }

    String update_profile(JSONObject updateResponse) {
        try {
            conn = DriverManager.getConnection(DB_NAME);
            statement = conn.createStatement();
            //String stringquery=String.format("UPDATE BankDatabase SET");
            /*statement.execute("UPDATE " + TABLE_DB + " SET " + COLUMN_PASS + " = " + updateResponse.getString("password") + ", " + COLUMN_FNAME + " = " + updateResponse.getString("fname") + ", "
                    + COLUMN_LNAME + " = " + updateResponse.getString("lname") + ", " + COLUMN_ADDR + " = " + updateResponse.getString("addr") + ", " + COLUMN_PNO + " = " + updateResponse.getString("mno") + ", "
                    + COLUMN_ADHAAR + " = " + updateResponse.getString("adhaar") + ", " + COLUMN_DOB + " = " + updateResponse.getString("dob") + ", " + COLUMN_GENDER + " = " + updateResponse.getString("gender") + ", "
                    + COLUMN_NATIONALITY + " = " + updateResponse.getString("nationality") + ", " + COLUMN_OCC + " = " + updateResponse.getString("occuption") + " WHERE " + COLUMN_ACC + " = " + updateResponse.getString("accno"));
*/
            String query = String.format("UPDATE BankDatabase SET password='%s',fname='%s',lname='%s',address='%s',phone='%s',adhaar='%s',dob='%s', gender='%s', nationality='%s', occupation='%s' WHERE acc_no=%s",
                    updateResponse.getString("password"), updateResponse.getString("fname"), updateResponse.getString("lname"), updateResponse.getString("addr"), updateResponse.getString("mno")
                    , updateResponse.getString("adhaar"), updateResponse.getString("dob"), updateResponse.getString("gender"), updateResponse.getString("nationality"), updateResponse.getString("occuption"),
                    updateResponse.getString("accno"));
            statement.execute(query);

            return "success";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Failed";
        } finally {
            try {
                statement.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("Already Closed");
            }
        }
    }

    String delete_account(JSONObject deleteac) {
        try {
            conn = DriverManager.getConnection(DB_NAME);
            statement = conn.createStatement();
            String query = String.format("DELETE FROM BankDatabase WHERE %s = %s", COLUMN_ACC, deleteac.getString("acno"));
            statement.execute(query);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Failed";
        } finally {
            try {
                statement.close();
                conn.close();
            } catch (SQLException e) {
                System.out.println("Already Closed");
            }
        }
        return "success";
    }
}


//    public static void main(String[] args) { DBConnector d = new DBConnector();System.out.println(d.check());
//        try (conn = DriverManager.getConnection("jdbc:sqlite:bankDatabase.db");
//
//
//
//             Statement statement = conn.createStatement()) {
//             statement.execute("CREATE TABLE BankDatabase ( acc_no INTEGER PRIMARY KEY AUTOINCREMENT ,name TEXT,phone INTEGER,email TEXT)");
//
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//        System.out.println(d.check());
//    }

