package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;


public class SysInitListener implements ServletContextListener {
    /*
        该方法是用来监听上下文（全局作用域）对象的方法，当服务器启动，上下文域对象创建
        对象创建完毕后，马上执行该方法

        event:该参数能够取得监听的对象
              监听的是什么对象，就可以通过该参数取的什么对象
              例如我们限制监听的上下文域对象 ServletContext，通过该参数就可以取的上下文域对象
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {

        //System.out.println("上下文域对象创建了");
        System.out.println("服务器缓存处理数据字典开始");

        ServletContext application = event.getServletContext();

        //取数据字典
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());

        /*
            应该管业务层要
            7个list

            可以打包成为一个map
            业务层应该是这样来保存数据的
                以类型为key
                map.put("appellationList",dvList1);
                map.put("clueStateList",dvList2);
                map.put("stateList",dvList3);
                ....



         */

        Map<String, List<DicValue>> map = ds.getAll();
        //将map中的上下文域对象中保存的键值对
        Set<String> set = map.keySet();
        for (String key : set) {
            application.setAttribute(key, map.get(key));
        }

        System.out.println("服务器缓存处理数据字典结束");

        //-----------------------------------------------------------------------------

        //数据字典处理完毕后，将处理Stage2Possibility.properties
        /*
            处理Stage2Possibility.properties步骤：
                 解析该文件，将该属性文件中的键值对关系处理成为java中的键值对关系

                Map<String(阶段stage),String(可能性possibility)> pMap = .....
                pMap.put("01-资质审查"，10)；
                pMap.put("02-需求分析"，25)；
                ......
                pMap保存值之后，放在服务器缓存中
                application.setAttribute("pMap",pMap);
         */

        //解析Properties文件

        HashMap<String, String> pMap = new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");

        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()){
            //阶段
            String key = e.nextElement();
            //可能性
            String value = rb.getString(key);
            pMap.put(key, value);
        }
        //将pMap保存到服务器缓存中
        application.setAttribute("pMap", pMap);
    }
}
