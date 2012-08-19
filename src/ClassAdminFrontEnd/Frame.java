package ClassAdminFrontEnd;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import ClassAdminBackEnd.FileHandler;
import ClassAdminBackEnd.Global;
import ClassAdminBackEnd.UnsupportedFileTypeException;
import org.jdesktop.swingx.JXPanel;

public class Frame extends JFrame {

	private JPanel contentPane;
	private FadePanel homePanel;
	private FadePanel workspacePanel;
	private ThreeStopGradientPanel bottomPanel;
	private BackgroundGradientPanel backgroundPanel;
	private GradientMenuBar menuBar;
	private FadePanel navBar;
	private FadePanel tabBar;
	private ReflectionImagePanel container;
	private ReflectionImagePanel containerRecentDocs;
	private ReflectionImagePanel containerImportImage;
	private ReflectionImagePanel containerWorkspace;
	private ReflectionImagePanel containerStudents;
	private ImagePanel containerImportText;
	private ImagePanel containerImportTextSub;
	private ImagePanel containerStudentsText;
	private ImagePanel containerStudentsTextSub;
	private ImagePanel containerWorkspaceText;
	private ImagePanel containerWorkspaceTextSub;
	private ImagePanel boxChartImage;
	private ImagePanel histogramChartImage;
	private ImagePanel scatterplotChartImage;
	private JFileChooser filechooser;
	private JFrame frame = this;
	private File currentFilePath;
	private int tabCount = -1;
	private FrmTable table;
	private JTabbedPane tabbedPane;
	private FileHandler fileHandler;
	private BlurBackground blur;
	private ReflectionButton homeButton;
	private ReflectionButton importButton;
	private ReflectionButton exportButton;

	private final int HOME_SPACE_LEFT_X = 3;
	private final int HOME_SPACE_Y = 55;
	private final int HOME_BOTTOM_SPACE_Y = 53;
	private final int HOME_SPACE_RIGHT_X = 22;

	private static String currentOs;
	private static String MAC_OS = "MAC";
	private static String WIN_OS = "WINDOWS";

	public class TabButton extends JPanel {

		private String text;
		private JLabel label;
		private JLabel button;
		private TabButton tabbutton = this;

