package com.example.demo.controller;

import com.example.demo.models.Tour;
import com.example.demo.models.User;
import com.example.demo.models.Cart;
import com.example.demo.repository.TourRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.CartRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

	private final TourRepository tourRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

	@Autowired
	public MainController(TourRepository tourRepository, UserRepository userRepository, CartRepository cartRepository){
		this.cartRepository = cartRepository;
		this.tourRepository = tourRepository;
		this.userRepository = userRepository;
	}
	@GetMapping("/")
	public String register(Model model) {
		return "register";
	}

	@GetMapping("/sign")
	public String sign(Model model){ 
		return "sign";
	}

	@PostMapping("/sign")
	public String sign(@RequestParam String name, 
		@RequestParam String email,
		@RequestParam String password,	
		Model model) {
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);
		try {
			userRepository.save(user);
        } catch (Exception e) {}
		return "sign";
	}

	@GetMapping("/tours")
	public String getTours(Model model) {
		List<Tour> tours = tourRepository.findAll();
		model.addAttribute("tours", tours);
		return "tours";
	}

	@PostMapping("/tours")
	public String tours(@RequestParam String email,
		@RequestParam String password, 
		HttpServletResponse response,
		Model model) {
		Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent()){
			User res = user.get();
			if (res.getPassword().equals(password)){
				Cookie cookie = new Cookie("userId", String.valueOf(res.getId()));
			
				cookie.setPath("/");
				cookie.setMaxAge(86400);
				response.addCookie(cookie);
				response.setContentType("text/plain");
				List<Tour> tours = tourRepository.findAll();
				model.addAttribute("tours", tours);
				return "tours";
			}
		}
		return "sign";
	}

	@GetMapping("/book/{tour_id}")
	public String tourAdd( @CookieValue(value = "userId") String data, @PathVariable(value = "tour_id") Long tourId, Model model) {
		Long userId = Long.valueOf(data);
		List<Cart> userTours = cartRepository.findByUserId(userId);
		boolean flag = false;

		for (Cart item : userTours) {
			if (item.getTourId().equals(tourId)){
				flag = true;
			}
		}
		if (!flag) {
			Cart cart = new Cart();
			cart.setTourId(tourId);
			cart.setUserId(userId);
			cartRepository.save(cart);
		}
		List<Tour> tours = tourRepository.findAll();
		model.addAttribute("tours", tours);
		return "tours";
	}

	@GetMapping("/delete/{tour_id}")
	public String tourUserDelete( @CookieValue(value = "userId") String data, @PathVariable(value = "tour_id") Long tourId, Model model) {
		Long userId = Long.valueOf(data);
		Optional<User> user = userRepository.findById(userId);

		List<Cart> cart = cartRepository.findByUserId(userId);
		List<Tour> tours = new ArrayList<Tour>();
		for (Cart item : cart) {
			Optional<Tour> tour = tourRepository.findById(item.getTourId());
			if (tour.isPresent()){
				if (tour.get().getId().equals(tourId)){
					cartRepository.delete(item);
				}
				tours.add(tour.get());
			}
		}
		if (user.isPresent()){
			model.addAttribute("user", user.get());
		}
		model.addAttribute("userTours", tours);
		return "profile";
	}


	@GetMapping("/profile")
	public String profile(Model model, @CookieValue(value = "userId") String data) {
		Long userId = Long.valueOf(data);
		Optional<User> user = userRepository.findById(userId);

		List<Cart> cart = cartRepository.findByUserId(userId);
		List<Tour> tours = new ArrayList<Tour>();
		for (Cart item : cart) {
			Optional<Tour> tour = tourRepository.findById(item.getTourId());
			if (tour.isPresent()){
				tours.add(tour.get());
			}
		}
		if (user.isPresent()){
			model.addAttribute("user", user.get());
		}
		model.addAttribute("userTours", tours);
		return "profile";
	}
}
