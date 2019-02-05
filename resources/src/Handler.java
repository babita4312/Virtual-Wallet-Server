import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler extends Thread {
    private Socket socket;
    private DBConnector dbConnector;


    Handler(@NotNull Socket socket) {
        this.socket = socket;
        Main.appendToPane(Main.logsText, String.format("[+] %s Connected.\n", socket.getInetAddress().toString().substring(1)), Color.GREEN);
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            String que[];
            dbConnector = new DBConnector();

            while (true) {
                String inputLine;
                String response = "Failed";
                inputLine = input.readLine();
                System.out.println(inputLine);
                try {
                    JSONObject myResponse = new JSONObject(inputLine);
                    if (myResponse.getString("code").equals("501")) {
                        response = dbConnector.checkLogin(myResponse.getString("ano"), myResponse.getString("pass"));
                    } else if (myResponse.getString("code").equals("502")) {
                        response = dbConnector.createUser(myResponse);
                    } else if (myResponse.getString("code").equals("503")) {
                        new OTPHandler(myResponse.getString("mno")).start();
                        response = "success";
                        //response = OTPHandler.sendOTP(myResponse.getString("mno"));
                    } else if (myResponse.getString("code").equals("504")) {
                        response = dbConnector.withdraw(myResponse.getString("accno"), myResponse.getString("amount"));
                        //response = "success";
                        //response = OTPHandler.sendOTP(myResponse.getString("mno"));
                    } else if (myResponse.getString("code").equals("505")) {
                        response = dbConnector.deposit(myResponse);
                    } else if (myResponse.getString("code").equals("506")) {
                        response = dbConnector.transfer(myResponse.getString("accno"), myResponse.getString("amount"), myResponse.getString("otheraccno"));
                    } else if (myResponse.getString("code").equals("507")) {
                        response = dbConnector.display_profile(myResponse);
                    } else if (myResponse.getString("code").equals("508")) {
                        response = dbConnector.update_profile(myResponse);
                    } else if (myResponse.getString("code").equals("509")) {
                        response = dbConnector.delete_account(myResponse);
                    }
                } catch (JSONException e) {
                    Main.appendToPane(Main.logsText, "ERROR Occured", Color.red);
                }

                output.println(response);
                /*String resp= null;
                String got = input.readLine();

                que = got.split(" ");
                if(que[0].equalsIgnoreCase("051")){
                     resp = dbConnector.checkLogin(que[1], que[2]);
                }
                if(que[0].equalsIgnoreCase("052")){
                    resp=dbConnector.createUser(que);
                }
                output.println(resp);*/

            }


//            while (true) {
//                String echoString = input.readLine();
//                System.out.println("REceived " + echoString);
//                if (echoString.equals("exit")) {
//                    break;
//                }
//                output.println("Echo From Server: " + echoString);
////                try{
////                    Thread.sleep(15000);
////                }catch (InterruptedException e){
////
////                }
//            }

        } catch (IOException e) {
            System.out.println("Client Disconnected");
            Main.appendToPane(Main.logsText, "[-] " + socket.getInetAddress().toString().substring(1) + " Disconnected\n", Color.RED);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
    }
}
