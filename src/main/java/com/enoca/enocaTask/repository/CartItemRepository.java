package com.enoca.enocaTask.repository;

import com.enoca.enocaTask.entity.Cart;
import com.enoca.enocaTask.entity.CartItem;
import com.enoca.enocaTask.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByProductAndCart(Product product, Cart cart);

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}
