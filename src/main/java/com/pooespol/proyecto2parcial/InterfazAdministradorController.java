/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pooespol.proyecto2parcial;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.input.MouseEvent;
/**
 * FXML Controller class
 *
 * @author UserPC
 */
public class InterfazAdministradorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void irMonitoreoR(MouseEvent event) {
    }

    @FXML
    private void irDisenoPlano(MouseEvent event) {
    }

    @FXML
    private void irGestionMenu(MouseEvent event) {
        try{
            App.setRoot("gestionMenu");
        }catch(IOException ex){
            System.out.println("Ocurrio algo");
            System.out.println(ex);
        }
        
    }

    @FXML
    private void verReporteVentas(MouseEvent event) {
    }

}
