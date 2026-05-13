package ynu.edu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long orderId;
    private Integer userId;
    private List<String> productList;
    private Double totalAmount;
    private String status;
    private Date createTime;
}