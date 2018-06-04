package com.view;

import com.controller.Controller;
import com.Main;
import com.dopTask.EulerCycle;
import com.dopTask.FindHamiltonCicle;
import com.model.*;
import com.model.Graph;
import com.model.TypeOperation;
import com.model.Vertex;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainWindow {
    private Display display;
    private Shell shell;
    private TabFolder tabFolder;
    private Color defaultColor;
    private Menu arcMenu;
    private Menu vertexMenu;
    private Map<TabItem, Graph> mapTabItem = new HashMap<>();
    private Canvas canvas;
    private TypeOperation typeOperation;
    private Graph graph;
    private Vertex ingoing;
    private Vertex outgoing;
    private Arc arcCreate;
    private boolean hasSelectVertex = false;
    private Vertex start;
    public BaseOfVertex base;
    public int id=0;

    public MainWindow() {
    	this.base = new BaseOfVertex();
        display = new Display();
        shell = new Shell(display);
        //defaultColor = new Color(null, 171, 245, 181);
        shell.setModified(false);
        shell.setSize(1400, 900);
        //shell.setBackground(defaultColor);
        shell.setText("GraphEditor");
        initLayout();
        initMenuBar();
        initToolBar();
        vertexMenuCanvas();
        arcMenuCanvas();
        tabFolder = new TabFolder(shell, SWT.BORDER);
        GridData gridDataTab = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridDataTab.grabExcessHorizontalSpace = true;
        tabFolder.setLayoutData(gridDataTab);
        initMenuTab();
        tabFolder.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                if (mapTabItem.size() != 0)
                    changeTab(tabFolder.getSelection()[0]);
            }
        });
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    public void addTab(String name) {
        TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
        initCanvas();
        tabItem.setControl(canvas);
        Graph graph = new Graph(name, canvas, base);
        this.graph = graph;
        mapTabItem.put(tabItem, graph);
        tabItem.setText(graph.getName());
        typeOperation = TypeOperation.CLICK;
        hasSelectVertex = false;
        ingoing = null;
        outgoing = null;
        arcCreate = null;
        tabFolder.setSelection(tabItem);

    }

    public void addTab(Graph graph) {
        TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
        tabItem.setControl(canvas);
        this.graph = graph;
        mapTabItem.put(tabItem, graph);
        tabItem.setText(graph.getName());
        typeOperation = TypeOperation.CLICK;
        hasSelectVertex = false;
        ingoing = null;
        outgoing = null;
        arcCreate = null;
        tabFolder.setSelection(tabItem);
    }

    private void changeTab(TabItem tabItem) {
        if (tabItem != null) {
            graph = mapTabItem.get(tabItem);
            typeOperation = TypeOperation.CLICK;
            hasSelectVertex = false;
            ingoing = null;
            outgoing = null;
            arcCreate = null;
            canvas = graph.getCanvas();
            start = null;
        }
    }

    private void initLayout() {
        GridLayout gridLayoutShell = new GridLayout();
        gridLayoutShell.numColumns = 1;
        gridLayoutShell.verticalSpacing = 8;
        gridLayoutShell.marginLeft = 5;
        gridLayoutShell.marginRight = 5;
        gridLayoutShell.marginTop = 5;
        gridLayoutShell.marginBottom = 5;
        shell.setLayout(gridLayoutShell);
    }

    private void initMenuBar() {
        Menu menuBar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menuBar);

        MenuItem fileMenu = new MenuItem(menuBar, SWT.CASCADE);
        fileMenu.setText("Файл");

        Menu fileMenuDrop = new Menu(shell, SWT.DROP_DOWN);
        fileMenu.setMenu(fileMenuDrop);

        MenuItem menuItemAdd = new MenuItem(fileMenuDrop, SWT.PUSH);
        menuItemAdd.setText("Создать");
        menuItemAdd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                Rectangle rectangle = shell.getBounds();
                new GraphWindow(display, MainWindow.this, (rectangle.x + rectangle.width) / 2, (rectangle.y + rectangle.height) / 2);
            }
        });

        MenuItem menuItemOpen = new MenuItem(fileMenuDrop, SWT.PUSH);
        menuItemOpen.setText("Открыть");
        menuItemOpen.setAccelerator(SWT.CTRL + 'O');
        menuItemOpen.addSelectionListener(new SelectionAdapter() {
            MessageBox messageBox = new MessageBox(shell);

            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                File file;
                try {
                    FileDialog fileDialog = openFileDialog("Открыть");
                    file = new File(fileDialog.open());
                } catch (NullPointerException ex) {
                    return;
                }
                Canvas canvas = MainWindow.this.canvas;
                initCanvas();
                Graph graph = Controller.getInstance().open(file, MainWindow.this.canvas, base);
                if (graph == null) {
                    messageBox.setMessage("Ошибка!");
                    messageBox.open();
                    MainWindow.this.canvas = canvas;
                } else {
                    addTab(graph);
                    graph.getCanvas().redraw();
                }
            }
        });

        MenuItem saveItem = new MenuItem(fileMenuDrop, SWT.PUSH);
        saveItem.setText("Сохранить");
        saveItem.setAccelerator(SWT.CTRL + 'S');

        saveItem.addSelectionListener(new SelectionAdapter() {
            MessageBox messageBox = new MessageBox(shell);

            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                File file;
                try {
                    FileDialog fileDialog = openFileDialog("Сохранить");
                    fileDialog.setFileName(graph.getName());
                    file = new File(fileDialog.open());
                } catch (NullPointerException ex) {
                    return;
                }
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    messageBox.setMessage("Ошибка!");
                    messageBox.open();
                    return;
                }
                if (!Controller.getInstance().save(file, graph)) {
                    messageBox.setMessage("Ошибка!");
                    messageBox.open();
                } else {
                    tabFolder.getItem(tabFolder.getSelectionIndex()).setText(file.getName().substring(0, file.getName().indexOf('.')));
                    messageBox.setMessage("Файл сохранен!");
                    messageBox.open();
                }
            }
        });

        MenuItem exitMenu = new MenuItem(fileMenuDrop, SWT.PUSH);
        exitMenu.setText("Выход");
        exitMenu.addListener(SWT.Selection, event -> {
            shell.close();
            display.dispose();
        });

        MenuItem menuItemInfo = new MenuItem(menuBar, SWT.PUSH);
        menuItemInfo.setText("Информация");

        menuItemInfo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                if (graph != null) {
                    MessageBox messageBox = new MessageBox(shell, SWT.OK);
                    messageBox.setText("Информация о графе");
                    String euler;
                    EulerCycle eulerCycle = new EulerCycle(graph);
                    boolean isEuler = eulerCycle.isEuler();
                    if (isEuler){
                        euler = "Это эйлеров граф";
                    }
                    else {
                        euler = "Это не эйлеров граф";
                    }
                    messageBox.setMessage("Граф " + graph.getName() + '\n' + "Ребер " + graph.getArcs().size() + '\n' + "Вершин " + graph.getVertices().size() + '\n' + euler );
                    messageBox.open();
                }
            }
        });

        MenuItem menuDopTask = new MenuItem(menuBar, SWT.CASCADE);
        menuDopTask.setText("Дополнительно");

        Menu menuDopTaskDrop = new Menu(shell, SWT.DROP_DOWN);
        menuDopTask.setMenu(menuDopTaskDrop);

        MenuItem isEuler = new MenuItem(menuDopTaskDrop, SWT.PUSH);
        isEuler.setText("Гамильтонов цикл");

        isEuler.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                FindHamiltonCicle hamiltonCycle = new FindHamiltonCicle(graph);
            }
        });

        MenuItem menuItemRadius = new MenuItem(menuDopTaskDrop, SWT.PUSH);
        menuItemRadius.setText("Радиус");

        menuItemRadius.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                int rad=  graph.findRadius();
                System.out.println("radius");
                MessageBox bb = new MessageBox(shell);
                bb.setMessage("Радиус графа "+rad);
                bb.open();
            }
        });

        MenuItem menuItemDiametr = new MenuItem(menuDopTaskDrop, SWT.PUSH);
        menuItemDiametr.setText("Диаметр");

        menuItemDiametr.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                int rad=  graph.findDiameter();
                MessageBox bb = new MessageBox(shell);
                bb.setMessage("Диаметр графа "+rad);
                bb.open();
            }
        });

        MenuItem menuItemCenter = new MenuItem(menuDopTaskDrop, SWT.PUSH);
        menuItemCenter.setText("Центр");

        menuItemCenter.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                int index = graph.findCentre();
                System.out.println("ee");
                graph.findVertexForId(index);
            }
        });
    }
    
    

    private void initToolBar() {
        ToolBar toolBar = new ToolBar(shell, SWT.BORDER);

        ToolItem toolItemAddVertex = new ToolItem(toolBar, SWT.PUSH);
        toolItemAddVertex.setText("Вершина");
    	 toolItemAddVertex.setToolTipText("Добавление вершины");
   
        toolItemAddVertex.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                typeOperation = TypeOperation.ADD_VERTEX;
            }
        });

        ToolItem separator = new ToolItem(toolBar, SWT.SEPARATOR);

        ToolItem toolItemClick = new ToolItem(toolBar, SWT.PUSH);
        toolItemClick.setText("Изменение");
        toolItemClick.setToolTipText("Изменение");
        toolItemClick.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                typeOperation = TypeOperation.CLICK;
            }
        });

        ToolItem separator1 = new ToolItem(toolBar, SWT.SEPARATOR);

        ToolItem toolItemArc = new ToolItem(toolBar, SWT.PUSH);
        toolItemArc.setText("Ребро");
        toolItemArc.setToolTipText("Добавление ребра");
        toolItemArc.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                typeOperation = TypeOperation.ADD_ARC;
            }
        });

       toolBar.pack();
    }

    private void initCanvas() {
        canvas = new Canvas(tabFolder, SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED);
        GridData gridDataCanvas = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridDataCanvas.grabExcessHorizontalSpace = true;
        canvas.setLayoutData(gridDataCanvas);
        //canvas.setBackground(new Color(null, 210, 230, 210));
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDoubleClick(MouseEvent mouseEvent) {
                if (typeOperation == TypeOperation.ADD_VERTEX && mouseEvent.button == 1) {
                    graph.addVertex(mouseEvent.x, mouseEvent.y, id, base);
                    System.out.println(id);;
                    id=id+1;
                }
                
            }

            @Override
            public void mouseDown(MouseEvent mouseEvent) {
                if (start != null) {
                    start.deselect();
                    for (Vertex vertex : graph.getVertices())
                        vertex.setDistance(-1);
                    start = null;
                }
                if (mouseEvent.button == 3) {
                    graph.select(graph.findVertex(mouseEvent.x, mouseEvent.y));
                    if (graph.getSelectVertex() == null) {
                        graph.select(graph.findArc(mouseEvent.x, mouseEvent.y));
                        if (graph.getSelectArc() != null)
                            canvas.setMenu(arcMenu);
                    } else {
                        canvas.setMenu(vertexMenu);
                    }
                } else {
                    if (typeOperation == TypeOperation.CLICK) {
                        graph.select(graph.findVertex(mouseEvent.x, mouseEvent.y));
                        if (graph.getSelectVertex() == null)
                            graph.select(graph.findArc(mouseEvent.x, mouseEvent.y));
                        else hasSelectVertex = true;
                    } else {
                        if (typeOperation == TypeOperation.ADD_ARC) {
                            outgoing = graph.findVertex(mouseEvent.x, mouseEvent.y);
                            if (outgoing != null)
                                arcCreate = graph.addArc(outgoing, outgoing.getX(), outgoing.getY());
                        } else {
                            if (typeOperation == TypeOperation.ADD_START_VERTEX) {
                                start = graph.findVertex(mouseEvent.x, mouseEvent.y);
                                if (start != null) start.setDefaultColor(new Color(null, 255, 10, 10));
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseUp(MouseEvent mouseEvent) {
                canvas.setMenu(null);
                if (typeOperation == TypeOperation.CLICK) {
                    hasSelectVertex = false;
                } else {
                    if (typeOperation == TypeOperation.ADD_ARC) {
                        if (arcCreate != null) {
                            ingoing = graph.findVertex(mouseEvent.x, mouseEvent.y);
                            if (ingoing != null) {
                                arcCreate.setIngoing(ingoing, base);
                                arcCreate = null;
                            } else {
                                graph.delete(arcCreate);
                                arcCreate = null;
                            }
                        }
                    }
                }
            }
        });
        canvas.addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent mouseEvent) {
                if (typeOperation == TypeOperation.CLICK) {
                    Rectangle rect = canvas.getBounds();
                    if (hasSelectVertex) {
                        if (rect.y - 25 <= mouseEvent.y && rect.x <= mouseEvent.x && rect.x + rect.width - 15 >= mouseEvent.x && rect.y + rect.height - 50 >= mouseEvent.y)
                            graph.getSelectVertex().move(mouseEvent.x, mouseEvent.y);
                    }
                } else {
                    if (typeOperation == TypeOperation.ADD_ARC) {
                        if (arcCreate != null)
                            arcCreate.setXY(mouseEvent.x, mouseEvent.y);
                    }
                }
            }
        });
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.keyCode == 127) {
                    graph.deleteSelected();
                    return;
                }
                if (keyEvent.keyCode == 105) {
                    Rectangle rectangle = canvas.getBounds();
                    if (graph.getSelectVertex() != null)
                        new NameVertexWindow(display, graph.getSelectVertex(), (rectangle.x + rectangle.width) / 2, (rectangle.y + rectangle.height) / 2);
                    if (graph.getSelectArc() != null)
                        new WeightArcWindow(display, graph.getSelectArc(), (rectangle.x + rectangle.width) / 2, (rectangle.y + rectangle.height) / 2, base);
                    return;
                }
                if (keyEvent.keyCode == 49) {
                    typeOperation = TypeOperation.ADD_VERTEX;
                    return;
                }
                if (keyEvent.keyCode == 50) {
                    typeOperation = TypeOperation.CLICK;
                    return;
                }
                if (keyEvent.keyCode == 51) {
                    typeOperation = TypeOperation.ADD_ARC;
                    return;
                }
                if (keyEvent.keyCode == 115) {
                    if (graph.getSelectArc() != null)
                        graph.getSelectArc().changeOrientation();
                    return;
                }
                if (keyEvent.keyCode == 99) {
                    if (graph.getSelectArc() != null)
                        graph.getSelectArc().setOriented(!graph.getSelectArc().isOriented());
                }
            }
        });
    }

    private void vertexMenuCanvas() {
        Menu menu = new Menu(shell, SWT.POP_UP);

        MenuItem setTextItem = new MenuItem(menu, SWT.PUSH);
        setTextItem.setText("Set text");
        
        MenuItem setColorYellowItem = new MenuItem(menu, SWT.PUSH);
        setColorYellowItem.setText("Set color yellow");  

        setColorYellowItem.addListener(SWT.Selection, event -> {
            graph.getSelectVertex().chaneColorYellow();
        });
        
        MenuItem setColorBlueItem = new MenuItem(menu, SWT.PUSH);
        setColorBlueItem.setText("Set color blue");
        setColorBlueItem.addListener(SWT.Selection, event -> {
        	 graph.getSelectVertex().chaneColorBlue();
        });
        
        MenuItem setColorRedItem = new MenuItem(menu, SWT.PUSH);
        setColorRedItem.setText("Set color red");
        setColorRedItem.addListener(SWT.Selection, event -> {
        	graph.getSelectVertex().chaneColorRed();
        });

        setTextItem.addListener(SWT.Selection, event -> {
            Rectangle rectangle = shell.getBounds();
            new NameVertexWindow(display, graph.getSelectVertex(), (rectangle.x + rectangle.width) / 2, (rectangle.y + rectangle.height) / 2);
        });

        MenuItem delItem = new MenuItem(menu, SWT.PUSH);
        delItem.setText("Delete");

        delItem.addListener(SWT.Selection, event -> {
            graph.deleteSelected();
        });

        vertexMenu = menu;
    }

    private void arcMenuCanvas() {
        Menu menu = new Menu(shell, SWT.POP_UP);

        MenuItem setTextItem = new MenuItem(menu, SWT.PUSH);
        setTextItem.setText("Set weight");

        setTextItem.addListener(SWT.Selection, event -> {
            Rectangle rectangle = shell.getBounds();
            new WeightArcWindow(display, graph.getSelectArc(), (rectangle.x + rectangle.width) / 2, (rectangle.y + rectangle.height) / 2, base);
        });
        
        MenuItem setColorYellowItem = new MenuItem(menu, SWT.PUSH);
        setColorYellowItem.setText("Set color yellow");  

        setColorYellowItem.addListener(SWT.Selection, event -> {
            graph.getSelectArc().chaneColorYellow();
        });
        
        MenuItem setColorBlueItem = new MenuItem(menu, SWT.PUSH);
        setColorBlueItem.setText("Set color blue");
        setColorBlueItem.addListener(SWT.Selection, event -> {
        	 graph.getSelectArc().chaneColorBlue();
        });
        
        MenuItem setColorRedItem = new MenuItem(menu, SWT.PUSH);
        setColorRedItem.setText("Set color red");
        setColorRedItem.addListener(SWT.Selection, event -> {
        	graph.getSelectArc().chaneColorRed();
        });
        
       // MenuItem setTextItem = new MenuItem(setColorItem, SWT.PUSH);
       // setTextItem.setText("Set weight");



        MenuItem changeItem = new MenuItem(menu, SWT.PUSH);
        changeItem.setText("Change orientation");

        changeItem.addListener(SWT.Selection, event -> {
            graph.getSelectArc().changeOrientation();
        });

        MenuItem deleteOrientationItem = new MenuItem(menu, SWT.PUSH);
        deleteOrientationItem.setText("Set unoriented");

        deleteOrientationItem.addListener(SWT.Selection, event -> {
            graph.getSelectArc().setOriented(false);
        });

        MenuItem setOrientedItem = new MenuItem(menu, SWT.PUSH);
        setOrientedItem.setText("Set oriented");

        setOrientedItem.addListener(SWT.Selection, event -> {
            graph.getSelectArc().setOriented(true);
        });

        MenuItem delItem = new MenuItem(menu, SWT.PUSH);
        delItem.setText("Delete");

        delItem.addListener(SWT.Selection, event -> {
            graph.deleteSelected();
        });

        arcMenu = menu;
    }

    private void initMenuTab() {
        Menu menu = new Menu(shell, SWT.POP_UP);

        MenuItem setTextItem = new MenuItem(menu, SWT.PUSH);
        setTextItem.setText("Add");

        setTextItem.addListener(SWT.Selection, event -> {
            Rectangle rectangle = shell.getBounds();
            new GraphWindow(display, MainWindow.this, (rectangle.x + rectangle.width) / 2, (rectangle.y + rectangle.height) / 2);
        });

        MenuItem delItem = new MenuItem(menu, SWT.PUSH);
        delItem.setText("Delete");

        delItem.addListener(SWT.Selection, event -> {
            TabItem tabItem = tabFolder.getSelection()[0];
            mapTabItem.remove(tabItem);
            canvas = null;
            graph = null;
            typeOperation = TypeOperation.CLICK;
            hasSelectVertex = false;
            ingoing = null;
            outgoing = null;
            arcCreate = null;
            start = null;
            tabItem.dispose();
            if (mapTabItem.size() > 0)
                changeTab(tabFolder.getItem(0));
        });
        tabFolder.setMenu(menu);

    }

    private FileDialog openFileDialog(String type) {
        FileDialog fileDialog = new FileDialog(shell, "Save".equals(type) ? SWT.SAVE : SWT.OPEN);
        fileDialog.setText(type);
        fileDialog.setFilterPath("C:/");
        String[] filterExst = new String[1];
        filterExst[0] = "*.xml";
        fileDialog.setFilterExtensions(filterExst);
        return fileDialog;
    }

}
