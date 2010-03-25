package com.batyrov.ps.module;

import com.batyrov.ps.bean.AbonentPayment;

import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: sulic
 * Date: 09.03.2008
 * Time: 10:22:16
 * To change this template use File | Settings | File Templates.
 */
public interface Processor {
    Properties check(AbonentPayment pay, int req_type);
    Properties payment(AbonentPayment pay, int req_type);
    Properties getStatus(AbonentPayment pay, int req_type);
}
