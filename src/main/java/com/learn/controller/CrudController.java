package com.learn.controller;

import static java.util.Collections.emptyList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learn.entity.Product;
import com.learn.repository.ProductRepository;

@RestController
@CrossOrigin("*")
public class CrudController {

	@Autowired
	private ProductRepository repo;

	@PostMapping("/product")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		Map<String, Object> map = new HashMap<>();
		Product save = repo.save(product);
		if (save == null) {
			map.put("Status", true);
			map.put("Data", product);
			map.put("Message", "Product Not Saved!!");
			return ResponseEntity.ok(map);
		} else {
			map.put("Status", true);
			map.put("Data", save);
			map.put("Message", "Product Saved Successfully!!");
			return ResponseEntity.ok(map);
		}
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<?> getProduct(@PathVariable Long productId) {
		Map<String, Object> map = new HashMap<>();
		Optional<Product> byId = repo.findById(productId);
		if (byId.isPresent()) {
			map.put("Status", true);
			map.put("Data", byId.get());
			return ResponseEntity.ok(map);
		} else {
			map.put("Status", false);
			map.put("Data", null);
			map.put("Message", "No Product Found with Id: " + productId);
			return ResponseEntity.ok(map);
		}
	}

	@PutMapping("/product/{productId}")
	public ResponseEntity<?> updateProduct(@RequestBody Product product, @PathVariable Long productId) {
		Map<String, Object> map = new HashMap<>();

		Optional<Product> byId = repo.findById(productId);
		if (byId.isPresent()) {
			Product oldProduct = byId.get();
			oldProduct.setProductName(product.getProductName());
			oldProduct.setType(product.getType());
			oldProduct.setQuantity(product.getQuantity());
			oldProduct.setPrice(product.getPrice());
			Product save = repo.save(oldProduct);
			if (save == null) {
				map.put("Status", true);
				map.put("Data", product);
				map.put("Message", "Product Not Saved!!");
				return ResponseEntity.ok(map);
			} else {
				map.put("Status", true);
				map.put("Data", save);
				map.put("Message", "Product Updated Successfully!!");
				return ResponseEntity.ok(map);
			}
		}
		return null;
	}
	
	@DeleteMapping("/product/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
		Map<String, Object> map = new HashMap<>();
            repo.deleteById(productId);
			map.put("Status", true);
			map.put("Data", null);
			map.put("Message", "Product Deleted Successfully!!");
			return ResponseEntity.ok(map);
	}
	
	@GetMapping("/product")
	public ResponseEntity<?> getAllProduct() {
		Map<String, Object> map = new HashMap<>();
		List<Product> all = repo.findAll();
		if (all.isEmpty()) {
			map.put("Status", false);
			map.put("Data", emptyList());
			map.put("Message", "No Product Avaliable");
			return ResponseEntity.ok(map);
		} else {
			map.put("Status", true);
			map.put("Data", all);
			return ResponseEntity.ok(map);
		}
	}
}
