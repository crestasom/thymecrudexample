package com.cretasom.hello.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cretasom.hello.entity.Hotel;
import com.cretasom.hello.repo.HotelRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/hotel")
public class HotelController {
	@Autowired
	HotelRepository hotelRepository;

	@GetMapping("/add")
	public String showHotelForm(Hotel hotel) {
		return "hotel/add";
	}

	@PostMapping("/addhotel")
	public String addHotel(@Valid Hotel hotel, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "hotel/add";
		}

		hotelRepository.save(hotel);
		return "redirect:/hotel/index";
	}

	@GetMapping({ "/index", "" })
	public String showHotelList(Model model) {
		List<Hotel> hotels = hotelRepository.findAll();
		model.addAttribute("hotels", hotels.isEmpty() ? null : hotels);
		return "hotel/index";
	}

	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		Hotel hotel = hotelRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

		model.addAttribute("hotel", hotel);
		return "hotel/update";
	}

	@PostMapping("/update/{id}")
	public String updateHotel(@PathVariable("id") long id, Hotel hotel, BindingResult result, Model model) {
		if (result.hasErrors()) {
			hotel.setId(id);
			return "hotel/update";
		}

		hotelRepository.save(hotel);
		return "redirect:/hotel/index";
	}

	@GetMapping("/delete/{id}")
	public String deleteHotel(@PathVariable("id") long id, Model model) {
		Hotel hotel = hotelRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid hotel Id:" + id));
		hotelRepository.delete(hotel);
		return "redirect:/hotel/index";
	}

	// additional CRUD methods
}