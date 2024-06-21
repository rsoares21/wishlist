package com.wishlist.controller.api;

import com.wishlist.dto.AddProductRequest;
import com.wishlist.exception.DuplicateProductException;
import com.wishlist.exception.FulllWishlistException;
import com.wishlist.exception.NotTheAccountOwnerException;
import com.wishlist.model.Wishlist;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RequestMapping("/wishlist")
public interface WishlistApi {

    @Operation(summary = "Inclui produto na wishlist do usuário informado")
    @PreAuthorize("hasRole('users')")
    @PostMapping("/{customerId}/add/{productId}")
    ResponseEntity<Wishlist> addProduct(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @Parameter(description = "Product ID") @PathVariable String productId)
            throws NotTheAccountOwnerException, DuplicateProductException, FulllWishlistException;

    @Operation(summary = "Inclui produto na wishlist do usuário informado usando RequestBody")
    @PreAuthorize("hasRole('vip')")
    @PostMapping("/add-product")
    ResponseEntity<Wishlist> addProductRequest(
            @RequestBody AddProductRequest request)
            throws NotTheAccountOwnerException, DuplicateProductException, FulllWishlistException;

    @Operation(summary = "Remove produto da wishlist do usuário informado")
    @PreAuthorize("hasRole('users')")
    @DeleteMapping("/{customerId}/remove/{productId}")
    ResponseEntity<Wishlist> removeProduct(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @Parameter(description = "Product ID") @PathVariable String productId)
            throws NotTheAccountOwnerException, FulllWishlistException;

    @Operation(summary = "Busca wishlist do usuário informado")
    @PreAuthorize("hasAnyRole('default-roles-lab','users')")
    @GetMapping("/{customerId}")
    ResponseEntity<Wishlist> getWishlist(
            @Parameter(description = "Customer ID") @PathVariable String customerId)
            throws NotTheAccountOwnerException;

    @Operation(summary = "Verifica se o produto informado está na wishlist do usuário informado")
    @PreAuthorize("hasRole('users')")
    @GetMapping("/{customerId}/contains/{productId}")
    ResponseEntity<Boolean> isProductInWishlist(
            @Parameter(description = "Customer ID") @PathVariable String customerId,
            @Parameter(description = "Product ID") @PathVariable String productId)
            throws NotTheAccountOwnerException;

    @Operation(summary = "API Pública. Não requer autenticação do SSO")
    @GetMapping("/public")
    ResponseEntity<String> publicApi();
}
