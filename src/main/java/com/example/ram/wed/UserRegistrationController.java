package com.example.ram.wed;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ram.model.HistorialPagos;
import com.example.ram.model.Servicios;
import com.example.ram.model.User;
import com.example.ram.pdf.PdfGenerator;
import com.example.ram.repository.HistorialRepository;
import com.example.ram.repository.ServiciosRepository;
import com.example.ram.repository.UserRepository;
import com.example.ram.service.UserService;
import com.example.ram.service.UserServiceImpl;
import com.example.ram.web.dto.UserRegistrationDto;
import com.lowagie.text.DocumentException;
import java.io.File;

import java.io.File;
import java.nio.file.Path;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;


@Controller
@RequestMapping("/registration")
public class UserRegistrationController {
	
	private UserService userService;
	
	@Autowired
	private ServiciosRepository serviciosRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private HistorialRepository historialRepository;
	
	String UPLOAD_DIR = Paths.get("src", "main", "resources", "static", "uploads").toAbsolutePath().toString() + File.separator;

	
	@Autowired
	private UserServiceImpl userServiceImpl;

	public UserRegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }
	
	@GetMapping
	public String showRegistrationForm() {
		return "registration";
	}
	
	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto, 
					@RequestParam(value = "file", required = false) MultipartFile file, RedirectAttributes redirectAttributes) {
		
		
		try {
		    // Guardar la imagen, incluso si es null
		    if (file != null && !file.isEmpty()) {
		        registrationDto.setImagen(file.getBytes());
		    }

		    // Guardar el servicio en la base de datos
		    userService.save(registrationDto);

		    redirectAttributes.addFlashAttribute("message", "Usuario registrado correctamente");
		} catch (IOException e) {
		    e.printStackTrace();
		    redirectAttributes.addFlashAttribute("error", "Error al registrar el servicio");
		}
		
		return "redirect:/registration?success";
	}
	
	

	
    @GetMapping("/showImage/{id}")
    public ResponseEntity<byte[]> showImage(@PathVariable String id) {
        Optional<Servicios> servicioOptional = serviciosRepository.findById(id);

        if (servicioOptional.isPresent()) {
            Servicios servicio = servicioOptional.get();
            byte[] imagenBytes = servicio.getImagen();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Ajusta según el tipo de imagen

            return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	

    
    
	
    @PostMapping("/agregarAlCarrito")
	public String agregarAlCarrito(@ModelAttribute("User") UserRegistrationDto registrationDto,
            @RequestParam("servicioId") String servicioId,
            @RequestParam("userEmail") String userEmail) {
    	
    	
    	User user = userRepository.findByEmail(userEmail);
    	
    	 if (user != null) {
    		 
    		 Optional<Servicios> servicioOptional = serviciosRepository.findById(servicioId);
             
    		 if (servicioOptional.isPresent()) {
                 Servicios servicio = servicioOptional.get();
                 
                 // Agrega el servicio al carrito del usuario
                 user.getServicios().add(servicio);
                 
                 // Guarda el usuario actualizado en la base de datos
                 userRepository.save(user);
             }
    	 }
    	
    	return "redirect:/index?email=" + userEmail;
    }
    
    
    @PostMapping("/comprar")
    public String comprarProducto(@ModelAttribute("User") UserRegistrationDto registrationDto,
            @RequestParam("servicioId") String servicioId,
            @RequestParam("userEmail") String userEmail, Model model) {
    	
    	User user = userRepository.findByEmail(userEmail);
    	Optional<Servicios> servicioOptional = serviciosRepository.findById(servicioId);
    	Servicios servicio = servicioOptional.get();
    	
    	 model.addAttribute("user", user);
    	model.addAttribute("usuario", user);
    	model.addAttribute("servicio", servicio);
    	
    	return "pagarServicios";
    }

    
    
   @GetMapping("/PaginaPrincipal")
   public String principal(@RequestParam("userEmail") String userEmail) {
	   return "redirect:/index?email=" + userEmail;
   }
    

    
    @PostMapping("/pagarServiciosDelCarritoo")
    public String pagarCarritoServicios(@RequestParam("emaill") String correo,
    		 @RequestParam("totalCosto") double totalServicios,
    		 Model model) {
    	User user = userRepository.findByEmail(correo);
    	
    	List<Servicios> serviciosEnCarrito = user.getServicios().stream()
                .filter(servicio -> !servicio.getRequerido().equals("Requerido"))
                .collect(Collectors.toList());
    	
    	
    	 model.addAttribute("user", user);
    	model.addAttribute("servicios", serviciosEnCarrito);
    	model.addAttribute("correo", correo);
    	model.addAttribute("totalCosto", totalServicios);
    	
    	return "pagarServiciosDelCarrito";
    }
    
    // ver imagen del Usuario
    @GetMapping("/showImageUserPerfil/{id}")
    public ResponseEntity<byte[]> showImageUser(@PathVariable String id) {
    	
    	
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            byte[] imagenBytes = user.getImagen();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Ajusta según el tipo de imagen

            return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @GetMapping("/verBolso")
    public String verBolsoVirtual(@RequestParam("email") String userEmail, Model model) {
    	User user = userRepository.findByEmail(userEmail);

        
    	 
    	 if (user != null) {
            List<Servicios> serviciosEnCarrito = user.getServicios().stream()
                    .filter(servicio -> !servicio.getRequerido().equals("Requerido"))
                    .collect(Collectors.toList());
            
            boolean requierePago = serviciosEnCarrito.stream()
                    .anyMatch(servicio -> "No Requerido".equals(servicio.getRequerido()));
            
            Double totalCosto = serviciosEnCarrito.stream()
                    .map(Servicios::getCosto)
                    .reduce(0.0, Double::sum);
            
            model.addAttribute("user", user);
            model.addAttribute("usuario", user);
            model.addAttribute("serviciosEnCarrito", serviciosEnCarrito);
            model.addAttribute("email", userEmail);
            model.addAttribute("requierePago", requierePago);
            model.addAttribute("totalCosto", totalCosto);
        }
        return "VerProductosCarrito";
    }

    
    
    
    
    @GetMapping("/mostrarHistorial")
    public String mostrarHistorial(@RequestParam("email") String userEmail, Model model) {
    	User user = userRepository.findByEmail(userEmail);
    	model.addAttribute("user", user);
        List<HistorialPagos> historialPagos = historialRepository.findByUsuarioEmail(userEmail);

        model.addAttribute("historialPagos", historialPagos);
    	
    	
    	 model.addAttribute("email", userEmail);
    	return "Historial";
    }
    
    
	// generar pdf de los Pagos
	@GetMapping("/generarPDFHistorial")
	public void generarPdf(HttpServletResponse response, @RequestParam("userEmail") String userEmail) throws DocumentException, IOException {
		
		response.setContentType("application/pdf");
		
		DateFormat dateFormet = new SimpleDateFormat("dd-MM-YYYY");
		
		String currenDateTime = dateFormet.format(new Date());
		
		String headerkey = "Content-Disposition";
		
		String headervalue = "attachment; filename=pagos" + currenDateTime + ".pdf";
		
		response.setHeader(headerkey, headervalue);
		
		User user = userRepository.findByEmail(userEmail);
		
		List<HistorialPagos> historialPagos = historialRepository.findByUsuarioEmail(userEmail);
		
		PdfGenerator generator = new PdfGenerator();
		
		generator.generatePDFHistory(historialPagos, response);
		
		
	}
    
    
    
    
    
    
	    @PostMapping("/pagarServiciosCarro")
	    public String pagarCarro(@RequestParam("userEmail") String correo,
	    		@RequestParam("totalCosto") double totalServicios, @RequestParam("valor") double valor, Model model) {
	    	
	    	
	    	
	    	User user = userRepository.findByEmail(correo);
	    	
	    	List<Servicios> serviciosEnCarrito = user.getServicios().stream()
	                .filter(servicio -> !servicio.getRequerido().equals("Requerido"))
	                .collect(Collectors.toList());
	    	
	    	double costoServicios = totalServicios;
	    	double saldo = valor - costoServicios;
	    	HistorialPagos history = new HistorialPagos();
	    	
	    	if(saldo < 0) {
	    		 model.addAttribute("faltaDinero", "Falta dinero. Necesitas $" + Math.abs(saldo) + " más para comprar el servicio.");
	    		 model.addAttribute("servicios", serviciosEnCarrito);
	    		 model.addAttribute("correo", correo);
	    		 model.addAttribute("totalCosto", totalServicios);
	    	}else if(costoServicios < valor) {
	    		
	    		serviciosEnCarrito.forEach(servicio -> servicio.setRequerido("Requerido"));
	    		userRepository.save(user);
	    		
	    		
	    		history.getServicios().addAll(serviciosEnCarrito);
	    		history.setUsuario(user);
	    		LocalDate fechaPago = LocalDate.now();
	    		history.setFechaActual(fechaPago);
	    		history.setTotalPagado(totalServicios);
	    		historialRepository.save(history);
	    		
	    		
	    		model.addAttribute("pagoRealizado", "Pago realizado exitosamente! sus Vueltos $" + Math.abs(valor - costoServicios));
		   		model.addAttribute("servicios", serviciosEnCarrito);
		   		model.addAttribute("correo", correo);
		   		model.addAttribute("totalCosto", totalServicios);
	    	}else {
	    		
	    		serviciosEnCarrito.forEach(servicio -> servicio.setRequerido("Requerido"));
	    		userRepository.save(user);
	    		
	    		history.getServicios().addAll(serviciosEnCarrito);
	    		history.setUsuario(user);
	    		LocalDate fechaPago = LocalDate.now();
	    		history.setFechaActual(fechaPago);
	    		history.setTotalPagado(totalServicios);
	    		historialRepository.save(history);
	    		
	    		model.addAttribute("pagoHecho", "Pago realizado exitosamente!");
		   		model.addAttribute("servicios", serviciosEnCarrito);
		   		model.addAttribute("correo", correo);
		   		model.addAttribute("totalCosto", totalServicios);
	    	}
	    	
	    	model.addAttribute("user", user);
	    	
	    	return "pagarServiciosDelCarrito";
	    }
  

    
    
    
    @PostMapping("/realizarPago")
    public String realizarPago(@RequestParam("userEmail") String correo,
            @RequestParam("valor") double valor,
            @RequestParam("servicioId") String servicioId, Model model) {
    	
    	Servicios servicio = serviciosRepository.findById(servicioId)
    	.orElseThrow(() -> new NoSuchElementException("No se encontró el servicio con ID: " + servicioId));
    	User user = userRepository.findByEmail(correo);
    	
    	double costoServicio = servicio.getCosto();
    	double saldo = valor - costoServicio;
    	HistorialPagos history = new HistorialPagos();
    	
    	
    	 if (saldo < 0) {
    		 model.addAttribute("faltaDinero", "Falta dinero. Necesitas $" + Math.abs(saldo) + " más para comprar el servicio.");
    		 model.addAttribute("usuario", user);
    		 model.addAttribute("servicio", servicio);
    		 
    	 }else if(costoServicio < valor){
    		 
    		 servicio.setRequerido("Requerido");
    		 user.getServicios().add(servicio);
    		 userRepository.save(user);
    		 
    		 
    		 history.getServicios().add(servicio);
    		 
    		 history.setUsuario(user);
    		 
    		 LocalDate fechaPago = LocalDate.now();
    		 history.setTotalPagado(costoServicio);
    		 history.setFechaActual(fechaPago);
    		 
    		 historialRepository.save(history);
    		 
    		 model.addAttribute("pagoRealizado", "Pago realizado exitosamente! sus Vueltos $" + Math.abs(valor - costoServicio));
    		 model.addAttribute("usuario", user);
    		 model.addAttribute("servicio", servicio);
    		 return "pagarServicios";
    		 
    		 
    	 }else {
    		 servicio.setRequerido("Requerido");
    		 user.getServicios().add(servicio);
    		 userRepository.save(user);
    		 history.getServicios().add(servicio);
    		 
    		 history.setUsuario(user);
    		 
    		 LocalDate fechaPago = LocalDate.now();
    		 history.setFechaActual(fechaPago);
    		 history.setTotalPagado(costoServicio);
    		 historialRepository.save(history);
    		 model.addAttribute("pagoHecho", "Pago realizado exitosamente!");
    		 model.addAttribute("usuario", user);
    		 model.addAttribute("servicio", servicio);
    		 return "pagarServicios";
    	 }
    	 model.addAttribute("user", user);
    	 return "pagarServicios";
    }

    
    
    
    
    
    
    
    
    
    
    @PostMapping("/eliminarDelCarrito")
    public String eliminarDelCarrito(@RequestParam("servicioId") String servicioId,
    								 @RequestParam("userEmail") String userEmail) {
    	
    	User user = userRepository.findByEmail(userEmail);
    	
        if (user != null) {
            // Elimina el servicio del carrito del usuario
            user.getServicios().removeIf(servicio -> servicio.getId().equals(servicioId));

            // Guarda el usuario actualizado en la base de datos
            userRepository.save(user);
        }
    	
    	return "redirect:/registration/verBolso?email=" + userEmail;
    }
    
    
	
	
	
    /*@RequestMapping("/upload")
    public String showUploadForm() {
        return "redirect:/index";
    }
	

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/upload";
        }

        try {
            // Obtén la ruta completa donde se guardará el archivo
            String filePath = UPLOAD_DIR + file.getOriginalFilename();

            // Guarda el archivo en la carpeta de uploads
            file.transferTo(new File(filePath));

            // Aquí puedes almacenar la ruta en una variable String o en una base de datos
            String imagePath = "/uploads/" + file.getOriginalFilename();

            // Realiza cualquier lógica adicional con la ruta de la imagen

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/registration/upload";
    }
    
    

    

    
    
    
    
    
    @GetMapping("/showImage/{filename:.+}")
    public ResponseEntity<Resource> showImage(@PathVariable String filename) {
        Path imagePath = Paths.get(UPLOAD_DIR).resolve(filename);
        Resource resource;

        try {
            resource = new UrlResource(imagePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.IMAGE_JPEG) // Ajusta según el tipo de imagen
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
	
*/
    
}
