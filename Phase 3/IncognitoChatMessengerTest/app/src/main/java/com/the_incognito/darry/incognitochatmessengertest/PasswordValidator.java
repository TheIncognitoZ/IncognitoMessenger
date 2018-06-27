package com.the_incognito.darry.incognitochatmessengertest;

/**
 * Created by darry on 12/6/2016.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator{

    private static Pattern passPattern;
    private static Matcher matcher;

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";

    public PasswordValidator(){
        passPattern = Pattern.compile(PASSWORD_PATTERN);
    }

    /**
     * Validate password with regular expression
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public static boolean validate(String password){
        System.out.println("password in validate is :"+password);
        System.out.println("matcher in validate is :"+matcher);
        System.out.println("passPattern.matcher(password) in validate is :"+passPattern.matcher(password));
        matcher = passPattern.matcher(password);
        return matcher.matches();

    }
    public static boolean isInvalidCharacter(String candidate) {
        boolean result = false;
        final char[] symbolsCharArray = { '\'', '"', '\\', '/', '~', '^', '&', '*', '+', '-', '_', ')', '(', '<', '>',
                ',', '.', '?' };
        char[] scharArray = candidate.toCharArray();
        for (int j = 0; j < symbolsCharArray.length; j++) {
            for (int i = 0; i < scharArray.length; i++) {
                //System.out.println(String.valueOf(symbolsCharArray[j]));
                //System.out.println(String.valueOf(scharArray[i]));
                if(symbolsCharArray[j]==scharArray[i]){
                    System.out.println("match found :"+symbolsCharArray[j]);
                    result = true;
                    break;
                }
            }
        }
        System.out.println("result in isInvalidCharacter is :"+result);
       return result;
    }

}
