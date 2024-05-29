package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.*;
import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.repository.FolderRepository;
import com.sparta.myselectshop.repository.ProductFolderRepository;
import com.sparta.myselectshop.repository.ProductRepository;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final FolderRepository folderRepository;
    private final ProductFolderRepository productFolderRepository;

    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto requesetDto, User user) {
        Product product = productRepository.save(new Product(requesetDto, user));
        return new ProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductMypriceRequestDto requesetDto) {
        int myprice = requesetDto.getMyprice();
        if (myprice < MIN_MY_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 " + MIN_MY_PRICE + "원 이상으로 설정해주세요");
        }
        Product product = productRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 상품을 찾을수 없습니다.")
        );

        product.update(requesetDto);

        return new ProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(User user, int page, int size, String sortBy, Boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        UserRoleEnum userRoleEnum = user.getRole();

        Page<Product> productList;

        if (userRoleEnum == UserRoleEnum.USER) {
            productList = productRepository.findAllByUser(user, pageable);
        } else {
            productList = productRepository.findAll(pageable);

        }
        return productList.map(ProductResponseDto::new);

    }


    public List<ProductResponseDto> getAllproducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Product product : productList) {
            responseDtoList.add(new ProductResponseDto(product));
        }
        return responseDtoList;
    }

    @Transactional
    public void updateBySearch(Long id, ItemDto itemDto) {
        Product procut = productRepository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 상품은 존재하지 않습니다"));
        procut.updateByItemDto(itemDto);
    }

    public void addFolder(Long productId, Long folderId, User user) {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NullPointerException("해당 상품이 존재하지 않습니다.")
        );

        Folder folder = folderRepository.findById(folderId).orElseThrow(
                () -> new NullPointerException("해당 폴더가 존재하지 않습니다.")
        );

        if (!product.getUser().getId().equals(user.getId())
        || !folder.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("회원님의 관심상품이 아니거나, 회원님의 폴더가 아닙니다");
        }

        Optional<ProductFolder> overLapFolder = productRepository.findByproductAndFolder(product, folder);

        if (overLapFolder.isPresent()) {
            throw new IllegalArgumentException("중복된 폴더입니다.");
        }

        productFolderRepository.save(new ProductFolder(product, folder));

    }
}