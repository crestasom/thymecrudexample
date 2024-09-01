package com.cretasom.hello.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ImageController {
	@Value("${upload.dir}")
	private String UPLOAD_DIRECTORY;

	@GetMapping("/show-image")
	public ResponseEntity<Resource> getImage(@RequestParam String name) {
		try {
			// Path should be a valid file system path
			Path filePath = Paths.get(UPLOAD_DIRECTORY, name);
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists() || resource.isReadable()) {
				return ResponseEntity.ok().body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (MalformedURLException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
