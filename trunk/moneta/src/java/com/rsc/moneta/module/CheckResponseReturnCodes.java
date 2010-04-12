///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
package com.rsc.moneta.module;
//
///**
// *
// * @author Солодовников Д.А.
// */

/**
 * Перечисление CheckResponseReturnCodes представляет собой список возможных
 * статусов заказов ПС ТЛСМ, возвращаемых информационной системой
 * Интернет-Магазина в ответ на запросы "check" от ПС ТЛСМ
 * @author Солодовников Д.А.
 */

public enum CheckResponseReturnCodes {
    // Заказ валиден в информационной системе Интернет-Магазина,
    // ответ Интернет-Магазина содержит сумму заказа для оплаты
    ORDER_IS_VALID_AND_RESPONSE_CONTAINS_AMOUNT,

    // Заказ считается оплаченным в информационной системе Интернет-Магазина,
    // информационная системе Интернет-Магазина уведомила ПС ТЛСМ
    ORDER_IS_COMPLETED_AND_TLSM_NOTIFIED,

    // Заказ валиден в информационной системе Интернет-Магазина и находится в 
    // обработке
    ORDER_IS_VALID_AND_PROCESSING,

    // Заказ не валиден в информационной системе Интернет-Магазина (отменён и т.д.)
    ORDER_IS_INVALID;

    // TODO: Денис, знаю что уродливо, но я пока не знаю, как сделать неуродливо,
    // причём не факт, что вообще нам понадобится приведение к int этих статусов
    public int toInt(CheckResponseReturnCodes checkResponseReturnCode) {
        if (checkResponseReturnCode == CheckResponseReturnCodes.ORDER_IS_VALID_AND_RESPONSE_CONTAINS_AMOUNT)
        {
            return 1;
        } else {
            if (checkResponseReturnCode == CheckResponseReturnCodes.ORDER_IS_COMPLETED_AND_TLSM_NOTIFIED) {
                return 2;
            } else {
                if (checkResponseReturnCode == CheckResponseReturnCodes.ORDER_IS_VALID_AND_PROCESSING) {
                    return 3;
                } else {
                    if (checkResponseReturnCode == CheckResponseReturnCodes.ORDER_IS_INVALID) {
                        return 4;
                    } else {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }
                }
            }
        }
    }
}