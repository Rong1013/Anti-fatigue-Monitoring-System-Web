package com.fatigue.monitoring.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fatigue.monitoring.common.exception.BusinessException;
import com.fatigue.monitoring.entity.Product;
import com.fatigue.monitoring.mapper.ProductMapper;
import com.fatigue.monitoring.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 产品服务实现类
 * 
 * @author Fatigue Monitoring System
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public Page<Product> selectProductPage(Page<Product> page, Product product) {
        return baseMapper.selectProductPage(page, product);
    }

    @Override
    public Product selectByProductCode(String productCode) {
        return baseMapper.selectByProductCode(productCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createProduct(Product product) {
        // 检查产品编号是否存在
        Product existProduct = selectByProductCode(product.getProductCode());
        if (existProduct != null) {
            throw new BusinessException("产品编号已存在");
        }

        // 设置状态为正常
        if (product.getStatus() == null) {
            product.setStatus(0);
        }

        return this.save(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProduct(Product product) {
        Product existProduct = this.getById(product.getId());
        if (existProduct == null) {
            throw new BusinessException("产品不存在");
        }

        return this.updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProduct(Long productId) {
        Product product = this.getById(productId);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }

        return this.removeById(productId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long productId, Integer status) {
        Product product = this.getById(productId);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }

        product.setStatus(status);
        return this.updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reduceQuantity(Long productId, Integer quantity) {
        Product product = this.getById(productId);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }

        // 检查数量是否足够
        if (product.getQuantity() < quantity) {
            throw new BusinessException("产品数量不足");
        }

        product.setQuantity(product.getQuantity() - quantity);
        return this.updateById(product);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addQuantity(Long productId, Integer quantity) {
        Product product = this.getById(productId);
        if (product == null) {
            throw new BusinessException("产品不存在");
        }

        product.setQuantity(product.getQuantity() + quantity);
        return this.updateById(product);
    }
}
