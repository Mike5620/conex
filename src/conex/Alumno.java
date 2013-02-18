/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Alumno.java
 *
 * Created on 30/06/2011, 10:27:32 AM
 */

package conex;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Mike
 * @fecha 18/02/2013
 */
public class Alumno extends javax.swing.JFrame {

    /** Creates new form Alumno */
    Statement st;
    ResultSet rt1;
    Connection con;
    DefaultTableModel mdAlumno=new DefaultTableModel();
    String[] Dat=new String[5];
    String[] CabeceraAlumno={"Codigo","Nombres","Apellidos","Direccion","DNI"};

    public Alumno() {
        initComponents();
        this.mdAlumno.setColumnIdentifiers(CabeceraAlumno);
        this.tblalumno.setModel(mdAlumno);        
        EditableText(false);
        this.cboconeccion.setSelectedIndex(-1);
        botonesEnable(false);
    }
    
     public void conectar(){
          try {              
              if (this.cboconeccion.getSelectedIndex()==0) {
                //CONEXION PARA MYSQL

                  //Conexion remota de BD
//                String url = "jdbc:mysql://192.168.1.12:3306/alumno";
//                Class.forName("com.mysql.jdbc.Driver").newInstance();
//                con = DriverManager.getConnection(url, "root", "kinko");

//                  Conexion local, en la propia maquina
                String url = "jdbc:mysql://localhost/alumno";
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(url, "root", "admin");

                System.out.println("Conexion a MySQL");
              }else if(this.cboconeccion.getSelectedIndex()==1){
                //CONEXION PARA BASE DE DATOS ACCESS
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                con=DriverManager.getConnection("jdbc:odbc:bdAlumnos", "USUARIO", "");
                System.out.println("Conexion a Access");
             }
            st=con.createStatement();
            st=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rt1=st.executeQuery("Select * from Alumno");
            rt1.next();
            primero();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            this.cboconeccion.setSelectedIndex(-1);
        }
    }

     public void CargarDatosTablaAlumnos(){
        this.LimpiarTabla(mdAlumno);
         try{
             String sq=null;
             sq="SELECT * FROM Alumno ";
             rt1=st.executeQuery(sq);
             while(rt1.next()){
                Dat[0]=rt1.getString(1);
                Dat[1]=rt1.getString(2);
                Dat[2]=rt1.getString(3);
                Dat[3]=rt1.getString(4);
                Dat[4]=rt1.getString(5);
                mdAlumno.addRow(Dat);
             }
             this.tblalumno.changeSelection(0,0, false,true);
             rt1.first();
        }catch(Exception e){}
    }

    public void LimpiarTabla(DefaultTableModel md){
        while(md.getRowCount()>0){
            md.removeRow(0);
        }
    }

    public void cargartext(){
        try{
             this.txtapellido.setText(rt1.getString("Apellidos"));
             this.txtnombre.setText(rt1.getString("Nombre"));
             this.txtdni.setText(rt1.getString("Dni"));
             this.txtdireccion.setText(rt1.getString("Direccion"));
             this.txtindice.setText(rt1.getString("IdAlumno"));
             int i=Integer.parseInt(rt1.getString("IdAlumno"));
             for (int j = 0; j <this.tblalumno.getColumnCount(); j++) {
                this.tblalumno.changeSelection(i-1, j, false,true);
             }
        }catch(Exception e){
            System.err.println(e);
        }
    }
    
    public void EditableText(boolean b){
        this.txtapellido.setEditable(b);
        this.txtdireccion.setEditable(b);
        this.txtdni.setEditable(b);
        this.txtnombre.setEditable(b);
    }
    
    public void primero(){
        try {
            if(rt1.first()) {
                cargartext();
            }
        } catch (SQLException px) {
        }
    }

    public void siguiente(){
        try {
            if (rt1.isLast()==false) {
                if(rt1.next()) {
                    cargartext();
                }
            }
        } catch (SQLException px) {
            System.out.println(""+ px);
        }
    }

    public void anterior(){
        try {
            if (rt1.isFirst()==false) {
                if(rt1.previous()) {
                    cargartext();
                }
            }
        } catch (SQLException px) {
        }
    }

    public void ultimo(){
        try {
            if(rt1.last()) {
                cargartext();
            }
        } catch (SQLException px) {
        }
    }

    public void botonesEnable(boolean b){
        this.btnagregar.setEnabled(b);
        this.btnlimpiar.setEnabled(b);
        this.btncancelar.setEnabled(b);
        this.btnmodificar.setEnabled(b);
        this.btninicio.setEnabled(b);
        this.btnfin.setEnabled(b);
        this.btnatras.setEnabled(b);
        this.btnsiguiente.setEnabled(b);
    }

