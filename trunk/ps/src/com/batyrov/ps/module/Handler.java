package com.batyrov.ps.module;

import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 26.03.2010
 * Time: 1:16:33
 * To change this template use File | Settings | File Templates.
 */
public interface Handler {
    String check(Properties inputData);
    String pay(Properties inputData);
    String getStatus(Properties inputData);
    String cancel(Properties inputData);
}
