package com.example.ram.wed;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ram.model.Servicios;
import com.example.ram.model.User;
import com.example.ram.repository.ServiciosRepository;
import com.example.ram.repository.UserRepository;
import com.example.ram.service.PasswordResetService;
import com.example.ram.service.UserService;
import com.example.ram.service.UserServiceImpl;
import com.example.ram.web.dto.UserRegistrationDto;
import com.example.ram.pdf.*;
import com.lowagie.text.DocumentException;


@Controller
@RequestMapping("/registrationAdmin")
public class AdminRegistrationController {
	
	String UPLOAD_DIR = Paths.get("src", "main", "resources", "static", "uploads").toAbsolutePath().toString() + File.separator;
	
	private UserService userService;
	
	@Autowired
	private UserServiceImpl userServiceImpl2;
	
	@Autowired
	private ServiciosRepository serviciosRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private PasswordResetService passwordResetService;
	
	
	
	@Autowired
	private UserServiceImpl userServiceImpl;

	public AdminRegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	
	@ModelAttribute("admin")
    public UserRegistrationDto userRegistrationDtoAdmin() {
        return new UserRegistrationDto();
    }
	
	@GetMapping
	public String showRegistrationForm() {
		return "registrationAdmin";
	}
	
	@PostMapping
	public String registerUserAccount(@ModelAttribute("admin") UserRegistrationDto registrationDto) {
		userServiceImpl.saveAdmin(registrationDto);
		return "redirect:/registrationAdmin?success";
	}
	
	@GetMapping("/panelGestion")
	public String panelAdmin() {
		return "eleccionGestion";
	}
	
	
	
		// gestion para servicios
		@GetMapping("/gesServicios")
		public String gesServicios(Model model) {
			List<Servicios> listadoServicios = serviciosRepository.findAll();
			model.addAttribute("servicios", listadoServicios);
			return "gestionServicios";
		}		
		
		
		
		
		// gestion para usuarios
		@GetMapping("/gesUsuario")
		public String gesUsuarios(Model model) {
			List<User> listadoUsuarios = userRepository.findAll();
			model.addAttribute("usuarios", listadoUsuarios);
			return "gestionUsers";
		}

	
		
		

	    
			//Eliminar para servicios
		    @GetMapping("/delete/{id}")
		    @PreAuthorize("permitAll()")
		    public String servicioDeleteProcess(@PathVariable("id") String id) {
		        serviciosRepository.deleteById(id);
		        return "redirect:/registrationAdmin/gesServicios";
		    }
		    
			//Eliminar para servicios
		    @GetMapping("/deleteUser/{id}")
		    @PreAuthorize("permitAll()")
		    public String usuarioDeleteProcess(@PathVariable("id") String id) {
		        userRepository.deleteById(id);
		        return "redirect:/registrationAdmin/gesUsuario";
		    }
		    
		    
		    
		    
		    
		    
		    
		    
		    // guardar servicios
		    @PostMapping("/save")
		    public String serviciosSaveProcess(@ModelAttribute("servicio") Servicios servicio,
		                                       @RequestParam(value = "file", required = false) MultipartFile file,
		                                       RedirectAttributes redirectAttributes) {
		        try {
		            // Guardar la imagen solo si se proporciona un nuevo archivo
		            if (file != null && !file.isEmpty()) {
		                servicio.setImagen(file.getBytes());
		            } else {
		                // Si no se proporciona un nuevo archivo, establecer la imagen actual en el objeto de servicio
		                Optional<Servicios> existingService = serviciosRepository.findById(servicio.getId());
		                if (existingService.isPresent()) {
		                    servicio.setImagen(existingService.get().getImagen());
		                }
		            }

		            // Guardar el servicio en la base de datos
		            servicio.setRequerido("No Requerido");
		            serviciosRepository.save(servicio);

		            redirectAttributes.addFlashAttribute("message", "Servicio registrado correctamente");
		        } catch (IOException e) {
		            e.printStackTrace();
		            redirectAttributes.addFlashAttribute("error", "Error al registrar el servicio");
		        }

		        return "redirect:/registrationAdmin/gesServicios";
		    }

		    
		    
		    
		    
		    
		    
		    
		    // guardar usuario
		    @PostMapping("/saveUser")
		    public String serviciosSaveProcess(@ModelAttribute("usuario") User user,
		    								   @RequestParam(value = "password", required = false) String password,
		    								   @RequestParam(value = "file", required = false) MultipartFile file,
		                                       RedirectAttributes redirectAttributes) {
		        
		    	
		        try {
		            // Guardar la imagen solo si se proporciona un nuevo archivo
		            if (file != null && !file.isEmpty()) {
		                user.setImagen(file.getBytes());
		            } else {
		                // Si no se proporciona un nuevo archivo, establecer la imagen actual en el objeto de servicio
		                Optional<User> existingUser = userRepository.findById(user.getId());
		                if (existingUser.isPresent()) {
		                    user.setImagen(existingUser.get().getImagen());
		                }
		            }

		            // Guardar el servicio en la base de datos
		            userServiceImpl2.SavaUserPassword(password, user);

		            redirectAttributes.addFlashAttribute("message", "Usuario registrado correctamente");
		        } catch (IOException e) {
		            e.printStackTrace();
		            redirectAttributes.addFlashAttribute("error", "Error al registrar el servicio");
		        }

		        return "redirect:/registrationAdmin/gesUsuario";
		    }


		    
		    
		    
		    
		    
		    
		    
		    // ver imagen del Usuario
		    @GetMapping("/showImageUser/{id}")
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
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    // ir a vista para agregar servicio
			  @GetMapping("/new")
			  public String servicioNewTemplate(Model model) {
				  List<String> tecnologiasDisponibles = Arrays.asList("Java", "Python", "JavaScript", "C#", "Ruby");
				  model.addAttribute("tecnologiasDisponibles", tecnologiasDisponibles);
				  model.addAttribute("servicio", new Servicios());
				  return "ImplementarServicio";
			  }
			  
			  
			    // ir a vista para agregar servicio
			  @GetMapping("/newUser")
			  public String usuarioNewTemplate(Model model) {
				  model.addAttribute("usuario", new User());
				  return "ImplementarUsuario";
			  }
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
		    
		    
		    
