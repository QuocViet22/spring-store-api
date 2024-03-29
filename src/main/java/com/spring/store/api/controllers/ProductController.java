package com.spring.store.api.controllers;

import com.spring.store.api.exception.ResourceNotFoundException;
import com.spring.store.api.models.Product;
import com.spring.store.api.payload.request.FilterProductRequest;
import com.spring.store.api.payload.request.FilterRequest;
import com.spring.store.api.payload.response.AIResponse;
import com.spring.store.api.payload.response.DetailProductResponse;
import com.spring.store.api.payload.response.MessageResponse;
import com.spring.store.api.projection.IRecommendProduct;
import com.spring.store.api.repository.CategoryRepository;
import com.spring.store.api.repository.ProductRepository;
import com.spring.store.api.repository.SizeRepository;
import com.spring.store.api.specifications.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    ProductSpecification productSpecification;

    @GetMapping("/products/multiple/filter")
    public ResponseEntity<List<Product>> multipleFilter(@RequestBody List<FilterRequest> filterRequests, @RequestParam int category_id, @RequestParam float size) {
        List<Product> products = productSpecification.getQueryResult(filterRequests);
        List<Product> productView = new ArrayList<>();
//        for (Product i : products) {
//            if (i.getCategory().getId().intValue() == category_id) {
//                for (ProductInfor p : i.getProductInfors()) {
//                    if (p.getSize().equals((Math.round(size))) {
//                        System.out.println("A");
//                        productView.add(i);
//                    }
//                }
//            }
//        }
        return new ResponseEntity<>(productView, HttpStatus.OK);
    }

    //    get all products
    @GetMapping("/products")
    //  @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
        //        return productRepository.findAllProduct();
    }

    //    get all products
    @GetMapping("/products/name")
    //  @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public List<Product> getAllProductsByName(@RequestParam String name) {
        return productRepository.findProductsByName(name);
        //        return productRepository.findAllProduct();
    }

    //    get all products pageable
    @GetMapping("/products/pageable")
    public Page<Product> getAllProductsPageable(@RequestParam int page,
                                                @RequestParam int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("status").descending());
        return productRepository.findAll(paging);
    }

    //    get all products by size
    @GetMapping("/products/{size}/size")
    //  @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public List<Product> getAllProductsBySize(@PathVariable(value = "size") String size) {
        return productRepository.findProductBySize(size);
        //        return productRepository.findAllProduct();
    }

    @PostMapping("/products/filter")
    public List<Product> getAllProductsBySizeAndPrice(@RequestBody FilterProductRequest filterProductRequest) {
        if (filterProductRequest.getSize() == null && filterProductRequest.getPrice() == 0)
            return productRepository.findAll();
        else if (filterProductRequest.getSize() == null) {
            return productRepository.findProductByPrice(filterProductRequest.getPrice());
        } else if (filterProductRequest.getPrice() == 0) {
            return productRepository.findProductBySize(filterProductRequest.getSize());
        } else
            return productRepository.findProductByPriceAndSize(filterProductRequest.getPrice(), filterProductRequest.getSize());
    }

    //    retrieve all Products of a Category
    @GetMapping("/category/{categoryId}/products")
    // @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Product>> getAllProductsByCategoryId(@PathVariable(value = "categoryId") Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Not found Category with id = " + categoryId);
        }
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //    retrieve a Product by product_id
    @GetMapping("/product/{id}")
    //    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<DetailProductResponse> getProductsByCategoryId(@PathVariable(value = "id") Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Product with id = " + id));
        DetailProductResponse detailProductResponse = new DetailProductResponse();
        Map<Integer, IRecommendProduct> map = new HashMap<>();
        List<IRecommendProduct> list;
        if (product.getStatus().equals("0")) {
            list = null;
        } else {
            String productName = product.getName();
            ResponseEntity<AIResponse> entity = restTemplate.getForEntity("/?name=" + productName, AIResponse.class);
            String recommend_products = entity.getBody().getRecommend_products();
            String parts[] = recommend_products.split(",");
                List<IRecommendProduct> iRecommendProducts = productRepository.recommendProducts(Long.parseLong(parts[0]), Long.parseLong(parts[1]), Long.parseLong(parts[2]), Long.parseLong(parts[3]), Long.parseLong(parts[4]));
            for (int i = 0; i < iRecommendProducts.size(); i++) {
                if (!map.containsKey(iRecommendProducts.get(i).getId().intValue())) {
                    map.put(iRecommendProducts.get(i).getId().intValue(), iRecommendProducts.get(i));
                }
            }
            list = new ArrayList<IRecommendProduct>(map.values());
        }
        detailProductResponse.setProduct(product);
        detailProductResponse.setIRecommendProducts(list);
        return new ResponseEntity<>(detailProductResponse, HttpStatus.OK);
    }

    //    get all products pageable
    //  @GetMapping("/products/active")
    //  public List<Product> getAllActiveProducts() {
    //  return productRepository.getActiveProducts();
    //}

    //    create new Product of a Category
    @PostMapping("/category/{categoryId}/products")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@PathVariable(value = "categoryId") Long categoryId,
                                                 @RequestBody Product productRequest) {
        Product product = categoryRepository.findById(categoryId).map(category -> {
            productRequest.setCategory(category);
            if (productRepository.existsByName(productRequest.getName())) {
                throw new ResourceNotFoundException("This product's name has been existed!");
            }
            if (Integer.valueOf(productRequest.getPrice()) < 0) {
                throw new ResourceNotFoundException("Price is not valid!");
            }
            return productRepository.save(productRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Category with id = " + categoryId));
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    //    update a product by product_id
    @PutMapping("/product/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable("id") long id, @RequestBody Product productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Id " + id + "not found"));
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setStatus(productRequest.getStatus());
        product.setDescription(productRequest.getDescription());
        product.setCreatedBy(productRequest.getCreatedBy());
        product.setCreatedDate(productRequest.getCreatedDate());
        product.setModifiedBy(productRequest.getModifiedBy());
        product.setModifiedDate(productRequest.getModifiedDate());
        productRepository.save(product);
        return ResponseEntity.ok().body(new MessageResponse("Product has been updated successfully!"));
    }
}
