package org.springframework.samples.petclinic.user;

import jakarta.validation.Valid;
import org.springframework.samples.petclinic.school.School;
import org.springframework.samples.petclinic.school.SchoolRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;

import java.util.Optional;

@Controller
public class AuthController {

	private final UserService userService;

	private final SchoolRepository schoolRepository;

	private final AuthenticationManager authenticationManager;

	public AuthController(UserService userService, SchoolRepository schoolRepository,
			AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.schoolRepository = schoolRepository;
		this.authenticationManager = authenticationManager;
	}

	@GetMapping("/register")
	public String initRegisterForm(Model model) {
		model.addAttribute("user", new User());
		return "auth/registerForm";
	}

	@PostMapping("/register")
	public String processRegisterForm(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "auth/registerForm";
		}

		String rawPassword = user.getPassword();

		// 1. Save the User (UserService handles password hashing)
		try {
			userService.registerNewUser(user);
		}
		catch (RuntimeException ex) {
			// Handle duplicate email or other service errors
			result.rejectValue("email", "duplicate", "This email is already registered");
			return "auth/registerForm";
		}

		// 2. LOGIN using the authenticationManager.
		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getEmail(),
					rawPassword);
			Authentication authentication = authenticationManager.authenticate(authToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		catch (Exception e) {
			redirectAttributes.addFlashAttribute("messageDanger", "Account created, but auto-login failed.");
			return "redirect:/login";
		}

		// 3. REDIRECT
		Optional<School> school = findSchoolByRecursiveDomain(user.getEmail());

		if (school.isPresent()) {
			// 1. Set the Success Message
			redirectAttributes.addFlashAttribute("messageSuccess",
					"Your user account is created. You have been redirected to " + school.get().getName()
							+ "'s school page.");
			return "redirect:/schools/" + school.get().getId();
		}
		else {
			// 1b. Set a Generic Message (Warn them since they didn't match a school)
			redirectAttributes.addFlashAttribute("messageWarning",
					"Your user account is created, but we could not find a school matching your email domain.");
			// Fallback if no school matches the email domain
			return "redirect:/";
		}
	}

	@GetMapping("/login")
	public String initLoginForm() {
		return "auth/loginForm";
	}

	private Optional<School> findSchoolByRecursiveDomain(String email) {
		// 1. Extract the initial domain (e.g., "student.kirkwood.edu")
		String domain = email.substring(email.indexOf("@") + 1);

		// 2. Loop while the domain is valid (has at least one dot)
		while (domain.contains(".")) {
			// 3. Check Database
			Optional<School> school = schoolRepository.findByDomain(domain);
			if (school.isPresent()) {
				return school; // Found match (e.g., "kirkwood.edu")
			}

			// 4. Strip the first part (e.g., "student.kirkwood.edu" -> "kirkwood.edu")
			int dotIndex = domain.indexOf(".");
			domain = domain.substring(dotIndex + 1);
		}

		return Optional.empty();
	}

}
