package Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import Dao.DaoCategoria;
import Dao.DaoProducto;
import Modelo.Categoria;
import Modelo.Producto;

import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.Toolkit;
import javax.swing.JComboBox;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class vProducto extends JFrame {

	private JPanel contentPane;
	private JLabel lblid;
	private JTextField txtprecio;
	private JTextField txtdescripcion;
	private JButton btnAgregar;
	private JButton btnEliminar;
	private JButton btnEditar;
	private JButton btnBorrar;
	DaoProducto dao = new DaoProducto();
	DefaultTableModel modelo = new DefaultTableModel();
	private JScrollPane scrollPane;
	private JTable tblProducto;
	ArrayList<Producto> lista = new ArrayList<Producto>();
	int fila = -1;
	Producto Producto;
	private JLabel lblNewLabel_2;
	private JComboBox cbocategoria;
	private JTextField txtcantidad;
	ArrayList<Categoria> listaCategorias = new ArrayList<Categoria>();
	private JButton btnPdf;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					vProducto frame = new vProducto();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void limpiar() {
		lblid.setText("");
		txtprecio.setText("");
		txtdescripcion.setText("");
	}

	public void cargarComboCategoria() {
		DaoCategoria daoCat = new DaoCategoria();
		listaCategorias = daoCat.fetchcategorias();
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for (Categoria categoria : listaCategorias) {
			model.addElement(categoria.getCategoria());
		}
		cbocategoria.setModel(model);
	}

	public String categoria(int id) {
		for (Categoria categoria : listaCategorias) {
			if (categoria.getIdCategoria() == id) {
				return categoria.getCategoria();
			}
		}
		return null;
	}

	public vProducto() {
		setLocationRelativeTo(null);
		setTitle("CRUDProducto");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 566, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("id");
		lblNewLabel.setBounds(20, 26, 33, 23);
		contentPane.add(lblNewLabel);

		lblid = new JLabel("1");
		lblid.setHorizontalAlignment(SwingConstants.LEFT);
		lblid.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblid.setBounds(106, 26, 86, 23);
		contentPane.add(lblid);

		JLabel lblNewLabel_1 = new JLabel("Categoria");
		lblNewLabel_1.setBounds(227, 60, 86, 23);
		contentPane.add(lblNewLabel_1);

		txtprecio = new JTextField();
		txtprecio.setBounds(106, 95, 86, 20);
		contentPane.add(txtprecio);
		txtprecio.setColumns(10);

		JLabel lblNewLabel_1_1 = new JLabel("Precio");
		lblNewLabel_1_1.setBounds(20, 94, 86, 23);
		contentPane.add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_2 = new JLabel("Descripcion");
		lblNewLabel_1_2.setBounds(10, 60, 86, 23);
		contentPane.add(lblNewLabel_1_2);

		txtdescripcion = new JTextField();
		txtdescripcion.setColumns(10);
		txtdescripcion.setBounds(106, 63, 86, 20);
		contentPane.add(txtdescripcion);

		btnAgregar = new JButton("Agregar");

		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (txtcantidad.getText().equals("") || cbocategoria.getSelectedItem().equals("")
							|| txtdescripcion.getText().equals("") || txtprecio.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "campos vacios");
						return;
					}
					Producto user = new Producto();
					user.setDescripcion(txtdescripcion.getText());
					user.setPrecio(Double.parseDouble(txtprecio.getText()));
					user.setCantidad(Integer.parseInt(txtcantidad.getText()));
					user.setCategoria(listaCategorias.get(cbocategoria.getSelectedIndex()).getIdCategoria());

					if (dao.insertarProducto(user)) {
						refrescarTabla();
						limpiar();
						JOptionPane.showMessageDialog(null, "Se agrego correctamente");
					} else {
						JOptionPane.showMessageDialog(null, "Error");
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error");
				}

			}
		});
		btnAgregar.setBounds(20, 141, 89, 23);
		contentPane.add(btnAgregar);

		btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					int opcion = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar");
					if (opcion == 0) {
						if (dao.eliminarProducto(lista.get(fila).getIdproducto())) {
							refrescarTabla();
							limpiar();
							JOptionPane.showMessageDialog(null, "Se elimino correctamente");
						} else {
							JOptionPane.showMessageDialog(null, "Error");
						}
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error");
				}

			}
		});
		btnEliminar.setBounds(122, 141, 89, 23);
		contentPane.add(btnEliminar);

		btnBorrar = new JButton("borrar");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpiar();

			}
		});
		btnBorrar.setBounds(227, 141, 89, 23);
		contentPane.add(btnBorrar);

		btnEditar = new JButton("editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (txtcantidad.getText().equals("") || cbocategoria.getSelectedItem().equals("")
							|| txtdescripcion.getText().equals("") || txtprecio.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "campos vacios");
						return;
					}
					Producto.setDescripcion(txtdescripcion.getText());
					Producto.setCantidad(Integer.parseInt(txtcantidad.getText()));
					Producto.setCategoria(Integer.parseInt((String) cbocategoria.getSelectedItem()));
					Producto.setPrecio(Integer.parseInt(txtprecio.getText()));
					if (dao.editarProducto(Producto)) {
						refrescarTabla();
						limpiar();
						JOptionPane.showMessageDialog(null, "Se edito correctamente");
					} else {
						JOptionPane.showMessageDialog(null, "Error");
					}
				} catch (Exception e2) {

				}

			}
		});
		btnEditar.setBounds(326, 141, 89, 23);
		contentPane.add(btnEditar);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 173, 398, 238);
		contentPane.add(scrollPane);

		tblProducto = new JTable();
		tblProducto.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fila = tblProducto.getSelectedRow();
				Producto = lista.get(fila);
				lblid.setText("" + lista.get(fila).getIdproducto());
				txtdescripcion.setText("" + Producto.getDescripcion());
				txtprecio.setText("" + Producto.getPrecio());
				txtcantidad.setText("" + Producto.getCantidad());
				cbocategoria.setSelectedItem("" + Producto.getCategoria());

			}
		});
		tblProducto.setModel(new DefaultTableModel(
				new Object[][] { { null, null, null, null, null }, { null, null, null, null, null },
						{ null, null, null, null, null }, { null, null, null, null, null },
						{ null, null, null, null, null }, },
				new String[] { "New column", "New column", "New column", "New column", "New column" }));
		scrollPane.setViewportView(tblProducto);

		modelo.addColumn("ID");
		modelo.addColumn("descripcion");
		modelo.addColumn("precio");
		modelo.addColumn("cantidad");
		modelo.addColumn("categoria");
		tblProducto.setModel(modelo);
		refrescarTabla();

		lblNewLabel_2 = new JLabel("cantidad");
		lblNewLabel_2.setBounds(227, 26, 60, 23);
		contentPane.add(lblNewLabel_2);

		cbocategoria = new JComboBox();
		cbocategoria.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				cargarComboCategoria();
			}
		});
		cbocategoria.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		cbocategoria.setModel(new DefaultComboBoxModel(
				new String[] { "Primero", "Segundo", "Tercero", "Cuarto", "Quinto", "Sexto" }));
		cbocategoria.setBounds(307, 60, 86, 22);
		contentPane.add(cbocategoria);

		txtcantidad = new JTextField();
		txtcantidad.setBounds(297, 27, 86, 20);
		contentPane.add(txtcantidad);
		txtcantidad.setColumns(10);

		btnPdf = new JButton("pdf");
		btnPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FileOutputStream archivo;
					File file = new File(
							"C:\\Users\\ALUMNO.DESKTOP-45QG2QD\\git\\repository4\\Trabajo\\src\\PDF\\reporte.pdf");
					archivo = new FileOutputStream(file);
					Document doc = new Document();
					PdfWriter.getInstance(doc, archivo);
					doc.open();
					Image img = Image.getInstance(
							"C:\\Users\\ALUMNO.DESKTOP-45QG2QD\\git\\repository4\\Trabajo\\src\\IMG\\DoctorSimi.png");
					img.setAlignment(Element.ALIGN_CENTER);
		            img.scaleToFit(200, 200);
					doc.add(img);
					Paragraph p = new Paragraph(10);
					com.itextpdf.text.Font negrita = new com.itextpdf.text.Font(
							com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
					p.add(Chunk.NEWLINE);
					p.add("Producto");
					p.add(Chunk.NEWLINE);
					p.add(Chunk.NEWLINE);
					p.setAlignment(Element.ALIGN_CENTER);
					doc.add(p);
					PdfPTable tabla = new PdfPTable(5);
					tabla.setWidthPercentage(100);
					PdfPCell c1 = new PdfPCell(new Phrase(" idproducto", negrita));
					PdfPCell c2 = new PdfPCell(new Phrase(" cantidad", negrita));
					PdfPCell c3 = new PdfPCell(new Phrase(" categoria", negrita));
					PdfPCell c4 = new PdfPCell(new Phrase(" descripcion", negrita));
					PdfPCell c5 = new PdfPCell(new Phrase(" precio", negrita));
					c1.setHorizontalAlignment(Element.ALIGN_CENTER);
					c2.setHorizontalAlignment(Element.ALIGN_CENTER);
					c3.setHorizontalAlignment(Element.ALIGN_CENTER);
					c4.setHorizontalAlignment(Element.ALIGN_CENTER);
					c5.setHorizontalAlignment(Element.ALIGN_CENTER);
					c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
					c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
					c3.setBackgroundColor(BaseColor.LIGHT_GRAY);
					c4.setBackgroundColor(BaseColor.LIGHT_GRAY);
					c5.setBackgroundColor(BaseColor.LIGHT_GRAY);
					tabla.addCell(c1);
					tabla.addCell(c2);
					tabla.addCell(c3);
					tabla.addCell(c4);
					tabla.addCell(c5);

					for (Producto u : lista) {
						tabla.addCell("" + u.getIdproducto());
						tabla.addCell("" + u.getCantidad());
						tabla.addCell("" + u.getCategoria());
						tabla.addCell(u.getDescripcion());
						tabla.addCell("" + u.getPrecio());

					}

					doc.add(tabla);
					Paragraph p1 = new Paragraph(10);
					p1.add(Chunk.NEWLINE);
					p1.add("NÚMERO DE REGISTRO " + lista.size());
					p1.add(Chunk.NEWLINE);
					p1.add(Chunk.NEWLINE);
					p1.setAlignment(Element.ALIGN_RIGHT);
					doc.add(p1);
					doc.close();
					archivo.close();
					Desktop.getDesktop().open(file);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "ERROR AL CREAR ARCHIVO");
				} catch (DocumentException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "ERROR AL CREAR DOCUMENTO PDF");
				} catch (IOException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "ERROR AL CREAR IO");
				}
			}
		});
		btnPdf.setBounds(434, 141, 89, 23);
		contentPane.add(btnPdf);

		cargarComboCategoria();
		refrescarTabla();
	}

	public void refrescarTabla() {
		while (modelo.getRowCount() > 0) {
			modelo.removeRow(0);
		}
		lista = dao.fetchProductos();
		for (Producto u : lista) {
			Object o[] = new Object[5];
			o[0] = u.getIdproducto();
			o[1] = u.getDescripcion();
			o[2] = u.getPrecio();
			o[3] = u.getCantidad();
			o[4] = categoria(u.getCategoria());
			modelo.addRow(o);
		}
		tblProducto.setModel(modelo);
	}
}