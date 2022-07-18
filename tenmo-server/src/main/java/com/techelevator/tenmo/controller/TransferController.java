package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.apache.catalina.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.PUT)
    public boolean transferMoney(@Valid @RequestBody Transfer transfer, Principal user) {
        //Principal user is current user, Transfer object provides both transferAmount and receiverId
        //Transfer is new object(model) that holds all needed info
        long senderId = userDao.findIdByUsername(user.getName());
        return transferDao.transferMoney(senderId, transfer);
    }

    @RequestMapping(path = "/transfer/history", method = RequestMethod.GET)
    public List<Transfer> viewTransfers(@Valid Principal user) {
        long userId = userDao.findIdByUsername(user.getName());
        return transferDao.viewTransfers(userId);
    }

    @RequestMapping(path = "/transfer/{transferId}/details", method = RequestMethod.GET)
    // need to pass an int into transferDetails
    public Transfer transferDetails(@Valid Principal user, @PathVariable int transferId) {
        //long userId = userDao.findIdByUsername(user.getName());
        return transferDao.transferDetails(transferId);
    }


}
