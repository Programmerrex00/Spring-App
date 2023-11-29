package com.example.ram.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;

@Service
public class PasswordResetService {
	
	@Autowired
    private UserServiceImpl userServiceImpl;
	
    public void sendPasswordResetEmail(String email) {
        // Genera una nueva contraseña temporal
        String newPassword = generateRandomPassword();

        // Actualiza la contraseña del usuario en la base de datos
        userServiceImpl.updatePassword(email, newPassword);

        // Envía la nueva contraseña por correo electrónico
        try {
        	userServiceImpl.sendPasswordResetEmail(email, newPassword);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Manejar la excepción según tus necesidades
        }
    }
    
    private String generateRandomPassword() {
        // Implementa lógica para generar una contraseña aleatoria
        // Puedes usar bibliotecas o implementar tu propia lógica
        return "NuevaContraseña123";
    }

}
