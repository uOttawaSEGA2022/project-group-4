package com.example.mealer_project.utils;

import android.os.AsyncTask;
import android.util.Log;

public class SendMailTask extends AsyncTask {

    /**
     * empty constructor
     */
    public SendMailTask() {}

    /**
     * this method sends the email in the background process while the app is running
     * @param args the arguments for the email (from, PW, to, subject, body)
     * @return null
     */
    @Override
    protected Object doInBackground(Object... args) {

        // Process: attempting to send email
        try {
            // LOG MESSAGE
            Log.i("SendMailTask", "About to instantiate Gmail...");

            // Variable Declaration
            Gmail email = new Gmail(args[0].toString(), args[1].toString(),
                    args[2].toString(), args[3].toString(), args[4].toString());

            email.createEmailMessage(); //creating message

            email.sendEmail(); //sending message

            // LOG MESSAGE
            Log.i("SendMailTask", "Mail sent.");
        }
        catch (Exception e) { //error-handling
            // LOG MESSAGE
            Log.e("SendMailTask", e.getMessage(), e);
        }

        // Output
        return null;

    }

}