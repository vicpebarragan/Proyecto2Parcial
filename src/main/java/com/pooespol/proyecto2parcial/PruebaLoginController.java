/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pooespol.proyecto2parcial;

import com.pooespol.proyecto2parcial.usuarios.Administrador;
import com.pooespol.proyecto2parcial.usuarios.Mesero;
import com.pooespol.proyecto2parcial.usuarios.Usuario;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
/**
 * FXML Controller class
 *
 * @author UserPC
 */
public class PruebaLoginController implements Initializable {
    
    App app = new App();


    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtContrasena;
    @FXML
    private Label txtMostrar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void irInterfaz(MouseEvent event) {
        ArrayList<Usuario> usuarios = app.getUsuarios();
        boolean b = false;
        for(Usuario u: usuarios){
            if((u.getCorreo().equals(txtCorreo.getText())) && (u.getContrasena().equals(txtContrasena.getText()))){
                    if(u instanceof Administrador){
                        try{
                            b = true;
                            App.setRoot("interfazAdministrador");
                            break;
                        }catch(IOException ex){
                            System.out.println("Sucedio algo");
                            System.err.println(ex);
                        }                     
                    }else if(u instanceof Mesero){
                        try{
                            b = true;
                            App.setRoot("interfazMesero");
                            break;
                        }catch(IOException ex){
                            System.out.println("Sucedio algo");
                            System.err.println(ex);
                        }   
                    }
                }
        }
        if(b == false){
            txtMostrar.setText("Datos incorrectos");
        }
    }

}