/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pooespol.proyecto2parcial;

import com.pooespol.proyecto2parcial.data.ArchivosException;
import com.pooespol.proyecto2parcial.data.MesaData;
import com.pooespol.proyecto2parcial.data.PlatoData;
import static com.pooespol.proyecto2parcial.data.PlatoData.editarPlatosArchivo;
import static com.pooespol.proyecto2parcial.data.PlatoData.eliminarPlatosArchivo;
import com.pooespol.proyecto2parcial.modelo.Mesa;
import com.pooespol.proyecto2parcial.modelo.Plato;
import com.pooespol.proyecto2parcial.modelo.Ubicacion;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Personal
 */
public class MonitoreoRestaurante2Controller implements Initializable {

    @FXML
    private Label numComensales;
    @FXML
    private Label totalFacturado;
    @FXML
    private TextField fechaInicio;
    @FXML
    private TextField fechaFin;
    @FXML
    private Button filtrar;
    @FXML
    private Pane panelMesas;
    @FXML
    private Pane panelMesaDP;
    @FXML
    private TableColumn<Ventas, String> fechaCol;
    @FXML
    private TableColumn<Ventas, String> mesaCol;
    @FXML
    private TableColumn<Ventas, String> meseroCol;
    @FXML
    private TableColumn<Ventas, String> cuentaCol;
    @FXML
    private TableColumn<Ventas, String> clienteCol;
    @FXML
    private TableColumn<Ventas, String> totalCol;
    @FXML
    private TableView<Ventas> ventasTable;
    @FXML
    private FlowPane panelGestionMenu;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Diseno Plano
        iniciarElementosPanel(panelMesas, "Monitoreo");
        iniciarElementosPanel(panelMesaDP, "DisenoPlano");
        panelMesaDP.setOnMouseClicked((MouseEvent ev) -> {

            //public Mesa(int numMesa, Ubicacion ub, String mesero, String estado, int capacidad) {
            informacionMesa(0, 0, null, "DesOcupado", "DisenoPlano", new Ubicacion(ev.getX(), ev.getY()));
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        List<Ventas> ventas = new ArrayList<>();
        final ObservableList<Ventas> ventasFiltradas = FXCollections.observableArrayList();
        try {
            ventas = Ventas.leerVentas();
            for (Ventas v : ventas) {
                ventasFiltradas.add(v);
            }

            fechaCol.setCellValueFactory(new PropertyValueFactory<Ventas, String>("fecha"));
            mesaCol.setCellValueFactory(new PropertyValueFactory<Ventas, String>("mesa"));
            meseroCol.setCellValueFactory(new PropertyValueFactory<Ventas, String>("mesero"));
            cuentaCol.setCellValueFactory(new PropertyValueFactory<Ventas, String>("cuenta"));
            clienteCol.setCellValueFactory(new PropertyValueFactory<Ventas, String>("cliente"));
            totalCol.setCellValueFactory(new PropertyValueFactory<Ventas, String>("venta"));
            ventasTable.setItems(ventasFiltradas);
        } catch (ArchivosException ex) {
            System.out.println("Ocurrio Algo");
        }

        String facturado;
        try {
            facturado = String.valueOf(Ventas.calcularFacturado());
            totalFacturado.setText("Total Facturado: $" + facturado);
        } catch (ArchivosException ex) {
            System.out.println("Ocurrio Algo en sistemas...");;
        }

        try {
            

            List<Plato> platos = PlatoData.leerPlatos();
            
            for (Plato p : platos) {

                System.out.println(p);
                VBox vbox = new VBox();
                InputStream inputImg = App.class.getResource(p.getImagen()).openStream();
                ImageView imgv = new ImageView(new Image(inputImg));
                vbox.getChildren().add(imgv);
                vbox.getChildren().add(new Label(p.getNombre()));
                Label labelDinero = new Label("$ " + String.valueOf(p.getPrecio()));
                vbox.getChildren().add(labelDinero);
                vbox.setPadding(new Insets(2, 3, 3, 4));
                panelGestionMenu.getChildren().add(vbox);
                
                vbox.setOnMouseClicked((MouseEvent ev)->{
                    Stage st = new Stage();           
                    Button bt1 = new Button("Editar");
                    Button bt2 = new Button("Eliminar");
                    HBox hb = new HBox(5,bt1,bt2);
                    hb.setAlignment(Pos.CENTER);
                    Scene sce = new Scene(hb,100,80);
                    st.setScene(sce);
                    st.show();
                    
                    bt2.setOnMouseClicked((MouseEvent e2) ->{
                        panelGestionMenu.getChildren().remove(vbox);
                        try {
                            eliminarPlatosArchivo(p.getNombre());
                         
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } catch (ArchivosException ex) {
                            ex.printStackTrace();
                        }
            
                    });
                    
                    
                    bt1.setOnMouseClicked((MouseEvent e)->{
                        VBox vb1 = new VBox();
                        Label lab1 = new Label("Ingrese Costo");
                        TextField tx1 = new TextField();
                        Button bt3 = new Button("Cambiar");
                        vb1.getChildren().addAll(lab1,tx1,bt3);
                        Scene sc1 = new Scene(vb1,100,80);
                        st.setScene(sc1);
                        
                        bt3.setOnMouseClicked((MouseEvent ev1)->{
                            labelDinero.setText("$ " +tx1.getText());
                            for(Plato pla: platos){
                                if((pla.getPrecio() == Double.valueOf(p.getPrecio())) && (pla.getNombre().equals(p.getNombre()))){
                                    pla.setPrecio(Double.valueOf(tx1.getText()));
                                    
                                    try{
                                        PlatoData.editarPlatosArchivo(pla);
                                    }catch(ArchivosException io1){
                                        System.out.println("Ocurrio un error al momento de escibir el cambio de plata de comida");
                                        System.out.println(io1);
                                    }catch(IOException io2){
                                        System.out.println("Ocurrio un error al momento de escibir el cambio de plata de comida");
                                        System.out.println(io2);

                                    }
                                    
                                }
                                    
                            }
                            
                            
                            
                        });
                        
                        
                    });
                    
                    
                });

            }

        } catch (IOException ex) {
            System.out.println("Paso algo");
        } catch (ArchivosException ex) {
            ex.printStackTrace();
        }
        
        

    }

    @FXML
    private void AgregarMenu(MouseEvent event) {
        Stage st = new Stage();
        VBox vb = new VBox(0.5);
        HBox hb = new HBox(0.5);
        HBox hb2 = new HBox(0.5);
        Label l1 = new Label("Ingrese el nombre del plato: ");
        Label l2 = new Label("Ingrese el precio del plato: ");
        TextField t1 = new TextField();
        ComboBox cb = new ComboBox();
        cb.getItems().addAll("Guatita", "Bollos", "Bandera","Salchipapa", "Yapingacho","Encebollado","Bolon","Empanada","Agua","Jugo","Cafe","Cola");
        hb.getChildren().addAll(l1, cb);
        hb2.getChildren().addAll(l2, t1);
        Button bt = new Button("Agregar Plato");
        bt.setAlignment(Pos.CENTER);
        bt.setOnAction((EvenAction) -> {

            InputStream inputImg = null;
            try {
                String nombrePlato = (String) cb.getValue();
                String precio = t1.getText();
                Plato plato = new Plato(nombrePlato, Integer.valueOf(precio), nombrePlato + ".jpeg");
                try {
                    PlatoData.agregarPlatosArchivo(plato);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ArchivosException ex) {
                    ex.printStackTrace();
                }
                VBox vb2 = new VBox();
                inputImg = App.class.getResource(plato.getImagen()).openStream();
                ImageView imgv = new ImageView(new Image(inputImg));
                //agregar imagen
                Label l3 = new Label(nombrePlato);
                Label l4 = new Label("$ " + precio);
                vb2.getChildren().addAll(imgv,l3, l4);
                panelGestionMenu.getChildren().add(vb2);
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    inputImg.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });
        vb.getChildren().addAll(hb, hb2, bt);
        Scene sc = new Scene(vb, 800, 400);
        st.setScene(sc);
        st.show();

    }

    @FXML
    private void EditarMenu(MouseEvent event) {

    }

    @FXML
    private void EliminarMenu(MouseEvent event) {

    }

    @FXML
    private void filtrar(MouseEvent event) throws ParseException, ArchivosException {


        final ObservableList<Ventas> ventasFiltradas = FXCollections.observableArrayList();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String fInicio = fechaInicio.getText();
        String fFin = fechaFin.getText();
        List<Ventas> ventas = new ArrayList<>();
        ventas = Ventas.leerVentas();
        for (Ventas v : ventas) {
            String fechaVenta = v.getFecha();
            Date dateInicio = dateFormat.parse(fInicio);
            Date dateFin = dateFormat.parse(fFin);
            Date dateVenta = dateFormat.parse(fechaVenta);
            if ((dateVenta.after(dateInicio) || dateVenta.equals(dateInicio))
                    && (dateVenta.before(dateFin) || dateVenta.equals(dateFin))) {
                System.out.println(v);
                System.out.println("entre");
                ventasFiltradas.add(v);
            }

        }
        fechaCol.setCellValueFactory(new PropertyValueFactory<Ventas, String>("fecha"));
        mesaCol.setCellValueFactory(new PropertyValueFactory<Ventas, String>("mesa"));
        meseroCol.setCellValueFactory(new PropertyValueFactory<Ventas, String>("mesero"));
        cuentaCol.setCellValueFactory(new PropertyValueFactory<Ventas, String>("cuenta"));
        clienteCol.setCellValueFactory(new PropertyValueFactory<Ventas, String>("cliente"));
        totalCol.setCellValueFactory(new PropertyValueFactory<Ventas, String>("venta"));
        ventasTable.setItems(ventasFiltradas);
    }

    @FXML
    private void IrLogin(MouseEvent event) {
        try {
            App.setRoot("Login");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    

    private void informacionMesa(int numero, int capacidad, String mesero, String estado, String pestana, Ubicacion u) {

        Stage st = new Stage();
        VBox vb = new VBox(0.5);
        if (pestana.equals("Monitoreo")) {
            Label lb1 = new Label("Mesa # " + numero);
            Label lb2 = new Label("Capacidad de la mesa: " + capacidad);
            Label lb3 = new Label("Nombre del Mesero: " + mesero);
            Label lb4 = new Label("Estado: " + estado);
            vb.getChildren().add(lb1);
            vb.getChildren().add(lb2);
            vb.getChildren().add(lb3);
            vb.getChildren().add(lb4);
            vb.setAlignment(Pos.CENTER);
            Scene scene = new Scene(vb, 400, 80);
            st.setScene(scene);
            st.show();
        } else if (pestana.equals("DisenoPlano")) {
            Label lb1 = new Label("Mesa #");
            TextField t1 = new TextField();
            Label lb2 = new Label("Capacidad de la mesa");
            TextField t2 = new TextField();
            Label lb4 = new Label("Estado");
            //TextField t4 = new TextField();
            ComboBox cb = new ComboBox();
            cb.getItems().add(new Mesa(0, null, null, "Ocupado", 0));
            cb.getItems().add(new Mesa(0, null, null, "DesOcupado", 0));
            Button bt = new Button("Cambiar");
            bt.setOnMouseClicked((MouseEvent ex) -> {
                try {
                    //modificar archivo
                    Mesa me = new Mesa(Integer.valueOf(t1.getText()), u, null, cb.getValue().toString(), Integer.valueOf(t2.getText()));
                    MesaData.agregarMesaArchivos(me);
                    
                    actualilzarMonitoreo a1 = new actualilzarMonitoreo(me);
                    Thread ad1 = new Thread(a1);
                    ad1.start();
                    
                    
                    try {
                        MesaData.agregarMesaArchivos(me);
                        iniciarElementosPanel(panelMesaDP, "DisenoPlano");
                    } catch (ArchivosException ex1) {
                        ex1.printStackTrace();
                    }
                    
                    //informacionMesa(Integer.valueOf(t1.getText()), Integer.valueOf(t2.getText()), null, cb.getValue().toString(), "DisenoPlano");
                    //cambiar
                } catch (ArchivosException ex1) {
                    ex1.printStackTrace();
                }


                //informacionMesa(Integer.valueOf(t1.getText()), Integer.valueOf(t2.getText()), null, cb.getValue().toString(), "DisenoPlano");
                //cambiar
            });

            vb.getChildren().add(lb1);
            vb.getChildren().add(t1);
            vb.getChildren().add(lb2);
            vb.getChildren().add(t2);
            vb.getChildren().add(lb4);
            vb.getChildren().add(cb);
            vb.getChildren().add(bt);
            vb.setAlignment(Pos.CENTER);

            Scene scene = new Scene(vb, 300, 200);
            st.setScene(scene);
            st.show();

        }

    }

    public void iniciarElementosPanel(Pane p, String pestana) {
        try {
            List<Mesa> mesas = MesaData.cargarMesaArchivos("UbicacionMesas.txt");
            int totalComensales = 0;
            for (Mesa m : mesas) {
                Circle  c = new Circle(30, Color.BLUE);
                if(m.getCapacidad()<=4){
                c = new Circle(25, Color.BLUE);
                }else if(m.getCapacidad()>4 && m.getCapacidad()<8){
                c = new Circle(40, Color.BLUE);
                }else if(m.getCapacidad()>=8){
                c = new Circle(55, Color.BLUE);
                }
                if(m.getEstado().equals("Ocupado")){
                     //c = new Circle(30, Color.RED);
                     c.setFill(Color.RED);
                     int capacidad = m.getCapacidad();
                     totalComensales+=capacidad;
                }else if(m.getEstado().equals("DesOcupado")){
                    //c = new Circle(30, Color.YELLOW);
                    c.setFill(Color.YELLOW);
                }
                Label l = new Label(String.valueOf(m.getNumMesa()));
                StackPane st = new StackPane();
                st.getChildren().addAll(c, l);
                p.getChildren().add(st);
                st.setLayoutX(m.getUbicacion().getX());
                st.setLayoutY(m.getUbicacion().getY());

                if (pestana.equals("Monitoreo")) {
                    
                    st.setOnMouseClicked((MouseEvent ev) -> {
                        informacionMesa(m.getNumMesa(), m.getCapacidad(), m.getMesero(), m.getEstado(), pestana, new Ubicacion(m.getUbicacion().getX(), m.getUbicacion().getY()));
                    });
                } else if (pestana.equals("DisenoPlano")) {
                    st.setOnMouseClicked((MouseEvent ev) -> {
                        informacionMesa(m.getNumMesa(), m.getCapacidad(), m.getMesero(), m.getEstado(), pestana, new Ubicacion(m.getUbicacion().getX(), m.getUbicacion().getY()));
                        
                    });
                    
                    st.setOnMouseDragged((MouseEvent ex2)->{
                        st.setLayoutX(ex2.getX());
                        st.setLayoutY(ex2.getY());
                    });

                }

            }
            numComensales.setText(String.valueOf("Total de Comensales: " + totalComensales));
        } catch (ArchivosException ex) {
            System.out.println("Ocurrio algo en monitoreo restaurante 2");
        }

    }
    
    
    class actualilzarMonitoreo implements Runnable{
        
        Mesa m;
        
        public actualilzarMonitoreo(Mesa m){
            this.m = m;
        }
        public void run(){
                Platform.runLater(()->{
                    try{
                    MesaData.agregarMesaArchivos(m);
                    Thread.sleep(8000);
                    iniciarElementosPanel(panelMesas, "Monitoreo");
                    }catch (ArchivosException ex) {
                        ex.printStackTrace();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                });      
        }
} 

}
