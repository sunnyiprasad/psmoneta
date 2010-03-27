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
public interface OutputHandler {
    String check(Map inputData);
    String pay(Map inputData);
    String getStatus(Map inputData);
    String cancel(Map inputData);
}
