package com.cretasom.hello.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cretasom.hello.entity.User;
import com.cretasom.hello.repo.UserRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserRepository userRepository;
	@Value("${upload.dir}")
	private String UPLOAD_DIRECTORY;

	@GetMapping("/add")
	public String showForm(User user) {
		return "user/add";
	}

	@PostMapping("/adduser")
	public String addUser(@Valid User user, @RequestParam("image") MultipartFile file, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			return "user/add";
		}
		storeFile(file, user.getUserName());
		// user.setFilePath(f);
		userRepository.save(user);
		return "redirect:/user/index";
	}

	@GetMapping({ "/index", "" })
	public String showUserList(Model model) {
		List<User> users = userRepository.findAll();

		model.addAttribute("users", users.isEmpty() ? null : users);
		return "user/index";
	}

	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		model.addAttribute("user", user);
		return "user/update";
	}

	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable("id") long id, User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			user.setId(id);
			return "user/update";
		}

		userRepository.save(user);
		return "redirect:/user/index";
	}

	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		userRepository.delete(user);
		return "redirect:/user/index";
	}

	private void storeFile(MultipartFile file, String userName) {
		try {
			Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, userName);
			Files.write(fileNameAndPath, file.getBytes());

		} catch (Exception ex) {

		}
	}

	// additional CRUD methods
}