/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.action.admin;

import com.rsc.moneta.action.BaseAction;
import com.rsc.moneta.module.cyberplat.Provider;
import java.util.Collection;

/**
 *
 * @author sulic
 */
public class ViewProviderList extends BaseAction{
    public Collection<Provider> providers;

    @Override
    public String execute() throws Exception {
        return super.execute();
    }

    public Collection<Provider> getProviders() {
        return providers;
    }

    public void setProviders(Collection<Provider> providers) {
        this.providers = providers;
    }

}
