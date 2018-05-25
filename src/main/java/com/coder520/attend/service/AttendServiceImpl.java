package com.coder520.attend.service;

import com.coder520.attend.dao.AttendMapper;
import com.coder520.attend.entity.Attend;
import com.coder520.attend.vo.QueryCondition;
import com.coder520.common.page.PageQueryBean;
import com.coder520.common.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("attendServiceImpl")
public class AttendServiceImpl implements AttendService {

    /**
     * 中午12点 判定上下午
     */
    private static final int NOON_HOUR = 12;
    private static final int NOON_MINUTE = 00;
    /**
     * 缺席一整天
     */
    private static final Integer ABSENCE_DAY = 480;
    /**
     * 考勤异常状态
     */
    private static final Byte ATTEND_STATUS_ABNORMAL = 2;
    private static final Byte ATTEND_STATUS_NORMAL = 1;
    private static final int MORNING_HOUR = 9;
    private static final int MORNING_MINUTE = 30;
    private static final int EVENING_HOUR = 18;
    private static final int EVENING_MINUTE = 30;


    private Log log= LogFactory.getLog(AttendServiceImpl.class);

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    private AttendMapper attendMapper;



    @Override
    public void signAttend(Attend attend) throws Exception {
        try {
            Date today = new Date();
            attend.setAttendDate(today);
            attend.setAttendWeek((byte) DateUtils.getTadayWeek());
            //查询当天 此人有没有打卡记录
            Attend todayRecord = attendMapper.selectTodaySignRecord(attend.getUserId());

            Date noon = DateUtils.getDate(NOON_HOUR,NOON_MINUTE);
            Date morningAttend = DateUtils.getDate(MORNING_HOUR,MORNING_MINUTE);
            if(todayRecord==null){
                //打卡记录不存在
                if(today.compareTo(noon)<=0){
                    //打卡时间 早于12点上午打卡
                    attend.setAttendMorning(today);
                    if(today.compareTo(morningAttend)>0){
                        //打卡晚于九点半 迟到
                        attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                        attend.setAbsence(DateUtils.getMinute(morningAttend,today));
                    }
                }else{
                    //晚上打卡
                    attend.setAttendEvening(today);
                }
                attendMapper.insertSelective(attend);
            }else{
                if(today.compareTo(noon)<=0){
                    //打卡时间 早于12点上午打卡
                    return;
                }else{
                    //晚上打卡
                    attend.setAttendEvening(today);
                    Date eveningAttend = DateUtils.getDate(EVENING_HOUR,EVENING_MINUTE);
                    if(today.compareTo(eveningAttend)<0){
                        //早于六点半 早退
                        todayRecord.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                        todayRecord.setAbsence(DateUtils.getMinute(today,eveningAttend));
                    }else {
                        todayRecord.setAttendStatus(ATTEND_STATUS_NORMAL);
                        todayRecord.setAbsence(0);
                    }
                    attendMapper.updateByPrimaryKeySelective(todayRecord);
                }
            }
            //中午十二点之前打卡 都算是早上打卡 如果是9.30以后 直接异常 算迟到
            //十二点以后都算是下午打卡
            //下午打卡 检查与上午打卡时间差 不足八小时都算是异常 并且 缺席多长时间 要存进去

        }catch (Exception e) {
            log.error("用户签到异常",e);
            throw e;
        }
    }

    @Override
    public PageQueryBean listAttend(QueryCondition condition) {

        //根据查询条件 count记录数目
        int count = attendMapper.countByCondition(condition);
        PageQueryBean pageResult = new PageQueryBean();
        if(count>=0){
            pageResult.setTotalRows(count);
            pageResult.setCurrentPage(condition.getCurrentPage());
            pageResult.setPageSize(condition.getPageSize());
            List<Attend> attendList = attendMapper.selectAttendPage(condition);
            pageResult.setItems(attendList);
        }
        //如果有记录 才去查询分页数据 没有相关数据 没有必要去查分页数据
        //修改的测试
        return pageResult;
    }

    @Override
    @Transactional
    public void checkAttend() {

        //查询缺勤用户id 插入打卡记录 并设置为异常 缺勤480分钟
        List<Long> userIdList = attendMapper.selectTodayAbsence();
        if(CollectionUtils.isNotEmpty(userIdList)){
            List<Attend> attendList = new ArrayList<Attend>();
            for (Long userId:userIdList){
                Attend attend = new Attend();
                attend.setUserId(userId);
                attend.setAttendDate(new Date());
                attend.setAttendWeek((byte) DateUtils.getTadayWeek());
                attend.setAbsence(ABSENCE_DAY);
                attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                attendList.add(attend);
            }
            attendMapper.batchInsert(attendList);
        }
        //检查晚上打卡 将未打卡记录设为异常
        List<Attend> attendList = attendMapper.selectTodayEveningAbsence();
        if(CollectionUtils.isNotEmpty(attendList)){
            for(Attend attend :attendList){
                attend.setAbsence(ABSENCE_DAY);
                attend.setAttendStatus(ATTEND_STATUS_ABNORMAL);
                attendMapper.updateByPrimaryKeySelective(attend);
            }
        }
    }
}
