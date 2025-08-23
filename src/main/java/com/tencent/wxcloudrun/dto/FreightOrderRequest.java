package com.tencent.wxcloudrun.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 运费订单请求DTO
 */
@Data
public class FreightOrderRequest {

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
     * 备注信息
     */
    private String remark;
}
