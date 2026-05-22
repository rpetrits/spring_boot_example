package at.ripet.first_app.views;

import at.ripet.first_app.entity.Branch;
import at.ripet.first_app.entity.Employee;
import at.ripet.first_app.service.BranchService;
import at.ripet.first_app.service.EmployeeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;

@Route("branches")
public class BranchView extends VerticalLayout {

    private final BranchService branchService;
    private final EmployeeService employeeService;
    private final Grid<Branch> grid = new Grid<>(Branch.class, false);
    private final Editor<Branch> editor;
    private final Binder<Branch> binder = new Binder<>(Branch.class);

    public BranchView(BranchService branchService, EmployeeService employeeService) {
        this.branchService = branchService;
        this.employeeService = employeeService;
        this.editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        editor.addSaveListener(event -> {
            branchService.save(event.getItem());
            refreshGrid();
        });

        setSizeFull();
        configureGrid();

        Button addButton = new Button("Neue Filiale", VaadinIcon.PLUS.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> addNewBranch());

        add(new HorizontalLayout(addButton), grid);
    }

    private void configureGrid() {
        grid.setSizeFull();

        grid.addColumn(Branch::getId).setHeader("ID").setWidth("80px").setFlexGrow(0).setResizable(true);

        TextField branchNameField = new TextField();
        branchNameField.setWidthFull();
        binder.bind(branchNameField, Branch::getBranchName, Branch::setBranchName);
        grid.addColumn(Branch::getBranchName).setHeader("Filialname").setEditorComponent(branchNameField).setResizable(true);

        TextField addressField = new TextField();
        addressField.setWidthFull();
        binder.bind(addressField, Branch::getAddress, Branch::setAddress);
        grid.addColumn(Branch::getAddress).setHeader("Adresse").setEditorComponent(addressField).setResizable(true);

        ComboBox<Employee> managerComboBox = new ComboBox<>();
        managerComboBox.setWidthFull();
        managerComboBox.setItems(employeeService.findAll());
        managerComboBox.setItemLabelGenerator(e -> e.getFirstName() + " " + e.getLastName());
        binder.bind(managerComboBox, Branch::getManager, Branch::setManager);
        grid.addColumn(new TextRenderer<>(branch ->
                branch.getManager() != null ? branch.getManager().getFirstName() + " " + branch.getManager().getLastName() : "-"))
                .setHeader("Manager").setEditorComponent(managerComboBox).setResizable(true);

        grid.addComponentColumn(branch -> {
            Button editBtn = new Button(VaadinIcon.EDIT.create());
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editBtn.addClickListener(e -> { if (editor.isOpen()) editor.cancel(); editor.editItem(branch); });

            Button deleteBtn = new Button(VaadinIcon.TRASH.create());
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
            deleteBtn.addClickListener(e -> { branchService.delete(branch); refreshGrid(); });

            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Aktionen").setWidth("120px").setFlexGrow(0);

        Button saveBtn = new Button(VaadinIcon.CHECK.create());
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        saveBtn.addClickListener(e -> editor.save());

        Button cancelBtn = new Button(VaadinIcon.CLOSE.create());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        cancelBtn.addClickListener(e -> editor.cancel());

        grid.addComponentColumn(branch -> new HorizontalLayout())
                .setEditorComponent(new HorizontalLayout(saveBtn, cancelBtn))
                .setWidth("100px").setFlexGrow(0);

        refreshGrid();
    }

    private void addNewBranch() {
        if (editor.isOpen()) editor.cancel();
        Branch newBranch = new Branch();
        branchService.save(newBranch);
        refreshGrid();
        editor.editItem(newBranch);
    }

    private void refreshGrid() {
        grid.setItems(branchService.findAll());
    }
}
