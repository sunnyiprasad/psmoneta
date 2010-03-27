package com.rsc.moneta.action;


import com.batyrov.cms.action.BaseAction;
import com.batyrov.cms.action.Const;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseListAction extends BaseAction
{
    protected int page = 0;
    protected List pages = null;
    protected Map prevPages = null;
    protected Map nextPages = null;

    protected String orderField = null;
    protected String isAsc = null;
    protected boolean isShowNavigation = false;





    public void setListPages(long count)
    {
        long pagesCount = (count / Const.ROWS_COUNT) + (count % Const.ROWS_COUNT == 0 ? 0 : 1);
        pages = new ArrayList();
        if ((count >= 0) && (page < pagesCount))
        {
            int firstPage = page / 10 * 10;//page / Const.ROWS_COUNT;
            //int lastPage =  (firstPage + Const.ROWS_COUNT <= count) ? firstPage + Const.ROWS_COUNT : page + 1;

            prevPages = new HashMap(2);
            prevPages.put("page", firstPage - 10);
            prevPages.put("isDisable", page < 10);

            for (int i = firstPage; i < (firstPage + 10); i++)
            {
                Map pageNumber = new HashMap(2);
                pageNumber.put("page", i);
                pageNumber.put("value", i + 1);
                pageNumber.put("isSelect", i == page);
                pageNumber.put("isDisable", i >= pagesCount);
                pages.add(pageNumber);
            }

            nextPages = new HashMap(3);
            nextPages.put("page", page / 10 + 10);
            nextPages.put("isDisable", pagesCount < firstPage + 10);
            this.isShowNavigation =  pagesCount > 1;
        }
    }

    public boolean isAsc()
    {
        if (isAsc == null)
        {
            isAsc = "true";
            return true;
        }
        try
        {
            return Boolean.parseBoolean(isAsc);
        }catch(Exception exception)
        {
            isAsc = "true";
            return true; 
        }
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }


    public List getPages()
    {
        return pages;
    }

    public Map getPrevPages()
    {
        return prevPages;
    }

    public Map getNextPages()
    {
        return nextPages;
    }


    public String getOrderField()
    {
        return orderField;
    }

    public void setOrderField(String orderField)
    {
        this.orderField = orderField;
    }

    public String getAsc()
    {
        return isAsc;
    }

    public void setAsc(String asc)
    {
        isAsc = asc;
    }


    public boolean isShowNavigation() {
        return isShowNavigation;
    }

    @Override
    public String execute() throws Exception {
        return super.execute(); 
    }
}
