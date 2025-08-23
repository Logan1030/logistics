# CounterController 完整架构学习指南

## 架构概览

CounterController 采用了经典的三层架构设计模式，实现了从HTTP请求到数据库存储的完整链路：

```
HTTP请求 → Controller → Service → DAO → MyBatis → MySQL数据库
```

## 完整链路分析

### 1. 请求入口层 (Controller Layer)

#### **CounterController.java**
```java
@RestController
public class CounterController {
  final CounterService counterService;
  final Logger logger;

  public CounterController(@Autowired CounterService counterService) {
    this.counterService = counterService;
    this.logger = LoggerFactory.getLogger(CounterController.class);
  }
}
```

**职责**：
- 接收HTTP请求
- 参数验证和业务逻辑处理
- 调用Service层方法
- 返回统一的API响应

**API接口**：
- `GET /api/count` - 获取当前计数
- `POST /api/count` - 更新计数（自增、乘10、清零）

**依赖注入**：
- 通过构造函数注入 `CounterService`
- 使用 `@Autowired` 注解自动装配

### 2. 业务逻辑层 (Service Layer)

#### **CounterService 接口**
```java
public interface CounterService {
  Optional<Counter> getCounter(Integer id);
  void upsertCount(Counter counter);
  void clearCount(Integer id);
}
```

#### **CounterServiceImpl 实现类**
```java
@Service
public class CounterServiceImpl implements CounterService {
  final CountersMapper countersMapper;

  public CounterServiceImpl(@Autowired CountersMapper countersMapper) {
    this.countersMapper = countersMapper;
  }

  @Override
  public Optional<Counter> getCounter(Integer id) {
    return Optional.ofNullable(countersMapper.getCounter(id));
  }

  @Override
  public void upsertCount(Counter counter) {
    countersMapper.upsertCount(counter);
  }

  @Override
  public void clearCount(Integer id) {
    countersMapper.clearCount(id);
  }
}
```

**职责**：
- 封装业务逻辑
- 调用DAO层方法
- 处理业务规则和异常
- 提供事务管理

**设计模式**：
- **接口隔离原则**: 定义清晰的Service接口
- **依赖倒置**: 依赖抽象而非具体实现
- **单一职责**: 每个方法职责明确

### 3. 数据访问层 (DAO Layer)

#### **CountersMapper 接口**
```java
@Mapper
public interface CountersMapper {
  Counter getCounter(@Param("id") Integer id);
  void upsertCount(Counter counter);
  void clearCount(@Param("id") Integer id);
}
```

**职责**：
- 定义数据访问方法
- 使用MyBatis注解
- 参数映射和类型转换

**MyBatis特性**：
- `@Mapper` 注解标识MyBatis映射器
- `@Param` 注解用于参数映射
- 方法名对应XML中的SQL ID

### 4. 数据持久化层 (MyBatis + MySQL)

#### **CountersMapper.xml**
```xml
<mapper namespace="com.tencent.wxcloudrun.dao.CountersMapper">
    <!-- 结果映射 -->
    <resultMap id="countersMapper" type="com.tencent.wxcloudrun.model.Counter">
        <id property="id" column="id"/>
        <result property="count" column="count"/>
        <result property="createdAt" column="createdAt" javaType="java.time.LocalDateTime" />
        <result property="updatedAt" column="updatedAt" javaType="java.time.LocalDateTime"  />
    </resultMap>

    <!-- 查询计数 -->
    <select id="getCounter" resultMap="countersMapper" parameterType="java.lang.Integer">
        SELECT `id`, `count`, `createdAt`, `updatedAt`
        FROM Counters
        WHERE id = #{id}
    </select>

    <!-- 删除计数 -->
    <delete id="clearCount" parameterType="java.lang.Integer">
        DELETE FROM Counters WHERE id = #{id} LIMIT 1
    </delete>

    <!-- 插入或更新计数 -->
    <update id="upsertCount" parameterType="com.tencent.wxcloudrun.model.Counter">
        INSERT INTO `Counters`(`id`, `count`)
        VALUE(#{id}, #{count}) 
        ON DUPLICATE KEY UPDATE count=#{count}
    </update>
</mapper>
```

