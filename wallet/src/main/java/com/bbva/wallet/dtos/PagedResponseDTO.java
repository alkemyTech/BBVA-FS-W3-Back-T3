package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponseDTO<T> {
    private List<T> data;
    private int currentPage;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private String prevPageUri;
    private String nextPageUri;

    public PagedResponseDTO(List<Account> accounts, int page, int size, long totalElements, int totalPages) {
    }
}