    public void botonNuevo() {
        String cod = null, ap = null, nom = null, dni = null, dir = null;
        cod = generarCodigo();
        ap = this.txtapellido.getText();
        nom = this.txtnombre.getText();
        dni = this.txtdni.getText().trim();
        dir = this.txtdireccion.getText();
        try {
            String sql = null;
            sql = "insert into alumno values('" + cod + "','" +
                    nom + "','" + ap + "','" + dir
                    + "','" + dni + "');";
            st = con.createStatement();
            int c = st.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Se grabo correctamente");
            con.close();
            conectar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrio un Problema al "
                    + "crear Nuevo Usuario");
        }
    }

    public String generarCodigo() {
        String cdg = "";
        int valor = 0;
        try {
            st = con.createStatement();
            rt1 = st.executeQuery("select count(*) from Alumno");
            while (rt1.next()) {
                valor = Integer.parseInt(rt1.getString(1));
            }
        } catch (Exception e) {}
        valor = valor + 1;
        cdg = Integer.toString(valor);   
        return cdg;
    }

    public void limpiarText(){
        this.txtapellido.setText("");
        this.txtdireccion.setText("");
        this.txtdni.setText("");
        this.txtnombre.setText("");
        this.txtnombre.requestFocus();
    }

    public void  modificar(){
        String id=null,nom=null,ape=null,dir=null,dni=null;
        id=String.valueOf(this.tblalumno.getSelectedRow()+1);
        nom=this.txtnombre.getText();
        ape=this.txtapellido.getText();
        dir=this.txtdireccion.getText();
        dni=this.txtdni.getText();
        try {
            String sql="UPDATE alumno set Nombre='"+nom+
                    "', Apellidos='"+ape+"', Direccion='"+dir+"', "
                    + "Dni='"+dni+"' where IdAlumno='"+id+"'";
            st = con.createStatement();
            int c = st.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Se actualizo correctamente");
            con.close();
            conectar();
        } catch (Exception e) {
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btninicio = new javax.swing.JButton();
        btnatras = new javax.swing.JButton();
        txtindice = new javax.swing.JTextField();
        btnsiguiente = new javax.swing.JButton();
        btnfin = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtnombre = new javax.swing.JTextField();
        txtapellido = new javax.swing.JTextField();
        txtdireccion = new javax.swing.JTextField();
        txtdni = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnagregar = new javax.swing.JButton();
        btnlimpiar = new javax.swing.JButton();
        btnsalir = new javax.swing.JButton();
        btnmodificar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblalumno = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cboconeccion = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tabla de Alumno");

        jLabel1.setBackground(new java.awt.Color(0, 102, 102));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TABLA ALUMNO");
        jLabel1.setOpaque(true);

        jPanel1.setMaximumSize(new java.awt.Dimension(343, 234));

        btninicio.setFont(new java.awt.Font("Tahoma", 1, 11));
        btninicio.setText("<<");
        btninicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btninicioActionPerformed(evt);
            }
        });

        btnatras.setFont(new java.awt.Font("Tahoma", 1, 11));
        btnatras.setText("<");
        btnatras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnatrasActionPerformed(evt);
            }
        });

        txtindice.setEditable(false);
        txtindice.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtindice.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnsiguiente.setFont(new java.awt.Font("Tahoma", 1, 11));
        btnsiguiente.setText(">");
        btnsiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsiguienteActionPerformed(evt);
            }
        });

        btnfin.setFont(new java.awt.Font("Tahoma", 1, 11));
        btnfin.setText(">>");
        btnfin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfinActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Nombre");

        txtnombre.setFont(new java.awt.Font("Tahoma", 1, 11));

        txtapellido.setFont(new java.awt.Font("Tahoma", 1, 11));

        txtdireccion.setFont(new java.awt.Font("Tahoma", 1, 11));

        txtdni.setFont(new java.awt.Font("Tahoma", 1, 11));
        txtdni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtdniKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtdniKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel6.setText("DNI");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel5.setText("Direccion");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setText("Apellidos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtapellido)
                            .addComponent(txtnombre, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                            .addComponent(txtdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtdni, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtapellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtdni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnagregar.setFont(new java.awt.Font("Tahoma", 1, 11));
        btnagregar.setText("Nuevo");
        btnagregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnagregarActionPerformed(evt);
            }
        });

        btnlimpiar.setFont(new java.awt.Font("Tahoma", 1, 11));
        btnlimpiar.setText("Limpiar");
        btnlimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlimpiarActionPerformed(evt);
            }
        });

        btnsalir.setFont(new java.awt.Font("Tahoma", 1, 11));
        btnsalir.setText("Salir");
        btnsalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsalirActionPerformed(evt);
            }
        });

        btnmodificar.setFont(new java.awt.Font("Tahoma", 1, 11));
        btnmodificar.setText("Modificar");
        btnmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmodificarActionPerformed(evt);
            }
        });

        btncancelar.setFont(new java.awt.Font("Tahoma", 1, 11));
        btncancelar.setText("Cancelar");
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(btninicio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnatras)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtindice, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnsiguiente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnfin))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnagregar, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnlimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(btnmodificar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btninicio)
                        .addComponent(btnatras))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnsiguiente)
                        .addComponent(btnfin)
                        .addComponent(txtindice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnagregar)
                    .addComponent(btnlimpiar)
                    .addComponent(btnsalir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncancelar)
                    .addComponent(btnmodificar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblalumno.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombres", "Apellidos", "Direccion", "DNI"
            }
        ));
        tblalumno.setEnabled(false);
        tblalumno.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblalumno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblalumnoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblalumno);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Tipo de Coneccion con:");

        cboconeccion.setFont(new java.awt.Font("Tahoma", 1, 11));
        cboconeccion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MySQl", "Access" }));
        cboconeccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboconeccionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(cboconeccion, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cboconeccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(350, 350, 350)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-739)/2, (screenSize.height-385)/2, 739, 385);
    }// </editor-fold>//GEN-END:initComponents

    private void btnfinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfinActionPerformed
        ultimo();
}//GEN-LAST:event_btnfinActionPerformed

    private void btnsiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsiguienteActionPerformed
        siguiente();
}//GEN-LAST:event_btnsiguienteActionPerformed

    private void btnatrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnatrasActionPerformed
        anterior();
}//GEN-LAST:event_btnatrasActionPerformed

    private void btninicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btninicioActionPerformed
        primero();
}//GEN-LAST:event_btninicioActionPerformed

    private void btnsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsalirActionPerformed
        this.dispose();
}//GEN-LAST:event_btnsalirActionPerformed

    private void btnagregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnagregarActionPerformed
        if (this.btnagregar.getText().compareTo("Nuevo")==0) {
            limpiarText();
            EditableText(true);
            this.btnagregar.setText("Agregar");
            this.btnmodificar.setEnabled(false);
            this.btnlimpiar.setEnabled(true);
        }else{
            botonNuevo();
            CargarDatosTablaAlumnos();
            EditableText(false);
            this.btnagregar.setText("Nuevo");
            this.btnmodificar.setEnabled(true);
            this.btnlimpiar.setEnabled(false);
        }        
}//GEN-LAST:event_btnagregarActionPerformed

    private void btnlimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarActionPerformed
        limpiarText();
    }//GEN-LAST:event_btnlimpiarActionPerformed

    private void btnmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmodificarActionPerformed
        if (this.btnmodificar.getText().compareTo("Modificar")==0) {           
            EditableText(true);
            this.btnmodificar.setText("Actualizar");
            this.txtnombre.requestFocus();
            this.txtnombre.select(0, this.txtnombre.getText().length());
            this.btnagregar.setEnabled(false);
            this.btnlimpiar.setEnabled(true);
        } else {
            modificar();
            CargarDatosTablaAlumnos();
            EditableText(false);
            this.btnmodificar.setText("Modificar");
            this.btnagregar.setEnabled(true);
            this.btnlimpiar.setEnabled(false);
        }
        
    }//GEN-LAST:event_btnmodificarActionPerformed

    private void tblalumnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblalumnoMouseClicked

    }//GEN-LAST:event_tblalumnoMouseClicked

    private void cboconeccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboconeccionActionPerformed
        if (this.cboconeccion.getSelectedIndex()!=-1) {
            conectar();
            CargarDatosTablaAlumnos();
            botonesEnable(true);
            this.btnlimpiar.setEnabled(false);
        }
    }//GEN-LAST:event_cboconeccionActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        try {
            limpiarText();
            EditableText(false);
            this.btnmodificar.setText("Modificar");
            this.btnagregar.setText("Nuevo");
            this.btnmodificar.setEnabled(true);
            this.btnagregar.setEnabled(true);
            rt1.first();
            this.tblalumno.changeSelection(0, 0, false,true);
            cargartext();
        } catch (SQLException ex) {
        }
    }//GEN-LAST:event_btncancelarActionPerformed

    private void txtdniKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdniKeyPressed
        
    }//GEN-LAST:event_txtdniKeyPressed

    private void txtdniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdniKeyTyped
        if (this.txtdni.getText().length()>=8) {
            evt.consume();
        }
    }//GEN-LAST:event_txtdniKeyTyped

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Alumno().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnagregar;
    private javax.swing.JButton btnatras;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnfin;
    private javax.swing.JButton btninicio;
    private javax.swing.JButton btnlimpiar;
    private javax.swing.JButton btnmodificar;
    private javax.swing.JButton btnsalir;
    private javax.swing.JButton btnsiguiente;
    private javax.swing.JComboBox cboconeccion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblalumno;
    private javax.swing.JTextField txtapellido;
    private javax.swing.JTextField txtdireccion;
    private javax.swing.JTextField txtdni;
    private javax.swing.JTextField txtindice;
    private javax.swing.JTextField txtnombre;
    // End of variables declaration//GEN-END:variables

}
