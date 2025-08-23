package com.tencent.wxcloudrun.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 运费订单详情实体类
 */
@Data
public class FreightOrderDetail implements Serializable {

    private Long id;
    
    /**
     * 订单ID
     */
    private Long orderId;
    
    /**
     * 详情类型：weight-重量 type-类型 vehicle-车型 freight-运费
     */
    private String detailType;
    
    /**
     * 详情键
     */
    private String detailKey;
    
    /**
     * 详情值
     */
    private String detailValue;
    
    /**
     * 单位
     */
    private String detailUnit;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
