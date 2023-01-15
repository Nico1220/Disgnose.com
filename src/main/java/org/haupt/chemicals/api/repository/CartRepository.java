package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.Cart;
import org.haupt.chemicals.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c inner join User u on c.user=u.email where u.email like %?1%")
    public Cart findByUser(String email);
}
