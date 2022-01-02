package atmMVC.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A staff member at the Bank called Bank Advisor.
 */
public class BankAdvisor extends BankPerson {
    private ObservableList<Appointment> appointments;

    /**
     * Constructs a bank person with a given username and password.
     * @param username the username for this customer
     * @param password the password for this customer
     * @param sinNumber the SIN number for this customer
     */
    public BankAdvisor(String username, String password, int sinNumber) {
        super(username, password, sinNumber);
        this.role = "Advisor";
        appointments = FXCollections.observableArrayList();
        readAppointments();
    }

    /**
     * Constructs a customer with a given username and password. This constructor is used specifically when files
     * are being read.
     * @param  employeeNumber the customer identifier
     * @param username the username for this customer
     * @param password the password for this customer
     * @param sinNumber the SIN number for this customer
     */
    public BankAdvisor(int employeeNumber, String username, String password, int sinNumber) {
        super(employeeNumber, username, password, sinNumber);
        this.role = "Advisor";
        appointments = FXCollections.observableArrayList();
        readAppointments();
    }

    /**
     * Makes an appointment for a customer regarding a particular subject
     * @param customerNumber the customer number of the customer making the appointment
     * @param subject the subject that is discussed during the appointment
     */
    public Appointment createAppointment(int customerNumber, String subject) {
        Appointment appointment = new Appointment(this.employeeNumber, customerNumber, LocalDateTime.now(), subject);
        appointments.add(appointment);
        addAppointmentToFile(appointment);
        return appointment;
    }

    private void addAppointmentToFile(Appointment appointment) {
        File appFile = new File(Config.appointmentFile);
        boolean hasAccess = true;
        if (!appFile.exists()) {
            try {
                hasAccess = appFile.createNewFile();
            }
            catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        if (appFile.exists() && appFile.isFile() && hasAccess) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(appFile, true))) {
                String line = appointment.getBankAdvisorNumber() + "\t" +
                        appointment.getCustomerNumber() + "\t" +
                        appointment.getRequestedOn() + "\t" +
                        appointment.getSubject() + "\n";
                bw.write(line);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void readAppointments() {
        File appFile = new File(Config.appointmentFile);
        if (appFile.exists() && appFile.isFile()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(appFile))) {
                Scanner in = new Scanner(reader);
                while (in.hasNext()) {
                    int advisor = in.nextInt();
                    int customerNumber = in.nextInt();
                    String strRequestedOn = in.next();
                    LocalDateTime requestedOn = LocalDateTime.parse(strRequestedOn);
                    String subject = in.nextLine().replaceFirst("\t", "");
                    Appointment a;
                    if (this.employeeNumber == advisor) {
                        a = new Appointment(advisor, customerNumber, requestedOn, subject);
                        appointments.add(a);
                    }
                }
                in.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public ObservableList<Appointment> getAppointments() {return appointments;}
}
