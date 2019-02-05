import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

public class OTPHandler extends Thread {
    private String url;

    OTPHandler(String pno) {
        char ch[] = "0123456789".toCharArray();
        char[] c = new char[6];
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            c[i] = ch[random.nextInt(ch.length)];
        }
        url = String.format("http://control.msg91.com/api/sendotp.php?authkey=249243A7rlMFbL5bfceb1a&message=%s is your One Time Password for Virtual Wallet. Please Do not share this with anyone.&sender=SHBZOTP&mobile=91%s&otp=%s", new String(c), pno, new String(c));
        System.out.println(url);
    }

    @Override
    public void run() {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
            JSONObject myResponse = new JSONObject(response.toString());
            if (!myResponse.getString("type").equalsIgnoreCase("success")) {
                JOptionPane.showMessageDialog(null, "OTP Sending failed");
            }
        } catch (Exception e) {
            System.out.println("OTP class failed" + e.getMessage());
        }
    }
}

