package com.codesoom.assignment.controllers;

import com.codesoom.assignment.application.AuthenticationService;
import com.codesoom.assignment.application.ProductService;
import com.codesoom.assignment.domain.Product;
import com.codesoom.assignment.dto.ProductData;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    private final ProductService productService;
    private final AuthenticationService authenticationService;

    public ProductController(
        final ProductService productService,
        final AuthenticationService authenticationService
    ) {
        this.productService = productService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public List<Product> list() {
        return productService.getProducts();
    }

    @GetMapping("{id}")
    public Product detail(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(
        @RequestHeader("Authorization") final String authorization,
        @RequestBody @Valid ProductData productData
    ) {
        final String accessToken = authorization.substring("Bearer ".length());
        final Long userId = authenticationService.parseToken(accessToken);
        return productService.createProduct(productData);
    }

    @PatchMapping("{id}")
    public Product update(
        @PathVariable final Long id,
        @RequestHeader("Authorization") final String authorization,
        @RequestBody @Valid final ProductData productData
    ) {
        final String accessToken = authorization.substring("Bearer ".length());
        final Long userId = authenticationService.parseToken(accessToken);
        return productService.updateProduct(id, productData);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(
        @PathVariable Long id,
        @RequestHeader("Authorization") final String authorization
    ) {
        final String accessToken = authorization.substring("Bearer ".length());
        final Long userId = authenticationService.parseToken(accessToken);
        productService.deleteProduct(id);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(MissingRequestHeaderException.class)
    public void handleMissingRequestHeader() {
    }
}
