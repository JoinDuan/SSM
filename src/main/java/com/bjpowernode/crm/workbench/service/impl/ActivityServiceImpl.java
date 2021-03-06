package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    @Override
    public boolean save(Activity a) {

        boolean flag = true;

        int count = activityDao.save(a);//受到影响的条数

        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {

        //取得total
        int total = activityDao.getTotalByCondition(map);

        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        //将total和dataList封装到vo中
        PaginationVO<Activity> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo反回
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;
        //删除之前考虑有没有关联的备注
        //查询出需要删除的备注的数量，用到哪张表，就给它创建一个dao
        int count1 = activityRemarkDao.getCountByAids(ids);

        //删除备注，返回一个受到影响的条数(实际删除的数量)
        int count2 = activityRemarkDao.deleteByAids(ids);

        if(count1 != count2){
            flag = false;
        }
        //删除市场活动
        int count3 = activityDao.delete(ids);
        if(count3 != ids.length){
            flag = false;
        }


        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        /*
            用到哪个表，就创建它的dao
         */
        //取ulist
        List<User> uList = userDao.getUserList();

        //取a 根据id查单条信息
        Activity a = activityDao.getById(id);

        // 将ulist和a打包到map中，
        //            返回map
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uList", uList);
        map.put("a", a);
        return map;
    }

    @Override
    public boolean update(Activity a) {
        boolean flag = true;

        int count = activityDao.update(a);//受到影响的条数

        if(count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {

        Activity a = activityDao.detail(id);

        return a;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {

        List<ActivityRemark> arlist = activityRemarkDao.getRemarkListByAid(activityId);

        return arlist;
    }

    @Override
    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = activityRemarkDao.deleteById(id);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);

        if(count != 1){
            flag = false;
        }

        return true;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {

        List<Activity> aList = activityDao.getActivityListByClueId(clueId);

        return aList;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(HashMap<String, String> map) {

        List<Activity> aList = activityDao.getActivityListByNameAndNotByClueId(map);

        return aList;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {

        List<Activity> aList = activityDao.getActivityListByName(aname);

        return aList;
    }
}
