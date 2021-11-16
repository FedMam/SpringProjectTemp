package com.fedormamaevv.SpringProjectTemp;

import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.lang.Math;

@RestController
public class ApiController {

    Map<String, User> users = new HashMap<>();

    @GetMapping("users/{username}")
    public User getUsername(@PathVariable String username)
            throws NotFoundException {
        if (users.containsKey(username)) {
            return users.get(username).secureReturn();
        }
        throw new NotFoundException();
    }

    private void verify(String string)
            throws BadRequestException {
        for (char c: string.toCharArray())
            if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c >='A' && c <= 'Z'))
                throw new BadRequestException();
    }

    @GetMapping("admin/{adminUsername}/users/{username}")
    public User getUsernameAsAdmin(@PathVariable String username,
                                   @PathVariable String adminUsername,
                                   @RequestParam String password)
            throws NotFoundException, ForbiddenException {
        if (!adminUsername.startsWith("admin"))
            throw new ForbiddenException();

        if (!users.containsKey(adminUsername))
            throw new ForbiddenException();

        if (users.get(adminUsername).getPassword().compareTo(password) != 0)
            throw new ForbiddenException();

        if (users.containsKey(username)) {
            return users.get(username);
        }

        throw new NotFoundException();
    }

    @GetMapping("users")
    public ArrayList<User> getAllUsers(@RequestParam(required = false, defaultValue = "q") String age,
                                       @RequestParam(name = "sort-order", required = false, defaultValue = "none") String sort_order)  {
        int ageRange;
        boolean byAge = true;
        try {
            ageRange = Integer.parseInt(age);
        }
        catch (Exception e) {
            ageRange = -1;
            byAge = false;
        }
        ArrayList<User> userList = new ArrayList<>(0);
        for (User user: users.values())
        {
            if (!byAge || (Math.abs(user.getAge() - ageRange) <= 5))
                userList.add(user.secureReturn());
        }
        if (sort_order.compareTo("up") == 0 || sort_order.compareTo("down") == 0)
        {
            userList.sort((user, t1) -> {
                int order = 1;
                if (sort_order.compareTo("down") == 0) order = -1;
                if (user.getAge() == t1.getAge()) return 0;
                else if (user.getAge() > t1.getAge()) return order;
                else return -order;
            });
        }
        return userList;
    }

    @GetMapping("admin/{adminUsername}/users")
    public ArrayList<User> getAllUsersAsAdmin(@PathVariable String adminUsername,
                                              @RequestParam String password,
                                              @RequestParam(required = false, defaultValue = "q") String age,
                                              @RequestParam(name = "sort-order", required = false, defaultValue = "none") String sort_order)
            throws ForbiddenException {
        int ageRange;
        boolean byAge = true;
        try {
            ageRange = Integer.parseInt(age);
        }
        catch (Exception e) {
            ageRange = -1;
            byAge = false;
        }
        if (!adminUsername.startsWith("admin"))
            throw new ForbiddenException();

        if (!users.containsKey(adminUsername))
            throw new ForbiddenException();

        ArrayList<User> userList = new ArrayList<>(0);
        for (User user: users.values())
        {
            if (!byAge || (Math.abs(user.getAge() - ageRange) <= 5))
                userList.add(user);
        }
        if (sort_order.compareTo("up") == 0 || sort_order.compareTo("down") == 0)
        {
            userList.sort((user, t1) -> {
                int order = 1;
                if (sort_order.compareTo("down") == 0) order = -1;
                if (user.getAge() == t1.getAge()) return 0;
                else if (user.getAge() > t1.getAge()) return order;
                else return -order;
            });
        }
        return userList;
    }

    @PostMapping("users")
    public void register(@RequestBody User user, @RequestParam String confirmPassword)
            throws ConflictException, BadRequestException {
        if (users.containsKey(user.getUsername()))
            throw new ConflictException();

        if (confirmPassword.compareTo(user.getPassword()) != 0)
            throw new BadRequestException();

        if (user.getUsername().length() == 0 || user.getPassword().length() == 0)
            throw new BadRequestException();

        verify(user.getUsername());
        verify(user.getPassword());

        for (char c: user.getPassword().toCharArray()) {
            if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c >= 'A' && c <= 'Z'))
                throw new BadRequestException();
        }

        users.put(user.getUsername(), user);
    }

    @PutMapping("users/{username}/password")
    public void updatePassword(@PathVariable String username,
                               @RequestParam String oldPassword,
                               @RequestParam String newPassword,
                               @RequestParam String repeatPassword)
            throws ForbiddenException, NotFoundException, BadRequestException
    {
        if (!username.startsWith("update"))
            throw new ForbiddenException();
        String user = username.substring(6);
        if (!users.containsKey(user))
            throw new NotFoundException();
        if (oldPassword.compareTo(users.get(user).getPassword()) != 0)
            throw new ForbiddenException();
        if (newPassword.compareTo(repeatPassword) != 0)
            throw new BadRequestException();
        verify(newPassword);
        users.get(user).setPassword(newPassword);
    }

    @DeleteMapping("admin/{adminUsername}/users/{username}")
    public void deleteUser(@PathVariable String adminUsername,
                           @PathVariable String username,
                           @RequestParam String password)
            throws ForbiddenException, NotFoundException
    {
        if (!adminUsername.startsWith("admin"))
            throw new ForbiddenException();

        if (!users.containsKey(adminUsername))
            throw new ForbiddenException();

        if (users.get(adminUsername).getPassword().compareTo(password) != 0)
            throw new ForbiddenException();

        if (users.containsKey(username))
            users.remove(username);
        else
            throw new NotFoundException();
    }
}