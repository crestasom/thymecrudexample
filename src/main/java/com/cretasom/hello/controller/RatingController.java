package com.cretasom.hello.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cretasom.hello.entity.Hotel;
import com.cretasom.hello.entity.Rating;
import com.cretasom.hello.entity.User;
import com.cretasom.hello.repo.HotelRepository;
import com.cretasom.hello.repo.RatingRepository;
import com.cretasom.hello.repo.UserRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/rating")
public class RatingController {
	@Autowired
	RatingRepository ratingRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	HotelRepository hotelRepository;

	@GetMapping("/add")
	public String showSignUpForm(Rating rating, Model model) {
		// List<User> users = userRepository.findAll();
		List<Hotel> hotels = hotelRepository.findAll();
		// model.addAttribute("users", users);
		model.addAttribute("hotels", hotels);
		return "rating/add";
	}

	@PostMapping("/addrating")
	public String addRating(@Valid Rating rating, BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "rating/add";
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userName = auth.getName();
		User u = userRepository.findByUserName(userName);
		Hotel h = hotelRepository.findById(rating.getHotelId()).get();

		rating.setHotel(h);
		rating.setUser(u);
		ratingRepository.save(rating);
		return "redirect:/rating/index";
	}

	@GetMapping({ "/index", "" })
	public String showRatingList(Model model) {
		List<Rating> ratings = ratingRepository.findAll();
		model.addAttribute("ratings", ratings.isEmpty() ? null : ratings);
		return "rating/index";
	}

	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Rating rating = ratingRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid rating Id:" + id));

		model.addAttribute("rating", rating);
		return "rating/update";
	}

	@PostMapping("/update/{id}")
	public String updateRating(@PathVariable("id") long id, Rating rating, BindingResult result, Model model) {
		if (result.hasErrors()) {
			rating.setId(id);
			return "rating/update";
		}

		ratingRepository.save(rating);
		return "redirect:/rating/index";
	}

	@GetMapping("/delete/{id}")
	public String deleteRating(@PathVariable("id") long id, Model model) {
		Rating rating = ratingRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid rating Id:" + id));
		ratingRepository.delete(rating);
		return "redirect:/rating/index";
	}

	// additional CRUD methods
}