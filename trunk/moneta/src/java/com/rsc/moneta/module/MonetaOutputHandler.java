/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module;

import java.util.Map;

/**
 *
 * @author sulic
 */
public class MonetaOutputHandler implements OutputHandler{

    // Данный метод отправляет запрос в интернет магазин по протоколу монета. См. MONETA.Assistant стр. 20
    public String check(Map inputData) {
        //TODO: Rashid 
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // Данный метод отправляет запрос в интернет магазин по протоколу монета. См. MONETA.Assistant стр. 21
    public String pay(Map inputData) {
        //TODO: Rashid
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // Данный метод не реализуется для Монеты
    public String getStatus(Map inputData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // Данный метод не реализуется для Монеты
    public String cancel(Map inputData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
