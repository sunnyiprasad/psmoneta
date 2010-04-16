/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.module;

import com.rsc.moneta.module.inputhandler.InputHandlerConfig;
import java.util.Map;

/**
 *
 * @author sulic
 */
public interface InputHandler {
    void setConfig(InputHandlerConfig config);
    String check(Map inputData);
    String pay(Map inputData);
    String getStatus(Map inputData);
    String cancel(Map inputData);
}
