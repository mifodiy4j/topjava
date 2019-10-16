package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<Meal> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll {}", userId);
        return service.getAll(userId);
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get id = {} for userId = {}", id, userId);
        return service.get(id, userId);
    }

    public Meal save(Meal meal) {
        log.info("save {}", meal);
        checkNew(meal);
        int userId = SecurityUtil.authUserId();
        meal.setUserId(userId);
        return service.save(meal);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete id = {} for userId = {}", id, userId);
        service.delete(id, userId);
    }
}