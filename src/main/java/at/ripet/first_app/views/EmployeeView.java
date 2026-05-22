package at.ripet.first_app.views;

import at.ripet.first_app.entity.Branch;
import at.ripet.first_app.entity.Employee;
import at.ripet.first_app.service.BranchService;
import at.ripet.first_app.service.EmployeeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;

@Route("employees")
public class EmployeeView extends VerticalLayout {

    private final EmployeeService employeeService;
    private final BranchService branchService;
    private final Grid<Employee> grid = new Grid<>(Employee.class, false);
    private final Editor<Employee> editor;
    private final Binder<Employee> binder = new Binder<>(Employee.class);

    public EmployeeView(EmployeeService employeeService, BranchService branchService) {
        this.employeeService = employeeService;
        this.branchService = branchService;
        this.editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        editor.addSaveListener(event -> {
            employeeService.save(event.getItem());
            refreshGrid();
        });

        setSizeFull();
        configureGrid();

        Button addButton = new Button("Neuer Mitarbeiter", VaadinIcon.PLUS.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> addNewEmployee());

        add(new HorizontalLayout(addButton), grid);
    }

    private void configureGrid() {
        grid.setSizeFull();

        TextField firstNameField = new TextField();
        firstNameField.setWidthFull();
        binder.bind(firstNameField, Employee::getFirstName, Employee::setFirstName);
        grid.addColumn(Employee::getFirstName).setHeader("Vorname").setEditorComponent(firstNameField).setResizable(true);

        TextField lastNameField = new TextField();
        lastNameField.setWidthFull();
        binder.bind(lastNameField, Employee::getLastName, Employee::setLastName);
        grid.addColumn(Employee::getLastName).setHeader("Nachname").setEditorComponent(lastNameField).setResizable(true);

        TextField phoneField = new TextField();
        phoneField.setWidthFull();
        binder.bind(phoneField, Employee::getPhone, Employee::setPhone);
        grid.addColumn(Employee::getPhone).setHeader("Telefon").setEditorComponent(phoneField).setResizable(true);

        DatePicker hireDatePicker = new DatePicker();
        hireDatePicker.setWidthFull();
        binder.bind(hireDatePicker, Employee::getHireDate, Employee::setHireDate);
        grid.addColumn(Employee::getHireDate).setHeader("Eintrittsdatum").setEditorComponent(hireDatePicker).setResizable(true);

        ComboBox<Branch> branchComboBox = new ComboBox<>();
        branchComboBox.setWidthFull();
        branchComboBox.setItems(branchService.findAll());
        branchComboBox.setItemLabelGenerator(b -> b.getBranchName() != null ? b.getBranchName() : "-");
        binder.bind(branchComboBox, Employee::getCurrentBranch, Employee::setCurrentBranch);
        grid.addColumn(new TextRenderer<>(emp ->
                emp.getCurrentBranch() != null ? emp.getCurrentBranch().getBranchName() : "-"))
                .setHeader("Filiale").setEditorComponent(branchComboBox).setResizable(true);

        grid.addComponentColumn(employee -> {
            Button editBtn = new Button(VaadinIcon.EDIT.create());
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            editBtn.addClickListener(e -> { if (editor.isOpen()) editor.cancel(); grid.getEditor().editItem(employee); });

            Button deleteBtn = new Button(VaadinIcon.TRASH.create());
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
            deleteBtn.addClickListener(e -> { employeeService.delete(employee); refreshGrid(); });

            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Aktionen").setWidth("120px").setFlexGrow(0);

        Button saveBtn = new Button(VaadinIcon.CHECK.create());
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        saveBtn.addClickListener(e -> editor.save());

        Button cancelBtn = new Button(VaadinIcon.CLOSE.create());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        cancelBtn.addClickListener(e -> editor.cancel());

        grid.addComponentColumn(employee -> new HorizontalLayout())
                .setEditorComponent(new HorizontalLayout(saveBtn, cancelBtn))
                .setWidth("100px").setFlexGrow(0);

        refreshGrid();
    }

    private void addNewEmployee() {
        if (editor.isOpen()) editor.cancel();
        Employee newEmployee = new Employee();
        employeeService.save(newEmployee);
        refreshGrid();
        grid.getEditor().editItem(newEmployee);
    }

    private void refreshGrid() {
        grid.setItems(employeeService.findAll());
    }
}
