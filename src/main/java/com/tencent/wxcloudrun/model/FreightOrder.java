package com.tencent.wxcloudrun.model;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 运费订单实体类
 */
@Data
public class FreightOrder implements Serializable {

    private Long id;
    
    /**
     * 订单编号
     */
    private String orderNo;
    
    /**
     * 发货城市
     */
    private String senderCity;
    
    /**
     * 收货城市
     */
    private String receiverCity;
    
    /**
     * 收货人姓名
     */
    private String receiverName;
    
    /**
     * 货物代码
     */
    private String cargoCode;
    
    /**
     * 货物类型
     */
    private String cargoType;
    
    /**
     * 车型信息
     */
    private String vehicleType;
    
    /**
     * 运费结果
     */
    private String freightResult;
    
    /**
     * 计费重量
     */
    private String billingWeight;
    
    /**
     * 费用统计详情(JSON格式)
     */
    private String costStatistics;
    
    /**
     * 总费用
     */
    private BigDecimal totalAmount;
    
    /**
     * 订单状态：1-待确认 2-已确认 3-运输中 4-已完成 5-已取消
     */
    private Integer status;
    
    /**
     * 备注信息
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