		public TabButton(String _text) {

			// create label with file name for tab
			text = _text;
			label = new JLabel(text);
			add(label);

			// create close button
			button = new JLabel("x");
			// button.setBorder(new EmptyBorder(1,1,1,1));
			add(button);
			button.setForeground(Color.white);

			// set this panel with label and close button to transparent
			this.setOpaque(false);
			this.setBorder(null);

			// close tab action
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					tabbedPane.remove(tabbedPane.indexOfTabComponent(tabbutton));
					tabCount--;
					// if (tabCount == -1)
					// contentPane.remove(tabbedPane);
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					button.setForeground(Color.darkGray);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					button.setForeground(Color.white);
				}

			});
		}
	}

	public Frame() {

		// set frame title
		setTitle("UP Admin");

		// frame setup
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 700);

		Image icon = Toolkit.getDefaultToolkit().getImage("Logo.png");
		this.setIconImage(icon);

		// Get the size of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// Determine the new location of the window
		int w = getSize().width;
		int h = getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		// Move the window
		setLocation(x, y);

		// maximize window
		// setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

		// create content pane
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setBackground(new Color(0x212121));
		contentPane.setLayout(null);

		// create menubar
		menuBar = new GradientMenuBar();
		menuBar.setBounds(0, 0, getWidth(), 30);
		contentPane.add(menuBar);

		// create menu
		JMenu mnFile = new JMenu("File");
		mnFile.setForeground(Color.white);
		menuBar.add(mnFile);

		// create little bottom bar of home screen
		bottomPanel = new ThreeStopGradientPanel(new Color(0xA1A1A1),
				new Color(0x696969), new Color(0x000000));
		bottomPanel.setSize(getWidth() - HOME_SPACE_RIGHT_X, 12);
		bottomPanel.setBounds(HOME_SPACE_LEFT_X, getHeight()
				- HOME_BOTTOM_SPACE_Y, bottomPanel.getWidth(),
				bottomPanel.getHeight());
		contentPane.add(bottomPanel);

		backgroundPanel = new BackgroundGradientPanel();
		backgroundPanel.setSize(getWidth() - HOME_SPACE_RIGHT_X, getHeight()
				- HOME_SPACE_Y - menuBar.getHeight());
		backgroundPanel.setBounds(HOME_SPACE_LEFT_X, menuBar.getHeight(),
				backgroundPanel.getWidth(), backgroundPanel.getHeight());
		backgroundPanel.setLayout(null);
		contentPane.add(backgroundPanel);

		setupHomeScreen();
		setupWorkspaceScreen();

		// frame resize listener to put nav bar at bottom of frame on resize
		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {

			}

			@Override
			public void componentMoved(ComponentEvent arg0) {

			}

			// resizes components with screen
			@Override
			public void componentResized(ComponentEvent arg0) {

				backgroundPanel.setBounds(HOME_SPACE_LEFT_X,
						menuBar.getHeight(), frame.getWidth()
								- HOME_SPACE_RIGHT_X, frame.getHeight()
								- HOME_SPACE_Y - menuBar.getHeight());
				workspacePanel.setBounds(0, 0, backgroundPanel.getWidth(), backgroundPanel.getHeight());
				bottomPanel.setBounds(HOME_SPACE_LEFT_X, frame.getHeight()
						- HOME_BOTTOM_SPACE_Y, frame.getWidth()
						- HOME_SPACE_RIGHT_X, 12);
				menuBar.setBounds(0, 0, getWidth(), 30);
				navBar.setBounds(0, backgroundPanel.getHeight() - 40 - 40,
						getWidth(), 80);
				workspacePanel.add(navBar);
				if (tabbedPane != null) {
					tabbedPane.setBounds(20, 20, workspacePanel.getWidth()-40, workspacePanel.getHeight()-40
							- navBar.getHeight());
				}
				if (tabBar != null) {
					tabBar.setBounds(0, 0, frame.getWidth(), frame.getHeight());
					tabBar.setLayout(null);
				}
				if (boxChartImage != null){
					boxChartImage.setBounds(tabBar.getWidth()-70, 15, 50, 40);
				}
				

			}

			@Override
			public void componentShown(ComponentEvent arg0) {

			}
		});

		fileHandler = FileHandler.get();
	}

	public void setupHomeScreen() {

		homePanel = new FadePanel(false);
		homePanel.setBounds(0, 0, backgroundPanel.getWidth(),
				backgroundPanel.getHeight());
		backgroundPanel.add(homePanel);
		homePanel.setLayout(null);

		blur = new BlurBackground(this);
		this.setGlassPane(blur);
		blur.setBounds(0, 0, getWidth(), getHeight());

		// add title bars
		try {
			container = new ReflectionImagePanel(ImageIO.read(getClass()
					.getResource("UPAdminHomeSelectTask.png")));
			container.setBounds(117, 25, 953, 88);
			homePanel.add(container);

			containerRecentDocs = new ReflectionImagePanel(
					ImageIO.read(getClass().getResource(
							"UPAdminHomeRecentDocs.png")));
			containerRecentDocs.setBounds(117, 366, 953, 81);
			homePanel.add(containerRecentDocs);

			containerImportImage = new ReflectionImagePanel(
					ImageIO.read(getClass().getResource("Import.png")));
			containerImportImage.setBounds(163, 139, 75, 138);
			homePanel.add(containerImportImage);

			containerWorkspace = new ReflectionImagePanel(
					ImageIO.read(getClass().getResource("Workspace.png")));
			containerWorkspace.setBounds(163, 235, 75, 102);
			homePanel.add(containerWorkspace);

			containerStudents = new ReflectionImagePanel(
					ImageIO.read(getClass().getResource("Students.png")));
			containerStudents.setBounds(544, 124, 75, 102);
			homePanel.add(containerStudents);

			containerImportText = new ImagePanel(ImageIO.read(getClass()
					.getResource("ImportText.png")), true);
			containerImportText.setBounds(210, 130, 89, 45);
			homePanel.add(containerImportText);

			containerImportTextSub = new ImagePanel(ImageIO.read(getClass()
					.getResource("ImportSub.png")), false);
			containerImportTextSub.setBounds(210, 166, 129, 32);
			homePanel.add(containerImportTextSub);

			containerStudentsText = new ImagePanel(ImageIO.read(getClass()
					.getResource("StudentsText.png")), true);
			containerStudentsText.setBounds(600, 130, 147, 54);
			homePanel.add(containerStudentsText);

			containerStudentsTextSub = new ImagePanel(ImageIO.read(getClass()
					.getResource("StudentsSub.png")), false);
			containerStudentsTextSub.setBounds(600, 166, 147, 32);
			homePanel.add(containerStudentsTextSub);

			containerWorkspaceText = new ImagePanel(ImageIO.read(getClass()
					.getResource("WorkspaceText.png")), true);
			containerWorkspaceText.setBounds(210, 224, 135, 53);
			homePanel.add(containerWorkspaceText);

			containerWorkspaceTextSub = new ImagePanel(ImageIO.read(getClass()
					.getResource("WorkspaceSub.png")), false);
			containerWorkspaceTextSub.setBounds(210, 259, 238, 32);
			homePanel.add(containerWorkspaceTextSub);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		containerWorkspaceText.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				homeToWorkspaceTransition();
			}
		});

		containerImportText.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				try {
					openFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		homePanel.fadeIn();
	}

	private void setupWorkspaceScreen() {
		// create background pane for home screen
		workspacePanel = new FadePanel(false);
		workspacePanel.setBounds(0, 0, backgroundPanel.getWidth(),
				backgroundPanel.getHeight());
		backgroundPanel.add(workspacePanel);
		workspacePanel.setLayout(null);

		navBar = new FadePanel(true);
		navBar.setBounds(0, workspacePanel.getHeight() - 40 - 40, getWidth(),
				80);
		workspacePanel.add(navBar);
		navBar.setLayout(null);

		try {
			homeButton = new ReflectionButton(ImageIO.read(getClass()
					.getResource("Home.png")));
			homeButton.setBounds(8, 8, 68, 80);
			navBar.add(homeButton);

			homeButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent arg0) {
					workspaceToHomeTransition();
				}
			});

			importButton = new ReflectionButton(ImageIO.read(getClass()
					.getResource("Import.png")));
			importButton.setBounds(84, 8, 68, 80);
			navBar.add(importButton);

			importButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent arg0) {
					try {
						openFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			exportButton = new ReflectionButton(ImageIO.read(getClass()
					.getResource("Export.png")));
			exportButton.setBounds(150, 8, 68, 80);
			navBar.add(exportButton);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void openFile() throws IOException, BadLocationException {

		File file;
		// set the file extentions that may be chosen
		FileFilter fileFilter = new FileNameExtensionFilter(
				"Supported files types: pdat, csv", "pdat", "csv");

		blur.fadeIn();
		// Create a file chooser
		filechooser = new JFileChooser();
		if (currentFilePath != null) {
			filechooser.setCurrentDirectory(currentFilePath);
		}

		// add the filter to the file chooser
		filechooser.addChoosableFileFilter(fileFilter);

		// shows the dialog, return value specifies file
		int returnVal = filechooser.showOpenDialog(this);

		// if the chosen file is valid
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = filechooser.getSelectedFile();
			currentFilePath = filechooser.getSelectedFile();
			blur.fadeOut();
			createTab(file);
			homeToWorkspaceTransition();
			tabBar.fadeIn();
		} else {
			blur.fadeOut();
		}
	}

	// function to determine OS that is currently running
	public static void determineOS() {
		currentOs = System.getProperty("os.name").toUpperCase();
		if (currentOs.contains("MAC")) {
			currentOs = MAC_OS;
		} else if (currentOs.contains("WINDOWS")) {
			currentOs = WIN_OS;
		} else {
			currentOs = null;
		}
	}

	// create a new Tab when a new file is imported
	public void createTab(File file) {
		try {
			fileHandler.openFile(file.getAbsolutePath());
		} catch (UnsupportedFileTypeException e) {
			e.printStackTrace();
		}
		// create table on panel
		table = new FrmTable(Global.getGlobal().getActiveProject().getHead()
				.getHeaders(), Global.getGlobal().getActiveProject().getHead()
				.getDataLinkedList());

		// create tabbedPane
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.setBounds(20, 20, workspacePanel.getWidth()-40, workspacePanel.getHeight()-40
					- navBar.getHeight());
		}

		// create panel on which tabbedPane will be
		if (tabBar == null) {
			tabBar = new FadePanel(false);
			tabBar.setBounds(0, 0, frame.getWidth(), frame.getHeight());
			tabBar.setLayout(null);
			createGraphIcons();
		}
		tabBar.add(tabbedPane);

		workspacePanel.add(tabBar);

		// put panel with table on a new tab
		tabbedPane.addTab(file.getName(), table);
		tabCount++;
		tabbedPane.setTabComponentAt(tabCount, new TabButton(file.getName()));
	}

	public void homeToWorkspaceTransition() {
		homePanel.fadeOut();
		workspacePanel.fadeIn();
		navBar.fadeIn();
		frame.remove(blur);
	}

	public void workspaceToHomeTransition() {
		homePanel.fadeIn();
		workspacePanel.fadeOut();
		navBar.fadeOut();
	}
	
	public void createGraphIcons() {
		try {
			boxChartImage = new ImagePanel(ImageIO.read(getClass().getResource("Box.png")), true);
			boxChartImage.setBounds(tabBar.getWidth()-70, 15, 60, 40);
			tabBar.add(boxChartImage);
			
			histogramChartImage = new ImagePanel(ImageIO.read(getClass().getResource("Histogram.png")), true);
			histogramChartImage.setBounds(tabBar.getWidth()-105, 15, 50, 40);
			tabBar.add(histogramChartImage);
			
			scatterplotChartImage = new ImagePanel(ImageIO.read(getClass().getResource("Scatterplot.png")), true);
			scatterplotChartImage.setBounds(tabBar.getWidth()-140, 15, 50, 40);
			tabBar.add(scatterplotChartImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
