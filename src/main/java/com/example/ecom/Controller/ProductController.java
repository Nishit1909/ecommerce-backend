package com.example.ecom.Controller;

import com.example.ecom.model.Product;
import com.example.ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("/products")
    public List<Product> getAllProducts()
    {
        return service.getAllProducts();
    }
    @GetMapping("/product/{id}")
    public Product getProductbyId(@PathVariable int id)
    {
        return service.getProductbyId(id);
    }
    @GetMapping("/product/{id}/image")
    public byte[] getimageById(@PathVariable("id") int productId)
    {
        Product prod=service.getProductbyId(productId);
        return prod.getImageData();
    }
    @PostMapping("/product")
    public Product addOrupdateProduct(@RequestPart("product") Product product, @RequestPart("imageFile") MultipartFile imageFile){
        try {
            return service.addOrupdateProduct(product,imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PutMapping("/product/{id}")
    public Product addOrupdateProduct(@PathVariable int id ,@RequestPart("product") Product product, @RequestPart("imageFile") MultipartFile imageFile){
        try {
            return service.addOrupdateProduct(product,imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id)
    {
        Product prod=service.getProductbyId(id);
        if(prod!=null)
        {
            service.deleteProduct(id);
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }
    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = service.searchProducts(keyword);
        
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
