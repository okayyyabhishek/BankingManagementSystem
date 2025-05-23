package ui;

import dao.AccountDAO;
import model.Account;

import javax.swing.*;
import java.awt.*;

public class BankingUI extends JFrame {

    private Account loggedInAccount;

    public BankingUI() {
        setTitle("Banking Management System");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        showHomeScreen();
    }

    private void showHomeScreen() {
        getContentPane().removeAll();
        setLayout(new GridBagLayout());

        JButton createAccountBtn = new JButton("Create Account");
        JButton loginBtn = new JButton("Login");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(createAccountBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(loginBtn, gbc);

        createAccountBtn.addActionListener(e -> showCreateAccountScreen());

        loginBtn.addActionListener(e -> showLoginScreen());

        revalidate();
        repaint();
    }

    private void showCreateAccountScreen() {
        getContentPane().removeAll();
        setLayout(new GridBagLayout());

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);

        JLabel balanceLabel = new JLabel("Initial Deposit:");
        JTextField balanceField = new JTextField(15);

        JButton submitBtn = new JButton("Submit");
        JButton backBtn = new JButton("Back");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(emailLabel, gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(balanceLabel, gbc);
        gbc.gridx = 1;
        add(balanceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(backBtn, gbc);
        gbc.gridx = 1;
        add(submitBtn, gbc);

        backBtn.addActionListener(e -> showHomeScreen());

        submitBtn.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String balanceStr = balanceField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || balanceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double balance;
            try {
                balance = Double.parseDouble(balanceStr);
                if (balance < 0) {
                    JOptionPane.showMessageDialog(this, "Balance cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid balance amount", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Account account = new Account();
            account.setName(name);
            account.setEmail(email);
            account.setPassword(password);
            account.setBalance(balance);

            AccountDAO dao = new AccountDAO();
            if (dao.addAccount(account)) {
                JOptionPane.showMessageDialog(this, "Account created successfully!");
                showHomeScreen();
            } else {
                JOptionPane.showMessageDialog(this, "Error creating account. Email might already be in use.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        revalidate();
        repaint();
    }

    private void showLoginScreen() {
        getContentPane().removeAll();
        setLayout(new GridBagLayout());

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);

        JButton loginBtn = new JButton("Login");
        JButton backBtn = new JButton("Back");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(emailLabel, gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(backBtn, gbc);
        gbc.gridx = 1;
        add(loginBtn, gbc);

        backBtn.addActionListener(e -> showHomeScreen());

        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter email and password", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            AccountDAO dao = new AccountDAO();
            Account account = dao.login(email, password);

            if (account != null) {
                loggedInAccount = account;
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        revalidate();
        repaint();
    }

    private void showDashboard() {
        getContentPane().removeAll();
        setLayout(new GridBagLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInAccount.getName());
        JLabel balanceLabel = new JLabel("Balance: $" + String.format("%.2f", loggedInAccount.getBalance()));

        JButton depositBtn = new JButton("Deposit");
        JButton logoutBtn = new JButton("Logout");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        gbc.gridy = 1;
        add(balanceLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(depositBtn, gbc);

        gbc.gridx = 1;
        add(logoutBtn, gbc);

        depositBtn.addActionListener(e -> {
            String amountStr = JOptionPane.showInputDialog(this, "Enter deposit amount:");
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double newBalance = loggedInAccount.getBalance() + amount;
                AccountDAO dao = new AccountDAO();
                if (dao.updateBalance(loggedInAccount.getId(), newBalance)) {
                    loggedInAccount.setBalance(newBalance);
                    showDashboard();  // Refresh dashboard
                    JOptionPane.showMessageDialog(this, "Deposit successful");
                } else {
                    JOptionPane.showMessageDialog(this, "Deposit failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        logoutBtn.addActionListener(e -> {
            loggedInAccount = null;
            showHomeScreen();
        });

        revalidate();
        repaint();
    }
}
