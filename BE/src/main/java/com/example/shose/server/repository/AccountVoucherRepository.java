package com.example.shose.server.repository;

import com.example.shose.server.entity.AccountVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountVoucherRepository extends JpaRepository<AccountVoucher,String> {
}
