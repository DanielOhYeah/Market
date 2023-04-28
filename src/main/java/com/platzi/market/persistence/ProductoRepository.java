package com.platzi.market.persistence;

import com.platzi.market.domain.Product;
import com.platzi.market.domain.repository.ProductRepository;
import com.platzi.market.persistence.crud.ProductoCrudRepository;
import com.platzi.market.persistence.entity.Producto;
import com.platzi.market.persistence.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductoRepository implements ProductRepository {
    @Autowired
    private ProductoCrudRepository productoCrudRepository;
    @Autowired
    private ProductMapper mapper;
    public List<Product> getAll(){
        List<Producto> productos = (List<Producto>) productoCrudRepository.findAll();
        return mapper.toProducts(productos);
    }

    @Override
    public Optional<List<Product>> getByCategory(int categoryId) {
        List<Producto> productos = productoCrudRepository.findByIdCategoriaOrderByNombreAsc(categoryId);
        return Optional.of(mapper.toProducts(productos));
    }

    @Override
    public Optional<List<Product>> getScarseProducts(int quantity) {
        return productoCrudRepository.findByCantidadStockLessThanAndEstado(quantity, true)
                .map(prods -> mapper.toProducts(prods));
    }

    @Override
    public Optional<Product> getProduct(int productId) {
        return productoCrudRepository.
                findById(productId).map(product -> mapper.toProduct(product));
    }

    @Override
    public Product save(Product productId) {
        Producto producto = mapper.toProducto(productId);
        return mapper.toProduct(productoCrudRepository.save(producto));
    }

    public Optional<List<Producto>> getEscasos (int cantidad) {
        return productoCrudRepository.findByCantidadStockLessThanAndEstado(cantidad, true);
    }

    public Optional<List<Producto>> getProductsOfCategoriaByPrice(int idCategoria, boolean select){
        if (select) {
        return Optional.ofNullable(productoCrudRepository.findByIdCategoriaOrderByPrecioVentaAsc(idCategoria));
        } else {
        return Optional.ofNullable(productoCrudRepository.findByIdCategoriaOrderByPrecioVentaDesc(idCategoria));
        }
    }



    @Override
    public void delete (int productId) {
        productoCrudRepository.deleteById(productId);
    }


    public ProductoCrudRepository getProductoCrudRepository() {
        return productoCrudRepository;
    }

    public void setProductoCrudRepository(ProductoCrudRepository productoCrudRepository) {
        this.productoCrudRepository = productoCrudRepository;
    }

    public ProductMapper getMapper() {
        return mapper;
    }

    public void setMapper(ProductMapper mapper) {
        this.mapper = mapper;
    }


}
