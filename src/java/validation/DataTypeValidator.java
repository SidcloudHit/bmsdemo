/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author acer
 */
public class DataTypeValidator {
      public static boolean isNumeric(String str){
      if (str == null){
          return false;
      }
      try{
          double d = Double.parseDouble(str);
      }
      catch(NumberFormatException e){
          return false;
      }
      return true;
  }
  
    public static boolean isValidDate(String inDate,String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
  }

}
