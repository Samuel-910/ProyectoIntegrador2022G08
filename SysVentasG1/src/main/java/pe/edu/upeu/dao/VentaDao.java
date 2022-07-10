package pe.edu.upeu.dao;

import pe.edu.upeu.crud.AppCrud;
import pe.edu.upeu.modelo.VentaDetalleTO;
import pe.edu.upeu.modelo.VentaTO;
import pe.edu.upeu.util.LeerArchivo;
import pe.edu.upeu.util.LeerTeclado;
import pe.edu.upeu.util.UtilsX;

public class VentaDao extends AppCrud {
    LeerTeclado leerT = new LeerTeclado();
    LeerArchivo leerA;
    UtilsX util = new UtilsX();

    final static String TABLA_VENTA = "Venta.txt";
    final static String TABLA_PRODUCTO = "Producto.txt";
    final static String TABLA_DETALLEVENTA = "DetalleVenta.txt";
    final static String TABLA_CLIENTE = "Cliente.txt";

    VentaTO vTO;
    VentaDetalleTO vdTO;

    public void registroVentaGeneral() {
        util.clearConsole();
        System.out.println("**************Realizar Venta*************");
        String dnix = leerT.leer("", "Ingrese DNI del cliente");
        if (validarCliente(dnix)) {
            vTO = new VentaTO();
            vTO.setDni(dnix);
            vTO.setDescuento(0);
            leerA = new LeerArchivo(TABLA_VENTA);
            String idV = generarId(leerA, 0, "V", 1);
            vTO.setIdVenta(idV);
            
        }
    }

    public void venta(VentaTO vtox) {
        leerA=new LeerArchivo(TABLA_VENTA);
        agregarContenido(leerA, vtox);
    }

    public void carritoVenta(VentaTO vtox) {
        char continuar=' ';
        do {
            mostrarProducto();
            //Agregar producto
            vdTO = new VentaDetalleTO();
            leerA=new LeerArchivo(TABLA_DETALLEVENTA);
            String idvX=generarId(leerA, 0, "DV", 2);
            vdTO.setIdDetalle(idvX);
            vdTO.setIdProducto(leerT.leer("", "Ingrese id producto"));
            vdTO.setCantidad(leerT.leer(0, "Ingrese cantidad"));
            vdTO.setIdVenta(vtox.getIdVenta());
            vdTO.setDescuento(0);
            leerA=new LeerArchivo(TABLA_PRODUCTO);
            Object[][]dataPX=buscarContenido(leerA, 5, vdTO.getIdProducto());
            vdTO.setPrecioUnit(Double.parseDouble(String.valueOf(dataPX[0][5])));
            vdTO.setTotal(vdTO.getCantidad()*vdTO.getPrecioUnit());
            leerA=new LeerArchivo(TABLA_DETALLEVENTA);
            agregarContenido(leerA, vdTO);

            continuar=leerT.leer("", "Desea Agregar mas Productos?S/N")
            .toLowerCase()
            .charAt(0);
        } while (continuar=='s');

    }

    public void mostrarProducto() {
        leerA=new LeerArchivo(TABLA_PRODUCTO);
        Object[][] datap=listarContenido(leerA);
        for (int i = 0; i < datap.length; i++) {
            System.out.print(datap[i][0]+"="+datap[i][1]+",");
        }
        System.out.println("");
    }

    public boolean validarCliente(String dni) {
        leerA = new LeerArchivo(TABLA_CLIENTE);
        Object[][] datac = buscarContenido(leerA, 0, dni);
        if (datac == null || datac.length == 0) {
            new ClienteDao().crearCliente(dni);
            return true;
        } else {
            return true;
        }

    }

}
