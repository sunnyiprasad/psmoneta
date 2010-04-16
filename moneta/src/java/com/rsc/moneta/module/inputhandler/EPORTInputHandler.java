/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module.inputhandler;

import com.rsc.moneta.module.InputHandler;
import java.util.Map;

/**
 *
 * @author sulic
 */
public class EPORTInputHandler implements InputHandler{

    private final static String RETURN_CODE_E1 = "E1"; // E1 - запрос не распознан
    private final static String RETURN_CODE_E2 = "E2"; // E2 - технические трудности обработки запроса
    private final static String RETURN_CODE_E3 = "E3"; // E3 - подпись запроса неверна 
    private final static String RETURN_CODE_E4 = "E4"; // E4 - результат операции еще не известен 
    private final static String RETURN_CODE_S1 = "S1"; // S1 - успешно, платеж считается зачисленным
    private final static String RETURN_CODE_S2 = "S2"; // S2 - успешно, платеж считается аннулированным
    private final static String RETURN_CODE_F1 = "F1"; // F1 - отказ, сумма меньше минимальной допустимой
    private final static String RETURN_CODE_F2 = "F2"; // F2 - отказ, сумма больше максимальной допустимой 
    private final static String RETURN_CODE_F3 = "F3"; // F3 - отказ, клиент с указанными реквизитами не найден 
    private final static String RETURN_CODE_F4 = "F4"; // F4 - отказ, платеж невозможен (но клиент с указкнными реквизитами найден) 
    private final static String RETURN_CODE_F5 = "F5"; // F5 - отказ, платеж с указанным id уже проведен 
    private final static String RETURN_CODE_F6 = "F6"; // F6 - отказ, платеж с указанным id не найден 
    private final static String RETURN_CODE_F7 = "F7"; // F7 - отказ, платеж с указанным id уже аннулирован 
    private final static String RETURN_CODE_F8 = "F8"; // F8 - отказ, аннулирование не возможно

    public String check(Map inputData) {

        throw new UnsupportedOperationException("Not supported yet.");
        //TODO: Alexey
        /*
        try {
            String account = inputData.get("account").toString();
            try{
                double sum = Double.valueOf(inputData.get("sum").toString()).doubleValue();
                try{

                }
                catch (Exception e){

                }
            }
            catch (Exception e){
                return RETURN_CODE_E1;
            }
        }
        catch (Exception e) {
            return RETURN_CODE_E1;
        }
        */
    }

    public String pay(Map inputData) {
        //TODO: Alexey
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getStatus(Map inputData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String cancel(Map inputData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setConfig(InputHandlerConfig config) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
