package com.example.kiube9.aplikasi_chating_budiluhur;

/**
 * Created by Kiube9 on 12/28/2016.
 */

public class Kripto_Registrasi {
    public static String encryptionMessage(String Msg)
    {
        String CTxt = "";
        int a = 5;
        int b = 8;
        for (int i = 0; i < Msg.length(); i++)
        {
            CTxt = CTxt + (char) ((((a * Msg.charAt(i)) + b) % 26) + 65);
        }
        return CTxt;
    }

    public static String decryptionMessage(String CTxt)
    {
        String Msg = "";
        int a = 5;
        int b = 8;
        int a_inv = 0;
        int flag = 0;
        for (int i = 0; i < 96; i++)
        {
            flag = (a * i) % 96;
            if (flag == 1)
            {
                a_inv = i;
                System.out.println(i);
            }
        }
        for (int i = 0; i < CTxt.length(); i++)
        {
            Msg = Msg + (char) (((a_inv * ((CTxt.charAt(i) - b)) % 26)) + 65);
        }
        return Msg;
    }
}
