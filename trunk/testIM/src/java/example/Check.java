/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package example;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 *
 * @author sulic
 */

public class Check extends ActionSupport{

    public Check() {
    }

    @Override
    public String execute() throws Exception {

        return Action.SUCCESS;
    }

}