
# Spring Boot Application Deployment to AWS EKS

This project demonstrates how to deploy a Spring Boot application to AWS EKS using Docker, Kubernetes, and Helm.

## Prerequisites

- Docker
- Kubernetes CLI (kubectl)
- AWS CLI
- Helm
- Maven or Gradle




## architect

## architecture
+-------------------+       +-------------------+       +-------------------+
|                   |       |                   |       |                   |
|   API Gateway     +------>+   Spring Boot     +------>+   AWS RDS         |
|                   |       |   Application     |       | (Managed Database)|
+-------------------+       |  (on EKS)         |       |                   |
+--------+----------+       +-------------------+
                                       |
                                       |
                                       v
                           +-------------------+
                           | Alibaba Redis     |
                           | (Distributed Cache)|
                           +-------------------+


## Project Structure
```
### 目录结构说明

- **Dockfile**：Dockerfile 文件，用于构建 Docker 镜像。
- **README.md**：项目的 README 文件。
- **build_and_push.sh**：构建和推送 Docker 镜像的脚本。
- **helm**：Helm Chart 目录。
   - **Chart.yaml**：Helm Chart 的元数据文件。
   - **templates**：Helm Chart 的模板文件。
      - **deployment.yaml**：Kubernetes Deployment 模板。
      - **service.yaml**：Kubernetes Service 模板。
   - **values.yaml**：Helm Chart 的默认值文件。
- **k8s**：Kubernetes YAML 文件目录。
   - **deployment.yaml**：Kubernetes Deployment 文件。
   - **service.yaml**：Kubernetes Service 文件。
- **pom.xml**：Maven 项目的 POM 文件。
- **regress**：回归测试脚本目录。
   - **integration.sh**：集成测试脚本。
- **src**：源代码目录。
   - **main**：主代码目录。
      - **java**：Java 源代码目录。
         - **com.example.controller**：控制器包。
            - **Application.java**：应用程序入口类。
            - **Main.java**：主类。
            - **transaction**：事务相关代码包。
               - **config**：配置类包。
                  - **CustomRedisSerializer.java**：自定义 Redis 序列化类。
                  - **RedisConfig.java**：Redis 配置类。
               - **controller**：控制器类包。
                  - **TransactionController.java**：事务控制器类。
                  - **UtilityController.java**：工具控制器类。
               - **mapper**：Mapper 类包。
                  - **BalanceMapper.java**：余额 Mapper 类。
                  - **TransactionMapper.java**：事务 Mapper 类。
               - **model**：模型类包。
                  - **Balance.java**：余额模型类。
                  - **Transaction.java**：事务模型类。
               - **repository**：存储库类包。
               - **service**：服务类包。
                  - **TransactionService.java**：事务服务类。
      - **resources**：资源文件目录。
         - **application.yml**：Spring Boot 配置文件。
         - **db**：数据库文件目录。
            - **data-mysql.sql**：MySQL 数据文件。
            - **schema-mysql.sql**：MySQL 模式文件。
   - **test**：测试代码目录。
      - **java**：Java 测试代码目录。
         - **com.example.controller**：控制器测试包。
            - **SampleTest.java**：示例测试类。
- **target**：Maven 构建输出目录。
```

## Using the Spring Boot Application

### Running Locally

1. **Build the application**:

    ```sh
    mvn clean package
    ```

2. **Run the application**:

    ```sh
    java -jar target/transaction-service-1.0.0.jar
    ```

3. **Access the application**:

    By default, the application will be available at `http://localhost:8080`.

### Configuration

Spring Boot applications can be configured using `application.properties` or `application.yml` files located in the `src/main/resources` directory. You can also use environment variables to override default configurations.

Example `application.properties`:

```yml
server.port=8080
spring.datasource.url=jdbc:mysql://database-1.cj2igaoyci8u.us-east-1.rds.amazonaws.com:3306/transaction_db
spring.datasource.username=admin
spring.datasource.password=admin1234
spring.redis.host=r-wz92pnydc518hrgzfapd.redis.rds.aliyuncs.com
spring.redis.port=6379
```

## Build and Push Docker Image

1. **Build the Spring Boot application**:

    ```sh
    mvn clean package
    ```

2. **Build and push the Docker image**:

    ```sh
    ./build_and_push.sh
    ```

## Deploy to AWS EKS

### Using kubectl

1. **Apply the Kubernetes deployment and service**:

    ```sh
    kubectl apply -f k8s/deployment.yaml
    kubectl apply -f k8s/service.yaml
    kubectl apply -f k8s/hpa.yaml
    ```

### Using Helm

1. **Install the Helm chart**:

    ```sh
    helm install app helm/
    ```

## Access the Application

After deployment, get the external IP address of the service:

```sh
kubectl get services
```

Use the external IP address to access your application.

## Clean Up

To delete the deployment and service:

### Using kubectl

```sh
kubectl delete -f k8s/deployment.yaml
kubectl delete -f k8s/service.yaml
```

### Using Helm

```sh
helm uninstall app
```






### Retry Mechanism and Idempotency

The application implements a retry mechanism to ensure idempotency and consistency. This is achieved using Spring Retry.

Example configuration in a service class:

```java
@Service
public class TransactionService {

    @Retryable(
        value = { SQLException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000)
    )
    public void processTransaction(Transaction transaction) throws SQLException {
        // Process the transaction
    }

    @Recover
    public void recover(SQLException e, Transaction transaction) {
        // Handle the failure
    }
}
```

### Caching with Redis

To avoid querying the database multiple times for the same information, the application uses Redis for caching.

Example usage in a service class:

```java
@Service
public class TransactionService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Transaction getTransaction(String transactionId) {
        Transaction transaction = (Transaction) redisTemplate.opsForValue().get(transactionId);
        if (transaction == null) {
            transaction = transactionRepository.findById(transactionId).orElse(null);
            if (transaction != null) {
                redisTemplate.opsForValue().set(transactionId, transaction);
            }
        }
        return transaction;
    }
}
```

### unit test
1. SampleTest 

### integration test
1. insert multi users
2. retry with endpoints with correct endpoints
3. retry with endpoints with failed endpoints (network issue, database connection issue)
integration.sh

### resilience test
1. HPA
2. pod expand/shink

### Performance Testing with JMeter

To test the performance of your API endpoints, you can use Apache JMeter. For example, you can use JMeter to call the `/transactions` API.

1. **Create a JMeter test plan**:

    - Add a Thread Group.
    - Add an HTTP Request sampler to the Thread Group.
    - Configure the HTTP Request sampler to point to your `/transactions` endpoint.

2. **Run the test**:

    - Execute the test plan and monitor the results to evaluate the performance of the `/transactions` endpoint.

## Conclusion

This README provides a comprehensive guide to building, configuring, and deploying a Spring Boot application to AWS EKS using Docker, Kubernetes, and Helm. It also includes information on using AWS RDS and ElastiCache, implementing a retry mechanism, caching, and performance testing with JMeter. Follow the steps outlined above to get your application up and running in the cloud.

