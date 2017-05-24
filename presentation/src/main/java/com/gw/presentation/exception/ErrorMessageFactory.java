package com.gw.presentation.exception;


import android.content.Context;

import com.gw.data.exception.NetworkConnectionException;
import com.gw.data.exception.NumberNotFoundException;
import com.gw.data.exception.SMSReadException;
import com.gw.domain.exception.BankException;
import com.gw.presentation.R;

/**
 * Factory used to create error messages from an Exception as a condition.
 */
public class ErrorMessageFactory {

    private ErrorMessageFactory() {
        //empty
    }

    /**
     * Creates a String representing an error message.
     *
     * @param context   Context needed to retrieve string resources.
     * @param exception An exception used as a condition to retrieve the correct error message.
     * @return {@link String} an error message.
     */
    public static String create(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_generic);

        if (exception instanceof NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection);
        }
        else if (exception instanceof BankException) {
            message = exception.getMessage();
        }
        else if(exception instanceof SMSReadException){
            message = context.getString(R.string.exception_message_sms_reading);
        }
        else if(exception instanceof NumberNotFoundException){
            message = context.getString(R.string.exception_message_number_not_found);
        }
        else{
            message = exception.toString();
            exception.printStackTrace();

        }


        return message;
    }
}
