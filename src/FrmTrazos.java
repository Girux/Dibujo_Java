import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

public class FrmTrazos extends JFrame {

    private Lista listaTrazos = new Lista();
    private JButton btnGuardar;
    private JButton btnCargar;
    private JButton btnEliminar;

    private String[] tipoTrazo = new String[] { "Linea", "Rectangulo", "Ovalo" };
    JComboBox cmbTipoTrazo;
    JTextField txtInfo;
    JPanel pnlDibujo;
    int x, y;
    boolean trazando = false;
    private String nombreArchivo = "./datos.txt";

    public FrmTrazos() {

        setSize(500, 400);
        setTitle("Editor de gráficas");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JToolBar tbTrazos = new JToolBar();
        cmbTipoTrazo = new JComboBox();
        DefaultComboBoxModel dcm = new DefaultComboBoxModel(tipoTrazo);
        cmbTipoTrazo.setModel(dcm);
        tbTrazos.add(cmbTipoTrazo);

        txtInfo = new JTextField();
        tbTrazos.add(txtInfo);

        pnlDibujo = new JPanel();
        pnlDibujo.setBackground(Color.BLACK);

        getContentPane().add(tbTrazos, BorderLayout.NORTH);
        getContentPane().add(pnlDibujo, BorderLayout.CENTER);

        // eventos
        // evento cuando haga CLICK con el raton
        pnlDibujo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                dibujar(me.getX(), me.getY());
            }
        });

        // evento cuando mueva el puntero del raton
        pnlDibujo.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent me) {
                dibujarTemporal(me.getX(), me.getY());
            }
        });

        // Botón guardar
        btnGuardar = new JButton();

        btnGuardar.setIcon(new ImageIcon(getClass().getResource("/Iconos/Guardar.png")));
        btnGuardar.setToolTipText("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnGuardarClick(evt);
            }
        });
        tbTrazos.add(btnGuardar);

        // Botón cargar
        btnCargar = new JButton();

        btnCargar.setIcon(new ImageIcon(getClass().getResource("/Iconos/cargar.png")));
        btnCargar.setToolTipText("Cargar");
        btnCargar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCargarClick(evt);
            }
        });
        tbTrazos.add(btnCargar);

        // Botón eliminar
        btnEliminar = new JButton();
            
        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/Iconos/Eliminar.png")));
        btnEliminar.setToolTipText("Eliminar");
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnEliminarClick(evt);
            }
        });
        tbTrazos.add(btnEliminar);

    }

    private void btnGuardarClick(ActionEvent evt) {
        if (listaTrazos.guardar(nombreArchivo)) {
            JOptionPane.showMessageDialog(null, "Los datos fueron guardados con éxito");
        } else {
            JOptionPane.showMessageDialog(null, "No se pudieron guardar los datos");
        }
    }

    private void btnCargarClick(ActionEvent evt) {
        listaTrazos.desdeArchivo(nombreArchivo);
        mostrarFiguras();

    }

    private void btnEliminarClick(ActionEvent evt) {
        if (!listaTrazos.obtenerLista().isEmpty()) {
            String[] opciones = { "Sí", "No" };
            int decision = JOptionPane.showOptionDialog(
                    null,
                    "¿Está seguro de eliminar el último trazo?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[1]);

            if (decision == JOptionPane.YES_OPTION) {
                listaTrazos.eliminarUltimo();
                pnlDibujo.repaint();
                mostrarFiguras();
                System.out.println("prueba");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay trazos para eliminar.");
        }
    }

    private void dibujar(int x, int y) {
        if (!trazando) {
            trazando = true;
            this.x = x;
            this.y = y;
            txtInfo.setText("Trazando desde x=" + this.x + ", y=" + this.y);
        } else {
            trazando = false;
            Graphics g = pnlDibujo.getGraphics();
            g.setColor(Color.RED);
            int ancho = Math.abs(this.x - x);
            int alto = Math.abs(this.y - y);
            Figura figura = null;
            switch (cmbTipoTrazo.getSelectedIndex()) {
                case 0:
                    /* Objeto figura creado */
                    figura = new Figura("Linea", this.x, this.y, x, y);
                    // g.drawLine(this.x, this.y, x, y);
                    break;
                case 1:
                    this.x = x < this.x ? x : this.x;
                    this.y = y < this.y ? y : this.y;
                    figura = new Figura("Rectangulo", this.x, this.y, ancho, alto);
                    // g.drawRect(this.x, this.y, ancho, alto);
                    break;
                case 2:
                    this.x = x < this.x ? x : this.x;
                    this.y = y < this.y ? y : this.y;
                    figura = new Figura("Ovalo", this.x, this.y, ancho, alto);
                    // g.drawOval(this.x, this.y, ancho, alto);
                    break;
            }
            if (figura != null) {
                listaTrazos.agregar(figura);

            }

            txtInfo.setText("");
        }
        mostrarFiguras();
    }

    private void mostrarFiguras() {
    
        for (Figura figura : listaTrazos.obtenerLista()) {
            Graphics g = pnlDibujo.getGraphics();
            g.setColor(Color.RED);
            switch (figura.tipo) {
                case "Linea":
                    g.drawLine(figura.x1, figura.y1, figura.x2, figura.y2);
                    break;

                case "Rectangulo":
                    g.drawRect(figura.x1, figura.y1, figura.x2, figura.y2);
                    break;

                case "Ovalo":
                    g.drawOval(figura.x1, figura.y1, figura.x2, figura.y2);
                    break;

            }
        }
    }

    private void dibujarTemporal(int x, int y) {
        if (trazando) {
            Graphics g = pnlDibujo.getGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, pnlDibujo.getWidth(), pnlDibujo.getHeight());
            mostrarFiguras();

            g.setColor(Color.YELLOW);
            int ancho = Math.abs(this.x - x);
            int alto = Math.abs(this.y - y);
            int x1 = x < this.x ? x : this.x;
            int y1 = y < this.y ? y : this.y;
            switch (cmbTipoTrazo.getSelectedIndex()) {
                case 0:
                    g.drawLine(this.x, this.y, x, y);
                    break;
                case 1:
                    g.drawRect(x1, y1, ancho, alto);
                    break;
                case 2:
                    g.drawOval(x1, y1, ancho, alto);
                    break;
            }
        }
    }
}