			  // editar un servicio
		   @GetMapping("/edit/{id}") 	
		   public String serviciosEditTemplate(@PathVariable("id") String id, Model model) {
			   Optional<Servicios> optionalServicio = serviciosRepository.findById(id);
			   Servicios servicio = optionalServicio.get();
			   List<String> tecnologiasDisponibles = Arrays.asList("Java", "Python", "JavaScript", "C#", "Ruby");

		        model.addAttribute("tecnologiasDisponibles", tecnologiasDisponibles);
		        model.addAttribute("tecnologiasSeleccionadas", servicio.getTecnologias());
		        
		        
			   model.addAttribute("servicio", serviciosRepository.findById(id));
		
			    return "updateServicios";
		   }
		   
		   
			  // editar un servicio
		   @GetMapping("/editUser/{id}") 	
		   public String usuarioEditTemplate(@PathVariable("id") String id, Model model) {
	
		        
			   model.addAttribute("usuario", userRepository.findById(id));
		
			    return "updateUsuarios";
		   }
		   
		   
		    
		    
		    
		    // ver imagen del servicio
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

		    
		    
			@GetMapping("/GestionRoles")
			public String volverGestion() {
				return "eleccionGestion";
			}
		    
			
			
			// generar pdf de los servicios
			@GetMapping("/generarPDF")
			public void generarPdf(HttpServletResponse response) throws DocumentException, IOException {
				
				response.setContentType("application/pdf");
				
				DateFormat dateFormet = new SimpleDateFormat("dd-MM-YYYY");
				
				String currenDateTime = dateFormet.format(new Date());
				
				String headerkey = "Content-Disposition";
				
				String headervalue = "attachment; filename=servicios" + currenDateTime + ".pdf";
				
				response.setHeader(headerkey, headervalue);
				
				List<Servicios> serviciosList = serviciosRepository.findAll();	
				
				PdfGenerator generator = new PdfGenerator();
				
				generator.generate(serviciosList, response);
				
				
			}
			
			
			
		    @GetMapping("/inputResetPassword")
		    public String resetPassword(Model model) {
		        return "confirmarCorreo";
		    }

		    
		    
		    
		    
		    
		    @PostMapping("/recuperarContra")
		    public String forgotPassword(@RequestParam("email") String email, Model model) {
		    	
		    	
		        User user = userRepository.findByEmail(email);
		        
		        if(user != null) {
		        	passwordResetService.sendPasswordResetEmail(email);
		        	model.addAttribute("Existe", "Constraseña Actualizada, Revisa tu correo");
		        }else {
		        	model.addAttribute("noExiste", "Correo no esta Registrado!!");
		      
		        }
		        
		        return "confirmarCorreo";
		    }
		    
		    
		    
		    
		    
		    
		    
		    
		    // regresar a listar los servicios despues de actualizar
			@GetMapping("/regresarDespuesDeActualizar")
			public String despuesActualizar() {
				return "redirect:/registrationAdmin/gesServicios";
			}
			
			
		    // regresar a listar los servicios despues de actualizar
			@GetMapping("/regresarDespuesDeActualizarUser")
			public String despuesActualizarUser() {
				return "redirect:/registrationAdmin/gesUsuario";
			}

		

		    
		    
			/* @PostMapping("/save")
			 public String serviciosSaveProcess(@ModelAttribute("servicio") Servicios servicio,
			                                    @RequestParam("file") MultipartFile file,
			                                    RedirectAttributes redirectAttributes) {
			     try {
			         // Guardar la imagen
			         if (!file.isEmpty()) {
			             String filePath = UPLOAD_DIR + file.getOriginalFilename();
			             file.transferTo(new File(filePath));
			             servicio.setRutaImagen(file.getOriginalFilename());
			         }


			         // Guardar el servicio en la base de datos
			         if (servicio.getId().isEmpty()) {
			             servicio.setId(null);
			         }
			         serviciosRepository.save(servicio);

			         redirectAttributes.addFlashAttribute("message", "Servicio registrado correctamente");
			     } catch (IOException e) {
			         e.printStackTrace();
			         redirectAttributes.addFlashAttribute("error", "Error al registrar el servicio");
			     }

			     return "redirect:/registrationAdmin/gesServicios";
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
			    }*/
		  
}
