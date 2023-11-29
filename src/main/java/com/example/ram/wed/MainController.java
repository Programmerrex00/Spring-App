package com.example.ram.wed;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ram.repository.ServiciosRepository;
import com.example.ram.repository.UserRepository;
import com.example.ram.web.dto.UserRegistrationDto;
import com.example.ram.model.Role;
import com.example.ram.model.Servicios;
import com.example.ram.model.User;
@Controller
public class MainController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ServiciosRepository serviciosRepository;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	
	@GetMapping("/conocenos")
	public String conocenos() {
		return "conocenos";
	}
	
	
	
	@GetMapping("/")
	public String home(Model model) {
		List<Servicios> listadoServicios = serviciosRepository.findAll();
		model.addAttribute("servicios", listadoServicios);
		return "indexx";
	}
	

    @GetMapping("/index") 
    public String index(Model model, @RequestParam("email") String email) {
    	List<Servicios> listadoServicios = serviciosRepository.findAll();
    	User user = userRepository.findByEmail(email);
    	model.addAttribute("servicios", listadoServicios);
        model.addAttribute("email", email);
        model.addAttribute("user", user);
        return "index"; 
    }
    

    
    @GetMapping("/gestionAdmin") 
    public String gestionAdmin(Model model, HttpSession session) {
    	String email = (String) session.getAttribute("email");
    	List<Servicios> listadoServicios = serviciosRepository.findAll();
    	User user = userRepository.findByEmail(email);
    	model.addAttribute("servicios", listadoServicios);
    	model.addAttribute("user", user);
    	 model.addAttribute("email", email);
        return "gestionAdmin"; 
    }
    
    
    
    
    @GetMapping("/login/oauth2/success")
    public String loginSuccess(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = getEmailFromAuthentication(authentication);
        List<Servicios> listadoServicios = serviciosRepository.findAll();

        // Guarda el correo electrónico en tu base de datos o realiza otras operaciones según tus necesidades
        User existingUser = userRepository.findByEmail(email);
        User user = userRepository.findByEmail(email);
        model.addAttribute("servicios", listadoServicios);
        model.addAttribute("email", email);
        model.addAttribute("user", user);
        if (existingUser == null) {
            // El correo no existe, crea un nuevo usuario y guárdalo en la base de datos
            
        	User newUser = new User(email, Arrays.asList(new Role("USER")));
            userRepository.save(newUser);
        }

        return "index";
    }
    
    
    
    
    
    
    private String getEmailFromAuthentication(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            return oAuth2User.getAttribute("email");
        }
        return null; // o manejar según tus necesidades si el principal no es un OAuth2User
    }

}