**SQL特性**：
- **结果映射**: 使用 `resultMap` 映射数据库字段到Java对象
- **参数绑定**: 使用 `#{paramName}` 进行参数绑定
- **UPSERT操作**: 使用 `ON DUPLICATE KEY UPDATE` 实现插入或更新

### 5. 数据模型层 (Model Layer)

#### **Counter 实体类**
```java
@Data
public class Counter implements Serializable {
  private Integer id;
  private Integer count;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
```

**特性**：
- 使用Lombok `@Data` 注解自动生成getter/setter
- 实现 `Serializable` 接口支持序列化
- 包含业务字段和审计字段

#### **CounterRequest DTO类**
```java
@Data
public class CounterRequest {
  private String action;  // 动作类型：inc、multiply、clear
}
```

**职责**：
- 数据传输对象
- 封装请求参数
- 参数验证和类型转换

### 6. 响应封装层 (Response Layer)

#### **ApiResponse 统一响应类**
```java
@Data
public final class ApiResponse {
  private Integer code;      // 响应码
  private String errorMsg;   // 错误信息
  private Object data;       // 响应数据

  public static ApiResponse ok(Object data) {
    return new ApiResponse(0, "", data);
  }

  public static ApiResponse error(String errorMsg) {
    return new ApiResponse(0, errorMsg, new HashMap<>());
  }
}
```

**设计优势**：
- 统一的响应格式
- 静态工厂方法创建实例
- 不可变类设计

## 数据流转过程

### 1. 获取计数流程
```
GET /api/count
    ↓
CounterController.get()
    ↓
CounterService.getCounter(1)
    ↓
CountersMapper.getCounter(1)
    ↓
MyBatis执行SELECT查询
    ↓
MySQL返回数据
    ↓
MyBatis映射到Counter对象
    ↓
Service层包装为Optional
    ↓
Controller返回ApiResponse
```

### 2. 更新计数流程
```
POST /api/count {"action": "inc"}
    ↓
CounterController.create(request)
    ↓
验证action参数
    ↓
CounterService.getCounter(1) 获取当前值
    ↓
计算新值（当前值 + 10）
    ↓
创建Counter对象
    ↓
CounterService.upsertCount(counter)
    ↓
CountersMapper.upsertCount(counter)
    ↓
MyBatis执行UPSERT SQL
    ↓
MySQL更新数据
    ↓
返回新计数值
```

## 技术特点分析

### 1. **Spring Boot特性**
- **自动配置**: 自动配置数据源、MyBatis等
- **依赖注入**: 使用构造函数注入，避免循环依赖
- **注解驱动**: `@RestController`、`@Service`、`@Mapper`等

### 2. **MyBatis特性**
- **XML映射**: 使用XML文件定义SQL映射
- **结果映射**: 自动映射数据库字段到Java对象
- **参数绑定**: 安全的参数绑定，防止SQL注入

### 3. **数据库设计**
- **主键自增**: `id` 字段自动递增
- **时间戳**: `createdAt` 和 `updatedAt` 自动维护
- **UPSERT操作**: 支持插入或更新操作

### 4. **异常处理**
- **Optional包装**: 使用Optional避免空指针异常
- **参数验证**: 验证action参数的有效性
- **统一响应**: 使用ApiResponse封装响应结果

## 扩展和优化建议

### 1. **性能优化**
- 添加缓存机制（Redis）
- 使用连接池优化数据库连接
- 添加数据库索引

### 2. **功能增强**
- 支持多个计数器
- 添加计数历史记录
- 支持批量操作

### 3. **安全性提升**
- 添加参数验证注解
- 实现访问控制
- 添加操作日志

### 4. **监控和运维**
- 添加性能监控
- 实现健康检查
- 添加操作审计

## 学习要点总结

1. **分层架构**: 理解Controller-Service-DAO三层架构
2. **依赖注入**: 掌握Spring的依赖注入机制
3. **MyBatis使用**: 学习XML映射和结果映射
4. **数据库操作**: 理解UPSERT和CRUD操作
5. **异常处理**: 学会使用Optional和统一响应
6. **设计模式**: 理解接口隔离和依赖倒置原则

这个架构展示了现代Java Web应用的最佳实践，是学习Spring Boot + MyBatis的经典案例！
