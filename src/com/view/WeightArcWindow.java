package com.view;

import com.model.Arc;
import com.model.BaseOfVertex;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class WeightArcWindow {
    private Display display;
    private Shell shell;
    private Arc arc;
    private BaseOfVertex base;
    private Color defaultColor;

    public WeightArcWindow(Display display, Arc arc, int x, int y,BaseOfVertex base ) {
        this.display = display;
        this.base = base;
        this.shell = new Shell(display);
        GridLayout gridLayout = new GridLayout();
        gridLayout.verticalSpacing = 8;
        gridLayout.numColumns = 2;
        defaultColor = new Color(null, 255, 247, 247);
        shell.setLayout(gridLayout);
        shell.setText("Add weight");
        shell.setLocation(x,y);
        shell.setSize(200, 150);
        shell.setModified(false);
        shell.setBackground(defaultColor);
        this.arc = arc;
        initWindow();
        shell.setFocus();
        shell.open();
    }

    private void initWindow() {
        Label labelSurname = new Label(shell, SWT.NULL);
        labelSurname.setText("Input weight");
        labelSurname.setBackground(defaultColor);

        Text name = new Text(shell, SWT.SINGLE | SWT.BORDER);
        GridData gridDataSurname = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gridDataSurname.horizontalSpan = 5;
        name.setLayoutData(gridDataSurname);

        Button button = new Button(shell, SWT.PUSH);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
        button.setLayoutData(gridData);
        button.setText("Add");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent selectionEvent) {
                if (!name.getText().equals("")) {
                    try {
                        arc.setWeight(Integer.parseInt(name.getText()),  base);
                        shell.close();
                    } catch (NumberFormatException ex){}
                }
            }
        });

    }
}
