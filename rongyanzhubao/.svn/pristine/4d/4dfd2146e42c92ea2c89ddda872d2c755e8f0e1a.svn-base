package com.cn.jm.quatz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.cn.jm.service.JMNoService;

import com.jfinal.aop.Aop;
/**
 * 定时生成用户唯一编号
 * @author Administrator
 */
public class AccountNoJob implements Job{
    //现在以七位数的编号记录用户
    private final long maxCount = 100000L;
    //如果数量低于该值则生成用户编码
    private final long minCount = maxCount / 2;
    
    JMNoService noService = Aop.get(JMNoService.class);
    
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        Long noCount = noService.selectNoCount();
        if(noCount < minCount || noCount == null) {
            //获取当前数据库中no的最大值
            final Long oldMaxNoNumber = noService.selectNoMaxNumber();
            final Long minNoNumber = (oldMaxNoNumber==null?maxCount:oldMaxNoNumber + 1);
            //设置最新的最大值  = 当前设置的最大总数 - 当前数据库的总数  + 获取的最大值
            final Long addNumber = maxCount - noCount;
            if(addNumber > 0) {
                final Long nowMaxNoNumber = addNumber + minNoNumber ; 
                noService.savesNo(minNoNumber,nowMaxNoNumber);
            }
        }
    }
    
}