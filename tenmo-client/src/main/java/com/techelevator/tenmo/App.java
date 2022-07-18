package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private final AccountService accountService = new AccountService();
    private final UserService userService = new UserService();
    private final TransferService transferService = new TransferService();

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
        else{
            transferService.setCurrentUser(currentUser);
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                System.exit(1);
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        System.out.println("Your current account balance is: " + accountService.viewCurrentBalance(currentUser));

    }

    private void viewTransferHistory() {
        consoleService.printTransferHeader();
        Transfer[] transfers = transferService.viewTransfers();
        for (Transfer transfer : transfers) {
            if (transfer.getSenderUserId() == currentUser.getUser().getId()) {
                System.out.println(transfer.getTransferId() + "      " + " To: " + transfer.getReceiverUsername() + "      $" + transfer.getTransferAmount());
            } else if (transfer.getReceiverUserId() == currentUser.getUser().getId()) {
                System.out.println(transfer.getTransferId() + "     " + "From: " + transfer.getSenderUsername() + "      $" + transfer.getTransferAmount());
            }
        }
        System.out.println();
        System.out.println();
        int transactionPicked;
        transactionPicked = consoleService.promptForInt("Enter Transfer ID for more details (0 to cancel): ");
        Transfer transfer = transferService.transferDetails(transactionPicked);
        if (transactionPicked == 0 ) {
            mainMenu();
        }
        if(transfer.getTransferAmount() != null) {
            System.out.println("--------------------------------------------");
            System.out.println("            Transfer Details            ");
            System.out.println("--------------------------------------------");
            System.out.println("    Id: " + transfer.getTransferId());
            System.out.println("  From: " + transfer.getSenderUsername());
            System.out.println("    To: " + transfer.getReceiverUsername());
            if (transfer.getTransferTypeId() == 1) {
                System.out.println("  Type: Request");
            } else if (transfer.getTransferTypeId() == 2) {
                System.out.println("  Type: Send");
            } else {
                System.out.println("An error occurred");
            }
            if (transfer.getTransferStatusId() == 1) {
                System.out.println("Status: Pending");
            } else if (transfer.getTransferStatusId() == 2) {
                System.out.println("Status: Approved");
            } else if (transfer.getTransferStatusId() == 3) {
                System.out.println("Status: Rejected");
            } else {
                System.out.println("An error occurred");
            }
            System.out.println("Amount: $" + transfer.getTransferAmount());
        } else {
            System.out.println("Please enter a valid transfer ID");
            consoleService.pause();
            viewTransferHistory();
        }
    }

        private void viewPendingRequests() {
            // TODO Auto-generated method stub

        }

        private void sendBucks() {
            consoleService.printAccountHeader();
            User[] users = userService.findAllExceptSelf(currentUser);
            for (User user : users) {
                System.out.println(user.getId() + "     " + user.getUsername());
            }
            System.out.println("--------------------");

            int receiverId;
            boolean isIdFound = false;
            String receiverUsername = " ";
            do {
                receiverId = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel): ");
                for (User user : users) {
                    if (receiverId == user.getId()) {
                        receiverUsername = user.getUsername();
                        isIdFound = true;
                        break;
                    }
                }
                if (receiverId == 0) {
                    mainMenu();
                }
                if (receiverId == currentUser.getUser().getId()) {
                    System.out.println("You cannot transfer money to yourself. Please enter another user ID.");
                } else if (!isIdFound) {
                    System.out.println("That user ID was not found in the system. Please enter a valid user ID.");
                }
            } while (receiverId == currentUser.getUser().getId() || !isIdFound);

            BigDecimal transferAmount;
            int transferGreaterThanZero;
            int transferLessThanCurrentBalance;
            do {
                transferAmount = consoleService.promptForBigDecimal("Enter amount: ");
                transferGreaterThanZero = transferAmount.compareTo(BigDecimal.ZERO);
                BigDecimal value = BigDecimal.valueOf(accountService.viewCurrentBalance(currentUser));
                transferLessThanCurrentBalance = transferAmount.compareTo(value);
                if (transferGreaterThanZero <= 0) {
                    System.out.println("Please enter a valid amount, you must transfer at least 1 penny.");
                }
                if (transferLessThanCurrentBalance > 0) {
                    System.out.println("You cannot transfer more money than you have in your account. Please enter a valid amount.");
                }
            } while (transferGreaterThanZero <= 0 || transferLessThanCurrentBalance > 0);

            if (transferService.transferMoney(receiverId, transferAmount)) {
                System.out.println("Transfer of " + transferAmount + " to " +  receiverUsername + " was successful!");
            } else {
                System.out.println("Sorry, the transfer did not go through. Make sure you entered a valid user ID and transfer amount.");
            }
        }

        private void requestBucks () {
            // TODO Auto-generated method stub

        }

    }
