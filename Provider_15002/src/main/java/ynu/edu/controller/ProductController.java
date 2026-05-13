package ynu.edu.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ynu.edu.entity.Product;

@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/getProductById/{productId}")
    public Product getProductById(@PathVariable("productId") Integer productId) {
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("苹果手机");
        product.setPrice(5999.00);
        product.setStock(100);
        product.setDescription("最新款智能手机，支持5G网络");

        return product;
    }
}